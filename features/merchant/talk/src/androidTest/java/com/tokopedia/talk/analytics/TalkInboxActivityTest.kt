package com.tokopedia.talk.analytics

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.talk.R
import com.tokopedia.talk.analytics.util.*
import com.tokopedia.talk.analytics.util.TalkPageRobot.Companion.TALK_ITEM_THREAD_MESSAGE_PATH
import com.tokopedia.talk.analytics.util.TalkPageRobot.Companion.TALK_VIEW_INBOX_TAB
import com.tokopedia.talk.analytics.util.TalkPageRobot.Companion.TALK_VIEW_INBOX_THREAD
import com.tokopedia.talk.feature.inbox.presentation.activity.TalkInboxActivity
import com.tokopedia.talk.feature.inbox.presentation.adapter.viewholder.TalkInboxViewHolder
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.user.session.UserSession
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class TalkInboxActivityTest {

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(targetContext)
    private val userSession = UserSession(targetContext)

    @get:Rule
    var activityRule: IntentsTestRule<TalkInboxActivity> = object : IntentsTestRule<TalkInboxActivity>(TalkInboxActivity::class.java) {

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponse(TalkMockResponse())
            fakeLogin()
            additionalLoginInfo()
        }

        override fun getActivityIntent(): Intent {
            return TalkInboxActivity.createIntent(targetContext)
        }

        override fun afterActivityLaunched() {
            super.afterActivityLaunched()
            waitForData()
        }
    }

    @Before
    fun setup() {
        gtmLogDBSource.deleteAll().toBlocking().first()
    }

    @After
    fun tear() {
        userSession.name = ""
        userSession.shopId = ""
        userSession.shopName = ""
        clearLogin()
    }

    @Test
    fun validateClickItemThread() {
        actionTest {
            clickItemThread()
        } assertTest {
            performClose(activityRule)
            waitForTrackerSent()
            validate(gtmLogDBSource, targetContext, TALK_ITEM_THREAD_MESSAGE_PATH)
            gtmLogDBSource.finishTest()
        }
    }

    @Test
    fun validateLoadedViewThread() {
        actionTest {
            refreshView()
        } assertTest {
            performClose(activityRule)
            waitForTrackerSent()
            validate(gtmLogDBSource, targetContext, TALK_VIEW_INBOX_THREAD)
            gtmLogDBSource.finishTest()
        }
    }

    @Test
    fun validateSwitchTab() {
        actionTest {
            swipeAnotherTab()
        } assertTest {
            performClose(activityRule)
            waitForTrackerSent()
            validate(gtmLogDBSource, targetContext, TALK_VIEW_INBOX_TAB)
            gtmLogDBSource.finishTest()
        }
    }

    private fun clickItemThread() {
        pauseTestFor(2000L)
        val viewInteraction = onView(withId(R.id.talkInboxRecyclerView)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TalkInboxViewHolder>(0, ViewActions.click()))
    }

    private fun swipeAnotherTab() {
        pauseTestFor(2000L)
        val viewInteraction = onView(withId(R.id.talkInboxContainer)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(ViewActions.swipeLeft())
    }

    private fun refreshView() {
        pauseTestFor(2000L)
        val viewInteraction = onView(withId(R.id.talkInboxContainer)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(ViewActions.swipeDown())
    }

    private fun additionalLoginInfo() {
        userSession.name = "User Name"
        userSession.shopId = "fakeShopId"
        userSession.shopName = "Shop Name"
    }
}