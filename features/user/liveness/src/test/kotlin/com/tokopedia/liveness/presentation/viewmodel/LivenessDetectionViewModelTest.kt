package com.tokopedia.liveness.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.liveness.data.model.response.LivenessData
import com.tokopedia.liveness.domain.UploadLivenessResultUseCase
import com.tokopedia.liveness.util.CoroutineTestDispatchersProvider
import com.tokopedia.liveness.view.viewmodel.LivenessDetectionViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertFailsWith

class LivenessDetectionViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var useCase: UploadLivenessResultUseCase

    private lateinit var viewModel : LivenessDetectionViewModel

    private val ktpPath = "test ktp path"
    private val facePath = "test face path"
    private val projectId = "1"

    @Before
    fun before() {
        MockKAnnotations.init(this)
        viewModel = LivenessDetectionViewModel(useCase, CoroutineTestDispatchersProvider)
    }

    @Test
    fun `Register - Success upload image and accepted`() {
        val livenessData = LivenessData()

        coEvery {
            useCase.uploadImages(any(), any(), any())
        } answers {
            livenessData.isSuccessRegister = true
            livenessData
        }

        viewModel.uploadImages(ktpPath, facePath, projectId)

        val result = viewModel.livenessResponseLiveData.value
        assertEquals(result, Success(livenessData))
        assertTrue((result as Success).data.isSuccessRegister)
    }

    @Test
    fun `Register - Success upload image but rejected`() {
        val livenessData = LivenessData()

        coEvery {
            useCase.uploadImages(any(), any(), any())
        } answers {
            livenessData.isSuccessRegister = false
            livenessData
        }

        viewModel.uploadImages(ktpPath, facePath, projectId)

        val result = viewModel.livenessResponseLiveData.value
        assertFalse((result as Success).data.isSuccessRegister)
    }

    @Test
    fun `API - get error response`() {
        val viewModelMock = mockk<LivenessDetectionViewModel>(relaxed = true)
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
        val viewModelMock = mockk<LivenessDetectionViewModel>(relaxed = true)
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

        val result = viewModel.livenessResponseLiveData.value
        assertTrue(result is Fail)
    }
}