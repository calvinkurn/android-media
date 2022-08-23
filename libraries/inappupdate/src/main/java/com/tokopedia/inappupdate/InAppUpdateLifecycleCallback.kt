package com.tokopedia.inappupdate

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tokopedia.inappupdate.AppUpdateManagerWrapper.checkUpdateInFlexibleProgressOrCompleted
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class InAppUpdateLifecycleCallback(
    val onCheckAppUpdateRemoteConfig: ((activity: Activity, (Boolean) -> (Unit)) -> (Unit))
) : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        checkAppUpdateAndInApp(activity)
    }

    fun checkAppUpdateAndInApp(activity: Activity) {
        if (activity is AppCompatActivity) {
            activity.lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val activityReference: WeakReference<Activity> = WeakReference(activity)
                    checkUpdateInFlexibleProgressOrCompleted(activity) { isOnProgress: Boolean ->
                        if (!isOnProgress) {
                            checkAppUpdateRemoteConfig(activityReference)
                        }
                    }
                } catch (ignored: Exception) {
                }
            }
        }
    }

    private fun checkAppUpdateRemoteConfig(
        activityReference: WeakReference<Activity>
    ) {
        val activity: Activity? = activityReference.get()
        if (activity != null && !activity.isFinishing) {
            onCheckAppUpdateRemoteConfig.invoke(activity) { onNeedUpdate ->
                if (!onNeedUpdate) {
                    activity.application.unregisterActivityLifecycleCallbacks(this)
                }
            }
        }
    }

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}
}