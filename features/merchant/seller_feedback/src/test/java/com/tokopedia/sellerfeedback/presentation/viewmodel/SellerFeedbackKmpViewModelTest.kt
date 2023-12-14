package com.tokopedia.sellerfeedback.presentation.viewmodel

import com.tokopedia.gql.Result
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller.feedback.data.param.FeedbackParam
import com.tokopedia.seller.feedback.domain.model.SubmitFeedbackModel
import com.tokopedia.sellerfeedback.data.SubmitResultKmp
import com.tokopedia.sellerfeedback.presentation.SellerFeedback
import com.tokopedia.sellerfeedback.presentation.uimodel.ImageFeedbackUiModel
import com.tokopedia.unit.test.ext.verifyValueEquals
import io.ktor.utils.io.errors.IOException
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import java.io.File

class SellerFeedbackKmpViewModelTest : SellerFeedbackViewKmpModelTestFixture() {

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

        val feedbackParam = FeedbackParam(
            shopId = anyLong(),
            score = anyString(),
            type = anyString(),
            page = anyString(),
            detail = anyString(),
            uploadId1 = anyString(),
            uploadId2 = anyString(),
            uploadId3 = anyString()
        )

        val sellerFeedback = SellerFeedback(
            feedbackScore = anyString(),
            feedbackType = anyString(),
            feedbackPage = anyString(),
            feedbackDetail = anyString(),
            uploadId1 = anyString(),
            uploadId2 = anyString(),
            uploadId3 = anyString()
        )

        val submitGlobalFeedback = SubmitFeedbackModel(
            state = "GOOD",
            isError = false,
            errorMessage = ""
        )

        val expected = Result.Success(submitGlobalFeedback)

        coEvery { uploaderUseCase(uploadParam1) } returns UploadResult.Success(anyString())
        coEvery { uploaderUseCase(uploadParam2) } returns UploadResult.Success(anyString())
        coEvery { uploaderUseCase(uploadParam3) } returns UploadResult.Success(anyString())

        coEvery { submitFeedbackUseCase.execute(feedbackParam) } returns expected

        viewModel.setImages(images)
        viewModel.submitFeedbackKmp(sellerFeedback)

        val expectedResult = (expected as? Result.Success)?.data
        val actualResult =
            (viewModel.getSubmitResultKmp().value as? SubmitResultKmp.SubmitFeedbackSuccess)?.submitFeedbackModel

        assertEquals(expectedResult, actualResult)

        coVerify { uploaderUseCase(uploadParam1) }
        coVerify { uploaderUseCase(uploadParam2) }
        coVerify { uploaderUseCase(uploadParam3) }
        coVerify { submitFeedbackUseCase.execute(feedbackParam) }
    }

    @Test
    fun `when upload image fail will return submit result upload fail`() {
        val imageUrl1 = anyString()
        val images = listOf(ImageFeedbackUiModel(imageUrl1))

        val sellerFeedback = SellerFeedback(
            feedbackScore = anyString(),
            feedbackType = anyString(),
            feedbackPage = anyString(),
            feedbackDetail = anyString(),
            uploadId1 = anyString(),
            uploadId2 = anyString(),
            uploadId3 = anyString()
        )

        val uploadParam1 = uploaderUseCase.createParams(
            sourceId = "ukrIHD",
            filePath = File(imageUrl1)
        )

        val errorMessage = anyString()
        coEvery { uploaderUseCase(uploadParam1) } returns UploadResult.Error(errorMessage)

        viewModel.setImages(images)
        viewModel.submitFeedbackKmp(sellerFeedback)

        val submitResult = viewModel.getSubmitResultKmp().value

        assertTrue(submitResult is SubmitResultKmp.UploadFail)
        assertEquals(errorMessage, (submitResult as SubmitResultKmp.UploadFail).cause.message)

        coVerify { uploaderUseCase(uploadParam1) }
    }

    @Test
    fun `when upload image success but submit feedback fail will return submit result submit fail`() {
        val imageUrl1 = anyString()
        val imageUrl2 = anyString()
        val imageUrl3 = anyString()
        val images = listOf(
            ImageFeedbackUiModel(imageUrl1),
            ImageFeedbackUiModel(imageUrl2),
            ImageFeedbackUiModel(imageUrl3)
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

        val feedbackParam = FeedbackParam(
            shopId = anyLong(),
            score = anyString(),
            type = anyString(),
            page = anyString(),
            detail = anyString(),
            uploadId1 = anyString(),
            uploadId2 = anyString(),
            uploadId3 = anyString()
        )

        val sellerFeedback = SellerFeedback(
            feedbackScore = anyString(),
            feedbackType = anyString(),
            feedbackPage = anyString(),
            feedbackDetail = anyString(),
            uploadId1 = anyString(),
            uploadId2 = anyString(),
            uploadId3 = anyString()
        )

        val submitGlobalFeedback = SubmitFeedbackModel(
            state = "GOOD",
            isError = true,
            errorMessage = "upload is always fail"
        )

        coEvery { uploaderUseCase(uploadParam1) } returns UploadResult.Success(anyString())
        coEvery { uploaderUseCase(uploadParam2) } returns UploadResult.Success(anyString())
        coEvery { uploaderUseCase(uploadParam3) } returns UploadResult.Success(anyString())

        coEvery { submitFeedbackUseCase.execute(feedbackParam) } returns Result.Success(
            submitGlobalFeedback
        )

        viewModel.setImages(images)
        viewModel.submitFeedbackKmp(sellerFeedback)

        val submitResult = viewModel.getSubmitResultKmp().value

        assertTrue(submitResult is SubmitResultKmp.SubmitFail)
        assertEquals(submitGlobalFeedback.errorMessage, (submitResult as SubmitResultKmp.SubmitFail).cause.message)

        coVerify { uploaderUseCase(uploadParam1) }
        coVerify { uploaderUseCase(uploadParam2) }
        coVerify { uploaderUseCase(uploadParam3) }

        coVerify { submitFeedbackUseCase.execute(feedbackParam) }
    }

    @Test
    fun `when upload image success but submit feedback unknown failure will return submit result submit fail`() {
        val errorMessage = "class cast exception"

        val errorException = MessageErrorException(errorMessage)

        val imageUrl1 = anyString()
        val imageUrl2 = anyString()
        val imageUrl3 = anyString()
        val images = listOf(
            ImageFeedbackUiModel(imageUrl1),
            ImageFeedbackUiModel(imageUrl2),
            ImageFeedbackUiModel(imageUrl3)
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

        val feedbackParam = FeedbackParam(
            shopId = anyLong(),
            score = anyString(),
            type = anyString(),
            page = anyString(),
            detail = anyString(),
            uploadId1 = anyString(),
            uploadId2 = anyString(),
            uploadId3 = anyString()
        )

        val sellerFeedback = SellerFeedback(
            feedbackScore = anyString(),
            feedbackType = anyString(),
            feedbackPage = anyString(),
            feedbackDetail = anyString(),
            uploadId1 = anyString(),
            uploadId2 = anyString(),
            uploadId3 = anyString()
        )

        coEvery { uploaderUseCase(uploadParam1) } returns UploadResult.Success(anyString())
        coEvery { uploaderUseCase(uploadParam2) } returns UploadResult.Success(anyString())
        coEvery { uploaderUseCase(uploadParam3) } returns UploadResult.Success(anyString())

        coEvery { submitFeedbackUseCase.execute(feedbackParam) } returns Result.Failure.UnknownFailure(
            errorException
        )

        viewModel.setImages(images)
        viewModel.submitFeedbackKmp(sellerFeedback)

        val submitResult = viewModel.getSubmitResultKmp().value

        assertTrue(submitResult is SubmitResultKmp.SubmitFail)
        assertEquals(errorMessage, (submitResult as SubmitResultKmp.SubmitFail).cause.localizedMessage)

        coVerify { uploaderUseCase(uploadParam1) }
        coVerify { uploaderUseCase(uploadParam2) }
        coVerify { uploaderUseCase(uploadParam3) }

        coVerify { submitFeedbackUseCase.execute(feedbackParam) }
    }

    @Test
    fun `when submit feedback throw unexpected error will return submit result fail network`() {
        val imageUrl1 = anyString()
        val imageUrl2 = anyString()
        val imageUrl3 = anyString()
        val images = listOf(
            ImageFeedbackUiModel(imageUrl1),
            ImageFeedbackUiModel(imageUrl2),
            ImageFeedbackUiModel(imageUrl3)
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

        val feedbackParam = FeedbackParam(
            shopId = anyLong(),
            score = anyString(),
            type = anyString(),
            page = anyString(),
            detail = anyString(),
            uploadId1 = anyString(),
            uploadId2 = anyString(),
            uploadId3 = anyString()
        )

        val sellerFeedback = SellerFeedback(
            feedbackScore = anyString(),
            feedbackType = anyString(),
            feedbackPage = anyString(),
            feedbackDetail = anyString(),
            uploadId1 = anyString(),
            uploadId2 = anyString(),
            uploadId3 = anyString()
        )

        coEvery { uploaderUseCase(uploadParam1) } returns UploadResult.Success(anyString())
        coEvery { uploaderUseCase(uploadParam2) } returns UploadResult.Success(anyString())
        coEvery { uploaderUseCase(uploadParam3) } returns UploadResult.Success(anyString())

        val errorMessage = "network failure"

        val throwable = IOException(errorMessage)

        coEvery { submitFeedbackUseCase.execute(feedbackParam) } returns Result.Failure.NetworkFailure(
            throwable
        )

        viewModel.setImages(images)
        viewModel.submitFeedbackKmp(sellerFeedback)

        val submitResult = viewModel.getSubmitResultKmp().value

        assertTrue(submitResult is SubmitResultKmp.NetworkFail)
        assertEquals(throwable.localizedMessage, (submitResult as SubmitResultKmp.NetworkFail).cause.cause?.localizedMessage)

        coVerify { uploaderUseCase(uploadParam1) }
        coVerify { submitFeedbackUseCase.execute(feedbackParam) }
    }

    @Test
    fun `when submit feedback throw message error exception will return submit result fail network`() {
        val imageUrl1 = anyString()
        val imageUrl2 = anyString()
        val imageUrl3 = anyString()
        val images = listOf(
            ImageFeedbackUiModel(imageUrl1),
            ImageFeedbackUiModel(imageUrl2),
            ImageFeedbackUiModel(imageUrl3)
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

        val feedbackParam = FeedbackParam(
            shopId = anyLong(),
            score = anyString(),
            type = anyString(),
            page = anyString(),
            detail = anyString(),
            uploadId1 = anyString(),
            uploadId2 = anyString(),
            uploadId3 = anyString()
        )

        val sellerFeedback = SellerFeedback(
            feedbackScore = anyString(),
            feedbackType = anyString(),
            feedbackPage = anyString(),
            feedbackDetail = anyString(),
            uploadId1 = anyString(),
            uploadId2 = anyString(),
            uploadId3 = anyString()
        )

        coEvery { uploaderUseCase(uploadParam1) } returns UploadResult.Success(anyString())
        coEvery { uploaderUseCase(uploadParam2) } returns UploadResult.Success(anyString())
        coEvery { uploaderUseCase(uploadParam3) } returns UploadResult.Success(anyString())

        val errorBody = "something went wrong"

        coEvery { submitFeedbackUseCase.execute(feedbackParam) } returns Result.Failure.HttpFailure(
            code = 403,
            errorBody = errorBody
        )

        viewModel.setImages(images)
        viewModel.submitFeedbackKmp(sellerFeedback)

        val submitResult = viewModel.getSubmitResultKmp().value

        assertTrue(submitResult is SubmitResultKmp.NetworkFail)
        assertEquals(errorBody, (submitResult as SubmitResultKmp.NetworkFail).cause.localizedMessage)

        coVerify { uploaderUseCase(uploadParam1) }
        coVerify { submitFeedbackUseCase.execute(feedbackParam) }
    }
}
