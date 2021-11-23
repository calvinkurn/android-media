package com.tokopedia.ovop2p

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.ovop2p.model.WalletDataBase
import com.tokopedia.ovop2p.util.OvoP2pUtil
import com.tokopedia.ovop2p.viewmodel.GetWalletBalanceViewModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import rx.observers.TestSubscriber

class WalletBalanceViewModelUnitTest {
    private val walletDatabase = mockk<WalletDataBase>(relaxed = true)
    private val graphqlResponse = mockk<GraphqlResponse>(relaxed = true)
    lateinit var instrumentationContext: Context
    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)
    private val ov2Util = mockk<OvoP2pUtil>(relaxed = true)


    private lateinit var viewModel: GetWalletBalanceViewModel

    @Before
    fun setUp() {
        viewModel = GetWalletBalanceViewModel()
        instrumentationContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun successViewModelTest() {
        viewModel.getWalletDataSubscriber(instrumentationContext)
        val testSubscriber: TestSubscriber<GraphqlResponse> = TestSubscriber<GraphqlResponse>()
        coEvery {
            (ov2Util.executeOvoGetWalletData(
                instrumentationContext,
                testSubscriber
            ))
        } coAnswers {}
        testSubscriber.onError(mockThrowable)

    }
}