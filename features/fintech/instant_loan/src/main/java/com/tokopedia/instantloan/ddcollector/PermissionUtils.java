package com.tokopedia.instantloan.ddcollector;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class PermissionUtils {

    private Context mContext;
    private PermissionResultCallback mCallback;
    static final int PERMISSION_REQUEST_CODE = 99;
    private List<String> mMissingPermissions = new ArrayList<>();

    PermissionUtils(@NonNull Context context, @NonNull PermissionResultCallback callback) {
        if (!(context instanceof Activity)) {
            throw new RuntimeException("'context' parameter must be type of Activity");
        }

        this.mContext = context;
        this.mCallback = callback;
    }

    /**
     * Check the API Level & Permission
     *
     * @param permissions
     * @param requestCode
     */
    void checkPermission(List<String> permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isPermissionsGranted(permissions)) {
                mCallback.permissionGranted(requestCode);
            } else {
                this.mMissingPermissions = getMissingPermission(permissions);
                requestPermissions(mMissingPermissions, requestCode);
            }
        } else {
            mCallback.permissionGranted(requestCode);
        }
    }

    private void requestPermissions(@NonNull List<String> missingPermission, int requestCode) {
        if (!missingPermission.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) mContext, missingPermission.toArray(new String[missingPermission.size()]), requestCode);
        }
    }

    boolean isPermissionsGranted(@NonNull List<String> permissions) {
        if (permissions.isEmpty()) {
            return true;
        }

        for (int i = 0; i < permissions.size(); i++) {
            if (ContextCompat.checkSelfPermission(mContext, permissions.get(i)) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

    private List<String> getMissingPermission(@NonNull List<String> permissions) {
        List<String> missingPermissions = new ArrayList<>();
        if (permissions.isEmpty()) {
            return missingPermissions;
        }

        for (int i = 0; i < permissions.size(); i++) {
            if (ContextCompat.checkSelfPermission(mContext, permissions.get(i)) != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permissions.get(i));
            }

        }
        return missingPermissions;
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    Map<String, Integer> permissionExeuted = new HashMap<>();

                    for (int i = 0; i < permissions.length; i++) {
                        permissionExeuted.put(permissions[i], grantResults[i]);
                    }

                    final List<String> pendingPermissions = new ArrayList<>();

                    for (int i = 0; i < mMissingPermissions.size(); i++) {
                        if (permissionExeuted.get(mMissingPermissions.get(i)) != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, mMissingPermissions.get(i))) {
                                pendingPermissions.add(mMissingPermissions.get(i));
                            }
                        }
                    }

                    if (pendingPermissions.size() > 0) {
                        //TODO @lavekush-t Need add logic for re-traversals for permission either for partial denied case or full denied case
                    } else {
                        mCallback.permissionGranted(requestCode);
                    }
                } else {
                    mCallback.permissionDenied(requestCode);
                }
                break;
        }
    }
}