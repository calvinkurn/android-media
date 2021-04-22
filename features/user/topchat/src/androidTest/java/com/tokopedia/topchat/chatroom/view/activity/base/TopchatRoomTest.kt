package com.tokopedia.topchat.chatroom.view.activity.base

import android.content.Context
import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.recyclerview.widget.RecyclerView
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
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_IMAGE_ANNOUNCEMENT
import com.tokopedia.chat_common.domain.pojo.ChatReplyPojo
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.chat_common.domain.pojo.Reply
import com.tokopedia.chat_common.domain.pojo.imageannouncement.ImageAnnouncementPojo
import com.tokopedia.chat_common.view.adapter.viewholder.chatmenu.AttachmentItemViewHolder
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.config.GlobalConfig
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.di.ChatRoomContextModule
import com.tokopedia.topchat.chatroom.domain.pojo.FavoriteData.Companion.IS_FOLLOW
import com.tokopedia.topchat.chatroom.domain.pojo.ShopFollowingPojo
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.StickerResponse
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.ChatListGroupStickerResponse
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import com.tokopedia.topchat.idling.FragmentTransactionIdle
import com.tokopedia.topchat.stub.chatroom.di.ChatComponentStub
import com.tokopedia.topchat.stub.chatroom.di.DaggerChatComponentStub
import com.tokopedia.topchat.stub.chatroom.usecase.*
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
    protected lateinit var getTemplateChatRoomUseCase: GetTemplateChatRoomUseCaseStub

    @Inject
    protected lateinit var getShopFollowingUseCaseStub: GetShopFollowingUseCaseStub

    @Inject
    protected lateinit var uploadImageUseCase: TopchatUploadImageUseCaseStub

    @Inject
    protected lateinit var replyChatGQLUseCase: ReplyChatGQLUseCaseStub

    @Inject
    protected lateinit var websocket: RxWebSocketUtilStub

    protected open lateinit var activity: TopChatRoomActivityStub
    protected open lateinit var fragmentTransactionIdling: FragmentTransactionIdle

    protected open val exMessageId = "66961"

    protected val KEYBOARD_DELAY = TimeUnit.SECONDS.toMillis(1)
    protected val RV_DELAY = TimeUnit.SECONDS.toMillis(2)

    protected lateinit var firstPageChatAsBuyer: GetExistingChatPojo
    protected lateinit var firstPageChatAsSeller: GetExistingChatPojo
    protected lateinit var chatAttachmentResponse: ChatAttachmentResponse
    protected lateinit var stickerGroupAsBuyer: ChatListGroupStickerResponse
    protected lateinit var stickerListAsBuyer: StickerResponse
    protected lateinit var firstPageChatBroadcastAsBuyer: GetExistingChatPojo
    protected lateinit var getShopFollowingStatus: ShopFollowingPojo
    protected lateinit var uploadImageReplyResponse: ChatReplyPojo

    protected lateinit var chatComponentStub: ChatComponentStub

    @ExperimentalCoroutinesApi
    @Before
    open fun before() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        setupResponse()
        val baseComponent = (applicationContext as BaseMainApplication).baseAppComponent
        chatComponentStub = DaggerChatComponentStub.builder()
                .baseAppComponent(baseComponent)
                .chatRoomContextModule(ChatRoomContextModule(context))
                .build()
        chatComponentStub.inject(this)
    }

    private fun setupResponse() {
        firstPageChatAsBuyer = AndroidFileUtil.parse(
                "success_get_chat_first_page_as_buyer.json",
                GetExistingChatPojo::class.java
        )
        firstPageChatAsSeller = AndroidFileUtil.parse(
                "success_get_chat_first_page_as_seller.json",
                GetExistingChatPojo::class.java
        )
        chatAttachmentResponse = AndroidFileUtil.parse(
                "success_get_chat_attachments.json",
                ChatAttachmentResponse::class.java
        )
        stickerGroupAsBuyer = AndroidFileUtil.parse(
                "success_chat_group_sticker.json",
                ChatListGroupStickerResponse::class.java
        )
        stickerListAsBuyer = AndroidFileUtil.parse(
                "success_chat_bundle_sticker.json",
                StickerResponse::class.java
        )
        firstPageChatBroadcastAsBuyer = AndroidFileUtil.parse(
                "success_get_chat_broadcast.json",
                GetExistingChatPojo::class.java
        )
        getShopFollowingStatus = AndroidFileUtil.parse(
                "success_get_shop_following_status.json",
                ShopFollowingPojo::class.java
        )
        uploadImageReplyResponse = AndroidFileUtil.parse(
                "success_upload_image_reply.json",
                ChatReplyPojo::class.java
        )
    }

    protected fun setupChatRoomActivity(
            sourcePage: String? = null,
            isSellerApp: Boolean = false,
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
        if (isSellerApp) {
            GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION
        }
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

    protected fun clickAttachImageMenu() {
        val viewAction = RecyclerViewActions
                .actionOnItemAtPosition<AttachmentItemViewHolder>(
                        1, click()
                )
        onView(withId(R.id.rv_topchat_attachment_menu))
                .perform(viewAction)
    }

    protected fun clickComposeArea() {
        onView(withId(R.id.new_comment))
                .perform(click())
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

    protected fun clickTemplateChatAt(position: Int) {
        val viewAction = RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position, click()
        )
        onView(withId(R.id.list_template)).perform(viewAction)
    }

    protected fun generateTemplateResponse(
            enable: Boolean = true,
            success: Boolean = true,
            templates: List<String> = listOf("Template Chat 1", "Template Chat 2")
    ): TemplateData {
        return TemplateData().apply {
            this.isIsEnable = enable
            this.isSuccess = success
            this.templates = templates
        }
    }
}

fun GetExistingChatPojo.blockPromo(blockPromo: Boolean): GetExistingChatPojo {
    chatReplies.block.isPromoBlocked = blockPromo
    return this
}

fun GetExistingChatPojo.hideBanner(hide: Boolean): GetExistingChatPojo {
    val banner: Reply? = chatReplies.list[0].chats[0].replies.find { reply ->
        reply.attachment.type.toString() == TYPE_IMAGE_ANNOUNCEMENT
    }
    val newAttribute = ImageAnnouncementPojo().apply {
        isHideBanner = hide
        imageUrl = "https://images.tokopedia.net/img/cache/1190/wmEwCC/2021/3/" +
                "8/d0389667-b822-43fa-bf3f-485b4518b286.jpg.webp?ect=4g"
    }
    banner?.attachment?.attributes = CommonUtil.toJson(newAttribute)
    return this
}

fun ShopFollowingPojo.setFollowing(following: Boolean): ShopFollowingPojo {
    val follow = if (following) IS_FOLLOW else 0
    shopInfoById.result[0].favoriteData.alreadyFavorited = follow
    return this
}