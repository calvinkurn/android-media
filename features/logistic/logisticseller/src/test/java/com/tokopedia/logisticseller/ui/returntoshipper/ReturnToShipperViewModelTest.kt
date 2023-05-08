package com.tokopedia.logisticseller.ui.returntoshipper

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.logisticseller.data.param.GeneralInfoRtsParam
import com.tokopedia.logisticseller.data.response.GetGeneralInfoRtsResponse
import com.tokopedia.logisticseller.data.response.ReqGeneralInfoRtsResponse
import com.tokopedia.logisticseller.domain.usecase.GetGeneralInfoRtsUseCase
import com.tokopedia.logisticseller.domain.usecase.RequestGeneralInfoRtsUseCase
import com.tokopedia.logisticseller.ui.returntoshipper.uimodel.ReturnToShipperState
import com.tokopedia.logisticseller.ui.returntoshipper.viewmodel.ReturnToShipperViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ReturnToShipperViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getGeneralInfoUseCase = mockk<GetGeneralInfoRtsUseCase>(relaxed = true)
    private val requestGeneralInfoUseCase = mockk<RequestGeneralInfoRtsUseCase>(relaxed = true)

    private val confirmationRtsObserver =
        mockk<Observer<ReturnToShipperState<GetGeneralInfoRtsResponse.GeneralInfoRtsData>>>(
            relaxed = true
        )

    lateinit var viewModel: ReturnToShipperViewModel

    private val mockThrowable = mockk<Throwable>(relaxed = true)

    @Before
    fun setup() {
        viewModel = ReturnToShipperViewModel(
            CoroutineTestDispatchersProvider,
            getGeneralInfoUseCase,
            requestGeneralInfoUseCase
        )
        viewModel.confirmationRtsState.observeForever(confirmationRtsObserver)
    }

    @Test
    fun `verify when get general information success`() {
        val orderId = "12345"
        val data = spyk<GetGeneralInfoRtsResponse.GeneralInfoRtsData>()
        val mockResponse = spyk(
            GetGeneralInfoRtsResponse(
                GetGeneralInfoRtsResponse.GetGeneralInfoRts(
                    status = 200,
                    data = data
                )
            )
        )

        coEvery {
            getGeneralInfoUseCase.invoke(any())
        } returns mockResponse

        viewModel.getGeneralInformation(orderId)

        verify {
            confirmationRtsObserver.onChanged(
                ReturnToShipperState.ShowRtsConfirmDialog(data)
            )
        }
    }

    @Test
    fun `verify when get general information success but data is null`() {
        val orderId = "12345"
        val errorMessage = "error"
        val mockResponse = spyk(
            GetGeneralInfoRtsResponse(
                GetGeneralInfoRtsResponse.GetGeneralInfoRts(
                    status = 200,
                    messageError = errorMessage,
                )
            )
        )

        coEvery {
            getGeneralInfoUseCase.invoke(any())
        } returns mockResponse

        viewModel.getGeneralInformation(orderId)

        verify {
            confirmationRtsObserver.onChanged(
                ReturnToShipperState.ShowToaster(errorMessage)
            )
        }
    }

    @Test
    fun `verify when get general information failed`() {
        val orderId = "12345"
        val errorMessage = "error"
        val mockResponse = spyk(
            GetGeneralInfoRtsResponse(
                GetGeneralInfoRtsResponse.GetGeneralInfoRts(
                    status = 400,
                    messageError = errorMessage,
                )
            )
        )

        coEvery {
            getGeneralInfoUseCase.invoke(any())
        } returns mockResponse

        viewModel.getGeneralInformation(orderId)

        verify {
            confirmationRtsObserver.onChanged(
                ReturnToShipperState.ShowToaster(errorMessage)
            )
        }
    }

    @Test
    fun `verify when get general information error`() {
        val orderId = "12345"
        val errorMessage = "error"
        coEvery {
            getGeneralInfoUseCase.invoke(any())
        } throws mockThrowable

        coEvery { mockThrowable.message } returns errorMessage
        viewModel.getGeneralInformation(orderId)

        verify {
            confirmationRtsObserver.onChanged(
                ReturnToShipperState.ShowToaster(errorMessage)
            )
        }
    }

    @Test
    fun `verify when get general information error and null message`() {
        val orderId = "12345"
        coEvery {
            getGeneralInfoUseCase.invoke(any())
        } throws mockThrowable

        coEvery { mockThrowable.message } returns null
        viewModel.getGeneralInformation(orderId)

        verify {
            confirmationRtsObserver.onChanged(
                ReturnToShipperState.ShowToaster("")
            )
        }
    }

    @Test
    fun `verify when request general information confirmation success`() {
        val orderId = "12345"
        val mockResponse = spyk(
            ReqGeneralInfoRtsResponse(
                ReqGeneralInfoRtsResponse.ReqGeneralInfoRts(
                    status = 200
                )
            )
        )

        coEvery {
            requestGeneralInfoUseCase.invoke(any())
        } returns mockResponse

        viewModel.requestGeneralInformation(orderId, GeneralInfoRtsParam.ACTION_RTS_CONFIRMATION)

        verify {
            confirmationRtsObserver.onChanged(
                ReturnToShipperState.ShowRtsSuccessDialog
            )
        }
    }

    @Test
    fun `verify when request general information helper success`() {
        val orderId = "12345"
        val mockResponse = spyk(
            ReqGeneralInfoRtsResponse(
                ReqGeneralInfoRtsResponse.ReqGeneralInfoRts(
                    status = 200
                )
            )
        )

        coEvery {
            requestGeneralInfoUseCase.invoke(any())
        } returns mockResponse

        viewModel.requestGeneralInformation(orderId, GeneralInfoRtsParam.ACTION_RTS_HELPER)

        verify(exactly = 0) {
            confirmationRtsObserver.onChanged(
                ReturnToShipperState.ShowRtsSuccessDialog
            )
        }
    }

    @Test
    fun `verify when request general information failed`() {
        val orderId = "12345"
        val mockResponse = spyk(
            ReqGeneralInfoRtsResponse(
                ReqGeneralInfoRtsResponse.ReqGeneralInfoRts(
                    status = 400,
                )
            )
        )

        coEvery {
            requestGeneralInfoUseCase.invoke(any())
        } returns mockResponse

        viewModel.requestGeneralInformation(orderId, GeneralInfoRtsParam.ACTION_RTS_CONFIRMATION)

        verify {
            confirmationRtsObserver.onChanged(
                ReturnToShipperState.ShowRtsFailedDialog
            )
        }
    }

    @Test
    fun `verify when request general information confirmation error`() {
        val orderId = "12345"
        val errorMessage = "error"
        coEvery {
            requestGeneralInfoUseCase.invoke(any())
        } throws mockThrowable

        coEvery { mockThrowable.message } returns errorMessage
        viewModel.requestGeneralInformation(orderId, GeneralInfoRtsParam.ACTION_RTS_CONFIRMATION)

        verify {
            confirmationRtsObserver.onChanged(
                ReturnToShipperState.ShowToaster(errorMessage)
            )
        }
    }

    @Test
    fun `verify when request general information helper error`() {
        val orderId = "12345"
        val errorMessage = "error"
        coEvery {
            requestGeneralInfoUseCase.invoke(any())
        } throws mockThrowable

        coEvery { mockThrowable.message } returns errorMessage
        viewModel.requestGeneralInformation(orderId, GeneralInfoRtsParam.ACTION_RTS_HELPER)

        verify(exactly = 0) {
            confirmationRtsObserver.onChanged(
                ReturnToShipperState.ShowToaster(errorMessage)
            )
        }
    }

    @Test
    fun `verify when request general information error and null message`() {
        val orderId = "12345"
        coEvery {
            requestGeneralInfoUseCase.invoke(any())
        } throws mockThrowable

        coEvery { mockThrowable.message } returns null
        viewModel.requestGeneralInformation(orderId, GeneralInfoRtsParam.ACTION_RTS_CONFIRMATION)

        verify {
            confirmationRtsObserver.onChanged(
                ReturnToShipperState.ShowToaster("")
            )
        }
    }
}
