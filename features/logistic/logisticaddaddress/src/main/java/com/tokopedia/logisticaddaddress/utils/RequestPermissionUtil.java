package com.tokopedia.logisticaddaddress.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.tokopedia.logisticaddaddress.R;

import java.util.List;

import permissions.dispatcher.PermissionRequest;

/**
 * Created by Fajar Ulin Nuha on 29/10/18.
 */
public class RequestPermissionUtil {

    public static boolean checkHasPermission(Activity activity, String permission) {
        return ContextCompat.checkSelfPermission(activity, permission)
                == PackageManager.PERMISSION_GRANTED;
    }
}
