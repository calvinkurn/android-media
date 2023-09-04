package com.tokopedia.topchat.chatroom.view.activity.base

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.pressBackUnconditionally
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.attachcommon.data.ResultProduct
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_IMAGE_ANNOUNCEMENT
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.chat_common.domain.pojo.Reply
import com.tokopedia.chat_common.domain.pojo.imageannouncement.ImageAnnouncementPojo
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.config.GlobalConfig
import com.tokopedia.picker.common.EXTRA_RESULT_PICKER
import com.tokopedia.picker.common.PickerResult
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.R
import com.tokopedia.topchat.action.ClickChildViewWithIdAction
import com.tokopedia.topchat.chatroom.di.ChatRoomContextModule
import com.tokopedia.topchat.chatroom.domain.mapper.TopChatRoomGetExistingChatMapper
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
import com.tokopedia.topchat.chatroom.view.custom.FlexBoxChatLayout
import com.tokopedia.topchat.chatroom.view.fragment.TopChatRoomFragment
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatViewModel
import com.tokopedia.topchat.chattemplate.domain.pojo.GetChatTemplateResponse
import com.tokopedia.topchat.common.TopChatInternalRouter
import com.tokopedia.topchat.common.network.TopchatCacheManager
import com.tokopedia.topchat.common.websocket.FakeTopchatWebSocket
import com.tokopedia.topchat.stub.chatroom.di.ChatComponentStub
import com.tokopedia.topchat.stub.chatroom.di.DaggerChatComponentStub
import com.tokopedia.topchat.stub.chatroom.usecase.AddToCartOccMultiUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.AddToCartUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.AddToWishlistV2UseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.ChatAttachmentUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.ChatListGroupStickerUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.ChatListStickerUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.ChatToggleBlockChatUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.CloseReminderTickerStub
import com.tokopedia.topchat.stub.chatroom.usecase.GetChatBackgroundUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.GetChatPreAttachPayloadUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.GetChatRoomSettingUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.GetChatUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.GetExistingMessageIdUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.GetKeygenUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.GetReminderTickerUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.GetShopFollowingUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.GetSmartReplyQuestionUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.OrderProgressUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.ReplyChatGQLUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.ToggleFavouriteShopUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.TopchatUploadImageUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.UnsendReplyUseCaseStub
import com.tokopedia.topchat.stub.chatroom.view.activity.TopChatRoomActivityStub
import com.tokopedia.topchat.stub.chattemplate.usecase.GetTemplateUseCaseStub
import com.tokopedia.topchat.stub.common.DefaultWebsocketPayloadFakeGenerator
import com.tokopedia.topchat.stub.common.di.DaggerFakeBaseAppComponent
import com.tokopedia.topchat.stub.common.di.module.FakeAppModule
import com.tokopedia.topchat.stub.common.usecase.MutationMoveChatToTrashUseCaseStub
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.WebSocketResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import java.sql.Date
import java.text.SimpleDateFormat
import javax.inject.Inject

abstract class TopchatRoomTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        TopChatRoomActivityStub::class.java,
        false,
        false
    )

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext

    private val rvChatRoomId = R.id.recycler_view_chatroom
    private val flexBoxBubbleId = R.id.fxChat

    @Inject
    protected lateinit var getChatUseCase: GetChatUseCaseStub

    @Inject
    protected lateinit var reminderTickerUseCase: GetReminderTickerUseCaseStub

    @Inject
    protected lateinit var closeReminderTicker: CloseReminderTickerStub

    @Inject
    protected lateinit var chatAttachmentUseCase: ChatAttachmentUseCaseStub

    @Inject
    protected lateinit var stickerGroupUseCase: ChatListGroupStickerUseCaseStub

    @Inject
    protected lateinit var chatListStickerUseCase: ChatListStickerUseCaseStub

    @Inject
    protected lateinit var getTemplateChatRoomUseCase: GetTemplateUseCaseStub

    @Inject
    protected lateinit var getShopFollowingUseCaseStub: GetShopFollowingUseCaseStub

    @Inject
    protected lateinit var uploadImageUseCase: TopchatUploadImageUseCaseStub

    @Inject
    protected lateinit var replyChatGQLUseCase: ReplyChatGQLUseCaseStub

    @Inject
    protected lateinit var chatSrwUseCase: GetSmartReplyQuestionUseCaseStub

    @Inject
    protected lateinit var chatBackgroundUseCase: GetChatBackgroundUseCaseStub

    @Inject
    protected lateinit var websocket: FakeTopchatWebSocket

    @Inject
    protected lateinit var getExistingMessageIdUseCaseNewStub: GetExistingMessageIdUseCaseStub

    @Inject
    protected lateinit var toggleFavouriteShopUseCaseStub: ToggleFavouriteShopUseCaseStub

    @Inject
    protected lateinit var getKeygenUseCase: GetKeygenUseCaseStub

    @Inject
    protected lateinit var getChatRoomSettingUseCase: GetChatRoomSettingUseCaseStub

    @Inject
    protected lateinit var orderProgressUseCase: OrderProgressUseCaseStub

    @Inject
    protected lateinit var addToCartOccMultiUseCase: AddToCartOccMultiUseCaseStub

    @Inject
    protected lateinit var addToCartUseCase: AddToCartUseCaseStub

    @Inject
    protected lateinit var moveChatToTrashUseCase: MutationMoveChatToTrashUseCaseStub

    @Inject
    protected lateinit var existingChatMapper: TopChatRoomGetExistingChatMapper

    @Inject
    protected lateinit var unsendReplyUseCase: UnsendReplyUseCaseStub

    @Inject
    protected lateinit var chatToggleBlockChatUseCase: ChatToggleBlockChatUseCaseStub

    @Inject
    protected lateinit var getChatPreAttachPayloadUseCase: GetChatPreAttachPayloadUseCaseStub

    @Inject
    protected lateinit var addToWishlistV2UseCase: AddToWishlistV2UseCaseStub

    @Inject
    protected lateinit var cacheManager: TopchatCacheManager

    @Inject
    protected lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var abTestPlatform: AbTestPlatform

    @Inject
    lateinit var remoteConfig: RemoteConfig

    @Inject
    lateinit var webSocketPayloadGenerator: DefaultWebsocketPayloadFakeGenerator

    protected open lateinit var activity: TopChatRoomActivityStub

    protected var firstPageChatAsBuyer = GetExistingChatPojo()
    protected var firstPageChatAsSeller = GetExistingChatPojo()
    protected var chatAttachmentResponse = ChatAttachmentResponse()
    protected var stickerGroupAsBuyer = ChatListGroupStickerResponse()
    protected var stickerListAsBuyer = StickerResponse()
    protected var firstPageChatBroadcastAsBuyer = GetExistingChatPojo()
    protected var getShopFollowingStatus = ShopFollowingPojo()
    protected var chatSrwResponse = ChatSmartReplyQuestionResponse()
    protected var orderProgressResponse = OrderProgressResponse()
    private var chatBackgroundResponse = ChatBackgroundResponse()
    protected var chatRoomSettingResponse = RoomSettingResponse()
    protected var successGetTemplateResponse = GetChatTemplateResponse()

    object ProductPreviewAttribute {
        const val productName = "Testing Attach Product 1"
        const val productThumbnail = "https://images.tokopedia.net/img/cache/350/attachment/" +
            "2020/8/24/40768394/40768394_732546f9-371d-45c6-a412-451ea50aa22c.jpg.webp"
        const val productPrice = "Rp 23.000.000"
    }

    companion object {
        const val MSG_ID = "66961"
        const val EX_PRODUCT_ID = "1111"
        var chatComponentStub: ChatComponentStub? = null
        var keyboardStateIdling: CountingIdlingResource? = null
    }

    @Before
    open fun before() {
        setupDaggerComponent()
        setupResponse()
        setupDefaultResponseWhenFirstOpenChatRoom()
        setupDummyImageChatService()
        setupKeyboardIdlingResource()
        disableUploadImageByService()
    }

    protected open fun enableUploadImageByService() {
        remoteConfig.setString(
            TopChatViewModel.ENABLE_UPLOAD_IMAGE_SERVICE,
            "true"
        )
    }

    protected open fun disableUploadImageByService() {
        remoteConfig.setString(
            TopChatViewModel.ENABLE_UPLOAD_IMAGE_SERVICE,
            "false"
        )
    }

    protected open fun enableUploadSecure() {
        abTestPlatform.setString(
            TopChatRoomFragment.ROLLENCE_UPLOAD_SECURE,
            TopChatRoomFragment.ROLLENCE_UPLOAD_SECURE
        )
    }

    protected open fun disableUploadSecure() {
        abTestPlatform.setString(
            TopChatRoomFragment.ROLLENCE_UPLOAD_SECURE,
            ""
        )
    }

    @After
    open fun tearDown() {
        IdlingRegistry.getInstance().unregister(keyboardStateIdling)
        chatComponentStub = null
        keyboardStateIdling = null
        GlobalConfig.APPLICATION_TYPE = GlobalConfig.CONSUMER_APPLICATION
        disableUploadImageByService()
    }

    protected open fun setupResponse() {
        firstPageChatAsSeller = getChatUseCase.defaultChatWithSellerResponse
        firstPageChatAsBuyer = getChatUseCase.defaultChatWithBuyerResponse
        firstPageChatBroadcastAsBuyer = getChatUseCase.defaultBroadCastChatWithBuyerResponse

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
        getShopFollowingStatus = AndroidFileUtil.parse(
            "success_get_shop_following_status.json",
            ShopFollowingPojo::class.java
        )
        chatRoomSettingResponse = AndroidFileUtil.parse(
            "success_get_chat_setting_fraud_alert.json",
            RoomSettingResponse::class.java
        )
        successGetTemplateResponse = AndroidFileUtil.parse(
            "template/success_get_template.json",
            GetChatTemplateResponse::class.java
        )
    }

    private fun setupDaggerComponent() {
        val baseComponent = DaggerFakeBaseAppComponent.builder()
            .fakeAppModule(FakeAppModule(applicationContext))
            .build()
        chatComponentStub = DaggerChatComponentStub.builder()
            .fakeBaseAppComponent(baseComponent)
            .chatRoomContextModule(ChatRoomContextModule(context))
            .build()
        chatComponentStub!!.inject(this)
    }

    private fun setupDefaultResponseWhenFirstOpenChatRoom() {
        getChatRoomSettingUseCase.response = chatRoomSettingResponse
        chatBackgroundUseCase.response = chatBackgroundResponse
        getChatUseCase.response = firstPageChatAsBuyer
        orderProgressUseCase.response = orderProgressResponse
        chatAttachmentUseCase.response = chatAttachmentResponse
        stickerGroupUseCase.response = stickerGroupAsBuyer
        chatListStickerUseCase.response = stickerListAsBuyer
        chatSrwUseCase.response = chatSrwResponse
        getShopFollowingUseCaseStub.response = getShopFollowingStatus
        getTemplateChatRoomUseCase.response = successGetTemplateResponse
        toggleFavouriteShopUseCaseStub.response = true
    }

    private fun setupDummyImageChatService() {
        UploadImageChatService.dummyMap.clear()
    }

    private fun setupKeyboardIdlingResource() {
        keyboardStateIdling = CountingIdlingResource("ChatRoom-Keyboard")
        IdlingRegistry.getInstance().register(keyboardStateIdling)
    }

    protected fun launchChatRoomActivity(
        sourcePage: String? = null,
        isSellerApp: Boolean = false,
        intentModifier: (Intent) -> Unit = {}
    ) {
        if (isSellerApp) {
            GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION
        }
        val intent = Intent().apply {
            putExtra(ApplinkConst.Chat.MESSAGE_ID, MSG_ID)
            sourcePage?.let {
                putExtra(ApplinkConst.Chat.SOURCE_PAGE, it)
            }
        }
        intentModifier(intent)
        activityTestRule.launchActivity(intent)
        activity = activityTestRule.activity
    }

    protected fun finishChatRoomActivity() {
        pressBackUnconditionally()
        activityTestRule.finishActivity()
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

    protected fun changeResponseWebSocket(
        response: WebSocketResponse,
        webSocketModifier: (WebSocketResponse) -> Unit
    ) {
        webSocketModifier(response)
    }

    protected fun finishActivity() {
        activityTestRule.finishActivity()
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

    protected fun intendingAttachProduct(totalProductAttached: Int) {
        intending(
            IntentMatchers.hasExtra(
                ApplinkConst.AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY,
                TopChatInternalRouter.Companion.SOURCE_TOPCHAT
            )
        )
            .respondWith(
                Instrumentation.ActivityResult(
                    Activity.RESULT_OK,
                    getAttachProductData(totalProductAttached)
                )
            )
    }

    protected open fun getAttachProductData(totalProduct: Int): Intent {
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
                ApplinkConst.AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY,
                products
            )
        }
    }

    protected fun getImageData(): Intent {
        return Intent().apply {
            putExtra(
                EXTRA_RESULT_PICKER,
                PickerResult(
                    originalPaths = listOf("https://images.tokopedia.net/img/LUZQDL/2021/3/18/fa23883b-4188-417b-ab8d-21255f62a324.jpg")
                )
            )
        }
    }

    protected fun getBubbleMsgAtPosition(position: Int): CharSequence {
        val rv = activity.findViewById<RecyclerView>(rvChatRoomId)
        (rv.layoutManager as? LinearLayoutManager)?.let {
            val child = it.getChildAt(position)
            val flexBox = child?.findViewById<FlexBoxChatLayout>(flexBoxBubbleId)
            return flexBox?.message?.text ?: ""
        }
        return ""
    }

    protected fun stubIntents() {
        intending(anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    protected fun putProductAttachmentIntent(
        intent: Intent,
        listOfProducts: List<String> = listOf(EX_PRODUCT_ID)
    ) {
        val stringProductPreviews = CommonUtil.toJson(listOfProducts)
        intent.putExtra(ApplinkConst.Chat.PRODUCT_PREVIEWS, stringProductPreviews)
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

fun GetExistingChatPojo.changeCtaBroadcast(
    url: String?,
    text: String?,
    label: String?
): GetExistingChatPojo {
    val banner: Reply? = chatReplies.list[0].chats[0].replies.find { reply ->
        reply.attachment.type.toString() == TYPE_IMAGE_ANNOUNCEMENT
    }
    val attribute = CommonUtil.fromJson<ImageAnnouncementPojo>(
        banner?.attachment?.attributes,
        ImageAnnouncementPojo::class.java
    ).apply {
        this.broadcastCtaUrl = url
        this.broadcastCtaText = text
        this.broadcastCtaLabel = label
    }
    banner?.attachment?.attributes = CommonUtil.toJson(attribute)
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
    val startTime = SimpleDateFormat(FakeTopchatWebSocket.START_TIME_FORMAT).format(date)
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
    productId: String,
    productImageUrl: String,
    productName: String,
    productPrice: String
): WebSocketResponse {
    val attrs = jsonObject?.getAsJsonObject("attachment")
        ?.getAsJsonObject("attributes")
    val attrProductProfile = attrs?.getAsJsonObject("product_profile")
    attrs?.apply {
        addProperty("product_id", productId)
    }
    attrProductProfile?.apply {
        addProperty("image_url", productImageUrl)
        addProperty("name", productName)
        addProperty("price", productPrice)
    }
    return this
}
