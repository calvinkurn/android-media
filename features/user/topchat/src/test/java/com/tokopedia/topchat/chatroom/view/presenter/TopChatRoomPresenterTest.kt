package com.tokopedia.topchat.chatroom.view.presenter

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.chatbot.domain.mapper.TopChatRoomWebSocketMessageMapper
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.seamless_login.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.topchat.chatlist.domain.usecase.DeleteMessageListUseCase
import com.tokopedia.topchat.chatroom.domain.usecase.*
import com.tokopedia.topchat.chatroom.view.listener.TopChatContract
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenterTest.Dummy.exMessageId
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.RxWebSocketUtil
import com.tokopedia.websocket.WebSocketInfo
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import okhttp3.WebSocket
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
    private lateinit var topChatRoomWebSocketMessageMapper: TopChatRoomWebSocketMessageMapper

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
    private lateinit var sharedPref: SharedPreferences

    @RelaxedMockK
    private lateinit var webSocket: WebSocket

    @RelaxedMockK
    private lateinit var view: TopChatContract.View

    lateinit var presenter: TopChatRoomPresenter

    object Dummy {
        val exMessageId = "1234051"
    }

    @Before
    fun before() {
        MockKAnnotations.init(this)
        presenter = TopChatRoomPresenter(
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
                sharedPref
        )
        presenter.attachView(view)
        presenter.autoRetryConnectWs = false
    }

    @Test
    fun `onOpen connect to webscoket`() {
        // Given
        val wsOpen = WebSocketInfo(webSocket, true)
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns Observable.just(wsOpen)

        // When
        presenter.connectWebSocket(exMessageId)

        // Then
        verify(exactly = 1) { view.showErrorWebSocket(false) }
    }


    @Test
    fun `onReconnect webscoket`() {
        // Given
        val wsReconnect = WebSocketInfo.createReconnect("Some Error Comes Up")
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns Observable.just(wsReconnect)

        // When
        presenter.connectWebSocket(exMessageId)

        // Then
        verify(exactly = 1) { view.showErrorWebSocket(true) }
    }

}