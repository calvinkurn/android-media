package com.tokopedia.talk.analytics.reading

import android.content.Context
import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.talk.analytics.util.*
import com.tokopedia.talk.analytics.util.TalkPageRobot.Companion.PRODUCT_ID
import com.tokopedia.talk.analytics.util.TalkPageRobot.Companion.SHOP_ID
import com.tokopedia.talk.analytics.util.TalkPageRobot.Companion.TALK_CLICK_CREATE_NEW_QUESTION_PATH
import com.tokopedia.talk.common.utils.TalkReadingLoadTimeMonitoringListener
import com.tokopedia.talk.feature.reading.presentation.activity.TalkReadingActivity
import com.tokopedia.talk_old.R
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class TalkReadingActivityTest {

    private val targetContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(targetContext)

    @get:Rule
    var activityRule: IntentsTestRule<TalkReadingActivity> = object: IntentsTestRule<TalkReadingActivity>(TalkReadingActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            clearLogin()
        }

        override fun getActivityIntent(): Intent {
            return TalkReadingActivity.createIntent(targetContext, PRODUCT_ID, SHOP_ID)
        }

        override fun afterActivityLaunched() {
            super.afterActivityLaunched()
            waitForData()
            talkReadingLoadTimeMonitoringListener.onStartPltListener()
            activity.talkReadingLoadTimeListener = talkReadingLoadTimeMonitoringListener
            markAsIdleIfPltIsSucceed()
        }
    }

    val talkReadingLoadTimeMonitoringListener = object : TalkReadingLoadTimeMonitoringListener {
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
    fun validateClickCreateNewQuestion() {
        actionTest {
            fakeLogin()
            withId(R.id.addFloatingActionButton).clickAction()
        } assertTest {
            performClose(activityRule)
            waitForTrackerSent()
            validate(gtmLogDBSource, targetContext, TALK_CLICK_CREATE_NEW_QUESTION_PATH)
            finishTest()
        }
    }

    private fun markAsIdleIfPltIsSucceed() {
        val performanceData = activityRule.activity.pageLoadTimePerformanceMonitoring?.getPltPerformanceData()
        if (performanceData?.isSuccess == true) {
            talkReadingLoadTimeMonitoringListener.onStopPltListener()
        }
    }

    private fun finishTest() {
        gtmLogDBSource.deleteAll().subscribe()
    }

}