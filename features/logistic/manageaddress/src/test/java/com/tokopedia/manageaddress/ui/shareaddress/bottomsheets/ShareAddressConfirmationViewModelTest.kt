package com.tokopedia.manageaddress.ui.shareaddress.bottomsheets

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.logisticCommon.domain.model.ShareAddressBottomSheetState
import com.tokopedia.logisticCommon.domain.response.ShareAddressResponse
import com.tokopedia.manageaddress.domain.usecase.ShareAddressUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShareAddressConfirmationViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val shareAddressUseCase = mockk<ShareAddressUseCase>(relaxed = true)
    private val observer = mockk<Observer<ShareAddressBottomSheetState<ShareAddressResponse>>>(relaxed = true)

    lateinit var viewModel: ShareAddressConfirmationViewModel

    private val mockThrowable = mockk<Throwable>(relaxed = true)

    @Before
    fun setup() {
        viewModel = ShareAddressConfirmationViewModel(shareAddressUseCase, CoroutineTestDispatchersProvider)
        viewModel.shareAddressResponse.observeForever(observer)
    }

    @Test
    fun `verify when share address success`() {
        val mockResponse = spyk(ShareAddressResponse().apply {
            shareAddressResponse = spyk(
                ShareAddressResponse.ShareAddressResponse(
                isSuccess = true
            ))
        })

        coEvery {
            shareAddressUseCase.invoke(any())
        } returns mockResponse

        viewModel.shareAddress(mockk())

        verify {
            observer.onChanged(ShareAddressBottomSheetState.Success(mockResponse))
        }
    }

    @Test
    fun `verify when share address not success`() {
        val errorMessage = "error message"
        val mockResponse = spyk(ShareAddressResponse().apply {
            shareAddressResponse = spyk(ShareAddressResponse.ShareAddressResponse(
                isSuccess = false,
                error = errorMessage
            ))
        })

        coEvery {
            shareAddressUseCase.invoke(any())
        } returns mockResponse

        viewModel.shareAddress(mockk())

        verify {
            observer.onChanged(ShareAddressBottomSheetState.Fail(errorMessage))
        }
    }

    @Test
    fun `verify when share address error`() {

        coEvery {
            shareAddressUseCase.invoke(any())
        } throws mockThrowable


        viewModel.shareAddress(mockk())

        verify {
            observer.onChanged(ShareAddressBottomSheetState.Fail(mockThrowable.message.orEmpty()))
        }
    }
}