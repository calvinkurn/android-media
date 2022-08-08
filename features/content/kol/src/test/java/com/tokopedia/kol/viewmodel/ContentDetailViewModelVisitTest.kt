package com.tokopedia.kol.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kol.common.util.ContentDetailResult
import com.tokopedia.kol.feature.postdetail.domain.ContentDetailRepository
import com.tokopedia.kol.feature.postdetail.view.viewmodel.ContentDetailViewModel
import com.tokopedia.kol.model.ContentDetailModelBuilder
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.Rule
import org.junit.Test

/**
 * Created by meyta.taliti on 08/08/22.
 */
class ContentDetailViewModelVisitTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val mockRepo: ContentDetailRepository = mockk(relaxed = true)
    private val viewModel = ContentDetailViewModel(
        testDispatcher,
        mockRepo,
    )

    private val channelId = "1234"
    private val contentId = "1234"
    private val builder = ContentDetailModelBuilder()

    @Test
    fun `when user watch vod content it should count as visit channel, given response success, then it should emit VisitContentModel`() {
        val rowNumber = 0
        val expectedResult = builder.getVisitContentModel(rowNumber)

        coEvery { mockRepo.trackVisitChannel(channelId, rowNumber) } returns expectedResult

        viewModel.trackVisitChannel(channelId, rowNumber)

        val result = viewModel.vodViewData.value

        Assertions
            .assertThat(result)
            .isEqualTo(ContentDetailResult.Success(expectedResult))
    }

    @Test
    fun `when user watch vod content it should count as visit channel, given response error, then it should emit error`() {
        val rowNumber = 0

        coEvery { mockRepo.trackVisitChannel(channelId, rowNumber) } throws Throwable()

        viewModel.trackVisitChannel(channelId, rowNumber)

        val result = viewModel.vodViewData.value

        Assertions
            .assertThat(result)
            .isInstanceOf(ContentDetailResult.Failure::class.java)
    }

    @Test
    fun `when user watch long video content it should count as visit channel, given response success, then it should emit VisitContentModel`() {
        val rowNumber = 0
        val expectedResult = builder.getVisitContentModel(rowNumber)

        coEvery { mockRepo.trackViewer(contentId, rowNumber) } returns expectedResult

        viewModel.trackLongVideoView(contentId, rowNumber)

        val result = viewModel.vodViewData.value

        Assertions
            .assertThat(result)
            .isEqualTo(ContentDetailResult.Success(expectedResult))
    }

    @Test
    fun `when user watch long vod content it should count as visit channel, given response error, then it should emit error`() {
        val rowNumber = 0

        coEvery { mockRepo.trackViewer(contentId, rowNumber) } throws Throwable()

        viewModel.trackLongVideoView(contentId, rowNumber)

        val result = viewModel.vodViewData.value

        Assertions
            .assertThat(result)
            .isInstanceOf(ContentDetailResult.Failure::class.java)
    }
}