package com.tokopedia.home_account.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.FileUtil
import com.tokopedia.home_account.ResultBalanceAndPoint
import com.tokopedia.home_account.account_settings.data.model.UserProfileSetting
import com.tokopedia.home_account.account_settings.data.model.UserProfileSettingResponse
import com.tokopedia.home_account.data.model.*
import com.tokopedia.home_account.domain.usecase.*
import com.tokopedia.home_account.pref.AccountPreference
import com.tokopedia.home_account.privacy_account.data.LinkStatusResponse
import com.tokopedia.home_account.privacy_account.domain.GetLinkStatusUseCase
import com.tokopedia.home_account.privacy_account.domain.GetUserProfile
import com.tokopedia.loginfingerprint.data.model.CheckFingerprintPojo
import com.tokopedia.loginfingerprint.data.model.CheckFingerprintResult
import com.tokopedia.loginfingerprint.domain.usecase.CheckFingerprintToggleStatusUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.sessioncommon.data.fingerprint.FingerprintPreference
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.usercomponents.tokopediaplus.domain.TokopediaPlusDataModel
import com.tokopedia.usercomponents.tokopediaplus.domain.TokopediaPlusResponseDataModel
import com.tokopedia.usercomponents.tokopediaplus.domain.TokopediaPlusUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
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

    private val homeAccountUserUsecase = mockk<HomeAccountUserUsecase>(relaxed = true)
    private val homeAccountShortcutUseCase = mockk<HomeAccountShortcutUseCase>(relaxed = true)
    private val updateSafeModeUseCase = mockk<UpdateSafeModeUseCase>(relaxed = true)
    private val homeAccountRecommendationUseCase = mockk<GetRecommendationUseCase>(relaxed = true)
    private val centralizedUserAssetConfigUseCase = mockk<GetCentralizedUserAssetConfigUseCase>(relaxed = true)
    private val balanceAndPointUseCase = mockk<GetBalanceAndPointUseCase>(relaxed = true)
    private val tokopointsBalanceAndPointUseCase = mockk<GetTokopointsBalanceAndPointUseCase>(relaxed = true)
    private val saldoBalanceUseCase = mockk<GetSaldoBalanceUseCase>(relaxed = true)
    private val coBrandCCBalanceAndPointUseCase = mockk<GetCoBrandCCBalanceAndPointUseCase>(relaxed = true)
    private val getLinkStatusUseCase = mockk<GetLinkStatusUseCase>(relaxed = true)
    private val getPhoneUseCase = mockk<GetUserProfile>(relaxed = true)
    private val topAdsImageViewUseCase = mockk<TopAdsImageViewUseCase>(relaxed = true)
    private val getSafeModeUseCase = mockk<GetSafeModeUseCase>(relaxed = true)
    private val checkFingerprintToggleUseCase = mockk<CheckFingerprintToggleStatusUseCase>(relaxed = true)
    private val saveAttributeOnLocal = mockk<SaveAttributeOnLocalUseCase>(relaxed = true)
    private val tokopediaPlusUseCase = mockk<TokopediaPlusUseCase>(relaxed = true)
    private val offerInterruptUseCase = mockk<OfferInterruptUseCase>(relaxed = true)

    private val shortCutResponse = mockk<Observer<Result<ShortcutResponse>>>(relaxed = true)
    private val centralizedUserAssetConfigObserver = mockk<Observer<Result<CentralizedUserAssetConfig>>>(relaxed = true)
    private val balanceAndPointObserver = mockk<Observer<ResultBalanceAndPoint<WalletappGetAccountBalance>>>(relaxed = true)
    private val tokopediaPlusObserver = mockk<Observer<Result<TokopediaPlusDataModel>>>(relaxed = true)

    private val safeStatusResponse = mockk<Observer<Boolean>>(relaxed = true)

    private val userSession = mockk<UserSessionInterface>(relaxed = true)
    private val accountPref = mockk<AccountPreference>(relaxed = true)
    private val fingerprintPreferenceManager = mockk<FingerprintPreference>(relaxed = true)

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var viewModel: HomeAccountUserViewModel

    private val throwable = Fail(Throwable(message = "Error"))
    private var checkFingerprintResult = mockk<Observer<Result<CheckFingerprintResult>>>(relaxed = true)

    private val shortcut = ShortcutResponse()
    private val responseResult = UserAccountDataModel()
    private val linkStatusResult = LinkStatusResponse()
    private val offerInterruptResponse = OfferInterruptResponse()
    private val profilePojo = ProfilePojo(profileInfo = ProfileInfo(phone = "089123456789"))
    private val throwableMock = mockk<Throwable>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = HomeAccountUserViewModel(
            userSession,
            accountPref,
            fingerprintPreferenceManager,
            homeAccountUserUsecase,
            homeAccountShortcutUseCase,
            updateSafeModeUseCase,
            homeAccountRecommendationUseCase,
            topAdsImageViewUseCase,
            centralizedUserAssetConfigUseCase,
            balanceAndPointUseCase,
            tokopointsBalanceAndPointUseCase,
            saldoBalanceUseCase,
            coBrandCCBalanceAndPointUseCase,
            getLinkStatusUseCase,
            getPhoneUseCase,
            getSafeModeUseCase,
            checkFingerprintToggleUseCase,
            tokopediaPlusUseCase,
            saveAttributeOnLocal,
            offerInterruptUseCase,
            dispatcher
        )

        viewModel.shortcutData.observeForever(shortCutResponse)
        viewModel.safeModeStatus.observeForever(safeStatusResponse)
        viewModel.checkFingerprintStatus.observeForever(checkFingerprintResult)
        viewModel.tokopediaPlusData.observeForever(tokopediaPlusObserver)
    }

    @Test
    fun `Execute refreshPhoneNo Success`() {
        coEvery { getPhoneUseCase(Unit) } returns profilePojo

        viewModel.refreshPhoneNo()

        verify {
            userSession.phoneNumber = profilePojo.profileInfo.phone
        }
        assertEquals(viewModel.phoneNo.value, profilePojo.profileInfo.phone)
    }

    @Test
    fun `Execute refreshPhoneNo Success but phone empty`() {
        coEvery { getPhoneUseCase(Unit) } returns ProfilePojo()

        viewModel.refreshPhoneNo()
        assertEquals(viewModel.phoneNo.value, null)
    }

    @Test
    fun `Execute refreshPhoneNo Failed`() {
        coEvery { getPhoneUseCase(Unit) } throws throwable.throwable

        viewModel.refreshPhoneNo()

        assertEquals(viewModel.phoneNo.value, "")
    }

    @Test
    fun `Execute getBuyerData Success`() {
        /* When */
        coEvery { homeAccountUserUsecase(Unit) } returns responseResult
        coEvery { homeAccountShortcutUseCase(Unit) } returns shortcut
        coEvery { getLinkStatusUseCase.invoke(any()) } returns linkStatusResult
        coEvery { offerInterruptUseCase.invoke(any()) } returns offerInterruptResponse

        viewModel.getBuyerData()

        responseResult.linkStatus = linkStatusResult.response

        assertEquals(viewModel.buyerAccountDataData.value, Success(responseResult))
    }

    @Test
    fun `Execute getBuyerData Failed`() {
        /* When */
        val exception = Exception("error")
        coEvery { homeAccountUserUsecase.invoke(Unit) } throws exception
        coEvery { getLinkStatusUseCase.invoke(any()) } throws exception
        coEvery { offerInterruptUseCase.invoke(any()) } returns offerInterruptResponse

        viewModel.getBuyerData()
        val actual = viewModel.buyerAccountDataData.getOrAwaitValue()
        assertTrue(actual is Fail)
        assertEquals((actual as Fail).throwable.message, exception.message)
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
        assertEquals((viewModel.firstRecommendationData.value as Success).data, expectedResult)
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
        assertEquals((viewModel.getRecommendationData.value as Success).data, expectedResult.recommendationItemList)
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

        println(expectedResult.recommendationItemList)
        coEvery {
            homeAccountRecommendationUseCase.getData(any())
        } returns listOf(expectedResult)

        coEvery { topAdsImageViewUseCase.getImageData(any()) } throws throwableMock

        viewModel.getRecommendation(testPage)

        assertEquals((viewModel.firstRecommendationData.value as Success).data.tdnBanner, null)
    }

    @Test
    fun `Successfully get recommendation with tdn data - less than tdn_index`() {
        val recomList = listOf(
            RecommendationItem(1),
            RecommendationItem(2),
            RecommendationItem(3)
        )
        val testPage = 1
        val expectedResult = RecommendationWidget(recommendationItemList = recomList)

        println(expectedResult.recommendationItemList)
        coEvery {
            homeAccountRecommendationUseCase.getData(any())
        } returns listOf(expectedResult)

        coEvery { topAdsImageViewUseCase.getImageData(any()) } throws throwableMock

        viewModel.getRecommendation(testPage)

        assertEquals((viewModel.firstRecommendationData.value as Success).data.tdnBanner, null)
    }

    @Test
    fun `Successfully get recommendation with tdn data - less than check first false`() {
        val recomList = listOf(
            RecommendationItem(1),
            RecommendationItem(2),
            RecommendationItem(3)
        )
        val testPage = 2
        val expectedResult = RecommendationWidget(recommendationItemList = recomList)

        println(expectedResult.recommendationItemList)
        coEvery {
            homeAccountRecommendationUseCase.getData(any())
        } returns listOf(expectedResult)

        coEvery { topAdsImageViewUseCase.getImageData(any()) } throws throwableMock

        viewModel.getRecommendation(testPage)

        assertEquals((viewModel.getRecommendationData.value as Success).data, recomList)
    }

    @Test
    fun `Successfully get recommendation with tdn data - check first false`() {
        val recomList = listOf(
            RecommendationItem(1),
            RecommendationItem(2),
            RecommendationItem(3),
            RecommendationItem(4)
        )
        val testPage = 2
        val expectedResult = RecommendationWidget(recommendationItemList = recomList)

        println(expectedResult.recommendationItemList)
        coEvery {
            homeAccountRecommendationUseCase.getData(any())
        } returns listOf(expectedResult)

        coEvery { topAdsImageViewUseCase.getImageData(any()) } throws throwableMock

        viewModel.getRecommendation(testPage)

        assertEquals((viewModel.getRecommendationData.value as Success).data, recomList)
    }


    @Test
    fun `Failed to get first recommendation`() {
        val expectedResult = mockk<Throwable>(relaxed = true)
        coEvery {
            homeAccountRecommendationUseCase.getData(any())
        } throws expectedResult

        viewModel.getFirstRecommendation()

        print(viewModel.firstRecommendationData.value)
        assertEquals((viewModel.firstRecommendationData.value as Fail).throwable, expectedResult)
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
        assertEquals((viewModel.getRecommendationData.value as Fail).throwable, expectedResult)
    }

    @Test
    fun `Get safe mode success`() {
        val isActive = true
        val data = UserProfileSetting(safeMode = isActive)
        val getSafeModeResponse = UserProfileSettingResponse(data)

        /* When */
        coEvery {
            getSafeModeUseCase(Unit)
        } returns getSafeModeResponse

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
        val getSafeModeResponse = UserProfileSettingResponse(data)

        /* When */
        coEvery {
            getSafeModeUseCase(Unit)
        } returns getSafeModeResponse

        viewModel.getSafeModeValue()

        assert(viewModel.safeModeStatus.getOrAwaitValue() == false)
        coVerify {
            accountPref.saveSettingValue(AccountConstants.KEY.KEY_PREF_SAFE_SEARCH, isActive)
        }
    }

    @Test
    fun `Get safe mode failed - throwable`() {
        /* When */
        coEvery {
            getSafeModeUseCase(Unit)
        } throws Exception()

        viewModel.getSafeModeValue()
    }

    @Test
    fun `Set safe mode success`() {
        val data = SetUserProfileSetting(isSuccess = true, error = "")
        val updateResponse = SetUserProfileSettingResponse(data)

        val isActive = true

        val getData = UserProfileSetting(safeMode = isActive)
        val getResponse = UserProfileSettingResponse(getData)

        val safeModeParam = SafeModeParam(isActive)

        /* When */
        coEvery {
            updateSafeModeUseCase(safeModeParam)
        } returns updateResponse

        coEvery {
            getSafeModeUseCase(Unit)
        } returns getResponse

        viewModel.setSafeMode(isActive)

        verify {
            accountPref.saveSettingValue(AccountConstants.KEY.KEY_PREF_SAFE_SEARCH, isActive)
            safeStatusResponse.onChanged(isActive)
        }
    }

    @Test
    fun `Set safe mode Failed`() {
        val isActive = true
        val safeModeParam = SafeModeParam(isActive)

        /* When */
        coEvery {
            updateSafeModeUseCase(safeModeParam)
        } throws Exception()

        viewModel.setSafeMode(isActive)
    }

    @Test
    fun `Set safe mode inactive success`() {
        val data = SetUserProfileSetting(isSuccess = true, error = "")
        val updateResponse = SetUserProfileSettingResponse(data)

        val isActive = false

        val getData = UserProfileSetting(safeMode = isActive)
        val getResponse = UserProfileSettingResponse(getData)

        val safeModeParam = SafeModeParam(isActive)

        /* When */
        coEvery {
            updateSafeModeUseCase(safeModeParam)
        } returns updateResponse

        coEvery {
            getSafeModeUseCase(Unit)
        } returns getResponse


        viewModel.setSafeMode(isActive)

        verify {
            accountPref.saveSettingValue(AccountConstants.KEY.KEY_PREF_SAFE_SEARCH, isActive)
            safeStatusResponse.onChanged(isActive)
        }
    }

//    @Test
//    fun `Set safe mode inactive Failed`() {
//        val isActive = false
//        /* When */
//        every {
//            homeAccountSafeSettingProfileUseCase.executeQuerySetSafeMode(
//                any(),
//                any(),
//                any()
//            )
//        } answers {
//            secondArg<(Throwable) -> Unit>().invoke(throwableMock)
//        }
//
//        viewModel.setSafeMode(isActive)
//
//        justRun { throwableMock.printStackTrace() }
//        verify(atLeast = 1) {
//            throwableMock.printStackTrace()
//        }
//    }

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
        val exception = mockk<Exception>(relaxed = true)
        /* When */
        coEvery { homeAccountShortcutUseCase(Unit) } throws exception

        viewModel.getShortcutData()

        verify {
            shortCutResponse.onChanged(Fail(exception))
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
    fun `Success get gopay balance and point and hide title false`() {
        viewModel.balanceAndPoint.observeForever(balanceAndPointObserver)
        coEvery { balanceAndPointUseCase(any()) } returns successGetBalanceAndPointResponse

        viewModel.getBalanceAndPoint(AccountConstants.WALLET.GOPAY, false)

        verify { balanceAndPointObserver.onChanged(any<ResultBalanceAndPoint.Success<WalletappGetAccountBalance>>()) }
        assert(viewModel.balanceAndPoint.value is ResultBalanceAndPoint.Success)

        val result = viewModel.balanceAndPoint.value as ResultBalanceAndPoint.Success<WalletappGetAccountBalance>
        assert(result.data == successGetBalanceAndPointResponse.data)
    }

    @Test
    fun `Failed get gopay balance and point and hide title false`() {
        viewModel.balanceAndPoint.observeForever(balanceAndPointObserver)
        coEvery { balanceAndPointUseCase(any()) } coAnswers { throw throwableResponse }

        viewModel.getBalanceAndPoint(AccountConstants.WALLET.GOPAY, false)

        verify { balanceAndPointObserver.onChanged(any()) }
        assert(viewModel.balanceAndPoint.value is ResultBalanceAndPoint.Fail)

        val result = viewModel.balanceAndPoint.value as ResultBalanceAndPoint.Fail
        assertEquals(throwableResponse, result.throwable)
    }

    @Test
    fun `Success get gopaylater balance and point and hide title false`() {
        viewModel.balanceAndPoint.observeForever(balanceAndPointObserver)
        coEvery { balanceAndPointUseCase(any()) } returns successGetBalanceAndPointResponse

        viewModel.getBalanceAndPoint(AccountConstants.WALLET.GOPAYLATER, false)

        verify { balanceAndPointObserver.onChanged(any<ResultBalanceAndPoint.Success<WalletappGetAccountBalance>>()) }
        assert(viewModel.balanceAndPoint.value is ResultBalanceAndPoint.Success)

        val result = viewModel.balanceAndPoint.value as ResultBalanceAndPoint.Success<WalletappGetAccountBalance>
        assert(result.data == successGetBalanceAndPointResponse.data)
    }

    @Test
    fun `Failed get gopaylater balance and point and hide title false`() {
        viewModel.balanceAndPoint.observeForever(balanceAndPointObserver)
        coEvery { balanceAndPointUseCase(any()) } coAnswers { throw throwableResponse }

        viewModel.getBalanceAndPoint(AccountConstants.WALLET.GOPAYLATER, false)

        verify { balanceAndPointObserver.onChanged(any()) }
        assert(viewModel.balanceAndPoint.value is ResultBalanceAndPoint.Fail)

        val result = viewModel.balanceAndPoint.value as ResultBalanceAndPoint.Fail
        assertEquals(throwableResponse, result.throwable)
    }

    @Test
    fun `Success get ovo balance and point and hide title false`() {
        viewModel.balanceAndPoint.observeForever(balanceAndPointObserver)
        coEvery { balanceAndPointUseCase(any()) } returns successGetBalanceAndPointResponse

        viewModel.getBalanceAndPoint(AccountConstants.WALLET.OVO, false)

        verify { balanceAndPointObserver.onChanged(any<ResultBalanceAndPoint.Success<WalletappGetAccountBalance>>()) }
        assert(viewModel.balanceAndPoint.value is ResultBalanceAndPoint.Success)

        val result = viewModel.balanceAndPoint.value as ResultBalanceAndPoint.Success<WalletappGetAccountBalance>
        assert(result.data == successGetBalanceAndPointResponse.data)
    }

    @Test
    fun `Failed get ovo balance and point and hide title false`() {
        viewModel.balanceAndPoint.observeForever(balanceAndPointObserver)
        coEvery { balanceAndPointUseCase(any()) } coAnswers { throw throwableResponse }

        viewModel.getBalanceAndPoint(AccountConstants.WALLET.OVO, false)

        verify { balanceAndPointObserver.onChanged(any()) }
        assert(viewModel.balanceAndPoint.value is ResultBalanceAndPoint.Fail)

        val result = viewModel.balanceAndPoint.value as ResultBalanceAndPoint.Fail
        assertEquals(throwableResponse, result.throwable)
    }

    @Test
    fun `Success get tokopoint balance and point and hide title false`() {
        viewModel.balanceAndPoint.observeForever(balanceAndPointObserver)
        coEvery { tokopointsBalanceAndPointUseCase(Unit) } returns successGetTokopointBalanceAndPointResponse

        viewModel.getBalanceAndPoint(AccountConstants.WALLET.TOKOPOINT, false)

        verify { balanceAndPointObserver.onChanged(any<ResultBalanceAndPoint.Success<WalletappGetAccountBalance>>()) }
        assert(viewModel.balanceAndPoint.value is ResultBalanceAndPoint.Success)

        val result = viewModel.balanceAndPoint.value as ResultBalanceAndPoint.Success<WalletappGetAccountBalance>
        assert(result.data == successGetTokopointBalanceAndPointResponse.data)
    }

    @Test
    fun `Failed get tokopoint balance and point and hide title false`() {
        viewModel.balanceAndPoint.observeForever(balanceAndPointObserver)
        coEvery { tokopointsBalanceAndPointUseCase(Unit) } coAnswers { throw throwableResponse }

        viewModel.getBalanceAndPoint(AccountConstants.WALLET.TOKOPOINT, false)

        verify { balanceAndPointObserver.onChanged(any()) }
        assert(viewModel.balanceAndPoint.value is ResultBalanceAndPoint.Fail)

        val result = viewModel.balanceAndPoint.value as ResultBalanceAndPoint.Fail
        assertEquals(throwableResponse, result.throwable)
    }

    @Test
    fun `Success get saldo balance and point and hide title false`() {
        viewModel.balanceAndPoint.observeForever(balanceAndPointObserver)
        coEvery { saldoBalanceUseCase(Unit) } returns successGetSaldoBalanceAndPointResponse

        viewModel.getBalanceAndPoint(AccountConstants.WALLET.SALDO, false)

        verify { balanceAndPointObserver.onChanged(any<ResultBalanceAndPoint.Success<WalletappGetAccountBalance>>()) }
        assert(viewModel.balanceAndPoint.value is ResultBalanceAndPoint.Success)

        val result = viewModel.balanceAndPoint.value as ResultBalanceAndPoint.Success<WalletappGetAccountBalance>
        assert(result.data == successGetSaldoBalanceAndPointResponse.data)
    }

    @Test
    fun `Failed get saldo balance and point and hide title false`() {
        viewModel.balanceAndPoint.observeForever(balanceAndPointObserver)
        coEvery { saldoBalanceUseCase(Unit) } coAnswers { throw throwableResponse }

        viewModel.getBalanceAndPoint(AccountConstants.WALLET.SALDO, false)

        verify { balanceAndPointObserver.onChanged(any()) }
        assert(viewModel.balanceAndPoint.value is ResultBalanceAndPoint.Fail)

        val result = viewModel.balanceAndPoint.value as ResultBalanceAndPoint.Fail
        assertEquals(throwableResponse, result.throwable)
    }

    @Test
    fun `Success get co brand cc balance and point and hide title false`() {
        viewModel.balanceAndPoint.observeForever(balanceAndPointObserver)
        coEvery { coBrandCCBalanceAndPointUseCase(Unit) } returns successGetCoBrandCCBalanceAndPointResponse

        viewModel.getBalanceAndPoint(AccountConstants.WALLET.CO_BRAND_CC, false)

        verify { balanceAndPointObserver.onChanged(any<ResultBalanceAndPoint.Success<WalletappGetAccountBalance>>()) }
        assert(viewModel.balanceAndPoint.value is ResultBalanceAndPoint.Success)

        val result = viewModel.balanceAndPoint.value as ResultBalanceAndPoint.Success<WalletappGetAccountBalance>
        assert(result.data == successGetCoBrandCCBalanceAndPointResponse.data)
    }

    @Test
    fun `Failed get co brand cc balance and point and hide title false`() {
        viewModel.balanceAndPoint.observeForever(balanceAndPointObserver)
        coEvery { coBrandCCBalanceAndPointUseCase(Unit) } coAnswers { throw throwableResponse }

        viewModel.getBalanceAndPoint(AccountConstants.WALLET.CO_BRAND_CC, false)

        verify { balanceAndPointObserver.onChanged(any()) }
        assert(viewModel.balanceAndPoint.value is ResultBalanceAndPoint.Fail)

        val result = viewModel.balanceAndPoint.value as ResultBalanceAndPoint.Fail
        assertEquals(throwableResponse, result.throwable)
    }

    @Test
    fun `Failed get balance and point and hide title false`() {
        viewModel.balanceAndPoint.observeForever(balanceAndPointObserver)

        viewModel.getBalanceAndPoint("", true)

        verify { balanceAndPointObserver.onChanged(any()) }
        assert(viewModel.balanceAndPoint.value is ResultBalanceAndPoint.Fail)

        val result = viewModel.balanceAndPoint.value as ResultBalanceAndPoint.Fail
        assert(result.throwable is IllegalArgumentException)
    }

    @Test
    fun `Get fingerprint status success`() {
        val data = CheckFingerprintResult(isSuccess = true, isRegistered = false, errorMessage = "")
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
    fun `Get fingerprint status fail - success trie, has errors `() {
        val errorMsg = "error"
        val data = CheckFingerprintResult(isSuccess = true, isRegistered = false, errorMessage = errorMsg)
        val mockResponse = CheckFingerprintPojo(data)

        /* When */
        coEvery {
            checkFingerprintToggleUseCase.invoke(any())
        } returns mockResponse

        viewModel.getFingerprintStatus()

        assert((viewModel.checkFingerprintStatus.value as Fail).throwable.message == "Gagal")
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

    @Test
    fun `Tokopedia Plus - Success Get Widget Data`() {
        val mockResponse = TokopediaPlusResponseDataModel(
            TokopediaPlusDataModel(
                isShown = true
            )
        )

        coEvery {
            tokopediaPlusUseCase(any())
        } returns mockResponse

        viewModel.getTokopediaWidgetContent()

        coVerify {
            tokopediaPlusObserver.onChanged(Success(mockResponse.tokopediaPlus))
        }
    }

    @Test
    fun `Tokopedia Plus - Failed Get Widget Data`() {
        coEvery {
            tokopediaPlusUseCase(any())
        } throws throwableResponse

        viewModel.getTokopediaWidgetContent()

        coVerify {
            tokopediaPlusObserver.onChanged(Fail(throwableResponse))
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
