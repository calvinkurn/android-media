package com.tokopedia.topchat.chatroom.view.presenter

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.collection.ArrayMap
import androidx.lifecycle.Observer
import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.attachcommon.data.ResultProduct
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.ProductAttachmentViewModel
import com.tokopedia.chat_common.data.preview.ProductPreview
import com.tokopedia.chatbot.domain.mapper.TopChatRoomWebSocketMessageMapper
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.seamless_login_common.subscriber.SeamlessLoginSubscriber
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.topchat.FileUtil
import com.tokopedia.topchat.chatroom.data.UploadImageDummy
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.Attachment
import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.ChatSettingsResponse
import com.tokopedia.topchat.chatroom.domain.pojo.orderprogress.OrderProgressResponse
import com.tokopedia.topchat.chatroom.domain.pojo.srw.ChatSmartReplyQuestionResponse
import com.tokopedia.topchat.chatroom.domain.pojo.srw.QuestionUiModel
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.Sticker
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.ChatListGroupStickerResponse
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.StickerGroup
import com.tokopedia.topchat.chatroom.domain.pojo.tokonow.ChatTokoNowWarehouse
import com.tokopedia.topchat.chatroom.domain.pojo.tokonow.ChatTokoNowWarehouseResponse
import com.tokopedia.topchat.chatroom.domain.usecase.*
import com.tokopedia.topchat.chatroom.service.UploadImageBroadcastListener
import com.tokopedia.topchat.chatroom.service.UploadImageChatService
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory
import com.tokopedia.topchat.chatroom.view.listener.TopChatContract
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.exMessageId
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.exOpponentId
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.exProductId
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.exResultProduct
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.exSendMessage
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.exShopId
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.exStartTime
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.exSticker
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.exUrl
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.exUserId
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.generateSendAbleInvoicePreview
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.generateSendAbleProductPreview
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.successGetChatListGroupSticker
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.successGetOrderProgressResponse
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.wsResponseEndTypingString
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.wsResponseImageAttachmentString
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.wsResponseProductAttachmentString
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.wsResponseReadMessageString
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.wsResponseReplyString
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.wsResponseTypingString
import com.tokopedia.topchat.chatroom.view.viewmodel.InvoicePreviewUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.SendablePreview
import com.tokopedia.topchat.chatroom.view.viewmodel.SendableProductPreview
import com.tokopedia.topchat.common.data.Resource
import com.tokopedia.topchat.common.domain.MutationMoveChatToTrashUseCase
import com.tokopedia.topchat.common.util.ImageUtil
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.RxWebSocket
import com.tokopedia.websocket.RxWebSocketUtil
import com.tokopedia.websocket.WebSocketInfo
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.impl.annotations.SpyK
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.flow.flow
import okhttp3.Interceptor
import okhttp3.WebSocket
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Observable
import rx.Subscriber
import rx.subjects.PublishSubject

class TopChatRoomPresenterTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var tkpdAuthInterceptor: TkpdAuthInterceptor

    @RelaxedMockK
    private lateinit var fingerprintInterceptor: FingerprintInterceptor

    @RelaxedMockK
    private lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    private lateinit var webSocketUtil: RxWebSocketUtil

    @RelaxedMockK
    private lateinit var getChatUseCase: GetChatUseCase

    @RelaxedMockK
    private lateinit var getTemplateChatRoomUseCase: GetTemplateChatRoomUseCase

    @RelaxedMockK
    private lateinit var replyChatUseCase: ReplyChatUseCase

    @RelaxedMockK
    private lateinit var getExistingMessageIdUseCase: GetExistingMessageIdUseCase

    @RelaxedMockK
    private lateinit var getShopFollowingUseCase: GetShopFollowingUseCase

    @RelaxedMockK
    private lateinit var toggleFavouriteShopUseCase: ToggleFavouriteShopUseCase

    @RelaxedMockK
    private lateinit var addToCartUseCase: AddToCartUseCase

    @RelaxedMockK
    private lateinit var compressImageUseCase: CompressImageUseCase

    @RelaxedMockK
    private lateinit var seamlessLoginUsecase: SeamlessLoginUsecase

    @RelaxedMockK
    private lateinit var getChatRoomSettingUseCase: GetChatRoomSettingUseCase

    @RelaxedMockK
    private lateinit var addWishListUseCase: AddWishListUseCase

    @RelaxedMockK
    private lateinit var removeWishListUseCase: RemoveWishListUseCase

    @RelaxedMockK
    private lateinit var uploadImageUseCase: TopchatUploadImageUseCase

    @RelaxedMockK
    private lateinit var orderProgressUseCase: OrderProgressUseCase

    @RelaxedMockK
    private lateinit var groupStickerUseCase: ChatListGroupStickerUseCase

    @RelaxedMockK
    private lateinit var chatAttachmentUseCase: ChatAttachmentUseCase

    @RelaxedMockK
    private lateinit var chatToggleBlockChat: ChatToggleBlockChatUseCase

    @RelaxedMockK
    private lateinit var chatBackgroundUseCase: ChatBackgroundUseCase

    @RelaxedMockK
    private lateinit var replyChatGQLUseCase: ReplyChatGQLUseCase

    @RelaxedMockK
    private lateinit var sharedPref: SharedPreferences

    private val dispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider

    @RelaxedMockK
    private lateinit var webSocket: WebSocket

    @RelaxedMockK
    private lateinit var view: TopChatContract.View

    @RelaxedMockK
    private lateinit var uploadImageBroadcastListener: UploadImageBroadcastListener

    @RelaxedMockK
    private lateinit var sendAbleProductPreview: SendablePreview

    @RelaxedMockK
    private lateinit var chatSrwUseCase: SmartReplyQuestionUseCase

    @RelaxedMockK
    private lateinit var tokoNowWHUsecase: ChatTokoNowWarehouseUseCase

    @RelaxedMockK
    private lateinit var moveChatToTrashUseCase: MutationMoveChatToTrashUseCase

    @RelaxedMockK
    private lateinit var remoteConfig: RemoteConfig

    @SpyK
    private var topChatRoomWebSocketMessageMapper = TopChatRoomWebSocketMessageMapper()

    private lateinit var presenter: TopChatRoomPresenter

    private lateinit var listInterceptor: ArrayList<Interceptor>

    private lateinit var wsOpen: WebSocketInfo
    private lateinit var wsReconnect: WebSocketInfo
    private lateinit var wsResponseReplyText: WebSocketInfo
    private lateinit var wsResponseTyping: WebSocketInfo
    private lateinit var wsResponseEndTyping: WebSocketInfo
    private lateinit var wsResponseReadMessage: WebSocketInfo
    private lateinit var wsResponseImageAttachment: WebSocketInfo
    private lateinit var wsResponseProductAttachment: WebSocketInfo
    private val websocketServer = PublishSubject.create<WebSocketInfo>()

    object Dummy {
        const val exMessageId = "190378584"
        const val exShopId = 423785L
        const val exProductId = "123123"
        const val exUserId = "321321"
        const val exSendMessage = "Hello World"
        const val exStartTime = "123321"
        const val exOpponentId = "39467501"
        const val exImageUrl = "https://ecs.tokopedia.com/image.jpg"
        const val exUrl = "https://tokopedia.com/url"
        const val source = "askseller"

        val exSticker = generateSticker()
        val exResultProduct = generateResultProduct()
        val wsResponseReplyString = FileUtil.readFileContent(
            "/ws_response_reply_text_is_opposite.json"
        )
        val wsResponseTypingString = FileUtil.readFileContent(
            "/ws_response_typing.json"
        )
        val wsResponseEndTypingString = FileUtil.readFileContent(
            "/ws_response_end_typing.json"
        )
        val wsResponseReadMessageString = FileUtil.readFileContent(
            "/ws_response_read_message.json"
        )
        val wsResponseImageAttachmentString = FileUtil.readFileContent(
            "/ws_response_image_attachment.json"
        )
        val wsResponseProductAttachmentString = FileUtil.readFileContent(
            "/ws_response_product_attachment.json"
        )
        val successGetOrderProgressResponse: OrderProgressResponse = FileUtil.parse(
            "/success_get_order_progress.json",
            OrderProgressResponse::class.java
        )
        val successGetChatListGroupSticker: ChatListGroupStickerResponse = FileUtil.parse(
            "/success_chat_list_group_sticker.json",
            ChatListGroupStickerResponse::class.java
        )

        fun generateSendAbleProductPreview(): SendablePreview {
            val productPreview = ProductPreview(
                name = "hello product",
                imageUrl = exImageUrl,
                price = "Rp120.000",
                id = "12398764"
            )
            return SendableProductPreview(productPreview)
        }

        fun generateSendAbleInvoicePreview(): SendablePreview {
            return InvoicePreviewUiModel(
                "1", "1", "1", "1", "http",
                "http", 200, "200", "1"
            )
        }

        private fun generateSticker(): Sticker {
            return Sticker()
        }

        private fun generateResultProduct(): ResultProduct {
            return ResultProduct(
                exProductId,
                "https://tokopedia.com/product/url",
                exImageUrl,
                "Rp12000",
                "Foo product"
            )
        }
    }

    @Before
    fun before() {
        MockKAnnotations.init(this)
        UploadImageChatService.dummyMap.clear()
        mockSingletonObject()
        presenter = spyk(
            TopChatRoomPresenter(
                tkpdAuthInterceptor,
                fingerprintInterceptor,
                userSession,
                webSocketUtil,
                getChatUseCase,
                topChatRoomWebSocketMessageMapper,
                getTemplateChatRoomUseCase,
                replyChatUseCase,
                getExistingMessageIdUseCase,
                getShopFollowingUseCase,
                toggleFavouriteShopUseCase,
                addToCartUseCase,
                compressImageUseCase,
                seamlessLoginUsecase,
                getChatRoomSettingUseCase,
                addWishListUseCase,
                removeWishListUseCase,
                uploadImageUseCase,
                orderProgressUseCase,
                groupStickerUseCase,
                chatAttachmentUseCase,
                chatToggleBlockChat,
                chatBackgroundUseCase,
                chatSrwUseCase,
                tokoNowWHUsecase,
                moveChatToTrashUseCase,
                sharedPref,
                dispatchers,
                remoteConfig
            )
        )
        presenter.attachView(view)
        presenter.autoRetryConnectWs = false
        listInterceptor = arrayListOf(tkpdAuthInterceptor, fingerprintInterceptor)
        wsReconnect = WebSocketInfo.createReconnect("Some Error Comes Up")
        wsOpen = WebSocketInfo(webSocket, true)
        wsResponseReplyText = WebSocketInfo(webSocket, wsResponseReplyString)
        wsResponseTyping = WebSocketInfo(webSocket, wsResponseTypingString)
        wsResponseEndTyping = WebSocketInfo(webSocket, wsResponseEndTypingString)
        wsResponseReadMessage = WebSocketInfo(webSocket, wsResponseReadMessageString)
        wsResponseImageAttachment = WebSocketInfo(webSocket, wsResponseImageAttachmentString)
        wsResponseProductAttachment = WebSocketInfo(webSocket, wsResponseProductAttachmentString)
    }

    @After
    fun after() {
        UploadImageChatService.dummyMap.clear()
    }

    private fun mockSingletonObject() {
        mockkObject(RxWebSocket)
        mockkObject(RxWebSocketUtil)
        mockkObject(ImageUtil)
        mockkObject(TopChatWebSocketParam)
        every { RxWebSocketUtil.getInstance(any()) } returns webSocketUtil
    }

    @Test
    fun `on success send sticker through websocket`() {
        // Given
        val mockOnSendingMessage: () -> Unit = mockk(relaxed = true)
        val stickerContract = CommonUtil.toJson(
            exSticker.generateWebSocketPayload(
                exMessageId, exOpponentId, exStartTime, emptyList()
            )
        )
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false

        // When
        presenter.connectWebSocket(exMessageId)
        presenter.sendAttachmentsAndSticker(
            exMessageId, exSticker, exStartTime, exOpponentId, mockOnSendingMessage
        )

        // Then
        verify(exactly = 1) { mockOnSendingMessage.invoke() }
        verify(exactly = 1) { RxWebSocket.send(stickerContract, listInterceptor) }
        verify(exactly = 1) { view.clearAttachmentPreviews() }
    }

    @Test
    fun `on success send SRW preview through websocket`() {
        // Given
        val srwQuestion = QuestionUiModel()
        val mockOnSendingMessage: () -> Unit = mockk(relaxed = true)
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false

        // When
        presenter.connectWebSocket(exMessageId)
        presenter.sendAttachmentsAndSrw(
            exMessageId, srwQuestion, exStartTime, exOpponentId, mockOnSendingMessage
        )

        // Then
        verify(exactly = 1) { mockOnSendingMessage.invoke() }
        verify(exactly = 1) { view.clearAttachmentPreviews() }
    }

    @Test
    fun `on success send SRW bubble through websocket`() {
        // Given
        val srwQuestion = QuestionUiModel()
        val products = listOf(generateSendAbleProductPreview())
        val mockOnSendingMessage: () -> Unit = mockk(relaxed = true)
        val paramSendMessage = "paramSendMessage"
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false
        every {
            TopChatWebSocketParam.generateParamSendMessage(any(), any(), any(), any(), any())
        } returns paramSendMessage

        // When
        presenter.connectWebSocket(exMessageId)
        presenter.sendSrwBubble(
            exMessageId, srwQuestion, products, exOpponentId, mockOnSendingMessage
        )

        // Then
        verify(exactly = 1) { RxWebSocket.send(paramSendMessage, listInterceptor) }
    }

    @Test
    fun `on get shop following status`() {
        // Given
        val onError: (Throwable) -> Unit = mockk(relaxed = true)
        val onSuccess: (Boolean) -> Unit = mockk(relaxed = true)

        // When
        presenter.getShopFollowingStatus(exShopId, onError, onSuccess)

        // Then
        verifyOrder {
            getShopFollowingUseCase.getStatus(exShopId, onError, onSuccess)
        }
    }

    @Test
    fun `on detachView`() {
        // When
        presenter.detachView()

        // Then
        verify {
            presenter.destroyWebSocket()
            getChatUseCase.unsubscribe()
            getTemplateChatRoomUseCase.unsubscribe()
            replyChatUseCase.unsubscribe()
            getShopFollowingUseCase.safeCancel()
            addToCartUseCase.unsubscribe()
            groupStickerUseCase.safeCancel()
            chatAttachmentUseCase.safeCancel()
        }
    }

    @Test
    fun `send ws event on start typing`() {
        //Given
        val typingParam = TopChatWebSocketParam.generateParamStartTyping(exMessageId)
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false

        // When
        presenter.connectWebSocket(exMessageId)
        presenter.startTyping()

        // Then
        verify { RxWebSocket.send(typingParam, listInterceptor) }
    }

    @Test
    fun `send ws event on stop typing`() {
        //Given
        val stopTypingParam = TopChatWebSocketParam.generateParamStopTyping(exMessageId)
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false

        // When
        presenter.connectWebSocket(exMessageId)
        presenter.stopTyping()

        // Then
        verify { RxWebSocket.send(stopTypingParam, listInterceptor) }
    }

    @Test
    fun `on success request follow and unfollow shop`() {
        //Given
        val onError: (Throwable) -> Unit = mockk(relaxed = true)
        val onSuccess: (Boolean) -> Unit = mockk(relaxed = true)
        val slot = slot<Subscriber<Boolean>>()
        every {
            toggleFavouriteShopUseCase.execute(any(), capture(slot))
        } answers {
            val subs = slot.captured
            subs.onNext(true)
        }

        // When
        presenter.followUnfollowShop(exShopId.toString(), onError, onSuccess)

        // Then
        verify { onSuccess.invoke(true) }
    }

    @Test
    fun `on error request follow and unfollow shop`() {
        //Given
        val onError: (Throwable) -> Unit = mockk(relaxed = true)
        val onSuccess: (Boolean) -> Unit = mockk(relaxed = true)
        val throwable = Throwable()
        val slot = slot<Subscriber<Boolean>>()
        every {
            toggleFavouriteShopUseCase.execute(any(), capture(slot))
        } answers {
            val subs = slot.captured
            subs.onError(throwable)
        }

        // When
        presenter.followUnfollowShop(
            exShopId.toString(), onError, onSuccess,
            ToggleFavouriteShopUseCase.Action.FOLLOW
        )

        // Then
        verify { onError.invoke(throwable) }
    }

    @Test
    fun `check hasEmptyAttachmentPreview`() {
        // When
        presenter.addAttachmentPreview(sendAbleProductPreview)
        val isEmptyAttachment = presenter.hasEmptyAttachmentPreview()

        // Then
        assert(!isEmptyAttachment)
    }

    @Test
    fun `should send JsonObject attachment preview`() {
        // Given
        val msgObj = JsonObject()
        every {
            sendAbleProductPreview.generateMsgObj(any(), any(), any(), any(), any())
        } returns msgObj

        // When
        presenter.addAttachmentPreview(sendAbleProductPreview)
        presenter.sendAttachmentsAndMessage(
            exMessageId, exSendMessage, exStartTime, exOpponentId
        ) {}

        // Then
        verify(exactly = 1) { RxWebSocket.send(msgObj, listInterceptor) }
    }

    @Test
    fun `should send request string attachment preview`() {
        // Given
        val msgAttachment = CommonUtil.toJson("WebsocketVoucherPayload")
        every {
            sendAbleProductPreview.generateMsgObj(any(), any(), any(), any(), any())
        } returns msgAttachment

        // When
        presenter.addAttachmentPreview(sendAbleProductPreview)
        presenter.sendAttachmentsAndMessage(
            exMessageId, exSendMessage, exStartTime, exOpponentId
        ) {}

        // Then
        verify(exactly = 1) { RxWebSocket.send(msgAttachment, listInterceptor) }
    }

    @Test
    fun `on initAttachmentPreview`() {
        // Given
        val attachmentList = arrayListOf(sendAbleProductPreview)

        // When
        presenter.addAttachmentPreview(sendAbleProductPreview)
        presenter.initAttachmentPreview()

        // Then
        verify(exactly = 1) {
            view.showAttachmentPreview(attachmentList)
            view.focusOnReply()
        }
    }

    @Test
    fun `on clearAttachmentPreview`() {
        // When
        presenter.addAttachmentPreview(sendAbleProductPreview)
        presenter.clearAttachmentPreview()
        val isEmptyAttachmentPreview = presenter.hasEmptyAttachmentPreview()

        // Then
        assert(isEmptyAttachmentPreview)
    }

    @Test
    fun `on initProductPreviewFromAttachProduct`() {
        // Given
        val productPreview = arrayListOf(exResultProduct)

        // When
        presenter.initProductPreviewFromAttachProduct(productPreview)
        val isEmptyAttachmentPreview = presenter.hasEmptyAttachmentPreview()

        // Then
        assert(!isEmptyAttachmentPreview)
        verify(exactly = 1) {
            presenter.initAttachmentPreview()
        }
    }

    @Test
    fun `on success click banned product seamless`() {
        // Given
        val liteUrl = "https://tokopedia/lite/url"
        val slot = slot<SeamlessLoginSubscriber>()
        every {
            seamlessLoginUsecase.generateSeamlessUrl(liteUrl, capture(slot))
        } answers {
            val subs = slot.captured
            subs.onUrlGenerated(liteUrl)
        }

        // When
        presenter.onClickBannedProduct(liteUrl)

        // Then
        verify(exactly = 1) {
            view.redirectToBrowser(liteUrl)
        }
    }

    @Test
    fun `on error click banned product seamless`() {
        // Given
        val liteUrl = "https://tokopedia/lite/url"
        val slot = slot<SeamlessLoginSubscriber>()
        every {
            seamlessLoginUsecase.generateSeamlessUrl(liteUrl, capture(slot))
        } answers {
            val subs = slot.captured
            subs.onError(liteUrl)
        }

        // When
        presenter.onClickBannedProduct(liteUrl)

        // Then
        verify(exactly = 1) {
            view.redirectToBrowser(liteUrl)
        }
    }

    @Test
    fun `on loadChatRoomSettings`() {
        // Given
        val onSuccess: (List<Visitable<TopChatTypeFactory>>) -> Unit = mockk(relaxed = true)

        // When
        presenter.loadChatRoomSettings(exMessageId, onSuccess)

        // Then
        verify(exactly = 1) {
            getChatRoomSettingUseCase.execute(exMessageId, onSuccess)
        }
    }

    @Test
    fun `on toggle add and remove WishList`() {
        // Given
        val wishlistActionListener: WishListActionListener = mockk(relaxed = true)

        // When
        presenter.addToWishList(exProductId, exUserId, wishlistActionListener)
        presenter.removeFromWishList(exProductId, exUserId, wishlistActionListener)

        // Then
        verify(exactly = 1) {
            addWishListUseCase.createObservable(exProductId, exUserId, wishlistActionListener)
            removeWishListUseCase.createObservable(exProductId, exUserId, wishlistActionListener)
        }
    }

    @Test
    fun `on success get order progress`() {
        // Given
        every { orderProgressUseCase.getOrderProgress(any(), captureLambda(), any()) } answers {
            val onSuccess = lambda<(OrderProgressResponse) -> Unit>()
            onSuccess.invoke(successGetOrderProgressResponse)
        }

        // When
        presenter.getOrderProgress(exMessageId)

        // Then
        verify(exactly = 1) {
            view.renderOrderProgress(successGetOrderProgressResponse.chatOrderProgress)
        }
    }

    @Test
    fun `on success get sticker group list`() {
        // Given
        val roomModel = ChatroomViewModel()
        val needTopUpdateCache = emptyList<StickerGroup>()
        val onLoadingSlot = slot<(ChatListGroupStickerResponse) -> Unit>()
        val onSuccessSlot = slot<(ChatListGroupStickerResponse, List<StickerGroup>) -> Unit>()
        every {
            groupStickerUseCase.getStickerGroup(
                roomModel.isSeller(),
                capture(onLoadingSlot),
                capture(onSuccessSlot),
                any()
            )
        } answers {
            val onLoading = onLoadingSlot.captured
            onLoading.invoke(successGetChatListGroupSticker)
            val onSuccess = onSuccessSlot.captured
            onSuccess.invoke(successGetChatListGroupSticker, needTopUpdateCache)
        }

        // When
        presenter.getStickerGroupList(roomModel)

        // Then
        verify(exactly = 2) {
            view.getChatMenuView()
        }
    }

    @Test
    fun `on success loadAttachmentData`() {
        // Given
        val roomModel = ChatroomViewModel(attachmentIds = "3213, 3123")
        val mapSuccessAttachment = ArrayMap<String, Attachment>().apply {
            put("test_attachment", Attachment())
        }
        every {
            chatAttachmentUseCase.getAttachments(
                exMessageId.toLongOrZero(), roomModel.attachmentIds,
                any(), captureLambda(), any()
            )
        } answers {
            val onSuccess = lambda<(ArrayMap<String, Attachment>) -> Unit>()
            onSuccess.invoke(mapSuccessAttachment)
        }

        // When
        presenter.initUserLocation(null)
        presenter.initUserLocation(LocalCacheModel())
        presenter.loadAttachmentData(exMessageId.toLongOrZero(), roomModel)

        // Then
        val attachments = presenter.attachments
        verify(exactly = 1) { view.updateAttachmentsView(attachments) }
        assertTrue(presenter.attachments.size == 1)
    }

    @Test
    fun `on error loadAttachmentData`() {
        // Given
        val roomModel = ChatroomViewModel(attachmentIds = "3213, 3123")
        val mapErrorAttachment = ArrayMap<String, Attachment>().apply {
            put("test_error_attachment", Attachment())
        }
        val throwable = Throwable()
        every {
            chatAttachmentUseCase.getAttachments(
                exMessageId.toLongOrZero(), roomModel.attachmentIds, any(),
                any(), captureLambda()
            )
        } answers {
            val onError = lambda<(Throwable, ArrayMap<String, Attachment>) -> Unit>()
            onError.invoke(throwable, mapErrorAttachment)
        }

        // When
        presenter.loadAttachmentData(exMessageId.toLongOrZero(), roomModel)

        // Then
        val attachments = presenter.attachments
        verify(exactly = 1) { view.updateAttachmentsView(attachments) }
        assertTrue(presenter.attachments.size == 1)
    }

    @Test
    fun `check setBeforeReplyTime`() {
        //Given
        val exCreateTime = "1234532"

        // When
        presenter.setBeforeReplyTime(exCreateTime)

        // Then
        verify(exactly = 1) { getChatUseCase.minReplyTime = exCreateTime }
    }

    @Test
    fun `check resetChatUseCase`() {
        // When
        presenter.resetChatUseCase()

        // Then
        verify(exactly = 1) { getChatUseCase.reset() }
    }

    @Test
    fun `check resetUnreadMessage`() {
        // When
        presenter.resetUnreadMessage()

        // Then
        assertTrue(presenter.newUnreadMessage == 0)
    }

    @Test
    fun `check requestBlockPromo`() {
        // Given
        val onSuccess: (ChatSettingsResponse) -> Unit = mockk()
        val onError: (Throwable) -> Unit = mockk()

        // When
        presenter.requestBlockPromo(exMessageId, onSuccess, onError)

        // Then
        verify(exactly = 1) {
            chatToggleBlockChat.blockPromo(exMessageId, onSuccess, onError)
        }
    }

    @Test
    fun `check requestAllowPromo`() {
        // Given
        val onSuccess: (ChatSettingsResponse) -> Unit = mockk()
        val onError: (Throwable) -> Unit = mockk()

        // When
        presenter.requestAllowPromo(exMessageId, onSuccess, onError)

        // Then
        verify(exactly = 1) {
            chatToggleBlockChat.allowPromo(exMessageId, onSuccess, onError)
        }
    }

    @Test
    fun `check blockChat`() {
        // Given
        val onSuccess: (ChatSettingsResponse) -> Unit = mockk()
        val onError: (Throwable) -> Unit = mockk()

        // When
        presenter.blockChat(exMessageId, onSuccess, onError)

        // Then
        verify(exactly = 1) {
            chatToggleBlockChat.blockChat(exMessageId, onSuccess, onError)
        }
    }

    @Test
    fun `check unBlockChat`() {
        // Given
        val onSuccess: (ChatSettingsResponse) -> Unit = mockk()
        val onError: (Throwable) -> Unit = mockk()

        // When
        presenter.unBlockChat(exMessageId, onSuccess, onError)

        // Then
        verify(exactly = 1) {
            chatToggleBlockChat.unBlockChat(exMessageId, onSuccess, onError)
        }
    }

    @Test
    fun `on load background from cache`() {
        // Given
        every {
            chatBackgroundUseCase.getBackground(
                captureLambda(), any(), any()
            )
        } answers {
            val onCache = lambda<(String) -> Unit>()
            onCache.invoke(exUrl)
        }

        // When
        presenter.getBackground()

        // Then
        verify(exactly = 1) {
            view.renderBackground(exUrl)
        }
    }

    @Test
    fun `on load background from success response and need to update`() {
        // Given
        every {
            chatBackgroundUseCase.getBackground(
                any(), captureLambda(), any()
            )
        } answers {
            val onSuccess = lambda<(String, Boolean) -> Unit>()
            onSuccess.invoke(exUrl, true)
        }

        // When
        presenter.getBackground()

        // Then
        verify(exactly = 1) {
            view.renderBackground(exUrl)
        }
    }

    @Test
    fun `when success addProductToCart`() {
        // Given
        val onSuccess: (data: DataModel) -> Unit = mockk(relaxed = true)
        val successAtc = getSuccessAtcModel()
        every {
            addToCartUseCase.createObservable(any())
        } returns Observable.just(successAtc)

        // When
        presenter.addProductToCart(RequestParams(), onSuccess, {})

        // Then
        verify(exactly = 1) {
            onSuccess.invoke(successAtc.data)
        }
    }

    @Test
    fun `when error addProductToCart`() {
        // Given
        val onError: (msg: String) -> Unit = mockk(relaxed = true)
        val errorAtc = getErrorAtcModel()
        every {
            addToCartUseCase.createObservable(any())
        } returns Observable.just(errorAtc)

        // When
        presenter.addProductToCart(RequestParams(), {}, onError)

        // Then
        verify(exactly = 1) {
            onError.invoke("Gagal menambahkan produk")
        }
    }

    @Test
    fun `when error throwable addProductToCart`() {
        // Given
        val onError: (msg: String) -> Unit = mockk(relaxed = true)
        val errorMsg = "Gagal menambahkan produk"
        every {
            addToCartUseCase.createObservable(any())
        } throws IllegalStateException(errorMsg)

        // When
        presenter.addProductToCart(RequestParams(), {}, onError)

        // Then
        verify(exactly = 1) {
            onError.invoke(errorMsg)
        }
    }

    @Test
    fun `success load srw`() {
        // Given
        val observer: Observer<Resource<ChatSmartReplyQuestionResponse>> = mockk()
        val expectedValue: Resource<ChatSmartReplyQuestionResponse> = Resource.success(
            ChatSmartReplyQuestionResponse()
        )
        val successFlow = flow { emit(expectedValue) }
        every {
            chatSrwUseCase.getSrwList(exMessageId)
        } returns successFlow

        // When
        presenter.srw.observeForever(observer)
        presenter.getSmartReplyWidget(exMessageId)

        // Then
        verify(exactly = 1) {
            observer.onChanged(expectedValue)
        }
    }

    @Test
    fun `error load srw`() {
        // Given
        val observer: Observer<Resource<ChatSmartReplyQuestionResponse>> = mockk()
        val throwable = IllegalStateException()
        val expectedValue: Resource<ChatSmartReplyQuestionResponse> = Resource.error(
            throwable, null
        )
        every {
            chatSrwUseCase.getSrwList(exMessageId)
        } throws throwable

        // When
        presenter.srw.observeForever(observer)
        presenter.getSmartReplyWidget(exMessageId)

        // Then
        verify(exactly = 1) {
            observer.onChanged(expectedValue)
        }
    }

    @Test
    fun `onGoingStockUpdate added`() {
        // Given
        val productId = "123"
        val product = ProductAttachmentViewModel(
            "", productId, "",
            "", "", "",
            "", false, 1
        )

        // When
        presenter.addOngoingUpdateProductStock(productId, product, 0, null)

        // Then
        assertThat(presenter.onGoingStockUpdate.containsKey(productId), `is`(true))
        assertThat(presenter.onGoingStockUpdate.size, `is`(1))
    }

    @Test
    fun `should filter product id on attachment preview`() {
        // Given
        val product = generateSendAbleProductPreview()
        val invoice = generateSendAbleInvoicePreview()

        // When
        presenter.addAttachmentPreview(product)
        presenter.addAttachmentPreview(invoice)
        val productIds = presenter.getProductIdPreview()
        val attachmentPreviews = presenter.getAttachmentsPreview()

        // Then
        assertThat(productIds.size, `is`(1))
        assertThat(attachmentPreviews.size, `is`(2))
        assertThat(productIds, hasItem("12398764"))
    }

    @Test
    fun `get interlocutor warehouse id`() {
        // Given
        val warehouseId = "123"
        val response = ChatTokoNowWarehouseResponse(
            ChatTokoNowWarehouse(warehouseId = warehouseId)
        )
        val expectedValue = Resource.success(response)
        val successFlow = flow { emit(expectedValue) }
        every {
            tokoNowWHUsecase.getWarehouseId(exMessageId)
        } returns successFlow

        // When
        presenter.adjustInterlocutorWarehouseId(exMessageId)

        // Then
        assertThat(presenter.attachProductWarehouseId, `is`(warehouseId))
    }

    private fun getErrorAtcModel(): AddToCartDataModel {
        return AddToCartDataModel().apply {
            data.success = 0
            data.message.add("Gagal menambahkan produk")
        }
    }

    private fun getSuccessAtcModel(): AddToCartDataModel {
        return AddToCartDataModel().apply {
            data.success = 1
        }
    }

}