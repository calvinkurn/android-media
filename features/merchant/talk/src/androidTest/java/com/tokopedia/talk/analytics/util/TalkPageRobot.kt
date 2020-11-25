package com.tokopedia.talk.analytics.util

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingPolicies
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analytics.performance.util.PltPerformanceData
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.hamcrest.Matcher
import java.util.concurrent.TimeUnit

class TalkPageRobot {

    fun <T : Activity> performClose(activityTestRule: ActivityTestRule<T>) {
        activityTestRule.finishActivity()
    }

    fun Matcher<View>.clickAction() {
        onView(this).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.click())
    }

    infix fun assertTest(action: TalkPageRobot.() -> Unit) = TalkPageRobot().apply(action)

    fun validate(gtmLogDbSource: GtmLogDBSource,
                 targetContext: Context,
                 fileName: String) {
        ViewMatchers.assertThat(getAnalyticsWithQuery(gtmLogDbSource, targetContext, fileName),
                hasAllSuccess())
    }

    fun fakeLogin() {
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
    }

    companion object {
        const val PARAM_APP_LINK_SHOP_ID = "shop_id"
        const val QUESTION_ID = "298623889"
        const val SHOP_ID = "6555276"
        const val PRODUCT_ID = "1267836204"
        const val TALK_CLICK_BUYER_TAB_AND_MESSAGE_PATH = "tracker/merchant/talk/click_buyer_tab_and_message.json"
        const val TALK_CLICK_CREATE_NEW_QUESTION_PATH = "tracker/merchant/talk/click_create_new_question.json"
        const val TALK_CLICK_SENT_NEW_QUESTION_PATH = "tracker/merchant/talk/click_sent_new_question.json"
        const val TALK_CLICK_SENT_TO_REPLY_PATH = "tracker/merchant/talk/click_sent_to_reply.json"
        const val SENT_TO_REPLY_TEXT = "Mantap gan, barangnya sudah nyampai. Recommended deh tokonya!"
    }
}

fun actionTest(action: TalkPageRobot.() -> Unit) = TalkPageRobot().apply(action)

fun registerIdlingResource() {
    IdlingRegistry.getInstance().register(TalkIdlingResource.idlingResource)
}

fun unRegisterIdlingResource() {
    IdlingRegistry.getInstance().unregister(TalkIdlingResource.idlingResource)
}

fun intendingIntent() {
    Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
}

fun setUpTimeoutIdlingResource() {
    IdlingPolicies.setMasterPolicyTimeout(5, TimeUnit.MINUTES)
    IdlingPolicies.setIdlingResourceTimeout(5, TimeUnit.MINUTES)
}

fun clearLogin() {
    InstrumentationAuthHelper.clearUserSession()
}

fun waitForData() {
    Thread.sleep(5000)
}

fun waitForTrackerSent() {
    Thread.sleep(3000)
}