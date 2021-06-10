package com.tokopedia.review.feature.historydetails

import com.tokopedia.review.common.data.*
import com.tokopedia.review.feature.historydetails.data.InboxReviewInsertReputation
import com.tokopedia.review.feature.historydetails.data.InboxReviewInsertReputationResponseWrapper
import com.tokopedia.review.utils.verifyReviewErrorEquals
import com.tokopedia.review.utils.verifyReviewSuccessEquals
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Assert
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyLong

class ReviewDetailViewModelTest : ReviewDetailViewModelTestFixture() {

    @Test
    fun `when setFeedbackId should set feedbackId to expected value and getReviewDetail`() {
        val feedbackId = anyLong()
        val expectedNetworkResponse = ProductrevGetReviewDetailResponseWrapper()

        onGetReviewDetails_thenReturn(expectedNetworkResponse)

        viewModel.setFeedbackId(feedbackId)

        verifyFeedbackIdEquals(feedbackId)
        verifyProductrevGetReviewDetailUseCaseCalled()
        verifyReviewDetailsSuccess(Success(expectedNetworkResponse.productrevGetReviewDetail))
    }

    @Test
    fun `when setFeedbackId but getReviewDetailFail should set feedbackId to expected value and set expected failure`() {
        val feedbackId = anyLong()
        val expectedNetworkResponse = Throwable()

        onGetReviewDetailsFails_thenReturn(expectedNetworkResponse)

        viewModel.setFeedbackId(feedbackId)

        verifyFeedbackIdEquals(feedbackId)
        verifyProductrevGetReviewDetailUseCaseCalled()
        verifyReviewDetailsError(Fail(expectedNetworkResponse))
    }

    @Test
    fun `when getReviewDetail success should execute expected usecase`() {
        val feedbackId = anyLong()
        val expectedNetworkResponse = ProductrevGetReviewDetailResponseWrapper()

        onGetReviewDetails_thenReturn(expectedNetworkResponse)

        viewModel.getReviewDetails(feedbackId, false)

        verifyProductrevGetReviewDetailUseCaseCalled()
        verifyReviewDetailsSuccess(Success(expectedNetworkResponse.productrevGetReviewDetail))
    }

    @Test
    fun `when getReviewDetail Fail should set expected failure`() {
        val feedbackId = anyLong()
        val expectedNetworkResponse = Throwable()

        onGetReviewDetailsFails_thenReturn(expectedNetworkResponse)

        viewModel.getReviewDetails(feedbackId)

        verifyProductrevGetReviewDetailUseCaseCalled()
        verifyReviewDetailsError(Fail(expectedNetworkResponse))
    }

    @Test
    fun `when retry() should execute usecase again`() {
        val feedbackId = anyLong()

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
        val expectedFeedbackId = 0L
        val actualFeedbackId = viewModel.feedbackId
        Assert.assertEquals(expectedFeedbackId, actualFeedbackId)
    }

    @Test
    fun `when submitReputation should execute expected usecase`() {
        val reputationId = anyLong()
        val reputationScore = anyInt()
        val expectedResponse = InboxReviewInsertReputationResponseWrapper(InboxReviewInsertReputation(success = 1))

        onSubmitReputation_thenReturn(expectedResponse)

        viewModel.submitReputation(reputationId, reputationScore)

        verifySubmitReputationUseCaseCalled()
        verifyInboxReviewInsertReputationSuccess(Success(reputationScore))
    }

    @Test
    fun `when submitReputation fail due to backend should return expected error`() {
        val reputationId = anyLong()
        val reputationScore = anyInt()
        val expectedResponse = InboxReviewInsertReputationResponseWrapper(InboxReviewInsertReputation(success = 0))

        onSubmitReputation_thenReturn(expectedResponse)

        viewModel.submitReputation(reputationId, reputationScore)

        verifySubmitReputationUseCaseCalled()
        verifyInboxReviewInsertReputationError(Fail(Throwable()))
    }

    @Test
    fun `when submitReputation fails due to network should execute expected usecase and throw expected throwable`() {
        val reputationId = anyLong()
        val reputationScore = anyInt()
        val expectedResponse = Throwable()

        onSubmitReputationFails_thenReturn(expectedResponse)

        viewModel.submitReputation(reputationId, reputationScore)

        verifySubmitReputationUseCaseCalled()
        verifyInboxReviewInsertReputationError(Fail(expectedResponse))
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

    private fun onSubmitReputation_thenReturn(response: InboxReviewInsertReputationResponseWrapper) {
        coEvery { inboxReviewInsertReputationUseCase.executeOnBackground() } returns response
    }

    private fun onSubmitReputationFails_thenReturn(throwable: Throwable) {
        coEvery { inboxReviewInsertReputationUseCase.executeOnBackground() } throws throwable
    }

    private fun verifySubmitReputationUseCaseCalled() {
        coVerify { inboxReviewInsertReputationUseCase.executeOnBackground() }
    }

    private fun verifyFeedbackIdEquals(feedbackId: Long) {
        Assert.assertEquals(feedbackId, viewModel.feedbackId)
    }

    private fun verifyInboxReviewInsertReputationSuccess(viewState: Success<Int>) {
        viewModel.submitReputationResult.verifyReviewSuccessEquals(viewState)
    }

    private fun verifyInboxReviewInsertReputationError(viewState: Fail<Int>) {
        viewModel.submitReputationResult.verifyReviewErrorEquals(viewState)
    }

    private fun verifyReviewDetailsSuccess(viewState: Success<ProductrevGetReviewDetail>) {
        viewModel.reviewDetails.verifyReviewSuccessEquals(viewState)
    }

    private fun verifyReviewDetailsError(viewState: Fail<ProductrevGetReviewDetail>) {
        viewModel.reviewDetails.verifyReviewErrorEquals(viewState)
    }
}