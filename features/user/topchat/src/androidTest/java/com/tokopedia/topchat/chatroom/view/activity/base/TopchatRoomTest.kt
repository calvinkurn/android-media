package com.tokopedia.topchat.chatroom.view.activity.base

import android.content.Context
import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.chat_common.view.adapter.viewholder.chatmenu.AttachmentItemViewHolder
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.di.ChatRoomContextModule
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.StickerResponse
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.ChatListGroupStickerResponse
import com.tokopedia.topchat.idling.FragmentTransactionIdle
import com.tokopedia.topchat.stub.chatroom.di.ChatComponentStub
import com.tokopedia.topchat.stub.chatroom.di.DaggerChatComponentStub
import com.tokopedia.topchat.stub.chatroom.usecase.ChatAttachmentUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.ChatListGroupStickerUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.ChatListStickerUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.GetChatUseCaseStub
import com.tokopedia.topchat.stub.chatroom.view.activity.TopChatRoomActivityStub
import com.tokopedia.topchat.stub.chatroom.websocket.RxWebSocketUtilStub
import com.tokopedia.websocket.WebSocketResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import java.util.concurrent.TimeUnit
import javax.inject.Inject

abstract class TopchatRoomTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
            TopChatRoomActivityStub::class.java, false, false
    )

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    protected val applicationContext: Context
        get() = InstrumentationRegistry
                .getInstrumentation().context.applicationContext

    @Inject
    protected lateinit var getChatUseCase: GetChatUseCaseStub

    @Inject
    protected lateinit var chatAttachmentUseCase: ChatAttachmentUseCaseStub

    @Inject
    protected lateinit var stickerGroupUseCase: ChatListGroupStickerUseCaseStub

    @Inject
    protected lateinit var chatListStickerUseCase: ChatListStickerUseCaseStub

    @Inject
    protected lateinit var websocket: RxWebSocketUtilStub

    protected open lateinit var activity: TopChatRoomActivityStub
    protected open lateinit var fragmentTransactionIdling: FragmentTransactionIdle

    protected open val exMessageId = "66961"

    protected val RV_DELAY = TimeUnit.SECONDS.toMillis(2)

    protected var firstPageChatAsBuyer: GetExistingChatPojo = AndroidFileUtil.parse(
            "success_get_chat_first_page_as_buyer.json",
            GetExistingChatPojo::class.java
    )
    protected var firstPageChatAsSeller: GetExistingChatPojo = AndroidFileUtil.parse(
            "success_get_chat_first_page_as_seller.json",
            GetExistingChatPojo::class.java
    )
    protected var chatAttachmentResponse: ChatAttachmentResponse = AndroidFileUtil.parse(
            "success_get_chat_attachments.json",
            ChatAttachmentResponse::class.java
    )
    protected var stickerGroupAsBuyer: ChatListGroupStickerResponse = AndroidFileUtil.parse(
            "success_chat_group_sticker.json",
            ChatListGroupStickerResponse::class.java
    )
    protected var stickerListAsBuyer: StickerResponse = AndroidFileUtil.parse(
            "success_chat_bundle_sticker.json",
            StickerResponse::class.java
    )

    protected lateinit var chatComponentStub: ChatComponentStub

    @ExperimentalCoroutinesApi
    @Before
    open fun before() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        val baseComponent = (applicationContext as BaseMainApplication).baseAppComponent
        chatComponentStub = DaggerChatComponentStub.builder()
                .baseAppComponent(baseComponent)
                .chatRoomContextModule(ChatRoomContextModule(context))
                .build()
        chatComponentStub.inject(this)
    }

    protected fun setupChatRoomActivity(
            sourcePage: String? = null,
            intentModifier: (Intent) -> Unit = {}
    ) {
        val intent = Intent().apply {
            putExtra(ApplinkConst.Chat.MESSAGE_ID, exMessageId)
            sourcePage?.let {
                putExtra(ApplinkConst.Chat.SOURCE_PAGE, it)
            }
        }
        intentModifier(intent)
        activityTestRule.launchActivity(intent)
        activity = activityTestRule.activity
        fragmentTransactionIdling = FragmentTransactionIdle(
                activity.supportFragmentManager,
                TopChatRoomActivityStub.TAG
        )
    }

    protected fun waitForFragmentResumed() {
        IdlingRegistry.getInstance().register(fragmentTransactionIdling)
        onView(withId(R.id.recycler_view))
                .check(matches(isDisplayed()))
        IdlingRegistry.getInstance().unregister(fragmentTransactionIdling)
    }

    protected fun waitForIt(millis: Long) {
        Thread.sleep(millis)
    }

    protected fun inflateTestFragment() {
        activity.setupTestFragment(chatComponentStub)
        waitForFragmentResumed()
    }

    protected fun changeResponseStartTime(
            response: WebSocketResponse,
            exStartTime: String
    ) {
        response.jsonObject?.addProperty(
                "start_time",
                exStartTime
        )
    }

    protected fun clickAttachProductMenu() {
        val viewAction = RecyclerViewActions
                .actionOnItemAtPosition<AttachmentItemViewHolder>(
                        0, click()
                )
        onView(withId(R.id.rv_topchat_attachment_menu))
                .perform(viewAction)
    }

    protected fun clickStickerIconMenu() {
        onView(withId(R.id.iv_chat_sticker))
                .perform(click())
    }

    protected fun clickPlusIconMenu() {
        onView(withId(R.id.iv_chat_menu))
                .perform(click())
    }

    protected fun clickSendBtn() {
        onView(withId(R.id.send_but))
                .perform(click())
    }

}