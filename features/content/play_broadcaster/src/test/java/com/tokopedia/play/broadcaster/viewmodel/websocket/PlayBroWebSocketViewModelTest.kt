package com.tokopedia.play.broadcaster.viewmodel.websocket

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.tokopedia.play.broadcaster.domain.model.GetSocketCredentialResponse
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.domain.usecase.GetSocketCredentialUseCase
import com.tokopedia.play.broadcaster.fake.FakePlayWebSocket
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.model.interactive.InteractiveUiModelBuilder
import com.tokopedia.play.broadcaster.model.websocket.WebSocketUiModelBuilder
import com.tokopedia.play.broadcaster.robot.PlayBroadcastViewModelRobot
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroProductUiMapper
import com.tokopedia.play.broadcaster.ui.model.game.GameType
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.assertFalse
import com.tokopedia.play.broadcaster.util.assertTrue
import com.tokopedia.play.broadcaster.util.assertType
import com.tokopedia.play.broadcaster.util.getOrAwaitValue
import com.tokopedia.play.broadcaster.util.logger.PlayLogger
import com.tokopedia.play.broadcaster.util.millisFromNow
import com.tokopedia.play.broadcaster.view.state.PlayLiveTimerState
import com.tokopedia.play_common.model.dto.interactive.InteractiveUiModel
import com.tokopedia.play_common.model.dto.interactive.PlayInteractiveTimeStatus
import com.tokopedia.play_common.util.event.Event
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.yield
import okhttp3.internal.wait
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on February 21, 2022
 */
class PlayBroWebSocketViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)
    private val mockLogger: PlayLogger = mockk(relaxed = true)
    private val fakePlayWebSocket= FakePlayWebSocket(testDispatcher)

    private val uiModelBuilder = UiModelBuilder()
    private val webSocketUiModelBuilder = WebSocketUiModelBuilder()
    private val interactiveUiModelBuilder = InteractiveUiModelBuilder()

    private val mockConfig = uiModelBuilder.buildConfigurationUiModel(
        streamAllowed = true,
        channelId = "123"
    )
    private val mockException = uiModelBuilder.buildException()

    @Before
    fun setUp() {
        coEvery { mockRepo.getChannelConfiguration() } returns mockConfig
    }

    @Test
    fun `when user received new total view event, then it should emit new total view data`() {
        val mockTotalViewString = webSocketUiModelBuilder.buildTotalViewString()
        val mockTotalView = webSocketUiModelBuilder.buildTotalViewModel()

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            playBroadcastWebSocket = fakePlayWebSocket,
        )

        robot.use {
            robot.executeViewModelPrivateFunction("startWebSocket")
            fakePlayWebSocket.fakeEmitMessage(mockTotalViewString)

            val result = robot.getViewModel().observableTotalView.getOrAwaitValue()

            result.assertEqualTo(mockTotalView)
        }
    }

    @Test
    fun `when user received new total like event, then it should emit new total like data`() {
        val mockTotalLikeString = webSocketUiModelBuilder.buildTotalLikeString()
        val mockTotalLike = webSocketUiModelBuilder.buildTotalLikeModel()

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            playBroadcastWebSocket = fakePlayWebSocket,
        )

        robot.use {
            robot.executeViewModelPrivateFunction("startWebSocket")
            fakePlayWebSocket.fakeEmitMessage(mockTotalLikeString)

            val result = robot.getViewModel().observableTotalLike.getOrAwaitValue()

            result.assertEqualTo(mockTotalLike)
        }
    }

    @Test
    fun `when user received chat event, then it should emit chat data`() {
        val mockChatString = webSocketUiModelBuilder.buildChatString()
        val mockChat = webSocketUiModelBuilder.buildChatModel()

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            playBroadcastWebSocket = fakePlayWebSocket,
        )

        robot.use {
            robot.executeViewModelPrivateFunction("startWebSocket")
            fakePlayWebSocket.fakeEmitMessage(mockChatString)
            val result = robot.getViewModel().observableChatList.getOrAwaitValue()

            result.assertEqualTo(listOf(mockChat))
        }
    }

    @Test
    fun `when user received pinned message event, then it should emit new pinned message data`() {
        val mockMessage = "Cek produk ini disini!"
        val mockPinnedMessageString = webSocketUiModelBuilder.buildPinnedMessageString(title = mockMessage)

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            playBroadcastWebSocket = fakePlayWebSocket,
        )

        robot.use {
            val state = robot.recordState {
                getConfig()
                robot.executeViewModelPrivateFunction("startWebSocket")
                fakePlayWebSocket.fakeEmitMessage(mockPinnedMessageString)
            }

            state.pinnedMessage.message.assertEqualTo(mockMessage)
        }
    }

    @Test
    fun `when user received live duration event and remaining time is 0, then it should log socket type`() {
        val mockLiveDurationString = webSocketUiModelBuilder.buildLiveDurationString()
        val mockLiveDuration = webSocketUiModelBuilder.buildLiveDurationModel()

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            logger = mockLogger,
            playBroadcastWebSocket = fakePlayWebSocket,
        )

        robot.use {
            robot.executeViewModelPrivateFunction("startWebSocket")
            fakePlayWebSocket.fakeEmitMessage(mockLiveDurationString)

            verify { mockLogger.logSocketType(mockLiveDuration) }
        }
    }

    @Test
    fun `when user received freeze event and its freeze, it should emit stop livestream`() {
        val mockFreezeString = webSocketUiModelBuilder.buildFreezeString()
        val mockFreeze = webSocketUiModelBuilder.buildFreezeModel()

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            logger = mockLogger,
            playBroadcastWebSocket = fakePlayWebSocket,
        )

        robot.use {
            val observableTimerState = it.getViewModelPrivateField<MutableLiveData<PlayLiveTimerState>>("_observableLiveTimerState")
            observableTimerState.value = PlayLiveTimerState.Active(remainingInMs = 1000)

            robot.executeViewModelPrivateFunction("startWebSocket")
            fakePlayWebSocket.fakeEmitMessage(mockFreezeString)

            val eventResult = robot.getViewModel().observableEvent.getOrAwaitValue()

            verify { mockLogger.logSocketType(any()) }
            fakePlayWebSocket.isOpen().assertFalse()
            eventResult.assertEqualTo(mockFreeze)
        }
    }

    @Test
    fun `when user received banned event and its banned, it should emit stop livestream`() {
        val mockBannedString = webSocketUiModelBuilder.buildBannedString()
        val mockBanned = webSocketUiModelBuilder.buildBannedModel()

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            logger = mockLogger,
            playBroadcastWebSocket = fakePlayWebSocket,
        )

        robot.use {
            val observableTimerState = it.getViewModelPrivateField<MutableLiveData<PlayLiveTimerState>>("_observableLiveTimerState")
            observableTimerState.value = PlayLiveTimerState.Active(remainingInMs = 1000)

            robot.executeViewModelPrivateFunction("startWebSocket")
            fakePlayWebSocket.fakeEmitMessage(mockBannedString)

            val eventResult = robot.getViewModel().observableEvent.getOrAwaitValue()

            verify { mockLogger.logSocketType(any()) }
            fakePlayWebSocket.isOpen().assertFalse()
            eventResult.assertEqualTo(mockBanned)
        }
    }

    @Test
    fun `when user received product tag event, it should emit new product tag`() {
        val mockProductTagString = webSocketUiModelBuilder.buildProductTagString()
        val mockProductTag = webSocketUiModelBuilder.buildProductTagModel()

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            logger = mockLogger,
            productMapper = PlayBroProductUiMapper(),
            playBroadcastWebSocket = fakePlayWebSocket,
        )

        robot.use {
            val state = robot.recordState {
                getConfig()
                robot.executeViewModelPrivateFunction("startWebSocket")
                fakePlayWebSocket.fakeEmitMessage(mockProductTagString)
            }

            state.selectedProduct.assertEqualTo(mockProductTag)
        }
    }

    @Test
    fun `when user received new metrics event, it should emit new metrics data`() {
        val mockNewMetricString = webSocketUiModelBuilder.buildNewMetricString()
        val mockNewMetric = webSocketUiModelBuilder.buildNewMetricModelList()

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            logger = mockLogger,
            productMapper = PlayBroProductUiMapper(),
            playBroadcastWebSocket = fakePlayWebSocket,
        )

        robot.use {
            robot.executeViewModelPrivateFunction("startWebSocket")
            fakePlayWebSocket.fakeEmitMessage(mockNewMetricString)

            val result = robot.getViewModel().observableNewMetrics.getOrAwaitValue()

            result.peekContent().assertEqualTo(mockNewMetric)
        }
    }

    @Test
    fun `when user received new channel interactive scheduled event, it should emit scheduled interactive`() {
        val mockInteractiveConfigResponse = interactiveUiModelBuilder.buildInteractiveConfigModel()

        coEvery { mockRepo.getInteractiveConfig() } returns mockInteractiveConfigResponse

        val mockChannelInteractiveString = webSocketUiModelBuilder.buildChannelInteractiveString()

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            logger = mockLogger,
            playBroadcastWebSocket = fakePlayWebSocket,
        )

        robot.use {
            val state = it.recordState {
                getConfig()

                robot.executeViewModelPrivateFunction("startWebSocket")
                fakePlayWebSocket.fakeEmitMessage(mockChannelInteractiveString)
            }

            state.interactive
                .assertType<InteractiveUiModel.Giveaway>()
        }
    }

    @Test
    fun `when user stop livestreaming, then it should close websocket`() {
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            playBroadcastWebSocket = fakePlayWebSocket,
        )

        robot.use {
            it.getViewModel().stopLiveStream(false)
            fakePlayWebSocket.isOpen().assertFalse()
        }
    }

    @Test
    fun `when user receive closed websocket event because of error, it should reconnect websocket again`() {
        val mockSocketCredentialUseCase = mockk<GetSocketCredentialUseCase>(relaxed = true)
        val mockSocketCredential = GetSocketCredentialResponse.SocketCredential()

        coEvery { mockSocketCredentialUseCase.executeOnBackground() } returns mockSocketCredential

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            playBroadcastWebSocket = fakePlayWebSocket,
            getSocketCredentialUseCase = mockSocketCredentialUseCase,
        )

        robot.use {
            fakePlayWebSocket.isOpen().assertFalse()

            robot.executeViewModelPrivateFunction("startWebSocket")
            fakePlayWebSocket.invokeFailure(mockException)

            fakePlayWebSocket.isOpen().assertTrue()
        }
    }
}