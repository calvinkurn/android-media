package com.tokopedia.kyc_centralized.gotoKyc.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.capture.CaptureKycDocumentsViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import util.getOrAwaitValue
import kotlin.test.assertEquals

class CaptureKycDocumentsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var viewModel: CaptureKycDocumentsViewModel

    @Before
    fun setup() {
        viewModel = CaptureKycDocumentsViewModel(dispatcher)
    }

    @Test
    fun `when failed 1 time then not return retry exhausted`() {
        viewModel.onFailedUpload()

        val result = viewModel.retryExhausted.value
        assertEquals(null, result)
    }

    @Test
    fun `when failed 2 time then not return retry exhausted`() {
        viewModel.onFailedUpload()
        viewModel.onFailedUpload()

        val result = viewModel.retryExhausted.value
        assertEquals(null, result)
    }

    @Test
    fun `when failed 3 time then not return retry exhausted`() {
        viewModel.onFailedUpload()
        viewModel.onFailedUpload()
        viewModel.onFailedUpload()

        val result = viewModel.retryExhausted.value
        assertEquals(null, result)
    }

    @Test
    fun `when failed 4 times then return retry exhausted`() {
        viewModel.onFailedUpload()
        viewModel.onFailedUpload()
        viewModel.onFailedUpload()
        viewModel.onFailedUpload()

        val result = viewModel.retryExhausted.getOrAwaitValue()
        assertEquals(Unit, result)
    }

}
