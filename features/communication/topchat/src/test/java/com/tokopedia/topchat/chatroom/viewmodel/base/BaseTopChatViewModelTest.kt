package com.tokopedia.topchat.chatroom.viewmodel.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.State
import androidx.lifecycle.LifecycleObserver
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.topchat.chatroom.domain.mapper.ChatAttachmentMapper
import com.tokopedia.topchat.chatroom.domain.mapper.GetTemplateChatRoomMapper
import com.tokopedia.topchat.chatroom.domain.mapper.TopChatRoomGetExistingChatMapper
import com.tokopedia.topchat.chatroom.domain.mapper.TopChatRoomWebSocketMessageMapper
import com.tokopedia.topchat.chatroom.domain.usecase.*
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatRoomWebSocketViewModel
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatViewModel
import com.tokopedia.topchat.chattemplate.domain.usecase.GetTemplateUseCase
import com.tokopedia.topchat.common.domain.MutationMoveChatToTrashUseCase
import com.tokopedia.topchat.common.network.TopchatCacheManager
import com.tokopedia.topchat.common.websocket.*
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.junit.Before
import org.junit.Rule

abstract class BaseTopChatViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule = UnconfinedTestRule()

    // UseCases
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
    lateinit var addToWishlistV2UseCase: AddToWishlistV2UseCase

    @RelaxedMockK
    lateinit var deleteWishlistV2UseCase: DeleteWishlistV2UseCase

    @RelaxedMockK
    lateinit var getChatUseCase: GetChatUseCase

    // Misc
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
    lateinit var getTemplateChatRoomUseCase: GetTemplateUseCase

    @RelaxedMockK
    lateinit var chatPreAttachPayload: GetChatPreAttachPayloadUseCase

    private val getTemplateChatRoomMapper: GetTemplateChatRoomMapper = GetTemplateChatRoomMapper()
    private val dispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider

    protected lateinit var viewModel: TopChatViewModel
    protected lateinit var webSocketViewModel: TopChatRoomWebSocketViewModel

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
            addToWishlistV2UseCase,
            deleteWishlistV2UseCase,
            getChatUseCase,
            unsendReplyUseCase,
            dispatchers,
            chatAttachmentMapper,
            existingChatMapper,
            getTemplateChatRoomUseCase,
            getTemplateChatRoomMapper,
            chatPreAttachPayload
        )

        webSocketViewModel = TopChatRoomWebSocketViewModel(
            chatWebSocket,
            webSocketStateHandler,
            webSocketParser,
            topChatRoomWebSocketMessageMapper,
            uploadImageUseCase,
            payloadGenerator,
            remoteConfig,
            dispatchers
        )

        webSocketViewModel.isInTheMiddleOfThePage = false
        webSocketViewModel.isFromBubble = false
    }

    protected fun onConnectWebsocket(listener: (WebSocketListener) -> Unit) {
        val slot = slot<WebSocketListener>()
        every { chatWebSocket.connectWebSocket(capture(slot)) } answers {
            listener.invoke(slot.captured)
        }
    }

    protected fun verifySendMarkAsRead() {
        val payload = payloadGenerator.generateMarkAsReadPayload(
            viewModel.roomMetaData.value ?: RoomMetaData()
        )
        verify {
            chatWebSocket.sendPayload(payload)
        }
    }

    protected fun verifySendStopTyping() {
        val payload = payloadGenerator.generateWsPayloadStopTyping(
            viewModel.roomMetaData.value?.msgId ?: ""
        )
        verify {
            chatWebSocket.sendPayload(payload)
        }
    }

    protected fun generateChatPojoFromWsResponse(response: String): ChatSocketPojo {
        val wsResponse = webSocketParser.parseResponse(response)
        return topChatRoomWebSocketMessageMapper.parseResponse(wsResponse)
    }

    protected fun getDummyLifeCycle(state: State = State.RESUMED): Lifecycle {
        return object : Lifecycle() {
            override fun addObserver(observer: LifecycleObserver) {}
            override fun removeObserver(observer: LifecycleObserver) {}
            override fun getCurrentState(): State {
                return state
            }
        }
    }
}
