package com.tokopedia.review.feature.imagepreview.presentation.viewmodel

import com.tokopedia.review.common.data.ToggleLikeReviewResponse
import com.tokopedia.review.common.data.ToggleProductReviewLike
import com.tokopedia.review.feature.gallery.data.ProductrevGetReviewImage
import com.tokopedia.review.feature.gallery.data.ProductrevGetReviewImageResponse
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Assert
import org.junit.Test
import org.mockito.ArgumentMatchers

class ReviewImagePreviewViewModelTest : ReviewImagePreviewViewModelTestFixture() {

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

    @Test
    fun `when setPage should call getProductReviews and return expected results`() {
        val page = ArgumentMatchers.anyInt()
        val expectedResponse = ProductrevGetReviewImageResponse()

        onGetReviewImagesSuccess_thenReturn(expectedResponse)

        viewModel.setPage(page)
        viewModel.setProductId(ArgumentMatchers.anyString())

        verifyGetReviewImagesUseCaseExecuted()
        verifyReviewImagesSuccessEquals(Success(expectedResponse.productrevGetReviewImage))
    }

    @Test
    fun `when setPage should call getReviewImages and return expected error`() {
        val page = ArgumentMatchers.anyInt()
        val expectedResponse = Throwable()

        onGetReviewsImagesFail_thenReturn(expectedResponse)

        viewModel.setPage(page)
        viewModel.setProductId(ArgumentMatchers.anyString())

        verifyGetReviewImagesUseCaseExecuted()
        verifyReviewImagesErrorEquals(Fail(expectedResponse))
    }

    @Test
    fun `when getUserId should return expected user ID`() {
        val expectedUserId = ""
        val actualUserId = viewModel.getUserId()

        Assert.assertEquals(expectedUserId, actualUserId)
    }

    @Test
    fun `when getProductId should return expected product ID`() {
        val expectedUserId = ""
        val actualProductId = viewModel.getProductId()

        Assert.assertEquals(expectedUserId, actualProductId)
    }

    private fun onToggleLikeReviewSuccess_thenReturn(expectedResponse: ToggleLikeReviewResponse) {
        coEvery { toggleLikeReviewUseCase.executeOnBackground() } returns expectedResponse
    }

    private fun onToggleLikeReviewFail_thenReturn(throwable: Throwable) {
        coEvery { toggleLikeReviewUseCase.executeOnBackground() } throws throwable
    }

    private fun onGetReviewImagesSuccess_thenReturn(expectedResponse: ProductrevGetReviewImageResponse) {
        coEvery { getReviewImagesUseCase.executeOnBackground() } returns expectedResponse
    }

    private fun onGetReviewsImagesFail_thenReturn(throwable: Throwable) {
        coEvery { getReviewImagesUseCase.executeOnBackground() } throws throwable
    }

    private fun verifyToggleLikeDislikeUseCaseExecuted() {
        coVerify { toggleLikeReviewUseCase.executeOnBackground() }
    }

    private fun verifyGetReviewImagesUseCaseExecuted() {
        coVerify { getReviewImagesUseCase.executeOnBackground() }
    }

    private fun verifyToggleLikeReviewSuccessEquals(expectedSuccessValue: Success<ToggleProductReviewLike>) {
        viewModel.toggleLikeReview.verifySuccessEquals(expectedSuccessValue)
    }

    private fun verifyToggleLikeReviewErrorEquals(expectedErrorValue: Fail) {
        viewModel.toggleLikeReview.verifyErrorEquals(expectedErrorValue)
    }

    private fun verifyReviewImagesSuccessEquals(expectedSuccessValue: Success<ProductrevGetReviewImage>) {
        viewModel.reviewImages.verifySuccessEquals(expectedSuccessValue)
    }

    private fun verifyReviewImagesErrorEquals(expectedErrorValue: Fail) {
        viewModel.reviewImages.verifyErrorEquals(expectedErrorValue)
    }
}