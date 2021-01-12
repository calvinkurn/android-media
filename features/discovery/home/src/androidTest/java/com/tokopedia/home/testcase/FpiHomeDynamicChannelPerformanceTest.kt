package com.tokopedia.home.testcase

import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analytics.performance.util.PerformanceDataFileUtils.writeFPIPerformanceFile
import com.tokopedia.home.environment.InstrumentationHomeTestActivity
import com.tokopedia.home.R
import com.tokopedia.home.mock.HomeMockResponseConfig
import com.tokopedia.searchbar.navigation_component.NavConstant
import com.tokopedia.test.application.util.setupGraphqlMockResponseWithCheck
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by DevAra
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class FpiHomeDynamicChannelPerformanceTest {
    val TEST_CASE_INITIAL_INFLATE_PERFORMANCE = "test_case_initial_inflate"
    val TEST_CASE_DYNAMIC_CHANNEL_SCROLL_PERFORMANCE = "test_case_dynamic_channel_scroll"
    val TEST_CASE_OVERALL_SCROLL_PERFORMANCE = "test_case_overall_scroll"

    @get:Rule
    var activityRule = object: ActivityTestRule<InstrumentationHomeTestActivity>(InstrumentationHomeTestActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            disableCoachMark()
            setupGraphqlMockResponseWithCheck(HomeMockResponseConfig())
        }
    }

    //for testing purpose, to check if mock response is working
//    @Test
//    fun testHomeLayout() {
//        Thread.sleep(10000000)
//    }
    private fun disableCoachMark(){
        val sharedPrefs = InstrumentationRegistry
                .getInstrumentation().context
                .getSharedPreferences(NavConstant.KEY_FIRST_VIEW_NAVIGATION, Context.MODE_PRIVATE)
        sharedPrefs.edit().putBoolean(
                NavConstant.KEY_FIRST_VIEW_NAVIGATION_ONBOARDING, false).apply()
    }

    @Before
    fun deleteDatabase() {
        activityRule. activity.deleteDatabase("HomeCache.db")
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
        Thread.sleep(8000)
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
        val performanceData = activityRule.activity.getFpiPerformanceResultData()
        performanceData?.let {
            writeFPIPerformanceFile(activityRule.activity, tag, performanceData)
        }
    }
}
