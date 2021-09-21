package com.tokopedia.notifications.inApp

import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.notifications.R
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import com.tokopedia.notifications.inApp.viewEngine.BannerView
import com.tokopedia.notifications.inApp.viewEngine.ViewEngine
import timber.log.Timber
import java.lang.ref.WeakReference

class CmDialogHandler {

    /**
     * dialog for interstitial and interstitialImg
     * */
    fun interstitialDialog(weakActivity: WeakReference<Activity>?, data: CMInApp, cmDialogHandlerCallback: CmDialogHandlerCallback) {
        val activity = weakActivity?.get() ?: return
        try {
            // show interstitial banner
            BannerView.create(activity, data)

            // set flag if has dialog showing
            cmDialogHandlerCallback.onShow(activity)
        } catch (e: Exception) {
            ServerLogger.log(Priority.P2, "CM_VALIDATION",
                    mapOf("type" to "exception",
                            "err" to Log.getStackTraceString(e).take(CMConstant.TimberTags.MAX_LIMIT),
                            "data" to data.toString().take(CMConstant.TimberTags.MAX_LIMIT)
                    ))
            cmDialogHandlerCallback.onException(e, data)
        }
    }

    fun showLegacyDialog(weakActivity: WeakReference<Activity>?, cmInApp: CMInApp, cmDialogHandlerCallback: CmDialogHandlerCallback) {
        val activity = weakActivity?.get() ?: return
        val viewEngine = ViewEngine(activity)
        val view = viewEngine.createView(cmInApp) ?: return
        val inAppViewPrev = activity.findViewById<View>(R.id.mainContainer)
        //In-App view already present on Activity
        if (null != inAppViewPrev) return
        val root = activity.window
                .decorView
                .findViewById<View>(android.R.id.content)
                .rootView as FrameLayout
        root.addView(view)
        cmDialogHandlerCallback.onShow(activity)
    }

    interface CmDialogHandlerCallback {
        fun onShow(activity: Activity)
        fun onException(e: Exception, data: CMInApp)
    }

    abstract class AbstractCmDialogHandlerCallback : CmDialogHandlerCallback {
        override fun onShow(activity: Activity) {}
        override fun onException(e: Exception, data: CMInApp) {}
    }
}