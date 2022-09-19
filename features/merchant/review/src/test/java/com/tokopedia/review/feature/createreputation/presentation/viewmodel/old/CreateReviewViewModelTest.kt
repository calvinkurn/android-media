package com.tokopedia.review.feature.createreputation.presentation.viewmodel.old

import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.review.common.data.ProductrevGetReviewDetail
import com.tokopedia.review.common.data.ProductrevGetReviewDetailResponseWrapper
import com.tokopedia.review.common.data.ProductrevGetReviewDetailReview
import com.tokopedia.review.common.data.ProductrevReviewImageAttachment
import com.tokopedia.review.feature.createreputation.model.BaseImageReviewUiModel
import com.tokopedia.review.feature.createreputation.model.DefaultImageReviewUiModel
import com.tokopedia.review.feature.createreputation.model.ImageReviewUiModel
import com.tokopedia.review.feature.createreputation.model.ProductRevEditReviewResponseWrapper
import com.tokopedia.review.feature.createreputation.model.ProductRevSuccessIndicator
import com.tokopedia.review.utils.verifyReviewErrorEquals
import com.tokopedia.review.utils.verifyReviewSuccessEquals
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyString

class CreateReviewViewModelTest : CreateReviewViewModelTestFixture() {

    @Test
    fun `when getSelectedImagesUrl should return expected list of Url`() {
        viewModel.clearImageData()
        viewModel.getImageList(images, emptyList())
        val actualData = viewModel.getSelectedImagesUrl()
        val expectedData =
            arrayListOf("ImageUrl1", "ImageUrl2", "ImageUrl3", "ImageUrl4", "ImageUrl5")

        Assert.assertEquals(actualData, expectedData)
        assertTrue(viewModel.isImageNotEmpty())
    }

    @Test
    fun `when no images should return false and return 0 count`() {
        val expectedImageCount = 0

        viewModel.clearImageData()

        assertFalse(viewModel.isImageNotEmpty())
        assertEquals(viewModel.getMediaCount(), expectedImageCount)
    }

    @Test
    fun `when removeImage should return expected list ofUrl`() {
        val images = viewModel.getImageList(images, emptyList())

        viewModel.removeImage(images.first() as BaseImageReviewUiModel)

        val actualData = viewModel.getSelectedImagesUrl()
        val expectedData = arrayListOf("ImageUrl2", "ImageUrl3", "ImageUrl4", "ImageUrl5")

        Assert.assertEquals(actualData, expectedData)
    }

    @Test
    fun `when removeImage in editMode should return expected list ofUrl`() {
        fillInImages()
        viewModel.getImageList(images, emptyList())

        viewModel.removeImage(ImageReviewUiModel(images.first().thumbnail, images.first().fullSize))

        val actualData = viewModel.getSelectedImagesUrl()
        val expectedData = arrayListOf("ImageUrl2", "ImageUrl3", "ImageUrl4", "ImageUrl5")

        Assert.assertEquals(expectedData, actualData)
    }

    @Test
    fun `when getReviewDetails should execute expected use case and get expected data`() {
        val feedbackId = anyString()
        val expectedResponse = ProductrevGetReviewDetailResponseWrapper()

        onGetReviewDetails_thenReturn(expectedResponse)

        viewModel.getReviewDetails(feedbackId)

        verifyGetReviewDetailUseCaseCalled()
        verifyReviewDetailsSuccess(com.tokopedia.review.common.data.Success(expectedResponse.productrevGetReviewDetail))
    }


    @Test
    fun `when getReviewDetails should execute expected use case and fail with expected exception`() {
        val feedbackId = anyString()
        val expectedResponse = Throwable()

        onGetReviewDetailsFails_thenReturn(expectedResponse)

        viewModel.getReviewDetails(feedbackId)

        verifyGetReviewDetailUseCaseCalled()
        verifyReviewDetailsError(com.tokopedia.review.common.data.Fail(expectedResponse))
    }

    @Test
    fun `when getImageList of 5 images should return expected ImageReviewModels`() {
        val expectedData = mutableListOf(
            ImageReviewUiModel("", "ImageUrl1"),
            ImageReviewUiModel("", "ImageUrl2"),
            ImageReviewUiModel("", "ImageUrl3"),
            ImageReviewUiModel("", "ImageUrl4"),
            ImageReviewUiModel("", "ImageUrl5")
        )

        val actualData = viewModel.getImageList(images, emptyList())

        Assert.assertEquals(expectedData, actualData)
    }

    @Test
    fun `when getImageList of less than 5 images should return expected ImageReviewModels and DefaultImageReviewModel`() {
        val selectedImages = listOf(
            ProductrevReviewImageAttachment("ImageUrl1", "ImageUrl1"),
            ProductrevReviewImageAttachment("ImageUrl2", "ImageUrl2"),
            ProductrevReviewImageAttachment("ImageUrl3", "ImageUrl3"),
            ProductrevReviewImageAttachment("ImageUrl4", "ImageUrl4")
        )

        val expectedData = mutableListOf(
            ImageReviewUiModel("", "ImageUrl1"),
            ImageReviewUiModel("", "ImageUrl2"),
            ImageReviewUiModel("", "ImageUrl3"),
            ImageReviewUiModel("", "ImageUrl4"),
            DefaultImageReviewUiModel()
        )

        val actualData = viewModel.getImageList(selectedImages, emptyList())

        Assert.assertEquals(expectedData, actualData)
    }

    @Test
    fun `when getUserName should return valid userName`() {
        val actualUserId = viewModel.getUserName()
        assertTrue(actualUserId.isEmpty())
    }

    @Test
    fun `when editReview with no images should execute expected usecases`() {
        val expectedResponse =
            ProductRevEditReviewResponseWrapper(ProductRevSuccessIndicator(success = true))

        onEditReview_thenReturn(expectedResponse)

        viewModel.editReview(
            feedbackID,
            reputationId,
            productId,
            shopId,
            reputationScore,
            rating,
            review,
            isAnonymous
        )

        verifyEditreviewUseCaseCalled()
        expectedResponse.productrevSuccessIndicator?.let {
            verifyEditReviewSuccess(com.tokopedia.review.common.data.Success(it.success))
        }
    }

    @Test
    fun `when editReview with no images results in back end error should return failure`() {
        val expectedResponse =
            ProductRevEditReviewResponseWrapper(ProductRevSuccessIndicator(success = false))

        onEditReview_thenReturn(expectedResponse)

        viewModel.editReview(
            feedbackID,
            reputationId,
            productId,
            shopId,
            reputationScore,
            rating,
            review,
            isAnonymous
        )

        verifyEditreviewUseCaseCalled()
        expectedResponse.productrevSuccessIndicator?.let {
            verifyEditReviewError(com.tokopedia.review.common.data.Fail(Throwable()))
        }
    }

    @Test
    fun `when editReview with no images results in network error should return failure`() {
        val expectedResponse = Throwable()

        onEditReviewError_thenReturn(expectedResponse)

        viewModel.editReview(
            feedbackID,
            reputationId,
            productId,
            shopId,
            reputationScore,
            rating,
            review,
            isAnonymous
        )

        verifyEditreviewUseCaseCalled()
        verifyEditReviewError(com.tokopedia.review.common.data.Fail(expectedResponse))
    }

    @Test
    fun `when editReview with images results in upload error should return failure`() {
        val imagePickerResult = mutableListOf("picture1")
        val originalImageUrl = mutableListOf<String>()
        val expectedResponse = Throwable()
        val expectedUploadResponse = UploadResult.Error("Network error")
        val expectedReviewDetailResponse = ProductrevGetReviewDetailResponseWrapper(
            ProductrevGetReviewDetail(
                review = ProductrevGetReviewDetailReview(videoAttachments = videos)
            )
        )

        onGetReviewDetails_thenReturn(expectedReviewDetailResponse)
        onUploadImage_thenReturn(expectedUploadResponse)
        onEditReviewError_thenReturn(expectedResponse)

        viewModel.getReviewDetails(feedbackID)
        viewModel.getAfterEditImageList(imagePickerResult, originalImageUrl)
        viewModel.editReview(
            feedbackID,
            reputationId,
            productId,
            shopId,
            reputationScore,
            rating,
            review,
            isAnonymous
        )

        verifyEditReviewError(com.tokopedia.review.common.data.Fail(expectedResponse))
    }

    @Test
    fun `when getAfterEditImageList should add all images when 5 images`() {
        val imagePickerResult =
            mutableListOf("picture1", "picture2", "picture3", "picture4", "picture5")
        val originalImageUrl =
            mutableListOf("picture1", "picture2", "picture3", "picture4", "picture5")

        val expectedData = mutableListOf<BaseImageReviewUiModel>(
            ImageReviewUiModel("picture1"),
            ImageReviewUiModel("picture2"), ImageReviewUiModel("picture3"),
            ImageReviewUiModel("picture4"), ImageReviewUiModel("picture5")
        )

        val actualData = viewModel.getAfterEditImageList(imagePickerResult, originalImageUrl)

        Assert.assertEquals(expectedData, actualData)
    }

    @Test
    fun `when getAfterEditImageList should add images and add when DefaultImageReviewUiModel less than 5 images`() {
        val imagePickerResult = mutableListOf("picture1", "picture2", "picture3", "picture4")
        val originalImageUrl = mutableListOf("picture1", "picture2", "picture3", "picture4")

        val expectedData = mutableListOf(
            ImageReviewUiModel("picture1"),
            ImageReviewUiModel("picture2"), ImageReviewUiModel("picture3"),
            ImageReviewUiModel("picture4"), DefaultImageReviewUiModel()
        )

        val actualData = viewModel.getAfterEditImageList(imagePickerResult, originalImageUrl)

        Assert.assertEquals(expectedData, actualData)
    }

    @Test
    fun `when getAfterEditImageList contains image from storage should add all images`() {
        val imagePickerResult =
            mutableListOf("picture1", "picture2", "picture3", "picture4", "storage/pic")
        val originalImageUrl = mutableListOf("picture1", "picture2", "picture3", "picture4")

        val expectedData = mutableListOf(
            ImageReviewUiModel("picture1"),
            ImageReviewUiModel("picture2"), ImageReviewUiModel("picture3"),
            ImageReviewUiModel("picture4"), ImageReviewUiModel("storage/pic")
        )

        val actualData = viewModel.getAfterEditImageList(imagePickerResult, originalImageUrl)

        Assert.assertEquals(expectedData, actualData)
    }

    @Test
    fun `when getAfterEditImageList contains cached image from cloud should replace it with real image url`() {
        val imagePickerResult = mutableListOf("picture1.0", "picture2.jpg")
        val imagesFedIntoPicker = mutableListOf("https://example.com/picture1.jpg", "https://example.com/picture1.jpg")

        val expectedData = mutableListOf(
            ImageReviewUiModel("https://example.com/picture1.jpg"),
            ImageReviewUiModel("picture2.jpg"),
            DefaultImageReviewUiModel()
        )

        val actualData = viewModel.getAfterEditImageList(imagePickerResult, imagesFedIntoPicker)

        assertEquals(expectedData, actualData)
    }

    @Test
    fun `when editReview with images should execute expected usecases`() {
        val expectedResponse =
            ProductRevEditReviewResponseWrapper(ProductRevSuccessIndicator(success = true))
        val expectedUploadResponse = UploadResult.Success("success")

        onUploadImage_thenReturn(expectedUploadResponse)
        onEditReview_thenReturn(expectedResponse)

        fillInImages()
        viewModel.removeImage(ImageReviewUiModel(images.first().thumbnail, images.first().fullSize))
        viewModel.editReview(
            feedbackID,
            reputationId,
            productId,
            shopId,
            reputationScore,
            rating,
            review,
            isAnonymous
        )

        verifyEditreviewUseCaseCalled()
        expectedResponse.productrevSuccessIndicator?.let {
            verifyEditReviewSuccess(com.tokopedia.review.common.data.Success(it.success))
        }
    }

    @Test
    fun `when editReview with images results in back end error should return failure`() {
        val expectedResponse =
            ProductRevEditReviewResponseWrapper(ProductRevSuccessIndicator(success = false))
        val expectedUploadResponse = UploadResult.Success("success")

        onUploadImage_thenReturn(expectedUploadResponse)
        onEditReview_thenReturn(expectedResponse)

        fillInImages()
        viewModel.removeImage(ImageReviewUiModel(images.first().thumbnail, images.first().fullSize))
        viewModel.editReview(
            feedbackID,
            reputationId,
            productId,
            shopId,
            reputationScore,
            rating,
            review,
            isAnonymous
        )

        verifyEditreviewUseCaseCalled()
        expectedResponse.productrevSuccessIndicator?.let {
            verifyEditReviewError(com.tokopedia.review.common.data.Fail(Throwable()))
        }
    }

    @Test
    fun `when editReview wit images results in network error should return failure`() {
        val expectedResponse = Throwable()

        onEditReviewError_thenReturn(expectedResponse)

        fillInImages()
        viewModel.editReview(
            feedbackID,
            reputationId,
            productId,
            shopId,
            reputationScore,
            rating,
            review,
            isAnonymous
        )

        verifyEditreviewUseCaseCalled()
        verifyEditReviewError(com.tokopedia.review.common.data.Fail(expectedResponse))
    }

    @Test
    fun `when getUserId should get expected userId`() {
        val expectedUserId = "123124"
        every { userSession.userId } returns expectedUserId
        Assert.assertEquals(expectedUserId, viewModel.getUserId())
    }

    @Test
    fun `when getMediaCount should get expected mediaCount`() {
        val expectedMediaCount = 5

        viewModel.clearImageData()
        viewModel.getImageList(images.take(4), videos)

        Assert.assertEquals(expectedMediaCount, viewModel.getMediaCount())
    }

    @Test
    fun `when getMediaCount contains only one ImageReviewUiModel should return 1`() {
        val expectedImageCount = 1

        viewModel.clearImageData()
        viewModel.getImageList(listOf(images.first()), emptyList())

        Assert.assertEquals(expectedImageCount, viewModel.getMediaCount())
    }

    @Test
    fun `when existing uploaded video removed then edit review should not send existing video`() {
        val expectedReviewDetailResponse = ProductrevGetReviewDetailResponseWrapper(
            ProductrevGetReviewDetail(
                review = ProductrevGetReviewDetailReview(videoAttachments = videos)
            )
        )
        val expectedEditReviewResponse = ProductRevEditReviewResponseWrapper(
            ProductRevSuccessIndicator(success = true)
        )

        onGetReviewDetails_thenReturn(expectedReviewDetailResponse)
        onEditReview_thenReturn(expectedEditReviewResponse)

        viewModel.getReviewDetails(feedbackID)
        viewModel.removeVideo()
        viewModel.editReview(
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyInt(),
            ArgumentMatchers.anyInt(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyBoolean()
        )

        verifyEditReviewParams(
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyInt(),
            ArgumentMatchers.anyInt(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyBoolean(),
            ArgumentMatchers.anyList(),
            ArgumentMatchers.anyList(),
            emptyList(),
        )
    }

    @Test
    fun `when edit review with image and existing uploaded video not removed then edit review should send existing video`() {
        val imagePickerResult = mutableListOf("picture1")
        val originalImageUrl = mutableListOf<String>()
        val expectedReviewDetailResponse = ProductrevGetReviewDetailResponseWrapper(
            ProductrevGetReviewDetail(
                review = ProductrevGetReviewDetailReview(videoAttachments = videos)
            )
        )
        val expectedEditReviewResponse = ProductRevEditReviewResponseWrapper(
            ProductRevSuccessIndicator(success = true)
        )
        onGetReviewDetails_thenReturn(expectedReviewDetailResponse)
        onUploadImage_thenReturn(UploadResult.Success(ArgumentMatchers.anyString()))
        onEditReview_thenReturn(expectedEditReviewResponse)
        viewModel.getReviewDetails(feedbackID)
        viewModel.getAfterEditImageList(imagePickerResult, originalImageUrl)
        viewModel.editReview(
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyInt(),
            ArgumentMatchers.anyInt(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyBoolean()
        )
        verifyEditReviewParams(
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyInt(),
            ArgumentMatchers.anyInt(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyBoolean(),
            ArgumentMatchers.anyList(),
            listOf(ArgumentMatchers.anyString()),
            videos.map { it.url!! },
        )
    }

    @Test
    fun `getMaxImagePickCount should return 4 when contain uploaded video`() {
        val expectedReviewDetailResponse = ProductrevGetReviewDetailResponseWrapper(
            ProductrevGetReviewDetail(
                review = ProductrevGetReviewDetailReview(videoAttachments = videos)
            )
        )
        onGetReviewDetails_thenReturn(expectedReviewDetailResponse)
        viewModel.getReviewDetails(feedbackID)
        Assert.assertEquals(4, viewModel.getMaxImagePickCount())
    }

    @Test
    fun `getMaxImagePickCount should return 5 when doesn't contain uploaded video`() {
        val expectedReviewDetailResponse = ProductrevGetReviewDetailResponseWrapper()
        onGetReviewDetails_thenReturn(expectedReviewDetailResponse)
        viewModel.getReviewDetails(feedbackID)
        Assert.assertEquals(5, viewModel.getMaxImagePickCount())
    }

    private fun fillInImages() {
        val feedbackId = anyString()
        val expectedReviewDetailResponse = ProductrevGetReviewDetailResponseWrapper(
            ProductrevGetReviewDetail(
                review =
                ProductrevGetReviewDetailReview(
                    imageAttachments = images
                )
            )
        )

        onGetReviewDetails_thenReturn(expectedReviewDetailResponse)

        viewModel.getReviewDetails(feedbackId)

        verifyGetReviewDetailUseCaseCalled()
        verifyReviewDetailsSuccess(
            com.tokopedia.review.common.data.Success(
                expectedReviewDetailResponse.productrevGetReviewDetail
            )
        )
    }

    private fun verifyGetReviewDetailUseCaseCalled() {
        coVerify { getReviewDetailUseCase.executeOnBackground() }
    }

    private fun verifyEditreviewUseCaseCalled() {
        coVerify { editReviewUseCase.executeOnBackground() }
    }

    private fun onGetReviewDetails_thenReturn(response: ProductrevGetReviewDetailResponseWrapper) {
        coEvery { getReviewDetailUseCase.executeOnBackground() } returns response
    }

    private fun onGetReviewDetailsFails_thenReturn(throwable: Throwable) {
        coEvery { getReviewDetailUseCase.executeOnBackground() } throws throwable
    }

    private fun onUploadImage_thenReturn(response: UploadResult) {
        coEvery { uploaderUseCase.invoke(any()) } returns response
    }

    private fun verifyEditReviewSuccess(viewState: com.tokopedia.review.common.data.Success<Boolean>) {
        viewModel.editReviewResult.verifyReviewSuccessEquals(viewState)
    }

    private fun verifyEditReviewError(viewState: com.tokopedia.review.common.data.Fail<Boolean>) {
        viewModel.editReviewResult.verifyReviewErrorEquals(viewState)
    }

    private fun verifyEditReviewParams(
        feedbackId: String,
        reputationId: String,
        productId: String,
        shopId: String,
        reputationScore: Int = 0,
        rating: Int,
        reviewText: String,
        isAnonymous: Boolean,
        oldAttachmentUrls: List<String> = emptyList(),
        attachmentIds: List<String> = emptyList(),
        oldVideoAttachmentUrls: List<String> = emptyList()
    ) {
        coVerify {
            editReviewUseCase.setParams(
                feedbackId,
                reputationId,
                productId,
                shopId,
                reputationScore,
                rating,
                reviewText,
                isAnonymous,
                oldAttachmentUrls,
                attachmentIds,
                oldVideoAttachmentUrls
            )
        }
    }

    private fun onEditReview_thenReturn(response: ProductRevEditReviewResponseWrapper) {
        coEvery { editReviewUseCase.executeOnBackground() } returns response
    }

    private fun onEditReviewError_thenReturn(throwable: Throwable) {
        coEvery { editReviewUseCase.executeOnBackground() } throws throwable
    }

    private fun verifyReviewDetailsSuccess(viewState: com.tokopedia.review.common.data.Success<ProductrevGetReviewDetail>) {
        viewModel.reviewDetails.verifyReviewSuccessEquals(viewState)
    }

    private fun verifyReviewDetailsError(viewState: com.tokopedia.review.common.data.Fail<ProductrevGetReviewDetail>) {
        viewModel.reviewDetails.verifyReviewErrorEquals(viewState)
    }
}