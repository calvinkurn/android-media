package com.tokopedia.talk.analytics

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.talk.R
import com.tokopedia.talk.analytics.util.*
import com.tokopedia.talk.analytics.util.TalkPageRobot.Companion.QUESTION_ID
import com.tokopedia.talk.analytics.util.TalkPageRobot.Companion.SENT_TO_REPLY_TEXT
import com.tokopedia.talk.analytics.util.TalkPageRobot.Companion.SHOP_ID_VALUE
import com.tokopedia.talk.analytics.util.TalkPageRobot.Companion.TALK_CLICK_SENT_TO_REPLY_PATH
import com.tokopedia.talk.feature.reply.presentation.activity.TalkReplyActivity
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@LargeTest
class TalkReplyActivityTest {

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    var activityRule: IntentsTestRule<TalkReplyActivity> = object: IntentsTestRule<TalkReplyActivity>(TalkReplyActivity::class.java) {

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            fakeLogin()
        }

        override fun getActivityIntent(): Intent {
            return TalkReplyActivity.createIntent(targetContext, QUESTION_ID, SHOP_ID_VALUE)
        }

        override fun afterActivityLaunched() {
            super.afterActivityLaunched()
            waitForData()
        }
    }

    @get:Rule
    var cassavaRule = CassavaTestRule()

    @Before
    fun setup() {
        setupGraphqlMockResponse(TalkMockResponse())
        intendingIntent()
    }

    @After
    fun tear() {
        clearLogin()
    }

    @Test
    fun validateClickSentToReplyTalk() {
        actionTest {
            typingToReplyTalk()
            clickAction(R.id.replySendButton)
        } assertTest {
            performClose(activityRule)
            waitForTrackerSent()
            validate(cassavaRule, TALK_CLICK_SENT_TO_REPLY_PATH)
        }
    }

    private fun typingToReplyTalk() {
        onView(ViewMatchers.withId(R.id.replyEditText)).perform(typeText(SENT_TO_REPLY_TEXT), closeSoftKeyboard())
        pauseTestFor(2000L)
        onView(ViewMatchers.withId(R.id.replyEditText)).check(matches(withText(SENT_TO_REPLY_TEXT)))
    }
}