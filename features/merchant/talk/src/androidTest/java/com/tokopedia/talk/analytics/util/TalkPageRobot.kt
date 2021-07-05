package com.tokopedia.talk.analytics.util

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.view.View
import android.widget.EditText
import androidx.test.espresso.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.google.android.material.tabs.TabLayout
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.unifycomponents.TabsUnify
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf.allOf
import java.util.concurrent.TimeUnit

class TalkPageRobot {

    fun <T : Activity> performClose(activityTestRule: ActivityTestRule<T>) {
        activityTestRule.finishActivity()
    }

    fun clickAction(idView: Int) {
        onView(withId(idView)).check(ViewAssertions.matches(isDisplayed()))
                .perform(ViewActions.click())
    }

    fun selectTabAtPosition(tabIndex: Int): ViewAction {
        return object : ViewAction {
            override fun getDescription() = "with tab at index $tabIndex"

            override fun getConstraints() = allOf(isDisplayed(), isAssignableFrom(TabsUnify::class.java))

            override fun perform(uiController: UiController, view: View) {
                val tabsUnify = view as TabsUnify
                val tabAtIndex: TabLayout.Tab = tabsUnify.tabLayout.getTabAt(tabIndex)
                        ?: throw PerformException.Builder()
                                .withCause(Throwable("No tab at index $tabIndex"))
                                .build()

                tabAtIndex.select()
            }
        }
    }

    infix fun assertTest(action: TalkPageRobot.() -> Unit) = TalkPageRobot().apply(action)

    fun GtmLogDBSource.finishTest() {
        deleteAll().subscribe()
    }

    fun validate(gtmLogDbSource: GtmLogDBSource,
                 targetContext: Context,
                 fileName: String) {
        assertThat(getAnalyticsWithQuery(gtmLogDbSource, targetContext, fileName),
                hasAllSuccess())
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
        const val EMAIL_LOGIN = "evy.maria@tokopedia.com"
        const val PASSWORD_LOGIN = "tokopedia888"
    }
}

fun pauseTestFor(milliseconds: Long) {
    try {
        Thread.sleep(milliseconds)
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
}

fun actionTest(action: TalkPageRobot.() -> Unit) = TalkPageRobot().apply(action)

fun intendingIntent() {
    Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
}

fun registerIdlingResource() {
    IdlingRegistry.getInstance().register(TalkIdlingResource.idlingResource)
}

fun unRegisterIdlingResource() {
    IdlingRegistry.getInstance().unregister(TalkIdlingResource.idlingResource)
}

fun setUpTimeoutIdlingResource() {
    IdlingPolicies.setMasterPolicyTimeout(5, TimeUnit.MINUTES)
    IdlingPolicies.setIdlingResourceTimeout(5, TimeUnit.MINUTES)
}

fun fakeLogin() {
    InstrumentationAuthHelper.loginInstrumentationTestUser1()
}

fun clearLogin() {
    InstrumentationAuthHelper.clearUserSession()
}

fun waitForData() {
    Thread.sleep(5000L)
}

fun waitForTrackerSent() {
    Thread.sleep(4000L)
}

fun forceTypeText(text: String): ViewAction {
    return object : ViewAction {
        override fun getDescription(): String {
            return "force type text"
        }

        override fun getConstraints(): Matcher<View> {
            return allOf(isEnabled())
        }

        override fun perform(uiController: UiController?, view: View?) {
            (view as? EditText)?.append(text)
            uiController?.loopMainThreadUntilIdle()
        }
    }
}