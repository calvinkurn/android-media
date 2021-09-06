package com.tokopedia.dev_monitoring_tools.session

import android.app.Activity
import android.os.Bundle
import android.os.Process
import com.tokopedia.dev_monitoring_tools.userjourney.UserJourney
import com.tokopedia.kotlin.extensions.view.formattedToMB
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.utils.network.NetworkTrafficUtils
import java.util.*

class SessionDataUsageLogger constructor(
    private val priority: Priority,
    private val sessionName: String,
    private val dataUsageName: String,
    private val intervalSession: Long
) {

    private val additionalData: MutableMap<String, String> = mutableMapOf()

    var lastSessionMillis: Long = -1
    var sessionCount: Long = 0
    var openedPageCount = 0
    var openedPageCountTotal = 0
    var firstSumTx: Long = 0
    var firstSumRx: Long = 0
    var lastSumTx: Long = 0
    var lastSumRx: Long = 0
    var running = false
    var returnFromOtherActivity = false

    fun addLogItem(key: String, value: String) {
        additionalData[key] = value
    }

    fun addLogItems(data: Map<String, String>) {
        additionalData.putAll(data)
    }

    fun addJourney(activity: Activity) {
        var bundle = Bundle()

        try {
            activity.intent.extras?.let {
                bundle = it
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        UserJourney.addJourneyActivity(activity.javaClass.simpleName, bundle)
    }

    fun checkSession(activityName: String, connectionType: String?) {
        running = true

        val currentMillis = System.currentTimeMillis()
        val minSessionTimeMillis = currentMillis - intervalSession

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
        ServerLogger.log(
            priority, sessionName, mapOf(
            "type" to activityName,
            "count" to sessionCount.toString(),
            "open_page_total" to openedPageCountTotal.toString(),
            "open_page" to openedPageCount.toString(),
            "diff_time" to getDiffDuration(lastSessionMillis, currentMillis)
        ))
    }

    private fun logDataUsage(activityName: String, connectionType: String?, currentMillis: Long) {
        val uid = Process.myUid()
        val bootTx = NetworkTrafficUtils.getUidTxBytes(uid)
        val bootRx = NetworkTrafficUtils.getUidRxBytes(uid)
        val bootNetwork = bootTx + bootRx

        if (bootTx <= 0 || bootRx <= 0) return

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

        val dataMap = mutableMapOf(
            "type" to activityName,
            "count" to sessionCount.toString(),
            "open_page_total" to openedPageCountTotal.toString(),
            "open_page" to openedPageCount.toString(),
            "diff_time" to getDiffDuration(lastSessionMillis, currentMillis),
            "conn_info" to connectionType.orEmpty(),
            "net" to network.formattedToMB(),
            "tx" to diffTx.formattedToMB(),
            "rx" to diffRx.formattedToMB(),
            "sum_net" to sumNetwork.formattedToMB(),
            "sum_tx" to sumDiffTx.formattedToMB(),
            "sum_rx" to sumDiffRx.formattedToMB(),
            "boot_net" to bootNetwork.formattedToMB(),
            "boot_tx" to bootTx.formattedToMB(),
            "boot_rx" to bootRx.formattedToMB()
        )

        // add an additional data
        dataMap.putAll(additionalData)

        ServerLogger.log(priority, dataUsageName, dataMap)
    }

    private fun updateLastSumTraffic(currentSumTx: Long, currentSumRx: Long) {
        lastSumTx = currentSumTx
        lastSumRx = currentSumRx
    }

    private fun getDiffDuration(startDuration: Long, stopDuration: Long): String {
        var diffTimeInMillis = 0f

        if (startDuration in 1 until stopDuration) {
            diffTimeInMillis = (stopDuration - startDuration).toFloat()
            diffTimeInMillis /= intervalSession.toFloat()
        }

        return try {
            String.format(Locale.ENGLISH, TIME_FORMAT, diffTimeInMillis)
        } catch (e: Exception) {
            diffTimeInMillis.toString()
        }
    }

    companion object {
        private const val TIME_FORMAT = "%.2f"
    }
}