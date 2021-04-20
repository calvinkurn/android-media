package com.tokopedia.analytics.performance.util

import android.app.Activity
import com.tokopedia.analytics.performance.fpi.FpiPerformanceData
import java.io.File

object PerformanceDataFileUtils {
    fun writePLTPerformanceFile(activity: Activity,
                                testCaseName: String,
                                pltPerformanceData: PltPerformanceData,
                                dataSourceType: String = "",
                                networkData: NetworkData? = null) {
        val path = activity.getExternalFilesDir(null)
        val perfDataDir = File(path, "perf_data")
        if (!perfDataDir.exists()) {
            makeInitialPerfDir(perfDataDir)
        }
        val perfReportPlt = File(perfDataDir, "report-plt.csv")
        perfReportPlt.appendText(
                "$testCaseName," +
                        "${pltPerformanceData.startPageDuration}," +
                        "${pltPerformanceData.networkRequestDuration}," +
                        "${pltPerformanceData.renderPageDuration}," +
                        "${pltPerformanceData.overallDuration}," +
                        "${pltPerformanceData.customMetric.toString().replace(",", ";")}," +
                        "$dataSourceType," +
                        "${networkData?.totalResponseSize ?: ""}," +
                        "${networkData?.totalResponseTime ?: ""}," +
                        "${networkData?.totalUserNetworkDuration ?: ""}," +
                        "${networkData?.responseSizeDetailMapString ?: ""}," +
                        "${networkData?.responseTimeDetailMapString ?: ""}\n")

        val perfReport = File(perfDataDir, "report.csv")
        perfReport.appendText(
                "$testCaseName," +
                        "${pltPerformanceData.overallDuration} PLT (ms) \n"
        )
    }

    fun writeFPIPerformanceFile(activity: Activity, testCaseName: String, fpiPerformanceData: FpiPerformanceData) {
        val path = activity.getExternalFilesDir(null)
        val perfDataDir = File(path, "perf_data")
        if (!perfDataDir.exists()) {
            makeInitialPerfDir(perfDataDir)
        }
        val perfReportFpi = File(perfDataDir, "report-fpi.csv")
        val fpiValue = (100 - fpiPerformanceData.jankyFramePercentage)
        perfReportFpi.appendText(
                "$testCaseName," +
                        "${fpiPerformanceData.allFrames}," +
                        "${fpiPerformanceData.jankyFrames}," +
                        "${fpiPerformanceData.jankyFramePercentage}," +
                        "$fpiValue\n")

        val perfReport = File(perfDataDir, "report.csv")
        perfReport.appendText(
                "$testCaseName," +
                        "$fpiValue FPI\n"
        )
    }

    private fun makeInitialPerfDir(perfDataDir: File) {
        val testcase = "Test Case"
        val metrics = "Metrics"
        val value = "Value"

        val startPagePlt = "Start Page Duration (ms)"
        val networkRequestPlt = "Network Request Duration (ms)"
        val renderPagePlt = "Render Page Duration (ms)"
        val overallPlt = "Page Load Time (FPI) (ms)"
        val datasource = "Data source"
        val totalResponseSize = "Total Response Size (bytes)"
        val totalResponseTime = "Total Response Time (ms)"
        val totalUserNetworkDuration = "Total User Network Duration (ms)"
        val responseSizeDetail = "ResponseSizeDetail"
        val responseTimeDetail = "ResponseTimeDetail"
        val customMetricTitle = "Custom Metrics"

        val allframes = "All Frames"
        val jankyframes = "Janky Frames"
        val jankyframespercentage = "Janky Frames (%)"
        val indexperformance = "Index Performance (FPI)"

        perfDataDir.mkdirs()
        val perfReportPlt = File(perfDataDir, "report-plt.csv")
        perfReportPlt.appendText("" +
                "$testcase," +
                "$startPagePlt," +
                "$networkRequestPlt," +
                "$renderPagePlt," +
                "$overallPlt," +
                "$customMetricTitle," +
                "$datasource," +
                "$totalResponseSize," +
                "$totalResponseTime," +
                "$totalUserNetworkDuration," +
                "$responseSizeDetail," +
                "$responseTimeDetail\n")

        val perfReportFpi = File(perfDataDir, "report-fpi.csv")
        perfReportFpi.appendText("" +
                "$testcase," +
                "$allframes," +
                "$jankyframes," +
                "$jankyframespercentage," +
                "$indexperformance\n")

        val perfReport = File(perfDataDir, "report.csv")
        perfReport.appendText("" +
                "$metrics," +
                "$value\n")
    }


}