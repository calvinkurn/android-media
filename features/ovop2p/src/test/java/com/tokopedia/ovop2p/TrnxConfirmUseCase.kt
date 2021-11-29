package com.tokopedia.ovop2p

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.ovop2p.domain.model.OvoP2pTransferConfirmBase
import com.tokopedia.ovop2p.domain.usecase.OvoTrxnConfirmationUseCase
import com.tokopedia.ovop2p.view.viewStates.OpenPinChlngWebView
import com.tokopedia.ovop2p.view.viewStates.TransferConfErrorSnkBar
import com.tokopedia.ovop2p.viewmodel.OvoP2pTrxnConfirmVM
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TrnxConfirmUseCase {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val ovoP2pTrnxConfirmUseCase = mockk<OvoTrxnConfirmationUseCase>(relaxed = true)
    private lateinit var trnxConfirmViewModel: OvoP2pTrxnConfirmVM

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)

    @Before
    fun setUp() {
        trnxConfirmViewModel = OvoP2pTrxnConfirmVM(ovoP2pTrnxConfirmUseCase)
    }

    @Test
    fun transferRequestSuccess() {
        val transferRequestBaseMock = mockk<OvoP2pTransferConfirmBase>(relaxed = true)
        coEvery {
            ovoP2pTrnxConfirmUseCase.getConfirmTransactionData(any(), any(), HashMap())
        } coAnswers {
            firstArg<(OvoP2pTransferConfirmBase) -> Unit>().invoke(transferRequestBaseMock)

        }
        trnxConfirmViewModel.makeTransferConfirmCall(HashMap())
        Assert.assertEquals(
            trnxConfirmViewModel.txnConfirmMutableLiveData.value,
            OpenPinChlngWebView(pinUrl = "")
        )
    }


    @Test
    fun transferRequestFail() {
        coEvery {
            ovoP2pTrnxConfirmUseCase.getConfirmTransactionData(any(), any(), HashMap())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)

        }
        trnxConfirmViewModel.makeTransferConfirmCall(HashMap())

        Assert.assertEquals(
            trnxConfirmViewModel.txnConfirmMutableLiveData.value,
            TransferConfErrorSnkBar("Ada yang salah. Silakan coba lagi")
        )
    }
}