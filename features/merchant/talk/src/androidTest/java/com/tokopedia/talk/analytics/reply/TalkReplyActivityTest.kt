package com.tokopedia.talk.analytics.reply

import android.content.Context
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.talk.analytics.util.*
import com.tokopedia.talk.analytics.util.TalkPageRobot.Companion.QUESTION_ID
import com.tokopedia.talk.analytics.util.TalkPageRobot.Companion.SENT_TO_REPLY_TEXT
import com.tokopedia.talk.analytics.util.TalkPageRobot.Companion.SHOP_ID
import com.tokopedia.talk.analytics.util.TalkPageRobot.Companion.TALK_CLICK_SENT_TO_REPLY_PATH
import com.tokopedia.talk.common.utils.TalkReplyLoadTimeMonitoringListener
import com.tokopedia.talk.feature.reply.presentation.activity.TalkReplyActivity
import com.tokopedia.talk_old.R
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class TalkReplyActivityTest {

    private val targetContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(targetContext)

    @get:Rule
    var activityRule: IntentsTestRule<TalkReplyActivity> = object: IntentsTestRule<TalkReplyActivity>(TalkReplyActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            clearLogin()
        }

        override fun getActivityIntent(): Intent {
            return TalkReplyActivity.createIntent(targetContext, QUESTION_ID, SHOP_ID)
        }

        override fun afterActivityLaunched() {
            super.afterActivityLaunched()
            waitForData()
            talkReplyLoadTimeMonitoringListener.onStartPltListener()
            activity.talkReplyLoadTimeMonitoringListener = talkReplyLoadTimeMonitoringListener
            markAsIdleIfPltIsSucceed()
        }
    }

    val talkReplyLoadTimeMonitoringListener = object : TalkReplyLoadTimeMonitoringListener {
        override fun onStartPltListener() {
            TalkIdlingResource.increment()
        }

        override fun onStopPltListener() {
            TalkIdlingResource.decrement()
        }
    }

    @Before
    fun setup() {
        setUpTimeoutIdlingResource()
        registerIdlingResource()
        gtmLogDBSource.deleteAll().toBlocking().first()
        setupGraphqlMockResponse(TalkMockResponse())
        intendingIntent()
    }

    @After
    fun tear() {
        unRegisterIdlingResource()
    }

    @Test
    fun validateClickSentToReplyTalk() {
        actionTest {
            fakeLogin()
            typingToReplyTalk()
            withId(R.id.replySendButton).clickAction()
        } assertTest {
            performClose(activityRule)
            waitForTrackerSent()
            validate(gtmLogDBSource, targetContext, TALK_CLICK_SENT_TO_REPLY_PATH)
            finishTest()
        }
    }

    private fun typingToReplyTalk() {
        onView(withId(R.id.replyEditText)).perform(typeText(SENT_TO_REPLY_TEXT))
        onView(withId(R.id.replyEditText)).check(matches(withText(SENT_TO_REPLY_TEXT)))
    }

    private fun markAsIdleIfPltIsSucceed() {
        val performanceData = activityRule.activity.pageLoadTimePerformanceMonitoring?.getPltPerformanceData()
        if (performanceData?.isSuccess == true) {
            talkReplyLoadTimeMonitoringListener.onStopPltListener()
        }
    }

    private fun finishTest() {
        gtmLogDBSource.deleteAll().subscribe()
    }
}