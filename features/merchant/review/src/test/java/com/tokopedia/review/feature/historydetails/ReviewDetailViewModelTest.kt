package com.tokopedia.review.feature.historydetails

import com.tokopedia.review.common.data.*
import com.tokopedia.review.utils.verifyErrorEquals
import com.tokopedia.review.utils.verifySuccessEquals
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Assert
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt

class ReviewDetailViewModelTest : ReviewDetailViewModelTestFixture() {

    @Test
    fun `when setFeedbackId should set feedbackId to expected value and getReviewDetail`() {
        val feedbackId = anyInt()
        val expectedNetworkResponse = ProductrevGetReviewDetailResponseWrapper()

        onGetReviewDetails_thenReturn(expectedNetworkResponse)

        viewModel.setFeedbackId(feedbackId)

        verifyFeedbackIdEquals(feedbackId)
        verifyProductrevGetReviewDetailUseCaseCalled()
        verifyReviewDetailsSuccess(Success(expectedNetworkResponse.productrevGetReviewDetail))
    }

    @Test
    fun `when setFeedbackId but getReviewDetailFail should set feedbackId to expected value and set expected failure`() {
        val feedbackId = anyInt()
        val expectedNetworkResponse = Throwable()

        onGetReviewDetailsFails_thenReturn(expectedNetworkResponse)

        viewModel.setFeedbackId(feedbackId)

        verifyFeedbackIdEquals(feedbackId)
        verifyProductrevGetReviewDetailUseCaseCalled()
        verifyReviewDetailsError(Fail(expectedNetworkResponse))
    }

    @Test
    fun `when retry() should execute usecase again`() {
        val feedbackId = anyInt()

        viewModel.setFeedbackId(feedbackId)
        verifyProductrevGetReviewDetailUseCaseCalled()

        viewModel.retry()
        verifyProductrevGetReviewDetailUseCaseCalled()
    }

    @Test
    fun `when getUserId should return valid userId`() {
        val actualUserId = viewModel.getUserId()
        Assert.assertTrue(actualUserId.isEmpty())
    }

    @Test
    fun `when feedback Id live data is not initialized should return 0`() {
        val expectedFeedbackId = 0
        val actualFeedbackId = viewModel.feedbackId
        Assert.assertEquals(expectedFeedbackId, actualFeedbackId)
    }

    private fun onGetReviewDetails_thenReturn(response: ProductrevGetReviewDetailResponseWrapper) {
        coEvery { productrevGetReviewDetailUseCase.executeOnBackground() } returns response
    }

    private fun onGetReviewDetailsFails_thenReturn(throwable: Throwable) {
        coEvery { productrevGetReviewDetailUseCase.executeOnBackground() } throws throwable
    }

    private fun verifyProductrevGetReviewDetailUseCaseCalled() {
        coVerify { productrevGetReviewDetailUseCase.executeOnBackground() }
    }

    private fun verifyFeedbackIdEquals(feedbackId: Int) {
        Assert.assertEquals(feedbackId, viewModel.feedbackId)
    }

    private fun verifyReviewDetailsSuccess(viewState: Success<ProductrevGetReviewDetail>) {
        viewModel.reviewDetails.verifySuccessEquals(viewState)
    }

    private fun verifyReviewDetailsError(viewState: Fail<ProductrevGetReviewDetail>) {
        viewModel.reviewDetails.verifyErrorEquals(viewState)
    }
}