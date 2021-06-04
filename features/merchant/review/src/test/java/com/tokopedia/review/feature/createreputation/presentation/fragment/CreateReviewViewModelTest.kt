package com.tokopedia.review.feature.createreputation.presentation.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.tokopedia.mediauploader.data.state.UploadResult
import com.tokopedia.review.common.data.ProductrevGetReviewDetail
import com.tokopedia.review.common.data.ProductrevGetReviewDetailResponseWrapper
import com.tokopedia.review.common.data.ProductrevGetReviewDetailReview
import com.tokopedia.review.common.data.ProductrevReviewAttachment
import com.tokopedia.review.feature.createreputation.domain.usecase.GetProductReputationForm
import com.tokopedia.review.feature.createreputation.model.*
import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewProgressBarState
import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain
import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoResponse
import com.tokopedia.review.utils.verifyReviewErrorEquals
import com.tokopedia.review.utils.verifyReviewSuccessEquals
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkObject
import org.junit.Assert
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.ArgumentMatchers.anyLong
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class CreateReviewViewModelTest : CreateReviewViewModelTestFixture() {

    @Test
    fun `when getSelectedImagesUrl should return expected list of Url`() {
        viewModel.clearImageData()
        viewModel.getImageList(images)
        val actualData = viewModel.getSelectedImagesUrl()
        val expectedData = arrayListOf("ImageUrl1", "ImageUrl2", "ImageUrl3", "ImageUrl4", "ImageUrl5")

        Assert.assertEquals(actualData, expectedData)
        assertTrue(viewModel.isImageNotEmpty())
    }

    @Test
    fun `when no images should return false`() {
        viewModel.clearImageData()
        assertFalse(viewModel.isImageNotEmpty())
    }

    @Test
    fun `when removeImage should return expected list ofUrl`() {
        val images = viewModel.getImageList(images)

        viewModel.removeImage(images.first())

        val actualData = viewModel.getSelectedImagesUrl()
        val expectedData = arrayListOf("ImageUrl2", "ImageUrl3", "ImageUrl4", "ImageUrl5")

        Assert.assertEquals(actualData, expectedData)
    }

    @Test
    fun `when removeImage in editMode should return expected list ofUrl`() {
        fillInImages()
        viewModel.getImageList(images)

        viewModel.removeImage(ImageReviewUiModel(images.first().thumbnail, images.first().fullSize), true)

        val actualData = viewModel.getSelectedImagesUrl()
        val expectedData = arrayListOf("ImageUrl2", "ImageUrl3", "ImageUrl4", "ImageUrl5")

        Assert.assertEquals(expectedData, actualData)
    }

    @Test
    fun `should success when get product reputation form`() {
        mockkObject(GetProductReputationForm)

        coEvery {
            getProductReputationForm.getReputationForm(GetProductReputationForm.createRequestParam(anyLong(), anyLong()))
        } returns ProductRevGetForm()

        viewModel.getProductReputation(anyLong(), anyLong())

        coVerify {
            getProductReputationForm.getReputationForm(GetProductReputationForm.createRequestParam(anyLong(), anyLong()))
        }

        assertTrue(viewModel.getReputationDataForm.observeAwaitValue() is Success)
    }

    @Test
    fun `should fail when get product reputation form`() {
        mockkObject(GetProductReputationForm)

        coEvery {
            getProductReputationForm.getReputationForm(GetProductReputationForm.createRequestParam(anyLong(), anyLong()))
        } throws Throwable()

        viewModel.getProductReputation(anyLong(), anyLong())

        coVerify {
            getProductReputationForm.getReputationForm(GetProductReputationForm.createRequestParam(anyLong(), anyLong()))
        }

        assertTrue(viewModel.getReputationDataForm.observeAwaitValue() is Fail)
    }

    @Test
    fun `should success when get product incentive ovo`() {
        coEvery {
            getProductIncentiveOvo.getIncentiveOvo()
        } returns ProductRevIncentiveOvoDomain(ProductRevIncentiveOvoResponse())

        viewModel.getProductIncentiveOvo()

        coVerify {
            getProductIncentiveOvo.getIncentiveOvo()
        }

        assertTrue(viewModel.isUserEligible())
        assertTrue(viewModel.incentiveOvo.observeAwaitValue() is Success)
        assertTrue(viewModel.getThankYouBottomSheetText().isEmpty())
    }

    @Test
    fun `should fail when get product incentive ovo`() {
        coEvery {
            getProductIncentiveOvo.getIncentiveOvo()
        } throws Throwable()

        viewModel.getProductIncentiveOvo()

        coVerify {
            getProductIncentiveOvo.getIncentiveOvo()
        }

        assertFalse(viewModel.isUserEligible())
        assertTrue(viewModel.incentiveOvo.observeAwaitValue() is Fail)
    }

    @Test
    fun `when getReviewDetails should execute expected use case and get expected data`() {
        val feedbackId = anyLong()
        val expectedResponse = ProductrevGetReviewDetailResponseWrapper()

        onGetReviewDetails_thenReturn(expectedResponse)

        viewModel.getReviewDetails(feedbackId)

        verifyGetReviewDetailUseCaseCalled()
        verifyReviewDetailsSuccess(com.tokopedia.review.common.data.Success(expectedResponse.productrevGetReviewDetail))
    }


    @Test
    fun `when getReviewDetails should execute expected use case and fail with expected exception`() {
        val feedbackId = anyLong()
        val expectedResponse = Throwable()

        onGetReviewDetailsFails_thenReturn(expectedResponse)

        viewModel.getReviewDetails(feedbackId)

        verifyGetReviewDetailUseCaseCalled()
        verifyReviewDetailsError(com.tokopedia.review.common.data.Fail(expectedResponse))
    }

    @Test
    fun `when getImageList of 5 images should return expected ImageReviewModels`() {
        val expectedData = mutableListOf(ImageReviewUiModel("ImageUrl1", "ImageUrl1"), ImageReviewUiModel("ImageUrl2", "ImageUrl2"), ImageReviewUiModel("ImageUrl3", "ImageUrl3"), ImageReviewUiModel("ImageUrl4", "ImageUrl4"), ImageReviewUiModel("ImageUrl5", "ImageUrl5"))

        val actualData = viewModel.getImageList(images)

        Assert.assertEquals(expectedData, actualData)
    }

    @Test
    fun `when getImageList of less than 5 images should return expected ImageReviewModels and DefaultImageReviewModel`() {
        val selectedImages = listOf(
                ProductrevReviewAttachment("ImageUrl1", "ImageUrl1"),
                ProductrevReviewAttachment("ImageUrl2", "ImageUrl2"),
                ProductrevReviewAttachment("ImageUrl3", "ImageUrl3"),
                ProductrevReviewAttachment("ImageUrl4", "ImageUrl4")
        )

        val expectedData = mutableListOf(ImageReviewUiModel("ImageUrl1", "ImageUrl1"), ImageReviewUiModel("ImageUrl2", "ImageUrl2"), ImageReviewUiModel("ImageUrl3", "ImageUrl3"), ImageReviewUiModel("ImageUrl4", "ImageUrl4"), DefaultImageReviewUiModel())

        val actualData = viewModel.getImageList(selectedImages)

        Assert.assertEquals(expectedData, actualData)
    }

    @Test
    fun `when getUserName should return valid userName`() {
        val actualUserId = viewModel.getUserName()
        assertTrue(actualUserId.isEmpty())
    }

    @Test
    fun `when submitReview with images results in back end error should return failure`() {
        val expectedResponse = ProductrevSubmitReviewResponseWrapper(ProductRevSuccessSubmitReview(success = false))
        val expectedUploadResponse = UploadResult.Success("success")

        onGetForm_thenReturn()
        onUploadImage_thenReturn(expectedUploadResponse)
        onSubmitReview_thenReturn(expectedResponse)

        viewModel.clearImageData()
        viewModel.getImageList(images)
        viewModel.getProductReputation(productId, reputationId)
        viewModel.submitReview(rating, review, reputationScore, isAnonymous, utmSource)

        verifySubmitReviewUseCaseCalled()
        expectedResponse.productrevSuccessIndicator?.let {
            verifySubmitReviewError(com.tokopedia.review.common.data.Fail(Throwable()))
        }
    }

    @Test
    fun `when submitReview with images results in network error should return failure`() {
        val expectedResponse = Throwable()
        val expectedUploadResponse = UploadResult.Success("success")

        onGetForm_thenReturn()
        onUploadImage_thenReturn(expectedUploadResponse)
        onSubmitReviewError_thenReturn(expectedResponse)

        viewModel.clearImageData()
        viewModel.getImageList(images)
        viewModel.getProductReputation(productId, reputationId)
        viewModel.submitReview(rating, review, reputationScore, isAnonymous, utmSource)

        verifySubmitReviewUseCaseCalled()
        verifySubmitReviewError(com.tokopedia.review.common.data.Fail(expectedResponse))
    }

    @Test
    fun `when submitReview with images results in upload error should return failure`() {
        val expectedResponse = Throwable()
        val expectedUploadResponse = UploadResult.Error("Network error")

        onGetForm_thenReturn()
        onUploadImage_thenReturn(expectedUploadResponse)
        onSubmitReviewError_thenReturn(expectedResponse)

        viewModel.clearImageData()
        viewModel.getImageList(images)
        viewModel.getProductReputation(productId, reputationId)
        viewModel.submitReview(rating, review, reputationScore, isAnonymous, utmSource)

        verifySubmitReviewError(com.tokopedia.review.common.data.Fail(expectedResponse))
    }

    @Test
    fun `when submitReview with no images should execute expected usecases`() {
        val expectedResponse = ProductrevSubmitReviewResponseWrapper(ProductRevSuccessSubmitReview(success = true, feedbackID = "feedbackId"))

        onGetForm_thenReturn()
        onSubmitReview_thenReturn(expectedResponse)

        viewModel.getProductReputation(productId, reputationId)
        viewModel.submitReview(rating, review, reputationScore, isAnonymous, utmSource)

        verifySubmitReviewUseCaseCalled()
        expectedResponse.productrevSuccessIndicator?.let {
            verifySubmitReviewSuccess(com.tokopedia.review.common.data.Success(it.feedbackID))
        }
    }

    @Test
    fun `when submitReview with no images results in back end error should return failure`() {
        val expectedResponse = ProductrevSubmitReviewResponseWrapper(ProductRevSuccessSubmitReview(success = false))

        onGetForm_thenReturn()
        onSubmitReview_thenReturn(expectedResponse)

        viewModel.getProductReputation(productId, reputationId)
        viewModel.submitReview(rating, review, reputationScore, isAnonymous, utmSource)

        verifySubmitReviewUseCaseCalled()
        expectedResponse.productrevSuccessIndicator?.let {
            verifySubmitReviewError(com.tokopedia.review.common.data.Fail(Throwable()))
        }
    }

    @Test
    fun `when submitReview with no images results in network error should return failure`() {
        val expectedResponse = Throwable()

        onGetForm_thenReturn()
        onSubmitReviewError_thenReturn(expectedResponse)

        viewModel.getProductReputation(productId, reputationId)
        viewModel.submitReview(rating, review, reputationScore, isAnonymous, utmSource)

        verifySubmitReviewUseCaseCalled()
        verifySubmitReviewError(com.tokopedia.review.common.data.Fail(expectedResponse))
    }

    @Test
    fun `when editReview with no images should execute expected usecases`() {
        val expectedResponse = ProductRevEditReviewResponseWrapper(ProductRevSuccessIndicator(success = true))

        onEditReview_thenReturn(expectedResponse)

        viewModel.editReview(feedbackID, reputationId, productId, shopId, reputationScore, rating, review, isAnonymous)

        verifyEditreviewUseCaseCalled()
        expectedResponse.productrevSuccessIndicator?.let {
            verifyEditReviewSuccess(com.tokopedia.review.common.data.Success(it.success))
        }
    }

    @Test
    fun `when editReview with no images results in back end error should return failure`() {
        val expectedResponse = ProductRevEditReviewResponseWrapper(ProductRevSuccessIndicator(success = false))

        onEditReview_thenReturn(expectedResponse)

        viewModel.editReview(feedbackID, reputationId, productId, shopId, reputationScore, rating, review, isAnonymous)

        verifyEditreviewUseCaseCalled()
        expectedResponse.productrevSuccessIndicator?.let {
            verifyEditReviewError(com.tokopedia.review.common.data.Fail(Throwable()))
        }
    }

    @Test
    fun `when editReview with no images results in network error should return failure`() {
        val expectedResponse = Throwable()

        onEditReviewError_thenReturn(expectedResponse)

        viewModel.editReview(feedbackID, reputationId, productId, shopId, reputationScore, rating, review, isAnonymous)

        verifyEditreviewUseCaseCalled()
        verifyEditReviewError(com.tokopedia.review.common.data.Fail(expectedResponse))
    }

    @Test
    fun `when getAfterEditImageList should add all images when 5 images`() {
        val imagePickerResult = mutableListOf("picture1", "picture2", "picture3", "picture4", "picture5")
        val originalImageUrl = mutableListOf("picture1", "picture2", "picture3", "picture4", "picture5")

        val expectedData = mutableListOf<BaseImageReviewUiModel>(ImageReviewUiModel("picture1"),
                ImageReviewUiModel("picture2"), ImageReviewUiModel("picture3"),
                ImageReviewUiModel("picture4"), ImageReviewUiModel("picture5"))

        val actualData = viewModel.getAfterEditImageList(imagePickerResult, originalImageUrl)

        Assert.assertEquals(expectedData, actualData)
    }

    @Test
    fun `when getAfterEditImageList should add images and add when DefaultImageReviewUiModel less than 5 images`() {
        val imagePickerResult = mutableListOf("picture1", "picture2", "picture3", "picture4")
        val originalImageUrl = mutableListOf("picture1", "picture2", "picture3", "picture4")

        val expectedData = mutableListOf(ImageReviewUiModel("picture1"),
                ImageReviewUiModel("picture2"), ImageReviewUiModel("picture3"),
                ImageReviewUiModel("picture4"), DefaultImageReviewUiModel())

        val actualData = viewModel.getAfterEditImageList(imagePickerResult, originalImageUrl)

        Assert.assertEquals(expectedData, actualData)
    }

    @Test
    fun `when editReview with images should execute expected usecases`() {
        val expectedResponse = ProductRevEditReviewResponseWrapper(ProductRevSuccessIndicator(success = true))
        val expectedUploadResponse = UploadResult.Success("success")

        onUploadImage_thenReturn(expectedUploadResponse)
        onEditReview_thenReturn(expectedResponse)

        fillInImages()
        viewModel.removeImage(ImageReviewUiModel(images.first().thumbnail, images.first().fullSize), true)
        viewModel.editReview(feedbackID, reputationId, productId, shopId, reputationScore, rating, review, isAnonymous)

        verifyEditreviewUseCaseCalled()
        expectedResponse.productrevSuccessIndicator?.let {
            verifyEditReviewSuccess(com.tokopedia.review.common.data.Success(it.success))
        }
    }

    @Test
    fun `when editReview with images results in back end error should return failure`() {
        val expectedResponse = ProductRevEditReviewResponseWrapper(ProductRevSuccessIndicator(success = false))
        val expectedUploadResponse = UploadResult.Success("success")

        onUploadImage_thenReturn(expectedUploadResponse)
        onEditReview_thenReturn(expectedResponse)

        fillInImages()
        viewModel.removeImage(ImageReviewUiModel(images.first().thumbnail, images.first().fullSize), true)
        viewModel.editReview(feedbackID, reputationId, productId, shopId, reputationScore, rating, review, isAnonymous)

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
        viewModel.editReview(feedbackID, reputationId, productId, shopId, reputationScore, rating, review, isAnonymous)

        verifyEditreviewUseCaseCalled()
        verifyEditReviewError(com.tokopedia.review.common.data.Fail(expectedResponse))
    }

    @Test
    fun `when getReviewTemplates should execute expected usecase`() {
        val expectedResponse = ProductrevGetReviewTemplateResponseWrapper()

        onGetReviewTemplate_thenReturn(expectedResponse)

        viewModel.getReviewTemplates(productId)

        verifyGetReviewTemplateUseCaseCalled()
        verifyReviewTemplatesSuccess(Success(expectedResponse.productrevGetPersonalizedReviewTemplate.templates))
        assertFalse(viewModel.isTemplateAvailable())
    }

    @Test
    fun `when getReviewTemplates error should still execute expected usecase and return error`() {
        val expectedResponse = Throwable()

        onGetReviewTemplateError_thenReturn(expectedResponse)

        viewModel.getReviewTemplates(productId)

        verifyGetReviewTemplateUseCaseCalled()
        verifyReviewTemplatesError(Fail(expectedResponse))
    }

    @Test
    fun `when updateProgressBarFromRating should only update isGoodRating`() {
        val isGoodRating = true
        val expectedProgressBarState = CreateReviewProgressBarState(isGoodRating = isGoodRating)

        viewModel.updateProgressBarFromRating(isGoodRating)

        verifyProgressBarValueEquals(expectedProgressBarState)
    }

    @Test
    fun `when updateProgressBarFromTextArea should only update isNotEmpty`() {
        val isTextAreaFilled = true
        val expectedProgressBarState = CreateReviewProgressBarState(isTextAreaFilled = isTextAreaFilled)

        viewModel.updateProgressBarFromTextArea(isTextAreaFilled)

        verifyProgressBarValueEquals(expectedProgressBarState)
    }

    @Test
    fun `when updateProgressBarFromPhotos should only update isPhotosFilled`() {
        val isPhotosFilled = true
        val expectedProgressBarState = CreateReviewProgressBarState(isPhotosFilled = isPhotosFilled)

        viewModel.clearImageData()
        viewModel.getImageList(images)
        viewModel.updateProgressBarFromPhotos()

        verifyProgressBarValueEquals(expectedProgressBarState)
    }

    @Test
    fun `when updateButtonState should update button state accordingly`() {
        val isEnabled = true

        viewModel.updateButtonState(isEnabled)

        viewModel.submitButtonState.verifyValueEquals(isEnabled)
    }

    @Test
    fun `when getUserId should get expected userId`() {
        val expectedUserId = "123124"
        every { userSession.userId } returns expectedUserId
        Assert.assertEquals(expectedUserId, viewModel.getUserId())
    }

    @Test
    fun `when getImageCount should get expected imageCount`() {
        val expectedImageCount = 5

        viewModel.clearImageData()
        viewModel.getImageList(images)

        Assert.assertEquals(expectedImageCount, viewModel.getImageCount())
    }

    private fun fillInImages() {
        val feedbackId = anyLong()
        val expectedReviewDetailResponse = ProductrevGetReviewDetailResponseWrapper(
                ProductrevGetReviewDetail(
                        review = ProductrevGetReviewDetailReview(
                                attachments = images
                        )
                )
        )

        onGetReviewDetails_thenReturn(expectedReviewDetailResponse)

        viewModel.getReviewDetails(feedbackId)

        verifyGetReviewDetailUseCaseCalled()
        verifyReviewDetailsSuccess(com.tokopedia.review.common.data.Success(expectedReviewDetailResponse.productrevGetReviewDetail))
    }

    private fun verifyGetReviewDetailUseCaseCalled() {
        coVerify { getReviewDetailUseCase.executeOnBackground() }
    }

    private fun verifySubmitReviewUseCaseCalled() {
        coVerify { submitReviewUseCase.executeOnBackground() }
    }

    private fun verifyEditreviewUseCaseCalled() {
        coVerify { editReviewUseCase.executeOnBackground() }
    }

    private fun verifyGetReviewTemplateUseCaseCalled() {
        coVerify { getReviewTemplatesUseCase.executeOnBackground() }
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

    private fun onGetReviewTemplate_thenReturn(response: ProductrevGetReviewTemplateResponseWrapper) {
        coEvery { getReviewTemplatesUseCase.executeOnBackground() } returns response
    }

    private fun onGetReviewTemplateError_thenReturn(throwable: Throwable) {
        coEvery { getReviewTemplatesUseCase.executeOnBackground() } throws throwable
    }

    private fun onGetForm_thenReturn() {
        coEvery {
            getProductReputationForm.getReputationForm(GetProductReputationForm.createRequestParam(anyLong(), anyLong()))
        } returns ProductRevGetForm()
    }

    private fun verifySubmitReviewSuccess(viewState: com.tokopedia.review.common.data.Success<String>) {
        viewModel.submitReviewResult.verifyReviewSuccessEquals(viewState)
    }

    private fun verifySubmitReviewError(viewState: com.tokopedia.review.common.data.Fail<Boolean>) {
        viewModel.submitReviewResult.verifyReviewErrorEquals(viewState)
    }

    private fun verifyEditReviewSuccess(viewState: com.tokopedia.review.common.data.Success<Boolean>) {
        viewModel.editReviewResult.verifyReviewSuccessEquals(viewState)
    }

    private fun verifyEditReviewError(viewState: com.tokopedia.review.common.data.Fail<Boolean>) {
        viewModel.editReviewResult.verifyReviewErrorEquals(viewState)
    }

    private fun verifyReviewTemplatesSuccess(reviewTemplates: Success<List<String>>) {
        viewModel.reviewTemplates.verifySuccessEquals(reviewTemplates)
    }

    private fun verifyReviewTemplatesError(error: Fail) {
        viewModel.reviewTemplates.verifyErrorEquals(error)
    }

    private fun onSubmitReview_thenReturn(response: ProductrevSubmitReviewResponseWrapper) {
        coEvery { submitReviewUseCase.executeOnBackground() } returns response
    }

    private fun onSubmitReviewError_thenReturn(throwable: Throwable) {
        coEvery { submitReviewUseCase.executeOnBackground() } throws throwable
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

    private fun verifyProgressBarValueEquals(progressBarState: CreateReviewProgressBarState) {
        viewModel.progressBarState.verifyValueEquals(progressBarState)
    }

    private fun <T> LiveData<T>.observeAwaitValue(): T? {
        var value: T? = null
        val latch = CountDownLatch(1)
        val observer = Observer<T> { t ->
            value = t
            latch.countDown()
        }
        observeForever(observer)
        latch.await(2, TimeUnit.SECONDS)
        return value
    }
}