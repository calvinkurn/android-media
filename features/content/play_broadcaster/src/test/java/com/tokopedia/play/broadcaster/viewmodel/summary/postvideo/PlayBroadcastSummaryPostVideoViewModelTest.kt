package com.tokopedia.play.broadcaster.viewmodel.summary.postvideo

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
class PlayBroadcastSummaryPostVideoViewModelTest {

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
    fun `when save video success, it should return success state`() {

    }

    @Test
    fun `when save video failed, it should return fail state`() {

    }
}