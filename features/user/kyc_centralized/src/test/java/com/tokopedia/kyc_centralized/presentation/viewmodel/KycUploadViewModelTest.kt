package com.tokopedia.kyc_centralized.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kyc_centralized.data.model.response.KycData
import com.tokopedia.kyc_centralized.domain.KycUploadUseCase
import com.tokopedia.kyc_centralized.view.viewmodel.KycUploadViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import junit.framework.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import util.TestAppDispatchProvider
import kotlin.test.assertFailsWith

class KycUploadViewModelTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var useCase: KycUploadUseCase

    private lateinit var viewModel : KycUploadViewModel

    private val ktpPath = "test ktp path"
    private val facePath = "test face path"
    private val projectId = "1"

    @Before
    fun before() {
        MockKAnnotations.init(this)
        viewModel = KycUploadViewModel(useCase, TestAppDispatchProvider)
    }

    @Test
    fun `Register - Success upload image and accepted`() {
        val livenessData = KycData()

        coEvery {
            useCase.uploadImages(any(), any(), any())
        } answers {
            livenessData.isSuccessRegister = true
            livenessData
        }

        viewModel.uploadImages(ktpPath, facePath, projectId)

        val result = viewModel.kycResponseLiveData.value
        Assert.assertEquals(result, Success(livenessData))
        Assert.assertTrue((result as Success).data.isSuccessRegister)
    }

    @Test
    fun `Register - Success upload image but rejected`() {
        val livenessData = KycData()

        coEvery {
            useCase.uploadImages(any(), any(), any())
        } answers {
            livenessData.isSuccessRegister = false
            livenessData
        }

        viewModel.uploadImages(ktpPath, facePath, projectId)

        val result = viewModel.kycResponseLiveData.value
        Assert.assertFalse((result as Success).data.isSuccessRegister)
    }

    @Test
    fun `API - get error response`() {
        val viewModelMock = mockk<KycUploadViewModel>(relaxed = true)
        val exceptionMock = Exception("Oops!")

        coEvery {
            viewModelMock.uploadImages(any(), any(), any())
        } throws exceptionMock

        assertFailsWith<Exception> {
            viewModelMock.uploadImages(ktpPath, facePath, projectId)
        }
    }

    @Test
    fun `API - get error response with empty params`() {
        val viewModelMock = mockk<KycUploadViewModel>(relaxed = true)
        val exceptionMock = Exception("Oops!")

        coEvery {
            viewModelMock.uploadImages(any(), any(), any())
        } throws exceptionMock

        assertFailsWith<Exception> {
            viewModelMock.uploadImages("", "", "")
        }
    }

    @Test
    fun `Register - Failed upload image and get exception`() {
        val exceptionMock = mockk<Exception>(relaxed = true)

        coEvery {
            useCase.uploadImages(any(), any(), any())
        } throws exceptionMock

        viewModel.uploadImages(ktpPath, facePath, projectId)

        val result = viewModel.kycResponseLiveData.value
        Assert.assertTrue(result is Fail)
    }
}