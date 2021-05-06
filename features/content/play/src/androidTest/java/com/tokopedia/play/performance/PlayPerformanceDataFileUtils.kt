package com.tokopedia.play.performance

import android.app.Activity
import com.tokopedia.analytics.performance.util.PltPerformanceData
import java.io.File


/**
 * Created by mzennis on 09/03/21.
 * copy from [com.tokopedia.analytics.performance.util.PerformanceDataFileUtils.writePLTPerformanceFile]
 */
class PlayPerformanceDataFileUtils(
        private val activity: Activity,
        private val testCaseName: String,
        private val performanceData: PltPerformanceData,
        private val videoLatencyDuration: Long
) {

    companion object {
        private const val FOLDER_NAME = "perf_data"
        private const val FILE_NAME = "report.csv"
        private const val FILE_PLT_NAME = "report-plt.csv"
    }

    fun writeReportToFile() {
        val path = activity.getExternalFilesDir(null)
        val perfDataDir = File(path, FOLDER_NAME)
        if (!perfDataDir.exists()) makeInitialPerfDir(perfDataDir)
        val perfReportPlt = File(perfDataDir, FILE_PLT_NAME)
        perfReportPlt.appendText(
                "$testCaseName," +
                        "${performanceData.startPageDuration}," +
                        "${performanceData.networkRequestDuration}," +
                        "${performanceData.renderPageDuration}," +
                        "${performanceData.overallDuration}," +
                        "${performanceData.customMetric.toString().replace(",", ";")}," +
                        "$videoLatencyDuration\n")

        val perfReport = File(perfDataDir, FILE_NAME)
        perfReport.appendText(
                "$testCaseName," +
                        "${performanceData.overallDuration} PLT (ms) \n"
        )
    }

    private fun makeInitialPerfDir(perfDataDir: File) {
        val testcase = "Test Case"
        val metrics = "Metrics"
        val value = "Value"

        val startPagePlt = "Start Page Duration (ms)"
        val networkRequestPlt = "Network Request Duration (ms)"
        val renderPagePlt = "Render Page Duration (ms)"
        val overallPlt = "Page Load Time (ms)"
        val customMetricTitle = "Custom Metrics"
        val videoLatencyDuration = "Video Latency Duration"

        perfDataDir.mkdirs()
        val perfReportPlt = File(perfDataDir, FILE_PLT_NAME)
        perfReportPlt.appendText("" +
                "$testcase," +
                "$startPagePlt," +
                "$networkRequestPlt," +
                "$renderPagePlt," +
                "$overallPlt," +
                "$customMetricTitle," +
                "$videoLatencyDuration\n")

        val perfReport = File(perfDataDir, FILE_NAME)
        perfReport.appendText("" +
                "$metrics," +
                "$value\n")
    }
}