package com.tokopedia.review.feature.gallery.presentation.viewmodel

import com.tokopedia.review.feature.gallery.data.ProductReviewRatingResponse
import com.tokopedia.review.feature.reading.data.ProductRating
import com.tokopedia.review.feature.reading.data.ProductReviewDetail
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductRevGetDetailedReviewMediaResponse
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductrevGetReviewMedia
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.ext.verifyValueEquals
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
        val productId = ArgumentMatchers.anyString()

        onGetReviewImagesSuccess_thenReturn(getDetailedReviewMediaResult1stPage)

        viewModel.setProductId(productId)
        viewModel.setPage(DEFAULT_FIRST_PAGE)

        Assert.assertEquals("11530573", viewModel.getShopId())
        verifyGetReviewImagesUseCaseExecuted()
        verifyReviewImagesSuccessEquals(Success(getDetailedReviewMediaResult1stPage.productrevGetReviewMedia))
    }

    @Test
    fun `when setPage should call getReviewImages and return expected error`() {
        val productId = ArgumentMatchers.anyString()
        val expectedResponse = Throwable()

        onGetReviewsImagesFail_thenReturn(expectedResponse)

        viewModel.setProductId(productId)
        viewModel.setPage(DEFAULT_FIRST_PAGE)

        Assert.assertEquals("", viewModel.getShopId())
        verifyGetReviewImagesUseCaseExecuted()
        verifyReviewImagesErrorEquals(Fail(expectedResponse))
    }

    @Test
    fun `when productId is null & call setPage should not call getReviewImages`() {
        viewModel.setPage(DEFAULT_FIRST_PAGE)

        verifyGetReviewImagesUseCaseWasNotExecuted()
    }

    @Test
    fun `when load more should merge previous result with new result`() {
        val productId = ArgumentMatchers.anyString()
        val mergedReviewImages = getDetailedReviewMediaResult1stPage
            .productrevGetReviewMedia
            .reviewMedia
            .plus(getDetailedReviewMediaResult2ndPage.productrevGetReviewMedia.reviewMedia)
        val mergedReviewDetail = getDetailedReviewMediaResult1stPage
            .productrevGetReviewMedia
            .detail
            .reviewDetail
            .plus(getDetailedReviewMediaResult2ndPage.productrevGetReviewMedia.detail.reviewDetail)
        val mergedReviewGalleryImages = getDetailedReviewMediaResult1stPage
            .productrevGetReviewMedia
            .detail
            .reviewGalleryImages
            .plus(getDetailedReviewMediaResult2ndPage.productrevGetReviewMedia.detail.reviewGalleryImages)
        val mergedReviewGalleryVideos = getDetailedReviewMediaResult1stPage
            .productrevGetReviewMedia
            .detail
            .reviewGalleryVideos
            .plus(getDetailedReviewMediaResult2ndPage.productrevGetReviewMedia.detail.reviewGalleryVideos)
        val expected = getDetailedReviewMediaResult1stPage.productrevGetReviewMedia.copy(
            reviewMedia = mergedReviewImages,
            detail = getDetailedReviewMediaResult1stPage.productrevGetReviewMedia.detail.copy(
                reviewDetail = mergedReviewDetail,
                reviewGalleryImages = mergedReviewGalleryImages,
                reviewGalleryVideos = mergedReviewGalleryVideos,
                mediaCountFmt = getDetailedReviewMediaResult2ndPage.productrevGetReviewMedia.detail.mediaCountFmt,
                mediaCount = getDetailedReviewMediaResult2ndPage.productrevGetReviewMedia.detail.mediaCount
            ),
            hasNext = getDetailedReviewMediaResult2ndPage.productrevGetReviewMedia.hasNext,
            hasPrev = getDetailedReviewMediaResult1stPage.productrevGetReviewMedia.hasPrev
        )

        onGetReviewImagesSuccess_thenReturn(getDetailedReviewMediaResult1stPage)

        viewModel.setProductId(productId)
        viewModel.setPage(DEFAULT_FIRST_PAGE)

        onGetReviewImagesSuccess_thenReturn(getDetailedReviewMediaResult2ndPage)

        viewModel.setPage(DEFAULT_FIRST_PAGE.plus(1))

        verifyConcatenatedReviewImages(expected)
        Assert.assertEquals(773, viewModel.getMediaCount())
    }

    @Test
    fun `when load previous should merge previous result with new result`() {
        val productId = ArgumentMatchers.anyString()
        val mergedReviewImages = getDetailedReviewMediaResult1stPage
            .productrevGetReviewMedia
            .reviewMedia
            .plus(getDetailedReviewMediaResult2ndPage.productrevGetReviewMedia.reviewMedia)
        val mergedReviewDetail = getDetailedReviewMediaResult1stPage
            .productrevGetReviewMedia
            .detail
            .reviewDetail
            .plus(getDetailedReviewMediaResult2ndPage.productrevGetReviewMedia.detail.reviewDetail)
        val mergedReviewGalleryImages = getDetailedReviewMediaResult1stPage
            .productrevGetReviewMedia
            .detail
            .reviewGalleryImages
            .plus(getDetailedReviewMediaResult2ndPage.productrevGetReviewMedia.detail.reviewGalleryImages)
        val mergedReviewGalleryVideos = getDetailedReviewMediaResult1stPage
            .productrevGetReviewMedia
            .detail
            .reviewGalleryVideos
            .plus(getDetailedReviewMediaResult2ndPage.productrevGetReviewMedia.detail.reviewGalleryVideos)
        val expected = getDetailedReviewMediaResult1stPage.productrevGetReviewMedia.copy(
            reviewMedia = mergedReviewImages,
            detail = getDetailedReviewMediaResult1stPage.productrevGetReviewMedia.detail.copy(
                reviewDetail = mergedReviewDetail,
                reviewGalleryImages = mergedReviewGalleryImages,
                reviewGalleryVideos = mergedReviewGalleryVideos,
                mediaCountFmt = getDetailedReviewMediaResult2ndPage.productrevGetReviewMedia.detail.mediaCountFmt,
                mediaCount = getDetailedReviewMediaResult2ndPage.productrevGetReviewMedia.detail.mediaCount
            ),
            hasNext = getDetailedReviewMediaResult2ndPage.productrevGetReviewMedia.hasNext,
            hasPrev = getDetailedReviewMediaResult1stPage.productrevGetReviewMedia.hasPrev
        )

        onGetReviewImagesSuccess_thenReturn(getDetailedReviewMediaResult2ndPage)

        viewModel.setProductId(productId)
        viewModel.setPage(DEFAULT_FIRST_PAGE.plus(1))

        onGetReviewImagesSuccess_thenReturn(getDetailedReviewMediaResult1stPage)

        viewModel.setPage(DEFAULT_FIRST_PAGE)

        verifyConcatenatedReviewImages(expected)
        Assert.assertEquals(773, viewModel.getMediaCount())
    }

    private fun onGetProductRatingSuccess_thenReturn(expectedResponse: ProductReviewRatingResponse) {
        coEvery { getProductRatingUseCase.executeOnBackground() } returns expectedResponse
    }

    private fun onGetProductRatingFail_thenReturn(throwable: Throwable) {
        coEvery { getProductRatingUseCase.executeOnBackground() } throws throwable
    }

    private fun onGetReviewImagesSuccess_thenReturn(expectedResponse: ProductRevGetDetailedReviewMediaResponse) {
        coEvery { getDetailedReviewMediaUseCase.executeOnBackground() } returns expectedResponse
    }

    private fun onGetReviewsImagesFail_thenReturn(throwable: Throwable) {
        coEvery { getDetailedReviewMediaUseCase.executeOnBackground() } throws throwable
    }

    private fun verifyGetProductRatingAndTopicsUseCaseExecuted() {
        coVerify { getProductRatingUseCase.executeOnBackground() }
    }

    private fun verifyGetReviewImagesUseCaseExecuted() {
        coVerify { getDetailedReviewMediaUseCase.executeOnBackground() }
    }

    private fun verifyGetReviewImagesUseCaseWasNotExecuted() {
        coVerify { getDetailedReviewMediaUseCase.executeOnBackground() wasNot Called }
    }

    private fun verifyRatingSuccessEquals(expectedSuccessValue: Success<ProductRating>) {
        viewModel.rating.verifySuccessEquals(expectedSuccessValue)
    }

    private fun verifyRatingErrorEquals(expectedErrorValue: Fail) {
        viewModel.rating.verifyErrorEquals(expectedErrorValue)
    }

    private fun verifyReviewImagesSuccessEquals(expectedSuccessValue: Success<ProductrevGetReviewMedia>) {
        viewModel.reviewMedia.verifySuccessEquals(expectedSuccessValue)
    }

    private fun verifyReviewImagesErrorEquals(expectedErrorValue: Fail) {
        viewModel.reviewMedia.verifyErrorEquals(expectedErrorValue)
    }

    private fun verifyConcatenatedReviewImages(expectedValue: Any) {
        viewModel.concatenatedReviewImages.verifyValueEquals(expectedValue)
    }
}