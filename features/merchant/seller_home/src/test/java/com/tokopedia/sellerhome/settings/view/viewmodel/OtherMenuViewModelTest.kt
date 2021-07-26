package com.tokopedia.sellerhome.settings.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.gm.common.constant.END_PERIOD
import com.tokopedia.gm.common.domain.interactor.GetShopInfoPeriodUseCase
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.seller.menu.common.domain.entity.OthersBalance
import com.tokopedia.seller.menu.common.domain.usecase.*
import com.tokopedia.seller.menu.common.view.uimodel.UserShopInfoWrapper
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingResponseState
import com.tokopedia.seller.menu.common.view.uimodel.base.ShopType
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.*
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.domain.usecase.GetShopOperationalUseCase
import com.tokopedia.sellerhome.settings.view.uimodel.menusetting.ShopOperationalUiModel
import com.tokopedia.sellerhome.utils.observeAwaitValue
import com.tokopedia.sellerhome.utils.observeOnce
import com.tokopedia.sellerhome.utils.verifyStateErrorEquals
import com.tokopedia.sellerhome.utils.verifyStateSuccessEquals
import com.tokopedia.shop.common.data.source.cloud.model.FreeOngkir
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfoFreeShipping
import com.tokopedia.shop.common.domain.interactor.GetShopFreeShippingInfoUseCase
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.*

@ExperimentalCoroutinesApi
class OtherMenuViewModelTest {

    @RelaxedMockK
    lateinit var getShopFreeShippingInfoUseCase: GetShopFreeShippingInfoUseCase

    @RelaxedMockK
    lateinit var getShopInfoPeriodUseCase: GetShopInfoPeriodUseCase

    @RelaxedMockK
    lateinit var getShopOperationalUseCase: GetShopOperationalUseCase

    @RelaxedMockK
    lateinit var balanceInfoUseCase: BalanceInfoUseCase

    @RelaxedMockK
    lateinit var getShopBadgeUseCase: GetShopBadgeUseCase

    @RelaxedMockK
    lateinit var getShopTotalFollowersUseCase: GetShopTotalFollowersUseCase

    @RelaxedMockK
    lateinit var getUserShopInfoUseCase: GetUserShopInfoUseCase

    @RelaxedMockK
    lateinit var topAdsAutoTopupUseCase: TopAdsAutoTopupUseCase

    @RelaxedMockK
    lateinit var topAdsDashboardDepositUseCase: TopAdsDashboardDepositUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var remoteConfig: FirebaseRemoteConfigImpl

    @RelaxedMockK
    lateinit var shopBadgeFollowersShimmerObserver: Observer<in Boolean>

    @RelaxedMockK
    lateinit var shouldShowAllErrorObserver: Observer<in Boolean>

    @RelaxedMockK
    lateinit var shouldShowMultipleErrorObserver: Observer<in Boolean>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var mViewModel: OtherMenuViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        mViewModel =
                OtherMenuViewModel(
                        coroutineTestRule.dispatchers,
                        getShopFreeShippingInfoUseCase,
                        getShopOperationalUseCase,
                        getShopInfoPeriodUseCase,
                        balanceInfoUseCase,
                        getShopBadgeUseCase,
                        getShopTotalFollowersUseCase,
                        getUserShopInfoUseCase,
                        topAdsAutoTopupUseCase,
                        topAdsDashboardDepositUseCase,
                        userSession,
                        remoteConfig
                )
        mViewModel.shopBadgeFollowersShimmerLiveData.observeForever(shopBadgeFollowersShimmerObserver)
        mViewModel.shouldShowAllError.observeForever(shouldShowAllErrorObserver)
        mViewModel.shouldShowMultipleErrorToaster.observeForever(shouldShowMultipleErrorObserver)
    }

    @After
    fun cleanup() {
        mViewModel.shopBadgeFollowersShimmerLiveData.removeObserver(shopBadgeFollowersShimmerObserver)
        mViewModel.shouldShowAllError.removeObserver(shouldShowAllErrorObserver)
        mViewModel.shouldShowMultipleErrorToaster.removeObserver(shouldShowMultipleErrorObserver)
    }

    @Test
    fun `when onCheckDelayErrorResponseTrigger should alter toaster flag between true and false`() = coroutineTestRule.runBlockingTest {
        mViewModel.onCheckDelayErrorResponseTrigger()

        mViewModel.isToasterAlreadyShown.observeOnce {
            assertTrue(it)
        }

        advanceTimeBy(5000L)

        mViewModel.isToasterAlreadyShown.observeOnce {
            assertFalse(it)
        }
    }

    @Test
    fun `Setting status bar initial state should change the live data value`() {

        mViewModel.setIsStatusBarInitialState(isInitialState = true)

        val isStatusBarInitialState = mViewModel.isStatusBarInitialState.value

        assertNotNull(isStatusBarInitialState)
        isStatusBarInitialState?.let {
            assert(it)
        }

    }

    @Test
    fun `getFreeShippingStatus should return when free shipping feature disabled from remote config`() {
        every {
            remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_FEATURE_DISABLED, true)
        } returns true

        mViewModel.getFreeShippingStatus()

        coVerify(inverse = true) {
            getShopFreeShippingInfoUseCase.execute(any())
        }

        assert(mViewModel.isFreeShippingActive.observeAwaitValue() == null)
    }

    @Test
    fun `getFreeShippingStatus should return when free shipping in transition status is true from remote config`() {
        every {
            remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_FEATURE_DISABLED, true)
        } returns false

        every {
            remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_TRANSITION_PERIOD, true)
        } returns true

        mViewModel.getFreeShippingStatus()

        coVerify(inverse = true) {
            getShopFreeShippingInfoUseCase.execute(any())
        }

        assert(mViewModel.isFreeShippingActive.observeAwaitValue() == null)
    }

    @Test
    fun `when getAllOtherMenuData called should get all other page data`() = runBlocking {
        mViewModel.getAllOtherMenuData()

        verifyGetShopBadgeCalled()
        verifyGetShopTotalFollowersCalled()
        verifyGetUserShopInfoCalled()
        verifyGetFreeShippingStatusCalled()
        verifyGetShopOperationalCalled()
        verifyGetBalanceCalled()
        verifyGetTopAdsKreditCalled()
        verifyGetTopAdsAutoTopupCalled()
    }

    @Test
    fun `when getAllOtherMenuData and all data fails, should set show all error live data true`() = runBlocking {
        onGetShopBadge_thenThrow()
        onGetShopTotalFollowers_thenThrow()
        onGetUserShopInfo_thenThrow()
        onGetShopOperational_thenThrow()
        onGetBalance_thenThrow()
        onGetTopAdsKredit_thenThrow()
        onGetTopAdsAutoTopup_thenThrow()

        mViewModel.getAllOtherMenuData()

        assert(mViewModel.shouldShowAllError.value == true)
    }

    @Test
    fun `when getAllOtherMenuData and two or more (but not all) data fails, should set show multiple error toaster live data true`() = runBlocking {
        onGetShopTotalFollowers_thenThrow()
        onGetUserShopInfo_thenThrow()
        onGetShopOperational_thenThrow()
        onGetBalance_thenThrow()
        onGetTopAdsKredit_thenThrow()
        onGetTopAdsAutoTopup_thenThrow()
        onGetShopBadge_thenReturn("")

        mViewModel.getAllOtherMenuData()

        assert(mViewModel.shouldShowMultipleErrorToaster.value == true)
    }

    @Test
    fun `when reloadErrorData should reload data that was failed`() = runBlocking {
        onGetShopBadge_thenThrow()
        onGetShopTotalFollowers_thenThrow()
        onGetUserShopInfo_thenThrow()
        onGetShopOperational_thenThrow()
        onGetBalance_thenThrow()
        onGetTopAdsKredit_thenThrow()
        mViewModel.getAllOtherMenuData()

        mViewModel.reloadErrorData()
        mViewModel.onReloadErrorData()

        verifyGetShopBadgeCalled(atLeast = 2)
        verifyGetShopTotalFollowersCalled(atLeast = 2)
        verifyGetShopOperationalCalled(atLeast = 2)
        verifyGetUserShopInfoCalled(atLeast = 2)
        verifyGetBalanceCalled(atLeast = 2)
        verifyGetTopAdsKreditCalled(atLeast = 2)
    }

    @Test
    fun `when reloadErrorData should not reload data that was success`() = runBlocking {
        onGetShopBadge_thenThrow()
        onGetShopTotalFollowers_thenThrow()
        onGetUserShopInfo_thenThrow()
        onGetShopOperational_thenThrow()
        onGetBalance_thenThrow()
        onGetTopAdsKredit_thenReturn(0F)
        mViewModel.getAllOtherMenuData()

        mViewModel.reloadErrorData()

        verifyGetShopBadgeCalled(atLeast = 2)
        verifyGetShopTotalFollowersCalled(atLeast = 2)
        verifyGetShopOperationalCalled(atLeast = 2)
        verifyGetUserShopInfoCalled(atLeast = 2)
        verifyGetBalanceCalled(atLeast = 2)
        verifyGetTopAdsKreditCalled()
    }

    @Test
    fun `when reloadErrorData and error state map hasn't been set, should not reload any data`() = runBlocking {
        onGetShopBadge_thenThrow()
        onGetShopTotalFollowers_thenThrow()
        onGetUserShopInfo_thenThrow()
        onGetShopOperational_thenThrow()
        onGetBalance_thenThrow()
        onGetTopAdsKredit_thenThrow()

        mViewModel.reloadErrorData()

        verifyGetShopBadgeNotCalled()
        verifyGetShopTotalFollowersNotCalled()
        verifyGetShopOperationalNotCalled()
        verifyGetUserShopInfoNotCalled()
        verifyGetBalanceNotCalled()
        verifyGetTopAdsKreditNotCalled()
    }

    @Test
    fun `when getShopBadgeAndFollowers and all other menu data error, should reload all error data`() = runBlocking {
        onGetShopBadge_thenThrow()
        onGetShopTotalFollowers_thenThrow()
        onGetUserShopInfo_thenThrow()
        onGetShopOperational_thenThrow()
        onGetBalance_thenThrow()
        onGetTopAdsKredit_thenThrow()
        mViewModel.getAllOtherMenuData()

        mViewModel.getShopBadgeAndFollowers()

        verifyGetShopBadgeCalled(atLeast = 2)
        verifyGetShopTotalFollowersCalled(atLeast = 2)
        verifyGetShopOperationalCalled(atLeast = 2)
        verifyGetUserShopInfoCalled(atLeast = 2)
        verifyGetBalanceCalled(atLeast = 2)
        verifyGetTopAdsKreditCalled(atLeast = 2)
    }

    @Test
    fun `when getShopBadgeAndFollowers and not all other menu data error, should reload badge and followers data only`() = runBlocking {
        onGetShopBadge_thenThrow()
        onGetShopTotalFollowers_thenThrow()
        val userShopInfoWrapper = UserShopInfoWrapper(ShopType.OfficialStore)
        onGetUserShopInfo_thenReturn(userShopInfoWrapper)
        val shopOperationalUiModel = ShopOperationalUiModel(anyString(), anyInt(), anyInt(), anyInt(), anyInt(), anyBoolean())
        onGetShopOperational_thenReturn(shopOperationalUiModel)
        onGetBalance_thenReturn(OthersBalance())
        onGetTopAdsKredit_thenReturn(0f)
        mViewModel.getAllOtherMenuData()

        mViewModel.getShopBadgeAndFollowers()

        verifyGetShopBadgeCalled(atLeast = 2)
        verifyGetShopTotalFollowersCalled(atLeast = 2)
        verifyGetShopOperationalCalled(atLeast = 1)
        verifyGetUserShopInfoCalled(atLeast = 1)
        verifyGetBalanceCalled(atLeast = 1)
        verifyGetTopAdsKreditCalled(atLeast = 1)
    }

    @Test
    fun `when shop badge and followers data error, should set shopBadgeFollowersErrorLiveData to true`() = runBlocking {
        onGetShopBadge_thenThrow()
        onGetShopTotalFollowers_thenThrow()
        val userShopInfoWrapper = UserShopInfoWrapper(ShopType.OfficialStore)
        onGetUserShopInfo_thenReturn(userShopInfoWrapper)
        val shopOperationalUiModel = ShopOperationalUiModel(anyString(), anyInt(), anyInt(), anyInt(), anyInt(), anyBoolean())
        onGetShopOperational_thenReturn(shopOperationalUiModel)
        onGetBalance_thenReturn(OthersBalance())
        onGetTopAdsKredit_thenReturn(0f)

        mViewModel.getAllOtherMenuData()

        assert(mViewModel.shopBadgeFollowersErrorLiveData.value == true)
    }

    @Test
    fun `when setErrorStateMapDefaultValue but errorStateMap has been set, should not set the value again`() = runBlocking {
        onGetShopBadge_thenThrow()
        onGetShopTotalFollowers_thenThrow()
        onGetUserShopInfo_thenThrow()
        onGetShopOperational_thenThrow()
        onGetBalance_thenThrow()
        onGetTopAdsKredit_thenThrow()
        mViewModel.getAllOtherMenuData()

        mViewModel.setErrorStateMapDefaultValue()

        assert(mViewModel.shouldShowAllError.value == true)
    }

    @Test
    fun `getFreeShippingStatus should success`() {
        every {
            remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_FEATURE_DISABLED, true)
            remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_TRANSITION_PERIOD, true)
        } returns false

        coEvery {
            getShopFreeShippingInfoUseCase.execute(any())
        } returns listOf(ShopInfoFreeShipping.FreeShippingInfo(FreeOngkir(isActive = true)))

        mViewModel.getFreeShippingStatus()

        coVerify {
            getShopFreeShippingInfoUseCase.execute(any())
        }

        assert(mViewModel.isFreeShippingActive.observeAwaitValue() == true)
    }

    @Test
    fun `getShopPeriodType should success`() {
        val shopInfoPeriodUiModel = ShopInfoPeriodUiModel(periodType = END_PERIOD)
        coEvery {
            getShopInfoPeriodUseCase.executeOnBackground()
        } returns shopInfoPeriodUiModel

        mViewModel.getShopPeriodType()

        coVerify {
            getShopInfoPeriodUseCase.executeOnBackground()
        }

        val actualResult = (mViewModel.shopPeriodType.observeAwaitValue() as Success).data
        assert(mViewModel.shopPeriodType.observeAwaitValue() is Success)
        assert(actualResult == shopInfoPeriodUiModel)
    }

    @Test
    fun `when getShopOperational success should set live data success`() {
        val uiModel = ShopOperationalUiModel(
            "00:00 - 23:59",
            R.string.shop_operational_hour_24_hour,
            R.string.settings_operational_hour_open,
            R.drawable.ic_sah_clock,
            Label.GENERAL_LIGHT_GREEN,
            true
        )

        coEvery {
            getShopOperationalUseCase.executeOnBackground()
        } returns uiModel

        mViewModel.setErrorStateMapDefaultValue()
        mViewModel.getShopOperational()

        val expectedResult = SettingResponseState.SettingSuccess(uiModel)

        mViewModel.shopOperationalLiveData
            .verifyStateSuccessEquals(expectedResult)
    }

    @Test
    fun `when getShopOperational error should set live data fail`() {
        val error = IllegalStateException()

        coEvery {
            getShopOperationalUseCase.executeOnBackground()
        } throws error

        mViewModel.setErrorStateMapDefaultValue()
        mViewModel.getShopOperational()

        val expectedResult = SettingResponseState.SettingError(error)

        mViewModel.shopOperationalLiveData
            .verifyStateErrorEquals(expectedResult)
    }

    @Test
    fun `when getShopBadge success should set live data state success`() = runBlocking {
        val badgeUrl = "www.abc.com"
        onGetShopBadge_thenReturn(badgeUrl)

        mViewModel.setErrorStateMapDefaultValue()
        mViewModel.getShopBadge()

        verifyGetShopBadgeCalled()
        val expectedResult = SettingResponseState.SettingSuccess(ShopBadgeUiModel(badgeUrl))
        mViewModel.shopBadgeLiveData.verifyStateSuccessEquals(expectedResult)
    }

    @Test
    fun `when getShopBadge error should set live data state error`() = runBlocking {
        val error = IllegalStateException()
        onGetShopBadge_thenThrow(error)

        mViewModel.setErrorStateMapDefaultValue()
        mViewModel.getShopBadge()

        verifyGetShopBadgeCalled()
        val expectedResult = SettingResponseState.SettingError(error)
        mViewModel.shopBadgeLiveData.verifyStateErrorEquals(expectedResult)
    }

    @Test
    fun `when getShopFollowers success should set live data state success`() = runBlocking {
        val shopFollowers = 10L
        onGetShopTotalFollowers_thenReturn(shopFollowers)

        mViewModel.setErrorStateMapDefaultValue()
        mViewModel.getShopTotalFollowers()

        verifyGetShopTotalFollowersCalled()
        val expectedResult = SettingResponseState.SettingSuccess(ShopFollowersUiModel(shopFollowers))
        mViewModel.shopTotalFollowersLiveData.verifyStateSuccessEquals(expectedResult)
    }

    @Test
    fun `when getShopFollowers error should set live data state error`() = runBlocking {
        val error = IllegalStateException()
        onGetShopTotalFollowers_thenThrow(error)

        mViewModel.setErrorStateMapDefaultValue()
        mViewModel.getShopTotalFollowers()

        verifyGetShopTotalFollowersCalled()
        val expectedResult = SettingResponseState.SettingError(error)
        mViewModel.shopTotalFollowersLiveData.verifyStateErrorEquals(expectedResult)
    }

    @Test
    fun `when getUserShopInfo success should set live data state success`() = runBlocking {
        val userShopInfoWrapper = UserShopInfoWrapper(ShopType.OfficialStore)
        onGetUserShopInfo_thenReturn(userShopInfoWrapper)

        mViewModel.setErrorStateMapDefaultValue()
        mViewModel.getUserShopInfo()

        verifyGetUserShopInfoCalled()
        assert((mViewModel.userShopInfoLiveData.value as? SettingResponseState.SettingSuccess)?.data?.userShopInfoWrapper == userShopInfoWrapper)
    }

    @Test
    fun `when getUserShopInfo error should set live data state error`() = runBlocking {
        val error = IllegalStateException()
        onGetUserShopInfo_thenThrow(error)

        mViewModel.setErrorStateMapDefaultValue()
        mViewModel.getUserShopInfo()

        verifyGetUserShopInfoCalled()
        val expectedResult = SettingResponseState.SettingError(error)
        mViewModel.userShopInfoLiveData.verifyStateErrorEquals(expectedResult)
    }

    @Test
    fun `when getBalanceInfo success should set live data state success`() = runBlocking {
        val uiModel = OthersBalance()
        onGetBalance_thenReturn(uiModel)

        mViewModel.setErrorStateMapDefaultValue()
        mViewModel.getBalanceInfo()

        verifyGetBalanceCalled()
        assert((mViewModel.balanceInfoLiveData.value as? SettingResponseState.SettingSuccess)?.data?.balanceValue == uiModel.totalBalance.orEmpty())
    }

    @Test
    fun `when getBalanceInfo error should set live data state error`() = runBlocking {
        val error = IllegalStateException()
        onGetBalance_thenThrow(error)

        mViewModel.setErrorStateMapDefaultValue()
        mViewModel.getBalanceInfo()

        verifyGetBalanceCalled()
        val expectedResult = SettingResponseState.SettingError(error)
        mViewModel.balanceInfoLiveData.verifyStateErrorEquals(expectedResult)
    }

    @Test
    fun `when getKreditTopads success should set live data state success`() = runBlocking {
        val topAdsKredit = 100f
        onGetTopAdsKredit_thenReturn(topAdsKredit)

        mViewModel.setErrorStateMapDefaultValue()
        mViewModel.getKreditTopAds()

        verifyGetTopAdsKreditCalled()
        assert((mViewModel.kreditTopAdsLiveData.value as? SettingResponseState.SettingSuccess)?.data?.balanceValue == topAdsKredit.getCurrencyFormatted())
    }

    @Test
    fun `when getKreditTopads error should set live data state error`() = runBlocking {
        val error = IllegalStateException()
        onGetTopAdsKredit_thenThrow(error)

        mViewModel.setErrorStateMapDefaultValue()
        mViewModel.getKreditTopAds()

        verifyGetTopAdsKreditCalled()
        val expectedResult = SettingResponseState.SettingError(error)
        mViewModel.kreditTopAdsLiveData.verifyStateErrorEquals(expectedResult)
    }

    @Test
    fun `when getAllOtherMenuData top ads topup value success should set live data success`() = runBlocking {
        val isTopAdsAutoTopup = true
        onGetTopAdsAutoTopup_thenReturn(true)

        mViewModel.getAllOtherMenuData()

        verifyGetTopAdsAutoTopupCalled()
        val expectedResult = Success(isTopAdsAutoTopup)
        mViewModel.isTopAdsAutoTopupLiveData.verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when getAllOtherMenuData top ads topup value error should set live data fail`() = runBlocking {
        val error = IllegalStateException()
        onGetTopAdsAutoTopup_thenThrow(error)

        mViewModel.getAllOtherMenuData()

        verifyGetTopAdsAutoTopupCalled()
        val expectedResult = Fail(error)
        mViewModel.isTopAdsAutoTopupLiveData.verifyErrorEquals(expectedResult)
    }

    private suspend fun onGetShopBadge_thenReturn(shopBadge: String) {
        coEvery { getShopBadgeUseCase.executeOnBackground() } returns shopBadge
    }

    private suspend fun onGetShopBadge_thenThrow(exception: Exception = IllegalStateException()) {
        coEvery { getShopBadgeUseCase.executeOnBackground() } throws exception
    }

    private suspend fun verifyGetShopBadgeCalled(atLeast: Int = 1) {
        coVerify(atLeast = atLeast) { getShopBadgeUseCase.executeOnBackground() }
    }

    private suspend fun verifyGetShopBadgeNotCalled() {
        coVerify(exactly = 0) { getShopBadgeUseCase.executeOnBackground() }
    }

    private suspend fun onGetShopTotalFollowers_thenReturn(totalFollowers: Long) {
        coEvery { getShopTotalFollowersUseCase.executeOnBackground() } returns totalFollowers
    }

    private suspend fun onGetShopTotalFollowers_thenThrow(exception: Exception = IllegalStateException()) {
        coEvery { getShopTotalFollowersUseCase.executeOnBackground() } throws exception
    }

    private suspend fun verifyGetShopTotalFollowersCalled(atLeast: Int = 1) {
        coVerify(atLeast = atLeast) { getShopTotalFollowersUseCase.executeOnBackground() }
    }

    private suspend fun verifyGetShopTotalFollowersNotCalled() {
        coVerify(exactly = 0) { getShopTotalFollowersUseCase.executeOnBackground() }
    }

    private suspend fun onGetUserShopInfo_thenReturn(shopInfo: UserShopInfoWrapper) {
        coEvery { getUserShopInfoUseCase.executeOnBackground() } returns shopInfo
    }

    private suspend fun onGetUserShopInfo_thenThrow(exception: Exception = IllegalStateException()) {
        coEvery { getUserShopInfoUseCase.executeOnBackground() } throws exception
    }

    private suspend fun verifyGetUserShopInfoCalled(atLeast: Int = 1) {
        coVerify(atLeast = atLeast) { getUserShopInfoUseCase.executeOnBackground() }
    }

    private suspend fun verifyGetUserShopInfoNotCalled() {
        coVerify(exactly = 0) { getUserShopInfoUseCase.executeOnBackground() }
    }

    private suspend fun verifyGetFreeShippingStatusCalled() {
        coVerify { getShopFreeShippingInfoUseCase.execute(any()) }
    }

    private suspend fun onGetShopOperational_thenReturn(shopOperational: ShopOperationalUiModel) {
        coEvery { getShopOperationalUseCase.executeOnBackground() } returns shopOperational
    }

    private suspend fun onGetShopOperational_thenThrow(exception: Exception = IllegalStateException()) {
        coEvery { getShopOperationalUseCase.executeOnBackground() } throws exception
    }

    private suspend fun verifyGetShopOperationalCalled(atLeast: Int = 1) {
        coVerify(atLeast = atLeast) { getShopOperationalUseCase.executeOnBackground() }
    }

    private suspend fun verifyGetShopOperationalNotCalled() {
        coVerify(exactly = 0) { getShopOperationalUseCase.executeOnBackground() }
    }

    private suspend fun onGetBalance_thenReturn(balance: OthersBalance) {
        coEvery { balanceInfoUseCase.executeOnBackground() } returns balance
    }

    private suspend fun onGetBalance_thenThrow(exception: Exception = IllegalStateException()) {
        coEvery { balanceInfoUseCase.executeOnBackground() } throws exception
    }

    private suspend fun verifyGetBalanceCalled(atLeast: Int = 1) {
        coVerify(atLeast = atLeast) { balanceInfoUseCase.executeOnBackground() }
    }

    private suspend fun verifyGetBalanceNotCalled() {
        coVerify(exactly = 0) { balanceInfoUseCase.executeOnBackground() }
    }

    private suspend fun onGetTopAdsKredit_thenReturn(kredit: Float) {
        coEvery { topAdsDashboardDepositUseCase.executeOnBackground() } returns kredit
    }

    private suspend fun onGetTopAdsKredit_thenThrow(exception: Exception = IllegalStateException()) {
        coEvery { topAdsDashboardDepositUseCase.executeOnBackground() } throws exception
    }

    private suspend fun verifyGetTopAdsKreditCalled(atLeast: Int = 1) {
        coVerify(atLeast = atLeast) { topAdsDashboardDepositUseCase.executeOnBackground() }
    }

    private suspend fun verifyGetTopAdsKreditNotCalled() {
        coVerify(exactly = 0) { topAdsDashboardDepositUseCase.executeOnBackground() }
    }

    private suspend fun onGetTopAdsAutoTopup_thenReturn(isAutoTopup: Boolean) {
        coEvery { topAdsAutoTopupUseCase.executeOnBackground() } returns isAutoTopup
    }

    private suspend fun onGetTopAdsAutoTopup_thenThrow(exception: Exception = IllegalStateException()) {
        coEvery { topAdsAutoTopupUseCase.executeOnBackground() } throws exception
    }

    private suspend fun verifyGetTopAdsAutoTopupCalled(atLeast: Int = 1) {
        coVerify(atLeast = atLeast) { topAdsAutoTopupUseCase.executeOnBackground() }
    }

}