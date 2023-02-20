package com.tokopedia.manageaddress.ui.shareaddress.bottomsheets

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.manageaddress.ui.uimodel.ShareAddressBottomSheetState
import com.tokopedia.manageaddress.domain.response.shareaddress.KeroAddressError
import com.tokopedia.manageaddress.domain.response.shareaddress.KeroAddrSendShareAddressData
import com.tokopedia.manageaddress.domain.response.shareaddress.KeroShareAddrToUserResponse
import com.tokopedia.manageaddress.domain.usecase.shareaddress.ShareAddressToUserUseCase
import com.tokopedia.manageaddress.domain.response.shareaddress.SelectShareAddressResponse
import com.tokopedia.manageaddress.domain.usecase.shareaddress.SelectShareAddressUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShareAddressConfirmationViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val shareAddressUseCase = mockk<ShareAddressToUserUseCase>(relaxed = true)
    private val selectShareAddressUseCase = mockk<SelectShareAddressUseCase>(relaxed = true)
    private val observer = mockk<Observer<ShareAddressBottomSheetState>>(relaxed = true)

    lateinit var viewModel: ShareAddressConfirmationViewModel

    private val mockThrowable = mockk<Throwable>(relaxed = true)

    @Before
    fun setup() {
        viewModel = ShareAddressConfirmationViewModel(
            shareAddressUseCase,
            selectShareAddressUseCase,
            CoroutineTestDispatchersProvider
        )
        viewModel.shareAddressResponse.observeForever(observer)
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

        viewModel.isApprove = true
        viewModel.shareAddress(mockk())

        Assert.assertTrue(viewModel.isApprove)
        verify {
            observer.onChanged(ShareAddressBottomSheetState.Success)
        }
    }

    @Test
    fun `verify when not approve share address success`() {
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

        viewModel.isApprove = false
        viewModel.shareAddress(mockk())

        Assert.assertFalse(viewModel.isApprove)
        verify {
            observer.onChanged(ShareAddressBottomSheetState.Success)
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

        viewModel.shareAddressFromNotif(mockk())

        verify {
            observer.onChanged(ShareAddressBottomSheetState.Success)
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

        viewModel.shareAddressFromNotif(mockk())

        verify {
            observer.onChanged(ShareAddressBottomSheetState.Fail(errorMessage))
        }
    }

    @Test
    fun `verify when share address from notif error`() {
        coEvery {
            selectShareAddressUseCase.invoke(any())
        } throws mockThrowable

        viewModel.shareAddressFromNotif(mockk())

        verify {
            observer.onChanged(ShareAddressBottomSheetState.Fail(mockThrowable.message.orEmpty()))
        }
    }
}
