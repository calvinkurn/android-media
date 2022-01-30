package com.tokopedia.home_account.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.FileUtil
import com.tokopedia.home_account.ResultBalanceAndPoint
import com.tokopedia.home_account.account_settings.data.model.UserProfileSetting
import com.tokopedia.home_account.account_settings.data.model.UserProfileSettingResponse
import com.tokopedia.home_account.account_settings.domain.UserProfileSafeModeUseCase
import com.tokopedia.home_account.data.model.*
import com.tokopedia.home_account.domain.usecase.*
import com.tokopedia.home_account.linkaccount.data.LinkStatusResponse
import com.tokopedia.home_account.linkaccount.domain.GetLinkStatusUseCase
import com.tokopedia.home_account.linkaccount.domain.GetUserProfile
import com.tokopedia.home_account.pref.AccountPreference
import com.tokopedia.loginfingerprint.data.model.CheckFingerprintPojo
import com.tokopedia.loginfingerprint.data.model.CheckFingerprintResult
import com.tokopedia.loginfingerprint.domain.usecase.CheckFingerprintToggleStatusUseCase
import com.tokopedia.navigation_common.model.DebitInstantData
import com.tokopedia.navigation_common.model.DebitInstantModel
import com.tokopedia.navigation_common.model.ProfileModel
import com.tokopedia.navigation_common.model.WalletPref
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import junit.framework.Assert.assertFalse
import org.assertj.core.api.Assertions
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
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
    private val homeAccountSafeSettingProfileUseCase =
        mockk<SafeSettingProfileUseCase>(relaxed = true)
    private val homeAccountRecommendationUseCase = mockk<GetRecommendationUseCase>(relaxed = true)
    private val centralizedUserAssetConfigUseCase =
        mockk<GetCentralizedUserAssetConfigUseCase>(relaxed = true)
    private val balanceAndPointUseCase = mockk<GetBalanceAndPointUseCase>(relaxed = true)
    private val tokopointsBalanceAndPointUseCase =
        mockk<GetTokopointsBalanceAndPointUseCase>(relaxed = true)
    private val saldoBalanceUseCase = mockk<GetSaldoBalanceUseCase>(relaxed = true)
    private val coBrandCCBalanceAndPointUseCase = mockk<GetCoBrandCCBalanceAndPointUseCase>(relaxed = true)
    private val getLinkStatusUseCase = mockk<GetLinkStatusUseCase>(relaxed = true)
    private val getPhoneUseCase = mockk<GetUserProfile>(relaxed = true)
    private val topAdsImageViewUseCase = mockk<TopAdsImageViewUseCase>(relaxed = true)
    private val userProfileSafeModeUseCase = mockk<UserProfileSafeModeUseCase>(relaxed = true)
    private val checkFingerprintToggleUseCase = mockk<CheckFingerprintToggleStatusUseCase>(relaxed = true)

    private val shortCutResponse = mockk<Observer<Result<ShortcutResponse>>>(relaxed = true)
    private val centralizedUserAssetConfigObserver = mockk<Observer<Result<CentralizedUserAssetConfig>>>(relaxed = true)
    private val balanceAndPointOvserver = mockk<Observer<ResultBalanceAndPoint<WalletappGetAccountBalance>>>(relaxed = true)

    private val safeStatusResponse = mockk<Observer<Boolean>>(relaxed = true)

    private val userSession = mockk<UserSessionInterface>(relaxed = true)
    private val walletPref = mockk<WalletPref>(relaxed = true)
    private val accountPref = mockk<AccountPreference>(relaxed = true)

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var viewModel: HomeAccountUserViewModel

    private val throwable = Fail(Throwable(message = "Error"))
    private var buyerAccountObserver = mockk<Observer<Result<UserAccountDataModel>>>(relaxed = true)
    private var checkFingerprintResult = mockk<Observer<Result<CheckFingerprintResult>>>(relaxed = true)

    private val shortcut = ShortcutResponse()
    private val responseResult = UserAccountDataModel()
    private val linkStatusResult = LinkStatusResponse()
    private val profilePojo = ProfilePojo(profileInfo = ProfileInfo(phone = "089123456789"))
    private val throwableMock = mockk<Throwable>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = HomeAccountUserViewModel(
            userSession,
            accountPref,
            homeAccountUserUsecase,
            homeAccountShortcutUseCase,
            homeAccountSafeSettingProfileUseCase,
            homeAccountRecommendationUseCase,
            topAdsImageViewUseCase,
            centralizedUserAssetConfigUseCase,
            balanceAndPointUseCase,
            tokopointsBalanceAndPointUseCase,
            saldoBalanceUseCase,
            coBrandCCBalanceAndPointUseCase,
            getLinkStatusUseCase,
            getPhoneUseCase,
            userProfileSafeModeUseCase,
            checkFingerprintToggleUseCase,
            walletPref,
            dispatcher
        )

        viewModel.buyerAccountDataData.observeForever(buyerAccountObserver)
        viewModel.shortcutData.observeForever(shortCutResponse)
        viewModel.safeModeStatus.observeForever(safeStatusResponse)
        viewModel.checkFingerprintStatus.observeForever(checkFingerprintResult)
    }

    @Test
    fun `Execute refreshPhoneNo Success`() {
        coEvery { getPhoneUseCase(Unit) } returns profilePojo

        viewModel.refreshPhoneNo()

        verify {
            userSession.phoneNumber = profilePojo.profileInfo.phone
        }
        Assertions.assertThat(viewModel.phoneNo.value)
            .isEqualTo(profilePojo.profileInfo.phone)
    }

    @Test
    fun `Execute refreshPhoneNo Success but phone empty`() {
        coEvery { getPhoneUseCase(Unit) } returns ProfilePojo()

        viewModel.refreshPhoneNo()

        Assertions.assertThat(viewModel.phoneNo.value)
            .isEqualTo(null)
    }

    @Test
    fun `Execute refreshPhoneNo Failed`() {
        coEvery { getPhoneUseCase(Unit) } throws throwable.throwable

        viewModel.refreshPhoneNo()

        Assertions.assertThat(viewModel.phoneNo.value).isEqualTo("")
    }

    @Test
    fun `Execute getBuyerData Success`() {
        /* When */
        coEvery { homeAccountUserUsecase(Unit) } returns responseResult
        coEvery { homeAccountShortcutUseCase(Unit) } returns shortcut
        coEvery { getLinkStatusUseCase.invoke(any()) } returns linkStatusResult

        viewModel.getBuyerData()

        responseResult.linkStatus = linkStatusResult.response

        verify {
            viewModel.saveLocallyAttributes(responseResult)
        }
        Assertions.assertThat(viewModel.buyerAccountDataData.value)
            .isEqualTo(Success(responseResult))
    }

    @Test
    fun `Execute getBuyerData Failed`() {
        /* When */
        coEvery { homeAccountUserUsecase(Unit) } throws throwable.throwable
        coEvery { homeAccountShortcutUseCase(Unit) } throws throwable.throwable
        coEvery { getLinkStatusUseCase.invoke(any()) } throws throwable.throwable

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
        val recommendationData = mockk<RecommendationWidget>(relaxed = true)
        coEvery {
            homeAccountRecommendationUseCase.getData(any())
        } returns listOf(recommendationData)

        val expectedResult = RecommendationWidgetWithTDN(recommendationData,null)

        viewModel.getFirstRecommendation()

        print(viewModel.firstRecommendationData.value)
        Assert.assertEquals((viewModel.firstRecommendationData.value as Success).data, expectedResult)
    }

    @Test
    fun `Successfully get recommendation load more`() {
        val testPage = 2
        val expectedResult = mockk<RecommendationWidget>(relaxed = true)
        coEvery {
            homeAccountRecommendationUseCase.getData(any())
        } returns listOf(expectedResult)

        viewModel.getRecommendation(testPage)

        print(viewModel.getRecommendationData.value)
        Assert.assertEquals((viewModel.getRecommendationData.value as Success).data, expectedResult.recommendationItemList)
    }

    @Test
    fun `Successfully get recommendation with tdn data`() {
        val recomList = listOf(
                RecommendationItem(1),
                RecommendationItem(2),
                RecommendationItem(3),
                RecommendationItem(4)
        )
        val testPage = 1
        val expectedResult = RecommendationWidget(recommendationItemList = recomList)
        val topAdsData = TopAdsImageViewModel(imageUrl = "abc123")
        val mockTopAdsData = arrayListOf(topAdsData)

        println(expectedResult.recommendationItemList)
        coEvery {
            homeAccountRecommendationUseCase.getData(any())
        } returns listOf(expectedResult)

        coEvery { topAdsImageViewUseCase.getImageData(any()) } returns mockTopAdsData

        viewModel.getRecommendation(testPage)

        coVerify { topAdsImageViewUseCase.getImageData(any()) }
    }

    @Test
    fun `Successfully get recommendation with tdn data throw error`() {
        val recomList = listOf(
                RecommendationItem(1),
                RecommendationItem(2),
                RecommendationItem(3),
                RecommendationItem(4)
        )
        val testPage = 1
        val expectedResult = RecommendationWidget(recommendationItemList = recomList)
        val topAdsData = TopAdsImageViewModel(imageUrl = "abc123")

        println(expectedResult.recommendationItemList)
        coEvery {
            homeAccountRecommendationUseCase.getData(any())
        } returns listOf(expectedResult)

        coEvery { topAdsImageViewUseCase.getImageData(any()) } throws throwableMock

        viewModel.getRecommendation(testPage)

        Assert.assertEquals((viewModel.firstRecommendationData.value as Success).data.tdnBanner, null)
    }

    @Test
    fun `Failed to get first recommendation`() {
        val expectedResult = mockk<Throwable>(relaxed = true)
        coEvery {
            homeAccountRecommendationUseCase.getData(any())
        } throws expectedResult

        viewModel.getFirstRecommendation()

        print(viewModel.firstRecommendationData.value)
        Assert.assertEquals((viewModel.firstRecommendationData.value as Fail).throwable, expectedResult)
    }

    @Test
    fun `Failed to get more recommendation`() {
        val testPage = 2
        val expectedResult = mockk<Throwable>(relaxed = true)
        coEvery {
            homeAccountRecommendationUseCase.getData(any())
        } throws expectedResult

        viewModel.getRecommendation(testPage)

        print(viewModel.getRecommendationData.value)
        Assert.assertEquals((viewModel.getRecommendationData.value as Fail).throwable, expectedResult)
    }

    @Test
    fun `Get safe mode success`() {
        val isActive = true
        val data = UserProfileSetting(safeMode = isActive)
        val setUserProfileResponse = UserProfileSettingResponse(data)

        /* When */
        every {
            userProfileSafeModeUseCase.executeQuerySafeMode(
                any(),
                any()
            )
        } answers {
            firstArg<(UserProfileSettingResponse) -> Unit>().invoke(setUserProfileResponse)
        }

        viewModel.getSafeModeValue()

        verify {
            accountPref.saveSettingValue(AccountConstants.KEY.KEY_PREF_SAFE_SEARCH, isActive)
            safeStatusResponse.onChanged(isActive)
        }
    }

    @Test
    fun `Get safe mode success, return false`() {
        val isActive = false
        val data = UserProfileSetting(safeMode = isActive)
        val setUserProfileResponse = UserProfileSettingResponse(data)

        /* When */
        every {
            userProfileSafeModeUseCase.executeQuerySafeMode(
                any(),
                any()
            )
        } answers {
            firstArg<(UserProfileSettingResponse) -> Unit>().invoke(setUserProfileResponse)
        }

        viewModel.getSafeModeValue()

        verify {
            accountPref.saveSettingValue(AccountConstants.KEY.KEY_PREF_SAFE_SEARCH, isActive)
            safeStatusResponse.onChanged(isActive)
        }
    }

    @Test
    fun `Get safe mode failed`() {
        val isActive = true
        val data = UserProfileSetting(safeMode = isActive)
        val setUserProfileResponse = UserProfileSettingResponse(data)

        /* When */
        every {
            userProfileSafeModeUseCase.executeQuerySafeMode(
                any(),
                any()
            )
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwableMock)
        }

        viewModel.getSafeModeValue()

        verify(atLeast = 1) {
            throwableMock.printStackTrace()
        }
    }

    @Test
    fun `Set safe mode success`() {
        val data = SetUserProfileSetting(isSuccess = true, error = "")
        val setUserProfileResponse = SetUserProfileSettingResponse(data)

        val isActive = true

        val getData = UserProfileSetting(safeMode = isActive)
        val getSafeModeData = UserProfileSettingResponse(getData)

        /* When */
        every {
            userProfileSafeModeUseCase.executeQuerySafeMode(
                any(),
                any()
            )
        } answers {
            firstArg<(UserProfileSettingResponse) -> Unit>().invoke(getSafeModeData)
        }

        every {
            homeAccountSafeSettingProfileUseCase.executeQuerySetSafeMode(
                any(),
                any(),
                isActive
            )
        } answers {
            firstArg<(SetUserProfileSettingResponse) -> Unit>().invoke(setUserProfileResponse)
        }

        viewModel.setSafeMode(isActive)

        verify {
            accountPref.saveSettingValue(AccountConstants.KEY.KEY_PREF_SAFE_SEARCH, isActive)
            safeStatusResponse.onChanged(isActive)
        }
    }

    @Test
    fun `Set safe mode Failed`() {
        val isActive = true
        /* When */
        every {
            homeAccountSafeSettingProfileUseCase.executeQuerySetSafeMode(
                any(),
                any(),
                any()
            )
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwableMock)
        }

        viewModel.setSafeMode(isActive)

        justRun { throwableMock.printStackTrace() }
        verify(atLeast = 1) {
            throwableMock.printStackTrace()
        }
    }

    @Test
    fun `Set safe mode inactive success`() {
        val data = SetUserProfileSetting(isSuccess = true, error = "")
        val setUserProfileResponse = SetUserProfileSettingResponse(data)

        val isActive = false

        val getData = UserProfileSetting(safeMode = isActive)
        val getSafeModeData = UserProfileSettingResponse(getData)

        /* When */
        every {
            userProfileSafeModeUseCase.executeQuerySafeMode(
                any(),
                any()
            )
        } answers {
            firstArg<(UserProfileSettingResponse) -> Unit>().invoke(getSafeModeData)
        }

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
            safeStatusResponse.onChanged(isActive)
        }
    }

    @Test
    fun `Set safe mode inactive Failed`() {
        val isActive = false
        /* When */
        every {
            homeAccountSafeSettingProfileUseCase.executeQuerySetSafeMode(
                any(),
                any(),
                any()
            )
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwableMock)
        }

        viewModel.setSafeMode(isActive)

        justRun { throwableMock.printStackTrace() }
        verify(atLeast = 1) {
            throwableMock.printStackTrace()
        }
    }

    @Test
    fun `get shortcut data success`() {
        /* When */
        coEvery { homeAccountShortcutUseCase(Unit) } returns shortcut

        viewModel.getShortcutData()

        verify {
            shortCutResponse.onChanged(Success(shortcut))
        }
    }

    @Test
    fun `get shortcut data fail`() {
        /* When */
        coEvery { homeAccountShortcutUseCase(Unit) } throws throwableResponse

        viewModel.getShortcutData()

        verify {
            shortCutResponse.onChanged(Fail(throwableResponse))
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
        coEvery { tokopointsBalanceAndPointUseCase(Unit) } returns successGetTokopointBalanceAndPointResponse

        viewModel.getBalanceAndPoint(AccountConstants.WALLET.TOKOPOINT)

        verify { balanceAndPointOvserver.onChanged(any<ResultBalanceAndPoint.Success<WalletappGetAccountBalance>>()) }
        assert(viewModel.balanceAndPoint.value is ResultBalanceAndPoint.Success)

        val result = viewModel.balanceAndPoint.value as ResultBalanceAndPoint.Success<WalletappGetAccountBalance>
        assert(result.data == successGetTokopointBalanceAndPointResponse.data)
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
    fun `Success get saldo balance and point`() {
        viewModel.balanceAndPoint.observeForever(balanceAndPointOvserver)
        coEvery { saldoBalanceUseCase(Unit) } returns successGetSaldoBalanceAndPointResponse

        viewModel.getBalanceAndPoint(AccountConstants.WALLET.SALDO)

        verify { balanceAndPointOvserver.onChanged(any<ResultBalanceAndPoint.Success<WalletappGetAccountBalance>>()) }
        assert(viewModel.balanceAndPoint.value is ResultBalanceAndPoint.Success)

        val result = viewModel.balanceAndPoint.value as ResultBalanceAndPoint.Success<WalletappGetAccountBalance>
        assert(result.data == successGetSaldoBalanceAndPointResponse.data)
    }

    @Test
    fun `Failed get saldo balance and point`() {
        viewModel.balanceAndPoint.observeForever(balanceAndPointOvserver)
        coEvery { saldoBalanceUseCase(Unit) } coAnswers { throw throwableResponse }

        viewModel.getBalanceAndPoint(AccountConstants.WALLET.SALDO)

        verify { balanceAndPointOvserver.onChanged(any()) }
        assert(viewModel.balanceAndPoint.value is ResultBalanceAndPoint.Fail)

        val result = viewModel.balanceAndPoint.value as ResultBalanceAndPoint.Fail
        assertEquals(throwableResponse, result.throwable)
    }

    @Test
    fun `Success get co brand cc balance and point`() {
        viewModel.balanceAndPoint.observeForever(balanceAndPointOvserver)
        coEvery { coBrandCCBalanceAndPointUseCase(Unit) } returns successGetCoBrandCCBalanceAndPointResponse

        viewModel.getBalanceAndPoint(AccountConstants.WALLET.CO_BRAND_CC)

        verify { balanceAndPointOvserver.onChanged(any<ResultBalanceAndPoint.Success<WalletappGetAccountBalance>>()) }
        assert(viewModel.balanceAndPoint.value is ResultBalanceAndPoint.Success)

        val result = viewModel.balanceAndPoint.value as ResultBalanceAndPoint.Success<WalletappGetAccountBalance>
        assert(result.data == successGetCoBrandCCBalanceAndPointResponse.data)
    }

    @Test
    fun `Failed get co brand cc balance and point`() {
        viewModel.balanceAndPoint.observeForever(balanceAndPointOvserver)
        coEvery { coBrandCCBalanceAndPointUseCase(Unit) } coAnswers { throw throwableResponse }

        viewModel.getBalanceAndPoint(AccountConstants.WALLET.CO_BRAND_CC)

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
    fun `Get fingerprint status success`() {
        val data = CheckFingerprintResult(isSuccess = true, isRegistered = false)
        val mockResponse = CheckFingerprintPojo(data)

        /* When */
        coEvery {
            checkFingerprintToggleUseCase.invoke(any())
        } returns mockResponse

        viewModel.getFingerprintStatus()

        verify {
            checkFingerprintResult.onChanged(Success(data))
        }
    }

    @Test
    fun `Get fingerprint status fail - has errors `() {
        val errorMsg = "error"
        val data = CheckFingerprintResult(isSuccess = false, isRegistered = false, errorMessage = errorMsg)
        val mockResponse = CheckFingerprintPojo(data)

        /* When */
        coEvery {
            checkFingerprintToggleUseCase.invoke(any())
        } returns mockResponse

        viewModel.getFingerprintStatus()

        assert((viewModel.checkFingerprintStatus.value as Fail).throwable.message == "Gagal")
    }

    @Test
    fun `Get fingerprint status fail - throw exception `() {
        /* When */
        coEvery {
            checkFingerprintToggleUseCase.invoke(any())
        } throws throwableMock

        viewModel.getFingerprintStatus()

        verify {
            checkFingerprintResult.onChanged(Fail(throwableMock))
        }
    }

    companion object {
        private val successGetCentralizedUserAssetConfigResponse: CentralizedUserAssetDataModel = FileUtil.parse(
            "/success_get_centralized_user_asset_config.json",
            CentralizedUserAssetDataModel::class.java
        )
        private val successGetBalanceAndPointResponse: BalanceAndPointDataModel = FileUtil.parse(
            "/success_get_balance_and_point.json",
            BalanceAndPointDataModel::class.java
        )
        private val successGetSaldoBalanceAndPointResponse: SaldoBalanceDataModel = FileUtil.parse(
            "/success_get_saldo_balance_and_point.json",
            SaldoBalanceDataModel::class.java
        )
        private val successGetTokopointBalanceAndPointResponse: TokopointsBalanceDataModel = FileUtil.parse(
            "/success_get_tokopoint_balance_and_point.json",
            TokopointsBalanceDataModel::class.java
        )
        private val successGetCoBrandCCBalanceAndPointResponse: CoBrandCCBalanceDataModel = FileUtil.parse(
            "/success_get_cobrandcc_balance_and_point.json",
            CoBrandCCBalanceDataModel::class.java
        )
        private val throwableResponse = Throwable()
    }
}