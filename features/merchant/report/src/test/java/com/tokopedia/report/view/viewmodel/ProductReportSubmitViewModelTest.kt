package com.tokopedia.report.view.viewmodel

import com.tokopedia.mediauploader.data.state.UploadResult
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import rx.observers.TestSubscriber

class ProductReportSubmitViewModelTest : ProductReportSubmitViewModelTestFixture() {

    @Test
    fun `when submitReport success should return expected result`() {
        val expectedReturn = mockk<Boolean>(relaxed = true)
        val testSubscriber: TestSubscriber<Boolean> = TestSubscriber()

        coEvery {
            submitReportUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedReturn)
        }

        onUploadImage_thenReturn("photo")

        val onSuccess: (Boolean) -> Unit = mockk()
        val onError: (Throwable?) -> Unit = mockk()

        viewModel.submitReport(anyInt(), anyInt(), reportInput, onSuccess, onError)

        verifySubmitReportUseCaseCalled()

        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedReturn)
        testSubscriber.assertCompleted()
    }

    @Test
    fun `when submitReport fail due to upload image should return expected failure`() {
        onUploadImageError_thenReturn(anyString())

        val onSuccess: (Boolean) -> Unit = mockk()
        val onError: (Throwable?) -> Unit = mockk()

        viewModel.submitReport(anyInt(), anyInt(), reportInput, onSuccess, onError)

        verify { onError.invoke(any()) }
    }

    @Test
    fun `when submitReport fail due to submit call should return expected failure`() {
        val expectedReturn = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<Boolean> = TestSubscriber()

        coEvery {
            submitReportUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onError(expectedReturn)
        }

        onUploadImage_thenReturn("photo")

        val onSuccess: (Boolean) -> Unit = mockk()
        val onError: (Throwable?) -> Unit = mockk()

        viewModel.submitReport(anyInt(), anyInt(), reportInput, onSuccess, onError)

        verifySubmitReportUseCaseCalled()

        testSubscriber.assertError(expectedReturn)
        testSubscriber.assertCompleted()
    }

    private fun onUploadImage_thenReturn(uploadId: String) {
        coEvery { uploaderUseCase.invoke(any()) } returns UploadResult.Success(uploadId)
    }
    private fun onUploadImageError_thenReturn(errorMessage: String) {
        coEvery { uploaderUseCase.invoke(any()) } returns UploadResult.Error(errorMessage)
    }

    private fun verifySubmitReportUseCaseCalled() {
        verify { submitReportUseCase.execute(any(), any()) }
    }
}