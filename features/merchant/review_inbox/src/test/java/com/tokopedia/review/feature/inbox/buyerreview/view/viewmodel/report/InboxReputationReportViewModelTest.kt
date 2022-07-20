package com.tokopedia.review.feature.inbox.buyerreview.view.viewmodel.report

import com.tokopedia.review.feature.inbox.buyerreview.domain.model.report.ReportReviewResponse
import com.tokopedia.review.inbox.R
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString

class InboxReputationReportViewModelTest: InboxReputationReportViewModelTestFixture() {
    @Test
    fun `report review should success when usecase return expected response`() {
        runBlocking {
            onReportReview_thenReturn()
            viewModel.reportReview(anyString(), anyInt(), anyString())

            verifySuccessReportReviewUseCaseCalled()
            val expectedValue = Success(ReportReviewResponse.ProductrevReportReview(anyBoolean()))
            TestCase.assertTrue(viewModel.reportReviewResult.value is Success)
            viewModel.reportReviewResult.verifyValueEquals(expectedValue)
        }
    }

    @Test
    fun `report review should fail when usecase throw an exception`() {
        runBlocking {
            val error = NullPointerException()
            onReportReview_thenError(error)
            viewModel.reportReview(anyString(), anyInt(), anyString())

            verifySuccessReportReviewUseCaseCalled()
            val expectedResult = Fail(error)
            viewModel.reportReviewResult.verifyErrorEquals(expectedResult)
        }
    }

    @Test
    fun `given checked report spam radio id then should set reason code param to 1 on usecase`() {
        runBlocking {
            viewModel.reportReview(anyString(), R.id.report_spam, anyString())
            verifySuccessReportReviewUseCaseCalled(reasonCode = 1)
        }
    }

    @Test
    fun `given checked report sara radio id then should set reason code param to 2 on usecase`() {
        runBlocking {
            viewModel.reportReview(anyString(), R.id.report_sara, anyString())
            verifySuccessReportReviewUseCaseCalled(reasonCode = 2)
        }
    }

    @Test
    fun `given checked report other radio id then should set reason code param to 3 on usecase`() {
        runBlocking {
            viewModel.reportReview(anyString(), R.id.report_other, anyString())
            verifySuccessReportReviewUseCaseCalled(reasonCode = 3)
        }
    }

    private fun onReportReview_thenReturn() {
        coEvery { reportReviewUseCase.execute(any(), any(), any()) } returns ReportReviewResponse.ProductrevReportReview(anyBoolean())
    }

    private fun onReportReview_thenError(error: Exception) {
        coEvery { reportReviewUseCase.execute(any(), any(), any()) } throws error
    }

    private fun verifySuccessReportReviewUseCaseCalled(
        feedbackId: String = anyString(),
        reasonCode: Int = 3,
        reasonText: String = anyString()
    ) {
        coVerify { reportReviewUseCase.execute(feedbackId, reasonCode, reasonText) }
    }
}