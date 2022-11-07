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
class ContentDetailViewModelReportTest {

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

    private val contentId = "1234"
    private val builder = ContentDetailModelBuilder()

    @Test
    fun `when user report content, given response success, then it should emit ReportContentModel`() {
        val rowNumber = 0
        val expectedResult = builder.getReportContentModel(rowNumber)

        coEvery { mockRepo.reportContent(contentId, "", "", rowNumber) } returns expectedResult

        viewModel.sendReport(rowNumber, contentId, "", "")

        val result = viewModel.reportResponse.value

        Assertions
            .assertThat(result)
            .isEqualTo(ContentDetailResult.Success(expectedResult))
    }

    @Test
    fun `when user report content, given response error, then it should emit error`() {
        val rowNumber = 0

        coEvery { mockRepo.reportContent(contentId, "", "", rowNumber) } throws Throwable()

        viewModel.sendReport(rowNumber, contentId, "", "")

        val result = viewModel.reportResponse.value

        Assertions
            .assertThat(result)
            .isInstanceOf(ContentDetailResult.Failure::class.java)
    }
}