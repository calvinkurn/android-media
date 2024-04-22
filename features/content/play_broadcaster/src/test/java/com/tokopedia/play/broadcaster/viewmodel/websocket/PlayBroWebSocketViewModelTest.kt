package com.tokopedia.play.broadcaster.viewmodel.websocket

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.content.product.picker.seller.util.PriceFormatUtil
import com.tokopedia.play.broadcaster.domain.model.GetSocketCredentialResponse
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.domain.usecase.GetSocketCredentialUseCase
import com.tokopedia.play.broadcaster.fake.FakePlayWebSocket
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.model.interactive.InteractiveUiModelBuilder
import com.tokopedia.play.broadcaster.model.websocket.WebSocketUiModelBuilder
import com.tokopedia.play.broadcaster.robot.PlayBroadcastViewModelRobot
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastEvent
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroProductUiMapper
import com.tokopedia.play.broadcaster.ui.model.report.live.LiveStatsUiModel
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.assertEvent
import com.tokopedia.play.broadcaster.util.assertFalse
import com.tokopedia.play.broadcaster.util.assertTrue
import com.tokopedia.play.broadcaster.util.getOrAwaitValue
import com.tokopedia.play.broadcaster.util.logger.PlayLogger
import com.tokopedia.play_common.model.dto.interactive.GameUiModel
import com.tokopedia.play_common.model.mapper.PlayInteractiveMapper
import com.tokopedia.play_common.model.ui.LeadeboardType
import com.tokopedia.play_common.model.ui.LeaderboardGameUiModel
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions
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
    private val mockInteractiveMapper: PlayInteractiveMapper = mockk(relaxed = true)
    private val fakePlayWebSocket = FakePlayWebSocket(testDispatcher)

    private val productUiMapper: PlayBroProductUiMapper = PlayBroProductUiMapper(PriceFormatUtil())

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
        coEvery { mockRepo.getAccountList() } returns uiModelBuilder.buildAccountListModel()
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfig
        coEvery { mockRepo.getBroadcastingConfig(any(), any()) } returns uiModelBuilder.buildBroadcastingConfigUiModel()
    }

    @Test
    fun `when user received new total view event, then it should emit new total view data`() {
        val mockTotalView = "123"
        val mockTotalViewString = webSocketUiModelBuilder.buildReportChannelString(totalViewFmt = mockTotalView)

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            playBroadcastWebSocket = fakePlayWebSocket,
        )

        robot.use {
            val state = robot.recordState {
                getAccountConfiguration()
                robot.executeViewModelPrivateFunction("startWebSocket")
                fakePlayWebSocket.fakeEmitMessage(mockTotalViewString)
            }

            state.liveReportSummary.liveStats
                .filterIsInstance<LiveStatsUiModel.TotalViewer>()
                .first()
                .value
                .assertEqualTo(mockTotalView)
        }
    }

    @Test
    fun `when user received new total like event, then it should emit new total like data`() {
        val mockTotalLike = "123"
        val mockTotalLikeString = webSocketUiModelBuilder.buildReportChannelString(totalLikeFmt = mockTotalLike)

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            playBroadcastWebSocket = fakePlayWebSocket,
        )

        robot.use {
            val state = robot.recordState {
                getAccountConfiguration()
                robot.executeViewModelPrivateFunction("startWebSocket")
                fakePlayWebSocket.fakeEmitMessage(mockTotalLikeString)
            }

            state.liveReportSummary.liveStats
                .filterIsInstance<LiveStatsUiModel.Like>()
                .first()
                .value
                .assertEqualTo(mockTotalLike)
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
            val resultNewChat = robot.getViewModel().observableNewChat.getOrAwaitValue()

            result.assertEqualTo(listOf(mockChat))
            resultNewChat.peekContent().assertEqualTo(result.last())
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
                getAccountConfiguration()
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
            robot.executeViewModelPrivateFunction("startWebSocket")
            fakePlayWebSocket.fakeEmitMessage(mockFreezeString)
            it.stopLive()

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
            robot.executeViewModelPrivateFunction("startWebSocket")
            fakePlayWebSocket.fakeEmitMessage(mockBannedString)
            it.stopLive()

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
            productMapper = productUiMapper,
            playBroadcastWebSocket = fakePlayWebSocket,
        )

        robot.use {
            val state = robot.recordState {
                getAccountConfiguration()
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
            productMapper = productUiMapper,
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
    fun `when user received new channel interactive with unknown status, it should stop interactive`() {
        val mockInteractiveConfigResponse = interactiveUiModelBuilder.buildInteractiveConfigModel()

        val mockChannelInteractiveString = webSocketUiModelBuilder.buildChannelInteractiveString(
            status = 123
        )

        coEvery { mockRepo.getInteractiveConfig(any(), any()) } returns mockInteractiveConfigResponse

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            logger = mockLogger,
            playBroadcastWebSocket = fakePlayWebSocket,
            playInteractiveMapper = PlayInteractiveMapper(mockk(relaxed = true)),
        )

        robot.use {
            val state = it.recordState {
                getAccountConfiguration()
                this.executeViewModelPrivateFunction("startWebSocket")
                fakePlayWebSocket.fakeEmitMessage(mockChannelInteractiveString)
            }

            state.game.assertEqualTo(GameUiModel.Unknown)
        }
    }

    @Test
    fun `when user received new channel interactive with finished status, it should emit event to show finished state`() {
        val mockInteractiveConfigResponse = interactiveUiModelBuilder.buildInteractiveConfigModel()
        val mockChannelInteractiveString = webSocketUiModelBuilder.buildChannelInteractiveString(
            status = 123
        )

        coEvery { mockRepo.getInteractiveConfig(any(), any()) } returns mockInteractiveConfigResponse
        coEvery { mockRepo.getSellerLeaderboardWithSlot(any(), any()) } returns interactiveUiModelBuilder.buildLeaderboardInfoModel(listOf(
            LeaderboardGameUiModel.Header(leaderBoardType = LeadeboardType.Quiz, id = "11", title = "Hehe"))
        )

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            logger = mockLogger,
            playBroadcastWebSocket = fakePlayWebSocket,
            playInteractiveMapper = PlayInteractiveMapper(mockk(relaxed = true)),
        )

        robot.use {
            val events = it.recordEvent {
                getAccountConfiguration()
                this.executeViewModelPrivateFunction("startWebSocket")
                fakePlayWebSocket.fakeEmitMessage(mockChannelInteractiveString)
            }

            Assertions.assertThat(events[1]).isInstanceOf(PlayBroadcastEvent.ShowInteractiveGameResultWidget::class.java) //first always init brodcast config
            Assertions.assertThat(events.last()).isInstanceOf(PlayBroadcastEvent.DismissGameResultCoachMark::class.java)
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
            it.stopLive()
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

    @Test
    fun `when user received unknown type quiz web socket event it should return unknown type interactive ui model`() {
        val mockUnknownQuizString = webSocketUiModelBuilder.buildUnknownTypeChannelQuizString()
        val mockSocketCredentialUseCase = mockk<GetSocketCredentialUseCase>(relaxed = true)
        val mockSocketCredential = GetSocketCredentialResponse.SocketCredential()

        coEvery { mockSocketCredentialUseCase.executeOnBackground() } returns mockSocketCredential
        coEvery { mockRepo.getSellerLeaderboardWithSlot(any(), any()) } returns emptyList()
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            playBroadcastWebSocket = fakePlayWebSocket,
            getSocketCredentialUseCase = mockSocketCredentialUseCase,
        )

        robot.use {
            val state = robot.recordState {
                getAccountConfiguration()
                this.executeViewModelPrivateFunction("startWebSocket")
                fakePlayWebSocket.fakeEmitMessage(mockUnknownQuizString)
            }
            Assertions.assertThat(state.game)
                .isInstanceOf(GameUiModel.Unknown::class.java)
        }
    }
}
