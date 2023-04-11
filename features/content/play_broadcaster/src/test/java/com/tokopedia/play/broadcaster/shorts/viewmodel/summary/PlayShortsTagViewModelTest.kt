package com.tokopedia.play.broadcaster.shorts.viewmodel.summary

import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.shorts.domain.PlayShortsRepository
import com.tokopedia.play.broadcaster.shorts.robot.PlayShortsViewModelRobot
import com.tokopedia.play.broadcaster.shorts.ui.model.action.PlayShortsAction
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.assertFalse
import com.tokopedia.play.broadcaster.util.assertTrue
import com.tokopedia.play_common.model.result.NetworkResult
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Test
import org.junit.jupiter.api.fail

/**
 * Created By : Jonathan Darwin on December 06, 2022
 */
class PlayShortsTagViewModelTest {

    private val mockRepo: PlayShortsRepository = mockk(relaxed = true)

    private val uiModelBuilder = UiModelBuilder()

    private val mockTagsSize5 = uiModelBuilder.buildTags(size = 5)
    private val mockTagsSize10 = uiModelBuilder.buildTags(size = 10)
    private val mockException = Exception("Network Error")

    @Test
    fun playShorts_summary_tag_loadTagSuccess() {
        coEvery { mockRepo.getTagRecommendation(any()) } returns mockTagsSize5

        val robot = PlayShortsViewModelRobot(
            repo = mockRepo
        )

        robot.use {
            val state = it.recordState {
                submitAction(PlayShortsAction.LoadTag)
            }

            assertTagSuccess(state.tags) { tags ->
                tags.assertEqualTo(mockTagsSize5)
            }
        }
    }

    @Test
    fun playShorts_summary_tag_loadTagError() {
        coEvery { mockRepo.getTagRecommendation(any()) } throws mockException

        val robot = PlayShortsViewModelRobot(
            repo = mockRepo
        )

        robot.use {
            val state = it.recordState {
                submitAction(PlayShortsAction.LoadTag)
            }

            assertTagFail(state.tags) { error ->
                error.assertEqualTo(mockException)
            }
        }
    }

    @Test
    fun playShorts_summary_tag_notAllowedToReloadTag() {
        coEvery { mockRepo.getTagRecommendation(any()) } returns mockTagsSize5

        val robot = PlayShortsViewModelRobot(
            repo = mockRepo
        )

        robot.use {
            val state = it.recordState {
                submitAction(PlayShortsAction.LoadTag)
            }

            assertTagSuccess(state.tags) { tags ->
                tags.assertEqualTo(mockTagsSize5)
            }

            coEvery { mockRepo.getTagRecommendation(any()) } returns mockTagsSize10

            val stateAfterReload = it.recordState {
                submitAction(PlayShortsAction.LoadTag)
            }

            assertTagSuccess(stateAfterReload.tags) { tags ->
                tags.assertEqualTo(mockTagsSize5)
            }
        }
    }

    @Test
    fun playShorts_summary_tag_selectTag() {
        val mockSelectedIdxList = listOf(2, 4)

        coEvery { mockRepo.getTagRecommendation(any()) } returns mockTagsSize5

        val robot = PlayShortsViewModelRobot(
            repo = mockRepo
        )

        robot.use {
            val state = it.setUp {
                submitAction(PlayShortsAction.LoadTag)
            }.recordState {
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.toList()[mockSelectedIdxList.first()]))
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.toList()[mockSelectedIdxList.last()]))
            }

            assertTagSuccess(state.tags) { tags ->
                assertTagIsChosen(tags, mockSelectedIdxList)
            }
        }
    }

    @Test
    fun playShorts_summary_tag_unselectTag() {
        val mockSelectedIdxList = listOf(2, 4)

        coEvery { mockRepo.getTagRecommendation(any()) } returns mockTagsSize5

        val robot = PlayShortsViewModelRobot(
            repo = mockRepo
        )

        robot.use {
            val state = it.setUp {
                submitAction(PlayShortsAction.LoadTag)
            }.recordState {
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.toList()[mockSelectedIdxList.first()]))
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.toList()[mockSelectedIdxList.last()]))
            }

            assertTagSuccess(state.tags) { tags ->
                assertTagIsChosen(tags, mockSelectedIdxList)
            }

            val stateAfterUnselect = it.setUp {
                submitAction(PlayShortsAction.LoadTag)
            }.recordState {
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.toList()[mockSelectedIdxList.first()]))
            }

            assertTagSuccess(stateAfterUnselect.tags) { tags ->
                assertTagIsChosen(tags, listOf(mockSelectedIdxList.last()))
            }
        }
    }

    @Test
    fun playShorts_summary_tag_cantSelectUnselectTagIfStatusNotSuccess() {
        val mockSelectedIdxList = listOf(2, 4)

        coEvery { mockRepo.getTagRecommendation(any()) } throws mockException

        val robot = PlayShortsViewModelRobot(
            repo = mockRepo
        )

        robot.use {
            val state = it.setUp {
                submitAction(PlayShortsAction.LoadTag)
            }.recordState {
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.toList()[mockSelectedIdxList.first()]))
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.toList()[mockSelectedIdxList.last()]))
            }

            assertTagFail(state.tags) {
                it.assertEqualTo(mockException)
            }
        }
    }

    /**
     * Assertion Helper Function
     */
    private fun assertTagSuccess(tags: NetworkResult<Set<PlayTagUiModel>>, fn: (tags: Set<PlayTagUiModel>) -> Unit) {
        if(tags is NetworkResult.Success) {
            fn(tags.data)
        }
        else {
            fail(Exception("tag status should be NetworkResult.Success"))
        }
    }

    private fun assertTagFail(tags: NetworkResult<Set<PlayTagUiModel>>, fn: (e: Throwable) -> Unit) {
        if(tags is NetworkResult.Fail) {
            fn(tags.error)
        }
        else {
            fail(Exception("tag status should be NetworkResult.Fail"))
        }
    }

    private fun assertTagIsChosen(tags: Set<PlayTagUiModel>, chosenIdxList: List<Int>) {
        tags.forEachIndexed { idx, e ->
            if(chosenIdxList.contains(idx)) {
                e.isChosen.assertTrue()
            }
            else {
                e.isChosen.assertFalse()
            }
        }
    }
}
