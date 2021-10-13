package com.tokopedia.gopay.kyc.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.gopay.kyc.domain.data.InitiateKycData
import com.tokopedia.gopay.kyc.domain.data.InitiateKycResponse
import com.tokopedia.gopay.kyc.domain.usecase.InitiateKycUseCase
import com.tokopedia.gopay.kyc.domain.usecase.SubmitKycUseCase
import com.tokopedia.gopay.kyc.domain.usecase.UploadKycDocumentUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GoPayKycImageUploadViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val initiateKycUseCase = mockk<InitiateKycUseCase>(relaxed = true)
    private val uploadKycDocumentUseCase = mockk<UploadKycDocumentUseCase>(relaxed = true)
    private val submitKycUseCase = mockk<SubmitKycUseCase>(relaxed = true)

    private val dispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: GoPayKycImageUploadViewModel

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)

    @Before
    fun setUp() {
        viewModel = GoPayKycImageUploadViewModel(
            initiateKycUseCase,
            uploadKycDocumentUseCase,
            submitKycUseCase, dispatcher
        )
    }

    @Test
    fun `Execute initiateGoPayKyc fail`() {
        coEvery { initiateKycUseCase.initiateGoPayKyc(any(), any()) } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.initiateGoPayKyc()
        assert(viewModel.uploadSuccessLiveData.value == false)
    }

    @Test
    fun `Execute initiateGoPayKyc code failed`() {
        val response = InitiateKycResponse("FAILED", InitiateKycData("1", arrayListOf()))
        mockkInitiateKycResponse(response)
        viewModel.initiateGoPayKyc()
        assert(viewModel.uploadSuccessLiveData.value == false)
    }

    @Test
    fun `Execute initiateGoPayKyc code success and upload exception`() {
        val response = InitiateKycResponse("SUCCESS", InitiateKycData("1", arrayListOf()))
        mockkInitiateKycResponse(response)
        coEvery { uploadKycDocumentUseCase.uploadKycDocuments(any(),any()) } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)

        }
        viewModel.initiateGoPayKyc()
        assert(viewModel.uploadSuccessLiveData.value == false)
    }

    @Test
    fun `Execute initiateGoPayKyc code success and upload un-successful`() {
        val response = InitiateKycResponse("SUCCESS", InitiateKycData("1", arrayListOf()))
        mockkInitiateKycResponse(response)
        mockUploadDocumentResponse(false)
        viewModel.initiateGoPayKyc()
        assert(viewModel.uploadSuccessLiveData.value == false)
    }

    @Test
    fun `Execute initiateGoPayKyc code success and upload successful and kyc submit failure`() {
        val response = InitiateKycResponse("SUCCESS", InitiateKycData("1", arrayListOf()))
        mockkInitiateKycResponse(response)
        mockUploadDocumentResponse(true)
        coEvery { submitKycUseCase.submitKyc(any(),any(), any()) } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)

        }
        viewModel.initiateGoPayKyc()
        assert(viewModel.uploadSuccessLiveData.value == false)
    }

    @Test
    fun `Execute initiateGoPayKyc code success and upload successful and kyc submit success code`() {
        val response = InitiateKycResponse("SUCCESS", InitiateKycData("1", arrayListOf()))
        mockkInitiateKycResponse(response)
        mockUploadDocumentResponse(true)
        mockSubmitKycResponse()
        viewModel.initiateGoPayKyc()
        assert(viewModel.uploadSuccessLiveData.value == true)
    }

    private fun mockSubmitKycResponse() {
        coEvery { submitKycUseCase.submitKyc(any(), any(), any()) } coAnswers {
            firstArg<(String) -> Unit>().invoke("SUCCESS")
        }
    }

    private fun mockkInitiateKycResponse(response: InitiateKycResponse) {
        coEvery { initiateKycUseCase.initiateGoPayKyc(any(), any()) } coAnswers {
            firstArg<(InitiateKycResponse) -> Unit>().invoke(response)
        }
    }

    private fun mockUploadDocumentResponse(isSuccess: Boolean) {
        coEvery { uploadKycDocumentUseCase.uploadKycDocuments(any(), any()) } coAnswers {
            firstArg<(Boolean) -> Unit>().invoke(isSuccess)
        }
    }
}