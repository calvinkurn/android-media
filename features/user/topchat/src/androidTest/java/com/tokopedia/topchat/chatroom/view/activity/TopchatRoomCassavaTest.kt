package com.tokopedia.topchat.chatroom.view.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.annotation.IdRes
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
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
import com.tokopedia.applink.RouteManager
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.R
import com.tokopedia.topchat.action.ClickChildViewWithIdAction
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.TopchatProductAttachmentViewHolder
import com.tokopedia.topchat.stub.chatroom.usecase.ChatAttachmentUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.GetChatUseCaseStub
import com.tokopedia.topchat.stub.chatroom.view.activity.TopChatRoomActivityStub
import com.tokopedia.utils.time.TimeHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.hamcrest.core.AllOf
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class TopchatRoomCassavaTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
            TopChatRoomActivityStub::class.java, false, false
    )

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var getChatUseCase: GetChatUseCaseStub
    private lateinit var chatAttachmentUseCase: ChatAttachmentUseCaseStub
    private lateinit var activity: TopChatRoomActivityStub

    private var firstPageChat: GetExistingChatPojo = AndroidFileUtil.parse(
            "success_get_chat_first_page.json",
            GetExistingChatPojo::class.java
    )
    private var firstPageChatBroadcast: GetExistingChatPojo = AndroidFileUtil.parse(
            "success_get_chat_broadcast.json",
            GetExistingChatPojo::class.java
    )
    private var chatAttachmentResponse: ChatAttachmentResponse = AndroidFileUtil.parse(
            "success_get_chat_attachments.json",
            ChatAttachmentResponse::class.java
    )

    private val cassavaDirTopchat = "tracker/user/topchat"
    private val cassavaProduct = "$cassavaDirTopchat/product_card_p0.json"
    private val cassavaBroadcastProduct = "$cassavaDirTopchat/product_card_from_broadcast_p0.json"
    private val cassavaSearchProduct = "$cassavaDirTopchat/product_card_from_search_p0.json"

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDbSource = GtmLogDBSource(context)
    private val exMessageId = "66961"

    @ExperimentalCoroutinesApi
    @Before
    fun before() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        getChatUseCase = GetChatUseCaseStub()
        chatAttachmentUseCase = ChatAttachmentUseCaseStub()
        gtmLogDbSource.deleteAll().subscribe()
    }

    @Test
    fun asses_view_click_cta_atc_and_buy_product_attachment_from_user() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChat
        chatAttachmentUseCase.response = chatAttachmentResponse
        intending(anyIntent()).respondWith(
                Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )

        // When
        activity.setupTestFragment(getChatUseCase, chatAttachmentUseCase)
        performClickOnProductCard(R.id.recycler_view)
        performClickAtcButton(R.id.recycler_view)
        performClickBuyButton(R.id.recycler_view)

        // Then
        verifyRecyclerViewDisplayed(R.id.recycler_view)
        assertThat(
                getAnalyticsWithQuery(gtmLogDbSource, context, cassavaProduct),
                hasAllSuccess()
        )
    }

    @Test
    fun asses_view_click_cta_atc_and_buy_product_attachment_from_broadcast() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatBroadcast
        chatAttachmentUseCase.response = chatAttachmentResponse
        intending(anyIntent()).respondWith(
                Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )

        // When
        activity.setupTestFragment(getChatUseCase, chatAttachmentUseCase)
        performClickOnProductCard(R.id.rv_product_carousel)
        performClickAtcButton(R.id.rv_product_carousel)
        performClickBuyButton(R.id.rv_product_carousel)

        // Then
        verifyRecyclerViewDisplayed(R.id.rv_product_carousel)
        assertThat(
                getAnalyticsWithQuery(gtmLogDbSource, context, cassavaBroadcastProduct),
                hasAllSuccess()
        )
    }

    @Test
    fun asses_view_click_cta_atc_and_buy_product_attachment_from_chat_search() {
        // Given
        setupChatRoomActivity(
                sourcePage = ApplinkConst.Chat.SOURCE_CHAT_SEARCH
        )
        getChatUseCase.response = firstPageChat
        chatAttachmentUseCase.response = chatAttachmentResponse
        intending(anyIntent()).respondWith(
                Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )

        // When
        activity.setupTestFragment(getChatUseCase, chatAttachmentUseCase)
        performClickOnProductCard(R.id.recycler_view)
        performClickAtcButton(R.id.recycler_view)
        performClickBuyButton(R.id.recycler_view)

        // Then
        verifyRecyclerViewDisplayed(R.id.recycler_view)
        assertThat(
                getAnalyticsWithQuery(gtmLogDbSource, context, cassavaSearchProduct),
                hasAllSuccess()
        )
    }

    private fun setupChatRoomActivity(
            sourcePage: String? = null
    ) {
        val intent = Intent().apply {
            putExtra(ApplinkConst.Chat.MESSAGE_ID, exMessageId)
            sourcePage?.let {
                putExtra(ApplinkConst.Chat.SOURCE_PAGE, it)
            }
        }
        activityTestRule.launchActivity(intent)
        activity = activityTestRule.activity
    }

    private fun verifyRecyclerViewDisplayed(@IdRes recyclerViewId: Int) {
        onView(AllOf.allOf(isDisplayed(), withId(recyclerViewId)))
                .check(matches(isDisplayed()))
    }

    private fun performClickOnProductCard(@IdRes recyclerViewId: Int) {
        val viewAction =
                RecyclerViewActions.actionOnItemAtPosition<TopchatProductAttachmentViewHolder>(
                        1, ViewActions.click()
                )
        onView(AllOf.allOf(isDisplayed(), withId(recyclerViewId)))
                .perform(viewAction)
    }

    private fun performClickAtcButton(@IdRes recyclerViewId: Int) {
        val viewAction =
                RecyclerViewActions.actionOnItemAtPosition<TopchatProductAttachmentViewHolder>(
                        1,
                        ClickChildViewWithIdAction()
                                .clickChildViewWithId(R.id.tv_atc)
                )
        onView(AllOf.allOf(isDisplayed(), withId(recyclerViewId)))
                .perform(viewAction)
    }

    private fun performClickBuyButton(@IdRes recyclerViewId: Int) {
        val viewAction =
                RecyclerViewActions.actionOnItemAtPosition<TopchatProductAttachmentViewHolder>(
                        1,
                        ClickChildViewWithIdAction()
                                .clickChildViewWithId(R.id.tv_buy)
                )
        onView(AllOf.allOf(isDisplayed(), withId(recyclerViewId)))
                .perform(viewAction)
    }
}