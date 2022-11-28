package com.tokopedia.play.broadcaster.viewmodel.streamer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.domain.model.GetAddedChannelTagsResponse
import com.tokopedia.play.broadcaster.domain.model.GetChannelResponse
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.domain.usecase.GetAddedChannelTagsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetChannelUseCase
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.pusher.state.PlayBroadcasterState
import com.tokopedia.play.broadcaster.pusher.timer.PlayBroadcastTimer
import com.tokopedia.play.broadcaster.robot.PlayBroadcastViewModelRobot
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastEvent
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.assertTrue
import com.tokopedia.play.broadcaster.util.assertType
import com.tokopedia.play_common.types.PlayChannelStatusType
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by meyta.taliti on 26/09/22.
 */
class PlayBroadcastStreamerTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)
    private val mockGetChannelUseCase: GetChannelUseCase = mockk(relaxed = true)
    private val mockBroadcastTimer: PlayBroadcastTimer = mockk(relaxed = true)
    private val mockGetAddedTagUseCase: GetAddedChannelTagsUseCase = mockk(relaxed = true)

    private val uiModelBuilder = UiModelBuilder()
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
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfig
        coEvery { mockGetChannelUseCase.executeOnBackground() } returns mockChannel
        coEvery { mockGetAddedTagUseCase.executeOnBackground() } returns mockAddedTag
    }

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

    @Test
    fun `when seller pause live, then it should trigger updateChannelStatus to Pause`() {
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
        )

        val mock = spyk(robot.getViewModel(), recordPrivateCalls = true)

        robot.use {
            mock.submitAction(
                PlayBroadcastAction.BroadcastStateChanged(
                    PlayBroadcasterState.Paused
                )
            )

            verify { mock invoke "updateChannelStatus" withArguments listOf(PlayChannelStatusType.Pause) }
        }
    }

    @Test
    fun `when seller stop live, then it should trigger updateChannelStatus to Stop`() {
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
        )

        val mock = spyk(robot.getViewModel(), recordPrivateCalls = true)

        robot.use {
            mock.submitAction(
                PlayBroadcastAction.BroadcastStateChanged(
                    PlayBroadcasterState.Stopped
                )
            )

            verify { mock invoke "updateChannelStatus" withArguments listOf(PlayChannelStatusType.Stop) }
        }
    }

    @Test
    fun `when seller stop live, then it should trigger closeWebSocket`() {
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
        )

        val mock = spyk(robot.getViewModel(), recordPrivateCalls = true)

        robot.use {
            mock.submitAction(
                PlayBroadcastAction.BroadcastStateChanged(
                    PlayBroadcasterState.Stopped
                )
            )

            verify { mock invokeNoArgs "closeWebSocket" }
        }
    }
}
