package com.tokopedia.play.broadcaster

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analytics.performance.util.PerformanceDataFileUtils
import com.tokopedia.play.broadcaster.view.activity.PlayBroadcastActivity
import com.tokopedia.test.application.TestRepeatRule
import org.junit.Rule
import org.junit.Test


/**
 * Created by mzennis on 08/09/20.
 */
class PltPlayBroadcasterPerformanceTest {

    @get:Rule
    var activityRule: ActivityTestRule<PlayBroadcastActivity> = ActivityTestRule(PlayBroadcastActivity::class.java, false, false)

    @get:Rule
    var testRepeatRule: TestRepeatRule = TestRepeatRule()

    @Test
    fun testPageLoadTimePerformance() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        activityRule.launchActivity(PlayBroadcastActivity.createIntent(context))
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
        const val TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE = "play_broadcaster_test_case_page_load_time"
    }
}