package com.tokopedia.promotionstarget.cm.lifecycle

import android.app.Activity
import android.os.Bundle
import com.tokopedia.promotionstarget.cm.ActivityProvider
import com.tokopedia.promotionstarget.presentation.subscriber.BaseApplicationLifecycleCallbacks
import java.lang.ref.WeakReference

class ActivityProviderImpl : BaseApplicationLifecycleCallbacks,ActivityProvider {
    private var weakActivity: WeakReference<Activity?>? = null

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        super.onActivityCreated(activity, savedInstanceState)
        weakActivity = WeakReference(activity)
    }

    override fun onActivityStarted(activity: Activity) {
        super.onActivityStarted(activity)
        weakActivity = WeakReference(activity)
    }

    override fun onActivityDestroyed(activity: Activity) {
        super.onActivityDestroyed(activity)
        clearCurrentActivity(activity)
    }

    private fun clearCurrentActivity(activity: Activity) {
        if (weakActivity != null) {
            val name = weakActivity?.get()?.javaClass?.simpleName ?: ""
            if (name.equals(activity.javaClass.simpleName, ignoreCase = true)) {
                weakActivity?.clear()
            }
        }
    }

    override fun getActivity(): WeakReference<Activity?>? {
        return weakActivity
    }
}