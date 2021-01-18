package com.tokopedia.topchat.chatsearch

import android.app.Activity
import android.app.Instrumentation
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.R
import com.tokopedia.topchat.action.ClickChildViewWithIdAction
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.TopchatProductAttachmentViewHolder
import com.tokopedia.topchat.chatsearch.data.GetMultiChatSearchResponse
import com.tokopedia.topchat.chatsearch.view.adapter.viewholder.ItemSearchChatReplyViewHolder
import com.tokopedia.topchat.stub.chatsearch.usecase.GetSearchQueryUseCaseStub
import com.tokopedia.topchat.stub.chatsearch.view.activity.ChatSearchActivityStub
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.hamcrest.core.AllOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class TopChatSearchActivityTest {

    @get:Rule
    var activityTestRule = ActivityTestRule(ChatSearchActivityStub::class.java)

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var usecase: GetSearchQueryUseCaseStub

    private lateinit var activity: ChatSearchActivityStub

    private var searchChatList: GetMultiChatSearchResponse = AndroidFileUtil.parse(
            "success_get_chat_search_list.json",
            GetMultiChatSearchResponse::class.java
    )

    private val trackerResultProductCard = "tracker/user/topchat/product_card_from_search_p0.json"

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private val gtmLogDbSource = GtmLogDBSource(context)

    private var testingKeyword = "testing789"

    @ExperimentalCoroutinesApi
    @Before
    fun before() {
        Intents.init()
        Dispatchers.setMain(TestCoroutineDispatcher())
        activity = activityTestRule.activity
        usecase = GetSearchQueryUseCaseStub()

        gtmLogDbSource.deleteAll().subscribe()

        setupChatSearchlist()
    }

    @After
    fun finish() {
        Intents.release()
        activityTestRule.finishActivity()
    }

    private fun setupChatSearchlist() {
        usecase.response = searchChatList
        activity.setupTestFragment(usecase)
    }

    @Test
    fun assess_chat_search() {
        typeKeyword()
        onClickProductSearch()

        Intents.intending(IntentMatchers.anyIntent()).respondWith(
                Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )

        onClickProductTopchat()
        onClickAddToCart()
        onClickBuy()

        Thread.sleep(2000)
        ViewMatchers.assertThat(getAnalyticsWithQuery(gtmLogDbSource, context, trackerResultProductCard), hasAllSuccess())
    }

    private fun typeKeyword() {
        val searchBar = Espresso.onView(ViewMatchers.withId(R.id.searchTextView))
        searchBar.perform(ViewActions.click(), ViewActions.typeText(testingKeyword), ViewActions.pressImeActionButton())

        //wait until the keyword & data settle
        Thread.sleep(2000)
    }

    private fun onClickProductSearch() {
        Espresso.onView(AllOf.allOf(ViewMatchers.isDisplayed(), ViewMatchers.withId(R.id.recycler_view)))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition<ItemSearchChatReplyViewHolder>(
                        1, ViewActions.click()))

        //wait until the chat room showed
        Thread.sleep(2000)
    }

    private fun onClickProductTopchat() {
        Espresso.onView(AllOf.allOf(ViewMatchers.isDisplayed(), ViewMatchers.withId(R.id.recycler_view)))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition<TopchatProductAttachmentViewHolder>(
                        1, ViewActions.click()))
    }

    private fun onClickAddToCart() {
        Espresso.onView(AllOf.allOf(ViewMatchers.isDisplayed(), ViewMatchers.withId(R.id.recycler_view)))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition<TopchatProductAttachmentViewHolder>(
                        1, ClickChildViewWithIdAction(null).clickChildViewWithId(R.id.tv_atc)))
    }

    private fun onClickBuy() {
        Espresso.onView(AllOf.allOf(ViewMatchers.isDisplayed(), ViewMatchers.withId(R.id.recycler_view)))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition<TopchatProductAttachmentViewHolder>(
                        1, ClickChildViewWithIdAction(null).clickChildViewWithId(R.id.tv_buy)))
    }
}