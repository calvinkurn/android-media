package com.tokopedia.play.broadcaster.shorts.viewmodel.summary

import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.shorts.domain.PlayShortsRepository
import com.tokopedia.play.broadcaster.shorts.robot.PlayShortsViewModelRobot
import com.tokopedia.play.broadcaster.shorts.ui.model.action.PlayShortsAction
import com.tokopedia.play.broadcaster.shorts.ui.model.event.PlayShortsUiEvent
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsUploadUiState
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.assertType
import com.tokopedia.play_common.shortsuploader.PlayShortsUploader
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on December 06, 2022
 */
class PlayShortsUploadViewModelTest {

    private val mockRepo: PlayShortsRepository = mockk(relaxed = true)
    private val mockPlayShortsUploader: PlayShortsUploader = mockk(relaxed = true)

    private val uiModelBuilder = UiModelBuilder()

    private val mockTagsSize5 = uiModelBuilder.buildTags(size = 5)
    private val mockException = Exception("Network Error")

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    @Test
    fun playShorts_summary_upload_uploadSuccess() {
        val mockSelectedIdxList = listOf(2, 4)

        coEvery { mockRepo.getTagRecommendation(any()) } returns mockTagsSize5
        coEvery { mockRepo.saveTag(any(), any()) } returns true

        val robot = PlayShortsViewModelRobot(
            repo = mockRepo
        )

        robot.use {
            val state = it.setUp {
                submitAction(PlayShortsAction.LoadTag)
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.tags.toList()[mockSelectedIdxList.first()]))
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.tags.toList()[mockSelectedIdxList.last()]))
            }.recordState {
                submitAction(PlayShortsAction.ClickUploadVideo)
            }

            state.uploadState.assertEqualTo(PlayShortsUploadUiState.Success)
        }
    }

    @Test
    fun playShorts_summary_upload_uploadSuccessWhenNoTag() {
        coEvery { mockRepo.getTagRecommendation(any()) } throws mockException

        val robot = PlayShortsViewModelRobot(
            repo = mockRepo
        )

        robot.use {
            val state = it.setUp {
                submitAction(PlayShortsAction.LoadTag)
            }.recordState {
                submitAction(PlayShortsAction.ClickUploadVideo)
            }

            state.uploadState.assertType<PlayShortsUploadUiState.Success>()
        }
    }

    @Test
    fun playShorts_summary_upload_uploadTagFail() {
        val mockSelectedIdxList = listOf(2, 4)

        coEvery { mockRepo.getTagRecommendation(any()) } returns mockTagsSize5
        coEvery { mockRepo.saveTag(any(), any()) } returns false

        val robot = PlayShortsViewModelRobot(
            repo = mockRepo
        )

        robot.use {
            val (state, events) = it.setUp {
                submitAction(PlayShortsAction.LoadTag)
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.tags.toList()[mockSelectedIdxList.first()]))
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.tags.toList()[mockSelectedIdxList.last()]))
            }.recordStateAndEvent {
                submitAction(PlayShortsAction.ClickUploadVideo)
            }

            state.uploadState.assertType<PlayShortsUploadUiState.Error>()
            events.last().assertType<PlayShortsUiEvent.ErrorUploadMedia>()
        }
    }

    @Test
    fun playShorts_summary_upload_uploadMediaFail() {
        val mockSelectedIdxList = listOf(2, 4)

        coEvery { mockRepo.getTagRecommendation(any()) } returns mockTagsSize5
        coEvery { mockRepo.saveTag(any(), any()) } returns true
        coEvery { mockPlayShortsUploader.upload(any()) } throws mockException

        val robot = PlayShortsViewModelRobot(
            repo = mockRepo,
            playShortsUploader = mockPlayShortsUploader,
        )

        robot.use {
            val (state, events) = it.setUp {
                submitAction(PlayShortsAction.LoadTag)
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.tags.toList()[mockSelectedIdxList.first()]))
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.tags.toList()[mockSelectedIdxList.last()]))
            }.recordStateAndEvent {
                submitAction(PlayShortsAction.ClickUploadVideo)
            }

            state.uploadState.assertType<PlayShortsUploadUiState.Error>()
            events.last().assertType<PlayShortsUiEvent.ErrorUploadMedia>()
        }
    }
}
