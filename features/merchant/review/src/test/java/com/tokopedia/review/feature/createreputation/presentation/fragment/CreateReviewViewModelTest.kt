package com.tokopedia.review.feature.createreputation.presentation.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.tokopedia.mediauploader.data.state.UploadResult
import com.tokopedia.review.common.data.ProductrevGetReviewDetail
import com.tokopedia.review.common.data.ProductrevGetReviewDetailResponseWrapper
import com.tokopedia.review.common.data.ProductrevReviewAttachment
import com.tokopedia.review.feature.createreputation.domain.usecase.GetProductReputationForm
import com.tokopedia.review.feature.createreputation.model.*
import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain
import com.tokopedia.review.utils.verifyErrorEquals
import com.tokopedia.review.utils.verifySuccessEquals
import com.tokopedia.review.utils.verifyValueEquals
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.ArgumentMatchers.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class  CreateReviewViewModelTest : CreateReviewViewModelTestFixture() {

    @Test
    fun `when getSelectedImagesUrl should return expected list of Url`() {
        val selectedImages = listOf(
                ProductrevReviewAttachment("ImageUrl1", "ImageUrl1"),
                ProductrevReviewAttachment("ImageUrl2", "ImageUrl2"),
                ProductrevReviewAttachment("ImageUrl3", "ImageUrl3"),
                ProductrevReviewAttachment("ImageUrl4", "ImageUrl4"),
                ProductrevReviewAttachment("ImageUrl5", "ImageUrl5")
        )

        viewModel.clearImageData()
        viewModel.getImageList(selectedImages)
        val actualData = viewModel.getSelectedImagesUrl()
        val expectedData = arrayListOf("ImageUrl1", "ImageUrl2", "ImageUrl3", "ImageUrl4", "ImageUrl5")

        Assert.assertEquals(actualData, expectedData)
    }

    @Test
    fun `when removeImage should return expected list ofUrl`() {
        val selectedImages = listOf(
                ProductrevReviewAttachment("ImageUrl1", "ImageUrl1"),
                ProductrevReviewAttachment("ImageUrl2", "ImageUrl2"),
                ProductrevReviewAttachment("ImageUrl3", "ImageUrl3"),
                ProductrevReviewAttachment("ImageUrl4", "ImageUrl4"),
                ProductrevReviewAttachment("ImageUrl5", "ImageUrl5")
        )
        val images = viewModel.getImageList(selectedImages)

        viewModel.removeImage(images.first())

        val actualData = viewModel.getSelectedImagesUrl()
        val expectedData = arrayListOf("ImageUrl2", "ImageUrl3", "ImageUrl4", "ImageUrl5")

        Assert.assertEquals(actualData, expectedData)
    }

    @Test
    fun `should success when get product reputation form`() {
        mockkObject(GetProductReputationForm)

        coEvery {
            getProductReputationForm.getReputationForm(GetProductReputationForm.createRequestParam(anyInt(), anyInt()))
        } returns ProductRevGetForm()

        viewModel.getProductReputation(anyInt(), anyInt())

        coVerify {
            getProductReputationForm.getReputationForm(GetProductReputationForm.createRequestParam(anyInt(), anyInt()))
        }

        assertTrue(viewModel.getReputationDataForm.observeAwaitValue() is Success)
    }

    @Test
    fun `should fail when get product reputation form`() {
        mockkObject(GetProductReputationForm)

        coEvery {
            getProductReputationForm.getReputationForm(GetProductReputationForm.createRequestParam(anyInt(), anyInt()))
        } throws Throwable()

        viewModel.getProductReputation(anyInt(), anyInt())

        coVerify {
            getProductReputationForm.getReputationForm(GetProductReputationForm.createRequestParam(anyInt(), anyInt()))
        }

        assertTrue(viewModel.getReputationDataForm.observeAwaitValue() is Fail)
    }

    @Test
    fun `should success when get product incentive ovo`() {
        coEvery {
            getProductIncentiveOvo.getIncentiveOvo()
        } returns ProductRevIncentiveOvoDomain()

        viewModel.getProductIncentiveOvo()

        coVerify {
            getProductIncentiveOvo.getIncentiveOvo()
        }

        assertTrue(viewModel.incentiveOvo.observeAwaitValue() is Success)
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

        assertTrue(viewModel.incentiveOvo.observeAwaitValue() is Fail)
    }

    @Test
    fun `when getReviewDetails should execute expected use case and get expected data`() {
        val feedbackId = anyInt()
        val expectedResponse = ProductrevGetReviewDetailResponseWrapper()

        onGetReviewDetails_thenReturn(expectedResponse)

        viewModel.getReviewDetails(feedbackId)

        verifyGetReviewDetailUseCaseCalled()
        verifyReviewDetailsSuccess(com.tokopedia.review.common.data.Success(expectedResponse.productrevGetReviewDetail))
    }


    @Test
    fun `when getReviewDetails should execute expected use case and fail with expected exception`() {
        val feedbackId = anyInt()
        val expectedResponse = Throwable()

        onGetReviewDetailsFails_thenReturn(expectedResponse)

        viewModel.getReviewDetails(feedbackId)

        verifyGetReviewDetailUseCaseCalled()
        verifyReviewDetailsError(com.tokopedia.review.common.data.Fail(expectedResponse))
    }

    @Test
    fun `when getImageList of 5 images should return expected ImageReviewModels`() {
        val selectedImages = listOf(
                ProductrevReviewAttachment("ImageUrl1", "ImageUrl1"),
                ProductrevReviewAttachment("ImageUrl2", "ImageUrl2"),
                ProductrevReviewAttachment("ImageUrl3", "ImageUrl3"),
                ProductrevReviewAttachment("ImageUrl4", "ImageUrl4"),
                ProductrevReviewAttachment("ImageUrl5", "ImageUrl5")
        )
        val expectedData = mutableListOf(ImageReviewUiModel("ImageUrl1", "ImageUrl1"), ImageReviewUiModel("ImageUrl2", "ImageUrl2"), ImageReviewUiModel("ImageUrl3", "ImageUrl3"), ImageReviewUiModel("ImageUrl4", "ImageUrl4"), ImageReviewUiModel("ImageUrl5", "ImageUrl5"))

        val actualData = viewModel.getImageList(selectedImages)

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

    private fun verifyGetReviewDetailUseCaseCalled() {
        coVerify { getReviewDetailUseCase.executeOnBackground() }
    }

    private fun verifySubmitReviewUseCaseCalled() {
        coVerify { submitReviewUseCase.executeOnBackground() }
    }

    private fun verifyImageUploaderUseCaseCalledBasedOnSizeOfList(size: Int) {
        coVerify(exactly = size) { uploaderUseCase(any()) }
    }

    private fun onGetReviewDetails_thenReturn(response: ProductrevGetReviewDetailResponseWrapper) {
        coEvery { getReviewDetailUseCase.executeOnBackground() } returns response
    }

    private fun onGetReviewDetailsFails_thenReturn(throwable: Throwable) {
        coEvery { getReviewDetailUseCase.executeOnBackground() } throws throwable
    }

    private fun verifyReviewDetailsSuccess(viewState: com.tokopedia.review.common.data.Success<ProductrevGetReviewDetail>) {
        viewModel.reviewDetails.verifySuccessEquals(viewState)
    }

    private fun verifyReviewDetailsError(viewState: com.tokopedia.review.common.data.Fail<ProductrevGetReviewDetail>) {
        viewModel.reviewDetails.verifyErrorEquals(viewState)
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