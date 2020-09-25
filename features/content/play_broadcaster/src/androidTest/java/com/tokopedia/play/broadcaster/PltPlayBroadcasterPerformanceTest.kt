package com.tokopedia.play.broadcaster

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.IdlingPolicies
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analytics.performance.util.PerformanceDataFileUtils
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.play.broadcaster.view.activity.PlayBroadcastActivity
import com.tokopedia.test.application.TestRepeatRule
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponseWithCheck
import com.tokopedia.unifycomponents.UnifyButton
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit


/**
 * Created by mzennis on 08/09/20.
 */
class PltPlayBroadcasterPerformanceTest {

    @get:Rule
    var intentsTestRule: IntentsTestRule<PlayBroadcastActivity> = IntentsTestRule(PlayBroadcastActivity::class.java, false, false)

    @get:Rule
    var testRepeatRule: TestRepeatRule = TestRepeatRule()

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val idlingResourceLogin: CountingIdlingResource = CountingIdlingResource(IDLING_RESOURCE)

    @Before
    fun setup() {
        IdlingPolicies.setMasterPolicyTimeout(1, TimeUnit.MINUTES)
        IdlingPolicies.setIdlingResourceTimeout(1, TimeUnit.MINUTES)

        setupGraphqlMockResponseWithCheck(PrepareModelConfig())
        InstrumentationAuthHelper.loginToAnUser(targetContext.applicationContext as Application, idlingResourceLogin)
        IdlingRegistry.getInstance().register(idlingResourceLogin)
    }

    @Test
    fun testPageLoadTimePerformance() {
        onIdle()
        intentsTestRule.launchActivity(Intent(targetContext, PlayBroadcastActivity::class.java).apply {
            data = Uri.parse(ApplinkConstInternalContent.INTERNAL_PLAY_BROADCASTER)
        })
        IdlingRegistry.getInstance().register(idlingResourceInit)
        onIdle()
        intentsTestRule.activity.getPltPerformanceResultData()?.let { data->
            PerformanceDataFileUtils.writePLTPerformanceFile(
                    intentsTestRule.activity,
                    TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE,
                    data)
        }
    }

    @After
    fun tearDown() {
        intentsTestRule.activity.finishAndRemoveTask()
        IdlingRegistry.getInstance().unregister(idlingResourceLogin)
        IdlingRegistry.getInstance().unregister(idlingResourceInit)
    }

    private val idlingResourceInit: IdlingResource by lazy {
        object : IdlingResource {
            override fun getName(): String = "prepare"

            private var callback: IdlingResource.ResourceCallback? = null

            override fun isIdleNow(): Boolean {
                val buttonSetup = intentsTestRule.activity.findViewById<UnifyButton>(R.id.btn_setup)
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
        const val IDLING_RESOURCE = "play_broadcaster_fake_login"
    }
}