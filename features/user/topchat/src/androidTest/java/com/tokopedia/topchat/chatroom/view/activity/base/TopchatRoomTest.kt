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
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.attachcommon.data.ResultProduct
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_IMAGE_ANNOUNCEMENT
import com.tokopedia.chat_common.data.preview.ProductPreview
import com.tokopedia.chat_common.domain.pojo.ChatReplyPojo
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.chat_common.domain.pojo.Reply
import com.tokopedia.chat_common.domain.pojo.imageannouncement.ImageAnnouncementPojo
import com.tokopedia.chat_common.view.adapter.viewholder.chatmenu.AttachmentItemViewHolder
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.config.GlobalConfig
import com.tokopedia.imagepicker.common.PICKER_RESULT_PATHS
import com.tokopedia.imagepicker.common.RESULT_IMAGES_FED_INTO_IMAGE_PICKER
import com.tokopedia.imagepicker.common.RESULT_PREVIOUS_IMAGE
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.R
import com.tokopedia.topchat.action.ClickChildViewWithIdAction
import com.tokopedia.topchat.chatroom.di.ChatRoomContextModule
import com.tokopedia.topchat.chatroom.domain.pojo.FavoriteData.Companion.IS_FOLLOW
import com.tokopedia.topchat.chatroom.domain.pojo.ShopFollowingPojo
import com.tokopedia.topchat.chatroom.domain.pojo.background.ChatBackgroundResponse
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse
import com.tokopedia.topchat.chatroom.domain.pojo.orderprogress.OrderProgressResponse
import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingResponse
import com.tokopedia.topchat.chatroom.domain.pojo.srw.ChatSmartReplyQuestionResponse
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.StickerResponse
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.ChatListGroupStickerResponse
import com.tokopedia.topchat.chatroom.service.UploadImageChatService
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.TopchatProductAttachmentViewHolder
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import com.tokopedia.topchat.common.TopChatInternalRouter
import com.tokopedia.topchat.idling.FragmentTransactionIdle
import com.tokopedia.topchat.isKeyboardOpened
import com.tokopedia.topchat.matchers.hasSrwBubble
import com.tokopedia.topchat.matchers.withRecyclerView
import com.tokopedia.topchat.matchers.withTotalItem
import com.tokopedia.topchat.stub.chatroom.di.ChatComponentStub
import com.tokopedia.topchat.stub.chatroom.di.DaggerChatComponentStub
import com.tokopedia.topchat.stub.chatroom.usecase.*
import com.tokopedia.topchat.stub.chatroom.view.activity.TopChatRoomActivityStub
import com.tokopedia.topchat.stub.chatroom.websocket.RxWebSocketUtilStub
import com.tokopedia.topchat.stub.chatroom.websocket.RxWebSocketUtilStub.Companion.START_TIME_FORMAT
import com.tokopedia.topchat.stub.common.di.DaggerFakeBaseAppComponent
import com.tokopedia.topchat.stub.common.di.module.FakeAppModule
import com.tokopedia.websocket.WebSocketResponse
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import java.sql.Date
import java.text.SimpleDateFormat
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
    protected lateinit var orderProgressUseCase: OrderProgressUseCaseStub

    @Inject
    protected lateinit var chatBackgroundUseCase: ChatBackgroundUseCaseStub

    @Inject
    protected lateinit var getChatRoomSettingUseCase: GetChatRoomSettingUseCaseStub

    @Inject
    protected lateinit var websocket: RxWebSocketUtilStub

    protected open lateinit var activity: TopChatRoomActivityStub
    protected open lateinit var fragmentTransactionIdling: FragmentTransactionIdle

    protected var firstPageChatAsBuyer = GetExistingChatPojo()
    protected var firstPageChatAsSeller = GetExistingChatPojo()
    protected var chatAttachmentResponse = ChatAttachmentResponse()
    protected var stickerGroupAsBuyer = ChatListGroupStickerResponse()
    protected var stickerListAsBuyer = StickerResponse()
    protected var firstPageChatBroadcastAsBuyer = GetExistingChatPojo()
    protected var getShopFollowingStatus = ShopFollowingPojo()
    protected var chatSrwResponse = ChatSmartReplyQuestionResponse()
    protected var uploadImageReplyResponse = ChatReplyPojo()
    protected var orderProgressResponse = OrderProgressResponse()
    protected var chatBackgroundResponse = ChatBackgroundResponse()
    protected var chatRoomSettingResponse = RoomSettingResponse()

    object ProductPreviewAttribute {
        const val productName = "Testing Attach Product 1"
        const val productThumbnail = "https://ecs7-p.tokopedia.net/img/cache/350/attachment/" +
                "2020/8/24/40768394/40768394_732546f9-371d-45c6-a412-451ea50aa22c.jpg.webp"
    }

    companion object {
        const val MSG_ID = "66961"
        var chatComponentStub: ChatComponentStub? = null
        var keyboardStateIdling: CountingIdlingResource? = null
    }

    @Before
    open fun before() {
        setupResponse()
        val baseComponent = DaggerFakeBaseAppComponent.builder()
            .fakeAppModule(FakeAppModule(applicationContext))
            .build()
        chatComponentStub = DaggerChatComponentStub.builder()
            .fakeBaseAppComponent(baseComponent)
            .chatRoomContextModule(ChatRoomContextModule(context))
            .build()
        chatComponentStub!!.inject(this)
        setupDefaultResponseWhenFirstOpenChatRoom()
        UploadImageChatService.dummyMap.clear()
        keyboardStateIdling = CountingIdlingResource("ChatRoom-Keyboard")
        IdlingRegistry.getInstance().register(keyboardStateIdling)
    }

    @After
    open fun tearDown() {
        IdlingRegistry.getInstance().unregister(keyboardStateIdling)
        chatComponentStub = null
        keyboardStateIdling = null
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

    protected fun setupDefaultResponseWhenFirstOpenChatRoom() {
        getChatRoomSettingUseCase.response = chatRoomSettingResponse
        chatBackgroundUseCase.response = chatBackgroundResponse
        getChatUseCase.response = firstPageChatAsBuyer
        orderProgressUseCase.response = orderProgressResponse
        chatAttachmentUseCase.response = chatAttachmentResponse
        stickerGroupUseCase.response = stickerGroupAsBuyer
        chatListStickerUseCase.response = stickerListAsBuyer
        chatSrwUseCase.response = chatSrwResponse
        getShopFollowingUseCaseStub.response = getShopFollowingStatus
        getTemplateChatRoomUseCase.response = generateTemplateResponse(true)
    }

    protected fun setupChatRoomActivity(
        sourcePage: String? = null,
        isSellerApp: Boolean = false,
        intentModifier: (Intent) -> Unit = {}
    ) {
        val intent = Intent().apply {
            putExtra(ApplinkConst.Chat.MESSAGE_ID, MSG_ID)
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
        activity.setupTestFragment()
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

    protected fun clickAttachInvoiceMenu() {
        val viewAction = RecyclerViewActions
            .actionOnItemAtPosition<AttachmentItemViewHolder>(
                2, click()
            )
        onView(withId(R.id.rv_topchat_attachment_menu))
            .perform(viewAction)
    }

    protected fun clickComposeArea() {
        onView(withId(R.id.new_comment))
            .perform(click())
    }

    protected fun typeMessage(msg: String) {
        onView(withId(R.id.new_comment))
            .perform(typeText(msg))
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

    protected fun clickSrwPreviewExpandCollapse() {
        onView(
            allOf(
                withId(R.id.rv_srw_content_container),
                isDescendantOfA(withId(R.id.cl_attachment_preview))
            )
        ).perform(click())
    }

    protected fun clickSrwBubbleExpandCollapse(position: Int) {
        onView(
            withRecyclerView(R.id.recycler_view).atPositionOnView(
                position, R.id.tp_srw_container_partial
            )
        ).perform(click())
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

    protected fun assertEmptyStockLabelOnProductCard(
        recyclerViewId: Int,
        atPosition: Int,
    ) {
        assertLabelTextOnProductCard(recyclerViewId, atPosition, "Stok habis")
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

    protected fun assertSrwPreviewContentContainerVisibility(
        visibilityMatcher: Matcher<in View>
    ) {
        onView(
            allOf(
                withId(R.id.rv_srw_content_container),
                isDescendantOfA(withId(R.id.cl_attachment_preview))
            )
        ).check(matches(visibilityMatcher))
    }

    protected fun assertSrwBubbleContentContainerVisibility(
        position: Int,
        visibilityMatcher: Matcher<in View>
    ) {
        onView(
            withRecyclerView(R.id.recycler_view).atPositionOnView(
                position, R.id.rv_srw_content_container
            )
        ).check(matches(visibilityMatcher))
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

    protected fun assertSrwPreviewErrorVisibility(
        visibilityMatcher: Matcher<in View>
    ) {
        onView(
            allOf(
                withId(R.id.ll_srw_partial),
                isDescendantOfA(withId(R.id.cl_attachment_preview))
            )
        ).check(matches(visibilityMatcher))
    }

    protected fun assertSrwBubbleErrorVisibility(
        position: Int,
        visibilityMatcher: Matcher<in View>
    ) {
        onView(
            withRecyclerView(R.id.recycler_view).atPositionOnView(
                position, R.id.ll_srw_partial
            )
        ).check(matches(visibilityMatcher))
    }

    protected fun assertSrwPreviewLoadingVisibility(
        visibilityMatcher: Matcher<in View>
    ) {
        onView(
            allOf(
                withId(R.id.lu_srw_partial),
                isDescendantOfA(withId(R.id.cl_attachment_preview))
            )
        ).check(matches(visibilityMatcher))
    }

    protected fun assertSrwBubbleLoadingVisibility(
        position: Int,
        visibilityMatcher: Matcher<in View>
    ) {
        onView(
            withRecyclerView(R.id.recycler_view).atPositionOnView(
                position, R.id.lu_srw_partial
            )
        ).check(matches(visibilityMatcher))
    }

    protected fun assertSnackbarText(msg: String) {
        onView(withText(msg)).check(matches(isDisplayed()))
    }

    protected fun assertSrwPreviewContentIsVisible() {
        assertSrwPreviewContentContainerVisibility(isDisplayed())
        assertTemplateChatVisibility(not(isDisplayed()))
        assertSrwPreviewErrorVisibility(not(isDisplayed()))
        assertSrwPreviewLoadingVisibility(not(isDisplayed()))
    }

    protected fun assertSrwBubbleDoesNotExist() {
        onView(withId(R.id.recycler_view)).check(matches(not(hasSrwBubble())))
    }

    protected fun assertSrwBubbleContentIsVisibleAt(
        position: Int
    ) {
        assertSrwBubbleContentContainerVisibility(position, isDisplayed())
        assertTemplateChatVisibility(not(isDisplayed()))
        assertSrwBubbleErrorVisibility(position, not(isDisplayed()))
        assertSrwBubbleLoadingVisibility(position, not(isDisplayed()))
    }

    protected fun assertSrwPreviewContentIsLoading() {
        assertSrwPreviewLoadingVisibility(isDisplayed())
        assertSrwPreviewContentContainerVisibility(not(isDisplayed()))
        assertTemplateChatVisibility(not(isDisplayed()))
        assertSrwPreviewErrorVisibility(not(isDisplayed()))
    }

    protected fun assertSrwPreviewContentIsError() {
        assertSrwPreviewErrorVisibility(isDisplayed())
        assertSrwPreviewLoadingVisibility(not(isDisplayed()))
        assertSrwPreviewContentContainerVisibility(not(isDisplayed()))
        assertTemplateChatVisibility(not(isDisplayed()))
    }

    protected fun assertSrwPreviewContentIsHidden() {
        assertSrwPreviewContentContainerVisibility(not(isDisplayed()))
        assertSrwPreviewErrorVisibility(not(isDisplayed()))
        assertSrwPreviewLoadingVisibility(not(isDisplayed()))
    }

    protected fun assertHeaderRightMsgBubbleVisibility(
        position: Int, visibilityMatcher: Matcher<in View>
    ) {
        onView(
            withRecyclerView(R.id.recycler_view).atPositionOnView(
                position, R.id.tvRole
            )
        ).check(matches(visibilityMatcher))
    }

    protected fun assertHeaderRightMsgBubbleText(position: Int, msg: String) {
        onView(
            withRecyclerView(R.id.recycler_view).atPositionOnView(
                position, R.id.tvRole
            )
        ).check(matches(withText(msg)))
    }

    protected fun assertHeaderRightMsgBubbleBlueDotVisibility(
        position: Int, visibilityMatcher: Matcher<in View>
    ) {
        onView(
            withRecyclerView(R.id.recycler_view).atPositionOnView(
                position, R.id.img_sr_blue_dot
            )
        ).check(matches(visibilityMatcher))
    }

    protected fun assertKeyboardIsVisible() {
        val isKeyboardOpened = isKeyboardOpened()
        assertThat(isKeyboardOpened, `is`(true))
    }

    protected fun assertKeyboardIsNotVisible() {
        val isKeyboardOpened = isKeyboardOpened()
        assertThat(isKeyboardOpened, `is`(false))
    }

    protected fun assertChatMenuVisibility(visibilityMatcher: Matcher<in View>) {
        onView(withId(R.id.fl_chat_menu)).check(
            matches(visibilityMatcher)
        )
    }

    protected fun assertChatAttachmentMenuVisibility(visibilityMatcher: Matcher<in View>) {
        onView(withId(R.id.rv_topchat_attachment_menu)).check(
            matches(visibilityMatcher)
        )
    }

    protected fun assertChatStickerMenuVisibility(
        visibilityMatcher: Matcher<in View>
    ) {
        onView(withId(R.id.ll_sticker_container)).check(
            matches(visibilityMatcher)
        )
    }

    protected fun assertComposedTextValue(msg: String) {
        onView(withId(R.id.new_comment)).check(
            matches(withText(msg))
        )
    }

    protected fun isKeyboardOpened(): Boolean {
        val rootView = activity.findViewById<View>(R.id.main)
        return isKeyboardOpened(rootView)
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

    protected fun clickStickerAtPosition(position: Int) {
        val viewAction = RecyclerViewActions
            .actionOnItemAtPosition<TopchatProductAttachmentViewHolder>(
                position,
                ClickChildViewWithIdAction()
                    .clickChildViewWithId(R.id.iv_sticker)
            )
        onView(withId(R.id.rv_sticker)).perform(viewAction)
    }

    protected fun clickSrwPreviewItemAt(position: Int) {
        onView(
            Matchers.allOf(
                withRecyclerView(R.id.rv_srw_partial).atPositionOnView(
                    position, R.id.tp_srw_title
                ),
                isDescendantOfA(withId(R.id.cl_attachment_preview))
            )
        ).perform(click())
    }

    protected fun scrollChatToPosition(position: Int) {
        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position)
        )
    }

    protected fun intendingAttachProduct(totalProductAttached: Int) {
        Intents.intending(
            IntentMatchers.hasExtra(
                ApplinkConst.AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY,
                TopChatInternalRouter.Companion.SOURCE_TOPCHAT
            )
        )
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

    protected fun getImageData(): Intent {
        return Intent().apply {
            putStringArrayListExtra(
                PICKER_RESULT_PATHS,
                arrayListOf("https://images.tokopedia.net/img/LUZQDL/2021/3/18/fa23883b-4188-417b-ab8d-21255f62a324.jpg")
            )
            putStringArrayListExtra(
                RESULT_PREVIOUS_IMAGE,
                arrayListOf("https://images.tokopedia.net/img/LUZQDL/2021/3/18/fa23883b-4188-417b-ab8d-21255f62a324.jpg")
            )
            putStringArrayListExtra(
                RESULT_IMAGES_FED_INTO_IMAGE_PICKER,
                arrayListOf("https://images.tokopedia.net/img/LUZQDL/2021/3/18/fa23883b-4188-417b-ab8d-21255f62a324.jpg")
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

fun WebSocketResponse.changeTimeStampTo(
    timeMillis: Long
): WebSocketResponse {
    val date = Date(timeMillis)
    val startTime = SimpleDateFormat(START_TIME_FORMAT).format(date)
    val msg = jsonObject?.getAsJsonObject("message")
    msg?.apply {
        addProperty("timestamp", startTime)
        addProperty("timestamp_fmt", startTime)
        addProperty("timestamp_unix", timeMillis)
        addProperty("timestamp_unix_nano", timeMillis * 1_000_000)
    }
    jsonObject?.apply {
        addProperty("start_time", startTime)
    }
    return this
}

fun WebSocketResponse.changeProductIdTo(
    productId: String
): WebSocketResponse {
    val attrs = jsonObject?.getAsJsonObject("attachment")
        ?.getAsJsonObject("attributes")
    attrs?.apply {
        addProperty("product_id", productId)
    }
    return this
}

fun WebSocketResponse.matchProductWith(
    productPreview: ProductPreview
): WebSocketResponse {
    val attrs = jsonObject?.getAsJsonObject("attachment")
        ?.getAsJsonObject("attributes")
    val attrProductProfile = attrs?.getAsJsonObject("product_profile")
    attrs?.apply {
        addProperty("product_id", productPreview.id)
    }
    attrProductProfile?.apply {
        addProperty("image_url", productPreview.imageUrl)
        addProperty("name", productPreview.name)
        addProperty("price", productPreview.price)
    }
    return this
}