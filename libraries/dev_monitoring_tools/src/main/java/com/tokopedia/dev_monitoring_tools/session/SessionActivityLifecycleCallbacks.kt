package com.tokopedia.dev_monitoring_tools.session

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.tokopedia.device.info.DeviceConnectionInfo.getConnectionType
import java.util.concurrent.TimeUnit

class SessionActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

    private val logger by lazy { SessionDataUsageLogger(
        sessionName = "ACTIVE_SESSION",
        dataUsageName = "DATA_USAGE",
        intervalSession = INTERVAL_SESSION
    ) }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        logger.openedPageCount++
        logger.openedPageCountTotal++
        logger.addJourney(activity)
    }

    override fun onActivityStarted(activity: Activity) { // No-op
    }

    override fun onActivityResumed(activity: Activity) {
        if (logger.returnFromOtherActivity) {
            logger.addJourney(activity)
        }
        if (logger.running) {
            return
        }
        logger.running = true
        val connectionType = getConnectionType(activity)
        Thread {
            logger.checkSession(activity.javaClass.simpleName, connectionType)
        }.start()
    }

    override fun onActivityPaused(activity: Activity) { // No-op
        logger.returnFromOtherActivity = true
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