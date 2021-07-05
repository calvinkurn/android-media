package com.tokopedia.utils.permission

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.utils.R
import com.tokopedia.utils.permission.PermissionCheckerHelper.Companion
import com.tokopedia.utils.permission.PermissionCheckerHelper.Companion.PERMISSION_ACCESS_COARSE_LOCATION
import com.tokopedia.utils.permission.PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION
import com.tokopedia.utils.permission.PermissionCheckerHelper.Companion.PERMISSION_CAMERA
import com.tokopedia.utils.permission.PermissionCheckerHelper.Companion.PERMISSION_NFC
import com.tokopedia.utils.permission.PermissionCheckerHelper.Companion.PERMISSION_READ_CONTACT
import com.tokopedia.utils.permission.PermissionCheckerHelper.Companion.PERMISSION_READ_EXTERNAL_STORAGE
import com.tokopedia.utils.permission.PermissionCheckerHelper.Companion.PERMISSION_RECORD_AUDIO
import com.tokopedia.utils.permission.PermissionCheckerHelper.Companion.PERMISSION_WRITE_EXTERNAL_STORAGE
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
 * .permission.CAMERA) in [Companion] to track which permissions used in this app and add visual
 * permission name (e.g : Kamera) in function
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
        const val PERMISSION_CAMERA = Manifest.permission.CAMERA
        const val PERMISSION_READ_CONTACT = Manifest.permission.READ_CONTACTS
        const val PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO
        const val PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
        const val PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
        const val PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
        const val PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
        const val PERMISSION_NFC = Manifest.permission.NFC
        const val PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE
        const val PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE
        const val PERMISSION_READ_CALL_LOG = Manifest.permission.READ_CALL_LOG
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
     * @param rationaleText (optional) to put in dialogue why you need the permission. Leave
     * empty to use default.
     */
    fun checkPermission(@NotNull activity: Activity,
                        @NotNull permission: String,
                        @NotNull listener: PermissionCheckListener,
                        @NotNull rationaleText: String = "") {
        checkPermissions(activity, arrayOf(permission), listener, rationaleText)
    }

    fun checkPermission(@NotNull fragment: Fragment,
                        @NotNull permission: String,
                        @NotNull listener: PermissionCheckListener,
                        @NotNull rationaleText: String = "") {
        checkPermissions(fragment, arrayOf(permission), listener, rationaleText)
    }

    /**
     * Use this function for multiple permissions
     * @param context you need to pass the activity / fragment where the permission is used.
     * @param permissions is from Manifest.permission.{PERMISSION_NAME}, or use this class'
     * companion object for easier naming.
     * @param listener to call dialogue or perform other action should permission be denied or
     * granted.
     * @param rationaleText (optional) to put in dialogue why you need the permission. Leave
     * empty to use default.
     */

    fun checkPermissions(@NotNull activity: Activity,
                         @NotNull permissions: Array<String>,
                         @NotNull listener: PermissionCheckListener,
                         @NotNull rationaleText: String = "") {
        this.listener = listener

        try {
            if (!hasPermission(activity, permissions)) {
                onPermissionNotGranted(activity, permissions, listener, rationaleText)
            } else {
                listener.onPermissionGranted()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun checkPermissions(@NotNull fragment: Fragment,
                         @NotNull permissions: Array<String>,
                         @NotNull listener: PermissionCheckListener,
                         @NotNull rationaleText: String = "") {
        this.listener = listener

        try {
            if (!hasPermission(fragment.requireContext(), permissions)) {
                onPermissionNotGranted(fragment, permissions, listener, rationaleText)
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
            PERMISSION_WRITE_EXTERNAL_STORAGE -> context.getString(R.string.permission_write_storage)
            PERMISSION_READ_EXTERNAL_STORAGE -> context.getString(R.string.permission_read_storage)
            PERMISSION_READ_CONTACT -> context.getString(R.string.permission_contacts)
            PERMISSION_NFC -> context.getString(R.string.permission_nfc)
            else -> ""
        }
    }

    private fun onPermissionNotGranted(activity: Activity, permissions: Array<String>, listener:
    PermissionCheckListener, rationaleText: String) {
        if (!permissions.isEmpty() && permissions.size > 1) {

            var permissionCount = 0
            var listPermissionText = ""
            val iterator = permissions.iterator()
            while (iterator.hasNext()) {
                val permission = iterator.next()
                if (shouldShowRequestPermissionRationale(activity, permission)) {
                    permissionCount += 1

                    val permissionName = getPermissionName(activity, permission)
                    if (listPermissionText.isBlank()) {
                        listPermissionText = permissionName
                    } else if (!listPermissionText.contains(permissionName)) {
                        listPermissionText = String.format("%s, %s", listPermissionText, permissionName)
                    }
                }
            }

            when {
                permissionCount > 1 -> onShowRationale(activity, permissions, listPermissionText,
                        listener, rationaleText)
                permissionCount == 1 -> onShowRationale(activity, permissions,
                        listPermissionText.replace(",", "").trim()
                        , listener, rationaleText)
                else -> requestPermissions(activity, permissions, REQUEST_PERMISSION_CODE)
            }

        } else if (!permissions.isEmpty()
                && shouldShowRequestPermissionRationale(activity, permissions[0])) {
            onShowRationale(activity, permissions, getPermissionName(activity, permissions[0]), listener, rationaleText)
        } else if (!permissions.isEmpty()) {
            requestPermissions(activity, permissions, REQUEST_PERMISSION_CODE)
        }
    }

    private fun onPermissionNotGranted(fragment: Fragment, permissions: Array<String>, listener:
    PermissionCheckListener, rationaleText: String) {
        if (!permissions.isEmpty() && permissions.size > 1) {

            var permissionCount = 0
            var listPermissionText = ""
            val iterator = permissions.iterator()
            while (iterator.hasNext()) {
                val permission = iterator.next()
                if (shouldShowRequestPermissionRationale(fragment.requireContext(), permission)) {
                    permissionCount += 1

                    val permissionName = getPermissionName(fragment.requireContext(), permission)
                    if (listPermissionText.isBlank()) {
                        listPermissionText = permissionName
                    } else if (!listPermissionText.contains(permissionName)) {
                        listPermissionText = String.format("%s, %s", listPermissionText, permissionName)
                    }
                }
            }

            when {
                permissionCount > 1 -> onShowRationale(fragment, permissions, listPermissionText,
                        listener, rationaleText)
                permissionCount == 1 -> onShowRationale(fragment, permissions,
                        listPermissionText.replace(",", "").trim()
                        , listener, rationaleText)
                else -> requestPermissions(fragment, permissions, REQUEST_PERMISSION_CODE)
            }

        } else if (!permissions.isEmpty()
                && shouldShowRequestPermissionRationale(fragment.requireContext(), permissions[0])) {
            onShowRationale(fragment, permissions, permissions[0], listener, rationaleText)
        } else if (!permissions.isEmpty()) {
            requestPermissions(fragment, permissions, REQUEST_PERMISSION_CODE)
        }
    }

    private fun requestPermissions(activity: Activity, permissions: Array<String>,
                                   REQUEST_PERMISSION_CODE: Int) {

        try {
            ActivityCompat.requestPermissions(activity, permissions,
                    REQUEST_PERMISSION_CODE)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    private fun requestPermissions(fragment: Fragment, permissions: Array<String>,
                                   REQUEST_PERMISSION_CODE: Int) {

        try {
            fragment.requestPermissions(permissions, REQUEST_PERMISSION_CODE)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    private fun shouldShowRequestPermissionRationale(context: Context, permission: String):
            Boolean {
        return try {
            when (context) {
                is AppCompatActivity, is Activity -> ActivityCompat
                        .shouldShowRequestPermissionRationale(context as Activity,
                                permission)
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


    private fun onShowRationale(activity: Activity, permissions: Array<String>,
                                permissionText: String, listener: PermissionCheckListener,
                                rationaleText: String) {

        // todo: change to dialog unify and remove tkpddesign dependency
        val dialog = DialogUnify(activity, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(activity.getString(TEXT_TITLE))
        dialog.setDescription(getNeedPermissionMessage(activity, permissionText, rationaleText))
        dialog.setPrimaryCTAText(activity.getString(TEXT_OK))
        dialog.setSecondaryCTAText(activity.getString(TEXT_CANCEL))
        dialog.setCancelable(true)
        dialog.setPrimaryCTAClickListener {
            dialog.dismiss()
            requestPermissions(activity, permissions, REQUEST_PERMISSION_CODE)
        }
        dialog.setSecondaryCTAClickListener {
            listener.onPermissionDenied(permissionText)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun onShowRationale(fragment: Fragment, permissions: Array<String>,
                                permissionText: String, listener: PermissionCheckListener,
                                rationaleText: String) {

        val activity: Activity = fragment.activity as Activity

        val dialog = DialogUnify(activity, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(fragment.getString(TEXT_TITLE))
        dialog.setDescription(getNeedPermissionMessage(fragment.requireContext(), permissionText, rationaleText))
        dialog.setPrimaryCTAText(fragment.getString(TEXT_OK))
        dialog.setSecondaryCTAText(fragment.getString(TEXT_CANCEL))
        dialog.setCancelable(true)
        dialog.setPrimaryCTAClickListener {
            dialog.dismiss()
            requestPermissions(fragment, permissions, REQUEST_PERMISSION_CODE)
        }
        dialog.setSecondaryCTAClickListener {
            listener.onPermissionDenied(permissionText)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun getNeedPermissionMessage(context: Context, permissionText: String, rationaleText: String): String {
        return if (rationaleText.isBlank()) String.format(context.getString(TEXT_NEED_PERMISSION),
                permissionText) else rationaleText
    }

    fun hasPermission(context: Context, permissions: Array<String>): Boolean {
        val iterator = permissions.iterator()
        while (iterator.hasNext()) {
            val permission = iterator.next()
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun onRequestPermissionsResult(context: Context?, requestCode: Int,
                                   permissions: Array<String>,
                                   grantResults: IntArray) {
        if (context == null) {
            return
        }
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
                listener.onNeverAskAgain(getJoinedText(permissionsDenied))
            } else {
                listener.onPermissionDenied(getJoinedText
                (permissionsDeniedNeedToShowRationale))
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

    private fun getJoinedText(permissions: Array<String>): String {
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
