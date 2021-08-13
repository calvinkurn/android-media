package com.tokopedia.play

import android.content.Intent
import android.net.Uri
import androidx.appcompat.widget.AppCompatImageButton
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.play.data.PlayMockModelConfig
import com.tokopedia.play.performance.PlayPerformanceDataFileUtils
import com.tokopedia.play.view.activity.PlayActivity
import com.tokopedia.test.application.TestRepeatRule
import com.tokopedia.test.application.util.setupGraphqlMockResponseWithCheck
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * Created by mzennis on 15/05/20.
 */
class PltPlayPerformanceTest {

    @get:Rule
    var activityTestRule: ActivityTestRule<PlayActivity> = ActivityTestRule(PlayActivity::class.java, false, false)

    @get:Rule
    var testRepeatRule: TestRepeatRule = TestRepeatRule()

    private val idlingResource: IdlingResource by lazy {
        object : IdlingResource {
            override fun getName(): String = "preparing-idle"

            private var callback: IdlingResource.ResourceCallback? = null

            override fun isIdleNow(): Boolean {
                val pauseButton = activityTestRule.activity.findViewById<AppCompatImageButton>(R.id.exo_pause)
                val isIdle = pauseButton.isVisible
                if (isIdle) callback?.onTransitionToIdle()
                return isIdle
            }

            override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
                this.callback = callback
            }
        }
    }

    @Before
    fun setup() {
        setupGraphqlMockResponseWithCheck(PlayMockModelConfig())
    }

    @Test
    fun testPageLoadTimePerformance() {
        launchActivity()

        IdlingRegistry.getInstance().register(idlingResource)

        Espresso.onIdle()

        writePerformanceReport()

        clearTask()
    }

    private fun launchActivity() {
        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        activityTestRule.launchActivity(Intent(targetContext, PlayActivity::class.java).apply {
            data = Uri.parse("${ApplinkConstInternalContent.INTERNAL_PLAY}/$CHANNEL_ID")
        })
    }

    private fun clearTask() {
        activityTestRule.activity.finishAndRemoveTask()
    }

    private fun writePerformanceReport() {
        val pageMonitoring = activityTestRule.activity.getPerformanceMonitoring()
        val videoLatency = activityTestRule.activity.activeFragment?.getVideoLatency().orZero()
        PlayPerformanceDataFileUtils(
                activity = activityTestRule.activity,
                testCaseName = TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE,
                performanceData = pageMonitoring.getPltPerformanceData(),
                videoLatencyDuration = videoLatency
        ).writeReportToFile()
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(idlingResource)
    }

    companion object {

        const val TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE = "play_test_case_page_load_time"

        const val CHANNEL_ID = "15774"
        // const val CHANNEL_ID = "10759" // staging

    }
}