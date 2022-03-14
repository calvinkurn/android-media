package com.tokopedia.play.broadcaster.viewmodel.summary.tag

import com.tokopedia.play.broadcaster.domain.model.GetRecommendedChannelTagsResponse
import com.tokopedia.play.broadcaster.domain.usecase.GetLiveStatisticsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetRecommendedChannelTagsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.PlayBroadcastUpdateChannelUseCase
import com.tokopedia.play.broadcaster.domain.usecase.SetChannelTagsUseCase
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.robot.PlayBroadcastSummaryViewModelRobot
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastSummaryAction
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import com.tokopedia.play.broadcaster.util.TestHtmlTextTransformer
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.assertFalse
import com.tokopedia.play.broadcaster.util.assertTrue
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on March 11, 2022
 */
class PlayBroadcastSummaryTagViewModelTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val mockGetRecommendedChannelTagsUseCase: GetRecommendedChannelTagsUseCase = mockk(relaxed = true)

    private val modelBuilder = UiModelBuilder()
    private val mockException = modelBuilder.buildException()
    private val listTag = listOf("A", "B", "C", "A", "B")
    private val mockTag = GetRecommendedChannelTagsResponse(
        recommendedTags = GetRecommendedChannelTagsResponse.GetRecommendedTags(
            tags = listTag
        )
    )
    private val mockTagUnique = listTag.toSet()

    @Test
    fun `when fetch recommended tag success, it should emit data with no duplicate tag`() {
        coEvery { mockGetRecommendedChannelTagsUseCase.executeOnBackground() } returns mockTag

        val robot = PlayBroadcastSummaryViewModelRobot(
            dispatcher = testDispatcher,
            getRecommendedChannelTagsUseCase = mockGetRecommendedChannelTagsUseCase
        )

        robot.use {
            val state = robot.recordState {  }

            with(state.tag) {
                tags.size.assertEqualTo(mockTagUnique.size)

                val mockTagUniqueList = mockTagUnique.toList()
                tags.forEachIndexed { idx, e ->
                    e.tag.assertEqualTo(mockTagUniqueList[idx])
                }
            }
        }
    }

    @Test
    fun `when fetch recommended tag error, it should emit empty tag`() {
        coEvery { mockGetRecommendedChannelTagsUseCase.executeOnBackground() } throws mockException

        val robot = PlayBroadcastSummaryViewModelRobot(
            dispatcher = testDispatcher,
            getRecommendedChannelTagsUseCase = mockGetRecommendedChannelTagsUseCase
        )

        robot.use {
            val state = robot.recordState {  }
            assertTrue(state.tag.tags.isEmpty())
        }
    }

    @Test
    fun `when user select tag, it should emit new tag with appropriate isChosen`() {
        coEvery { mockGetRecommendedChannelTagsUseCase.executeOnBackground() } returns mockTag

        val mockTagString = "A"
        val mockSelectedTag = PlayTagUiModel(mockTagString, false)

        val robot = PlayBroadcastSummaryViewModelRobot(
            dispatcher = testDispatcher,
            getRecommendedChannelTagsUseCase = mockGetRecommendedChannelTagsUseCase
        )

        robot.use {
            val state = robot.recordState {
                getViewModel().submitAction(PlayBroadcastSummaryAction.ToggleTag(mockSelectedTag))
            }

            state.tag.tags.first { it.tag == mockTagString }.isChosen.assertTrue()
        }
    }

    @Test
    fun `when user de-select tag, it should emit new tag with appropriate isChosen`() {
        coEvery { mockGetRecommendedChannelTagsUseCase.executeOnBackground() } returns mockTag

        val mockTagString = "A"
        val mockSelectedTag = PlayTagUiModel(mockTagString, false)

        val robot = PlayBroadcastSummaryViewModelRobot(
            dispatcher = testDispatcher,
            getRecommendedChannelTagsUseCase = mockGetRecommendedChannelTagsUseCase
        )

        robot.use {
            val state = robot.recordState {
                getViewModel().submitAction(PlayBroadcastSummaryAction.ToggleTag(mockSelectedTag))
            }

            state.tag.tags.first { it.tag == mockTagString }.isChosen.assertTrue()

            val state2 = robot.recordState {
                getViewModel().submitAction(PlayBroadcastSummaryAction.ToggleTag(mockSelectedTag))
            }

            state2.tag.tags.first { it.tag == mockTagString }.isChosen.assertFalse()
        }
    }
}