package com.tokopedia.play.broadcaster

import android.content.Intent
import android.net.Uri
import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.IdlingPolicies
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analytics.performance.util.PerformanceDataFileUtils
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.play.broadcaster.util.idling.PlayBroadcasterIdlingResource
import com.tokopedia.play.broadcaster.view.activity.PlayBroadcastActivity
import com.tokopedia.test.application.TestRepeatRule
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
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

    @Before
    fun setup() {
        IdlingPolicies.setMasterPolicyTimeout(1, TimeUnit.MINUTES)
        IdlingPolicies.setIdlingResourceTimeout(1, TimeUnit.MINUTES)

        /**
         * Use mock for now since we can't perform real gql network request due to
         * unable to login real user with InstrumentationAuthHelper.loginToAnUser()
         */
        setupGraphqlMockResponse(PrepareModelConfig())

        InstrumentationAuthHelper.userSession {
            /** content.prod.automation2+frontendtest@tokopedia.com */
            userId = "17211048"
            shopId = "3533069"
        }
        IdlingRegistry.getInstance().register(PlayBroadcasterIdlingResource.idlingResource)
    }

    @Test
    fun testPageLoadTimePerformance() {
        intentsTestRule.launchActivity(Intent(targetContext, PlayBroadcastActivity::class.java).apply {
            data = Uri.parse(ApplinkConstInternalContent.INTERNAL_PLAY_BROADCASTER)
        })
        onIdle()
        intentsTestRule.activity.getPltPerformanceResultData().let { data->
            PerformanceDataFileUtils.writePLTPerformanceFile(
                    intentsTestRule.activity,
                    TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE,
                    data,
            )
        }
    }

    @After
    fun tearDown() {
        intentsTestRule.activity.finishAndRemoveTask()
        IdlingRegistry.getInstance().unregister(PlayBroadcasterIdlingResource.idlingResource)
    }

    companion object {
        const val TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE = "play_broadcaster_test_case_page_load_time"
    }
}
