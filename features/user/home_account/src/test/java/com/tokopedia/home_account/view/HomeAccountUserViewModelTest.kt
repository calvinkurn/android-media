package com.tokopedia.home_account.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.home_account.FileUtil
import com.tokopedia.home_account.data.model.*
import com.tokopedia.home_account.domain.usecase.*
import com.tokopedia.home_account.getOrAwaitValue
import com.tokopedia.home_account.pref.AccountPreference
import com.tokopedia.navigation_common.model.*
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertFalse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Created by Yoris Prayogo on 14/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

@ExperimentalCoroutinesApi
class HomeAccountUserViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val homeAccountUserUsecase = mockk<HomeAccountUserUsecase>(relaxed = true)
    private val homeAccountShortcutUseCase = mockk<HomeAccountShortcutUseCase>(relaxed = true)
    private val homeAccountWalletBalanceUseCase = mockk<HomeAccountWalletBalanceUseCase>(relaxed = true)
    private val homeAccountSafeSettingProfileUseCase = mockk<SafeSettingProfileUseCase>(relaxed = true)
    private val homeAccountRecommendationUseCase = mockk<GetRecommendationUseCase>(relaxed = true)
    private val userPageAssetConfigUseCase = mockk<GetUserPageAssetConfigUseCase>(relaxed = true)
    private val homeAccountSaldoBalanceUseCase = mockk<HomeAccountSaldoBalanceUseCase>(relaxed = true)
    private val homeAccountTokopointsUseCase = mockk<HomeAccountTokopointsUseCase>(relaxed = true)

    private val userPageAssetConfigObserver = mockk<Observer<Result<UserPageAssetConfig>>>(relaxed = true)
    private val saldoBalanceObserver = mockk<Observer<Result<Balance>>>(relaxed = true)
    private val tokopointsDrawerListObserver = mockk<Observer<Result<TokopointsDrawerList>>>(relaxed = true)

    private val userSession = mockk<UserSessionInterface>(relaxed = true)
    private val walletPref = mockk<WalletPref>(relaxed = true)
    private val accountPref = mockk<AccountPreference>(relaxed = true)

    private val dispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: HomeAccountUserViewModel

    private val throwable = Fail(Throwable(message = "Error"))
    private var buyerAccountObserver = mockk<Observer<Result<UserAccountDataModel>>>(relaxed = true)

    private val wallet = WalletModel().apply {
        isLinked = true
    }
    private val shortcut = ShortcutResponse()
    private val responseResult = UserAccountDataModel()

    @Before
    fun setUp() {
        viewModel = HomeAccountUserViewModel(
                userSession,
                accountPref,
                homeAccountUserUsecase,
                homeAccountShortcutUseCase,
                homeAccountWalletBalanceUseCase,
                homeAccountSafeSettingProfileUseCase,
                homeAccountRecommendationUseCase,
                userPageAssetConfigUseCase,
                homeAccountSaldoBalanceUseCase,
                homeAccountTokopointsUseCase,
                walletPref,
                dispatcher
        )

        viewModel.buyerAccountDataData.observeForever(buyerAccountObserver)
    }

    @Test
    fun `Execute getBuyerData Success`() {
        /* When */
        coEvery { homeAccountUserUsecase.executeOnBackground() } returns responseResult
        every { homeAccountWalletBalanceUseCase.createObservable(RequestParams.EMPTY).toBlocking().single() } returns wallet
        coEvery { homeAccountShortcutUseCase.executeOnBackground() } returns shortcut

        viewModel.getBuyerData()
        verify {
            viewModel.saveLocallyAttributes(responseResult)
        }
        Assertions.assertThat(viewModel.buyerAccountDataData.value).isEqualTo(Success(responseResult))
    }

    @Test
    fun `Execute getBuyerData Failed`() {
        /* When */
        coEvery { homeAccountUserUsecase.executeOnBackground() } throws throwable.throwable
        every { homeAccountWalletBalanceUseCase.createObservable(RequestParams.EMPTY).toBlocking().single() } returns wallet
        coEvery { homeAccountShortcutUseCase.executeOnBackground() } returns shortcut

        viewModel.getBuyerData()
        Assertions.assertThat(viewModel.buyerAccountDataData.value).isEqualTo(throwable)
    }

    @Test
    fun `Execute saveLocallyAttributes`() {
        val debitInstandData = mockk<DebitInstantData>(relaxed = true)
        val debitInstantModel = mockk<DebitInstantModel>(relaxed = true)

        every { debitInstandData.redirectUrl } returns "redirect"
        every { debitInstantModel.data } returns debitInstandData

        /* When */
        val response = UserAccountDataModel(
                wallet = WalletModel().apply { isLinked = true },
                profile = ProfileModel().apply { isPhoneVerified = true },
                isAffiliate = true,
                debitInstant = debitInstantModel
        )

        viewModel.saveLocallyAttributes(response)

        verify {
            userSession.setIsMSISDNVerified(response.profile.isPhoneVerified)
            userSession.setIsAffiliateStatus(response.isAffiliate)
        }
    }

    @Test
    fun `Execute getInitialData`() {
        viewModel.getBuyerData()

        assertFalse(viewModel.settingData.value?.items?.isEmpty() == true)
        assertFalse(viewModel.settingApplication.value?.items?.isEmpty() == true)
        assertFalse(viewModel.aboutTokopedia.value?.items?.isEmpty() == true)
    }

    @Test
    fun `Successfully get recommendation first page`() {
        val expectedResult = mockk<RecommendationWidget>(relaxed = true)
        every {
            homeAccountRecommendationUseCase.createObservable(any()).toBlocking().single()[0]
        } returns expectedResult

        viewModel.getFirstRecommendation()

        val result = viewModel.firstRecommendationData.getOrAwaitValue()
        Assertions.assertThat(result).isEqualTo(Success(expectedResult))
    }

    @Test
    fun `Successfully get recommendation load more`() {
        val testPage = 2
        val expectedResult = mockk<RecommendationWidget>(relaxed = true)
        every {
            homeAccountRecommendationUseCase.createObservable(any()).toBlocking().single()[0]
        } returns expectedResult

        viewModel.getRecommendation(testPage)

        val result = viewModel.getRecommendationData.getOrAwaitValue()
        Assertions.assertThat(result).isEqualTo(Success(expectedResult.recommendationItemList))
    }

    @Test
    fun `Failed to get first recommendation`() {
        val expectedResult = mockk<Throwable>(relaxed = true)
        every {
            homeAccountRecommendationUseCase.createObservable(any()).toBlocking().single()[0]
        } throws expectedResult

        viewModel.getFirstRecommendation()

        val result = viewModel.firstRecommendationData.getOrAwaitValue()
        Assertions.assertThat(result).isEqualTo(Fail(expectedResult))
    }

    @Test
    fun `Failed to get more recommendation`() {
        val testPage = 2
        val expectedResult = mockk<Throwable>(relaxed = true)
        every {
            homeAccountRecommendationUseCase.createObservable(any()).toBlocking().single()[0]
        } throws expectedResult

        viewModel.getRecommendation(testPage)

        val result = viewModel.getRecommendationData.getOrAwaitValue()
        Assertions.assertThat(result).isEqualTo(Fail(expectedResult))
    }

    @Test
    fun `Success get user page asset config`() {
        viewModel.userPageAssetConfig.observeForever(userPageAssetConfigObserver)
        coEvery { userPageAssetConfigUseCase.executeOnBackground() } returns successGetUserPageAssetConfigResponse

        viewModel.getUserPageAssetConfig()

        verify { userPageAssetConfigObserver.onChanged(any<Success<UserPageAssetConfig>>()) }
        assert(viewModel.userPageAssetConfig.value is Success)

        val result = viewModel.userPageAssetConfig.value as Success<UserPageAssetConfig>
        assert(result.data == successGetUserPageAssetConfigResponse.data)
    }

    @Test
    fun `Failed get user page asset config`() {
        viewModel.userPageAssetConfig.observeForever(userPageAssetConfigObserver)
        coEvery { userPageAssetConfigUseCase.executeOnBackground() } coAnswers { throw throwableResponse }

        viewModel.getUserPageAssetConfig()

        verify { userPageAssetConfigObserver.onChanged(any<Fail>()) }
        assert(viewModel.userPageAssetConfig.value is Fail)

        val result = viewModel.userPageAssetConfig.value as Fail
        assertEquals(throwableResponse, result.throwable)
    }

    @Test
    fun `Success get saldo balance`() {
        viewModel.saldoBalance.observeForever(saldoBalanceObserver)
        coEvery { homeAccountSaldoBalanceUseCase.executeOnBackground() } returns successGetSaldoBalanceResponse

        viewModel.getSaldoBalance()

        verify { saldoBalanceObserver.onChanged(any<Success<Balance>>()) }
        assert(viewModel.saldoBalance.value is Success)

        val result = viewModel.saldoBalance.value as Success<Balance>
        assert(result.data == successGetSaldoBalanceResponse.data)
    }

    @Test
    fun `Failed get saldo balance`() {
        viewModel.saldoBalance.observeForever(saldoBalanceObserver)
        coEvery { homeAccountSaldoBalanceUseCase.executeOnBackground() } coAnswers { throw throwableResponse }

        viewModel.getSaldoBalance()

        verify { saldoBalanceObserver.onChanged(any<Fail>()) }
        assert(viewModel.saldoBalance.value is Fail)

        val result = viewModel.saldoBalance.value as Fail
        assertEquals(throwableResponse, result.throwable)
    }

    @Test
    fun `Success get tokopoints`() {
        viewModel.tokopointsDrawerList.observeForever(tokopointsDrawerListObserver)
        coEvery { homeAccountTokopointsUseCase.executeOnBackground() } returns successGetTokopointsResponse

        viewModel.getTokopoints()

        verify { tokopointsDrawerListObserver.onChanged(any<Success<TokopointsDrawerList>>()) }
        assert(viewModel.tokopointsDrawerList.value is Success)

        val result = viewModel.tokopointsDrawerList.value as Success<TokopointsDrawerList>
        assert(result.data == successGetTokopointsResponse.data)
    }

    @Test
    fun `Failed get tokopoints`() {
        viewModel.tokopointsDrawerList.observeForever(tokopointsDrawerListObserver)
        coEvery { homeAccountTokopointsUseCase.executeOnBackground() } coAnswers { throw throwableResponse }

        viewModel.getTokopoints()

        verify { tokopointsDrawerListObserver.onChanged(any<Fail>()) }
        assert(viewModel.tokopointsDrawerList.value is Fail)

        val result = viewModel.tokopointsDrawerList.value as Fail
        assertEquals(throwableResponse, result.throwable)
    }

    companion object {
        private val successGetUserPageAssetConfigResponse: UserPageAssetConfigDataModel = FileUtil.parse(
                "/success_get_user_page_asset_config.json",
                UserPageAssetConfigDataModel::class.java
        )
        private val successGetSaldoBalanceResponse: BalanceDataModel = FileUtil.parse(
                "/success_get_saldo_balance.json",
                BalanceDataModel::class.java
        )
        private val successGetTokopointsResponse: TokopointsDataModel = FileUtil.parse(
                "/success_get_tokopoints.json",
                TokopointsDataModel::class.java
        )
        private val throwableResponse = Throwable()
    }
}