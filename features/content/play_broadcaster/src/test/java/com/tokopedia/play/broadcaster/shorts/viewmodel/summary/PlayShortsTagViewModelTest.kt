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
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
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

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

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

            assertTagSuccess(state.tags) { data ->
                data.tags.assertEqualTo(mockTagsSize5.tags)
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

            assertTagSuccess(state.tags) { data ->
                data.tags.assertEqualTo(mockTagsSize5.tags)
            }

            coEvery { mockRepo.getTagRecommendation(any()) } returns mockTagsSize10

            val stateAfterReload = it.recordState {
                submitAction(PlayShortsAction.LoadTag)
            }

            assertTagSuccess(stateAfterReload.tags) { data ->
                data.tags.assertEqualTo(mockTagsSize5.tags)
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
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.tags.toList()[mockSelectedIdxList.first()]))
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.tags.toList()[mockSelectedIdxList.last()]))
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
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.tags.toList()[mockSelectedIdxList.first()]))
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.tags.toList()[mockSelectedIdxList.last()]))
            }

            assertTagSuccess(state.tags) { tags ->
                assertTagIsChosen(tags, mockSelectedIdxList)
            }

            val stateAfterUnselect = it.setUp {
                submitAction(PlayShortsAction.LoadTag)
            }.recordState {
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.tags.toList()[mockSelectedIdxList.first()]))
            }

            assertTagSuccess(stateAfterUnselect.tags) { tags ->
                assertTagIsChosen(tags, listOf(mockSelectedIdxList.last()))
            }
        }
    }

    @Test
    fun playShorts_summary_tag_selectTagToMaxTagAllowed() {
        val mockSelectedIdxList = listOf(1, 2, 4)
        val maxTags = 3

        coEvery { mockRepo.getTagRecommendation(any()) } returns mockTagsSize5.copy(maxTags = maxTags)

        val robot = PlayShortsViewModelRobot(
            repo = mockRepo
        )

        robot.use {
            val state = it.setUp {
                submitAction(PlayShortsAction.LoadTag)
            }.recordState {
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.tags.toList()[mockSelectedIdxList.first()]))
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.tags.toList()[mockSelectedIdxList.last()]))
            }

            assertTagSuccess(state.tags) { data ->
                data.tags.forEachIndexed { idx, e ->
                    if (idx == mockSelectedIdxList.first() || idx == mockSelectedIdxList.last()) {
                        e.isChosen.assertTrue()
                    }
                    else {
                        e.isChosen.assertFalse()
                    }
                    e.isActive.assertTrue()
                }
            }

            val state2 = it.recordState {
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.tags.toList()[mockSelectedIdxList[1]]))
            }

            assertTagSuccess(state2.tags) { data ->
                data.tags.forEachIndexed { idx, e ->
                    if (mockSelectedIdxList.contains(idx)) {
                        e.isChosen.assertTrue()
                        e.isActive.assertTrue()
                    }
                    else {
                        e.isChosen.assertFalse()
                        e.isActive.assertFalse()
                    }
                }
            }
        }
    }

    @Test
    fun playShorts_summary_tag_AlrMaxTag_unselectTag() {
        val mockSelectedIdxList = listOf(1, 2, 4)
        val maxTags = 3

        coEvery { mockRepo.getTagRecommendation(any()) } returns mockTagsSize5.copy(maxTags = maxTags)

        val robot = PlayShortsViewModelRobot(
            repo = mockRepo
        )

        robot.use {
            val state = it.setUp {
                submitAction(PlayShortsAction.LoadTag)
            }.recordState {
                mockSelectedIdxList.forEach { idx ->
                    submitAction(PlayShortsAction.SelectTag(mockTagsSize5.tags.toList()[idx]))
                }
            }

            assertTagSuccess(state.tags) { data ->
                data.tags.forEachIndexed { idx, e ->
                    if (mockSelectedIdxList.contains(idx)) {
                        e.isChosen.assertTrue()
                        e.isActive.assertTrue()
                    }
                    else {
                        e.isChosen.assertFalse()
                        e.isActive.assertFalse()
                    }
                }
            }

            val state2 = it.recordState {
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.tags.toList()[mockSelectedIdxList.first()]))
            }

            assertTagSuccess(state2.tags) { data ->
                data.tags.forEachIndexed { idx, e ->
                    if (mockSelectedIdxList.contains(idx) && idx != mockSelectedIdxList.first()) {
                        e.isChosen.assertTrue()
                    }
                    else {
                        e.isChosen.assertFalse()
                    }
                    e.isActive.assertTrue()
                }
            }
        }
    }

    @Test
    fun playShorts_summary_tag_selectDisabledTag() {
        val mockSelectedIdxList = listOf(1, 2, 4)
        val maxTags = 3

        coEvery { mockRepo.getTagRecommendation(any()) } returns mockTagsSize5.copy(maxTags = maxTags)

        val robot = PlayShortsViewModelRobot(
            repo = mockRepo
        )

        robot.use {
            val state = it.setUp {
                submitAction(PlayShortsAction.LoadTag)
            }.recordState {
                mockSelectedIdxList.forEach { idx ->
                    submitAction(PlayShortsAction.SelectTag(mockTagsSize5.tags.toList()[idx]))
                }
            }

            assertTagSuccess(state.tags) { data ->
                data.tags.forEachIndexed { idx, e ->
                    if (mockSelectedIdxList.contains(idx)) {
                        e.isChosen.assertTrue()
                        e.isActive.assertTrue()
                    }
                    else {
                        e.isChosen.assertFalse()
                        e.isActive.assertFalse()
                    }
                }
            }

            val state2 = it.recordState {
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.tags.toList()[3]))
            }

            assertTagSuccess(state2.tags) { data ->
                data.tags.toList()[3].isChosen.assertFalse()
                data.tags.toList()[3].isActive.assertFalse()
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
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.tags.toList()[mockSelectedIdxList.first()]))
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.tags.toList()[mockSelectedIdxList.last()]))
            }

            assertTagFail(state.tags) { throwable ->
                throwable.assertEqualTo(mockException)
            }
        }
    }

    /**
     * Assertion Helper Function
     */
    private fun assertTagSuccess(tag: NetworkResult<PlayTagUiModel>, fn: (tags: PlayTagUiModel) -> Unit) {
        if(tag is NetworkResult.Success) {
            fn(tag.data)
        }
        else {
            fail(Exception("tag status should be NetworkResult.Success"))
        }
    }

    private fun assertTagFail(tag: NetworkResult<PlayTagUiModel>, fn: (e: Throwable) -> Unit) {
        if(tag is NetworkResult.Fail) {
            fn(tag.error)
        }
        else {
            fail(Exception("tag status should be NetworkResult.Fail"))
        }
    }

    private fun assertTagIsChosen(tag: PlayTagUiModel, chosenIdxList: List<Int>) {
        tag.tags.forEachIndexed { idx, e ->
            if(chosenIdxList.contains(idx)) {
                e.isChosen.assertTrue()
            }
            else {
                e.isChosen.assertFalse()
            }
        }
    }
}
