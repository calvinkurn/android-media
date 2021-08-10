package com.tokopedia.screenshot_observer

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.screenshot_observer.R
import com.tokopedia.config.GlobalConfig
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.bottomsheet_action_screenshot.view.*


open class Screenshot @JvmOverloads constructor(contentResolver: ContentResolver, protected open val listener: BottomSheetListener? = null,
                      protected open val toasterSellerListener: ToasterSellerListener? = null) : Application.ActivityLifecycleCallbacks, ScreenshotObserver.Listener {
    private val mHandlerThread: HandlerThread = HandlerThread("ScreenshotObserver")
    private val mHandler: Handler
    private val mContentResolver: ContentResolver
    private val mContentObserver: ContentObserver
    private var currentActivity: Activity? = null
    private var savedUri: Uri? = null
    private var className: String = ""
    private val requiredPermissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)

    init {
        mHandlerThread.start()
        mHandler = Handler(mHandlerThread.looper)
        mContentResolver = contentResolver
        mContentObserver = ScreenshotObserver(mHandler, contentResolver, this)
    }

    fun register() {
        mContentResolver.registerContentObserver(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                true,
                mContentObserver
        )
    }

    private fun allPermissionsGranted(activity: Activity): Boolean {
        for (permission in requiredPermissions) {
            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun openBottomSheetFeedback(activity: Activity?, uri: Uri?, className: String) {
        if (activity == null) {
            return
        }

        val bottomSheetFeedback = BottomSheetUnify()
        val viewBottomSheet = View.inflate(activity, R.layout.bottomsheet_action_screenshot, null).apply {
            btn_add_feedback.setOnClickListener {
                listener?.onFeedbackClicked(uri, className, true)
                bottomSheetFeedback.dismiss()
            }
            btn_dismiss.setOnClickListener {
                bottomSheetFeedback.dismiss()
            }
        }

       bottomSheetFeedback.apply {
           setChild(viewBottomSheet)
        }

        val fm = (activity as AppCompatActivity).supportFragmentManager
        bottomSheetFeedback.show(fm, "")
    }

    fun unregister() {
        mContentResolver.unregisterContentObserver(mContentObserver)
    }

    interface BottomSheetListener {
        fun onFeedbackClicked(uri: Uri?, className: String, isFromScreenshot: Boolean)
    }

    interface ToasterSellerListener {
        fun showToaster(uri: Uri?, currentActivity: Activity?)
    }

    override fun onActivityPaused(activity: Activity) {
        unregister()
        currentActivity = null
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityResumed(activity: Activity) {
        register()
        currentActivity = activity
        className = activity.localClassName
    }

    override fun onScreenShotTaken(uri: Uri) {
        savedUri = uri
        currentActivity?.let {
            if (!allPermissionsGranted(it) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (GlobalConfig.isSellerApp()) {
                    toasterSellerListener?.showToaster(uri, it)
                } else {
                    openBottomSheetFeedback(it, uri, className)
                    ScreenshotAnalytics.eventUseScreenshot()
                }
            } else {
                handleItem(uri)
            }
        }
    }

    private fun isFileScreenshot(fileName: String): Boolean {
        return fileName.toLowerCase().startsWith(FILE_NAME_PREFIX)
    }

    private fun handleItem(uri: Uri) {
        var cursor: Cursor? = null
        try {
            cursor = mContentResolver.query(uri, PROJECTION, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val Name = generateScreenshotDataFromCursor(cursor)
                if (Name != null && currentActivity != null) {
                    if (GlobalConfig.isSellerApp()) {
                        toasterSellerListener?.showToaster(uri, currentActivity)
                    } else {
                        openBottomSheetFeedback(currentActivity, uri, className)
                        ScreenshotAnalytics.eventUseScreenshot()
                    }
                }
            }
        } finally {
            cursor?.close()
        }
    }

    private fun generateScreenshotDataFromCursor(cursor: Cursor): String? {
        val fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
        return if (isFileScreenshot(fileName)) {
            fileName
        } else {
            null
        }
    }

    companion object {
        private val FILE_NAME_PREFIX = "screenshot"
        private val PROJECTION = arrayOf(
                MediaStore.Images.Media.DISPLAY_NAME
        )
    }

}