package com.tokopedia.homecredit.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.homecredit.view.fragment.HomeCreditKTPFragment;
import com.tokopedia.homecredit.view.fragment.HomeCreditSelfieFragment;

import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.applink.ApplinkConst.HOME_CREDIT_KTP_WITHOUT_TYPE;

public class HomeCreditRegisterActivity extends BaseSimpleActivity {

    private final static String SHOW_KTP = "show_ktp";
    public static final String HCI_KTP_IMAGE_PATH = "ktp_image_path";
    private List<String> permissionsToRequest;
    private boolean isPermissionGotDenied;
    private boolean showKtp = false;
    protected static final int REQUEST_CAMERA_PERMISSIONS = 932;

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
        String[] permissions;
        permissions = new String[]{Manifest.permission.CAMERA};
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

    @SuppressLint("MissingPermission")
    @Override
    protected Fragment getNewFragment() {
        Intent intent = getIntent();
        if (intent!= null) {
            Uri uri = intent.getData();
            showKtp = "true".equals(uri.getQueryParameter(SHOW_KTP));
        }
        if (showKtp) {
            return HomeCreditKTPFragment.createInstance();
        } else {
            return HomeCreditSelfieFragment.createInstance();
        }
    }
}
