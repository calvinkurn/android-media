package com.tokopedia.play.broadcaster.shorts.viewmodel.preparation

import android.net.Uri
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.shorts.robot.PlayShortsViewModelRobot
import com.tokopedia.play.broadcaster.shorts.ui.model.action.PlayShortsAction
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsCoverFormUiState
import com.tokopedia.play.broadcaster.ui.model.CoverSource
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Test

/**
 * Created By : Jonathan Darwin on December 06, 2022
 */
class PlayShortsCoverViewModelTest {

    private val mockImageUrl = "https://testing"

    private val mockCoverImage: Uri = mockk(relaxed = true)

    private val uiModelBuilder = UiModelBuilder()
    private val mockCover = uiModelBuilder.buildCoverSetupStateUploaded(
        localImage = null,
        coverImage = mockCoverImage,
        coverSource = CoverSource.Product(id = "123"),
    )

    @Test
    fun playShorts_preparation_cover_openForm() {
        val robot = PlayShortsViewModelRobot()

        robot.use {
            val state = it.recordState {
                submitAction(PlayShortsAction.OpenCoverForm)
            }

            state.coverForm.state.assertEqualTo(PlayShortsCoverFormUiState.State.Editing)
        }
    }

    @Test
    fun playShorts_preparation_cover_setCover() {

        coEvery { mockCoverImage.toString() } returns mockImageUrl

        val robot = PlayShortsViewModelRobot()

        robot.use {
            val state = it.setUp {
                submitAction(PlayShortsAction.OpenCoverForm)
            }.recordState {
                submitAction(PlayShortsAction.SetCover(mockCover))
            }

            state.coverForm.cover.assertEqualTo(mockCover)
            state.coverForm.coverUri.assertEqualTo(mockImageUrl)
        }
    }

    @Test
    fun playShorts_preparation_cover_closeForm() {
        val robot = PlayShortsViewModelRobot()

        robot.use {
            val state = it.setUp {
                submitAction(PlayShortsAction.OpenCoverForm)
            }.recordState {
                submitAction(PlayShortsAction.CloseCoverForm)
            }

            state.coverForm.state.assertEqualTo(PlayShortsCoverFormUiState.State.Unknown)
        }
    }
}
