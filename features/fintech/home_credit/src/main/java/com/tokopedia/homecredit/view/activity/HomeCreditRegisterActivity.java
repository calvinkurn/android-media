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
import com.tokopedia.homecredit.view.fragment.HomeCreditKTPFragment;
import com.tokopedia.homecredit.view.fragment.HomeCreditSelfieFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeCreditRegisterActivity extends BaseSimpleActivity {

    private final static String SHOW_KTP = "show_ktp";
    public static final String HCI_KTP_IMAGE_PATH = "ktp_image_path";
    private List<String> permissionsToRequest;
    private boolean isPermissionGotDenied;
    protected static final int REQUEST_CAMERA_PERMISSIONS = 932;

    @DeepLink(ApplinkConst.HOME_CREDIT_KTP)
    public static Intent getHomeCreditKTPIntent(Context context, Bundle bundle) {
        Uri.Builder uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon();
        bundle.putBoolean(SHOW_KTP, true);
        return new Intent(context, HomeCreditRegisterActivity.class)
                .setData(uri.build())
                .putExtras(bundle);
    }

    @DeepLink(ApplinkConst.HOME_CREDIT_SELFIE)
    public static Intent getHomeCreditSelfieIntent(Context context, Bundle bundle) {
        Uri.Builder uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon();
        bundle.putBoolean(SHOW_KTP, false);
        return new Intent(context, HomeCreditRegisterActivity.class)
                .setData(uri.build())
                .putExtras(bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionsToRequest != null && grantResults.length == permissionsToRequest.size()) {
            int grantCount = 0;
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    isPermissionGotDenied = true;
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
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    protected Fragment getNewFragment() {
        if (getIntent() != null &&
                getIntent().getBooleanExtra(SHOW_KTP, false)) {

            return HomeCreditKTPFragment.createInstance();
        } else {
            return HomeCreditSelfieFragment.createInstance();
        }
    }
}
