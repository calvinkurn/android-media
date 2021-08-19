package com.tokopedia.universal_sharing.view.bottomsheet



import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.database.ContentObserver
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.TextUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ScreenShotListener
import java.lang.Exception

class ScreenshotDetector(internal val context: Context, internal var screenShotListener: ScreenShotListener?) {

    private var contentObserver: ContentObserver? = null
    val pendingRegex = ".pending"
    val screenShotRegex = "screenshot"
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
                                UniversalShareBottomSheet.setImageOnlySharingOption(true)
                                UniversalShareBottomSheet.setScreenShotImagePath(path)
                                screenShotListener?.screenShotTaken()
                            }
                        }
                    }
                }
            }
        }catch (exception:Exception){
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
                                UniversalShareBottomSheet.setImageOnlySharingOption(true)
                                UniversalShareBottomSheet.setScreenShotImagePath(relativePath)
                                screenShotListener?.screenShotTaken()
                            }
                        }
                    }
                }
            }
        }catch (exception:Exception){
            logError(exception.localizedMessage)
        }
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
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        } == PackageManager.PERMISSION_GRANTED

    fun requestPermission(fragment: Fragment) {
        if (!haveStoragePermission()) {
            val permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            fragment.requestPermissions(permissions, READ_EXTERNAL_STORAGE_REQUEST)
        }
    }

    fun detectScreenshots(fragment: Fragment) {
        if (haveStoragePermission()) {
            start()
        } else {
            requestPermission(fragment)
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        grantResults: IntArray
    ) {
        when (requestCode) {
            READ_EXTERNAL_STORAGE_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    start()
                }
                return
            }
        }
    }

    private fun logError(exceptionMessage:String?){
        exceptionMessage?.let {
            if(!TextUtils.isEmpty(exceptionMessage)) {
                val messageMap: Map<String, String> =
                    mapOf(
                        LABEL_TYPE to LABEL_ERROR,
                        LABEL_REASON to exceptionMessage
                    )
                ServerLogger.log(Priority.P2, TAG_SS_ERR, messageMap)
            }
        }
    }

    companion object {
        //permission request code
        const val READ_EXTERNAL_STORAGE_REQUEST = 500
        const val TAG_SS_ERR = "SCREENSHOT_ERROR"
        const val LABEL_TYPE = "type"
        const val LABEL_ERROR = "error"
        const val LABEL_REASON = "reason"
    }
}