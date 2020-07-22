package com.tokopedia.liveness.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.liveness.data.model.response.LivenessData
import com.tokopedia.liveness.domain.UploadLivenessResultUseCase
import com.tokopedia.liveness.util.getOrAwaitValue
import com.tokopedia.liveness.view.viewmodel.LivenessDetectionViewModel
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class LivenessDetectionViewModelTest {

    private val useCase = mockk<UploadLivenessResultUseCase>(relaxed = true)
    private lateinit var viewModel: LivenessDetectionViewModel

    private val ktpPath = "test"
    private val facePath = "test"
    private val projectId = "1"

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        viewModel = LivenessDetectionViewModel(useCase, Dispatchers.Unconfined)
    }

    /**
     * Liveness Detection Response
     * */
    @Test
    fun `Register - Test do success response`() {
        val livenessData = LivenessData()

        coEvery {
            useCase.uploadImages(any(), any(), any())
        } answers {
            livenessData.isSuccessRegister = true
            livenessData
        }

        viewModel.uploadImages(ktpPath, facePath, projectId)

        val result = viewModel.livenessResponseLiveData.getOrAwaitValue()
        assertEquals(result, Success(livenessData))
        assertTrue((result as Success).data.isSuccessRegister)
    }

    @Test
    fun `Register - Test do failed response`() {
        val livenessData = LivenessData()

        coEvery {
            useCase.uploadImages(any(), any(), any())
        } answers {
            livenessData.isSuccessRegister = false
            livenessData
        }

        viewModel.uploadImages(ktpPath, facePath, projectId)

        val result = viewModel.livenessResponseLiveData.getOrAwaitValue()
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
}