package com.tokopedia.talk.analytics

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.talk.R
import com.tokopedia.talk.analytics.util.TalkPageRobot.Companion.TALK_ITEM_THREAD_MESSAGE_PATH
import com.tokopedia.talk.analytics.util.TalkPageRobot.Companion.TALK_VIEW_INBOX_TAB
import com.tokopedia.talk.analytics.util.TalkPageRobot.Companion.TALK_VIEW_INBOX_THREAD
import com.tokopedia.talk.analytics.util.actionTest
import com.tokopedia.talk.analytics.util.intendingIntent
import com.tokopedia.talk.feature.inbox.data.DiscussionInboxResponseWrapper
import com.tokopedia.talk.feature.inbox.presentation.adapter.viewholder.TalkInboxViewHolder
import com.tokopedia.talk.stub.common.utils.Utils
import com.tokopedia.talk.stub.feature.inbox.presentation.activity.TalkInboxActivityStub
import org.junit.Rule
import org.junit.Test

class TalkInboxActivityTest : TalkCassavaTestFixture() {

    @get:Rule
    var activityRule = IntentsTestRule(
        TalkInboxActivityStub::class.java,
        false,
        false
    )

    override fun setup() {
        super.setup()
        mockResponse()
        additionalLoginInfo()
        launchActivity()
    }

    override fun launchActivity() {
        val intent = TalkInboxActivityStub.createIntent(context)
        activityRule.launchActivity(intent)
    }

    @Test
    fun validateClickItemThread() {
        actionTest {
            intendingIntent()
            clickItemThread()
        } assertTest {
            performClose(activityRule)
            validate(cassavaTestRule, TALK_ITEM_THREAD_MESSAGE_PATH)
        }
    }

    @Test
    fun validateLoadedViewThread() {
        actionTest {
            refreshView()
        } assertTest {
            performClose(activityRule)
            validate(cassavaTestRule, TALK_VIEW_INBOX_THREAD)
        }
    }

    @Test
    fun validateSwitchTab() {
        actionTest {
            swipeAnotherTab()
        } assertTest {
            performClose(activityRule)
            validate(cassavaTestRule, TALK_VIEW_INBOX_TAB)
        }
    }

    private fun clickItemThread() {
        val viewInteraction = onView(withId(R.id.talkInboxRecyclerView)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TalkInboxViewHolder>(0, ViewActions.click()))
    }

    private fun swipeAnotherTab() {
        val viewInteraction = onView(withId(R.id.talkInboxContainer)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(ViewActions.swipeLeft())
    }

    private fun refreshView() {
        val viewInteraction = onView(withId(R.id.talkInboxContainer)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(ViewActions.swipeDown())
    }

    private fun additionalLoginInfo() {
        userSession.name = "User Name"
        userSession.shopId = "fakeShopId"
        userSession.shopName = "Shop Name"
    }

    private fun mockResponse() {
        graphqlRepositoryStub.createMapResult(
            DiscussionInboxResponseWrapper::class.java,
            Utils.parseFromJson<DiscussionInboxResponseWrapper>("mock_response_discussion_inbox.json")
        )
    }
}
