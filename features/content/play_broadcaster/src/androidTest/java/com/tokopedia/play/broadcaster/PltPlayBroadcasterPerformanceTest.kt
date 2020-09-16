package com.tokopedia.play.broadcaster

import android.view.View
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analytics.performance.util.PerformanceDataFileUtils
import com.tokopedia.play.broadcaster.view.activity.PlayBroadcastActivity
import com.tokopedia.test.application.TestRepeatRule
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.unifycomponents.UnifyButton
import org.junit.After
import org.junit.Before
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

    @Before
    fun setup() {
        setupGraphqlMockResponse(PrepareModelConfig())

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        activityRule.launchActivity(PlayBroadcastActivity.createIntent(context))
        fakeLogin()

        IdlingRegistry.getInstance().register(idlingResource)
    }

    @Test
    fun testPageLoadTimePerformance() {
        activityRule.activity.getPltPerformanceResultData()?.let { data->
            PerformanceDataFileUtils.writePLTPerformanceFile(
                    activityRule.activity,
                    TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE,
                    data)
        }
    }

    @After
    fun tearDown() {
        activityRule.activity.finishAndRemoveTask()
        IdlingRegistry.getInstance().unregister(idlingResource)
    }

    private fun fakeLogin() {
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
    }

    private val idlingResource: IdlingResource by lazy {
        object : IdlingResource {

        override fun getName(): String = "prepare"

        private var callback: IdlingResource.ResourceCallback? = null

        override fun isIdleNow(): Boolean {
            val buttonSetup = activityRule.activity.findViewById<UnifyButton>(R.id.btn_setup)
            val isIdle =  buttonSetup != null && buttonSetup.visibility == View.VISIBLE
            if (isIdle) callback?.onTransitionToIdle()
            return isIdle
        }

        override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
            this.callback = callback
        }
    }
    }

    companion object {
        const val TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE = "play_broadcaster_test_case_page_load_time"
    }
}