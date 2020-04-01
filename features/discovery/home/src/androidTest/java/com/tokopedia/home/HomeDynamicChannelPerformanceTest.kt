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
import org.hamcrest.Matchers.not
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
    fun testInitialInflatePerformance() {
        waitForData()
        savePerformanceResultData(TEST_CASE_INITIAL_INFLATE_PERFORMANCE)
    }

    @Test
    fun testDynamicChannelPerformance() {
        waitForData()
        while (!isHomeRecommendationVisible()){
            scrollWithDelay()
        }
        savePerformanceResultData(TEST_CASE_DYNAMIC_CHANNEL_SCROLL_PERFORMANCE)
    }

    @Test
    fun testOverallHomePerformance() {
        waitForData()
        for (i in 1..20) {
            scrollWithDelay()
        }
        savePerformanceResultData(TEST_CASE_OVERALL_SCROLL_PERFORMANCE)
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

    private fun savePerformanceResultData(tag: String) {
        val performanceData = activityRule.activity.getPerformanceResultData()
        performanceData?.let {
            writeToFile(tag, performanceData)
        }
    }

    private fun writeToFile(testCaseName: String, performanceData: PerformanceData) {
        val testcase = "Test Case"
        val allframes = "All Frames"
        val jankyframes = "Janky Frames"
        val jankyframespercentage = "Janky Frames (%)"
        val indexperformance = "Index Performance (FPI)"

        val path = activityRule.activity.getExternalFilesDir(null)
        val perfDataDir = File(path, "perf_data")
        if (!perfDataDir.exists()) {
            perfDataDir.mkdirs()
            val perfReport = File(perfDataDir, "report.csv")
            perfReport.appendText("" +
                    "$testcase," +
                    "$allframes," +
                    "$jankyframes," +
                    "$jankyframespercentage," +
                    "$indexperformance\n")
        }
        val perfReport = File(perfDataDir, "report.csv")
        perfReport.appendText(
                "$testCaseName," +
                        "${performanceData.allFrames}," +
                        "${performanceData.jankyFrames}," +
                        "${performanceData.jankyFramePercentage}," +
                        "${(100 - performanceData.jankyFramePercentage)}\n")
    }
}
