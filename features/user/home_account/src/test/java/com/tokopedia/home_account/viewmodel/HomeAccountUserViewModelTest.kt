package com.tokopedia.home_account.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.home_account.PermissionChecker
import com.tokopedia.home_account.data.model.ShortcutResponse
import com.tokopedia.home_account.data.model.UserAccountDataModel
import com.tokopedia.home_account.domain.usecase.HomeAccountShortcutUseCase
import com.tokopedia.home_account.domain.usecase.HomeAccountUserUsecase
import com.tokopedia.home_account.domain.usecase.HomeAccountWalletBalanceUseCase
import com.tokopedia.home_account.domain.usecase.SafeSettingProfileUseCase
import com.tokopedia.home_account.getOrAwaitValue
import com.tokopedia.home_account.pref.AccountPreference
import com.tokopedia.home_account.view.HomeAccountUserViewModel
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
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Yoris Prayogo on 14/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class HomeAccountUserViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val homeAccountUserUsecase = mockk<HomeAccountUserUsecase>(relaxed = true)
    val homeAccountShortcutUseCase = mockk<HomeAccountShortcutUseCase>(relaxed = true)
    val homeAccountWalletBalanceUseCase = mockk<HomeAccountWalletBalanceUseCase>(relaxed = true)
    private val homeAccountSafeSettingProfileUseCase = mockk<SafeSettingProfileUseCase>(relaxed = true)
    private val homeAccountRecommendationUseCase = mockk<GetRecommendationUseCase>(relaxed = true)

    val userSession = mockk<UserSessionInterface>(relaxed = true)
    val walletPref = mockk<WalletPref>(relaxed = true)
    val accountPref = mockk<AccountPreference>(relaxed = true)

    val permissionChecker = mockk<PermissionChecker>(relaxed = true)

    val dispatcher = TestCoroutineDispatcher()
    lateinit var viewModel: HomeAccountUserViewModel

    val throwable = Fail(Throwable(message = "Error"))
    private var buyerAccountObserver = mockk<Observer<Result<UserAccountDataModel>>>(relaxed = true)

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
                walletPref,
                dispatcher
        )

        viewModel.buyerAccountDataData.observeForever(buyerAccountObserver)
    }

    val wallet = WalletModel().apply {
        isLinked = true
    }
    val shortcut = ShortcutResponse()

    val responseResult = UserAccountDataModel()

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
        val debitInstantModel = mockk<DebitInstantModel>(relaxed = true)
        val debitInstandData = mockk<DebitInstantData>(relaxed = true)

        every { debitInstandData.redirectUrl } returns "redirect"
        every { debitInstantModel.data } returns debitInstandData

        /* When */
        val response = UserAccountDataModel(vccUserStatus = VccUserStatus().apply {
            status = "status"
            redirectionUrl = "http://redirect"
        }, wallet = WalletModel().apply {
            isLinked = true
        }, profile = ProfileModel().apply {
            isPhoneVerified = true
        }, isAffiliate = true,
        debitInstant = debitInstantModel)

        viewModel.saveLocallyAttributes(response)
        verify {
            walletPref.saveWallet(response.wallet)
            walletPref.tokoSwipeUrl = response.vccUserStatus.redirectionUrl
            walletPref.saveVccUserStatus(response.vccUserStatus)
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

        verify {
            homeAccountRecommendationUseCase.createObservable(any())
        }

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

        verify {
            homeAccountRecommendationUseCase.createObservable(any())
        }

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

        verify {
            homeAccountRecommendationUseCase.createObservable(any())
        }

        val result = viewModel.getRecommendationData.getOrAwaitValue()
        Assertions.assertThat(result).isEqualTo(Fail(expectedResult))
    }

}