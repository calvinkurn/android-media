package com.tokopedia.talk.analytics.util

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess

class TalkPageRobot {

    fun <T : Activity> performClose(activityTestRule: ActivityTestRule<T>) {
        activityTestRule.finishActivity()
    }

    fun clickAction(idView: Int) {
        onView(withId(idView)).check(ViewAssertions.matches(isDisplayed()))
            .perform(ViewActions.click())
    }

    infix fun assertTest(action: TalkPageRobot.() -> Unit) = TalkPageRobot().apply(action)

    fun validate(cassavaRule: CassavaTestRule, fileName: String, timeout: Long = 5000L) {
        val startTime = System.currentTimeMillis()
        var passed = false
        while (!passed && System.currentTimeMillis().minus(startTime) <= timeout) {
            try {
                assertThat(cassavaRule.validate(fileName), hasAllSuccess())
                passed = true
            } catch (_: Throwable) {}
        }
        if (!passed) assertThat(cassavaRule.validate(fileName), hasAllSuccess())
    }

    companion object {
        const val QUESTION_ID = "298623889"
        const val SHOP_ID_VALUE = "6555276"
        const val PRODUCT_ID_VALUE = "1267836204"
        const val TALK_ITEM_THREAD_MESSAGE_PATH = "tracker/merchant/talk/click_item_thread_message.json"
        const val TALK_CLICK_CREATE_NEW_QUESTION_PATH = "tracker/merchant/talk/click_create_new_question.json"
        const val TALK_CLICK_SENT_NEW_QUESTION_PATH = "tracker/merchant/talk/click_sent_new_question.json"
        const val TALK_CLICK_SENT_TO_REPLY_PATH = "tracker/merchant/talk/click_sent_to_reply.json"
        const val TALK_VIEW_INBOX_THREAD = "tracker/merchant/talk/view_inbox_thread.json"
        const val TALK_VIEW_INBOX_TAB = "tracker/merchant/talk/view_inbox_tab.json"
        const val SENT_TO_REPLY_TEXT = "Mantap gan, barangnya sudah nyampai. Thank you"
        const val SENT_TO_WRITE_QUESTION_TEXT = "Barangnya, ready gan?"
    }
}

fun actionTest(action: TalkPageRobot.() -> Unit) = TalkPageRobot().apply(action)

fun intendingIntent() {
    Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
}
