package com.tokopedia.homecredit.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.homecredit.R;
import com.tokopedia.homecredit.view.fragment.HomeCreditFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeCreditRegisterActivity extends BaseSimpleActivity {

    private List<String> permissionsToRequest;
    private boolean isPermissionGotDenied;
    protected static final int REQUEST_CAMERA_PERMISSIONS = 932;

    @DeepLink(ApplinkConst.HOMECREDIT)
    public static Intent getHomeCreditRegisterIntent(Context context, Bundle bundle) {
        Uri.Builder uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, HomeCreditRegisterActivity.class)
                .setData(uri.build())
                .putExtras(bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionsToRequest != null && grantResults.length == permissionsToRequest.size()) {
            int grantCount = 0;
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    isPermissionGotDenied = true;
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsToRequest.get(grantCount))) {
                        //Never ask again selected, or device policy prohibits the app from having that permission.
                        //Toast.makeText(getContext(), getString(R.string.permission_enabled_needed), Toast.LENGTH_LONG).show();
                    }
                    break;
                }
                grantCount++;
            }
            if (grantCount == grantResults.length) {
                isPermissionGotDenied = false;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPermissionGotDenied) {
            finish();
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            String[] permissions;
            permissions = new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
            permissionsToRequest = new ArrayList<>();
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(permission);
                }
            }
            if (!permissionsToRequest.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        permissionsToRequest.toArray(new String[permissionsToRequest.size()]), REQUEST_CAMERA_PERMISSIONS);
            } else {
//                initView();
            }
        } else { // under jellybean, no need to check runtime permission
            //initView();
        }
    }

    @SuppressLint("MissingPermission")
    private void initView() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.parent_view, HomeCreditFragment.createInstance(), HomeCreditFragment.class.getSimpleName())
                .commit();

    }

    @SuppressLint("MissingPermission")
    @Override
    protected Fragment getNewFragment() {
        return HomeCreditFragment.createInstance();
    }

}
