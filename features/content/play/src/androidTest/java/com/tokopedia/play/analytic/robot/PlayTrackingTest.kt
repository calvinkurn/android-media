package com.tokopedia.play.analytic.robot

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.hamcrest.MatcherAssert


/**
 * Created by mzennis on 14/09/20.
 */
internal class PlayTrackingTest(
        private val fileName: String) {

    /**
     * fake intent activity, the destination activity will not be launched.
     */
    fun fakeLaunch() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    fun fakeLogin() {
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
    }

    fun validate(cassava: CassavaTestRule) {
        MatcherAssert.assertThat(cassava.validate(fileName), hasAllSuccess())
    }
}