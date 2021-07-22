package com.tokopedia.dev_monitoring_tools.session

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.tokopedia.device.info.DeviceConnectionInfo.getConnectionType
import java.util.concurrent.TimeUnit

class SessionActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

    private val dataUsageLogging by lazy { SessionDataUsageLogger(
        sessionName = "ACTIVE_SESSION",
        dataUsageName = "DATA_USAGE",
        intervalSession = INTERVAL_SESSION
    ) }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        dataUsageLogging.openedPageCount++
        dataUsageLogging.openedPageCountTotal++
        dataUsageLogging.addJourney(activity)
    }

    override fun onActivityStarted(activity: Activity) { // No-op
    }

    override fun onActivityResumed(activity: Activity) {
        if (dataUsageLogging.returnFromOtherActivity) {
            dataUsageLogging.addJourney(activity)
        }
        if (dataUsageLogging.running) {
            return
        }
        dataUsageLogging.running = true
        val connectionType = getConnectionType(activity)
        Thread {
            dataUsageLogging.checkSession(activity.javaClass.simpleName, connectionType)
        }.start()
    }

    override fun onActivityPaused(activity: Activity) { // No-op
        dataUsageLogging.returnFromOtherActivity = true
    }

    override fun onActivityStopped(activity: Activity) { // No-op
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) { // No-op
    }

    override fun onActivityDestroyed(activity: Activity) { // No-op
    }

    companion object {
        private val INTERVAL_SESSION = TimeUnit.MINUTES.toMillis(1)
    }

}