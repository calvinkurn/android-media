package com.tokopedia.report.view.viewmodel

import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.report.data.model.SubmitReportResponse
import com.tokopedia.report.data.model.SubmitReportResponseWrapper
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
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
        val productId = anyLong()
        val categoryId = anyInt()

        val uploadParam1 = getUploadParam1()
        val uploadParam2 = getUploadParam2()

        val fakeUploadId1 = "123"
        val fakeUploadId2 = "456"

        val response = SubmitReportResponse("success")
        val wrapper = SubmitReportResponseWrapper(response)

        coEvery { submitReportUseCase.executeOnBackground() } returns wrapper

        coEvery { uploaderUseCase(uploadParam1) } returns UploadResult.Success(fakeUploadId1)
        coEvery { uploaderUseCase(uploadParam2) } returns UploadResult.Success(fakeUploadId2)

        viewModel.submitReport(productId, categoryId, reportInput)

        viewModel.getSubmitResult().verifyValueEquals(Success(true))
        coVerify { uploaderUseCase(uploadParam1) }
        coVerify { uploaderUseCase(uploadParam2) }
        coVerify { submitReportUseCase.executeOnBackground() }

        val currentParams = viewModel.getCurrentParams()
        assert(currentParams != null)
        assert(currentParams?.productId == productId)
        assert(currentParams?.categoryId == categoryId)

        val uploadIds = currentParams?.fields?.get("upload_ids") as List<*>
        assert(uploadIds[0] == fakeUploadId1)
        assert(uploadIds[1] == fakeUploadId2)
    }

    @Test
    fun `when submitReport fail due to upload image should return expected failure`() {
        val uploadParam1 = getUploadParam1()

        coEvery { uploaderUseCase(uploadParam1) } returns UploadResult.Error(anyString())

        viewModel.submitReport(anyLong(), anyInt(), reportInput)

        assertTrue(viewModel.getSubmitResult().value is Fail)
        coVerify { uploaderUseCase(uploadParam1) }

    }

    @Test
    fun `when submitReport fail due to submit call should return expected failure`() {
        val productId = anyLong()
        val categoryId = anyInt()
        val fakeUploadId1 = "123"
        val fakeUploadId2 = "456"

        val uploadParam1 = getUploadParam1()
        val uploadParam2 = getUploadParam2()

        val expectedThrowable = Throwable()

        coEvery { uploaderUseCase(uploadParam1) } returns UploadResult.Success(fakeUploadId1)
        coEvery { uploaderUseCase(uploadParam2) } returns UploadResult.Success(fakeUploadId2)
        coEvery { submitReportUseCase.executeOnBackground() } throws expectedThrowable

        viewModel.submitReport(anyLong(), anyInt(), reportInput)

        viewModel.getSubmitResult().verifyValueEquals(Fail(expectedThrowable))

        val currentParams = viewModel.getCurrentParams()
        assert(currentParams != null)
        assert(currentParams?.productId == productId)
        assert(currentParams?.categoryId == categoryId)

        val uploadIds = currentParams?.fields?.get("upload_ids") as List<*>
        assert(uploadIds[0] == fakeUploadId1)
        assert(uploadIds[1] == fakeUploadId2)

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
        viewModel.getSubmitResult().verifyValueEquals(Success(true))

        coVerify(exactly = 0) { uploaderUseCase(any()) }
        coVerify { submitReportUseCase.executeOnBackground() }
    }

}