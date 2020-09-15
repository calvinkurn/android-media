package com.tokopedia.play.analytic.robot

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.core.getAnalyticsWithQuery
import com.tokopedia.analyticsdebugger.validator.core.hasAllSuccess
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.hamcrest.MatcherAssert


/**
 * Created by mzennis on 14/09/20.
 */
internal class PlayTrackingTest(
        private val targetContext: Context,
        private val gtmLogDbSource: GtmLogDBSource,
        private val fileName: String) {

//    private var idlingResources: MutableList<IdlingResource> = mutableListOf()

    /**
     * fake intent activity, the destination activity will not be launched.
     */
    fun fakeLaunch() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    fun fakeLogin() {
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
    }

    /**
     * idling resource: artificial delays to the tests
     */
//    fun setIdlingResource(idlingResource: IdlingResource) {
//        this.idlingResources.add(idlingResource)
//
//        // register idling resources
//        IdlingRegistry.getInstance().register(idlingResource)
//    }

//    fun delay(isIdleNow: Boolean) {
//        // Submit the message to the delayer.
//        idlingResource.setIdleState(isIdleNow = isIdleNow)
//    }

    fun validate() {
        MatcherAssert.assertThat(getAnalyticsWithQuery(gtmLogDbSource, targetContext, fileName),
                hasAllSuccess())
    }

    infix fun clear(action: PlayDoneTrackingTest.() -> Unit) = PlayDoneTrackingTest().apply { action() }
}