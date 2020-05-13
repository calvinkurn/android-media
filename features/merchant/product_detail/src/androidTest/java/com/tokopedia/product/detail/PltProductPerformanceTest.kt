package com.tokopedia.product.detail

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analytics.performance.util.PltPerformanceData
import com.tokopedia.product.detail.view.activity.ProductDetailActivity
import org.junit.Rule
import org.junit.Test
import java.io.File

/**
 * Created by Yehezkiel on 22/04/20
 */
class PltProductPerformanceTest {
    private val TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE = "pdp_test_case_page_load_time"

    @get:Rule
    var activityRule: ActivityTestRule<ProductDetailActivity> = ActivityTestRule(ProductDetailActivity::class.java, false, false)

    @Test
    fun testPageLoadTimePerformance() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = ProductDetailActivity.createIntent(context, "220891000")
        activityRule.launchActivity(intent)
        waitForData()
        savePLTPerformanceResultData(TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE)
        activityRule.activity.finish()
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
        val perfDataDir = File(path, "PerformanceDataFileUtils")
        if (!perfDataDir.exists()) {
            makeInitialPerfDir(perfDataDir)
        }
        val perfReportPlt = File(perfDataDir, "report-plt.csv")
        var datasource = ""
        datasource = if (!pltPerformanceData.isSuccess) {
            "failed"
        } else "network"
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