package com.tokopedia.sellerfeedback.presentation.viewmodel

import com.tokopedia.mediauploader.data.state.UploadResult
import com.tokopedia.sellerfeedback.data.SubmitGlobalFeedback
import com.tokopedia.sellerfeedback.data.SubmitGlobalFeedbackResponseWrapper
import com.tokopedia.sellerfeedback.data.SubmitResult
import com.tokopedia.sellerfeedback.presentation.SellerFeedback
import com.tokopedia.sellerfeedback.presentation.uimodel.ImageFeedbackUiModel
import com.tokopedia.unit.test.ext.verifyValueEquals
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import java.io.File

class SellerFeedbackViewModelTest : SellerFeedbackViewModelTestFixture() {

    @Test
    fun `when set images should update livedata feedbackImages`() {
        val images = listOf(ImageFeedbackUiModel())
        viewModel.setImages(images)
        viewModel.getFeedbackImages().verifyValueEquals(images)
    }

    @Test
    fun `when upload image and submit feedback success will return submit result success`() {
        val imageUrl1 = anyString()
        val imageUrl2 = anyString()
        val imageUrl3 = anyString()
        val images = listOf(
                ImageFeedbackUiModel(imageUrl1),
                ImageFeedbackUiModel(imageUrl2),
                ImageFeedbackUiModel(imageUrl3)
        )

        val sellerFeedback = SellerFeedback(
                feedbackScore = anyString(),
                feedbackType = anyString(),
                feedbackPage = anyString(),
                feedbackDetail = anyString()
        )

        val uploadParam1 = uploaderUseCase.createParams(
                sourceId = "ukrIHD",
                filePath = File(imageUrl1)
        )

        val uploadParam2 = uploaderUseCase.createParams(
                sourceId = "ukrIHD",
                filePath = File(imageUrl2)
        )

        val uploadParam3 = uploaderUseCase.createParams(
                sourceId = "ukrIHD",
                filePath = File(imageUrl3)
        )

        val submitGlobalFeedback = SubmitGlobalFeedback(error = false)
        val wrapper = SubmitGlobalFeedbackResponseWrapper(submitGlobalFeedback)

        coEvery { uploaderUseCase(uploadParam1) } returns UploadResult.Success(anyString())
        coEvery { uploaderUseCase(uploadParam2) } returns UploadResult.Success(anyString())
        coEvery { uploaderUseCase(uploadParam3) } returns UploadResult.Success(anyString())

        coEvery { submitGlobalFeedbackUseCase.executeOnBackground() } returns wrapper

        viewModel.setImages(images)
        viewModel.submitFeedback(sellerFeedback)
        viewModel.getSubmitResult().verifyValueEquals(SubmitResult.Success)

        coVerify { uploaderUseCase(uploadParam1) }
        coVerify { uploaderUseCase(uploadParam2) }
        coVerify { uploaderUseCase(uploadParam3) }
        coVerify { submitGlobalFeedbackUseCase.executeOnBackground() }
    }

    @Test
    fun `when upload image fail will return submit result upload fail`() {
        val imageUrl1 = anyString()
        val images = listOf(ImageFeedbackUiModel(imageUrl1))

        val sellerFeedback = SellerFeedback(
                feedbackScore = anyString(),
                feedbackType = anyString(),
                feedbackPage = anyString(),
                feedbackDetail = anyString()
        )

        val uploadParam1 = uploaderUseCase.createParams(
                sourceId = "ukrIHD",
                filePath = File(imageUrl1)
        )

        val errorMessage = anyString()
        coEvery { uploaderUseCase(uploadParam1) } returns UploadResult.Error(errorMessage)

        viewModel.setImages(images)
        viewModel.submitFeedback(sellerFeedback)
        viewModel.getSubmitResult().verifyValueEquals(SubmitResult.UploadFail(errorMessage))

        coVerify { uploaderUseCase(uploadParam1) }
    }

    @Test
    fun `when upload image success but submit feedback fail will return submit result submit fail`() {
        val imageUrl1 = anyString()
        val images = listOf(ImageFeedbackUiModel(imageUrl1))

        val sellerFeedback = SellerFeedback(
                feedbackScore = anyString(),
                feedbackType = anyString(),
                feedbackPage = anyString(),
                feedbackDetail = anyString()
        )

        val uploadParam1 = uploaderUseCase.createParams(
                sourceId = "ukrIHD",
                filePath = File(imageUrl1)
        )

        val errorMessage = anyString()
        val submitGlobalFeedback = SubmitGlobalFeedback(
                error = true,
                errorMsg = errorMessage
        )
        val wrapper = SubmitGlobalFeedbackResponseWrapper(submitGlobalFeedback)

        coEvery { uploaderUseCase(uploadParam1) } returns UploadResult.Success(anyString())

        coEvery { submitGlobalFeedbackUseCase.executeOnBackground() } returns wrapper

        viewModel.setImages(images)
        viewModel.submitFeedback(sellerFeedback)
        viewModel.getSubmitResult().verifyValueEquals(SubmitResult.SubmitFail(errorMessage))

        coVerify { uploaderUseCase(uploadParam1) }
        coVerify { submitGlobalFeedbackUseCase.executeOnBackground() }
    }

    @Test
    fun `when submit feedback throw unexpected error will return submit result fail network`() {
        val imageUrl1 = anyString()
        val images = listOf(ImageFeedbackUiModel(imageUrl1))

        val sellerFeedback = SellerFeedback(
                feedbackScore = anyString(),
                feedbackType = anyString(),
                feedbackPage = anyString(),
                feedbackDetail = anyString()
        )

        val uploadParam1 = uploaderUseCase.createParams(
                sourceId = "ukrIHD",
                filePath = File(imageUrl1)
        )

        coEvery { uploaderUseCase(uploadParam1) } returns UploadResult.Success(anyString())

        coEvery { submitGlobalFeedbackUseCase.executeOnBackground() } throws Throwable()

        viewModel.setImages(images)
        viewModel.submitFeedback(sellerFeedback)
        viewModel.getSubmitResult().verifyValueEquals(SubmitResult.NetworkFail)

        coVerify { uploaderUseCase(uploadParam1) }
        coVerify { submitGlobalFeedbackUseCase.executeOnBackground() }
    }

}