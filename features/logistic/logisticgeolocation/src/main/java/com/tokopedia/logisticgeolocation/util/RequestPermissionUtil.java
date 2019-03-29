package com.tokopedia.logisticgeolocation.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.tokopedia.logisticgeolocation.R;

import java.util.List;

import permissions.dispatcher.PermissionRequest;

/**
 * Created by Fajar Ulin Nuha on 29/10/18.
 */
public class RequestPermissionUtil {

    public static void onPermissionDenied(Context context, String permission) {

        switch (permission) {
            case Manifest.permission.ACCESS_FINE_LOCATION:
                Toast.makeText(context, R.string.permission_location_denied, Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(context, R.string.permission_multi_denied, Toast.LENGTH_LONG).show();
                break;
        }
    }

    public static void onShowRationale(Context context, final PermissionRequest request,
                                       String permission) {
        new android.support.v7.app.AlertDialog.Builder(context)
                .setMessage(getNeedPermissionMessage(permission))
                .setPositiveButton(R.string.title_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .show();
    }

    public static int getNeedPermissionMessage(String permission) {
        switch (permission) {
            case Manifest.permission.ACCESS_FINE_LOCATION:
                return R.string.need_permission_location;
            default:
                return R.string.need_permission_multi;
        }
    }

    public static void onNeverAskAgain(Context context, String permission) {
        switch (permission) {
            case Manifest.permission.ACCESS_FINE_LOCATION:
                Toast.makeText(context, R.string.permission_location_neverask, Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(context, R.string.permission_multi_neverask, Toast.LENGTH_LONG).show();
                break;
        }
    }

    public static boolean checkHasPermission(Activity activity, String permission) {
        return ContextCompat.checkSelfPermission(activity, permission)
                == PackageManager.PERMISSION_GRANTED;
    }
}
