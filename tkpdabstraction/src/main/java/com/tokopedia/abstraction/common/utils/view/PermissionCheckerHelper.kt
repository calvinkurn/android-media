package com.tokopedia.abstraction.common.utils.view

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.tokopedia.abstraction.R
import org.jetbrains.annotations.NotNull

/**
 * @author by nisie on 28/11/18.
 */
class PermissionCheckerHelper {

    val REQUEST_PERMISSION_CODE = 789

    object Companion {
        val PERMISSION_CAMERA = Manifest.permission.CAMERA
        val PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO
    }

    interface PermissionCheckListener {
        fun onPermissionDenied(permissions: Array<String>)
        fun onNeverAskAgain(permissions: Array<String>)
        fun onPermissionGranted()
    }

    /**
     * @param activity you need to pass the activity where the permission is used.
     * @param permission is from Manifest.permission.{PERMISSION_NAME}, or use this class'
     * companion object for easier naming.
     * @param listener to call dialogue or perform other action should permission be denied or
     * granted.
     */
    fun checkPermission(@NotNull activity: Activity,
                        @NotNull permission: String,
                        @NotNull listener: PermissionCheckListener) {
        checkPermissions(activity, arrayOf(permission), listener)
    }

    /**
     * Use this function for multiple permissions
     * @param activity you need to pass the activity where the permission is used.
     * @param permissions is from Manifest.permission.{PERMISSION_NAME}, or use this class'
     * companion object for easier naming.
     * @param listener to call dialogue or perform other action should permission be denied or
     * granted.
     */
    fun checkPermissions(@NotNull activity: Activity,
                         @NotNull permissions: Array<String>,
                         @NotNull listener: PermissionCheckListener) {
        try {
            if (!hasPermission(activity, permissions)) {
                onPermissionNotGranted(activity, permissions, listener)
            } else {
                listener.onPermissionGranted()
            }
        } catch (exception: NullPointerException) {
            exception.printStackTrace()
            listener.onPermissionDenied(permissions)
        }
    }

    private fun onPermissionNotGranted(activity: Activity, permissions: Array<String>, listener: PermissionCheckListener) {
        if (!permissions.isEmpty() && permissions.size > 1) {

            var permissionCount = 0
            var permissionText = ""
            val iterator = permissions.iterator()
            while (iterator.hasNext()) {
                val permission = iterator.next()
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    permissionCount += 1
                    permissionText = String.format("%s, %s", permissionText, permission)
                }
            }

            when {
                permissionCount > 1 -> onShowRationale(activity, permissions, permissionText,
                        listener)
                permissionCount == 1 -> onShowRationale(activity, permissions,
                        permissionText.replace(",", "").trim(), listener)
                else -> listener.onNeverAskAgain(permissions)
            }

        } else if (!permissions.isEmpty()
                && ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[0])) {
            onShowRationale(activity, permissions, permissions[0], listener)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun onShowRationale(activity: Activity, permissions: Array<String>,
                                permissionText: String, listener : PermissionCheckListener) {
        android.support.v7.app.AlertDialog.Builder(activity)
                .setMessage(getNeedPermissionMessage(activity, permissionText))
                .setPositiveButton(R.string.title_ok) { _, _ ->
                    activity.requestPermissions(permissions, REQUEST_PERMISSION_CODE)
                }
                .setNegativeButton(R.string.dialog_cancel) { _, _ ->
                    listener.onPermissionDenied(permissions)}
                .show()
    }

    private fun getNeedPermissionMessage(activity : Activity, permissionText: String): String {
        return String.format(activity.getString(R.string.need_permission), permissionText)
    }

    private fun hasPermission(activity: Activity, permissions: Array<String>): Boolean {
        val iterator = permissions.iterator()
        while (iterator.hasNext()) {
            val permission = iterator.next()
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun onRequestPermissionsResult(requestCode: Int,
                                   permissions: Array<String>,
                                   grantResults: IntArray,
                                   listener: PermissionCheckListener) {

        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (i in permissions.indices) {
                if (grantResults.isNotEmpty() && grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    onPermissionDenied()
                }
            }
        }
        when (requestCode) {
            REQUEST_PERMISSION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    listener.onPermissionGranted()
                } else {
                    listener.onPermissionDenied(permissions)
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    fun onPermissionDenied() {

    }
}