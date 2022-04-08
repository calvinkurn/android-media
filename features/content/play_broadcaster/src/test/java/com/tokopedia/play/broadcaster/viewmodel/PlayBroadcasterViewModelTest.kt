package com.tokopedia.play.broadcaster.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.broadcaster.widget.SurfaceAspectRatioView
import com.tokopedia.play.broadcaster.domain.model.GetAddedChannelTagsResponse
import com.tokopedia.play.broadcaster.domain.model.GetChannelResponse
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastChannelRepository
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.domain.usecase.GetAddedChannelTagsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetChannelUseCase
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.model.setup.product.ProductSetupUiModelBuilder
import com.tokopedia.play.broadcaster.pusher.mediator.PusherMediator
import com.tokopedia.play.broadcaster.robot.PlayBroadcastViewModelRobot
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastEvent
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMockMapper
import com.tokopedia.play.broadcaster.ui.model.ChannelInfoUiModel
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play_common.types.PlayChannelStatusType
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on October 18, 2021
 */
class PlayBroadcasterViewModelTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)
    private val mockLivePusherMediator: PusherMediator = mockk(relaxed = true)
    private val mockSharedPref: HydraSharedPreferences = mockk(relaxed = true)
    private val mockGetChannelUseCase: GetChannelUseCase = mockk(relaxed = true)
    private val mockGetAddedTagUseCase: GetAddedChannelTagsUseCase = mockk(relaxed = true)
    private val mockMapper: PlayBroadcastMapper = mockk(relaxed = true)

    private val uiModelBuilder = UiModelBuilder()
    private val productSetupUiModelBuilder = ProductSetupUiModelBuilder()

    private val mockConfig = uiModelBuilder.buildConfigurationUiModel(
        streamAllowed = true,
        channelId = "123"
    )
    private val mockChannel = GetChannelResponse.Channel(
        basic = GetChannelResponse.ChannelBasic(
            coverUrl = "https://tokopedia.com"
        )
    )
    private val mockAddedTag = GetAddedChannelTagsResponse()

    @Before
    fun setUp() {
        coEvery { mockRepo.getChannelConfiguration() } returns mockConfig
        coEvery { mockGetChannelUseCase.executeOnBackground() } returns mockChannel
        coEvery { mockGetAddedTagUseCase.executeOnBackground() } returns mockAddedTag
    }

    @Test
    fun `given config info, when get live countdown duration, it should return duration stated on config info`() {
        val countDown = 5
        val configMock = uiModelBuilder.buildConfigurationUiModel(countDown = countDown.toLong())

        val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)
        coEvery { mockRepo.getChannelConfiguration() } returns configMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            it.getConfig()
            it.getViewModel().getBeforeLiveCountDownDuration().assertEqualTo(countDown)
        }
    }

    @Test
    fun `given no config info, when get live countdown duration, it should return default duration`() {
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher
        )

        robot.use {
            val defaultCountDown = it.getViewModelPrivateField<Int>("DEFAULT_BEFORE_LIVE_COUNT_DOWN")
            it.getViewModel().getBeforeLiveCountDownDuration().assertEqualTo(defaultCountDown)
        }
    }

    @Test
    fun `when user submit set product action, it should emit new product section list state`() {
        val mockProductTagSection = productSetupUiModelBuilder.buildProductTagSectionList()

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        robot.use {
            val state = robot.recordState {
                getConfig()
                getViewModel().submitAction(PlayBroadcastAction.SetProduct(mockProductTagSection))
            }

            state.selectedProduct.assertEqualTo(mockProductTagSection)
        }
    }

    @Test
    fun `when user stop livestreaming and some error occur, it should emit error event`() {
        val mockWebsocket = mockk<PlayWebSocket>(relaxed = true)
        val mockException = uiModelBuilder.buildException()

        coEvery { mockWebsocket.close() } throws mockException

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            playBroadcastWebSocket = mockWebsocket,
        )

        robot.use {
            val event = robot.recordEvent {
                it.getViewModel().stopLiveStream()
            }

            event.last().assertEqualTo(PlayBroadcastEvent.ShowError(mockException))
        }
    }

    @Test
    fun `when user wants to switch camera, it should trigger live pusher mediator switch camera`() {

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            livePusherMediator = mockLivePusherMediator,
            channelRepo = mockRepo,
        )

        robot.use {
            robot.getViewModel().switchCamera()

            verify { mockLivePusherMediator.switchCamera() }
        }
    }

    @Test
    fun `when user wants to start preview, it should trigger live pusher mediator switch on camera changed`() {

        val mockSurfaceView = mockk<SurfaceAspectRatioView>(relaxed = true)

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            livePusherMediator = mockLivePusherMediator,
            channelRepo = mockRepo,
        )

        robot.use {
            robot.getViewModel().startPreview(mockSurfaceView)

            verify { mockLivePusherMediator.onCameraChanged(mockSurfaceView) }
        }
    }

    @Test
    fun `when user wants to stop preview, it should trigger live pusher mediator switch on camera destroyed`() {

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            livePusherMediator = mockLivePusherMediator,
            channelRepo = mockRepo,
        )

        robot.use {
            robot.getViewModel().stopPreview()

            verify { mockLivePusherMediator.onCameraDestroyed() }
        }
    }

    @Test
    fun `when user start livestreaming for the first time, it should trigger shared pref and set not first stream anymore`() {

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            sharedPref = mockSharedPref,
            channelRepo = mockRepo,
        )

        robot.use {
            robot.getViewModel().setFirstTimeLiveStreaming()

            verify { mockSharedPref.setNotFirstStreaming() }
        }
    }

    @Test
    fun `when user wants to continue livestream when theres no livestraming, it should trigger start live stream function`() {

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            playBroadcastMapper = mockMapper,
            livePusherMediator = mockLivePusherMediator,
            channelRepo = mockRepo,
        )

        robot.use {
            it.getViewModel().continueLiveStream()

            verify { mockLivePusherMediator.startLiveStreaming("", true) }
        }
    }

    @Test
    fun `when user wants to reconnect livestream, it should resume the livestream`() {

        val mockChannelInfo = ChannelInfoUiModel(
            channelId = "",
            title = "",
            description = "",
            coverUrl = "",
            ingestUrl = "",
            status = PlayChannelStatusType.Pause
        )

        coEvery { mockMapper.mapChannelInfo(any()) } returns mockChannelInfo

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            playBroadcastMapper = mockMapper,
            livePusherMediator = mockLivePusherMediator,
            channelRepo = mockRepo,
        )

        robot.use {
            it.getViewModel().reconnectLiveStream()

            verify { mockLivePusherMediator.resume() }
        }
    }

    @Test
    fun `when user tries to reconnect livestream but theres no live channel, it should trigget stop livestream`() {

        val mockChannelInfo = ChannelInfoUiModel(
            channelId = "",
            title = "",
            description = "",
            coverUrl = "",
            ingestUrl = "",
            status = PlayChannelStatusType.Draft
        )

        coEvery { mockMapper.mapChannelInfo(any()) } returns mockChannelInfo

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            playBroadcastMapper = mockMapper,
            livePusherMediator = mockLivePusherMediator,
            channelRepo = mockRepo,
        )

        robot.use {
            it.getViewModel().reconnectLiveStream()

            verify { mockLivePusherMediator.stopLiveStreaming() }
        }
    }
}