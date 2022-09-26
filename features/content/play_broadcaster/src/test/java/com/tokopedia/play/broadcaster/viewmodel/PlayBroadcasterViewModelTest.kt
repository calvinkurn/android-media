package com.tokopedia.play.broadcaster.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.domain.model.GetAddedChannelTagsResponse
import com.tokopedia.play.broadcaster.domain.model.GetChannelResponse
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.domain.usecase.GetAddedChannelTagsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetChannelUseCase
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.model.setup.product.ProductSetupUiModelBuilder
import com.tokopedia.play.broadcaster.pusher.timer.PlayBroadcastTimer
import com.tokopedia.play.broadcaster.robot.PlayBroadcastViewModelRobot
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.model.ChannelStatus
import com.tokopedia.play.broadcaster.util.assertEmpty
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.assertTrue
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
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
    private val mockGetChannelUseCase: GetChannelUseCase = mockk(relaxed = true)
    private val mockGetAddedTagUseCase: GetAddedChannelTagsUseCase = mockk(relaxed = true)
    private val mockUserSessionInterface: UserSessionInterface = mockk(relaxed = true)

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
    fun `given seller allowed to stream, and channelStatus Unknown, then it should trigger createChannel()`() {
        val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)

        val mockConfig = uiModelBuilder.buildConfigurationUiModel(
            streamAllowed = true,
            channelStatus = ChannelStatus.Unknown
        )
        coEvery { mockRepo.getChannelConfiguration() } returns mockConfig

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        val mock = spyk(robot.getViewModel(), recordPrivateCalls = true)

        robot.use {
            mock.getConfiguration()

            verify { mock invokeNoArgs "createChannel" }
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

    //region usersession
    /**
     * User Session
     */
    @Test
    fun `given user session logged in true, then it should return true`() {
        every { mockUserSessionInterface.isLoggedIn } returns true

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            userSession = mockUserSessionInterface,
        )

        robot.use {
            it.getViewModel().isUserLoggedIn.assertTrue()
        }
    }

    @Test
    fun `given user session shop avatar is empty, then it should return empty`() {
        every { mockUserSessionInterface.shopAvatar } returns ""

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            userSession = mockUserSessionInterface,
        )

        robot.use {
            it.getViewModel().getShopIconUrl().assertEmpty()
        }
    }

    @Test
    fun `given user session shop name is empty, then it should return empty`() {
        every { mockUserSessionInterface.shopName } returns ""

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            userSession = mockUserSessionInterface,
        )

        robot.use {
            it.getViewModel().getShopName().assertEmpty()
        }
    }
    //endregion

    //region timer
    /**
     * Timer
     */
    @Test
    fun `when trigger startTimer(), then it will trigger broadcasterTimer start`() {
        val mockTimer: PlayBroadcastTimer = mockk(relaxed = true)

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            broadcastTimer = mockTimer,
        )

        robot.use {
            it.getViewModel().startTimer()

            verify { mockTimer.start() }
        }
    }

    @Test
    fun `when trigger stopTimer(), then it will trigger broadcasterTimer stop`() {
        val mockTimer: PlayBroadcastTimer = mockk(relaxed = true)

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            broadcastTimer = mockTimer,
        )

        robot.use {
            it.getViewModel().stopTimer()

            verify { mockTimer.stop() }
        }
    }
    //endregion
}
