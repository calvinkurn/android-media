package com.tokopedia.topchat.chatroom.view.activity.base

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.attachcommon.data.ResultProduct
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
import com.tokopedia.topchat.action.ClickChildViewWithIdAction
import com.tokopedia.topchat.chatroom.di.ChatRoomContextModule
import com.tokopedia.topchat.chatroom.domain.pojo.FavoriteData.Companion.IS_FOLLOW
import com.tokopedia.topchat.chatroom.domain.pojo.ShopFollowingPojo
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse
import com.tokopedia.topchat.chatroom.domain.pojo.srw.ChatSmartReplyQuestionResponse
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.StickerResponse
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.ChatListGroupStickerResponse
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.TopchatProductAttachmentViewHolder
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import com.tokopedia.topchat.common.TopChatInternalRouter
import com.tokopedia.topchat.idling.FragmentTransactionIdle
import com.tokopedia.topchat.matchers.withRecyclerView
import com.tokopedia.topchat.matchers.withTotalItem
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
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

abstract class TopchatRoomTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
            TopChatRoomActivityStub::class.java, false, false
    )

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
    protected lateinit var chatSrwUseCase: SmartReplyQuestionUseCaseStub

    @Inject
    protected lateinit var websocket: RxWebSocketUtilStub

    protected open lateinit var activity: TopChatRoomActivityStub
    protected open lateinit var fragmentTransactionIdling: FragmentTransactionIdle

    protected open val exMessageId = "66961"

    protected var firstPageChatAsBuyer: GetExistingChatPojo = GetExistingChatPojo()
    protected var firstPageChatAsSeller: GetExistingChatPojo = GetExistingChatPojo()
    protected var chatAttachmentResponse: ChatAttachmentResponse = ChatAttachmentResponse()
    protected var stickerGroupAsBuyer: ChatListGroupStickerResponse = ChatListGroupStickerResponse()
    protected var stickerListAsBuyer: StickerResponse = StickerResponse()
    protected var firstPageChatBroadcastAsBuyer: GetExistingChatPojo = GetExistingChatPojo()
    protected var getShopFollowingStatus: ShopFollowingPojo = ShopFollowingPojo()
    protected var chatSrwResponse: ChatSmartReplyQuestionResponse = ChatSmartReplyQuestionResponse()
    protected var uploadImageReplyResponse: ChatReplyPojo = ChatReplyPojo()

    protected lateinit var chatComponentStub: ChatComponentStub

    object ProductPreviewAttribute {
        const val productName = "Testing Attach Product 1"
        const val productThumbnail = "https://ecs7-p.tokopedia.net/img/cache/350/attachment/" +
                "2020/8/24/40768394/40768394_732546f9-371d-45c6-a412-451ea50aa22c.jpg.webp"
    }

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

    protected open fun setupResponse() {
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

    protected fun assertLabelOnProductCard(
            recyclerViewId: Int,
            atPosition: Int,
            viewMatcher: Matcher<in View>
    ) {
        onView(
                withRecyclerView(recyclerViewId).atPositionOnView(
                        atPosition, R.id.lb_product_label
                )
        ).check(matches(viewMatcher))
    }

    protected fun assertLabelTextOnProductCard(
            recyclerViewId: Int,
            atPosition: Int,
            text: String
    ) {
        onView(
                withRecyclerView(recyclerViewId).atPositionOnView(
                        atPosition, R.id.lb_product_label
                )
        ).check(matches(withText(text)))
    }

    protected fun assertProductStockType(
            recyclerViewId: Int,
            atPosition: Int,
            viewMatcher: Matcher<in View>
    ) {
        onView(
                withRecyclerView(recyclerViewId).atPositionOnView(
                        atPosition, R.id.tp_seller_stock_category
                )
        ).check(matches(viewMatcher))
    }

    protected fun assertProductStockTypeText(
            recyclerViewId: Int,
            atPosition: Int,
            text: String
    ) {
        onView(
                withRecyclerView(recyclerViewId).atPositionOnView(
                        atPosition, R.id.tp_seller_stock_category
                )
        ).check(matches(withText(text)))
    }

    protected fun assertTemplateChatVisibility(
            visibilityMatcher: Matcher<in View>
    ) {
        onView(withId(R.id.list_template)).check(
                matches(visibilityMatcher)
        )
    }

    protected fun assertSrwContentContainerVisibility(
            visibilityMatcher: Matcher<in View>
    ) {
        onView(withId(R.id.rv_srw_content_container)).check(
                matches(visibilityMatcher)
        )
    }

    protected fun assertSrwTitle(
            title: String
    ) {
        onView(withId(R.id.tp_srw_partial)).check(
                matches(withText(title))
        )
    }

    protected fun assertSrwTotalQuestion(
            totalQuestion: Int
    ) {
        onView(withId(R.id.rv_srw_partial)).check(matches(withTotalItem(totalQuestion)))
    }

    protected fun assertSrwErrorVisibility(
            visibilityMatcher: Matcher<in View>
    ) {
        onView(withId(R.id.ll_srw_partial)).check(
                matches(visibilityMatcher)
        )
    }

    protected fun assertSrwLoadingVisibility(
            visibilityMatcher: Matcher<in View>
    ) {
        onView(withId(R.id.lu_srw_partial)).check(
                matches(visibilityMatcher)
        )
    }

    protected fun assertSnackbarText(msg: String) {
        onView(withText(msg)).check(matches(isDisplayed()))
    }

    protected fun assertSrwContentIsVisible() {
        assertSrwContentContainerVisibility(isDisplayed())
        assertTemplateChatVisibility(not(isDisplayed()))
        assertSrwErrorVisibility(not(isDisplayed()))
        assertSrwLoadingVisibility(not(isDisplayed()))
    }

    protected fun assertSrwContentIsLoading() {
        assertSrwLoadingVisibility(isDisplayed())
        assertSrwContentContainerVisibility(not(isDisplayed()))
        assertTemplateChatVisibility(not(isDisplayed()))
        assertSrwErrorVisibility(not(isDisplayed()))
    }

    protected fun assertSrwContentIsError() {
        assertSrwErrorVisibility(isDisplayed())
        assertSrwLoadingVisibility(not(isDisplayed()))
        assertSrwContentContainerVisibility(not(isDisplayed()))
        assertTemplateChatVisibility(not(isDisplayed()))
    }

    protected fun assertSrwContentIsHidden() {
        assertTemplateChatVisibility(isDisplayed())
        assertSrwContentContainerVisibility(not(isDisplayed()))
        assertSrwErrorVisibility(not(isDisplayed()))
        assertSrwLoadingVisibility(not(isDisplayed()))
    }

    protected fun assertHeaderRightMsgBubbleVisibility(
            position: Int, visibilityMatcher: Matcher<in View>
    ) {
        onView(withRecyclerView(R.id.recycler_view).atPositionOnView(
                position, R.id.tvRole
        )).check(matches(visibilityMatcher))
    }

    protected fun assertHeaderRightMsgBubbleText(position: Int, msg: String) {
        onView(withRecyclerView(R.id.recycler_view).atPositionOnView(
                position, R.id.tvRole
        )).check(matches(withText(msg)))
    }

    protected fun assertHeaderRightMsgBubbleBlueDotVisibility(
            position: Int, visibilityMatcher: Matcher<in View>
    ) {
        onView(withRecyclerView(R.id.recycler_view).atPositionOnView(
                position, R.id.tvRole
        )).check(matches(visibilityMatcher))
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

    protected fun clickCloseAttachmentPreview(position: Int) {
        val viewAction = RecyclerViewActions
                .actionOnItemAtPosition<TopchatProductAttachmentViewHolder>(
                        position,
                        ClickChildViewWithIdAction()
                                .clickChildViewWithId(R.id.iv_close)
                )
        onView(withId(R.id.rv_attachment_preview)).perform(viewAction)
    }


    protected fun intendingAttachProduct(totalProductAttached: Int) {
        Intents.intending(IntentMatchers.hasExtra(ApplinkConst.AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY, TopChatInternalRouter.Companion.SOURCE_TOPCHAT))
                .respondWith(
                        Instrumentation.ActivityResult(
                                Activity.RESULT_OK, getAttachProductData(totalProductAttached)
                        )
                )
    }

    protected fun getAttachProductData(totalProduct: Int): Intent {
        val products = ArrayList<ResultProduct>(totalProduct)
        for (i in 0 until totalProduct) {
            products.add(
                    ResultProduct(
                            "11111",
                            "tokopedia://product/1111",
                            ProductPreviewAttribute.productThumbnail,
                            "Rp ${i + 1}5.000.000",
                            "${i + 1} ${ProductPreviewAttribute.productName}"
                    )
            )
        }
        return Intent().apply {
            putParcelableArrayListExtra(
                    ApplinkConst.AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY, products
            )
        }
    }

    protected fun waitForIt(timeMillis: Long) {
        Thread.sleep(timeMillis)
    }
}

/*
    Extension function below
 */

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

fun ChatSmartReplyQuestionResponse.hasQuestion(
        hasQuestion: Boolean
): ChatSmartReplyQuestionResponse {
    chatSmartReplyQuestion.hasQuestion = hasQuestion
    return this
}