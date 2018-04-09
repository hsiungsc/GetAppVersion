package sample.hsiungsc.com.getappversion;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "getappversion";

    public final class AppVersion
    {
        private String mVersionName = "0.0.0.0";
        private int    mVersionCode = 0;
        private int    mBaseRevisionCode = 0;
        private String[] mRequestedPermissions=null;

        private AppVersion(String packageName)
        {
            Context context  = MainActivity.this;
            PackageManager pm = context.getPackageManager();

            try
            {
                PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
                mVersionName = pi.versionName;
                mVersionCode = pi.versionCode;
                mRequestedPermissions = pi.requestedPermissions;
                mBaseRevisionCode = pi.baseRevisionCode;
            }
            catch (PackageManager.NameNotFoundException e)
            {
                Log.d(TAG, "Android package \"" + packageName + "\" is not found");

                Toast.makeText(
                        MainActivity.this,
                        "Android package \"" + packageName + "\" is not found",
                        Toast.LENGTH_LONG).show();
            }
            catch (Throwable th)  // catch throwable in case of new/missing fields
            {
                Log.d(TAG, "PackageInfo Exception/Error: " + th);
            }
        }

        private String getVersionString()
        {
            return (mVersionName);
        }

        private String getVersion()
        {
            return (Integer.toString(mVersionCode));
        }

        private String getBaseRevision()
        {
            return (Integer.toString(mBaseRevisionCode));
        }

        private String[] getRequestedPermissions() {
            if(mRequestedPermissions == null)
            {
                return null;
            }
            return mRequestedPermissions;
        }

        @Override
        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            sb.append("version=");
            sb.append(mVersionName);
            sb.append(" code=");
            sb.append(mVersionCode);
            sb.append(" baserev=");
            sb.append(mBaseRevisionCode);
            return (sb.toString());
        }
    }

    private void populateUsedPermissionsList(String[] requestedPermissions)
    {
        String[] permissios = {"No permission"};

        if(requestedPermissions != null)
        {
            permissios = requestedPermissions;
        }

        Spinner permissionsSpinner = findViewById(R.id.permissions_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, permissios);

        permissionsSpinner.setAdapter(adapter);
    }

    private void getAppVersion(String pkgName)
    {
        AppVersion appVersion = new AppVersion(pkgName);

        EditText appVersionTextView = findViewById(R.id.editTextVersion);
        appVersionTextView.setText(appVersion.getVersionString());

        EditText appVersionCodeTextView = findViewById(R.id.editTextVersionCode);
        appVersionCodeTextView.setText(appVersion.getVersion());

        EditText appBaseRevisionCodeTextView = findViewById(R.id.editTextBaseRevisionCode);
        appBaseRevisionCodeTextView.setText(appVersion.getBaseRevision());

        populateUsedPermissionsList(appVersion.getRequestedPermissions());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    public void onClickGetAppVersion(View v) {
        String pkgName;
        EditText pkgNameTextView = findViewById(R.id.editTextPkgName);

        pkgName = pkgNameTextView.getText().toString();
        if(pkgName.isEmpty())
        {
            Toast.makeText(
                    MainActivity.this,
                    "Please enter app package name",
                    Toast.LENGTH_LONG).show();
        }
        else
        {
            getAppVersion(pkgName);
        }
    }
}
