package com.tokopedia.ovop2p

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.ovop2p.domain.model.OvoP2pTransferConfirmBase
import com.tokopedia.ovop2p.domain.model.OvoP2pTransferRequestBase
import com.tokopedia.ovop2p.domain.model.WalletDataBase
import com.tokopedia.ovop2p.domain.usecase.GetWalletBalanceUseCase
import com.tokopedia.ovop2p.domain.usecase.OvoP2pTransferUseCase
import com.tokopedia.ovop2p.domain.usecase.OvoTrxnConfirmationUseCase
import com.tokopedia.ovop2p.view.viewStates.OpenPinChlngWebView
import com.tokopedia.ovop2p.view.viewStates.TransferConfErrorSnkBar
import com.tokopedia.ovop2p.view.viewStates.TransferReqData
import com.tokopedia.ovop2p.view.viewStates.TransferReqErrorSnkBar
import com.tokopedia.ovop2p.view.viewStates.WalletData
import com.tokopedia.ovop2p.view.viewStates.WalletError
import com.tokopedia.ovop2p.viewmodel.OvoDetailViewModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WalletViewModelUnitTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getWalletBalanceUsecase = mockk<GetWalletBalanceUseCase>(relaxed = true)
    private lateinit var walletBalanceViewModel: OvoDetailViewModel
    private val ovoP2pTrnxConfirmUseCase = mockk<OvoTrxnConfirmationUseCase>(relaxed = true)
    private val ovoP2pTransferUseCase = mockk<OvoP2pTransferUseCase>(relaxed = true)

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)

    @Before
    fun setUp() {
        walletBalanceViewModel = OvoDetailViewModel(getWalletBalanceUsecase,ovoP2pTrnxConfirmUseCase,ovoP2pTransferUseCase)
    }

    @Test
    fun walletBalanceViewModelSuccess() {
        val walletDataBaseMock = mockk<WalletDataBase>(relaxed = true)
        coEvery {
            getWalletBalanceUsecase.getWalletDetail(any(), any())
        } coAnswers {
            firstArg<(WalletDataBase) -> Unit>().invoke(walletDataBaseMock)

        }
        walletBalanceViewModel.fetchWalletDetails()
        Assert.assertEquals(
            walletBalanceViewModel.walletLiveData.value,
            WalletData(cashBalance = "Saldo ", rawCashBalance = 0)
        )
    }


    @Test
    fun walletBalanceViewModelFail() {
        coEvery {
            getWalletBalanceUsecase.getWalletDetail(any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)

        }
        walletBalanceViewModel.fetchWalletDetails()
        Assert.assertEquals(
            walletBalanceViewModel.walletLiveData.value,
            WalletError("Ada yang salah. Silakan coba lagi")
        )
    }

    @Test
    fun transferConfirmRequestSuccess() {
        val transferRequestBaseMock = mockk<OvoP2pTransferConfirmBase>(relaxed = true)
        coEvery {
            ovoP2pTrnxConfirmUseCase.getConfirmTransactionData(any(), any(), HashMap())
        } coAnswers {
            firstArg<(OvoP2pTransferConfirmBase) -> Unit>().invoke(transferRequestBaseMock)

        }
        walletBalanceViewModel.makeTransferConfirmCall(HashMap())
        Assert.assertEquals(
            walletBalanceViewModel.txnConfirmMutableLiveData.value,
            OpenPinChlngWebView(pinUrl = "")
        )
    }


    @Test
    fun transferConfirmRequestFail() {
        coEvery {
            ovoP2pTrnxConfirmUseCase.getConfirmTransactionData(any(), any(), HashMap())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)

        }
        walletBalanceViewModel.makeTransferConfirmCall(HashMap())

        Assert.assertEquals(
            walletBalanceViewModel.txnConfirmMutableLiveData.value,
            TransferConfErrorSnkBar("Ada yang salah. Silakan coba lagi")
        )
    }

    @Test
    fun transferRequestSuccess() {
        val transferRequestBaseMock = mockk<OvoP2pTransferRequestBase>(relaxed = true)
        coEvery {
            ovoP2pTransferUseCase.transferOvo(any(), any(), HashMap())
        } coAnswers {
            firstArg<(OvoP2pTransferRequestBase) -> Unit>().invoke(transferRequestBaseMock)

        }
        walletBalanceViewModel.makeTransferRequestCall(HashMap())
        Assert.assertEquals(
            walletBalanceViewModel.transferReqBaseMutableLiveData.value,
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
        walletBalanceViewModel.makeTransferRequestCall(HashMap())
        Assert.assertEquals(
            walletBalanceViewModel.transferReqBaseMutableLiveData.value,
            TransferReqErrorSnkBar("Ada yang salah. Silakan coba lagi")
        )
    }
}