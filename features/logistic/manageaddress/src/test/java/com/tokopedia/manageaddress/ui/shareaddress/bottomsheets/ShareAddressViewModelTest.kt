package com.tokopedia.manageaddress.ui.shareaddress.bottomsheets

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.manageaddress.ui.uimodel.ShareAddressBottomSheetState
import com.tokopedia.manageaddress.domain.response.shareaddress.KeroAddressError
import com.tokopedia.manageaddress.domain.response.shareaddress.KeroAddrSendShareAddressData
import com.tokopedia.manageaddress.domain.response.shareaddress.KeroShareAddrRequestResponse
import com.tokopedia.manageaddress.domain.response.shareaddress.KeroShareAddrToUserResponse
import com.tokopedia.manageaddress.domain.usecase.shareaddress.ShareAddressToUserUseCase
import com.tokopedia.manageaddress.domain.usecase.shareaddress.SendShareAddressRequestUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShareAddressViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val sendShareAddressRequestUseCase = mockk<SendShareAddressRequestUseCase>(relaxed = true)
    private val shareAddressToUserUseCase = mockk<ShareAddressToUserUseCase>(relaxed = true)
    private val observer = mockk<Observer<ShareAddressBottomSheetState>>(relaxed = true)

    lateinit var viewModel: ShareAddressViewModel

    private val mockThrowable = mockk<Throwable>(relaxed = true)

    @Before
    fun setup() {
        viewModel = ShareAddressViewModel(
            sendShareAddressRequestUseCase,
            shareAddressToUserUseCase,
            CoroutineTestDispatchersProvider
        )
        viewModel.requestAddressResponse.observeForever(observer)
        viewModel.checkShareAddressResponse.observeForever(observer)
    }

    @Test
    fun `verify when send share address request is success`() {
        val mockResponse = spyk(
            KeroShareAddrRequestResponse().apply {
                keroAddrSendShareAddressRequest = spyk(
                    KeroAddrSendShareAddressData(
                        numberOfRequest = 1
                    )
                )
            }
        )

        coEvery {
            sendShareAddressRequestUseCase.invoke(any())
        } returns mockResponse

        viewModel.requestShareAddress(mockk())

        verify {
            observer.onChanged(ShareAddressBottomSheetState.Success)
        }
    }

    @Test
    fun `verify when send share address request not success`() {
        val errorMessage = "error message"
        val mockResponse = spyk(
            KeroShareAddrRequestResponse().apply {
                keroAddrSendShareAddressRequest = spyk(
                    KeroAddrSendShareAddressData(
                        numberOfRequest = 0,
                        error = spyk(KeroAddressError(message = errorMessage))
                    )
                )
            }
        )

        coEvery {
            sendShareAddressRequestUseCase.invoke(any())
        } returns mockResponse

        viewModel.requestShareAddress(mockk())

        verify {
            observer.onChanged(ShareAddressBottomSheetState.Fail(errorMessage))
        }
    }

    @Test
    fun `verify when send share address request error`() {
        coEvery {
            sendShareAddressRequestUseCase.invoke(any())
        } throws mockThrowable
        
        viewModel.requestShareAddress(mockk())

        verify {
            observer.onChanged(ShareAddressBottomSheetState.Fail(mockThrowable.message.orEmpty()))
        }
    }

    @Test
    fun `verify when check share address is success`() {
        val mockResponse = spyk(
            KeroShareAddrToUserResponse().apply {
                keroAddrSendShareAddressToUser = spyk(
                    KeroAddrSendShareAddressData(
                        numberOfRequest = 1,
                        error = KeroAddressError(code = 0)
                    )
                )
            }
        )

        coEvery {
            shareAddressToUserUseCase.invoke(any())
        } returns mockResponse

        viewModel.checkShareAddress(mockk())

        verify {
            observer.onChanged(ShareAddressBottomSheetState.Success)
        }
    }

    @Test
    fun `verify when check share address not success`() {
        val errorMessage = "error message"
        val mockResponse = spyk(
            KeroShareAddrToUserResponse().apply {
                keroAddrSendShareAddressToUser = spyk(
                    KeroAddrSendShareAddressData(
                        numberOfRequest = 0,
                        error = spyk(KeroAddressError(message = errorMessage, code = 1))
                    )
                )
            }
        )

        coEvery {
            shareAddressToUserUseCase.invoke(any())
        } returns mockResponse

        viewModel.checkShareAddress(mockk())

        verify {
            observer.onChanged(ShareAddressBottomSheetState.Fail(errorMessage))
        }
    }

    @Test
    fun `verify when check share address error`() {
        coEvery {
            shareAddressToUserUseCase.invoke(any())
        } throws mockThrowable

        viewModel.checkShareAddress(mockk())

        verify {
            observer.onChanged(ShareAddressBottomSheetState.Fail(mockThrowable.message.orEmpty()))
        }
    }
}
