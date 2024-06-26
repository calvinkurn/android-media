package com.tokopedia.test.application.assertion.topads

import android.content.Context
import android.os.Environment
import com.tokopedia.analyticsdebugger.database.STATUS_MATCH
import com.tokopedia.analyticsdebugger.database.TopAdsLogDB
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object TopAdsVerificationTestReportUtil {
    private val FILE_TOPADS_REPORT_ROOT = "topads_verificator_data"
    private val FILE_TOPADS_LOG = "report-topads-verificator-log.log"
    private val FILE_TOPADS_VERIFICATOR_DATA = "report-topads-verificator.csv"
    val REPORT_HEADER = "Component Name,Impression Match,Click Match"
    val FAILED_URL_HEADER = "Component Name,Failed Url,Status"
    val EVENT_IMPRESSION = "impression"
    val EVENT_CLICK = "click"

    fun writeTopAdsVerificatorLog(context: Context, message: String) {
        val path = context.getExternalFilesDir(null)
        val topadsVerificatorData = File(path, FILE_TOPADS_REPORT_ROOT)
        makeInitialLogDir(topadsVerificatorData)
        val logTopAdsFile = File(topadsVerificatorData, FILE_TOPADS_LOG)
        logTopAdsFile.appendText( "${message}\n")
    }

    fun deleteTopAdsVerificatorReportData(context: Context) {
        val path = context.getExternalFilesDir(null)
        val topadsRootDir = File(path, FILE_TOPADS_REPORT_ROOT)
        val topadsLogData = File(topadsRootDir, FILE_TOPADS_LOG)
        val topadsVerificatorData = File(topadsRootDir, FILE_TOPADS_VERIFICATOR_DATA)

        topadsLogData.delete()
        topadsVerificatorData.delete()
        topadsRootDir.delete()

        topadsVerificatorData.delete()
    }

    private fun makeInitialLogDir(topadsDataDir: File) {
        if (!topadsDataDir.exists()) {
            topadsDataDir.mkdirs()
        }

        val perfReportPlt = File(topadsDataDir, FILE_TOPADS_LOG)
        perfReportPlt.appendText("")
    }


    private fun makeInitialReportDir(topadsDataDir: File) {
        val timestamp = "Timestamp"
        val sourceName = "Source Name"
        val eventType = "Type"
        val eventStatus = "Status"
        val url = "Url"

        topadsDataDir.mkdirs()
        val perfReportPlt = File(topadsDataDir, FILE_TOPADS_VERIFICATOR_DATA)
        perfReportPlt.appendText("" +
                "$timestamp," +
                "$sourceName," +
                "$eventType," +
                "$eventStatus," +
                "$url\n")
    }

    fun writeTopAdsVerificatorReportCoverage(callerClass: String, topAdsLogDBList: List<TopAdsLogDB>) {
        val reportList = generateTopAdsVerificatorReportCoverage(topAdsLogDBList)

        createTopAdsCoverageReportFile(callerClass, reportList)
    }

    fun generateTopAdsVerificatorReportCoverage(topAdsLogDBList: List<TopAdsLogDB>): List<String> {
        if (topAdsLogDBList.isEmpty()) return listOf()

        val topAdsVerificatorDataList = topAdsLogDBList.groupBy { it.componentOrSource }

        var totalImpressionSuccess = 0
        var totalImpressionCount = 0
        var totalClickSuccess = 0
        var totalClickCount = 0

        val topAdsCoverageReportList = mutableListOf<TopAdsCoverageReport>()

        topAdsVerificatorDataList.forEach {
            val topAdsCoverageReport = mapToTopAdsCoverageReport(it.key, it.value)

            topAdsCoverageReportList.add(topAdsCoverageReport)

            totalImpressionSuccess += topAdsCoverageReport.impressionSuccess
            totalImpressionCount += topAdsCoverageReport.impressionCount
            totalClickSuccess += topAdsCoverageReport.clickSuccess
            totalClickCount += topAdsCoverageReport.clickCount
        }

        val totalImpressionCoverage = getSuccessPercentage(totalImpressionSuccess, totalImpressionCount)
        val totalClickCoverage = getSuccessPercentage(totalClickSuccess, totalClickCount)

        return constructSuccessRateReport(topAdsCoverageReportList, totalImpressionCoverage, totalClickCoverage) +
                constructFailedUrlReport(topAdsLogDBList)
    }

    private val TopAdsLogDB.componentOrSource: String
        get() = if (componentName.isEmpty()) sourceName else componentName

    private fun constructSuccessRateReport(
            topAdsCoverageReportList: MutableList<TopAdsCoverageReport>,
            totalImpressionCoverage: String,
            totalClickCoverage: String
    ): List<String> {
        return mutableListOf<String>().apply {
            add(REPORT_HEADER)
            addAll(topAdsCoverageReportList.map { it.generateReport() })
            add(",$totalImpressionCoverage,$totalClickCoverage")
        }
    }

    private fun constructFailedUrlReport(topAdsLogDBList: List<TopAdsLogDB>): List<String> {
        val failedTopAdsLogDBList = topAdsLogDBList
                .filter { it.eventStatus != STATUS_MATCH }
                .map { "${it.componentOrSource},${it.url},${it.eventStatus}" }

        return if (failedTopAdsLogDBList.isNotEmpty()) {
            mutableListOf<String>().apply {
                add("")
                add(FAILED_URL_HEADER)
                addAll(failedTopAdsLogDBList)
            }
        }
        else listOf()
    }

    private fun mapToTopAdsCoverageReport(componentName: String, topAdsLogDBList: List<TopAdsLogDB>): TopAdsCoverageReport {
        val impressionLog = topAdsLogDBList.filter { it.eventType == EVENT_IMPRESSION }
        val impressionCount = impressionLog.size
        val impressionSuccess = impressionLog.filter { it.eventStatus == STATUS_MATCH }.size

        val clickLog = topAdsLogDBList.filter { it.eventType == EVENT_CLICK }
        val clickCount = clickLog.size
        val clickSuccess = clickLog.filter { it.eventStatus == STATUS_MATCH }.size

        return TopAdsCoverageReport(componentName, impressionSuccess, impressionCount, clickSuccess, clickCount)
    }

    private fun getSuccessPercentage(success: Int, total: Int): String {
        if (total == 0) return "-"

        return (success * 100 / total).toDouble().toString() + "%"
    }

    private fun createTopAdsCoverageReportFile(callerClass: String, reportList: List<String>) {
        val externalFilesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        val topAdsReportRootDir = File(externalFilesDir, FILE_TOPADS_REPORT_ROOT)
        if (!topAdsReportRootDir.exists()) {
            topAdsReportRootDir.mkdirs()
        }

        val topAdsVerificatorFileName = getTopAdsVerificatorFileName(callerClass)
        val topAdsCoverageReportFile = File(topAdsReportRootDir, topAdsVerificatorFileName)

        topAdsCoverageReportFile.appendText(reportList.joinToString("\n"))
    }

    private fun getTopAdsVerificatorFileName(callerClass: String) =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date()) +
                    "_" +
                    callerClass +
                    ".csv"

    data class TopAdsCoverageReport(
            val componentName: String,
            val impressionSuccess: Int,
            val impressionCount: Int,
            val clickSuccess: Int,
            val clickCount: Int
    ) {
        private val impressionMatch = getSuccessPercentage(impressionSuccess, impressionCount)
        private val clickMatch = getSuccessPercentage(clickSuccess, clickCount)

        fun generateReport() = "$componentName,$impressionMatch,$clickMatch"
    }
}