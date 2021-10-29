package com.tokopedia.gallery.viewmodel

import com.tokopedia.gallery.networkmodel.ProductrevGetReviewImageResponse
import com.tokopedia.gallery.uimodel.GalleryData
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Test
import org.mockito.ArgumentMatchers

class GalleryViewModelTest : GalleryViewModelTestFixture() {

    @Test
    fun `when setPage should call getProductReviews and return expected results`() {
        val productId = ArgumentMatchers.anyString()
        val page = ArgumentMatchers.anyInt()
        val expectedResponse = ProductrevGetReviewImageResponse()
        val expectedLiveDataValue = GalleryData(listOf(), false)

        onGetReviewImagesSuccess_thenReturn(expectedResponse)

        viewModel.setPage(productId, page)

        verifyGetReviewImagesUseCaseExecuted()
        verifyReviewImagesSuccessEquals(Success(expectedLiveDataValue))
    }

    @Test
    fun `when setPage should call getReviewImages and return expected error`() {
        val productId = ArgumentMatchers.anyString()
        val page = ArgumentMatchers.anyInt()
        val expectedResponse = Throwable()

        onGetReviewsImagesFail_thenReturn(expectedResponse)

        viewModel.setPage(productId, page)

        verifyGetReviewImagesUseCaseExecuted()
        verifyReviewImagesErrorEquals(Fail(expectedResponse))
    }

    private fun onGetReviewImagesSuccess_thenReturn(expectedResponse: ProductrevGetReviewImageResponse) {
        coEvery { getReviewImagesUseCase.executeOnBackground() } returns expectedResponse
    }

    private fun onGetReviewsImagesFail_thenReturn(throwable: Throwable) {
        coEvery { getReviewImagesUseCase.executeOnBackground() } throws throwable
    }

    private fun verifyGetReviewImagesUseCaseExecuted() {
        coVerify { getReviewImagesUseCase.executeOnBackground() }
    }

    private fun verifyReviewImagesSuccessEquals(expectedSuccessValue: Success<GalleryData>) {
        viewModel.reviewImages.verifySuccessEquals(expectedSuccessValue)
    }

    private fun verifyReviewImagesErrorEquals(expectedErrorValue: Fail) {
        viewModel.reviewImages.verifyErrorEquals(expectedErrorValue)
    }
}