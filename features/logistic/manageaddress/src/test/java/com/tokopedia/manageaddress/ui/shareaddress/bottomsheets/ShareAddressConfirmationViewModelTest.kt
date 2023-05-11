package com.tokopedia.manageaddress.ui.shareaddress.bottomsheets

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.manageaddress.domain.request.shareaddress.SelectShareAddressParam
import com.tokopedia.manageaddress.domain.response.shareaddress.KeroAddrSendShareAddressData
import com.tokopedia.manageaddress.domain.response.shareaddress.KeroAddressError
import com.tokopedia.manageaddress.domain.response.shareaddress.KeroShareAddrToUserResponse
import com.tokopedia.manageaddress.domain.response.shareaddress.SelectShareAddressResponse
import com.tokopedia.manageaddress.domain.usecase.shareaddress.SelectShareAddressUseCase
import com.tokopedia.manageaddress.domain.usecase.shareaddress.ShareAddressToUserUseCase
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

    private val shareAddressUseCase = mockk<ShareAddressToUserUseCase>(relaxed = true)
    private val selectShareAddressUseCase = mockk<SelectShareAddressUseCase>(relaxed = true)
    private val observer = mockk<Observer<ShareAddressConfirmationViewModel.Toast>>(relaxed = true)

    lateinit var viewModel: ShareAddressConfirmationViewModel

    private val exception = Exception("error")

    @Before
    fun setup() {
        viewModel = ShareAddressConfirmationViewModel(
            shareAddressUseCase,
            selectShareAddressUseCase,
            CoroutineTestDispatchersProvider
        )
        viewModel.toastEvent.observeForever(observer)
    }

    @Test
    fun `verify when approve share address success`() {
        val mockResponse = spyk(
            KeroShareAddrToUserResponse().apply {
                keroAddrSendShareAddressToUser = spyk(
                    KeroAddrSendShareAddressData(
                        numberOfRequest = 1
                    )
                )
            }
        )

        coEvery {
            shareAddressUseCase.invoke(any())
        } returns mockResponse

        viewModel.shareAddress(mockk())

        verify {
            observer.onChanged(ShareAddressConfirmationViewModel.Toast.Success)
        }
    }

    @Test
    fun `verify when share address not success`() {
        val errorMessage = "error message"
        val mockResponse = spyk(
            KeroShareAddrToUserResponse().apply {
                keroAddrSendShareAddressToUser = spyk(
                    KeroAddrSendShareAddressData(
                        numberOfRequest = 0,
                        error = spyk(KeroAddressError(message = errorMessage))
                    )
                )
            }
        )

        coEvery {
            shareAddressUseCase.invoke(any())
        } returns mockResponse

        viewModel.shareAddress(mockk())

        verify {
            observer.onChanged(ShareAddressConfirmationViewModel.Toast.Error(errorMessage))
        }
    }

    @Test
    fun `verify when share address error`() {
        coEvery {
            shareAddressUseCase.invoke(any())
        } throws exception

        viewModel.shareAddress(mockk())

        verify {
            observer.onChanged(ShareAddressConfirmationViewModel.Toast.Error(exception.message.orEmpty()))
        }
    }

    @Test
    fun `verify when share address from notif success`() {
        val mockResponse = spyk(
            SelectShareAddressResponse(
                data = spyk(
                    SelectShareAddressResponse.KeroAddrSelectAddressForShareAddressRequest(
                        isSuccess = true
                    )
                )
            )
        )

        coEvery {
            selectShareAddressUseCase.invoke(any())
        } returns mockResponse

        viewModel.shareAddressFromNotif(SelectShareAddressParam.Param(approve = true))

        verify {
            observer.onChanged(ShareAddressConfirmationViewModel.Toast.Success)
        }
    }

    @Test
    fun `verify when share address from notif not success`() {
        val errorMessage = "error message"
        val mockResponse = spyk(
            SelectShareAddressResponse(
                data = spyk(
                    SelectShareAddressResponse.KeroAddrSelectAddressForShareAddressRequest(
                        isSuccess = false,
                        error = spyk(KeroAddressError(message = errorMessage))
                    )
                )
            )
        )

        coEvery {
            selectShareAddressUseCase.invoke(any())
        } returns mockResponse

        viewModel.shareAddressFromNotif(SelectShareAddressParam.Param(approve = true))

        verify {
            observer.onChanged(ShareAddressConfirmationViewModel.Toast.Error(errorMessage))
        }
    }

    @Test
    fun `verify when share address from notif error`() {
        coEvery {
            selectShareAddressUseCase.invoke(any())
        } throws exception

        viewModel.shareAddressFromNotif(SelectShareAddressParam.Param())

        verify {
            observer.onChanged(ShareAddressConfirmationViewModel.Toast.Error(exception.message.orEmpty()))
        }
    }
}
