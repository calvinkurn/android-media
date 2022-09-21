package com.tokopedia.play.broadcaster.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.broadcaster.revamp.util.statistic.BroadcasterMetric
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_SHOP
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_USER
import com.tokopedia.content.common.ui.model.AccountStateInfoType
import com.tokopedia.play.broadcaster.domain.model.GetAddedChannelTagsResponse
import com.tokopedia.play.broadcaster.domain.model.GetChannelResponse
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.domain.usecase.GetAddedChannelTagsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetChannelUseCase
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.model.setup.product.ProductSetupUiModelBuilder
import com.tokopedia.play.broadcaster.pusher.state.PlayBroadcasterState
import com.tokopedia.play.broadcaster.pusher.statistic.PlayBroadcasterMetric
import com.tokopedia.play.broadcaster.pusher.timer.PlayBroadcastTimer
import com.tokopedia.play.broadcaster.robot.PlayBroadcastViewModelRobot
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastEvent
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroProductUiMapper
import com.tokopedia.play.broadcaster.ui.model.ChannelStatus
import com.tokopedia.play.broadcaster.util.assertEmpty
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.assertTrue
import com.tokopedia.play.broadcaster.util.assertType
import com.tokopedia.play.broadcaster.util.error.DefaultErrorThrowable
import com.tokopedia.play.broadcaster.util.logger.PlayLogger
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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
    private val mockBroadcastTimer: PlayBroadcastTimer = mockk(relaxed = true)
    private val mockUserSessionInterface: UserSessionInterface = mockk(relaxed = true)
    private val mockHydraSharedPreferences: HydraSharedPreferences = mockk(relaxed = true)

    private val productSetupUiModelBuilder = ProductSetupUiModelBuilder()
    private val uiModelBuilder = UiModelBuilder()

    private val mockChannel = GetChannelResponse.Channel(
        basic = GetChannelResponse.ChannelBasic(
            coverUrl = "https://tokopedia.com"
        )
    )
    private val mockAddedTag = GetAddedChannelTagsResponse()
    private val mockProductTagSectionList = productSetupUiModelBuilder.buildProductTagSectionList()

    @Before
    fun setUp() {
        coEvery { mockGetChannelUseCase.executeOnBackground() } returns mockChannel
        coEvery { mockGetAddedTagUseCase.executeOnBackground() } returns mockAddedTag
        coEvery { mockRepo.getProductTagSummarySection(any()) } returns mockProductTagSectionList
    }

    @Test
    fun `given config info, when get live countdown duration, it should return duration stated on config info`() {
        val countDown = 5
        val configMock = uiModelBuilder.buildConfigurationUiModel(countDown = countDown.toLong())

        coEvery { mockRepo.getAccountList() } returns uiModelBuilder.buildAccountListModel()
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
        )

        robot.use {
            it.getAccountConfiguration()
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
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel()

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
        )

        robot.use {
            val state = robot.recordState {
                getAccountConfiguration()
                getViewModel().submitAction(PlayBroadcastAction.SetProduct(mockProductTagSection))
            }

            state.selectedProduct.assertEqualTo(mockProductTagSection)
        }
    }

    /**
     * Live Streaming State
     */
    @Test
    fun `when user trigger resume livestream from onResume Fragment, then it should show resume dialog`() {
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
        )

        robot.use {
            val events = robot.recordEvent {
                it.getViewModel().submitAction(
                    PlayBroadcastAction.BroadcastStateChanged(
                        PlayBroadcasterState.Resume(startedBefore = false, shouldContinue = false)
                    )
                )
            }

            events
                .last()
                .assertEqualTo(PlayBroadcastEvent.ShowResumeLiveDialog)
        }
    }

    @Test
    fun `when user trigger resume livestream and there is a livestream started before, and it should continue broadcast, then its ready to start broadcast`() {
        val mockChannel = mockChannel.copy(
            basic = mockChannel.basic.copy(
                status = GetChannelResponse.ChannelBasicStatus(
                    id = "3", // channel status pause
                )
            )
        )

        coEvery { mockGetChannelUseCase.executeOnBackground() } returns mockChannel

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
        )

        robot.use {
            val events = robot.recordEvent {
                it.getViewModel().submitAction(
                    PlayBroadcastAction.BroadcastStateChanged(
                        PlayBroadcasterState.Resume(startedBefore = true, shouldContinue = true)
                    )
                )
            }

            events
                .last()
                .assertEqualTo(PlayBroadcastEvent.BroadcastReady(""))
        }
    }

    @Test
    fun `when user trigger resume livestream and there is a livestream started before, but it should not continue livestream, then it should show resume dialog`() {
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
        )

        robot.use {
            val events = robot.recordEvent {
                it.getViewModel().submitAction(
                    PlayBroadcastAction.BroadcastStateChanged(
                        PlayBroadcasterState.Resume(startedBefore = true, shouldContinue = false)
                    )
                )
            }

            events
                .last()
                .assertEqualTo(PlayBroadcastEvent.ShowResumeLiveDialog)
        }
    }

    @Test
    fun `when user trigger resume livestream and there is a livestream started before, and its before past pause duration, then it should show resume dialog`() {
        every { mockBroadcastTimer.isPastPauseDuration } returns false

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            broadcastTimer = mockBroadcastTimer,
        )

        robot.use {
            val events = robot.recordEvent {
                it.getViewModel().submitAction(
                    PlayBroadcastAction.BroadcastStateChanged(
                        PlayBroadcasterState.Resume(startedBefore = true, shouldContinue = false)
                    )
                )
            }

            events
                .last()
                .assertEqualTo(PlayBroadcastEvent.ShowResumeLiveDialog)
        }
    }

    @Test
    fun `when user trigger resume livestream and there is a livestream started before but its already past pause duration, then it should show live ended dialog`() {
        every { mockBroadcastTimer.isPastPauseDuration } returns true

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            broadcastTimer = mockBroadcastTimer,
        )

        robot.use {
            val events = robot.recordEvent {
                it.getViewModel().submitAction(
                    PlayBroadcastAction.BroadcastStateChanged(
                        PlayBroadcasterState.Resume(startedBefore = true, shouldContinue = true)
                    )
                )

            }

            events
                .last()
                .assertEqualTo(PlayBroadcastEvent.ShowLiveEndedDialog)
        }
    }

    @Test
    fun `when user trigger resume livestream and there is no livestream started before, and its before past pause duration, given channel status pause, then its ready to start broadcast`() {
        val mockChannel = mockChannel.copy(
            basic = mockChannel.basic.copy(
                status = GetChannelResponse.ChannelBasicStatus(
                    id = "3", // channel status pause
                )
            )
        )

        coEvery { mockGetChannelUseCase.executeOnBackground() } returns mockChannel
        every { mockBroadcastTimer.isPastPauseDuration } returns false

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            broadcastTimer = mockBroadcastTimer,
        )

        robot.use {
            val events = robot.recordEvent {
                it.getViewModel().submitAction(
                    PlayBroadcastAction.BroadcastStateChanged(
                        PlayBroadcasterState.Resume(startedBefore = false, shouldContinue = true)
                    )
                )
            }

            events
                .last()
                .assertEqualTo(PlayBroadcastEvent.BroadcastReady(""))
        }
    }

    @Test
    fun `when user trigger resume livestream and there is no livestream started before, but its already past pause duration, given channel status pause, then its ready to start broadcast`() {
        val mockChannel = mockChannel.copy(
            basic = mockChannel.basic.copy(
                status = GetChannelResponse.ChannelBasicStatus(
                    id = "3", // channel status pause
                )
            )
        )

        coEvery { mockGetChannelUseCase.executeOnBackground() } returns mockChannel
        every { mockBroadcastTimer.isPastPauseDuration } returns false

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            broadcastTimer = mockBroadcastTimer,
        )

        robot.use {
            val events = robot.recordEvent {
                it.getViewModel().submitAction(
                    PlayBroadcastAction.BroadcastStateChanged(
                        PlayBroadcasterState.Resume(startedBefore = false, shouldContinue = true)
                    )
                )

            }

            events
                .last()
                .assertEqualTo(PlayBroadcastEvent.BroadcastReady(""))
        }
    }

    @Test
    fun `when user resume livestream, given channel status pause, and it should not continue live stream, then it should show resume dialog`() {
        val mockChannel = mockChannel.copy(
            basic = mockChannel.basic.copy(
                status = GetChannelResponse.ChannelBasicStatus(
                    id = "3", // channel status pause
                )
            )
        )

        coEvery { mockGetChannelUseCase.executeOnBackground() } returns mockChannel

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
        )

        robot.use {
            val events = robot.recordEvent {
                it.getViewModel().doResumeBroadcaster(shouldContinue = false)
            }

            events
                .last()
                .assertEqualTo(PlayBroadcastEvent.ShowResumeLiveDialog)
        }
    }


    @Test
    fun `when user resume livestream, given channel status live, and it should not continue live stream, then it should show resume dialog`() {
        val mockChannel = mockChannel.copy(
            basic = mockChannel.basic.copy(
                status = GetChannelResponse.ChannelBasicStatus(
                    id = "2", // channel status live
                )
            )
        )

        coEvery { mockGetChannelUseCase.executeOnBackground() } returns mockChannel

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
        )

        robot.use {
            val events = robot.recordEvent {
                it.getViewModel().doResumeBroadcaster(shouldContinue = false)
            }

            events
                .last()
                .assertEqualTo(PlayBroadcastEvent.ShowResumeLiveDialog)
        }
    }

    @Test
    fun `when user resume livestream, given channel status stop, then it should show live ended dialog`() {
        val mockChannel = mockChannel.copy(
            basic = mockChannel.basic.copy(
                status = GetChannelResponse.ChannelBasicStatus(
                    id = "4", // channel status stop
                )
            )
        )

        coEvery { mockGetChannelUseCase.executeOnBackground() } returns mockChannel

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
        )

        robot.use {
            val events = robot.recordEvent {
                it.getViewModel().doResumeBroadcaster(shouldContinue = false)
            }

            events
                .last()
                .assertEqualTo(PlayBroadcastEvent.ShowLiveEndedDialog)
        }
    }

    @Test
    fun `when user resume livestream, given channel error, then it should show error`() {

        val errorThrowable = DefaultErrorThrowable()

        coEvery { mockGetChannelUseCase.executeOnBackground() } throws errorThrowable

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
        )

        robot.use {
            val event = robot.recordEvent {
                it.getViewModel().doResumeBroadcaster(true)
            }

            event
                .last()
                .assertType<PlayBroadcastEvent.ShowError>()
        }
    }

    @Test
    fun `when broadcaster error, then it should show broadcast error`() {
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
        )

        val errorThrowable = DefaultErrorThrowable()

        robot.use {
            val event = robot.recordEvent {
                it.getViewModel().submitAction(
                    PlayBroadcastAction.BroadcastStateChanged(
                        PlayBroadcasterState.Error(errorThrowable)
                    )
                )
            }

            event
                .last()
                .assertEqualTo(PlayBroadcastEvent.ShowBroadcastError(errorThrowable))
        }
    }

    @Test
    fun `when broadcaster is recovered, given channel status pause, then it should notify ui that broadcaster is recovered`() {
        val mockChannel = mockChannel.copy(
            basic = mockChannel.basic.copy(
                status = GetChannelResponse.ChannelBasicStatus(
                    id = "3",
                )
            )
        )

        coEvery { mockGetChannelUseCase.executeOnBackground() } returns mockChannel

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
        )

        robot.use {
            val event = robot.recordEvent {
                it.getViewModel().submitAction(
                    PlayBroadcastAction.BroadcastStateChanged(
                        PlayBroadcasterState.Recovered
                    )
                )
            }

            event
                .last()
                .assertEqualTo(PlayBroadcastEvent.BroadcastRecovered)
        }
    }

    @Test
    fun `when broadcaster is recovered, given channel status live, then it should notify ui that broadcaster is recovered`() {
        val mockChannel = mockChannel.copy(
            basic = mockChannel.basic.copy(
                status = GetChannelResponse.ChannelBasicStatus(
                    id = "2",
                )
            )
        )

        coEvery { mockGetChannelUseCase.executeOnBackground() } returns mockChannel

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
        )

        robot.use {
            val event = robot.recordEvent {
                it.getViewModel().submitAction(
                    PlayBroadcastAction.BroadcastStateChanged(
                        PlayBroadcasterState.Recovered
                    )
                )
            }

            event
                .last()
                .assertEqualTo(PlayBroadcastEvent.BroadcastRecovered)
        }
    }

    @Test
    fun `when broadcaster is recovered, given channel status stop, then it should show live ended dialog`() {
        val mockChannel = mockChannel.copy(
            basic = mockChannel.basic.copy(
                status = GetChannelResponse.ChannelBasicStatus(
                    id = "4",
                )
            )
        )

        coEvery { mockGetChannelUseCase.executeOnBackground() } returns mockChannel

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
        )

        robot.use {
            val event = robot.recordEvent {
                it.getViewModel().submitAction(
                    PlayBroadcastAction.BroadcastStateChanged(
                        PlayBroadcasterState.Recovered
                    )
                )
            }

            event
                .last()
                .assertEqualTo(PlayBroadcastEvent.ShowLiveEndedDialog)
        }
    }

    @Test
    fun `when broadcaster is recovered, given channel error, then it should show error`() {
        val errorThrowable = DefaultErrorThrowable()

        coEvery { mockGetChannelUseCase.executeOnBackground() } throws errorThrowable

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
        )

        robot.use {
            val event = robot.recordEvent {
                it.getViewModel().submitAction(
                    PlayBroadcastAction.BroadcastStateChanged(
                        PlayBroadcasterState.Recovered
                    )
                )
            }

            event
                .last()
                .assertType<PlayBroadcastEvent.ShowError>()
        }
    }

    @Test
    fun `when seller stop live streaming, then isBroadcastStopped should return true`() {
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
        )

        robot.use {
            it.stopLive()

            it.getViewModel().isBroadcastStopped.assertTrue()
        }
    }

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

    /**
     * Logger
     */
    @Test
    fun `when send broadcaster metrics, then it should call logger sendBroadcasterLog`() {
        val mockMetric = BroadcasterMetric.Empty
        val mockLogger: PlayLogger = mockk(relaxed = true)

        val mappedMetric = PlayBroadcasterMetric(
            authorId = "",
            channelId = "",
            videoBitrate = mockMetric.videoBitrate,
            audioBitrate = mockMetric.audioBitrate,
            resolution = "${mockMetric.resolutionWidth}x${mockMetric.resolutionHeight}",
            traffic = mockMetric.traffic,
            bandwidth = mockMetric.bandwidth,
            fps = mockMetric.fps,
            packetLossIncreased = mockMetric.packetLossIncreased,
            videoBufferTimestamp = mockMetric.videoBufferTimestamp,
            audioBufferTimestamp = mockMetric.audioBufferTimestamp,
        )

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            logger = mockLogger,
        )

        robot.use {
            it.getViewModel().sendBroadcasterLog(mockMetric)

            verify { mockLogger.sendBroadcasterLog(mappedMetric) }
        }
    }

    @Test
    fun `when send all logs, then it should call logger sendAll`() {
        val mockLogger: PlayLogger = mockk(relaxed = true)

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            logger = mockLogger,
        )

        robot.use {
            it.getViewModel().sendLogs()

            verify { mockLogger.sendAll("") }
        }
    }

    @Test
    fun `when user only have shop and eligible then selected account is shop`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel(onlyShop = true)

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
        )

        robot.use {
            val state = robot.recordState {
                it.getAccountConfiguration()
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_SHOP)
        }
    }

    @Test
    fun `when user only have shop and not eligible then selected account is shop with info`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel(tncShop = false, onlyShop = true)

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
        )

        robot.use {
            val state = robot.recordState {
                it.getAccountConfiguration()
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_SHOP)
            state.accountStateInfo.type.assertEqualTo(AccountStateInfoType.NotAcceptTNC)
            state.accountStateInfo.selectedAccount.type.assertEqualTo(TYPE_SHOP)
        }
    }

    @Test
    fun `when user only have buyer and eligible then selected account is buyer`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel(onlyBuyer = true)

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
        )

        robot.use {
            val state = robot.recordState {
                it.getAccountConfiguration()
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_USER)
        }
    }

    @Test
    fun `when user only have buyer and not eligible then selected account is buyer with info`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel(tncBuyer = false, onlyBuyer = true)

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
        )

        robot.use {
            val state = robot.recordState {
                it.getAccountConfiguration()
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_USER)
            state.accountStateInfo.type.assertEqualTo(AccountStateInfoType.NotAcceptTNC)
            state.accountStateInfo.selectedAccount.type.assertEqualTo(TYPE_USER)
        }
    }

    @Test
    fun `when user have already switch account before then selected account is from preferences`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel()

        coEvery { mockHydraSharedPreferences.getLastSelectedAccount() } returns TYPE_USER
        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            sharedPref = mockHydraSharedPreferences,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
        )

        robot.use {
            val state = robot.recordState {
                it.getAccountConfiguration()
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_USER)
        }
    }

    @Test
    fun `when selected account is from preferences but selected account is not eligible then switch to other`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel(tncShop = false)

        coEvery { mockHydraSharedPreferences.getLastSelectedAccount() } returns TYPE_SHOP
        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            sharedPref = mockHydraSharedPreferences,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
        )

        robot.use {
            val state = robot.recordState {
                it.getAccountConfiguration()
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_USER)
        }
    }

    @Test
    fun `when shop account eligible then selected account is shop`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel()

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
        )

        robot.use {
            val state = robot.recordState {
                it.getAccountConfiguration()
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_SHOP)
        }
    }

    @Test
    fun `when shop account not eligible but buyer account is eligible then selected account is buyer`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel(tncShop = false)

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
        )

        robot.use {
            val state = robot.recordState {
                it.getAccountConfiguration()
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_USER)
        }
    }

    @Test
    fun `when shop account not eligible and buyer account not eligible then selected account is shop with info`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel(tncShop = false, usernameBuyer = false)

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
        )

        robot.use {
            val state = robot.recordState {
                it.getAccountConfiguration()
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_SHOP)
            state.accountStateInfo.type.assertEqualTo(AccountStateInfoType.NoUsername)
            state.accountStateInfo.selectedAccount.type.assertEqualTo(TYPE_USER)
        }
    }

    @Test
    fun `when shop and buyer account eligible but live stream then selected account is shop with info`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel(channelStatus = ChannelStatus.Live)
        val accountMock = uiModelBuilder.buildAccountListModel()

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
        )

        robot.use {
            val state = robot.recordState {
                it.getAccountConfiguration()
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_SHOP)
            state.accountStateInfo.type.assertEqualTo(AccountStateInfoType.Live)
            state.selectedContentAccount.type.assertEqualTo(TYPE_SHOP)
        }
    }

    @Test
    fun `when shop and buyer account eligible but banned then selected account is shop with info`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel(streamAllowed = false)
        val accountMock = uiModelBuilder.buildAccountListModel()

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
        )

        robot.use {
            val state = robot.recordState {
                it.getAccountConfiguration()
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_SHOP)
            state.accountStateInfo.type.assertEqualTo(AccountStateInfoType.Banned)
            state.selectedContentAccount.type.assertEqualTo(TYPE_SHOP)
        }
    }

}