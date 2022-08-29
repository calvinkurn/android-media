package com.tokopedia.ovop2p

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.ovop2p.domain.model.OvoP2pTransferThankyouBase
import com.tokopedia.ovop2p.domain.usecase.OvoTrnxThankyouPageUseCase
import com.tokopedia.ovop2p.view.viewStates.ThankYouErrSnkBar
import com.tokopedia.ovop2p.view.viewStates.ThankYouSucs
import com.tokopedia.ovop2p.viewmodel.OvoP2PTransactionThankYouVM
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TrnxThankYouUnitTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val ovoP2pTrnxThankYouUseCase = mockk<OvoTrnxThankyouPageUseCase>(relaxed = true)
    private lateinit var trnxThankYouViewModel: OvoP2PTransactionThankYouVM

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)

    @Before
    fun setUp() {
        trnxThankYouViewModel = OvoP2PTransactionThankYouVM(ovoP2pTrnxThankYouUseCase)
    }

    @Test
    fun transferRequestSuccess() {
        val transferThankYouBase = mockk<OvoP2pTransferThankyouBase>(relaxed = true)
        coEvery {
            ovoP2pTrnxThankYouUseCase.getThankyouPageData(any(), any(), HashMap())
        } coAnswers {
            firstArg<(OvoP2pTransferThankyouBase) -> Unit>().invoke(transferThankYouBase)

        }
        trnxThankYouViewModel.makeThankyouDataCall(HashMap())
        Assert.assertEquals(
            trnxThankYouViewModel.transferThankyouLiveData.value,
            ThankYouSucs(
                thankyouBase = transferThankYouBase
            )
        )
    }


    @Test
    fun transferRequestFail() {
        coEvery {
            ovoP2pTrnxThankYouUseCase.getThankyouPageData(any(), any(), HashMap())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)

        }
        trnxThankYouViewModel.makeThankyouDataCall(HashMap())

        Assert.assertEquals(
            trnxThankYouViewModel.transferThankyouLiveData.value,
            ThankYouErrSnkBar("Ada yang salah. Silakan coba lagi")
        )
    }
}