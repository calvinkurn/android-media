package com.tokopedia.abstraction.common.utils.view;

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.annotation.NonNull
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.tokopedia.abstraction.R
import org.jetbrains.annotations.NotNull

/**
 * @author by nisie on 28/11/18.
 */
@RequiresApi(Build.VERSION_CODES.M)
class PermissionCheckerHelper {

    val REQUEST_PERMISSION_CODE = 789
    private val TEXT_OK: Int = R.string.title_ok
    private val TEXT_CANCEL: Int = R.string.dialog_cancel
    private val TEXT_NEED_PERMISSION: Int = R.string.need_permission
    private val TEXT_PERMISSION_DENIED: Int = R.string.permission_denied
    private val TEXT_NEVER_ASK_AGAIN: Int = R.string.permission_neverask

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
     * @param context you need to pass the activity / fragment where the permission is used.
     * @param permission is from Manifest.permission.{PERMISSION_NAME}, or use this class'
     * companion object for easier naming.
     * @param listener to call dialogue or perform other action should permission be denied or
     * granted.
     */
    fun checkPermission(@NotNull context: Context,
                        @NotNull permission: String,
                        @NotNull listener: PermissionCheckListener) {
        checkPermissions(context, arrayOf(permission), listener)
    }

    /**
     * Use this function for multiple permissions
     * @param context you need to pass the activity / fragment where the permission is used.
     * @param permissions is from Manifest.permission.{PERMISSION_NAME}, or use this class'
     * companion object for easier naming.
     * @param listener to call dialogue or perform other action should permission be denied or
     * granted.
     */
    fun checkPermissions(@NotNull context: Context,
                         @NotNull permissions: Array<String>,
                         @NotNull listener: PermissionCheckListener) {
        if (!(context is Activity || context is Fragment)) {
            return
        }

        try {
            if (!hasPermission(context, permissions)) {
                onPermissionNotGranted(context, permissions, listener)
            } else {
                listener.onPermissionGranted()
            }
        } catch (exception: NullPointerException) {
            exception.printStackTrace()
            listener.onPermissionDenied(permissions)
        }
    }

    private fun onPermissionNotGranted(context: Context, permissions: Array<String>, listener:
    PermissionCheckListener) {
        if (!permissions.isEmpty() && permissions.size > 1) {

            var permissionCount = 0
            var permissionText = ""
            val iterator = permissions.iterator()
            while (iterator.hasNext()) {
                val permission = iterator.next()
                if (shouldShowRequestPermissionRationale(context, permission)) {
                    permissionCount += 1
                    permissionText = String.format("%s, %s", permissionText, permission)
                }
            }

            when {
                permissionCount > 1 -> onShowRationale(context, permissions, permissionText,
                        listener)
                permissionCount == 1 -> onShowRationale(context, permissions,
                        permissionText.replace(",", "").trim(), listener)
                else -> listener.onNeverAskAgain(permissions)
            }

        } else if (!permissions.isEmpty()
                && shouldShowRequestPermissionRationale(context, permissions[0])) {
            onShowRationale(context, permissions, permissions[0], listener)
        } else if (!permissions.isEmpty()) {
            requestPermissions(context, permissions, REQUEST_PERMISSION_CODE)
        }
    }

    private fun requestPermissions(context: Context, permissions: Array<String>,
                                   REQUEST_PERMISSION_CODE: Int) {
        return try {
            when (context) {
                is Activity -> context.requestPermissions(permissions, REQUEST_PERMISSION_CODE)
                is Fragment -> context.requestPermissions(permissions, REQUEST_PERMISSION_CODE)
                else -> {
                }
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    private fun shouldShowRequestPermissionRationale(context: Context, permission: String):
            Boolean {
        return try {
            when (context) {
                is Activity -> ActivityCompat.shouldShowRequestPermissionRationale(context, permission)
                is Fragment -> {
                    val activity: Activity = context.requireActivity().parent
                    ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
                }
                else -> false
            }
        } catch (e: IllegalStateException) {
            false
        }
    }


    private fun onShowRationale(context: Context, permissions: Array<String>,
                                permissionText: String, listener: PermissionCheckListener) {
        android.support.v7.app.AlertDialog.Builder(context)
                .setMessage(getNeedPermissionMessage(context, permissionText))
                .setPositiveButton(TEXT_OK) { _, _ ->
                    requestPermissions(context, permissions, REQUEST_PERMISSION_CODE)
                }
                .setNegativeButton(TEXT_CANCEL) { _, _ ->
                    listener.onPermissionDenied(permissions)
                }
                .show()
    }


    private fun getNeedPermissionMessage(context: Context, permissionText: String): String {
        return String.format(context.getString(TEXT_NEED_PERMISSION), permissionText)
    }

    private fun hasPermission(context: Context, permissions: Array<String>): Boolean {
        val iterator = permissions.iterator()
        while (iterator.hasNext()) {
            val permission = iterator.next()
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun onRequestPermissionsResult(context: Context, requestCode: Int,
                                   permissions: Array<String>,
                                   grantResults: IntArray,
                                   listener: PermissionCheckListener) {

        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (i in permissions.indices) {
                if (grantResults.isNotEmpty() && grantResults[i] == PackageManager.PERMISSION_DENIED) {

                    if (shouldShowRequestPermissionRationale(context, permissions[i])) {
                        listener.onPermissionDenied(permissions)
                    } else {
                        listener.onNeverAskAgain(permissions)
                    }
                }
            }
        }
    }


    fun onPermissionDenied(@NonNull context: Context, @NonNull permissions: Array<String>) {
        Toast.makeText(context, String.format(context.getString(TEXT_PERMISSION_DENIED), permissions
                .joinToString
                ("," +
                        "")), Toast
                .LENGTH_SHORT).show()
    }

    fun onNeverAskAgain(@NonNull context: Context, @NonNull permissions: Array<String>) {
        Toast.makeText(context, String.format(context.getString(TEXT_NEVER_ASK_AGAIN), permissions
                .joinToString("," +
                        "")), Toast
                .LENGTH_SHORT).show()
    }
}