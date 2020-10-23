package com.tokopedia.notifications.inApp

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import java.lang.ref.WeakReference
import java.util.*

class CmActivityLifecycleHandler(val applicationCallback: CmActivityApplicationCallback,
                                 val pushIntentHandler: PushIntentHandler,
                                 val callback: ShowInAppCallback,
                                 val weakHashMap: WeakHashMap<Activity, Boolean>) {

    var currentWeakActivity: WeakReference<Activity>? = null
        private set

    fun onNewIntent(activity: Activity, intent: Intent?) {
        checkApplication(activity)
        updateCurrentActivity(activity)
        if (intent != null && intent.extras != null) {
            pushIntentHandler.checkPushIntent(activity, intent.extras)
        }
    }

    fun onActivityCreatedInternalForPush(activity: Activity) {
        checkApplication(activity)
        updateCurrentActivity(activity)

        var finalBundle: Bundle? = null
        val intent = activity.intent
        if (intent != null) {
            finalBundle = intent.extras
        }

        pushIntentHandler.checkPushIntent(activity, finalBundle)
    }

    fun onActivityStartedInternal(activity: Activity) {
        checkApplication(activity)
        updateCurrentActivity(activity)
        showInApp(activity.javaClass.name, activity.hashCode())
    }

    fun onActivityStopInternal(activity: Activity) {
        clearCurrentActivity(activity)
    }

    fun onActivityDestroyedInternal(activity: Activity) {
        weakHashMap.remove(activity)
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
            callback.showInAppForScreen(name, entityHashCode)
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