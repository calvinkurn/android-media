package com.tokopedia.play.analytic

import com.tokopedia.play.data.PlayErrorMockModelConfig
import com.tokopedia.play.view.activity.PlayActivity
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import org.junit.Test


/**
 * Created by mzennis on 07/09/20.
 * Instrumentation Test for checking Analytics Validator in Play Viewer Module
 *
 * Channel Type: Error
 */
class CavPlayTrackingErrorTest: BaseCavPlayTrackingTest() {

    override fun getFileName(): String = "tracker/content/play/play_error_analytic.json"

    override fun mockModelConfig(): MockModelConfig = PlayErrorMockModelConfig()

    @Test
    fun runValidateTracking() {
        // launch play activity with dummy channel id
        intentsTestRule.launchActivity(PlayActivity.createIntent(targetContext, "43215"))

        // idling resource: add artificial delays to the tests
        Thread.sleep(8000)

        // finish / close activity
        intentsTestRule.finishActivity()

        Thread.sleep(2000)

        validateTracker()
    }
}