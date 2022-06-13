package com.tokopedia.manageaddress.ui.shareaddress.bottomsheets

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.logisticCommon.domain.response.ShareAddressResponse
import com.tokopedia.manageaddress.domain.ShareAddressUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShareAddressConfirmationViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val shareAddressUseCase = mockk<ShareAddressUseCase>(relaxed = true)
    private val observer = mockk<Observer<Result<ShareAddressResponse>>>(relaxed = true)

    lateinit var viewModel: ShareAddressConfirmationViewModel

    private val mockThrowable = mockk<Throwable>(relaxed = true)

    @Before
    fun setup() {
        viewModel = ShareAddressConfirmationViewModel(shareAddressUseCase, CoroutineTestDispatchersProvider)
        viewModel.shareAddressResponse.observeForever(observer)
    }

    @Test
    fun `verify when share address success`() {
        val mockResponse = ShareAddressResponse()

        coEvery {
            shareAddressUseCase.invoke(any())
        } returns mockResponse

        viewModel.shareAddress(mockk())

        verify {
            observer.onChanged(Success(mockResponse))
        }
    }

    @Test
    fun `verify when share address error`() {

        coEvery {
            shareAddressUseCase.invoke(any())
        } throws mockThrowable


        viewModel.shareAddress(mockk())

        verify {
            observer.onChanged(Fail(mockThrowable))
        }
    }
}