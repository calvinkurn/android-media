package com.tokopedia.review.feature.gallery.presentation.viewmodel

import com.tokopedia.review.common.data.ToggleLikeReviewResponse
import com.tokopedia.review.common.data.ToggleProductReviewLike
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Test
import org.mockito.ArgumentMatchers

class ReviewGalleryViewModelTest : ReviewGalleryViewModelTestFixture() {

    @Test
    fun `when toggleLike should call toggleLikeReviewUseCase and return expected results`() {
        val reviewId = ArgumentMatchers.anyString()
        val shopId = ArgumentMatchers.anyString()
        val likeStatus = ArgumentMatchers.anyInt()
        val productId = ArgumentMatchers.anyString()
        val expectedResponse = ToggleLikeReviewResponse()

        onToggleLikeReviewSuccess_thenReturn(expectedResponse)

        viewModel.toggleLikeReview(reviewId, shopId, productId, likeStatus)

        verifyToggleLikeDislikeUseCaseExecuted()
        verifyToggleLikeReviewSuccessEquals(Success(expectedResponse.toggleProductReviewLike))
    }

    @Test
    fun `when toggleLike should call toggleLikeReviewUseCase and return expected error`() {
        val reviewId = ArgumentMatchers.anyString()
        val shopId = ArgumentMatchers.anyString()
        val productId = ArgumentMatchers.anyString()
        val likeStatus = ArgumentMatchers.anyInt()
        val expectedResponse = Throwable()

        onToggleLikeReviewFail_thenReturn(expectedResponse)

        viewModel.toggleLikeReview(reviewId, shopId, productId, likeStatus)

        verifyToggleLikeDislikeUseCaseExecuted()
        verifyToggleLikeReviewErrorEquals(Fail(expectedResponse))
    }

    private fun onToggleLikeReviewSuccess_thenReturn(expectedResponse: ToggleLikeReviewResponse) {
        coEvery { toggleLikeReviewUseCase.executeOnBackground() } returns expectedResponse
    }

    private fun onToggleLikeReviewFail_thenReturn(throwable: Throwable) {
        coEvery { toggleLikeReviewUseCase.executeOnBackground() } throws throwable
    }

    private fun verifyToggleLikeDislikeUseCaseExecuted() {
        coVerify { toggleLikeReviewUseCase.executeOnBackground() }
    }

    private fun verifyToggleLikeReviewSuccessEquals(expectedSuccessValue: Success<ToggleProductReviewLike>) {
        viewModel.toggleLikeReview.verifySuccessEquals(expectedSuccessValue)
    }

    private fun verifyToggleLikeReviewErrorEquals(expectedErrorValue: Fail) {
        viewModel.toggleLikeReview.verifyErrorEquals(expectedErrorValue)
    }
}