package com.tokopedia.test.application.assertion.topads

import android.content.Context
import com.tokopedia.analyticsdebugger.database.STATUS_MATCH
import com.tokopedia.analyticsdebugger.database.TopAdsLogDB
import java.io.File

object TopAdsVerificationTestReportUtil {
    private val FILE_TOPADS_REPORT_ROOT = "topads_verificator_data"
    private val FILE_TOPADS_LOG = "report-topads-verificator-log.log"
    private val FILE_TOPADS_VERIFICATOR_DATA = "report-topads-verificator.csv"
    val REPORT_HEADER = "Component Name,Impression Match,Click Match"
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

    fun writeTopAdsVerificatorReportCoverage(context: Context, topAdsLogDBList: List<TopAdsLogDB>) {
        val reportList = generateTopAdsVerificatorReportCoverage(topAdsLogDBList)

        createTopAdsCoverageReportFile(context, reportList)
    }

    fun generateTopAdsVerificatorReportCoverage(topAdsLogDBList: List<TopAdsLogDB>): List<String> {
        if (topAdsLogDBList.isEmpty()) return listOf()

        val topAdsVerificatorDataList = topAdsLogDBList.groupBy {
            if (it.componentName.isEmpty()) it.sourceName else it.componentName
        }

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

        return listOf(REPORT_HEADER) +
                topAdsCoverageReportList.map { it.generateReport() } +
                listOf(",$totalImpressionCoverage,$totalClickCoverage")
    }

    private fun getSuccessPercentage(success: Int, total: Int): String {
        if (total == 0) return "-"

        return (success * 100 / total).toDouble().toString() + "%"
    }

    private fun createTopAdsCoverageReportFile(context: Context, reportList: List<String>) {
        val externalFilesDir = context.getExternalFilesDir(null)
        val topAdsReportRootDir = File(externalFilesDir, FILE_TOPADS_REPORT_ROOT)
        if (!topAdsReportRootDir.exists()) {
            makeInitialReportDir(topAdsReportRootDir)
        }
        val topAdsCoverageReportFile = File(topAdsReportRootDir, FILE_TOPADS_VERIFICATOR_DATA)

        topAdsCoverageReportFile.appendText(reportList.joinToString("\n"))
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