package com.tokopedia.home

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analytics.performance.util.PerformanceData
import org.junit.Rule
import org.junit.Test
import java.io.File

/**
 * Created by DevAra
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class HomeDynamicChannelPerformanceTest {
    val TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE = "test_case_page_load_time"
    val TEST_CASE_INITIAL_INFLATE_PERFORMANCE = "test_case_initial_inflate"
    val TEST_CASE_DYNAMIC_CHANNEL_SCROLL_PERFORMANCE = "test_case_dynamic_channel_scroll"
    val TEST_CASE_OVERALL_SCROLL_PERFORMANCE = "test_case_overall_scroll"

    @get:Rule
    var activityRule: ActivityTestRule<InstrumentationHomeTestActivity> = ActivityTestRule(InstrumentationHomeTestActivity::class.java)

    //for testing purpose, to check if mock response is working
//    @Test
//    fun testHomeLayout() {
//        Thread.sleep(10000000)
//    }

    @Test
    fun testPageLoadTimePerformance() {
        waitForData()
        savePLTPerformanceResultData(TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE, activityRule.activity.getPageLoadTime())
    }

    @Test
    fun testInitialInflatePerformance() {
        waitForData()
        saveFPIPerformanceResultData(TEST_CASE_INITIAL_INFLATE_PERFORMANCE)
    }

    @Test
    fun testDynamicChannelPerformance() {
        waitForData()
        while (!isHomeRecommendationVisible()){
            scrollWithDelay()
        }
        saveFPIPerformanceResultData(TEST_CASE_DYNAMIC_CHANNEL_SCROLL_PERFORMANCE)
    }

    @Test
    fun testOverallHomePerformance() {
        waitForData()
        for (i in 1..20) {
            scrollWithDelay()
        }
        saveFPIPerformanceResultData(TEST_CASE_OVERALL_SCROLL_PERFORMANCE)
    }

    private fun waitForData() {
        Thread.sleep(4000)
    }

    fun ViewInteraction.isDisplayed(): Boolean {
        try {
            check(matches(ViewMatchers.isDisplayed()))
            return true
        } catch (e: NoMatchingViewException) {
            return false
        }
    }

    private fun isHomeRecommendationVisible(): Boolean {
        return onView(withId(R.id.home_recommendation_feed_container)).isDisplayed()
    }

    private fun scrollWithDelay() {
        Thread.sleep(1000)
        onView(withId(R.id.home_fragment_recycler_view))
                .perform(ViewActions.swipeUp())
    }

    private fun saveFPIPerformanceResultData(tag: String) {
        val performanceData = activityRule.activity.getPerformanceResultData()
        performanceData?.let {
            writeFPIPerformanceFile(tag, performanceData)
        }
    }

    private fun savePLTPerformanceResultData(tag: String, pageLoadTime: Long) {
        val performanceData = activityRule.activity.getPerformanceResultData()
        performanceData?.let {
            writePLTPerformanceFile(tag, performanceData, pageLoadTime)
        }
    }

    private fun writeFPIPerformanceFile(testCaseName: String, performanceData: PerformanceData) {
        val path = activityRule.activity.getExternalFilesDir(null)
        val perfDataDir = File(path, "perf_data")
        if (!perfDataDir.exists()) {
            makeInitialPerfDir(perfDataDir)
        }
        val perfReport = File(perfDataDir, "report-fpi.csv")
        perfReport.appendText(
                "$testCaseName," +
                        "${performanceData.allFrames}," +
                        "${performanceData.jankyFrames}," +
                        "${performanceData.jankyFramePercentage}," +
                        "${(100 - performanceData.jankyFramePercentage)}\n")
    }

    private fun writePLTPerformanceFile(testCaseName: String, performanceData: PerformanceData, duration: Long) {
        val path = activityRule.activity.getExternalFilesDir(null)
        val perfDataDir = File(path, "perf_data")
        if (!perfDataDir.exists()) {
            makeInitialPerfDir(perfDataDir)
        }
        val perfReport = File(perfDataDir, "report-plt.csv")
        perfReport.appendText(
                "$testCaseName," +
                        "${duration}," +
                        "${(100 - performanceData.jankyFramePercentage)}\n")
    }

    private fun makeInitialPerfDir(perfDataDir: File) {
        val testcase = "Test Case"
        val plt = "PLT (ms)"
        val initialfpi = "Initial FPI"
        val allframes = "All Frames"
        val jankyframes = "Janky Frames"
        val jankyframespercentage = "Janky Frames (%)"
        val indexperformance = "Index Performance (FPI)"
        val homeFpi = "Home FPI"
        val homePlt = "Home PLT"

        perfDataDir.mkdirs()
        val perfReportPlt = File(perfDataDir, "report-plt.csv")
        perfReportPlt.appendText("" +
                "$testcase," +
                "$plt," +
                "$initialfpi\n")

        val perfReportFpi = File(perfDataDir, "report-fpi.csv")
        perfReportFpi.appendText("" +
                "$testcase," +
                "$allframes," +
                "$jankyframes," +
                "$jankyframespercentage," +
                "$indexperformance\n")

        val perfReport = File(perfDataDir, "report.csv")
        perfReport.appendText("" +
                "$homeFpi," +
                "$homePlt\n")
    }
}
