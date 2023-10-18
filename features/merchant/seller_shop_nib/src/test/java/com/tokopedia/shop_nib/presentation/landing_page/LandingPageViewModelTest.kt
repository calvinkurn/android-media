package com.tokopedia.shop_nib.presentation.landing_page

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop_nib.domain.entity.NibSubmissionResult
import com.tokopedia.shop_nib.domain.usecase.SellerSubmitNIBStatusUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LandingPageViewModelTest {

    @RelaxedMockK
    lateinit var sellerSubmitNIBStatusUseCase: SellerSubmitNIBStatusUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val viewModel by lazy {
        LandingPageViewModel(CoroutineTestDispatchersProvider, sellerSubmitNIBStatusUseCase)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    //region checkPreviousSubmission
    @Test
    fun `When get previous file submission success, observer should get success result`() {
        //Given
        val result = NibSubmissionResult(success = true, errorMessage = "", hasPreviousSubmission = false)
        coEvery { sellerSubmitNIBStatusUseCase.execute() } returns result

        //When
        viewModel.checkPreviousSubmission()

        //Then
        val actual = viewModel.previousSubmissionState.getOrAwaitValue()
        assertEquals(Success(result), actual)
    }

    @Test
    fun `When get previous file submission error, observer should get error result`() {
        //Given
        val error = MessageErrorException("Server error")
        coEvery { sellerSubmitNIBStatusUseCase.execute() } throws error

        //When
        viewModel.checkPreviousSubmission()

        //Then
        val actual = viewModel.previousSubmissionState.getOrAwaitValue()
        assertEquals(Fail(error), actual)
    }

    //endregion

}
