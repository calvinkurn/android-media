package com.tokopedia.screenshot_observer

import android.app.Activity
import android.app.Application
import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.screenshot_observer.R
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.config.GlobalConfig
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottomsheet_action_screenshot.view.*


open class Screenshot(contentResolver: ContentResolver, listener: BottomSheetListener) : BaseSimpleActivity() ,Application.ActivityLifecycleCallbacks, ScreenshotObserver.Listener {
    private val mHandlerThread: HandlerThread = HandlerThread("ScreenshotObserver")
    private val mHandler: Handler
    private val mContentResolver: ContentResolver
    private val mContentObserver: ContentObserver
    private val mListener: BottomSheetListener
    private var currentActivity: Activity? = null
    private var className: String = ""

    init {
        mHandlerThread.start()
        mHandler = Handler(mHandlerThread.looper)
        mContentResolver = contentResolver
        mContentObserver = ScreenshotObserver(mHandler, this)
        mListener = listener
    }

    fun register() {
        mContentResolver.registerContentObserver(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                true,
                mContentObserver
        )
    }


    fun openBottomSheetFeedback(activity: Activity, uri: Uri, className: String) {
        val bottomSheetFeedback = BottomSheetUnify()
        val viewBottomSheet = View.inflate(activity, R.layout.bottomsheet_action_screenshot, null).apply {
            btn_add_feedback.setOnClickListener {
                mListener.onFeedbackClicked(uri, className)
                bottomSheetFeedback.dismiss()
            }
            btn_dismiss.setOnClickListener {
                bottomSheetFeedback.dismiss()
            }
        }

       bottomSheetFeedback.apply {
            setChild(viewBottomSheet)
            setOnDismissListener {
                dismiss()
            }
        }

        val fm = (activity as AppCompatActivity).supportFragmentManager
        bottomSheetFeedback.show(fm, "")
    }

    fun unregister() {
        mContentResolver.unregisterContentObserver(mContentObserver)
    }

    interface BottomSheetListener {
        fun onFeedbackClicked(uri: Uri?, className: String)
    }

    override fun onActivityPaused(activity: Activity) {
        if (!GlobalConfig.isSellerApp()) {
            unregister()
            currentActivity = null
            className = ""
        }
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
        if (!GlobalConfig.isSellerApp()) {
            register()
            currentActivity = activity
            className = activity.localClassName
        }
    }

    override fun onScreenShotTaken(uri: Uri) {
        currentActivity?.let { openBottomSheetFeedback(it, uri, className) }
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

}