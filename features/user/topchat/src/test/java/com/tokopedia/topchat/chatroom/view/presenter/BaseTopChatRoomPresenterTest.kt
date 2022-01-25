package com.tokopedia.topchat.chatroom.view.presenter

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.attachcommon.data.ResultProduct
import com.tokopedia.chat_common.data.AttachmentType
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.chat_common.data.ReplyChatViewModel
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chatbot.domain.mapper.TopChatRoomWebSocketMessageMapper
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.topchat.FileUtil
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.Sticker
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.ChatListGroupStickerResponse
import com.tokopedia.topchat.chatroom.domain.usecase.*
import com.tokopedia.topchat.chatroom.service.UploadImageBroadcastListener
import com.tokopedia.topchat.chatroom.service.UploadImageChatService
import com.tokopedia.topchat.chatroom.view.listener.TopChatContract
import com.tokopedia.topchat.chatroom.view.viewmodel.InvoicePreviewUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.SendablePreview
import com.tokopedia.topchat.chatroom.view.viewmodel.TopchatProductAttachmentPreviewUiModel
import com.tokopedia.topchat.common.util.ImageUtil
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.RxWebSocket
import com.tokopedia.websocket.RxWebSocketUtil
import com.tokopedia.websocket.WebSocketInfo
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.impl.annotations.SpyK
import io.mockk.mockkObject
import io.mockk.spyk
import okhttp3.Interceptor
import okhttp3.WebSocket
import org.junit.Before
import org.junit.Rule
import rx.subjects.PublishSubject

abstract class BaseTopChatRoomPresenterTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    protected lateinit var tkpdAuthInterceptor: TkpdAuthInterceptor

    @RelaxedMockK
    protected lateinit var fingerprintInterceptor: FingerprintInterceptor

    @RelaxedMockK
    protected lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    protected lateinit var webSocketUtil: RxWebSocketUtil

    @RelaxedMockK
    protected lateinit var getTemplateChatRoomUseCase: GetTemplateChatRoomUseCase

    @RelaxedMockK
    protected lateinit var replyChatUseCase: ReplyChatUseCase

    @RelaxedMockK
    protected lateinit var compressImageUseCase: CompressImageUseCase

    @RelaxedMockK
    protected lateinit var uploadImageUseCase: TopchatUploadImageUseCase

    @RelaxedMockK
    protected lateinit var replyChatGQLUseCase: ReplyChatGQLUseCase

    @RelaxedMockK
    protected lateinit var sharedPref: SharedPreferences

    protected val dispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider

    @RelaxedMockK
    protected lateinit var webSocket: WebSocket

    @RelaxedMockK
    protected lateinit var view: TopChatContract.View

    @RelaxedMockK
    protected lateinit var uploadImageBroadcastListener: UploadImageBroadcastListener

    @RelaxedMockK
    protected lateinit var sendAbleProductPreview: SendablePreview

    @RelaxedMockK
    protected lateinit var remoteConfig: RemoteConfig

    @SpyK
    protected var topChatRoomWebSocketMessageMapper = TopChatRoomWebSocketMessageMapper()

    protected lateinit var presenter: TopChatRoomPresenter

    protected lateinit var listInterceptor: ArrayList<Interceptor>

    protected lateinit var wsOpen: WebSocketInfo
    protected lateinit var wsReconnect: WebSocketInfo
    protected lateinit var wsResponseReplyText: WebSocketInfo
    protected lateinit var wsResponseTyping: WebSocketInfo
    protected lateinit var wsResponseEndTyping: WebSocketInfo
    protected lateinit var wsResponseReadMessage: WebSocketInfo
    protected lateinit var wsResponseImageAttachment: WebSocketInfo
    protected lateinit var wsResponseProductAttachment: WebSocketInfo
    protected val websocketServer = PublishSubject.create<WebSocketInfo>()

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
        const val exImageUploadId = "667056"
        const val toUserId = "12345"
        const val toShopId = "54321"

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
        val successGetChatListGroupSticker: ChatListGroupStickerResponse = FileUtil.parse(
            "/success_chat_list_group_sticker.json",
            ChatListGroupStickerResponse::class.java
        )

        fun generateSendAbleProductPreview(): SendablePreview {
            return TopchatProductAttachmentPreviewUiModel.Builder().apply {
                withProductName("hello product")
                withImages(listOf(exImageUrl))
                withProductPrice("Rp120.000")
                withProductId("12398764")
            }.build()
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

        val imageUploadViewModel = ImageUploadUiModel.Builder()
            .withMsgId(exMessageId)
            .withFromUid("123123")
            .withAttachmentId("123987")
            .withAttachmentType(AttachmentType.Companion.TYPE_IMAGE_UPLOAD)
            .withReplyTime(SendableUiModel.SENDING_TEXT)
            .withStartTime("123")
            .withIsDummy(true)
            .withImageUrl("https://ecs.tokopedia.com/image.jpg")
            .build()

        val readParam = TopChatWebSocketParam.generateParamRead(exMessageId)
        val replyChatViewModelApiSuccess = generateReplyChatViewModelApi()

        private fun generateReplyChatViewModelApi(): ReplyChatViewModel {
            return ReplyChatViewModel(imageUploadViewModel, true)
        }
    }

    @Before
    fun before() {
        MockKAnnotations.init(this)
        UploadImageChatService.dummyMap.clear()
        mockSingletonObject()
        presenter = spyk(
            TopChatRoomPresenter(
                userSession,
                topChatRoomWebSocketMessageMapper,
                getTemplateChatRoomUseCase,
                dispatchers,
            )
        )
        presenter.attachView(view)
        listInterceptor = arrayListOf(tkpdAuthInterceptor, fingerprintInterceptor)
        wsReconnect = WebSocketInfo.createReconnect("Some Error Comes Up")
        wsOpen = WebSocketInfo(webSocket, true)
        wsResponseReplyText = WebSocketInfo(webSocket, Dummy.wsResponseReplyString)
        wsResponseTyping = WebSocketInfo(webSocket, Dummy.wsResponseTypingString)
        wsResponseEndTyping = WebSocketInfo(webSocket, Dummy.wsResponseEndTypingString)
        wsResponseReadMessage = WebSocketInfo(webSocket, Dummy.wsResponseReadMessageString)
        wsResponseImageAttachment = WebSocketInfo(webSocket, Dummy.wsResponseImageAttachmentString)
        wsResponseProductAttachment = WebSocketInfo(webSocket,
            Dummy.wsResponseProductAttachmentString
        )
    }

    private fun mockSingletonObject() {
        mockkObject(RxWebSocket)
        mockkObject(RxWebSocketUtil)
        mockkObject(ImageUtil)
        mockkObject(TopChatWebSocketParam)
        every { RxWebSocketUtil.getInstance(any()) } returns webSocketUtil
        mockkObject(UploadImageChatService)
    }
}