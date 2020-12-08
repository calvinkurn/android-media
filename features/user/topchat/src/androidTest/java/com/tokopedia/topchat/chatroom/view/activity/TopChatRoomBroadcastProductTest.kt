package com.tokopedia.topchat.chatroom.view.activity

import android.app.Activity
import android.app.Instrumentation
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.FragmentTransactIdlingResource
import com.tokopedia.topchat.R
import com.tokopedia.topchat.action.ClickChildViewWithIdAction
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.TopchatProductAttachmentViewHolder
import com.tokopedia.topchat.stub.chatroom.usecase.ChatAttachmentUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.GetChatUseCaseStub
import com.tokopedia.topchat.stub.chatroom.view.activity.TopChatRoomActivityStub
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
class TopChatRoomBroadcastProductTest {

    @get:Rule
    var activityTestRule = ActivityTestRule(TopChatRoomActivityStub::class.java)

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var getChatUseCase: GetChatUseCaseStub
    private lateinit var chatAttachmentUseCase: ChatAttachmentUseCaseStub

    private lateinit var activity: TopChatRoomActivityStub

    private var firstPageChat: GetExistingChatPojo = AndroidFileUtil.parse(
            "success_get_chat_broadcast.json",
            GetExistingChatPojo::class.java
    )
    private var chatAttachmentResponse: ChatAttachmentResponse = AndroidFileUtil.parse(
            "success_get_chat_attachments.json",
            ChatAttachmentResponse::class.java
    )

    private val trackerResultProductCard = "tracker/user/topchat/product_card_from_broadcast_p0.json"

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private val gtmLogDbSource = GtmLogDBSource(context)

    private var fragmentIdlingResource: FragmentTransactIdlingResource? = null

    private val exMessageId = "66961"

    @ExperimentalCoroutinesApi
    @Before
    fun before() {
        Intents.init()
        Dispatchers.setMain(TestCoroutineDispatcher())
        activity = activityTestRule.activity
        getChatUseCase = GetChatUseCaseStub()
        chatAttachmentUseCase = ChatAttachmentUseCaseStub()

        gtmLogDbSource.deleteAll().subscribe()

        RemoteConfigInstance.getInstance().abTestPlatform.setString(TopchatProductAttachmentViewHolder.AB_TEST_KEY, TopchatProductAttachmentViewHolder.VARIANT_DEFAULT)

        setIdlingResource()
        setupChatRoom()
        Intents.intending(IntentMatchers.anyIntent()).respondWith(
                Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )
    }

    @After
    fun finish() {
        IdlingRegistry.getInstance().unregister(fragmentIdlingResource)
        Intents.release()
        activityTestRule.finishActivity()
    }

    private fun setIdlingResource() {
        fragmentIdlingResource = FragmentTransactIdlingResource(
                activity.supportFragmentManager,
                "ChatToolbarActivity"
        ).apply {
            registerIdleTransitionCallback {
                Log.i("ChatToolbarActivity", "transition to idle")
            }
        }

        IdlingRegistry.getInstance().register(fragmentIdlingResource)
    }

    private fun setupChatRoom() {
        activityTestRule.activity.intent.putExtra(ApplinkConst.Chat.MESSAGE_ID, exMessageId)
        getChatUseCase.response = firstPageChat
        chatAttachmentUseCase.response = chatAttachmentResponse
        activity.setupTestFragment(getChatUseCase, chatAttachmentUseCase)
    }

    @Test
    fun assess_product_broadcast_topchat() {
        onViewChatRoom()
        onClickProductTopchat()
        onClickAddToCart()
        onClickBuy()

        Thread.sleep(2000)
        assertThat(getAnalyticsWithQuery(gtmLogDbSource, context, trackerResultProductCard), hasAllSuccess())
    }

    private fun onViewChatRoom() {
        onView(AllOf.allOf(isDisplayed(), withId(R.id.recycler_view))).check(matches(isDisplayed()))
    }

    private fun onClickProductTopchat() {
        onView(AllOf.allOf(isDisplayed(), withId(R.id.rv_product_carousel)))
                .check(matches(isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition<TopchatProductAttachmentViewHolder>(
                        1, ViewActions.click()))
    }

    private fun onClickAddToCart() {
        onView(AllOf.allOf(isDisplayed(), withId(R.id.rv_product_carousel)))
                .check(matches(isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition<TopchatProductAttachmentViewHolder>(
                        1, ClickChildViewWithIdAction(null).clickChildViewWithId(R.id.tv_atc)))
    }

    private fun onClickBuy() {
        onView(AllOf.allOf(isDisplayed(), withId(R.id.rv_product_carousel)))
                .check(matches(isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition<TopchatProductAttachmentViewHolder>(
                        1, ClickChildViewWithIdAction(null).clickChildViewWithId(R.id.tv_buy)))
    }
}