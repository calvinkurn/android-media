package com.tokopedia.report.view.viewmodel

import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.report.data.model.SubmitReportResponse
import com.tokopedia.report.data.model.SubmitReportResponseWrapper
import com.tokopedia.report.data.model.SubmitReportResult
import com.tokopedia.unit.test.ext.verifyValueEquals
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.Assert.assertTrue
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString

class ProductReportSubmitViewModelTest : ProductReportSubmitViewModelTestFixture() {

    @Test
    fun `when submitReport success should return expected result`() {
        val uploadParam1 = getUploadParam1()
        val uploadParam2 = getUploadParam2()

        val response = SubmitReportResponse("success")
        val wrapper = SubmitReportResponseWrapper(response)

        coEvery { submitReportUseCase.executeOnBackground() } returns wrapper

        coEvery { uploaderUseCase(uploadParam1) } returns UploadResult.Success("123")
        coEvery { uploaderUseCase(uploadParam2) } returns UploadResult.Success("456")

        viewModel.submitReport(anyLong(), anyInt(), reportInput)
        viewModel.getSubmitResult().verifyValueEquals(SubmitReportResult.Success(true))

        coVerify { uploaderUseCase(uploadParam1) }
        coVerify { uploaderUseCase(uploadParam2) }
        coVerify { submitReportUseCase.executeOnBackground() }
    }

    @Test
    fun `when submitReport fail due to upload image should return expected failure`() {
        val uploadParam1 = getUploadParam1()

        coEvery { uploaderUseCase(uploadParam1) } returns UploadResult.Error(anyString())

        viewModel.submitReport(anyLong(), anyInt(), reportInput)
        assertTrue(viewModel.getSubmitResult().value is SubmitReportResult.Fail)

        coVerify { uploaderUseCase(uploadParam1) }

    }

    @Test
    fun `when submitReport fail due to submit call should return expected failure`() {

        val uploadParam1 = getUploadParam1()
        val uploadParam2 = getUploadParam2()

        coEvery { uploaderUseCase(uploadParam1) } returns UploadResult.Success("123")
        coEvery { uploaderUseCase(uploadParam2) } returns UploadResult.Success("456")
        coEvery { submitReportUseCase.executeOnBackground() } throws Throwable()

        viewModel.submitReport(anyLong(), anyInt(), reportInput)

        assertTrue(viewModel.getSubmitResult().value is SubmitReportResult.Fail)

        coVerify { uploaderUseCase(uploadParam1) }
        coVerify { uploaderUseCase(uploadParam2) }
    }

    @Test
    fun `when upload input photos is empty submitReport can be success`() {

        val reportInputNoPhoto = reportInput.toMutableMap()
        reportInputNoPhoto.remove("photo")

        val response = SubmitReportResponse("success")
        val wrapper = SubmitReportResponseWrapper(response)

        coEvery { submitReportUseCase.executeOnBackground() } returns wrapper

        viewModel.submitReport(anyLong(), anyInt(), reportInputNoPhoto)
        viewModel.getSubmitResult().verifyValueEquals(SubmitReportResult.Success(true))

        coVerify(exactly = 0) { uploaderUseCase(any()) }

        coVerify { submitReportUseCase.executeOnBackground() }
    }

}