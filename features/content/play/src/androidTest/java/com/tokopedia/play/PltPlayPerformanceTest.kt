package com.tokopedia.play

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analytics.performance.util.PerformanceDataFileUtils
import com.tokopedia.play.view.activity.PlayActivity
import com.tokopedia.test.application.TestRepeatRule
import com.tokopedia.test.application.util.setupGraphqlMockResponseWithCheck
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * Created by mzennis on 15/05/20.
 */
class PltPlayPerformanceTest {

    @get:Rule
    var activityRule: ActivityTestRule<PlayActivity> = ActivityTestRule(PlayActivity::class.java, false, false)

    @get:Rule
    var testRepeatRule: TestRepeatRule = TestRepeatRule()

    @Before
    fun setup() {
        setupGraphqlMockResponseWithCheck(PlayMockModelConfig())
    }

    @Test
    fun testPageLoadTimePerformance() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = PlayActivity.createIntent(context, "10708")
        activityRule.launchActivity(intent)
        Thread.sleep(10000)
        activityRule.activity.getPltPerformanceResultData()?.let { data->
            PerformanceDataFileUtils.writePLTPerformanceFile(
                    activityRule.activity,
                    TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE,
                    data)
        }
        activityRule.activity.finishAndRemoveTask()
        Thread.sleep(1000)
    }

    companion object {
        const val TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE = "play_test_case_page_load_time"
    }
}