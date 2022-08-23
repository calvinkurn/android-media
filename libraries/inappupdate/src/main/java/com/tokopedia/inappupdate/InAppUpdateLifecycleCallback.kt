package com.tokopedia.inappupdate

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tokopedia.inappupdate.AppUpdateManagerWrapper.checkUpdateInFlexibleProgressOrCompleted
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.lang.ref.WeakReference
import kotlin.coroutines.resume

class InAppUpdateLifecycleCallback(
    val onCheckAppUpdateRemoteConfig: ((activity: Activity, cont: CancellableContinuation<Unit>, (Boolean) -> (Unit)) -> (Unit))
) : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        checkAppUpdateAndInApp(activity)
    }

    fun checkAppUpdateAndInApp(activity: Activity) {
        if (activity is AppCompatActivity) {
            activity.lifecycleScope.launch(Dispatchers.IO) {
                suspendCancellableCoroutine { cont ->
                    try {
                        val activityReference: WeakReference<Activity> = WeakReference(activity)
                        checkUpdateInFlexibleProgressOrCompleted(activity) { isOnProgress: Boolean ->
                            if (!isOnProgress) {
                                checkAppUpdateRemoteConfig(activityReference, cont)
                            }
                        }
                    } catch (ignored: Exception) {
                        cont.resume(Unit)
                    }
                }
            }
        }
    }

    private fun checkAppUpdateRemoteConfig(
        activityReference: WeakReference<Activity>,
        cont: CancellableContinuation<Unit>
    ) {
        val activity: Activity? = activityReference.get()
        if (activity != null && !activity.isFinishing) {
            onCheckAppUpdateRemoteConfig.invoke(activity, cont) { onNeedUpdate ->
                if (!onNeedUpdate) {
                    activity.application.unregisterActivityLifecycleCallbacks(this)
                }
            }
        } else {
            cont.resume(Unit)
        }
    }

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}
}