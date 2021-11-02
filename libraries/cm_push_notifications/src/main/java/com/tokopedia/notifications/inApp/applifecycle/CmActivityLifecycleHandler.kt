package com.tokopedia.notifications.inApp.applifecycle

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.inApp.CMInAppManager
import com.tokopedia.notifications.inApp.external.PushIntentHandler
import java.lang.ref.WeakReference

class CmActivityLifecycleHandler(val applicationCallback: CmActivityApplicationCallback,
                                 val pushIntentHandler: PushIntentHandler,
                                 val callback: ShowInAppCallback,
                                 val cmInAppManager: CMInAppManager) {

    var currentWeakActivity: WeakReference<Activity>? = null
        private set

    fun onNewIntent(activity: Activity, intent: Intent?) {
        try {
            checkApplication(activity)
            updateCurrentActivity(activity)
            if (intent != null && intent.extras != null) {
                pushIntentHandler.isHandledByPush =
                    pushIntentHandler.processPushIntent(activity, intent.extras)
            }
        } catch (t: Throwable) {
            ServerLogger.log(
                Priority.P2, "CM_VALIDATION",
                mapOf(
                    "type" to "exception",
                    "err" to Log.getStackTraceString(t).take(CMConstant.TimberTags.MAX_LIMIT),
                    "data" to ""
                )
            )
        }
    }

    fun onActivityCreatedInternalForPush(activity: Activity) {
        try {
            checkApplication(activity)
            updateCurrentActivity(activity)

            //only push flow
            var finalBundle: Bundle? = null
            val intent = activity.intent
            if (intent != null) {
                finalBundle = intent.extras
            }
            pushIntentHandler.isHandledByPush =
                    pushIntentHandler.processPushIntent(activity, finalBundle)
        } catch (t: Throwable) {
            ServerLogger.log(
                    Priority.P2, "CM_VALIDATION",
                    mapOf(
                            "type" to "exception",
                            "err" to Log.getStackTraceString(t).take(CMConstant.TimberTags.MAX_LIMIT),
                            "data" to ""
                    )
            )
        }
    }

    fun onActivityStartedInternal(activity: Activity) {
        checkApplication(activity)
        updateCurrentActivity(activity)
        if (!pushIntentHandler.isHandledByPush) {
            showInApp(activity.javaClass.name, activity.hashCode())
        }
    }

    fun onActivityStopInternal(activity: Activity) {
        clearCurrentActivity(activity)
        pushIntentHandler.isHandledByPush = false
    }

    fun onActivityDestroyedInternal(activity: Activity) {
        cmInAppManager.onDialogDismiss(activity)
    }

    private fun clearCurrentActivity(activity: Activity) {
        currentWeakActivity?.let {
            if (activity == it.get()) {
                it.clear()
            }
        }
    }

    private fun updateCurrentActivity(activity: Activity) {
        currentWeakActivity = WeakReference(activity)
    }

    private fun checkApplication(activity: Activity) {
        if (applicationCallback.getApplication() == null) {
            applicationCallback.setApplication(activity.application)
        }
    }

    private fun showInApp(name: String, entityHashCode: Int) {
        if (callback.canShowDialog()) {
            callback.showInAppForScreen(name, entityHashCode, true)
        }
    }

    fun getCurrentActivity(): Activity? {
        val activity = currentWeakActivity?.get()
        activity?.let {
            return if (it.isFinishing) {
                null
            } else {
                it
            }
        }
        return null
    }

    fun onFirstScreenOpen(activity: WeakReference<Activity>) {
        applicationCallback.onFirstScreenOpen(activity)
    }

    interface CmActivityApplicationCallback {
        fun getApplication(): Application?
        fun setApplication(application: Application)
        fun onFirstScreenOpen(activity: WeakReference<Activity>)
    }
}