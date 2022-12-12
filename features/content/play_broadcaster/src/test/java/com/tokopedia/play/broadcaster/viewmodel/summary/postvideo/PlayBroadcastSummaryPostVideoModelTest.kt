package com.tokopedia.play.broadcaster.viewmodel.summary.postvideo

import com.tokopedia.play.broadcaster.domain.model.SetChannelTagsResponse
import com.tokopedia.play.broadcaster.domain.usecase.SetChannelTagsUseCase
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.robot.PlayBroadcastSummaryViewModelRobot
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastSummaryAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastSummaryEvent
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play_common.domain.model.ChannelId
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on March 11, 2022
 */
class PlayBroadcastSummaryPostVideoModelTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val mockSetChannelTagUseCase: SetChannelTagsUseCase = mockk(relaxed = true)
    private val mockUpdateChannelUseCase: PlayBroadcastUpdateChannelUseCase = mockk(relaxed = true)

    private val modelBuilder = UiModelBuilder()
    private val mockException = modelBuilder.buildException()
    private val mockSetChannelTagResponse = SetChannelTagsResponse(SetChannelTagsResponse.SetTags(true))
    private val mockSetChannelTagsResponseFail = SetChannelTagsResponse(SetChannelTagsResponse.SetTags(false))
    private val mockUpdateChannelStatusResponse = ChannelId("")

    @Test
    fun `when save video success, it should return success state`() {

        coEvery { mockSetChannelTagUseCase.executeOnBackground() } returns mockSetChannelTagResponse
        coEvery { mockUpdateChannelUseCase.executeOnBackground() } returns mockUpdateChannelStatusResponse

        val robot = PlayBroadcastSummaryViewModelRobot(
            dispatcher = testDispatcher,
            updateChannelUseCase = mockUpdateChannelUseCase,
            setChannelTagsUseCase =  mockSetChannelTagUseCase,
        )

        robot.use {
            val events = robot.recordEvent {
                submitAction(PlayBroadcastSummaryAction.ClickPostVideoNow)
            }

            events.last().assertEqualTo(
                PlayBroadcastSummaryEvent.PostVideo(NetworkResult.Success(true))
            )
        }
    }

    @Test
    fun `when save video & save tag failed, it should return fail state`() {

        coEvery { mockSetChannelTagUseCase.executeOnBackground() } throws mockException
        coEvery { mockUpdateChannelUseCase.executeOnBackground() } returns mockUpdateChannelStatusResponse

        val robot = PlayBroadcastSummaryViewModelRobot(
            dispatcher = testDispatcher,
            updateChannelUseCase = mockUpdateChannelUseCase,
            setChannelTagsUseCase =  mockSetChannelTagUseCase,
        )

        robot.use {
            val events = robot.recordEvent {
                submitAction(PlayBroadcastSummaryAction.ClickPostVideoNow)
            }

            assertTrue(events.last() is PlayBroadcastSummaryEvent.PostVideo)

            val event = events.last() as PlayBroadcastSummaryEvent.PostVideo
            assertTrue(event.networkResult is NetworkResult.Fail)

            val networkResultFail = event.networkResult as NetworkResult.Fail
            assertTrue(networkResultFail.error.message == mockException.message)
        }
    }

    @Test
    fun `when save video & save tag returns failed, it should return fail state`() {
        coEvery { mockSetChannelTagUseCase.executeOnBackground() } returns mockSetChannelTagsResponseFail
        coEvery { mockUpdateChannelUseCase.executeOnBackground() } returns mockUpdateChannelStatusResponse

        val robot = PlayBroadcastSummaryViewModelRobot(
            dispatcher = testDispatcher,
            updateChannelUseCase = mockUpdateChannelUseCase,
            setChannelTagsUseCase =  mockSetChannelTagUseCase,
        )

        robot.use {
            val events = robot.recordEvent {
                submitAction(PlayBroadcastSummaryAction.ClickPostVideoNow)
            }

            assertTrue(events.last() is PlayBroadcastSummaryEvent.PostVideo)

            val event = events.last() as PlayBroadcastSummaryEvent.PostVideo
            assertTrue(event.networkResult is NetworkResult.Fail)

            val networkResultFail = event.networkResult as NetworkResult.Fail
            assertTrue(networkResultFail.error.message == "${DefaultErrorThrowable.DEFAULT_MESSAGE}: Error Tag")
        }
    }

    @Test
    fun `when save video & update channel status failed, it should return fail state`() {
        coEvery { mockSetChannelTagUseCase.executeOnBackground() } returns mockSetChannelTagResponse
        coEvery { mockUpdateChannelUseCase.executeOnBackground() } throws mockException

        val robot = PlayBroadcastSummaryViewModelRobot(
            dispatcher = testDispatcher,
            updateChannelUseCase = mockUpdateChannelUseCase,
            setChannelTagsUseCase =  mockSetChannelTagUseCase,
        )

        robot.use {
            val events = robot.recordEvent {
                submitAction(PlayBroadcastSummaryAction.ClickPostVideoNow)
            }

            assertTrue(events.last() is PlayBroadcastSummaryEvent.PostVideo)

            val event = events.last() as PlayBroadcastSummaryEvent.PostVideo
            assertTrue(event.networkResult is NetworkResult.Fail)

            val networkResultFail = event.networkResult as NetworkResult.Fail
            assertTrue(networkResultFail.error.message == mockException.message)
        }
    }
}
