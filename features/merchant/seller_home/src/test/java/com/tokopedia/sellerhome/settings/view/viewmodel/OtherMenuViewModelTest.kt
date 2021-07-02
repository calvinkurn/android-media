package com.tokopedia.sellerhome.settings.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.gm.common.constant.END_PERIOD
import com.tokopedia.gm.common.domain.interactor.GetShopInfoPeriodUseCase
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.seller.menu.common.domain.entity.OthersBalance
import com.tokopedia.seller.menu.common.domain.entity.UserShopInfoResponse
import com.tokopedia.seller.menu.common.domain.usecase.*
import com.tokopedia.seller.menu.common.view.uimodel.UserShopInfoWrapper
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingResponseState
import com.tokopedia.seller.menu.common.view.uimodel.base.ShopType
import com.tokopedia.seller.menu.common.view.uimodel.base.partialresponse.PartialSettingSuccessInfoType
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.ShopBadgeUiModel
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.common.viewmodel.NonNullLiveData
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
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.*
import java.lang.reflect.Field

@ExperimentalCoroutinesApi
class OtherMenuViewModelTest {

    @RelaxedMockK
    lateinit var getAllShopInfoUseCase: GetAllShopInfoUseCase

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

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var isToasterAlreadyShownField: Field

    private lateinit var mViewModel: OtherMenuViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        mViewModel =
                OtherMenuViewModel(
                        coroutineTestRule.dispatchers,
                        getAllShopInfoUseCase,
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
    }

    @Test
    fun `success get all setting shop info data`() = runBlocking {
        val userShopInfoWrapper = UserShopInfoWrapper(shopType = ShopType.OfficialStore)
        val partialShopInfoSuccess = PartialSettingSuccessInfoType.PartialShopSettingSuccessInfo(
                userShopInfoWrapper,
                anyLong(),
                anyString()
        )
        val partialTopAdsSuccess = PartialSettingSuccessInfoType.PartialTopAdsSettingSuccessInfo(
                OthersBalance(),
                anyFloat()
        )
        val successPair = Pair(partialShopInfoSuccess, partialTopAdsSuccess)

        coEvery {
            getAllShopInfoUseCase.executeOnBackground()
        } returns successPair

        mViewModel.getAllSettingShopInfo()

        coVerify {
            getAllShopInfoUseCase.executeOnBackground()
        }

        assertEquals((mViewModel.settingShopInfoLiveData.value as? Success)?.data?.shopBadgeUiModel, ShopBadgeUiModel(partialShopInfoSuccess.shopBadgeUrl))
    }

    @Test
    fun `error get setting shop info data`() = runBlocking {
        val throwable = ResponseErrorException()

        coEvery {
            getAllShopInfoUseCase.executeOnBackground()
        } throws throwable

        mViewModel.getAllSettingShopInfo()

        coVerify {
            getAllShopInfoUseCase.executeOnBackground()
        }

        assert(mViewModel.settingShopInfoLiveData.value == Fail(throwable))
    }

    @Test
    fun `Check delay will alter isToasterAlreadyShown between true and false`() = runBlocking {
        coroutineTestRule.runBlockingTest {
            val mockViewModel = spyk(mViewModel, recordPrivateCalls = true)

            mockViewModel.getAllSettingShopInfo(true)

            verify {
                mockViewModel["checkDelayErrorResponseTrigger"]()
            }

            mockViewModel.isToasterAlreadyShown.observeOnce {
                assertTrue(it)
            }

            advanceTimeBy(5000L)

            mockViewModel.isToasterAlreadyShown.observeOnce {
                assertFalse(it)
            }
        }
    }

    @Test
    fun `Toaster already shown will not alter values`() {
        val isToasterAlreadyShown = mViewModel.isToasterAlreadyShown.value
        mViewModel.getAllSettingShopInfo(false)
        assertEquals(isToasterAlreadyShown, mViewModel.isToasterAlreadyShown.value)
    }

    @Test
    fun `will not change live data value if toaster is already shown`() {
        coroutineTestRule.runBlockingTest {
            mViewModel.getAllSettingShopInfo(true)
            mViewModel.isToasterAlreadyShown.observeOnce {
                assertTrue(it)
            }
            advanceTimeBy(10L)
            mViewModel.getAllSettingShopInfo(true)
            mViewModel.isToasterAlreadyShown.observeOnce {
                assertTrue(it)
            }
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

        mViewModel.getShopOperational()

        val expectedResult = SettingResponseState.SettingError(error)

        mViewModel.shopOperationalLiveData
            .verifyStateErrorEquals(expectedResult)
    }

    private suspend fun onGetShopBadge_thenReturn(shopBadge: String) {
        coEvery { getShopBadgeUseCase.executeOnBackground() } returns shopBadge
    }

    private suspend fun onGetShopBadge_thenThrow(exception: Exception) {
        coEvery { getShopBadgeUseCase.executeOnBackground() } throws exception
    }

    private suspend fun onGetShopTotalFollowers_thenReturn(totalFollowers: Long) {
        coEvery { getShopTotalFollowersUseCase.executeOnBackground() } returns totalFollowers
    }

    private suspend fun onGetShopTotalFollowers_thenThrow(exception: Exception) {
        coEvery { getShopTotalFollowersUseCase.executeOnBackground() } throws exception
    }

    private suspend fun onGetUserShopInfo_thenReturn(shopInfo: UserShopInfoWrapper) {
        coEvery { getUserShopInfoUseCase.executeOnBackground() } returns shopInfo
    }

    private suspend fun onGetUserShopInfo_thenThrow(exception: Exception) {
        coEvery { getUserShopInfoUseCase.executeOnBackground() } throws exception
    }

    private suspend fun onGetShopOperational_thenReturn(shopOperational: ShopOperationalUiModel) {
        coEvery { getShopOperationalUseCase.executeOnBackground() } returns shopOperational
    }

    private suspend fun onGetShopOperational_thenThrow(exception: Exception) {
        coEvery { getShopOperationalUseCase.executeOnBackground() } throws exception
    }

    private suspend fun onGetBalance_thenReturn(balance: OthersBalance) {
        coEvery { balanceInfoUseCase.executeOnBackground() } returns balance
    }

    private suspend fun onGetBalance_thenThrow(exception: Exception) {
        coEvery { balanceInfoUseCase.executeOnBackground() } throws exception
    }

    private suspend fun onGetTopAdsKredit_thenReturn(kredit: Float) {
        coEvery { topAdsDashboardDepositUseCase.executeOnBackground() } returns kredit
    }

    private suspend fun onGetTopAdsKredit_thenThrow(exception: Exception) {
        coEvery { topAdsDashboardDepositUseCase.executeOnBackground() } throws exception
    }

    private suspend fun onGetTopAdsAutoTopup_thenReturn(isAutoTopup: Boolean) {
        coEvery { topAdsAutoTopupUseCase.executeOnBackground() } returns isAutoTopup
    }

    private suspend fun onGetTopAdsAutoTopup_thenThrow(exception: Exception) {
        coEvery { topAdsAutoTopupUseCase.executeOnBackground() } throws exception
    }

}