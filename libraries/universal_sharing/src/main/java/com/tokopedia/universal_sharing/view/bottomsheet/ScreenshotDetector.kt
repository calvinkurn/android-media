package com.tokopedia.universal_sharing.view.bottomsheet

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.ContentObserver
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.R
import com.tokopedia.universal_sharing.view.bottomsheet.listener.PermissionListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ScreenShotListener
import java.lang.Exception

class ScreenshotDetector(
    internal val context: Context,
    internal var screenShotListener: ScreenShotListener?,
    private val permissionListener: PermissionListener? = null
) {

    private var contentObserver: ContentObserver? = null
    private val pendingRegex = ".pending"
    private val actionPermissionDialog = "click - access photo media and files"
    private val labelAllow = "allow"
    private val labelDeny = "deny"
    private val readingDelayTime = 1100L
    private val screenShotDelayTime = 500L
    private var ssUriPath = ""

    fun start() {
        if (haveStoragePermission() && contentObserver == null) {
            contentObserver = context.contentResolver.registerObserver()
        }
    }

    fun stop() {
        contentObserver?.let { context.contentResolver.unregisterContentObserver(it) }
        contentObserver = null
    }

    private fun queryScreenshots(uri: Uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            queryRelativeDataColumn(uri)
        } else {
            queryDataColumn(uri)
        }
    }

    private fun queryDataColumn(uri: Uri) {
        try {
            val projection = arrayOf(
                MediaStore.Images.Media.DATA
            )
            context.contentResolver.query(
                uri,
                projection,
                null,
                null,
                null
            )?.use { cursor ->
                val dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                while (cursor.moveToNext()) {
                    val path = cursor.getString(dataColumn)
                    if (path.contains(screenShotRegex, true)) {
                        // do something
                        if (!ssUriPath.equals(path)) {
                            ssUriPath = path
                            if (!path.contains(pendingRegex)) {
                                notifyScreenShotTaken(path)
                            }
                        }
                    }
                }
            }
        } catch (exception: Exception) {
            logError(exception.localizedMessage)
        }
    }

    private fun queryRelativeDataColumn(uri: Uri) {
        try {
            val projection = arrayOf(
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATA
            )
            context.contentResolver.query(
                uri,
                projection,
                null,
                null,
                null
            )?.use { cursor ->
                val relativePathColumn =
                    cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                val displayNameColumn =
                    cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
                while (cursor.moveToNext()) {
                    val name = cursor.getString(displayNameColumn)
                    val relativePath = cursor.getString(relativePathColumn)
                    if (name.contains(screenShotRegex, true) or
                        relativePath.contains(screenShotRegex, true)
                    ) {
                        // do something
                        if (!ssUriPath.equals(relativePath)) {
                            ssUriPath = relativePath
                            if (!relativePath.contains(pendingRegex)) {
                                notifyScreenShotTaken(relativePath)
                            }
                        }
                    }
                }
            }
        } catch (exception: Exception) {
            logError(exception.localizedMessage)
        }
    }

    private fun notifyScreenShotTaken(path: String) {
        Handler(Looper.getMainLooper()).postDelayed({
            screenShotListener?.screenShotTaken(path)
        }, screenShotDelayTime)
    }

    private fun ContentResolver.registerObserver(): ContentObserver {
        val contentObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean, uri: Uri?) {
                super.onChange(selfChange, uri)
                uri?.let {
                    queryScreenshots(it)
                }
            }
        }
        registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, contentObserver)
        return contentObserver
    }

    fun haveStoragePermission() =
        context.let {
            ContextCompat.checkSelfPermission(
                it,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Manifest.permission.READ_MEDIA_IMAGES
                } else {
                    Manifest.permission.READ_EXTERNAL_STORAGE
                }
            )
        } == PackageManager.PERMISSION_GRANTED

    fun requestPermission(fragment: Fragment) {
        if (!haveStoragePermission()) {
            val permissions = arrayOf(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Manifest.permission.READ_MEDIA_IMAGES
                } else {
                    Manifest.permission.READ_EXTERNAL_STORAGE
                }
            )
            fragment.requestPermissions(permissions, READ_EXTERNAL_STORAGE_REQUEST)
        }
    }

    fun detectScreenshots(
        fragment: Fragment,
        display: (() -> Unit)? = null,
        requestPermission: Boolean = false,
        toastView: View? = null
    ) {
        if (haveStoragePermission()) {
            start()
            display?.invoke()
        } else {
            if (requestPermission) {
                fragment.context?.let {
                    if (isValidPermissionAskingAttempt(it)) {
                        showCustomPermissionDialog(fragment, toastView, display)
                    } else {
                        display?.invoke()
                    }
                }
            }
        }
    }

    private fun showCustomPermissionDialog(fragment: Fragment, toastView: View?, display: (() -> Unit)?) {
        var permissionDialogCustom = DialogUnify(
            fragment.requireContext(),
            DialogUnify.VERTICAL_ACTION,
            DialogUnify.WITH_ILLUSTRATION
        ).apply {
            setPrimaryCTAText(fragment.getString(R.string.permission_dialog_primary_cta))
            setPrimaryCTAClickListener {
                this.setOnDismissListener { }
                display?.invoke()
                requestPermission(fragment)
                dismiss()
                permissionListener?.permissionAction(actionPermissionDialog, fragment.getString(R.string.permission_dialog_primary_cta))
            }
            setSecondaryCTAText(fragment.getString(R.string.permission_dialog_secondary_cta))
            setSecondaryCTAClickListener {
                this.setOnDismissListener { }
                dismiss()
                toastView?.let { Toaster.build(it, text = fragment.getString(R.string.permission_denied_toast)).show() }
                Handler(Looper.getMainLooper()).postDelayed({
                    display?.invoke()
                }, readingDelayTime)
                permissionListener?.permissionAction(actionPermissionDialog, fragment.getString(R.string.permission_dialog_secondary_cta))
            }
            setOnDismissListener {
                dismiss()
                toastView?.let { Toaster.build(it, text = fragment.getString(R.string.permission_denied_toast)).show() }
                Handler(Looper.getMainLooper()).postDelayed({
                    display?.invoke()
                }, readingDelayTime)
                permissionListener?.permissionAction(actionPermissionDialog, fragment.getString(R.string.permission_dialog_secondary_cta))
            }
            setTitle(fragment.getString(R.string.permission_dialog_title))
            setDescription(fragment.getString(R.string.permission_dialog_description))
            setImageDrawable(R.drawable.permission_dialog_image)
        }
        permissionDialogCustom.show()
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        grantResults: IntArray,
        fragment: Fragment
    ) {
        when (requestCode) {
            READ_EXTERNAL_STORAGE_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    start()
                    permissionListener?.permissionAction(actionPermissionDialog, labelAllow)
                } else {
                    permissionListener?.permissionAction(actionPermissionDialog, labelDeny)
                    if (!fragment.shouldShowRequestPermissionRationale(
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                Manifest.permission.READ_MEDIA_IMAGES
                            } else {
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            }
                        )
                    ) {
                        goToDeviceSetting(fragment)
                    }
                }
                return
            }
        }
    }

    private fun logError(exceptionMessage: String?) {
        exceptionMessage?.let {
            if (!TextUtils.isEmpty(exceptionMessage)) {
                val messageMap: Map<String, String> =
                    mapOf(
                        LABEL_TYPE to LABEL_ERROR,
                        LABEL_REASON to exceptionMessage
                    )
                ServerLogger.log(Priority.P2, TAG_SS_ERR, messageMap)
            }
        }
    }

    private fun goToDeviceSetting(fragment: Fragment) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts(LABEL_PACKAGE, context.packageName, null)
        intent.data = uri
        fragment.activity?.startActivityForResult(intent, READ_EXTERNAL_STORAGE_REQUEST)
    }

    private fun isValidPermissionAskingAttempt(context: Context): Boolean {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        val mSharedPrefs = context.getSharedPreferences(
            SCREENSHOT_PREFERENCE,
            Context.MODE_PRIVATE
        )
        val attemptCounter = remoteConfig.getLong(PERMISSION_ATTEMPT_COUNTER_FLAG)
        return if (remoteConfig.getBoolean(PERMISSION_ATTEMPT_RESET_FLAG)) {
            mSharedPrefs.edit().putLong(SCREENSHOT_PERMISSION_ATTEMPT_COUNTER, attemptCounter - 1).apply()
            attemptCounter > 0
        } else {
            val remainingAttempts = mSharedPrefs.getLong(SCREENSHOT_PERMISSION_ATTEMPT_COUNTER, attemptCounter)
            mSharedPrefs.edit().putLong(SCREENSHOT_PERMISSION_ATTEMPT_COUNTER, remainingAttempts - 1).apply()
            remainingAttempts > 0
        }
    }

    companion object {
        // permission request code
        const val screenShotRegex = "screenshot"
        const val READ_EXTERNAL_STORAGE_REQUEST = 500
        const val TAG_SS_ERR = "SCREENSHOT_ERROR"
        const val LABEL_TYPE = "type"
        const val LABEL_ERROR = "error"
        const val LABEL_REASON = "reason"
        const val LABEL_PACKAGE = "package"
        const val SCREENSHOT_PREFERENCE = "screenshot_preference"
        const val SCREENSHOT_PERMISSION_ATTEMPT_COUNTER = "screenshot_permission_attempt_counter"
        const val PERMISSION_ATTEMPT_RESET_FLAG = "android_ss_permission_attempt_reset"
        const val PERMISSION_ATTEMPT_COUNTER_FLAG = "android_ss_permission_attempt_counter"
    }
}
