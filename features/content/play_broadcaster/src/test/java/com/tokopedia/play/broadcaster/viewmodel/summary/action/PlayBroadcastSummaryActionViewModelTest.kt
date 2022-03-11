package com.tokopedia.play.broadcaster.viewmodel.summary.action

import com.tokopedia.play.broadcaster.domain.model.GetRecommendedChannelTagsResponse
import com.tokopedia.play.broadcaster.domain.usecase.GetLiveStatisticsUseCase
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.play.broadcaster.util.TestHtmlTextTransformer
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on March 11, 2022
 */
class PlayBroadcastSummaryActionViewModelTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val playBroadcastMapper = PlayBroadcastUiMapper(TestHtmlTextTransformer())

    private val mockGetLiveStatisticsUseCase: GetLiveStatisticsUseCase = mockk(relaxed = true)

    private val modelBuilder = UiModelBuilder()
    private val mockException = Exception("Network Error")
    private val mockLiveStats by lazy { modelBuilder.buildLiveStats() }

    private val listTag = listOf("A", "B", "C", "A", "B")
    private val mockTag = GetRecommendedChannelTagsResponse(
        recommendedTags = GetRecommendedChannelTagsResponse.GetRecommendedTags(
            tags = listTag
        )
    )
    private val mockTagUnique = listTag.toSet()

    @Test
    fun `when user click close button on report page, it should emit event close report page`() {

    }

    @Test
    fun `when user click view leaderboard, it should emit event open leaderboard bottom sheet`() {

    }

    @Test
    fun `when user click post video on report page, it should emit event open post video page`() {

    }

    @Test
    fun `when user click back to report page, it should emit event back to report page`() {

    }

    @Test
    fun `when user click edit cover, it should emit event open select cover bottom sheet`() {

    }
}