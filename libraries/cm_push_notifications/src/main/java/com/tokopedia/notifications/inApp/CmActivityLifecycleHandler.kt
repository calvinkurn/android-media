package com.tokopedia.notifications.inApp

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.notifications.common.CMConstant
import timber.log.Timber
import java.lang.ref.WeakReference

class CmActivityLifecycleHandler(val applicationCallback: CmActivityApplicationCallback,
                                 val pushIntentHandler: PushIntentHandler,
                                 val callback: ShowInAppCallback,
                                 val dialogVisibilityContract: CmDialogVisibilityContract) {

    var currentWeakActivity: WeakReference<Activity>? = null
        private set

    fun onNewIntent(activity: Activity, intent: Intent?) {
        try {
            checkApplication(activity)
            updateCurrentActivity(activity)
            if (intent != null && intent.extras != null) {
                pushIntentHandler.isHandledByPush = pushIntentHandler.processPushIntent(activity, intent.extras)
            }
        } catch (t: Throwable) {
            ServerLogger.log(Priority.P2, "CM_VALIDATION",
                    mapOf("type" to "exception",
                            "err" to Log.getStackTraceString(t).take(CMConstant.TimberTags.MAX_LIMIT),
                            "data" to ""
                    ))
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
            pushIntentHandler.isHandledByPush = pushIntentHandler.processPushIntent(activity, finalBundle)
        } catch (t: Throwable) {
            ServerLogger.log(Priority.P2, "CM_VALIDATION",
                    mapOf("type" to "exception",
                            "err" to Log.getStackTraceString(t).take(CMConstant.TimberTags.MAX_LIMIT),
                            "data" to ""
                    ))
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
        dialogVisibilityContract.onDialogDismiss(activity)
    }

    private fun clearCurrentActivity(activity: Activity) {
        if (currentWeakActivity != null) {
            val name = currentWeakActivity?.get()?.javaClass?.simpleName ?: ""
            if (name.equals(activity.javaClass.simpleName, ignoreCase = true)) {
                currentWeakActivity?.clear()
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
            if (it.isFinishing) {
                return null
            } else {
                return it
            }
        }
        return null
    }

    interface CmActivityApplicationCallback {
        fun getApplication(): Application?
        fun setApplication(application: Application)
    }
}