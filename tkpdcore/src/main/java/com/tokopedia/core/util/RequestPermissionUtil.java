package com.tokopedia.core.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.Toast;

import com.tokopedia.core2.R;

import java.util.List;

import permissions.dispatcher.PermissionRequest;

/**
 * Created by Nisie on 8/5/16.
 * Deprecated. Use from tkpdabstraction instead.
 */
@Deprecated
public class RequestPermissionUtil {

    public static void onPermissionDenied(Context context, List<String> listPermission) {
        String allPermission = "";
        for (int i = 0; i < listPermission.size(); i++) {
            String permission = getPermissionName(context, listPermission.get(i));
            if (!TextUtils.isEmpty(permission)) {
                if (i == listPermission.size() - 1)
                    allPermission += permission;
                else
                    allPermission += String.format("%s, ", permission);
            }
        }
        if (!allPermission.equals(""))
            Toast.makeText(context, context.getString(R.string.permission_multi_denied) + " untuk " + allPermission, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context, context.getString(R.string.permission_multi_denied), Toast.LENGTH_LONG).show();

    }

    private static String getPermissionName(Context context, String permission) {
        switch (permission) {
            case Manifest.permission.CAMERA:
                return context.getString(R.string.permission_camera);
            case Manifest.permission.READ_EXTERNAL_STORAGE:
                return context.getString(R.string.permission_read_storage);
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                return context.getString(R.string.permission_write_storage);
            case Manifest.permission.READ_CONTACTS:
                return context.getString(R.string.permission_contacts);
            case Manifest.permission.ACCESS_FINE_LOCATION:
                return context.getString(R.string.permission_location);
            case Manifest.permission.ACCESS_COARSE_LOCATION:
                return context.getString(R.string.permission_location);
            case Manifest.permission.GET_ACCOUNTS:
                return context.getString(R.string.permission_accounts);
            case Manifest.permission.CALL_PHONE:
                return context.getString(R.string.permission_phone);
            default:
                return "";
        }
    }

    public static void onPermissionDenied(Context context, String permission) {
        switch (permission) {
            case Manifest.permission.CAMERA:
                Toast.makeText(context, R.string.permission_camera_denied, Toast.LENGTH_LONG).show();
                break;
            case Manifest.permission.READ_EXTERNAL_STORAGE:
                Toast.makeText(context, R.string.permission_storage_denied, Toast.LENGTH_LONG).show();
                break;
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                Toast.makeText(context, R.string.permission_storage_denied, Toast.LENGTH_LONG).show();
                break;
            case Manifest.permission.READ_CONTACTS:
                Toast.makeText(context, R.string.permission_contacts_denied, Toast.LENGTH_LONG).show();
                break;
            case Manifest.permission.ACCESS_FINE_LOCATION:
                Toast.makeText(context, R.string.permission_location_denied, Toast.LENGTH_LONG).show();
                break;
            case Manifest.permission.GET_ACCOUNTS:
                Toast.makeText(context, R.string.permission_get_accounts_denied, Toast.LENGTH_LONG).show();
                break;
            case Manifest.permission.READ_SMS:
            case Manifest.permission.RECEIVE_SMS:
                Toast.makeText(context, R.string.permission_sms_denied, Toast.LENGTH_LONG).show();
                break;
            case Manifest.permission.SEND_SMS:
                Toast.makeText(context, R.string.permission_send_sms_denied, Toast.LENGTH_LONG).show();
                break;
            case Manifest.permission.CALL_PHONE:
                Toast.makeText(context, R.string.permission_phone_denied, Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(context, R.string.permission_multi_denied, Toast.LENGTH_LONG).show();
                break;
        }

    }

    public static void onNeverAskAgain(Context context, String permission) {
        switch (permission) {
            case Manifest.permission.CAMERA:
                Toast.makeText(context, R.string.permission_camera_neverask, Toast.LENGTH_LONG).show();
                break;
            case Manifest.permission.READ_EXTERNAL_STORAGE:
                Toast.makeText(context, R.string.permission_storage_neverask, Toast.LENGTH_LONG).show();
                break;
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                Toast.makeText(context, R.string.permission_storage_neverask, Toast.LENGTH_LONG).show();
                break;
            case Manifest.permission.READ_CONTACTS:
                Toast.makeText(context, R.string.permission_contacts_neverask, Toast.LENGTH_LONG).show();
                break;
            case Manifest.permission.ACCESS_FINE_LOCATION:
                Toast.makeText(context, R.string.permission_location_neverask, Toast.LENGTH_LONG).show();
                break;
            case Manifest.permission.GET_ACCOUNTS:
                Toast.makeText(context, R.string.permission_get_accounts_neverask, Toast.LENGTH_LONG).show();
                break;
            case Manifest.permission.READ_SMS:
            case Manifest.permission.RECEIVE_SMS:
                Toast.makeText(context, R.string.permission_sms_neverask, Toast.LENGTH_LONG).show();
                break;
            case Manifest.permission.SEND_SMS:
                Toast.makeText(context, R.string.permission_send_sms_neverask, Toast.LENGTH_LONG).show();
                break;
            case Manifest.permission.CALL_PHONE:
                Toast.makeText(context, R.string.permission_phone_neverask, Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(context, R.string.permission_multi_neverask, Toast.LENGTH_LONG).show();
                break;

        }
    }

    public static void onNeverAskAgain(Context context, List<String> listPermission) {
        Toast.makeText(context, R.string.permission_multi_neverask, Toast.LENGTH_LONG).show();

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

    public static void onShowRationale(Context context, final PermissionRequest request,
                                       List<String> permission) {
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

    public static int getNeedPermissionMessage(List<String> permission) {
        return R.string.need_permission_multi;
    }

    public static int getNeedPermissionMessage(String permission) {
        switch (permission) {
            case Manifest.permission.CAMERA:
                return R.string.need_permission_camera;
            case Manifest.permission.READ_EXTERNAL_STORAGE:
                return R.string.need_permission_storage;
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                return R.string.need_permission_storage;
            case Manifest.permission.READ_CONTACTS:
                return R.string.need_permission_contacts;
            case Manifest.permission.ACCESS_FINE_LOCATION:
                return R.string.need_permission_location;
            case Manifest.permission.GET_ACCOUNTS:
                return R.string.need_permission_get_accounts;
            case Manifest.permission.READ_SMS:
            case Manifest.permission.RECEIVE_SMS:
                return R.string.need_permission_SMS;
            case Manifest.permission.SEND_SMS:
                return R.string.need_permission_send_SMS;
            case Manifest.permission.CALL_PHONE:
                return R.string.need_permission_SMS;
            default:
                return R.string.need_permission_multi;
        }
    }

    public static void onFinishActivityIfNeverAskAgain(Activity activity, String permission) {
        if (ContextCompat.checkSelfPermission(activity, permission)
                == PackageManager.PERMISSION_DENIED) {
            RequestPermissionUtil.onNeverAskAgain(activity, permission);
            activity.finish();
        }
    }

    public static boolean checkHasPermission(Activity activity, String permission) {
        return ContextCompat.checkSelfPermission(activity, permission)
                == PackageManager.PERMISSION_GRANTED;
    }


}
