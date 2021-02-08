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
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
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
class TopChatRoomManualProductTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(TopChatRoomActivityStub::class.java)

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var getChatUseCase: GetChatUseCaseStub
    private lateinit var chatAttachmentUseCase: ChatAttachmentUseCaseStub
    private lateinit var activity: TopChatRoomActivityStub

    private var firstPageChat: GetExistingChatPojo = AndroidFileUtil.parse(
            "success_get_chat_first_page.json",
            GetExistingChatPojo::class.java
    )
    private var chatAttachmentResponse: ChatAttachmentResponse = AndroidFileUtil.parse(
            "success_get_chat_attachments.json",
            ChatAttachmentResponse::class.java
    )

    private val trackerResultProductCard = "tracker/user/topchat/product_card_p0.json"
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDbSource = GtmLogDBSource(context)
    private val exMessageId = "66961"

    @ExperimentalCoroutinesApi
    @Before
    fun before() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        activity = activityTestRule.activity
        getChatUseCase = GetChatUseCaseStub()
        chatAttachmentUseCase = ChatAttachmentUseCaseStub()
        gtmLogDbSource.deleteAll().subscribe()
        setupOccAbTest()
        setupChatRoom()
    }

    private fun setupOccAbTest() {
        RemoteConfigInstance.getInstance().abTestPlatform.setString(
                TopchatProductAttachmentViewHolder.AB_TEST_KEY,
                TopchatProductAttachmentViewHolder.VARIANT_DEFAULT
        )
    }

    private fun setupChatRoom() {
        activityTestRule.activity.intent.putExtra(ApplinkConst.Chat.MESSAGE_ID, exMessageId)
    }

    @Test
    fun asses_view_click_cta_atc_and_buy_product_attachment_from_user() {
        // Given
        getChatUseCase.response = firstPageChat
        chatAttachmentUseCase.response = chatAttachmentResponse
        intending(anyIntent()).respondWith(
                Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )

        // When
        activity.setupTestFragment(getChatUseCase, chatAttachmentUseCase)
        performClickOnProductCard()
        performClickAtcButton()
        performClickBuyButton()

        // Then
        verifyRecyclerViewDisplayed()
        assertThat(
                getAnalyticsWithQuery(gtmLogDbSource, context, trackerResultProductCard),
                hasAllSuccess()
        )
    }

    private fun verifyRecyclerViewDisplayed() {
        onView(AllOf.allOf(isDisplayed(), withId(R.id.recycler_view)))
                .check(matches(isDisplayed()))
    }

    private fun performClickOnProductCard() {
        val viewAction =
                RecyclerViewActions.actionOnItemAtPosition<TopchatProductAttachmentViewHolder>(
                        1, ViewActions.click()
                )
        onView(AllOf.allOf(isDisplayed(), withId(R.id.recycler_view)))
                .perform(viewAction)
    }

    private fun performClickAtcButton() {
        val viewAction =
                RecyclerViewActions.actionOnItemAtPosition<TopchatProductAttachmentViewHolder>(
                        1,
                        ClickChildViewWithIdAction(null)
                                .clickChildViewWithId(R.id.tv_atc)
                )
        onView(AllOf.allOf(isDisplayed(), withId(R.id.recycler_view)))
                .perform(viewAction)
    }

    private fun performClickBuyButton() {
        val viewAction =
                RecyclerViewActions.actionOnItemAtPosition<TopchatProductAttachmentViewHolder>(
                        1,
                        ClickChildViewWithIdAction(null)
                                .clickChildViewWithId(R.id.tv_buy)
                )
        onView(AllOf.allOf(isDisplayed(), withId(R.id.recycler_view)))
                .perform(viewAction)
    }
}