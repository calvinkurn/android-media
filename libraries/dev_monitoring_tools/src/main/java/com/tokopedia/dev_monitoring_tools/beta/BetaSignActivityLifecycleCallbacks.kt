package com.tokopedia.dev_monitoring_tools.beta

import android.R
import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import com.tokopedia.grapqhl.beta.notif.BetaInterceptor.Companion.isBeta

class BetaSignActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) { // No-op
    }

    override fun onActivityStarted(activity: Activity) { // No-op
    }

    override fun onActivityResumed(activity: Activity) {
        if (Build.VERSION.SDK_INT >= 21 && isBeta(activity)) {
            val window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = activity.resources.getColor(R.color.holo_red_dark)
        }
    }

    override fun onActivityPaused(activity: Activity) { // No-op
    }

    override fun onActivityStopped(activity: Activity) { // No-op
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) { // No-op
    }

    override fun onActivityDestroyed(activity: Activity) { // No-op
    }
}