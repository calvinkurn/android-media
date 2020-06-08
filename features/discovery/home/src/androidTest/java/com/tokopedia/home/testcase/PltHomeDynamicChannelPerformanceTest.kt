package com.tokopedia.home.testcase

import androidx.test.rule.ActivityTestRule
import com.tokopedia.analytics.performance.util.PltPerformanceData
import com.tokopedia.home.environment.InstrumentationHomeTestActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

/**
 * Created by DevAra
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class PltHomeDynamicChannelPerformanceTest {
    val TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE = "test_case_page_load_time"

    @get:Rule
    var activityRule: ActivityTestRule<InstrumentationHomeTestActivity> = ActivityTestRule(InstrumentationHomeTestActivity::class.java)

    //for testing purpose, to check if mock response is working
//    @Test
//    fun testHomeLayout() {
//        Thread.sleep(10000000)
//    }

    @Before
    fun deleteDatabase() {
        activityRule. activity.deleteDatabase("HomeCache.db")
    }

    @Test
    fun testPageLoadTimePerformance() {
        waitForData()
        savePLTPerformanceResultData(TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE)
    }


    private fun waitForData() {
        Thread.sleep(10000)
    }

    private fun savePLTPerformanceResultData(tag: String) {
        val performanceData = activityRule.activity.getPltPerformanceResultData()
        performanceData?.let {
            writePLTPerformanceFile(tag, performanceData)
        }
    }

    private fun writePLTPerformanceFile(testCaseName: String,
                                        pltPerformanceData: PltPerformanceData) {
        val path = activityRule.activity.getExternalFilesDir(null)
        val perfDataDir = File(path, "perf_data")
        if (!perfDataDir.exists()) {
            makeInitialPerfDir(perfDataDir)
        }
        val perfReportPlt = File(perfDataDir, "report-plt.csv")
        var datasource = ""
        if (activityRule.activity.isFromCache) {
            datasource = "cache"
        } else if (!pltPerformanceData.isSuccess) {
            datasource = "failed"
        } else datasource = "network"
        perfReportPlt.appendText(
                "$testCaseName," +
                        "${pltPerformanceData.startPageDuration}," +
                        "${pltPerformanceData.networkRequestDuration}," +
                        "${pltPerformanceData.renderPageDuration}," +
                        "${pltPerformanceData.overallDuration}," +
                        "$datasource\n")

        val perfReport = File(perfDataDir, "report.csv")
        perfReport.appendText(
                "$testCaseName," +
                        "${pltPerformanceData.overallDuration} PLT (ms) \n"
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
                "$datasource\n")

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
