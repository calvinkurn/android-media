package com.tokopedia.permissionchecker

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.annotation.NonNull
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.tokopedia.design.component.Dialog
import com.tokopedia.permissionchecker.PermissionCheckerHelper.Companion
import com.tokopedia.permissionchecker.PermissionCheckerHelper.Companion.PERMISSION_ACCESS_COARSE_LOCATION
import com.tokopedia.permissionchecker.PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION
import com.tokopedia.permissionchecker.PermissionCheckerHelper.Companion.PERMISSION_CAMERA
import com.tokopedia.permissionchecker.PermissionCheckerHelper.Companion.PERMISSION_RECORD_AUDIO
import org.jetbrains.annotations.NotNull

/**
 * @author by nisie on 28/11/18.
 * How to use this class :
 *
 * 1. call either [checkPermission] or [checkPermissions] from your class
 *
 * 2. also call [onRequestPermissionsResult] on your activity / fragment's
 * onRequestPermissionResult.
 *
 * 3. (optional) you can also call [onPermissionDenied] and [onNeverAskAgain] if you don't want
 * to handle it yourself.
 *
 * 4. (optional) if you want to add new permission, make sure to add permission name (e.g : android
 * .permission.CAMERA) in [Companion] and add visual permission name (e.g : Kamera) in function
 * [getPermissionName]
 */

class PermissionCheckerHelper {

    val REQUEST_PERMISSION_CODE = 789
    private val TEXT_TITLE: Int = R.string.permission_title
    private val TEXT_OK: Int = R.string.permission_btn_ok
    private val TEXT_CANCEL: Int = R.string.permission_cancel
    private val TEXT_NEED_PERMISSION: Int = R.string.needs_permission
    private val TEXT_PERMISSION_DENIED: Int = R.string.permission_denied
    private val TEXT_NEVER_ASK_AGAIN: Int = R.string.permission_never_ask_again

    private lateinit var listener: PermissionCheckListener

    object Companion {
        val PERMISSION_CAMERA = Manifest.permission.CAMERA
        val PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO
        val PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
        val PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    }

    interface PermissionCheckListener {
        fun onPermissionDenied(permissionText: String)
        fun onNeverAskAgain(permissionText: String)
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
                        @NotNull listener: PermissionCheckListener,
                        @NotNull rationaleText : String = "") {
        checkPermissions(context, arrayOf(permission), listener, rationaleText)
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
                         @NotNull listener: PermissionCheckListener,
                         @NotNull rationaleText : String = "") {
        this.listener = listener

        if (!(context is Activity || context is Fragment)) {
            return
        }

        try {
            if (!hasPermission(context, permissions)) {
                onPermissionNotGranted(context, permissions, listener, rationaleText)
            } else {
                listener.onPermissionGranted()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getPermissionName(context: Context, permission: String): String {
        return when (permission) {
            PERMISSION_ACCESS_COARSE_LOCATION -> context.getString(R.string.permission_location)
            PERMISSION_ACCESS_FINE_LOCATION -> context.getString(R.string.permission_location)
            PERMISSION_CAMERA -> context.getString(R.string.permission_camera)
            PERMISSION_RECORD_AUDIO -> context.getString(R.string.permission_audio)
            else -> ""
        }
    }

    private fun onPermissionNotGranted(context: Context, permissions: Array<String>, listener:
    PermissionCheckListener, rationaleText: String) {
        if (!permissions.isEmpty() && permissions.size > 1) {

            var permissionCount = 0
            var listPermissionText = ""
            val iterator = permissions.iterator()
            while (iterator.hasNext()) {
                val permission = iterator.next()
                if (shouldShowRequestPermissionRationale(context, permission)) {
                    permissionCount += 1

                    val permissionName = getPermissionName(context, permission)
                    if (listPermissionText.isBlank()) {
                        listPermissionText = permissionName
                    } else if (!listPermissionText.contains(permissionName)) {
                        listPermissionText = String.format("%s, %s", listPermissionText, permissionName)
                    }
                }
            }

            when {
                permissionCount > 1 -> onShowRationale(context, permissions, listPermissionText ,
                        listener, rationaleText)
                permissionCount == 1 -> onShowRationale(context, permissions,
                        listPermissionText.replace(",", "").trim()
                        , listener, rationaleText)
                else -> requestPermissions(context, permissions, REQUEST_PERMISSION_CODE)
            }

        } else if (!permissions.isEmpty()
                && shouldShowRequestPermissionRationale(context, permissions[0])) {
            onShowRationale(context, permissions, permissions[0], listener, rationaleText)
        } else if (!permissions.isEmpty()) {
            requestPermissions(context, permissions, REQUEST_PERMISSION_CODE)
        }
    }

    private fun requestPermissions(context: Context, permissions: Array<String>,
                                   REQUEST_PERMISSION_CODE: Int) {

        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                when (context) {
                    is AppCompatActivity -> context.requestPermissions(permissions, REQUEST_PERMISSION_CODE)
                    is Activity -> context.requestPermissions(permissions, REQUEST_PERMISSION_CODE)
                    is Fragment -> context.requestPermissions(permissions, REQUEST_PERMISSION_CODE)
                    else -> {
                    }
                }
            } else {

            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    private fun shouldShowRequestPermissionRationale(context: Context, permission: String):
            Boolean {
        return try {
            when (context) {
                is AppCompatActivity -> ActivityCompat.shouldShowRequestPermissionRationale(context,
                        permission)
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
                                permissionText: String, listener: PermissionCheckListener,
                                rationaleText : String) {

        val activity: Activity = context as? Activity ?: if (context is Fragment) {
            context.activity as Activity
        } else {
            return
        }

        val dialog = Dialog(activity, Dialog.Type.PROMINANCE)
        dialog.setTitle(context.getString(TEXT_TITLE))
        dialog.setDesc(getNeedPermissionMessage(context, permissionText, rationaleText))
        dialog.setBtnOk(context.getString(TEXT_OK))
        dialog.setBtnCancel(context.getString(TEXT_CANCEL))
        dialog.alertDialog.setCancelable(true)
        dialog.alertDialog.setCanceledOnTouchOutside(true)
        dialog.setOnOkClickListener {
            requestPermissions(context, permissions, REQUEST_PERMISSION_CODE)
            dialog.dismiss()
        }
        dialog.setOnCancelClickListener {
            listener.onPermissionDenied(permissionText)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun getNeedPermissionMessage(context: Context, permissionText: String, rationaleText: String): String {
        return if (rationaleText.isBlank()) String.format(context.getString(TEXT_NEED_PERMISSION),
                permissionText) else rationaleText
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
                                   grantResults: IntArray) {

        var permissionsDenied: Array<String> = arrayOf()
        var permissionsDeniedNeedToShowRationale: Array<String> = arrayOf()

        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (i in permissions.indices) {
                if (grantResults.isNotEmpty() && grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    if (shouldShowRequestPermissionRationale(context, permissions[i])) {
                        permissionsDeniedNeedToShowRationale = permissionsDeniedNeedToShowRationale
                                .plus(getPermissionName(context,
                                        permissions[i]))
                    } else {
                        permissionsDenied = permissionsDenied.plus(getPermissionName(context,
                                permissions[i]))
                    }

                }
            }

            if (permissions.isNotEmpty()
                    && permissionsDenied.isEmpty()
                    && permissionsDeniedNeedToShowRationale.isEmpty()) {
                listener.onPermissionGranted()
            } else if (permissionsDenied.size > 1
                    && permissionsDenied.size > permissionsDeniedNeedToShowRationale.size) {
                listener.onNeverAskAgain(getJoinedText(context, permissionsDenied))
            } else {
                listener.onPermissionDenied(getJoinedText
                (context, permissionsDeniedNeedToShowRationale))
            }
        }
    }

    fun onPermissionDenied(@NonNull context: Context, @NonNull permissionText: String) {
        Toast.makeText(context, String.format(context.getString(TEXT_PERMISSION_DENIED),
                permissionText),
                Toast.LENGTH_LONG).show()
    }

    fun onNeverAskAgain(@NonNull context: Context, @NonNull permissionText: String) {
        Toast.makeText(context, String.format(context.getString(TEXT_NEVER_ASK_AGAIN),
                permissionText),
                Toast.LENGTH_LONG).show()
    }

    private fun getJoinedText(context: Context, permissions: Array<String>): String {
        var permissionText = ""
        val iterator = permissions.iterator()
        while (iterator.hasNext()) {
            val permission = iterator.next()

            if (permissionText.isBlank()) {
                permissionText = permission
            } else if (!permissionText.contains(permission)) {
                permissionText = String.format("%s, %s", permissionText, permission)
            }
        }
        return permissionText
    }
}
