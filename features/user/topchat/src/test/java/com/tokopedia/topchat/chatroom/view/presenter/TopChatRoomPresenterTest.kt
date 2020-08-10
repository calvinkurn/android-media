package com.tokopedia.topchat.chatroom.view.presenter

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chatbot.domain.mapper.TopChatRoomWebSocketMessageMapper
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.seamless_login.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.topchat.FileUtil
import com.tokopedia.topchat.chatlist.domain.usecase.DeleteMessageListUseCase
import com.tokopedia.topchat.chatroom.domain.usecase.*
import com.tokopedia.topchat.chatroom.view.listener.TopChatContract
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.exMessageId
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.readParam
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.wsResponseReply
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.RxWebSocket
import com.tokopedia.websocket.RxWebSocketUtil
import com.tokopedia.websocket.WebSocketInfo
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.impl.annotations.SpyK
import okhttp3.Interceptor
import okhttp3.WebSocket
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Observable

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
    private lateinit var changeChatBlockSettingUseCase: ChangeChatBlockSettingUseCase

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
    private lateinit var sharedPref: SharedPreferences

    @RelaxedMockK
    private lateinit var webSocket: WebSocket

    @RelaxedMockK
    private lateinit var view: TopChatContract.View

    @SpyK
    private var topChatRoomWebSocketMessageMapper = TopChatRoomWebSocketMessageMapper()

    private lateinit var presenter: TopChatRoomPresenter

    private lateinit var listInterceptor: ArrayList<Interceptor>

    private lateinit var wsOpen: WebSocketInfo
    private lateinit var wsReconnect: WebSocketInfo
    private lateinit var wsResponseReplyText: WebSocketInfo

    object Dummy {
        const val exMessageId = "190378584"
        val readParam = TopChatWebSocketParam.generateParamRead(exMessageId)
        val wsResponseReply = FileUtil.readFileContent("/ws_response_reply_text_is_opposite.json")
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
                        changeChatBlockSettingUseCase,
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
                        sharedPref
                )
        )
        presenter.attachView(view)
        presenter.autoRetryConnectWs = false
        listInterceptor = arrayListOf(tkpdAuthInterceptor, fingerprintInterceptor)
        wsReconnect = WebSocketInfo.createReconnect("Some Error Comes Up")
        wsOpen = WebSocketInfo(webSocket, true)
        wsResponseReplyText = WebSocketInfo(webSocket, wsResponseReply)
    }

    private fun mockSingletonObject() {
        mockkObject(RxWebSocket)
        mockkObject(RxWebSocketUtil)
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
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns Observable.just(wsOpen)

        // When
        presenter.connectWebSocket(exMessageId)

        // Then
        verify(exactly = 1) { view.showErrorWebSocket(false) }
        verifyReadMessageSentToWs()
    }


    @Test
    fun `onReconnect webscoket`() {
        // Given
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns Observable.just(wsReconnect)

        // When
        presenter.connectWebSocket(exMessageId)

        // Then
        verify(exactly = 1) { view.showErrorWebSocket(true) }
    }


    @Test
    fun `onClose ws`() {
        // Given
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns Observable.just(wsOpen)

        // When
        presenter.connectWebSocket(exMessageId)

        // Then
        verify(exactly = 2) { presenter.destroyWebSocket() }
        verify(exactly = 1) { view.showErrorWebSocket(true) }
    }

    @Test
    fun `onMessage ws event reply when not in the middle of the page`() {
        // Given
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns Observable.just(wsResponseReplyText)
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false
        val wsChatPojo = mockkParseResponse(wsResponseReplyText)
        val wsChatVisitable = mockkWsMapper(wsChatPojo)

        // When
        presenter.connectWebSocket(exMessageId)

        // Then
        assertThat(presenter.newUnreadMessage, equalTo(0))
        verify(exactly = 1) { view.hideUnreadMessage() }
        verify(exactly = 1) { view.onReceiveMessageEvent(wsChatVisitable) }
        verifyReadMessageSentToWs()
    }

    private fun mockkParseResponse(wsInfo: WebSocketInfo): ChatSocketPojo {
        val wsChatPojo = topChatRoomWebSocketMessageMapper.parseResponse(wsInfo.response)
        every { topChatRoomWebSocketMessageMapper.parseResponse(wsInfo.response) } returns wsChatPojo
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