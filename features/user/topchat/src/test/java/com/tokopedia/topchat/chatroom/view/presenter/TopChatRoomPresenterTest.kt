package com.tokopedia.topchat.chatroom.view.presenter

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.collection.ArrayMap
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.attachcommon.data.ResultProduct
import com.tokopedia.chat_common.data.preview.ProductPreview
import com.tokopedia.chat_common.domain.pojo.ChatReplies
import com.tokopedia.chat_common.domain.pojo.ChatReplyPojo
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chatbot.domain.mapper.TopChatRoomWebSocketMessageMapper
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.seamless_login_common.subscriber.SeamlessLoginSubscriber
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.topchat.FileUtil
import com.tokopedia.topchat.R
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.topchat.chatlist.domain.usecase.DeleteMessageListUseCase
import com.tokopedia.topchat.chatlist.viewmodel.DeleteChatListUiModel
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.Attachment
import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.ChatSettingsResponse
import com.tokopedia.topchat.chatroom.domain.pojo.orderprogress.OrderProgressResponse
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.Sticker
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.ChatListGroupStickerResponse
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.StickerGroup
import com.tokopedia.topchat.chatroom.domain.usecase.*
import com.tokopedia.topchat.chatroom.service.UploadImageBroadcastListener
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory
import com.tokopedia.topchat.chatroom.view.listener.TopChatContract
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.exImageUploadId
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
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.imageUploadViewModel
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.readParam
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.replyChatViewModelApiSuccess
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.source
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.successGetChatListGroupSticker
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.successGetOrderProgressResponse
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.toShopId
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.toUserId
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.wsResponseEndTypingString
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.wsResponseImageAttachmentString
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.wsResponseReadMessageString
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.wsResponseReplyString
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.wsResponseTypingString
import com.tokopedia.topchat.chatroom.view.viewmodel.SendablePreview
import com.tokopedia.topchat.chatroom.view.viewmodel.SendableProductPreview
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chat_common.data.*
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.topchat.chatroom.domain.pojo.srw.ChatSmartReplyQuestionResponse
import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateUiModel
import com.tokopedia.topchat.common.data.Resource
import com.tokopedia.topchat.common.util.ImageUtil
import com.tokopedia.topchat.common.util.ImageUtil.IMAGE_EXCEED_SIZE_LIMIT
import com.tokopedia.topchat.common.util.ImageUtil.IMAGE_UNDERSIZE
import com.tokopedia.topchat.common.util.ImageUtil.IMAGE_VALID
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
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
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
    private lateinit var deleteMessageListUseCase: DeleteMessageListUseCase

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
    private lateinit var uploadImageBroadcastListener : UploadImageBroadcastListener

    @RelaxedMockK
    private lateinit var sendAbleProductPreview: SendablePreview

    @RelaxedMockK
    private lateinit var chatSrwUseCase: SmartReplyQuestionUseCase

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
    private val websocketServer = PublishSubject.create<WebSocketInfo>()

    object Dummy {
        const val exMessageId = "190378584"
        const val exShopId = 423785L
        const val exImageUploadId = "667056"
        const val exProductId = "123123"
        const val exUserId = "321321"
        const val exSendMessage = "Hello World"
        const val exStartTime = "123321"
        const val exOpponentId = "39467501"
        const val exImageUrl = "https://ecs.tokopedia.com/image.jpg"
        const val exUrl = "https://tokopedia.com/url"
        const val toUserId = "12345"
        const val toShopId = "54321"
        const val source = "askseller"
        val readParam = TopChatWebSocketParam.generateParamRead(exMessageId)
        val imageUploadViewModel = generateImageUploadViewModel()
        val replyChatViewModelApiSuccess = generateReplyChatViewModelApi()
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
        val successGetOrderProgressResponse: OrderProgressResponse = FileUtil.parse(
                "/success_get_order_progress.json",
                OrderProgressResponse::class.java
        )
        val successGetChatListGroupSticker: ChatListGroupStickerResponse = FileUtil.parse(
                "/success_chat_list_group_sticker.json",
                ChatListGroupStickerResponse::class.java
        )

        private fun generateImageUploadViewModel(): ImageUploadViewModel {
            return ImageUploadViewModel(
                    exMessageId,
                    "123123",
                    "123987",
                    exImageUrl,
                    "123"
            )
        }

        private fun generateReplyChatViewModelApi(): ReplyChatViewModel {
            return ReplyChatViewModel(imageUploadViewModel, true)
        }

        private fun generateSendAbleProductPreview(): SendablePreview {
            val productPreview = ProductPreview(
                    name = "hello product",
                    imageUrl = exImageUrl,
                    price = "Rp120.000",
                    id = "12398764"
            )
            return SendableProductPreview(productPreview)
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
                        deleteMessageListUseCase,
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
    }

    private fun mockSingletonObject() {
        mockkObject(RxWebSocket)
        mockkObject(RxWebSocketUtil)
        mockkObject(ImageUtil)
        mockkObject(TopChatWebSocketParam)
        every { RxWebSocketUtil.getInstance(any()) } returns webSocketUtil
    }

    @Test
    fun `call onDestroy when start connect ws`() {
        // When
        presenter.connectWebSocket(exMessageId)

        // Then
        verify(exactly = 1) { presenter.destroyWebSocket() }
    }


    @Test
    fun `onOpen connect to webscoket`() {
        // Given
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer

        // When
        presenter.connectWebSocket(exMessageId)
        websocketServer.onNext(wsOpen)

        // Then
        verify(exactly = 1) { view.showErrorWebSocket(false) }
        verifyReadMessageSentToWs()
    }


    @Test
    fun `onReconnect webscoket`() {
        // Given
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer

        // When
        presenter.connectWebSocket(exMessageId)
        websocketServer.onNext(wsReconnect)

        // Then
        verify(exactly = 1) { view.showErrorWebSocket(true) }
    }


    @Test
    fun `onClose ws`() {
        // Given
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer

        // When
        presenter.connectWebSocket(exMessageId)
        websocketServer.onNext(wsOpen)
        websocketServer.onCompleted()

        // Then
        verify(exactly = 2) { presenter.destroyWebSocket() }
        verify(exactly = 1) { view.showErrorWebSocket(true) }
    }

    @Test
    fun `onMessage ws event reply when not in the middle of the page`() {
        // Given
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false
        val wsChatPojo = mockkParseResponse(wsResponseReplyText)
        val wsChatVisitable = mockkWsMapper(wsChatPojo)

        // When
        presenter.connectWebSocket(exMessageId)
        websocketServer.onNext(wsResponseReplyText)

        // Then
        assertThat(presenter.newUnreadMessage, equalTo(0))
        verify(exactly = 1) { view.hideUnreadMessage() }
        verify(exactly = 1) { view.onReceiveMessageEvent(wsChatVisitable) }
        verifyReadMessageSentToWs()
    }

    @Test
    fun `onMessage ws event reply when in the middle of the page`() {
        // Given
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns true
        mockkParseResponse(wsResponseReplyText)

        // When
        presenter.connectWebSocket(exMessageId)
        websocketServer.onNext(wsResponseReplyText)

        // Then
        assertThat(presenter.newUnreadMessage, equalTo(1))
        verify(exactly = 1) { view.showUnreadMessage(1) }
    }

    @Test
    fun `onMessage ws event typing when not the middle of the page`() {
        // Given
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false

        // When
        presenter.connectWebSocket(exMessageId)
        websocketServer.onNext(wsResponseTyping)

        // Then
        verify(exactly = 1) { view.onReceiveStartTypingEvent() }
    }

    @Test
    fun `onMessage ws event end typing when not the middle of the page`() {
        // Given
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false

        // When
        presenter.connectWebSocket(exMessageId)
        websocketServer.onNext(wsResponseEndTyping)

        // Then
        verify(exactly = 1) { view.onReceiveStopTypingEvent() }
    }

    @Test
    fun `onMessage ws event read message`() {
        // Given
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false

        // When
        presenter.connectWebSocket(exMessageId)
        websocketServer.onNext(wsResponseReadMessage)

        // Then
        verify(exactly = 1) { view.onReceiveReadEvent() }
    }

    @Test
    fun `Get chat usecase called when load page`() {
        // Given
        val mockOnSuccess: (ChatroomViewModel, ChatReplies) -> Unit = mockk()
        val mockOnError: (Throwable) -> Unit = mockk()

        // When
        presenter.getExistingChat(exMessageId, mockOnError, mockOnSuccess)

        // Then
        verify(exactly = 1) { getChatUseCase.getFirstPageChat(exMessageId, mockOnSuccess, mockOnError) }
    }

    @Test
    fun `Get message usecase called when no message id provided`() {
        // Given
        val mockOnSuccess: (String) -> Unit = mockk()
        val mockOnError: (Throwable) -> Unit = mockk()

        // When
        presenter.getMessageId(toUserId, toShopId, source, mockOnError, mockOnSuccess)

        // Then
        verify(exactly = 1) { getExistingMessageIdUseCase.getMessageId(toShopId, toUserId, source, mockOnSuccess, mockOnError) }
    }

    @Test
    fun `Get chat usecase called when load top page chat`() {
        // Given
        val mockOnSuccess: (ChatroomViewModel, ChatReplies) -> Unit = mockk()
        val mockOnError: (Throwable) -> Unit = mockk()

        // When
        presenter.loadTopChat(exMessageId, mockOnError, mockOnSuccess)

        // Then
        verify(exactly = 1) { getChatUseCase.getTopChat(exMessageId, mockOnSuccess, mockOnError) }
    }

    @Test
    fun `Get chat usecase called when load bottom page chat`() {
        // Given
        val mockOnSuccess: (ChatroomViewModel, ChatReplies) -> Unit = mockk()
        val mockOnError: (Throwable) -> Unit = mockk()

        // When
        presenter.loadBottomChat(exMessageId, mockOnError, mockOnSuccess)

        // Then
        verify(exactly = 1) { getChatUseCase.getBottomChat(exMessageId, mockOnSuccess, mockOnError) }
    }

    @Test
    fun `On success get chat template`() {
        // Given
        val slot = slot<Subscriber<GetTemplateUiModel>>()
        every {
            getTemplateChatRoomUseCase.execute(
                    any(),
                    capture(slot)
            )
        } answers {
            val subs = slot.captured
            subs.onNext(GetTemplateUiModel())
        }

        // When
        presenter.getTemplate(true)

        // Then
        verify(exactly = 1) { view.onSuccessGetTemplate(emptyList()) }
    }

    @Test
    fun `On error get chat template`() {
        // Given
        val slot = slot<Subscriber<GetTemplateUiModel>>()
        every {
            getTemplateChatRoomUseCase.execute(
                    any(),
                    capture(slot)
            )
        } answers {
            val subs = slot.captured
            subs.onError(Throwable())
        }

        // When
        presenter.getTemplate(true)

        // Then
        verify(exactly = 1) { view.onErrorGetTemplate() }
    }

    @Test
    fun `on success upload image and sent through websocket`() {
        // Given
        every {
            remoteConfig.getBoolean(any())
        } returns false
        every {
            ImageUtil.validateImageAttachment(imageUploadViewModel.imageUrl)
        } returns Pair(true, IMAGE_VALID)
        every {
            compressImageUseCase.compressImage(imageUploadViewModel.imageUrl!!)
        } returns Observable.just(imageUploadViewModel.imageUrl)
        every {
            uploadImageUseCase.upload(imageUploadViewModel, captureLambda(), any())
        } answers {
            val onSuccess = lambda<(String, ImageUploadViewModel) -> Unit>()
            onSuccess.invoke(exImageUploadId, imageUploadViewModel)
        }
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false
        val wsChatPojo = mockkParseResponse(wsResponseImageAttachment, false)
        val wsChatVisitable = mockkWsMapper(wsChatPojo)
        val websocketParam = TopChatWebSocketParam.generateParamSendImage(
                exMessageId, exImageUploadId, imageUploadViewModel.startTime
        )

        // When
        presenter.connectWebSocket(exMessageId)
        presenter.startCompressImages(imageUploadViewModel)
        websocketServer.onNext(wsResponseImageAttachment)

        // Then
        verify(exactly = 1) { view.addDummyMessage(imageUploadViewModel) }
        verify(exactly = 1) { RxWebSocket.send(websocketParam, listInterceptor) }
        verify(exactly = 1) { view.onReceiveMessageEvent(wsChatVisitable) }
        verify(exactly = 1) { view.removeDummy(imageUploadViewModel) }
    }

    @Test
    fun `on success upload image and sent through API`() {
        // Given
        val slot = slot<Subscriber<ReplyChatViewModel>>()
        every {
            remoteConfig.getBoolean(any())
        } returns false
        every {
            ImageUtil.validateImageAttachment(imageUploadViewModel.imageUrl)
        } returns Pair(true, IMAGE_VALID)
        every {
            compressImageUseCase.compressImage(imageUploadViewModel.imageUrl!!)
        } returns Observable.just(imageUploadViewModel.imageUrl)
        every {
            uploadImageUseCase.upload(imageUploadViewModel, captureLambda(), any())
        } answers {
            val onSuccess = lambda<(String, ImageUploadViewModel) -> Unit>()
            onSuccess.invoke(exImageUploadId, imageUploadViewModel)
        }
        every {
            replyChatUseCase.execute(any(), capture(slot))
        } answers {
            val subs = slot.captured
            subs.onNext(replyChatViewModelApiSuccess)
        }
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false

        // When
        presenter.connectWebSocket(exMessageId)
        websocketServer.onNext(wsOpen)
        websocketServer.onCompleted()
        presenter.startCompressImages(imageUploadViewModel)

        // Then
        verify(exactly = 1) { view.onReceiveMessageEvent(replyChatViewModelApiSuccess.chat) }
        verify(exactly = 1) { view.removeDummy(imageUploadViewModel) }
    }

    @Test
    fun `on success upload image with service`() {
        val chatReply = mockk<ChatReplyPojo>()
        every {
            remoteConfig.getBoolean(any())
        } returns true
        every {
            ImageUtil.validateImageAttachment(imageUploadViewModel.imageUrl)
        } returns Pair(true, IMAGE_VALID)
        every {
            compressImageUseCase.compressImage(imageUploadViewModel.imageUrl!!)
        } returns Observable.just(imageUploadViewModel.imageUrl)
        every {
            uploadImageUseCase.upload(imageUploadViewModel, captureLambda(), any())
        } answers {
            val onSuccess = lambda<(String, ImageUploadViewModel) -> Unit>()
            onSuccess.invoke(exImageUploadId, imageUploadViewModel)
        }
        coEvery {
            replyChatGQLUseCase.replyMessage(any(), any(), any(), any())
        } returns chatReply

        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false

        // When
        presenter.connectWebSocket(exMessageId)
        websocketServer.onNext(wsOpen)
        websocketServer.onCompleted()
        presenter.startCompressImages(imageUploadViewModel)

        // Then
        verify(exactly = 1) { view.addDummyMessage(imageUploadViewModel) }
    }

    @Test
    fun `on error upload image`() {
        // Given
        val errorUploadImage = Throwable()
        every {
            remoteConfig.getBoolean(any())
        } returns false
        every {
            ImageUtil.validateImageAttachment(imageUploadViewModel.imageUrl)
        } returns Pair(true, IMAGE_VALID)
        every {
            compressImageUseCase.compressImage(imageUploadViewModel.imageUrl!!)
        } returns Observable.just(imageUploadViewModel.imageUrl)
        every {
            uploadImageUseCase.upload(imageUploadViewModel, any(), captureLambda())
        } answers {
            val onError = lambda<(Throwable, ImageUploadViewModel) -> Unit>()
            onError.invoke(errorUploadImage, imageUploadViewModel)
        }

        // When
        presenter.startCompressImages(imageUploadViewModel)

        // Then
        verify(exactly = 1) { view.addDummyMessage(imageUploadViewModel) }
        verify(exactly = 1) {
            view.onErrorUploadImage(
                    ErrorHandler.getErrorMessage(view.context, errorUploadImage),
                    imageUploadViewModel
            )
        }
    }

    @Test
    fun `on error image file to upload validation IMAGE_UNDERSIZE`() {
        // Given
        every {
            ImageUtil.validateImageAttachment(imageUploadViewModel.imageUrl)
        } returns Pair(false, IMAGE_UNDERSIZE)

        // When
        presenter.startCompressImages(imageUploadViewModel)

        // Then
        verify(exactly = 1) { view.showSnackbarError(view.getStringResource(R.string.undersize_image)) }
    }

    @Test
    fun `on error image file to upload validation IMAGE_EXCEED_SIZE_LIMIT`() {
        // Given
        every {
            ImageUtil.validateImageAttachment(imageUploadViewModel.imageUrl)
        } returns Pair(false, IMAGE_EXCEED_SIZE_LIMIT)

        // When
        presenter.startCompressImages(imageUploadViewModel)

        // Then
        verify(exactly = 1) { view.showSnackbarError(view.getStringResource(R.string.oversize_image)) }
    }

    @Test
    fun `on uploading image`() {
        // Given
        every {
            uploadImageUseCase.isUploading
        } returns true

        // When
        presenter.isUploading()

        // Then
        verify(exactly = 1) { uploadImageUseCase.isUploading }
        assertTrue(uploadImageUseCase.isUploading)
    }

    @Test
    fun `on success send attachment and message through Websocket`() {
        // Given
        val mockOnSendingMessage: () -> Unit = mockk(relaxed = true)
        val dummyMessage = MessageViewModel(
                exMessageId, userSession.userId, userSession.name, exStartTime, exSendMessage
        )
        val paramSendMessage = "paramSendMessage"
        val paramStopTyping = TopChatWebSocketParam.generateParamStopTyping(exMessageId)
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false
        every {
            topChatRoomWebSocketMessageMapper.mapToDummyMessage(any(), any(), any(), any(), any())
        } returns dummyMessage
        every {
            TopChatWebSocketParam.generateParamSendMessage(any(), any(), any(), any())
        } returns paramSendMessage

        // When
        presenter.connectWebSocket(exMessageId)
        presenter.addAttachmentPreview(sendAbleProductPreview)
        presenter.sendAttachmentsAndMessage(
                exMessageId, exSendMessage, exStartTime, exOpponentId, mockOnSendingMessage
        )

        // Then
        verify(exactly = 1) {
            sendAbleProductPreview.generateMsgObj(
                exMessageId,
                exOpponentId,
                exSendMessage,
                listInterceptor,
                LocalCacheModel()
            )
        }
        verify(exactly = 1) { view.sendAnalyticAttachmentSent(sendAbleProductPreview) }
        verify(exactly = 1) { view.addDummyMessage(dummyMessage) }
        verify(exactly = 1) { RxWebSocket.send(paramSendMessage, listInterceptor) }
        verify(exactly = 1) { RxWebSocket.send(paramStopTyping, listInterceptor) }
        verify(exactly = 1) { view.clearAttachmentPreviews() }
    }

    @Test
    fun `on success send message through API`() {
        // Given
        val slot = slot<Subscriber<ReplyChatViewModel>>()
        val mockOnSendingMessage: () -> Unit = mockk(relaxed = true)
        val dummyMessage = MessageViewModel(
                exMessageId, userSession.userId, userSession.name, exStartTime, exSendMessage
        )
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false
        every {
            topChatRoomWebSocketMessageMapper.mapToDummyMessage(any(), any(), any(), any(), any())
        } returns dummyMessage
        every { replyChatUseCase.execute(any(), capture(slot)) } answers {
            val subs = slot.captured
            subs.onNext(replyChatViewModelApiSuccess)
        }

        // When
        presenter.connectWebSocket(exMessageId)
        websocketServer.onNext(wsOpen)
        websocketServer.onCompleted()
        presenter.sendAttachmentsAndMessage(
                exMessageId, exSendMessage, exStartTime, exOpponentId, mockOnSendingMessage
        )

        // Then
        verify(exactly = 1) { view.addDummyMessage(dummyMessage) }
        verify(exactly = 1) { view.onReceiveMessageEvent(replyChatViewModelApiSuccess.chat) }
        verify(exactly = 1) { view.removeDummy(dummyMessage) }
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
    fun `on success delete chat`() {
        // Given
        val slot = slot<Subscriber<DeleteChatListUiModel>>()
        val onError: (Throwable) -> Unit = mockk(relaxed = true)
        val onSuccessDeleteConversation: () -> Unit = mockk(relaxed = true)
        every { deleteMessageListUseCase.execute(any(), capture(slot)) } answers {
            val subs = slot.captured
            subs.onNext(DeleteChatListUiModel())
        }

        // When
        presenter.deleteChat(exMessageId, onError, onSuccessDeleteConversation)

        // Then
        verifyOrder {
            onSuccessDeleteConversation.invoke()
        }
    }

    @Test
    fun `on error delete chat`() {
        // Given
        val slot = slot<Subscriber<DeleteChatListUiModel>>()
        val onError: (Throwable) -> Unit = mockk(relaxed = true)
        val onSuccessDeleteConversation: () -> Unit = mockk(relaxed = true)
        val errorDeleteChat = Throwable()
        every { deleteMessageListUseCase.execute(any(), capture(slot)) } answers {
            val subs = slot.captured
            subs.onError(errorDeleteChat)
        }

        // When
        presenter.deleteChat(exMessageId, onError, onSuccessDeleteConversation)

        // Then
        verifyOrder {
            onError.invoke(errorDeleteChat)
        }
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
            deleteMessageListUseCase.unsubscribe()
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
        presenter.followUnfollowShop(exShopId.toString(), onError, onSuccess)

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


    private fun mockkParseResponse(
            wsInfo: WebSocketInfo, isOpposite: Boolean = true
    ): ChatSocketPojo {
        val wsChatPojo = topChatRoomWebSocketMessageMapper.parseResponse(wsInfo.response).apply {
            this.isOpposite = isOpposite
        }
        every {
            topChatRoomWebSocketMessageMapper.parseResponse(wsInfo.response)
        } returns wsChatPojo
        return wsChatPojo
    }

    private fun mockkWsMapper(wsChatPojo: ChatSocketPojo): Visitable<*> {
        val wsChatVisitable = topChatRoomWebSocketMessageMapper.map(wsChatPojo)
        every { topChatRoomWebSocketMessageMapper.map(wsChatPojo) } returns wsChatVisitable
        return wsChatVisitable
    }

    private fun verifyReadMessageSentToWs() {
        verify(exactly = 1) { RxWebSocket.send(readParam, listInterceptor) }
    }

}