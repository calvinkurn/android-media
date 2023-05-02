package com.tokopedia.play.broadcaster.shorts.viewmodel.preparation

import com.tokopedia.play.broadcaster.shorts.domain.PlayShortsRepository
import com.tokopedia.play.broadcaster.shorts.robot.PlayShortsViewModelRobot
import com.tokopedia.play.broadcaster.shorts.ui.model.action.PlayShortsAction
import com.tokopedia.play.broadcaster.shorts.ui.model.event.PlayShortsUiEvent
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsTitleFormUiState
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.assertType
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Test

/**
 * Created By : Jonathan Darwin on December 06, 2022
 */
class PlayShortsTitleViewModelTest {

    private val mockRepo: PlayShortsRepository = mockk(relaxed = true)
    private val mockTitle = "Pokemon"
    private val mockException = Exception("Network Error")

    @Test
    fun playShorts_preparation_title_openForm() {
        val robot = PlayShortsViewModelRobot()

        robot.use {
            val state = it.recordState {
                submitAction(PlayShortsAction.OpenTitleForm)
            }

            state.titleForm.state.assertEqualTo(PlayShortsTitleFormUiState.State.Editing)
        }
    }

    @Test
    fun playShorts_preparation_title_uploadTitleSuccess() {
        coEvery { mockRepo.uploadTitle(any(), any(), any()) } returns Unit

        val robot = PlayShortsViewModelRobot(
            repo = mockRepo
        )

        robot.use {
            val state = it.setUp {
                submitAction(PlayShortsAction.OpenTitleForm)
            }.recordState {
                submitAction(PlayShortsAction.UploadTitle(mockTitle))
            }

            state.titleForm.state.assertEqualTo(PlayShortsTitleFormUiState.State.Unknown)
            state.titleForm.title.assertEqualTo(mockTitle)
        }
    }

    @Test
    fun playShorts_preparation_title_uploadTitleError() {
        coEvery { mockRepo.uploadTitle(any(), any(), any()) } throws mockException

        val robot = PlayShortsViewModelRobot(
            repo = mockRepo
        )

        robot.use {
            val (state, events) = it.setUp {
                submitAction(PlayShortsAction.OpenTitleForm)
            }.recordStateAndEvent {
                submitAction(PlayShortsAction.UploadTitle(mockTitle))
            }

            state.titleForm.state.assertEqualTo(PlayShortsTitleFormUiState.State.Editing)
            state.titleForm.title.assertEqualTo("")
            events.last().assertType<PlayShortsUiEvent.ErrorUploadTitle>()
        }
    }

    @Test
    fun playShorts_preparation_title_closeForm() {
        val robot = PlayShortsViewModelRobot()

        robot.use {
            val state = it.recordState {
                submitAction(PlayShortsAction.OpenTitleForm)
                submitAction(PlayShortsAction.CloseTitleForm)
            }

            state.titleForm.state.assertEqualTo(PlayShortsTitleFormUiState.State.Unknown)
        }
    }
}
