package com.tokopedia.ovop2p

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.ovop2p.domain.model.WalletDataBase
import com.tokopedia.ovop2p.domain.usecase.GetWalletBalanceUseCase
import com.tokopedia.ovop2p.view.viewStates.WalletData
import com.tokopedia.ovop2p.view.viewStates.WalletError
import com.tokopedia.ovop2p.viewmodel.GetWalletBalanceViewModel
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
    private lateinit var walletBalanceViewModel: GetWalletBalanceViewModel

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)

    @Before
    fun setUp() {
        walletBalanceViewModel = GetWalletBalanceViewModel(getWalletBalanceUsecase)
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

}