package com.tokopedia.device.info.permission

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.tokopedia.device.info.DeviceInfo
import com.tokopedia.device.info.cache.DeviceInfoCache
import com.tokopedia.device.info.cache.FingerprintCache


/**
 * ImeiPermissionAsker is helper to ask Imei Permission
 * It will automatically setImei to cache whenever the user accept the Imei Permission
 * How to use:
 *
{
ImeiPermissionAsker.checkImeiPermission(activity, onNeedAskPermission = {
// show intro of why this permission needed
// for example: show dialog to info that this permission is needed to enable promo
// when user click ok, call ImeiPermissionAsker.askImeiPermission(activity)
}, onAlreadyGranted = {
// user already grant imei permission, so go to next step
})
}

override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
super.onRequestPermissionsResult(requestCode, permissions, grantResults)
ImeiPermissionAsker.onImeiRequestPermissionsResult(activity, requestCode, permissions, grantResults,
onUserDenied = {
// user click don't allow imei permission
}, onUserDeniedAndDontAskAgain = {
// user click don't allow imei permission and don't ask again
}, onUserAcceptPermission = {
// user click allow, so go to next step
})
}
 *
 */

object ImeiPermissionAsker {
    const val IMEI_PERMISION_REQ_CODE = 720

    fun checkImeiPermission(activity: Activity, onNeedAskPermission: (() -> Unit), onAlreadyGranted: (() -> Unit)) {
        val hasPhoneStatePermission = ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
        if (hasPhoneStatePermission) {
            onAlreadyGranted.invoke()
        } else {
            onNeedAskPermission.invoke()
        }
    }

    fun askImeiPermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity, arrayOf(Manifest.permission.READ_PHONE_STATE),
            IMEI_PERMISION_REQ_CODE
        );
    }

    fun onImeiRequestPermissionsResult(activity: Activity, requestCode: Int, permissions: Array<String>, grantResults: Array<Int>,
                                       onUserDenied: (() -> Unit),
                                       onUserDeniedAndDontAskAgain: (() -> Unit),
                                       onUserAcceptPermission: (() -> Unit)) {
        if (requestCode == IMEI_PERMISION_REQ_CODE) {
            // for each permission check if the user granted/denied them
            // you may want to group the rationale in a single dialog,
            // this is just an example
            for ((i, value) in permissions.withIndex()) {
                if (value == Manifest.permission.READ_PHONE_STATE) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        // user rejected the permission
                        val showRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_PHONE_STATE)
                        if (!showRationale) {
                            onUserDeniedAndDontAskAgain.invoke()
                        } else {
                            onUserDenied.invoke()
                        }
                    } else if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        DeviceInfoCache(activity).clearImei()
                        DeviceInfo.getImei(activity)
                        FingerprintCache.clearFingerprintCache(activity)
                        onUserAcceptPermission.invoke()
                    }
                    break
                }
            }
        }
    }
}