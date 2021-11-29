package com.tokopedia.ovop2p

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.ovop2p.domain.model.OvoP2pTransferRequestBase
import com.tokopedia.ovop2p.domain.usecase.OvoP2pTransferUseCase
import com.tokopedia.ovop2p.view.viewStates.TransferReqData
import com.tokopedia.ovop2p.view.viewStates.TransferReqErrorSnkBar
import com.tokopedia.ovop2p.viewmodel.OvoP2pTransferRequestViewModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TransferRequestViewModelUnitTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val ovoP2pTransferUseCase = mockk<OvoP2pTransferUseCase>(relaxed = true)
    private lateinit var transferRequestViewModel: OvoP2pTransferRequestViewModel

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)

    @Before
    fun setUp() {
        transferRequestViewModel = OvoP2pTransferRequestViewModel(ovoP2pTransferUseCase)
    }

    @Test
    fun transferRequestSuccess() {
        val transferRequestBaseMock = mockk<OvoP2pTransferRequestBase>(relaxed = true)
        coEvery {
            ovoP2pTransferUseCase.transferOvo(any(), any(), HashMap())
        } coAnswers {
            firstArg<(OvoP2pTransferRequestBase) -> Unit>().invoke(transferRequestBaseMock)

        }
        transferRequestViewModel.makeTransferRequestCall(HashMap())
        Assert.assertEquals(
            transferRequestViewModel.transferReqBaseMutableLiveData.value,
            TransferReqData(dstAccName = "")
        )
    }


    @Test
    fun transferRequestFail() {
        coEvery {
            ovoP2pTransferUseCase.transferOvo(any(), any(), HashMap())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)

        }
        transferRequestViewModel.makeTransferRequestCall(HashMap())
        Assert.assertEquals(
            transferRequestViewModel.transferReqBaseMutableLiveData.value,
            TransferReqErrorSnkBar("Ada yang salah. Silakan coba lagi")
        )
    }
}