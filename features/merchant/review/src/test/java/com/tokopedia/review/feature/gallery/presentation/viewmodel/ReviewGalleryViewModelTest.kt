package com.tokopedia.review.feature.gallery.presentation.viewmodel

import com.tokopedia.review.feature.gallery.data.ProductReviewRatingResponse
import com.tokopedia.review.feature.gallery.data.ProductrevGetReviewImage
import com.tokopedia.review.feature.gallery.data.ProductrevGetReviewImageResponse
import com.tokopedia.review.feature.reading.data.ProductRating
import com.tokopedia.review.feature.reading.data.ProductReviewDetail
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Assert
import org.junit.Test
import org.mockito.ArgumentMatchers

class ReviewGalleryViewModelTest : ReviewGalleryViewModelTestFixture() {

    @Test
    fun `when productId is not set should return empty`() {
        val expectedProductId = ""

        val actualProductId = viewModel.getProductId()

        Assert.assertEquals(expectedProductId, actualProductId)
    }

    @Test
    fun `when setProductId should call getRating and return expected results`() {
        val productId = ArgumentMatchers.anyString()
        val expectedResponse = ProductReviewRatingResponse(
            ProductRating(
                satisfactionRate = "90% pembeli merasa puas",
                detail = listOf(
                    ProductReviewDetail(5, "70", 70F),
                    ProductReviewDetail(4, "10", 10F),
                    ProductReviewDetail(3, "10", 10F),
                    ProductReviewDetail(2, "10", 10F),
                    ProductReviewDetail(1, "0", 0F),
                )
            )

        )

        onGetProductRatingSuccess_thenReturn(expectedResponse)

        viewModel.setProductId(productId)
        val actualProductId = viewModel.getProductId()

        verifyGetProductRatingAndTopicsUseCaseExecuted()
        verifyRatingSuccessEquals(Success(expectedResponse.productRating))
        Assert.assertEquals(productId, actualProductId)
    }

    @Test
    fun `when setProductId should call getRatingAndTopics and return expected error`() {
        val productId = ArgumentMatchers.anyString()
        val expectedResponse = Throwable()

        onGetProductRatingFail_thenReturn(expectedResponse)

        viewModel.setProductId(productId)

        verifyGetProductRatingAndTopicsUseCaseExecuted()
        verifyRatingErrorEquals(Fail(expectedResponse))
    }

    @Test
    fun `when setPage should call getProductReviews and return expected results`() {
        val page = ArgumentMatchers.anyInt()
        val productId = ArgumentMatchers.anyString()
        val expectedResponse = ProductrevGetReviewImageResponse()

        onGetReviewImagesSuccess_thenReturn(expectedResponse)

        viewModel.setProductId(productId)
        viewModel.setPage(page)

        Assert.assertEquals("", viewModel.getShopId())
        verifyGetReviewImagesUseCaseExecuted()
        verifyReviewImagesSuccessEquals(Success(expectedResponse.productrevGetReviewImage))
    }

    @Test
    fun `when setPage should call getReviewImages and return expected error`() {
        val page = ArgumentMatchers.anyInt()
        val productId = ArgumentMatchers.anyString()
        val expectedResponse = Throwable()

        onGetReviewsImagesFail_thenReturn(expectedResponse)

        viewModel.setProductId(productId)
        viewModel.setPage(page)

        Assert.assertEquals("", viewModel.getShopId())
        verifyGetReviewImagesUseCaseExecuted()
        verifyReviewImagesErrorEquals(Fail(expectedResponse))
    }

    @Test
    fun `when productId is null & call setPage should not call getReviewImages`() {
        val page = ArgumentMatchers.anyInt()

        viewModel.setPage(page)

        verifyGetReviewImagesUseCaseWasNotExecuted()
    }

    private fun onGetProductRatingSuccess_thenReturn(expectedResponse: ProductReviewRatingResponse) {
        coEvery { getProductRatingUseCase.executeOnBackground() } returns expectedResponse
    }

    private fun onGetProductRatingFail_thenReturn(throwable: Throwable) {
        coEvery { getProductRatingUseCase.executeOnBackground() } throws throwable
    }

    private fun onGetReviewImagesSuccess_thenReturn(expectedResponse: ProductrevGetReviewImageResponse) {
        coEvery { getReviewImagesUseCase.executeOnBackground() } returns expectedResponse
    }

    private fun onGetReviewsImagesFail_thenReturn(throwable: Throwable) {
        coEvery { getReviewImagesUseCase.executeOnBackground() } throws throwable
    }

    private fun verifyGetProductRatingAndTopicsUseCaseExecuted() {
        coVerify { getProductRatingUseCase.executeOnBackground() }
    }

    private fun verifyGetReviewImagesUseCaseExecuted() {
        coVerify { getReviewImagesUseCase.executeOnBackground() }
    }

    private fun verifyGetReviewImagesUseCaseWasNotExecuted() {
        coVerify { getReviewImagesUseCase.executeOnBackground() wasNot Called }
    }

    private fun verifyRatingSuccessEquals(expectedSuccessValue: Success<ProductRating>) {
        viewModel.rating.verifySuccessEquals(expectedSuccessValue)
    }

    private fun verifyRatingErrorEquals(expectedErrorValue: Fail) {
        viewModel.rating.verifyErrorEquals(expectedErrorValue)
    }

    private fun verifyReviewImagesSuccessEquals(expectedSuccessValue: Success<ProductrevGetReviewImage>) {
        viewModel.reviewImages.verifySuccessEquals(expectedSuccessValue)
    }

    private fun verifyReviewImagesErrorEquals(expectedErrorValue: Fail) {
        viewModel.reviewImages.verifyErrorEquals(expectedErrorValue)
    }
}