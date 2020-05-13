package com.tokopedia.officialstore.testcase

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analytics.performance.util.PltPerformanceData
import com.tokopedia.officialstore.environment.InstrumentationOfficialStoreTestActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File
import androidx.test.rule.ActivityTestRule

/**
 * Created by DevAra
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class PltOsHomePerformanceTest {
    private val TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE = "official_store_test_case_page_load_time"
    private var context: Context? = null

    @get:Rule
    var activityRule: ActivityTestRule<InstrumentationOfficialStoreTestActivity> = ActivityTestRule(InstrumentationOfficialStoreTestActivity::class.java)

    @Before
    fun init(){
        context =  InstrumentationRegistry.getInstrumentation().targetContext
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
        context?.getExternalFilesDir(null)?.let{path ->
            val perfDataDir = File(path, "perf_data")
            if (!perfDataDir.exists()) {
                makeInitialPerfDir(perfDataDir)
            }
            val perfReportPlt = File(perfDataDir, "report-plt.csv")
            val datasource: String = if (!pltPerformanceData.isSuccess) {
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
