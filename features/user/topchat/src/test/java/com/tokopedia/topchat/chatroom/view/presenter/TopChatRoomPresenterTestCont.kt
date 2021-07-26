package com.tokopedia.topchat.chatroom.view.presenter

import android.content.SharedPreferences
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.chat_common.data.*
import com.tokopedia.chat_common.domain.pojo.ChatReplies
import com.tokopedia.chat_common.domain.pojo.ChatReplyPojo
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chatbot.domain.mapper.TopChatRoomWebSocketMessageMapper
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.pojo.ChatDelete
import com.tokopedia.topchat.chatlist.pojo.ChatDeleteStatus
import com.tokopedia.topchat.chatroom.data.UploadImageDummy
import com.tokopedia.topchat.chatroom.domain.usecase.*
import com.tokopedia.topchat.chatroom.service.UploadImageChatService
import com.tokopedia.topchat.chatroom.view.listener.TopChatContract
import com.tokopedia.topchat.chatroom.view.viewmodel.SendablePreview
import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateUiModel
import com.tokopedia.topchat.common.domain.MutationMoveChatToTrashUseCase
import com.tokopedia.topchat.common.util.ImageUtil
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.RxWebSocket
import com.tokopedia.websocket.RxWebSocketUtil
import com.tokopedia.websocket.WebSocketInfo
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.impl.annotations.SpyK
import junit.framework.Assert
import okhttp3.Interceptor
import okhttp3.WebSocket
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import rx.Observable
import rx.Subscriber
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.subjects.PublishSubject
import java.lang.reflect.Field
import java.lang.reflect.Modifier

class TopChatRoomPresenterTestCont {

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

    @Before
    fun before() {
        MockKAnnotations.init(this)
        UploadImageChatService.dummyMap.clear()
        mockSingletonObject()
        createPresenter()
        presenter.attachView(view)
        presenter.autoRetryConnectWs = false
        listInterceptor = arrayListOf(tkpdAuthInterceptor, fingerprintInterceptor)
        wsReconnect = WebSocketInfo.createReconnect("Some Error Comes Up")
        wsOpen = WebSocketInfo(webSocket, true)
        wsResponseReplyText = WebSocketInfo(webSocket,
            TopChatRoomPresenterTest.Dummy.wsResponseReplyString
        )
        wsResponseTyping = WebSocketInfo(webSocket,
            TopChatRoomPresenterTest.Dummy.wsResponseTypingString
        )
        wsResponseEndTyping = WebSocketInfo(webSocket,
            TopChatRoomPresenterTest.Dummy.wsResponseEndTypingString
        )
        wsResponseReadMessage = WebSocketInfo(webSocket,
            TopChatRoomPresenterTest.Dummy.wsResponseReadMessageString
        )
        wsResponseImageAttachment = WebSocketInfo(webSocket,
            TopChatRoomPresenterTest.Dummy.wsResponseImageAttachmentString
        )
        wsResponseProductAttachment = WebSocketInfo(webSocket,
            TopChatRoomPresenterTest.Dummy.wsResponseProductAttachmentString
        )
    }

    @After
    fun after() {
        UploadImageChatService.dummyMap.clear()
    }

    private fun createPresenter() {
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
        MatcherAssert.assertThat(presenter.newUnreadMessage, equalTo(0))
        verify(exactly = 1) { view.hideUnreadMessage() }
        verify(exactly = 1) { view.onReceiveMessageEvent(wsChatVisitable) }
        verifyReadMessageSentToWs()
    }

    @Test
    fun `onMessage ws event reply when response has different msgId`() {
        // Given
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false
        val wsChatPojo = mockkParseResponse(wsResponseReplyText)
        val wsChatVisitable = mockkWsMapper(wsChatPojo)

        // When
        presenter.connectWebSocket("123")
        websocketServer.onNext(wsResponseReplyText)

        // Then
        MatcherAssert.assertThat(presenter.newUnreadMessage, equalTo(0))
        verify(exactly = 0) { view.hideUnreadMessage() }
        verify(exactly = 0) { view.onReceiveMessageEvent(wsChatVisitable) }
        verify(exactly = 0) { RxWebSocket.send(readParam, listInterceptor) }
    }

    @Test
    fun `should remove SRW bubble if receive image attachment event`() {
        // Given
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false

        // When
        presenter.connectWebSocket(exMessageId)
        websocketServer.onNext(wsResponseImageAttachment)

        // Then
        verify(exactly = 1) { view.removeSrwBubble() }
    }

    @Test
    fun `should remove SRW bubble if receive product attachment event`() {
        // Given
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false
        val wsChatPojo = mockkParseResponse(wsResponseProductAttachment)
        val wsChatVisitable = mockkWsMapper(wsChatPojo) as ProductAttachmentViewModel

        // When
        presenter.connectWebSocket(exMessageId)
        websocketServer.onNext(wsResponseProductAttachment)

        // Then
        verify(exactly = 1) { view.removeSrwBubble(wsChatVisitable.productId) }
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
        MatcherAssert.assertThat(presenter.newUnreadMessage, equalTo(1))
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
    fun `check upload image using service`() {
        //Given
        val image = ImageUploadViewModel(
            "123", "123", "123", "test", "123"
        )

        mockkObject(UploadImageChatService)
        every {
            UploadImageChatService.enqueueWork(
                any(),
                any(),
                any()
            )
        } returns Unit

        every {
            remoteConfig.getBoolean(any(), any())
        } returns true

        setFinalStatic(Build::class.java.getField("MODEL"), "samsung")

        //When
        presenter.startUploadImages(image)

        //Then
        Assert.assertTrue(UploadImageChatService.dummyMap.isNotEmpty())
    }

    @Test
    fun `check upload image problematic device`() {
        //Given
        val image = mockk<ImageUploadViewModel>(relaxed = true)
        every {
            remoteConfig.getBoolean(any(), any())
        } returns true

        setFinalStatic(Build::class.java.getField("MODEL"), "iris88")

        //When
        presenter.startUploadImages(image)

        //Then
        verify { view.addDummyMessage(image) }
    }

    @Throws(Exception::class) //For mocking Build class
    fun setFinalStatic(field: Field, newValue: Any) {
        field.isAccessible = true

        val modifiersField = Field::class.java.getDeclaredField("modifiers")
        modifiersField.isAccessible = true
        modifiersField.setInt(field, field.modifiers and Modifier.FINAL.inv())

        field.set(null, newValue)
    }

    @Test
    fun `check upload image failed to get remote config`() {
        //Given
        val image = mockk<ImageUploadViewModel>()
        val exception = mockk<Exception>("Oops!")
        every {
            remoteConfig.getBoolean(any(), any())
        } throws exception

        //When
        presenter.startUploadImages(image)

        //Then
        verify { view.addDummyMessage(image) }
    }

    @Test
    fun `Get chat usecase called when load page`() {
        // Given
        val mockOnSuccess: (ChatroomViewModel, ChatReplies) -> Unit = mockk()
        val mockOnError: (Throwable) -> Unit = mockk()

        // When
        presenter.getExistingChat(exMessageId, mockOnError, mockOnSuccess)

        // Then
        verify(exactly = 1) {
            getChatUseCase.getFirstPageChat(
                exMessageId,
                mockOnSuccess,
                mockOnError
            )
        }
    }

    @Test
    fun `Get message usecase called when no message id provided`() {
        // Given
        val mockOnSuccess: (String) -> Unit = mockk()
        val mockOnError: (Throwable) -> Unit = mockk()

        // When
        presenter.getMessageId(toUserId, toShopId, source, mockOnError, mockOnSuccess)

        // Then
        verify(exactly = 1) {
            getExistingMessageIdUseCase.getMessageId(
                toShopId,
                toUserId,
                source,
                mockOnSuccess,
                mockOnError
            )
        }
    }

    @Test
    fun `Get chat usecase called when load top page chat`() {
        // Given
        val mockOnSuccess: (ChatroomViewModel, ChatReplies) -> Unit = mockk()
        val mockOnError: (Throwable) -> Unit = mockk()

        // When
        presenter.loadTopChat(exMessageId, mockOnError, mockOnSuccess)

        // Then
        verify(exactly = 1) {
            getChatUseCase.getTopChat(exMessageId, mockOnSuccess, mockOnError)
        }
    }

    @Test
    fun `Get chat usecase called when load bottom page chat`() {
        // Given
        val mockOnSuccess: (ChatroomViewModel, ChatReplies) -> Unit = mockk()
        val mockOnError: (Throwable) -> Unit = mockk()

        // When
        presenter.loadBottomChat(exMessageId, mockOnError, mockOnSuccess)

        // Then
        verify(exactly = 1) {
            getChatUseCase.getBottomChat(
                exMessageId,
                mockOnSuccess,
                mockOnError
            )
        }
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
            remoteConfig.getBoolean(TopChatRoomPresenter.ENABLE_UPLOAD_IMAGE_SERVICE)
        } throws IllegalStateException("Failed to get remote config value")
        every {
            ImageUtil.validateImageAttachment(imageUploadViewModel.imageUrl)
        } returns Pair(true, ImageUtil.IMAGE_VALID)
        every {
            compressImageUseCase.compressImage(imageUploadViewModel.imageUrl!!)
        } returns Observable.just(imageUploadViewModel.imageUrl)
        every {
            uploadImageUseCase.upload(
                imageUploadViewModel, captureLambda(),
                any()
            )
        } answers {
            val onSuccess = lambda<(String, ImageUploadViewModel) -> Unit>()
            onSuccess.invoke(exImageUploadId, imageUploadViewModel)
        }
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false
        val wsChatPojo = mockkParseResponse(wsResponseImageAttachment, false)
        val wsChatVisitable = mockkWsMapper(wsChatPojo)
        val websocketParam = TopChatWebSocketParam.generateParamSendImage(
            exMessageId, exImageUploadId, imageUploadViewModel.startTime)

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
        } returns Pair(true, ImageUtil.IMAGE_VALID)
        every {
            compressImageUseCase.compressImage(imageUploadViewModel.imageUrl!!)
        } returns Observable.just(imageUploadViewModel.imageUrl)
        every {
            uploadImageUseCase.upload(
                imageUploadViewModel, captureLambda(),
                any()
            )
        } answers {
            val onSuccess = lambda<(String, ImageUploadViewModel) -> Unit>()
            onSuccess.invoke(
                exImageUploadId,
                imageUploadViewModel
            )
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
        } returns Pair(true, ImageUtil.IMAGE_VALID)
        every {
            compressImageUseCase.compressImage(imageUploadViewModel.imageUrl!!)
        } returns Observable.just(imageUploadViewModel.imageUrl)

        every {
            uploadImageUseCase.upload(
                imageUploadViewModel, captureLambda(), any())
        } answers {
            val onSuccess = lambda<(String, ImageUploadViewModel) -> Unit>()
            onSuccess.invoke(exImageUploadId, imageUploadViewModel)
        }
        coEvery {
            replyChatGQLUseCase.replyMessage(
                any(),
                any(),
                any(),
                any()
            )
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
        } returns Pair(true, ImageUtil.IMAGE_VALID)
        every {
            compressImageUseCase.compressImage(imageUploadViewModel.imageUrl!!)
        } returns Observable.just(imageUploadViewModel.imageUrl)
        every {
            uploadImageUseCase.upload(
                imageUploadViewModel,
                any(), captureLambda())
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
    fun `should have dummy image when upload image by service`() {
        // Given
        val imageViewModel = ImageUploadViewModel(
            exMessageId, "fromUid", "attachmentId",
            "fileLoc", "startTime"
        )
        val imageDummy = UploadImageDummy(messageId = exMessageId, visitable = imageViewModel)
        mockkObject(UploadImageChatService.Companion)
        every {
            UploadImageChatService.enqueueWork(any(), any(), any())
        } returns Unit
        every {
            remoteConfig.getBoolean(TopChatRoomPresenter.ENABLE_UPLOAD_IMAGE_SERVICE, any())
        } returns true

        // When
        presenter.connectWebSocket(exMessageId)
        presenter.startUploadImages(imageViewModel)

        // Then
        verify (exactly = 1) { view.addDummyMessage(imageViewModel) }
        MatcherAssert.assertThat(UploadImageChatService.dummyMap, hasItem(imageDummy))
        MatcherAssert.assertThat(UploadImageChatService.dummyMap.size, `is`(1))
    }

    @Test
    fun `on error image file to upload validation IMAGE_UNDERSIZE`() {
        // Given
        every {
            ImageUtil.validateImageAttachment(imageUploadViewModel.imageUrl)
        } returns Pair(false, ImageUtil.IMAGE_UNDERSIZE)

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
        } returns Pair(false, ImageUtil.IMAGE_EXCEED_SIZE_LIMIT)

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
        Assert.assertTrue(uploadImageUseCase.isUploading)
    }

    @Test
    fun `on success send attachment and message through Websocket`() {
        // Given
        val mockOnSendingMessage: () -> Unit = mockk(relaxed = true)
        val dummyMessage = MessageViewModel(
            exMessageId, userSession.userId, userSession.name, TopChatRoomPresenterTest.Dummy.exStartTime, TopChatRoomPresenterTest.Dummy.exSendMessage
        )
        val paramSendMessage = "paramSendMessage"
        val paramStopTyping = TopChatWebSocketParam.generateParamStopTyping(exMessageId)
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false
        every {
            topChatRoomWebSocketMessageMapper.mapToDummyMessage(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns dummyMessage
        every {
            TopChatWebSocketParam.generateParamSendMessage(
                any(),
                any(),
                any(),
                any()
            )
        } returns paramSendMessage

        // When
        presenter.connectWebSocket(exMessageId)
        presenter.addAttachmentPreview(sendAbleProductPreview)
        presenter.sendAttachmentsAndMessage(
            exMessageId,
            TopChatRoomPresenterTest.Dummy.exSendMessage,
            TopChatRoomPresenterTest.Dummy.exStartTime,
            TopChatRoomPresenterTest.Dummy.exOpponentId, mockOnSendingMessage
        )

        // Then
        verify(exactly = 1) {
            sendAbleProductPreview.generateMsgObj(
                exMessageId,
                TopChatRoomPresenterTest.Dummy.exOpponentId,
                TopChatRoomPresenterTest.Dummy.exSendMessage,
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
            exMessageId, userSession.userId, userSession.name, TopChatRoomPresenterTest.Dummy.exStartTime, TopChatRoomPresenterTest.Dummy.exSendMessage
        )
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false
        every {
            topChatRoomWebSocketMessageMapper.mapToDummyMessage(
                any(),
                any(),
                any(),
                any(),
                any()
            )
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
            exMessageId,
            TopChatRoomPresenterTest.Dummy.exSendMessage,
            TopChatRoomPresenterTest.Dummy.exStartTime,
            TopChatRoomPresenterTest.Dummy.exOpponentId, mockOnSendingMessage
        )

        // Then
        verify(exactly = 1) { view.addDummyMessage(dummyMessage) }
        verify(exactly = 1) { view.onReceiveMessageEvent(replyChatViewModelApiSuccess.chat) }
        verify(exactly = 1) { view.removeDummy(dummyMessage) }
    }

    @Test
    fun `on success delete chat`() {
        // Given
        val successDelete = ChatDelete(
            isSuccess = 1, detailResponse = "", messageId = exMessageId.toLong())
        val result = ChatDeleteStatus().apply {
            this.chatMoveToTrash.list = listOf(successDelete)
        }

        val onError: (Throwable) -> Unit = mockk(relaxed = true)
        val onSuccessDeleteConversation: () -> Unit = mockk(relaxed = true)
        coEvery {
            moveChatToTrashUseCase.execute(exMessageId)
        } returns result

        // When
        presenter.deleteChat(exMessageId, onError, onSuccessDeleteConversation)

        // Then
        verify {
            onSuccessDeleteConversation.invoke()
        }
    }

    @Test
    fun `on failed to delete chat`() {
        // Given
        val failedDelete = ChatDelete(
            isSuccess = 0, detailResponse = "Error", messageId = exMessageId.toLong())
        val result = ChatDeleteStatus().apply {
            this.chatMoveToTrash.list = listOf(failedDelete)
        }

        val onError: (Throwable) -> Unit = mockk(relaxed = true)
        val onSuccessDeleteConversation: () -> Unit = mockk(relaxed = true)
        coEvery {
            moveChatToTrashUseCase.execute(exMessageId)
        } returns result

        // When
        presenter.deleteChat(exMessageId, onError, onSuccessDeleteConversation)

        // Then
        verify {
            onError.invoke(any())
        }
    }

    @Test
    fun `on error delete chat`() {
        // Given
        val throwable = mockk<Throwable>(relaxed = true)
        val onError: (Throwable) -> Unit = mockk(relaxed = true)
        val onSuccessDeleteConversation: () -> Unit = mockk(relaxed = true)
        coEvery {
            moveChatToTrashUseCase.execute(exMessageId)
        } throws throwable

        // When
        presenter.deleteChat(exMessageId, onError, onSuccessDeleteConversation)

        // Then
        verify {
            onError.invoke(throwable)
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

    companion object {
        const val exMessageId = "190378584"
        const val exImageUploadId = "667056"
        const val toUserId = "12345"
        const val toShopId = "54321"
        const val source = "askseller"

        val imageUploadViewModel = ImageUploadViewModel(
            exMessageId,
            "123123",
            "123987",
            "https://ecs.tokopedia.com/image.jpg",
            "123"
        )

        val readParam = TopChatWebSocketParam.generateParamRead(exMessageId)
        val replyChatViewModelApiSuccess = generateReplyChatViewModelApi()

        private fun generateReplyChatViewModelApi(): ReplyChatViewModel {
            return ReplyChatViewModel(imageUploadViewModel, true)
        }
    }
}