package com.tokopedia.play.broadcaster.pinnedmessage

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.robot.PlayBroadcastViewModelRobot
import com.tokopedia.play.broadcaster.util.assertEmpty
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.assertTrue
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PlayBroadcastPinnedMessageTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()
    private val testDispatcher = coroutineTestRule.dispatchers

    private val uiModelBuilder = UiModelBuilder()

    private val mockConfig = uiModelBuilder.buildConfigurationUiModel(
        streamAllowed = true,
        channelId = "123"
    )
    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)

    @Before
    fun setUp() {
        coEvery { mockRepo.getChannelConfiguration() } returns mockConfig
    }

    @Test
    fun `given there is active pinned message, when live stream start, then show correct pinned message`() {
        val message = "message test"
        val mockPinned = uiModelBuilder.buildPinnedMessageUiModel(
            id = "1",
            message = message,
            isActive = true
        )
        coEvery { mockRepo.getActivePinnedMessage(any()) } returns mockPinned

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        robot.use {
            val state = robot.recordState {
                getConfig()
                startLive()
            }

            state.pinnedMessage
                .message
                .assertEqualTo(message)
        }
    }

    @Test
    fun `given there is no active pinned message, when live stream start, then pinned message will be empty`() {
        val message = "message test"
        val mockPinned = uiModelBuilder.buildPinnedMessageUiModel(
            message = message,
            isActive = false
        )
        coEvery { mockRepo.getActivePinnedMessage(any()) } returns mockPinned

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        robot.use {
            val state = robot.recordState {
                getConfig()
                startLive()
            }

            state.pinnedMessage
                .message
                .assertEmpty()
        }
    }

    @Test
    fun `given there is no pinned message, when live stream start, then pinned message will be empty`() {
        coEvery { mockRepo.getActivePinnedMessage(any()) } returns null

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        robot.use {
            val state = robot.recordState {
                getConfig()
                startLive()
            }

            state.pinnedMessage
                .message
                .assertEmpty()
        }
    }

    @Test
    fun `given network error, when set pinned message, then pinned message will not be changed`() {
        val prevPinned = uiModelBuilder.buildPinnedMessageUiModel(id = "1", message = "message 1")
        val newMessage = "abc"
        coEvery { mockRepo.getActivePinnedMessage(any()) } returns prevPinned
        coEvery { mockRepo.setPinnedMessage(any(), any(), any()) } throws IllegalStateException()

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        robot.use {
            val state = robot.recordState {
                getConfig()
                startLive()

                setPinned(newMessage)
            }

            state.pinnedMessage
                .message
                .assertEqualTo(prevPinned.message)
        }
    }

    @Test
    fun `given network is fine, when set pinned message, then pinned message will be changed`() {
        val prevPinned = uiModelBuilder.buildPinnedMessageUiModel(id = "1", message = "message 1")
        val newMessage = "abc"
        coEvery { mockRepo.getActivePinnedMessage(any()) } returns prevPinned
        coEvery { mockRepo.setPinnedMessage(any(), any(), any()) } returns prevPinned.copy(
            message = newMessage
        )

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        robot.use {
            val state = robot.recordState {
                getConfig()
                startLive()

                setPinned(newMessage)
            }

            state.pinnedMessage
                .message
                .assertEqualTo(newMessage)
        }
    }

    @Test
    fun `when edit pinned message, then status should be edit`() {
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        robot.use {
            val state = robot.recordState {
                getConfig()
                startLive()

                editPinned()
            }

            state.pinnedMessage
                .editStatus.isEditing
                .assertTrue()
        }
    }

    @Test
    fun `when cancel edit pinned message, then status should be nothing`() {
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        robot.use {
            val stateList = robot.recordStateAsList {
                getConfig()
                startLive()

                editPinned()
                cancelEditPinned()
            }.takeLast(2)

            stateList.first().pinnedMessage
                .editStatus.isEditing
                .assertTrue()

            stateList.last().pinnedMessage
                .editStatus.isNothing
                .assertTrue()
        }
    }


}