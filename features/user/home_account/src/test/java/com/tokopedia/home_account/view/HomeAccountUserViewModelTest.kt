package com.tokopedia.home_account.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.FileUtil
import com.tokopedia.home_account.ResultBalanceAndPoint
import com.tokopedia.home_account.data.model.*
import com.tokopedia.home_account.domain.usecase.*
import com.tokopedia.home_account.getOrAwaitValue
import com.tokopedia.home_account.pref.AccountPreference
import com.tokopedia.navigation_common.model.*
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import junit.framework.Assert.assertFalse
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.assertj.core.api.Assertions
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.IllegalArgumentException
import kotlin.test.assertEquals

/**
 * Created by Yoris Prayogo on 14/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class HomeAccountUserViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val homeAccountUserUsecase = mockk<HomeAccountUserUsecase>(relaxed = true)
    private val homeAccountShortcutUseCase = mockk<HomeAccountShortcutUseCase>(relaxed = true)
    private val homeAccountWalletBalanceUseCase =
        mockk<HomeAccountWalletBalanceUseCase>(relaxed = true)
    private val homeAccountSafeSettingProfileUseCase =
        mockk<SafeSettingProfileUseCase>(relaxed = true)
    private val homeAccountRecommendationUseCase = mockk<GetRecommendationUseCase>(relaxed = true)
    private val userPageAssetConfigUseCase = mockk<GetUserPageAssetConfigUseCase>(relaxed = true)
    private val homeAccountSaldoBalanceUseCase =
        mockk<HomeAccountSaldoBalanceUseCase>(relaxed = true)
    private val homeAccountTokopointsUseCase = mockk<HomeAccountTokopointsUseCase>(relaxed = true)
    private val centralizedUserAssetConfigUseCase =
        mockk<GetCentralizedUserAssetConfigUseCase>(relaxed = true)
    private val balanceAndPointUseCase = mockk<GetBalanceAndPointUseCase>(relaxed = true)
    private val tokopointsBalanceAndPointUseCase =
        mockk<GetTokopointsBalanceAndPointUseCase>(relaxed = true)
    private val walletEligibleUseCase = mockk<GetWalletEligibleUseCase>(relaxed = true)

    private val userPageAssetConfigObserver =
        mockk<Observer<Result<UserPageAssetConfig>>>(relaxed = true)
    private val saldoBalanceObserver = mockk<Observer<Result<Balance>>>(relaxed = true)
    private val tokopointsDrawerListObserver =
        mockk<Observer<Result<TokopointsDrawerList>>>(relaxed = true)
    private val shortCutResponse = mockk<Observer<Result<ShortcutResponse>>>(relaxed = true)
    private val ovoBalanceResponse = mockk<Observer<Result<WalletModel>>>(relaxed = true)
    private val centralizedUserAssetConfigObserver = mockk<Observer<Result<CentralizedUserAssetConfig>>>(relaxed = true)
    private val walletEligibleObserver = mockk<Observer<Result<WalletappWalletEligibility>>>(relaxed = true)
    private val balanceAndPointOvserver = mockk<Observer<ResultBalanceAndPoint<WalletappGetAccountBalance>>>(relaxed = true)

    private val userSession = mockk<UserSessionInterface>(relaxed = true)
    private val walletPref = mockk<WalletPref>(relaxed = true)
    private val accountPref = mockk<AccountPreference>(relaxed = true)

    private val dispatcher = CoroutineTestDispatchersProvider
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
            centralizedUserAssetConfigUseCase,
            balanceAndPointUseCase,
            tokopointsBalanceAndPointUseCase,
            walletEligibleUseCase,
            walletPref,
            dispatcher
        )

        viewModel.buyerAccountDataData.observeForever(buyerAccountObserver)
        viewModel.shortcutData.observeForever(shortCutResponse)
        viewModel.ovoBalance.observeForever(ovoBalanceResponse)
    }

    @Test
    fun `Execute getBuyerData Success`() {
        /* When */
        coEvery { homeAccountUserUsecase.executeOnBackground() } returns responseResult
        every {
            homeAccountWalletBalanceUseCase.createObservable(RequestParams.EMPTY).toBlocking()
                .single()
        } returns wallet
        coEvery { homeAccountShortcutUseCase.executeOnBackground() } returns shortcut

        viewModel.getBuyerData()
        verify {
            viewModel.saveLocallyAttributes(responseResult)
        }
        Assertions.assertThat(viewModel.buyerAccountDataData.value)
            .isEqualTo(Success(responseResult))
    }

    @Test
    fun `Execute getBuyerData Failed`() {
        /* When */
        coEvery { homeAccountUserUsecase.executeOnBackground() } throws throwable.throwable
        every {
            homeAccountWalletBalanceUseCase.createObservable(RequestParams.EMPTY).toBlocking()
                .single()
        } returns wallet
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
        val expectedResult = mutableListOf(RecommendationWidget())
        coEvery {
            homeAccountRecommendationUseCase.createObservable(any()).toBlocking().first()
        } returns expectedResult

        viewModel.getFirstRecommendation()

        coVerify {
            homeAccountRecommendationUseCase.createObservable(any())
        }
        print(viewModel.firstRecommendationData.value)
        Assert.assertEquals((viewModel.firstRecommendationData.value as Success).data, expectedResult[0])
    }

    @Test
    fun `Successfully get recommendation load more`() {
        val testPage = 2
        val expectedResult = mutableListOf(RecommendationWidget())
        coEvery {
            homeAccountRecommendationUseCase.createObservable(any()).toBlocking().first()
        } returns expectedResult

        viewModel.getRecommendation(testPage)

        coVerify {
            homeAccountRecommendationUseCase.createObservable(any())
        }
        print(viewModel.getRecommendationData.value)
        Assert.assertEquals((viewModel.getRecommendationData.value as Success).data, expectedResult[0].recommendationItemList)
    }

    @Test
    fun `Failed to get first recommendation`() {
        val expectedResult = Throwable()
        coEvery {
            homeAccountRecommendationUseCase.createObservable(any()).toBlocking()
        } throws expectedResult

        viewModel.getFirstRecommendation()

        coVerify {
            homeAccountRecommendationUseCase.createObservable(any())
        }
        print(viewModel.firstRecommendationData.value)
        Assert.assertEquals((viewModel.firstRecommendationData.value as Fail).throwable, expectedResult)
    }

    @Test
    fun `Failed to get more recommendation`() {
        val testPage = 2
        val expectedResult = Throwable()
        coEvery {
            homeAccountRecommendationUseCase.createObservable(any()).toBlocking()
        } throws expectedResult

        viewModel.getRecommendation(testPage)

        coVerify {
            homeAccountRecommendationUseCase.createObservable(any())
        }
        print(viewModel.getRecommendationData.value)
        Assert.assertEquals((viewModel.getRecommendationData.value as Fail).throwable, expectedResult)
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

    @Test
    fun `Set safe mode success`() {
        val data = SetUserProfileSetting(isSuccess = true, error = "")
        val setUserProfileResponse = SetUserProfileSettingResponse(data)

        val isActive = true
        /* When */
        every {
            homeAccountSafeSettingProfileUseCase.executeQuerySetSafeMode(
                any(),
                any(),
                any()
            )
        } answers {
            firstArg<(SetUserProfileSettingResponse) -> Unit>().invoke(setUserProfileResponse)
        }

        viewModel.setSafeMode(isActive)

        verify {
            accountPref.saveSettingValue(AccountConstants.KEY.KEY_PREF_SAFE_SEARCH, isActive)
            accountPref.saveSettingValue(AccountConstants.KEY.CLEAR_CACHE, isActive)
        }
    }

    @Test
    fun `get shortcut data success`() {
        /* When */
        coEvery { homeAccountShortcutUseCase.executeOnBackground() } returns shortcut

        viewModel.getShortcutData()

        verify {
            shortCutResponse.onChanged(Success(shortcut))
        }
    }

    @Test
    fun `get shortcut data fail`() {
        /* When */
        coEvery { homeAccountShortcutUseCase.executeOnBackground() } throws throwableResponse

        viewModel.getShortcutData()

        verify {
            shortCutResponse.onChanged(Fail(throwableResponse))
        }
    }

    @Test
    fun `get ovo balance success`() {
        val walletModel = WalletModel().apply {
            text = "test text"
        }
        /* When */
        every { viewModel.getBuyerOvoBalance() } returns walletModel

        viewModel.getOvoBalance()

        verify {
            ovoBalanceResponse.onChanged(Success(walletModel))
            walletPref.saveWallet(walletModel)
        }
    }

    @Test
    fun `get ovo balance fail`() {
        /* When */
        coEvery { viewModel.getBuyerOvoBalance() } throws throwableResponse

        viewModel.getOvoBalance()

        verify {
            ovoBalanceResponse.onChanged(Fail(throwableResponse))
        }
    }

    @Test
    fun `Success get centralized user asset config`() {
        viewModel.centralizedUserAssetConfig.observeForever(centralizedUserAssetConfigObserver)
        coEvery { centralizedUserAssetConfigUseCase(any()) } returns successGetCentralizedUserAssetConfigResponse

        viewModel.getCentralizedUserAssetConfig("")

        verify { centralizedUserAssetConfigObserver.onChanged(any<Success<CentralizedUserAssetConfig>>()) }
        assert(viewModel.centralizedUserAssetConfig.value is Success)

        val result = viewModel.centralizedUserAssetConfig.value as Success<CentralizedUserAssetConfig>
        assert(result.data == successGetCentralizedUserAssetConfigResponse.data)
    }

    @Test
    fun `Failed get centralized user asset config`() {
        viewModel.centralizedUserAssetConfig.observeForever(centralizedUserAssetConfigObserver)
        coEvery { centralizedUserAssetConfigUseCase(any()) } coAnswers { throw throwableResponse }

        viewModel.getCentralizedUserAssetConfig("")

        verify { centralizedUserAssetConfigObserver.onChanged(any<Fail>()) }
        assert(viewModel.centralizedUserAssetConfig.value is Fail)

        val result = viewModel.centralizedUserAssetConfig.value as Fail
        assertEquals(throwableResponse, result.throwable)
    }

    @Test
    fun `Success get gopay balance and point`() {
        viewModel.balanceAndPoint.observeForever(balanceAndPointOvserver)
        coEvery { balanceAndPointUseCase(any()) } returns successGetBalanceAndPointResponse

        viewModel.getBalanceAndPoint(AccountConstants.WALLET.GOPAY)

        verify { balanceAndPointOvserver.onChanged(any<ResultBalanceAndPoint.Success<WalletappGetAccountBalance>>()) }
        assert(viewModel.balanceAndPoint.value is ResultBalanceAndPoint.Success)

        val result = viewModel.balanceAndPoint.value as ResultBalanceAndPoint.Success<WalletappGetAccountBalance>
        assert(result.data == successGetBalanceAndPointResponse.data)
    }

    @Test
    fun `Failed get gopay balance and point`() {
        viewModel.balanceAndPoint.observeForever(balanceAndPointOvserver)
        coEvery { balanceAndPointUseCase(any()) } coAnswers { throw throwableResponse }

        viewModel.getBalanceAndPoint(AccountConstants.WALLET.GOPAY)

        verify { balanceAndPointOvserver.onChanged(any()) }
        assert(viewModel.balanceAndPoint.value is ResultBalanceAndPoint.Fail)

        val result = viewModel.balanceAndPoint.value as ResultBalanceAndPoint.Fail
        assertEquals(throwableResponse, result.throwable)
    }

    @Test
    fun `Success get gopaylater balance and point`() {
        viewModel.balanceAndPoint.observeForever(balanceAndPointOvserver)
        coEvery { balanceAndPointUseCase(any()) } returns successGetBalanceAndPointResponse

        viewModel.getBalanceAndPoint(AccountConstants.WALLET.GOPAYLATER)

        verify { balanceAndPointOvserver.onChanged(any<ResultBalanceAndPoint.Success<WalletappGetAccountBalance>>()) }
        assert(viewModel.balanceAndPoint.value is ResultBalanceAndPoint.Success)

        val result = viewModel.balanceAndPoint.value as ResultBalanceAndPoint.Success<WalletappGetAccountBalance>
        assert(result.data == successGetBalanceAndPointResponse.data)
    }

    @Test
    fun `Failed get gopaylater balance and point`() {
        viewModel.balanceAndPoint.observeForever(balanceAndPointOvserver)
        coEvery { balanceAndPointUseCase(any()) } coAnswers { throw throwableResponse }

        viewModel.getBalanceAndPoint(AccountConstants.WALLET.GOPAYLATER)

        verify { balanceAndPointOvserver.onChanged(any()) }
        assert(viewModel.balanceAndPoint.value is ResultBalanceAndPoint.Fail)

        val result = viewModel.balanceAndPoint.value as ResultBalanceAndPoint.Fail
        assertEquals(throwableResponse, result.throwable)
    }

    @Test
    fun `Success get ovo balance and point`() {
        viewModel.balanceAndPoint.observeForever(balanceAndPointOvserver)
        coEvery { balanceAndPointUseCase(any()) } returns successGetBalanceAndPointResponse

        viewModel.getBalanceAndPoint(AccountConstants.WALLET.OVO)

        verify { balanceAndPointOvserver.onChanged(any<ResultBalanceAndPoint.Success<WalletappGetAccountBalance>>()) }
        assert(viewModel.balanceAndPoint.value is ResultBalanceAndPoint.Success)

        val result = viewModel.balanceAndPoint.value as ResultBalanceAndPoint.Success<WalletappGetAccountBalance>
        assert(result.data == successGetBalanceAndPointResponse.data)
    }

    @Test
    fun `Failed get ovo balance and point`() {
        viewModel.balanceAndPoint.observeForever(balanceAndPointOvserver)
        coEvery { balanceAndPointUseCase(any()) } coAnswers { throw throwableResponse }

        viewModel.getBalanceAndPoint(AccountConstants.WALLET.OVO)

        verify { balanceAndPointOvserver.onChanged(any()) }
        assert(viewModel.balanceAndPoint.value is ResultBalanceAndPoint.Fail)

        val result = viewModel.balanceAndPoint.value as ResultBalanceAndPoint.Fail
        assertEquals(throwableResponse, result.throwable)
    }

    @Test
    fun `Success get tokopoint balance and point`() {
        viewModel.balanceAndPoint.observeForever(balanceAndPointOvserver)
        coEvery { tokopointsBalanceAndPointUseCase(Unit) } returns successGetBalanceAndPointResponse

        viewModel.getBalanceAndPoint(AccountConstants.WALLET.TOKOPOINT)

        verify { balanceAndPointOvserver.onChanged(any<ResultBalanceAndPoint.Success<WalletappGetAccountBalance>>()) }
        assert(viewModel.balanceAndPoint.value is ResultBalanceAndPoint.Success)

        val result = viewModel.balanceAndPoint.value as ResultBalanceAndPoint.Success<WalletappGetAccountBalance>
        assert(result.data == successGetBalanceAndPointResponse.data)
    }

    @Test
    fun `Failed get tokopoint balance and point`() {
        viewModel.balanceAndPoint.observeForever(balanceAndPointOvserver)
        coEvery { tokopointsBalanceAndPointUseCase(Unit) } coAnswers { throw throwableResponse }

        viewModel.getBalanceAndPoint(AccountConstants.WALLET.TOKOPOINT)

        verify { balanceAndPointOvserver.onChanged(any()) }
        assert(viewModel.balanceAndPoint.value is ResultBalanceAndPoint.Fail)

        val result = viewModel.balanceAndPoint.value as ResultBalanceAndPoint.Fail
        assertEquals(throwableResponse, result.throwable)
    }

    @Test
    fun `Failed get balance and point`() {
        viewModel.balanceAndPoint.observeForever(balanceAndPointOvserver)

        viewModel.getBalanceAndPoint("")

        verify { balanceAndPointOvserver.onChanged(any()) }
        assert(viewModel.balanceAndPoint.value is ResultBalanceAndPoint.Fail)

        val result = viewModel.balanceAndPoint.value as ResultBalanceAndPoint.Fail
        assert(result.throwable is IllegalArgumentException)
    }


    @Test
    fun `Success get wallet eligible`() {
        viewModel.walletEligible.observeForever(walletEligibleObserver)
        coEvery { walletEligibleUseCase(any()) } returns successGetWalletEligibleResponse

        viewModel.getWalletEligible("", "")

        verify { walletEligibleObserver.onChanged(any<Success<WalletappWalletEligibility>>()) }
        assert(viewModel.walletEligible.value is Success)

        val result = viewModel.walletEligible.value as Success<WalletappWalletEligibility>
        assert(result.data == successGetWalletEligibleResponse.data)
    }

    @Test
    fun `Failed get wallet eligible`() {
        viewModel.walletEligible.observeForever(walletEligibleObserver)
        coEvery { walletEligibleUseCase(any()) } coAnswers { throw throwableResponse }

        viewModel.getWalletEligible("", "")

        verify { walletEligibleObserver.onChanged(any()) }
        assert(viewModel.walletEligible.value is Fail)

        val result = viewModel.walletEligible.value as Fail
        assertEquals(throwableResponse, result.throwable)
    }

    companion object {
        private val successGetUserPageAssetConfigResponse: UserPageAssetConfigDataModel =
            FileUtil.parse(
                "/success_get_user_page_asset_config.json",
                UserPageAssetConfigDataModel::class.java
            )
        private val successGetSaldoBalanceResponse: SaldoBalanceDataModel =
            FileUtil.parse(
                "/success_get_saldo_balance.json",
                SaldoBalanceDataModel::class.java
            )
        private val successGetTokopointsResponse: TokopointsDataModel = FileUtil.parse(
            "/success_get_tokopoints.json",
            TokopointsDataModel::class.java
        )
        private val successGetCentralizedUserAssetConfigResponse: CentralizedUserAssetDataModel = FileUtil.parse(
            "/success_get_centralized_user_asset_config.json",
            CentralizedUserAssetDataModel::class.java
        )
        private val successGetBalanceAndPointResponse: BalanceAndPointDataModel = FileUtil.parse(
            "/success_get_balance_and_point.json",
            BalanceAndPointDataModel::class.java
        )
        private val successGetWalletEligibleResponse: WalletEligibleDataModel = FileUtil.parse(
            "/success_get_wallet_eligible.json",
            WalletEligibleDataModel::class.java
        )
        private val throwableResponse = Throwable()
    }
}