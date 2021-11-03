package com.tokopedia.report.view.viewmodel

import com.tokopedia.mediauploader.common.state.UploadResult
import io.mockk.*
import org.junit.Test
import org.mockito.ArgumentMatchers.*
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

        viewModel.submitReport(anyLong(), anyInt(), reportInput, onSuccess, onError)

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

        viewModel.submitReport(anyLong(), anyInt(), reportInput, onSuccess, onError)

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

        viewModel.submitReport(anyLong(), anyInt(), reportInput, onSuccess, onError)

        verifySubmitReportUseCaseCalled()

        testSubscriber.assertError(expectedReturn)
        testSubscriber.assertCompleted()
    }

    @Test
    fun onCleared() {
        every { submitReportUseCase.unsubscribe() } just runs

        val method = viewModel::class.java.getDeclaredMethod("onCleared")
        method.isAccessible = true
        method.invoke(viewModel)

        verify { submitReportUseCase.unsubscribe() }
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