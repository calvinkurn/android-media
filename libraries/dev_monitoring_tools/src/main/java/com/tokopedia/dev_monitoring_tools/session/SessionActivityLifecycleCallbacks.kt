package com.tokopedia.dev_monitoring_tools.session

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Process
import com.tokopedia.device.info.DeviceConnectionInfo.getConnectionType
import com.tokopedia.utils.network.NetworkTrafficUtils.getUidRxBytes
import com.tokopedia.utils.network.NetworkTrafficUtils.getUidTxBytes
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

class SessionActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
    private var lastSessionMillis: Long = -1
    private var sessionCount: Long = 0
    private var openedPageCount = 0
    private var openedPageCountTotal = 0
    private var firstSumTx: Long = 0
    private var firstSumRx: Long = 0
    private var lastSumTx: Long = 0
    private var lastSumRx: Long = 0
    private var running = false
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        openedPageCount++
        openedPageCountTotal++
    }

    override fun onActivityStarted(activity: Activity) { // No-op
    }

    override fun onActivityResumed(activity: Activity) {
        UserJourney.addJourneyActivity(activity.javaClass.simpleName)
        if (running) {
            return
        }
        running = true
        val connectionType = getConnectionType(activity)
        Thread(Runnable { checkSession(activity.javaClass.simpleName, connectionType) }).start()
    }

    override fun onActivityPaused(activity: Activity) { // No-op
    }

    override fun onActivityStopped(activity: Activity) { // No-op
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) { // No-op
    }

    override fun onActivityDestroyed(activity: Activity) { // No-op
    }

    private fun checkSession(activityName: String, connectionType: String?) {
        running = true
        val currentMillis = System.currentTimeMillis()
        val minSessionTimeMillis = currentMillis - INTERVAL_SESSION
        if (lastSessionMillis < minSessionTimeMillis) {
            sessionCount++
            logSession(activityName, currentMillis)
            logDataUsage(activityName, connectionType, currentMillis)
            lastSessionMillis = System.currentTimeMillis()
            openedPageCount = 0
        }
        running = false
    }

    private fun logSession(activityName: String, currentMillis: Long) {
        Timber.w("P1#ACTIVE_SESSION#%s;count=%s;open_page_total=%s;open_page=%s;diff_time=%s",
                activityName, sessionCount, openedPageCountTotal, openedPageCount, getDiffDuration(lastSessionMillis, currentMillis))
    }

    private fun getDiffDuration(startDuration: Long, stopDuration: Long): String {
        var diffTimeInMillis = 0f
        if (startDuration in 1 until stopDuration) {
            diffTimeInMillis = (stopDuration - startDuration).toFloat()
            diffTimeInMillis /= INTERVAL_SESSION.toFloat()
        }
        return String.format(Locale.ENGLISH, TIME_FORMAT, diffTimeInMillis)
    }

    private fun logDataUsage(activityName: String, connectionType: String?, currentMillis: Long) {
        val uid = Process.myUid()
        val bootTx = getUidTxBytes(uid)
        val bootRx = getUidRxBytes(uid)
        val bootNetwork = bootTx + bootRx
        if (bootTx <= 0 || bootRx <= 0) {
            return
        }
        if (firstSumTx <= 0 || firstSumRx <= 0) {
            firstSumTx = bootTx
            firstSumRx = bootRx
        }
        if (lastSumTx <= 0 || lastSumRx <= 0) {
            updateLastSumTraffic(bootTx, bootRx)
        }
        val diffTx = bootTx - lastSumTx
        val diffRx = bootRx - lastSumRx
        val network = diffTx + diffRx
        val sumDiffTx = bootTx - firstSumTx
        val sumDiffRx = bootRx - firstSumRx
        val sumNetwork = sumDiffTx + sumDiffRx
        updateLastSumTraffic(bootTx, bootRx)
        Timber.w("P1#DATA_USAGE#%s;count=%s;open_page_total=%s;open_page=%s;diff_time=%s;conn_info='%s';net=%s;tx=%s;rx=%s;sum_net=%s;sum_tx=%s;sum_rx=%s;boot_net=%s;boot_tx=%s;boot_rx=%s",
                activityName, sessionCount, openedPageCountTotal, openedPageCount, getDiffDuration(lastSessionMillis, currentMillis),
                connectionType,
                getFormattedMBSize(network), getFormattedMBSize(diffTx), getFormattedMBSize(diffRx),
                getFormattedMBSize(sumNetwork), getFormattedMBSize(sumDiffTx), getFormattedMBSize(sumDiffRx),
                getFormattedMBSize(bootNetwork), getFormattedMBSize(bootTx), getFormattedMBSize(bootRx))
    }

    private fun updateLastSumTraffic(currentSumTx: Long, currentSumRx: Long) {
        lastSumTx = currentSumTx
        lastSumRx = currentSumRx
    }

    private fun getFormattedMBSize(sizeInByte: Long): String {
        val sizeInMB = sizeInByte.toFloat() / MB_SIZE
        return String.format(Locale.ENGLISH, SIZE_FORMAT, sizeInMB)
    }

    companion object {
        private const val TIME_FORMAT = "%.2f"
        private val INTERVAL_SESSION = TimeUnit.MINUTES.toMillis(1)
        private const val SIZE_FORMAT = "%.2f"
        private const val MB_SIZE: Long = 1000000
    }
}