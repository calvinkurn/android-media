package com.tokopedia.notifications.inApp

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import com.tokopedia.promotionstarget.domain.presenter.GratifCancellationExceptionType
import java.lang.ref.WeakReference
import java.util.*

class CmActivityLifecycleHandler(val applicationCallback: CmActivityApplicationCallback,
                                 val pushIntentHandler: PushIntentHandler,
                                 val broadcastHandler: BroadcastHandler,
                                 val callback: ShowInAppCallback,
                                 val weakHashMap: WeakHashMap<Activity, Boolean>) {

    var currentWeakActivity: WeakReference<Activity>? = null
    private set

    //todo create arraylist of screens and loop it
    private val DISCO_PAGE_ACTIVITY_NAME = "com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity"

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

        val className = activity.javaClass.name
        if (className == DISCO_PAGE_ACTIVITY_NAME) {
            broadcastHandler.registerBroadcastManager(activity)
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
        broadcastHandler.onActivityStop(activity)
        cancelJob(activity.hashCode(), GratifCancellationExceptionType.ACTIVITY_STOP)
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

    private fun cancelJob(entityHashCode: Int, @GratifCancellationExceptionType reason: String) {
        callback.cancelGratifJob(entityHashCode, reason)
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
                return@let
            }
        }
        return null
    }

    interface CmActivityApplicationCallback {
        fun getApplication(): Application?
        fun setApplication(application: Application)
    }
}