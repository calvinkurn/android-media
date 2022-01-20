package com.tokopedia.topchat.chatroom.viewmodel.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chatbot.domain.mapper.TopChatRoomWebSocketMessageMapper
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.topchat.chatroom.domain.mapper.ChatAttachmentMapper
import com.tokopedia.topchat.chatroom.domain.mapper.TopChatRoomGetExistingChatMapper
import com.tokopedia.topchat.chatroom.domain.usecase.*
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatViewModel
import com.tokopedia.topchat.common.domain.MutationMoveChatToTrashUseCase
import com.tokopedia.topchat.common.network.TopchatCacheManager
import com.tokopedia.topchat.common.websocket.*
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.junit.Before
import org.junit.Rule

abstract class BaseTopChatViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    //UseCases
    @MockK
    lateinit var getExistingMessageIdUseCase: GetExistingMessageIdUseCase

    @RelaxedMockK
    lateinit var getShopFollowingUseCase: GetShopFollowingUseCase

    @RelaxedMockK
    lateinit var toggleFavouriteShopUseCase: ToggleFavouriteShopUseCase

    @RelaxedMockK
    lateinit var addToCartUseCase: AddToCartUseCase

    @RelaxedMockK
    lateinit var seamlessLoginUsecase: SeamlessLoginUsecase

    @RelaxedMockK
    lateinit var getChatRoomSettingUseCase: GetChatRoomSettingUseCase

    @RelaxedMockK
    lateinit var orderProgressUseCase: OrderProgressUseCase

    @MockK
    lateinit var reminderTickerUseCase: GetReminderTickerUseCase

    @RelaxedMockK
    lateinit var closeReminderTicker: CloseReminderTicker

    @RelaxedMockK
    lateinit var addToCartOccMultiUseCase: AddToCartOccMultiUseCase

    @MockK
    lateinit var chatToggleBlockChatUseCase: ChatToggleBlockChatUseCase

    @RelaxedMockK
    lateinit var mutationMoveChatToTrashUseCase: MutationMoveChatToTrashUseCase

    @RelaxedMockK
    lateinit var getChatBackgroundUseCase: GetChatBackgroundUseCase

    @MockK
    lateinit var chatAttachmentUseCase: ChatAttachmentUseCase

    @RelaxedMockK
    lateinit var getChatListGroupStickerUseCase: GetChatListGroupStickerUseCase

    @MockK
    lateinit var getSmartReplyQuestionUseCase: GetSmartReplyQuestionUseCase

    @RelaxedMockK
    lateinit var getChatTokoNowWarehouseUseCase: GetChatTokoNowWarehouseUseCase

    @RelaxedMockK
    lateinit var addWishListUseCase: AddWishListUseCase

    @RelaxedMockK
    lateinit var removeWishListUseCase: RemoveWishListUseCase

    @RelaxedMockK
    lateinit var getChatUseCase: GetChatUseCase

    //Misc
    @RelaxedMockK
    lateinit var unsendReplyUseCase: UnsendReplyUseCase

    @RelaxedMockK
    lateinit var remoteConfig: RemoteConfig

    @RelaxedMockK
    lateinit var cacheManager: TopchatCacheManager

    @RelaxedMockK
    lateinit var chatAttachmentMapper: ChatAttachmentMapper

    @RelaxedMockK
    lateinit var existingChatMapper: TopChatRoomGetExistingChatMapper

    @RelaxedMockK
    lateinit var chatWebSocket: TopchatWebSocket

    @RelaxedMockK
    lateinit var webSocketStateHandler: WebSocketStateHandler

    protected val webSocketParser: WebSocketParser = DefaultWebSocketParser()

    protected val topChatRoomWebSocketMessageMapper = TopChatRoomWebSocketMessageMapper()

    @RelaxedMockK
    lateinit var payloadGenerator: WebsocketPayloadGenerator

    @RelaxedMockK
    lateinit var uploadImageUseCase: TopchatUploadImageUseCase

    @RelaxedMockK
    lateinit var compressImageUseCase: CompressImageUseCase

    private val dispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider

    protected lateinit var viewModel: TopChatViewModel

    protected val testShopId = "123"
    protected val testUserId = "345"
    protected val source = "testSource"
    protected val expectedThrowable = Throwable("Oops!")
    protected val testMessageId = "123123"

    @RelaxedMockK
    lateinit var websocket: WebSocket

    @RelaxedMockK
    lateinit var websocketResponse: Response

    @Before
    open fun before() {
        MockKAnnotations.init(this)
        viewModel = TopChatViewModel(
            getExistingMessageIdUseCase,
            getShopFollowingUseCase,
            toggleFavouriteShopUseCase,
            addToCartUseCase,
            seamlessLoginUsecase,
            getChatRoomSettingUseCase,
            orderProgressUseCase,
            reminderTickerUseCase,
            closeReminderTicker,
            addToCartOccMultiUseCase,
            chatToggleBlockChatUseCase,
            mutationMoveChatToTrashUseCase,
            getChatBackgroundUseCase,
            chatAttachmentUseCase,
            getChatListGroupStickerUseCase,
            getSmartReplyQuestionUseCase,
            getChatTokoNowWarehouseUseCase,
            addWishListUseCase,
            removeWishListUseCase,
            getChatUseCase,
            unsendReplyUseCase,
            dispatchers,
            remoteConfig,
            chatAttachmentMapper,
            existingChatMapper,
            chatWebSocket,
            webSocketStateHandler,
            webSocketParser,
            topChatRoomWebSocketMessageMapper,
            payloadGenerator,
            uploadImageUseCase,
            compressImageUseCase
        )
    }

    protected fun onConnectWebsocket(listener: (WebSocketListener) -> Unit) {
        val slot = slot<WebSocketListener>()
        every { chatWebSocket.connectWebSocket(capture(slot)) } answers {
            listener.invoke(slot.captured)
        }
    }

    protected fun verifySendMarkAsRead() {
        val payload = payloadGenerator.generateMarkAsReadPayload(viewModel.roomMetaData)
        verify {
            chatWebSocket.sendPayload(payload)
        }
    }

    protected fun verifySendStopTyping() {
        val payload = payloadGenerator.generateWsPayloadStopTyping(viewModel.roomMetaData.msgId)
        verify {
            chatWebSocket.sendPayload(payload)
        }
    }

    protected fun generateChatPojoFromWsResponse(response: String): ChatSocketPojo {
        val wsResponse = webSocketParser.parseResponse(response)
        return topChatRoomWebSocketMessageMapper.parseResponse(wsResponse)
    }
}