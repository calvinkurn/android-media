package com.tokopedia.home.account.presentation.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliatecommon.domain.CheckAffiliateUseCase
import com.tokopedia.home.account.data.model.AccountModel
import com.tokopedia.home.account.domain.GetBuyerWalletBalanceUseCase
import com.tokopedia.home.account.presentation.util.dispatchers.TestDispatcherProvider
import com.tokopedia.home.account.revamp.domain.GetBuyerAccountDataUseCase
import com.tokopedia.home.account.revamp.viewmodel.BuyerAccountViewModel
import com.tokopedia.navigation_common.model.WalletModel
import com.tokopedia.navigation_common.model.WalletPref
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class BuyerAccountViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val context: Context = mockk(relaxed = true)

    private val getBuyerAccountDataUseCase: GetBuyerAccountDataUseCase = mockk(relaxed = true)
    private val checkAffiliateUseCase: CheckAffiliateUseCase = mockk(relaxed = true)
    private val getBuyerWalletBalanceUseCase: GetBuyerWalletBalanceUseCase = mockk(relaxed = true)
    private val userSession: UserSessionInterface = mockk(relaxed = true)
    private val walletPref: WalletPref = mockk(relaxed = true)

    private lateinit var viewModel: BuyerAccountViewModel
    private val dispatcherProvider = TestDispatcherProvider()

    @Before
    fun setUp() {
        viewModel = BuyerAccountViewModel(
                getBuyerAccountDataUseCase,
                checkAffiliateUseCase,
                getBuyerWalletBalanceUseCase,
                userSession,
                walletPref,
                dispatcherProvider
        )
    }

    @Test
    fun `it successfully get buyer account data`() = runBlockingTest {
        val expectedReturn = Success(AccountModel())
        val expectedWallet = WalletModel()

        coEvery {
            getBuyerAccountDataUseCase.executeOnBackground()
        } answers {
            expectedReturn.data
        }

        coEvery {
            getBuyerWalletBalanceUseCase.createObservable(RequestParams.EMPTY).toBlocking().single()
        } answers {
            expectedReturn.data.wallet = expectedWallet
            expectedWallet
        }

        coEvery {
            checkAffiliateUseCase.createObservable(RequestParams.EMPTY).toBlocking().single()
        } answers {
            expectedReturn.data.isAffiliate = true
            true
        }

        viewModel.getBuyerData()

        Assertions.assertThat(viewModel.buyerAccountData.value).isEqualTo(expectedReturn)
    }
}