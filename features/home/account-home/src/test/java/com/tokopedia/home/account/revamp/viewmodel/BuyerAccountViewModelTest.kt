package com.tokopedia.home.account.revamp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliatecommon.domain.CheckAffiliateUseCase
import com.tokopedia.home.account.data.model.AccountModel
import com.tokopedia.home.account.domain.GetBuyerWalletBalanceUseCase
import com.tokopedia.home.account.presentation.util.dispatchers.TestDispatcherProvider
import com.tokopedia.home.account.revamp.domain.usecase.GetBuyerAccountDataUseCase
import com.tokopedia.navigation_common.model.WalletModel
import com.tokopedia.navigation_common.model.WalletPref
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class BuyerAccountViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getBuyerAccountDataUseCase: GetBuyerAccountDataUseCase = mockk(relaxed = true)
    private val checkAffiliateUseCase: CheckAffiliateUseCase = mockk(relaxed = true)
    private val getBuyerWalletBalanceUseCase: GetBuyerWalletBalanceUseCase = mockk(relaxed = true)
    private val addWishListUseCase: AddWishListUseCase = mockk(relaxed = true)
    private val removeWishListUseCase: RemoveWishListUseCase = mockk(relaxed = true)
    private val getRecommendationUseCase: GetRecommendationUseCase = mockk(relaxed = true)
    private val topAdsWishlishedUseCase: TopAdsWishlishedUseCase = mockk(relaxed = true)
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
                addWishListUseCase,
                removeWishListUseCase,
                getRecommendationUseCase,
                topAdsWishlishedUseCase,
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

        Assertions.assertThat(viewModel.buyerAccountDataData.value).isEqualTo(expectedReturn)
    }

    @Test
    fun `it failed to get buyer account data` () = runBlockingTest {
        val expectedReturn = Fail(Throwable("Oops"))
        val expectedWallet = WalletModel()

        coEvery {
            getBuyerAccountDataUseCase.executeOnBackground()
        } throws expectedReturn.throwable

        coEvery {
            getBuyerWalletBalanceUseCase.createObservable(RequestParams.EMPTY).toBlocking().single()
        } answers {
            expectedWallet
        }

        coEvery {
            checkAffiliateUseCase.createObservable(RequestParams.EMPTY).toBlocking().single()
        } answers {
            true
        }

        viewModel.getBuyerData()

        Assertions.assertThat(viewModel.buyerAccountDataData.value).isEqualTo(expectedReturn)
    }

    @Test
    fun `it failed to get buyer wallet` () = runBlockingTest {
        val expectedReturn = Fail(Throwable("Oops"))

        coEvery {
            getBuyerAccountDataUseCase.executeOnBackground()
        } answers {
            AccountModel()
        }

        coEvery {
            getBuyerWalletBalanceUseCase.createObservable(RequestParams.EMPTY).toBlocking().single()
        } throws expectedReturn.throwable

        coEvery {
            checkAffiliateUseCase.createObservable(RequestParams.EMPTY).toBlocking().single()
        } answers {
            true
        }

        viewModel.getBuyerData()

        Assertions.assertThat(viewModel.buyerAccountDataData.value).isEqualTo(expectedReturn)
    }

    @Test
    fun `it failed to check is affiliate` () = runBlockingTest {
        val expectedReturn = Fail(Throwable("Oops"))
        val expectedWallet = WalletModel()

        coEvery {
            getBuyerAccountDataUseCase.executeOnBackground()
        } answers {
            AccountModel()
        }

        coEvery {
            getBuyerWalletBalanceUseCase.createObservable(RequestParams.EMPTY).toBlocking().single()
        } answers {
            expectedWallet
        }

        coEvery {
            checkAffiliateUseCase.createObservable(RequestParams.EMPTY).toBlocking().single()
        } throws expectedReturn.throwable

        viewModel.getBuyerData()

        Assertions.assertThat(viewModel.buyerAccountDataData.value).isEqualTo(expectedReturn)
    }
}