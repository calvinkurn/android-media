package com.tokopedia.sellerhome.settings.view.viewmodel

import com.tokopedia.gm.common.constant.END_PERIOD
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.seller.menu.common.domain.entity.OthersBalance
import com.tokopedia.seller.menu.common.view.uimodel.UserShopInfoWrapper
import com.tokopedia.seller.menu.common.view.uimodel.base.PowerMerchantStatus
import com.tokopedia.seller.menu.common.view.uimodel.base.RegularMerchant
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingResponseState
import com.tokopedia.seller.menu.common.view.uimodel.base.ShopType
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.settings.view.adapter.uimodel.OtherMenuShopShareData
import com.tokopedia.sellerhome.settings.view.adapter.uimodel.ShopOperationalData
import com.tokopedia.sellerhome.utils.observeAwaitValue
import com.tokopedia.sellerhome.utils.observeOnce
import com.tokopedia.sellerhome.utils.verifyStateErrorEquals
import com.tokopedia.sellerhome.utils.verifyStateSuccessEquals
import com.tokopedia.shop.common.data.source.cloud.model.FreeOngkir
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfoFreeShipping
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

@ExperimentalCoroutinesApi
class OtherMenuViewModelTest : OtherMenuViewModelTestFixture() {

    @Test
    fun `when onCheckDelayErrorResponseTrigger should alter toaster flag between true and false`() {
        coroutineTestRule.runBlockingTest {
            mViewModel.setDefaultToasterState(false)
            mViewModel.onCheckDelayErrorResponseTrigger()

            mViewModel.isToasterAlreadyShown.verifyValueEquals(true)

            advanceTimeBy(5000L)

            mViewModel.isToasterAlreadyShown.verifyValueEquals(false)
        }
    }

    @Test
    fun `when onCheckDelayErrorResponseTrigger with isToasterAlreadyShown default value true should do nothing`() {
        coroutineTestRule.runBlockingTest {
            mViewModel.setDefaultToasterState(true)
            mViewModel.onCheckDelayErrorResponseTrigger()

            mViewModel.isToasterAlreadyShown.verifyValueEquals(true)

            advanceTimeBy(5000L)

            mViewModel.isToasterAlreadyShown.verifyValueEquals(true)
        }
    }

    @Test
    fun `when getAllOtherMenuData called should get all other page data`() {
        coroutineTestRule.runBlockingTest {
            onGetFreeShippingRemoteConfigDisabled_thenReturn(
                isFreeShippingEnabled = false,
                isInTransitionPeriod = false
            )
            mViewModel.getAllOtherMenuData()

            verifyGetShopBadgeCalled()
            verifyGetShopTotalFollowersCalled()
            verifyGetUserShopInfoCalled()
            verifyGetFreeShippingCalled()
            verifyGetShopOperationalCalled()
            verifyGetBalanceCalled()
            verifyGetTopAdsKreditCalled()
            verifyGetFreeShippingCalled()
        }
    }

    @Test
    fun `when getAllOtherMenuData called with free shipping disabled and in transition period should get all other page data`() {
        coroutineTestRule.runBlockingTest {
            onGetFreeShippingRemoteConfigDisabled_thenReturn(
                isFreeShippingEnabled = false,
                isInTransitionPeriod = true
            )
            mViewModel.getFreeShippingStatus()

            verifyGetFreeShippingNotCalled()
            val expectedResult = SettingResponseState.SettingSuccess(false to "")
            mViewModel.freeShippingLiveData.verifyStateSuccessEquals(expectedResult)
        }
    }

    @Test
    fun `when getAllOtherMenuData called with free shipping enabled and not in transition period should get all other page data`() {
        coroutineTestRule.runBlockingTest {
            onGetFreeShippingRemoteConfigDisabled_thenReturn(
                isFreeShippingEnabled = true,
                isInTransitionPeriod = false
            )
            mViewModel.getFreeShippingStatus()

            verifyGetFreeShippingNotCalled()
            val expectedResult = SettingResponseState.SettingSuccess(false to "")
            mViewModel.freeShippingLiveData.verifyStateSuccessEquals(expectedResult)
        }
    }

    @Test
    fun `when getAllOtherMenuData and two or more (but not all) data fails, should set show multiple error toaster live data true`() =
        runBlocking {
            onGetShopTotalFollowers_thenThrow()
            onGetUserShopInfo_thenThrow()
            onGetShopOperational_thenThrow()
            onGetBalance_thenThrow()
            onGetTopAdsKredit_thenThrow()
            onGetFreeShipping_thenThrow()
            onGetShopBadge_thenReturn("")

            mViewModel.getAllOtherMenuData()

            assert(mViewModel.shouldShowMultipleErrorToaster.value == true)
        }

    @Test
    fun `when reloadErrorData should reload data that was failed`() =
        coroutineTestRule.runBlockingTest {
            onGetShopBadge_thenThrow()
            onGetShopTotalFollowers_thenThrow()
            onGetUserShopInfo_thenThrow()
            onGetShopOperational_thenThrow()
            onGetBalance_thenThrow()
            onGetTopAdsKredit_thenThrow()
            onGetFreeShipping_thenThrow()
            onGetFreeShippingRemoteConfigDisabled_thenReturn(
                isFreeShippingEnabled = false,
                isInTransitionPeriod = false
            )
            mViewModel.getAllOtherMenuData()

            mViewModel.reloadErrorData()
            mViewModel.onShownMultipleError()

            verifyGetShopBadgeCalled(atLeast = 2)
            verifyGetShopTotalFollowersCalled(atLeast = 2)
            verifyGetShopOperationalCalled(atLeast = 2)
            verifyGetUserShopInfoCalled(atLeast = 2)
            verifyGetBalanceCalled(atLeast = 2)
            verifyGetTopAdsKreditCalled(atLeast = 2)
            verifyGetFreeShippingCalled(atLeast = 2)
        }

    @Test
    fun `when reloadErrorData should not reload data that was success`() =
        runBlocking {
            onGetShopBadge_thenThrow()
            onGetShopTotalFollowers_thenThrow()
            onGetUserShopInfo_thenThrow()
            onGetShopOperational_thenThrow()
            onGetBalance_thenThrow()
            onGetFreeShipping_thenThrow()
            onGetTopAdsKredit_thenReturn(0F)
            onGetFreeShippingRemoteConfigDisabled_thenReturn(
                isFreeShippingEnabled = false,
                isInTransitionPeriod = false
            )
            mViewModel.getAllOtherMenuData()

            mViewModel.reloadErrorData()

            verifyGetShopBadgeCalled(atLeast = 2)
            verifyGetShopTotalFollowersCalled(atLeast = 2)
            verifyGetShopOperationalCalled(atLeast = 2)
            verifyGetUserShopInfoCalled(atLeast = 2)
            verifyGetBalanceCalled(atLeast = 2)
            verifyGetFreeShippingCalled(atLeast = 2)
            verifyGetTopAdsKreditCalled()
        }

    @Test
    fun `when reloadErrorData and error state map hasn't been set, should not reload any data`() =
        coroutineTestRule.runBlockingTest {
            onGetShopBadge_thenThrow()
            onGetShopTotalFollowers_thenThrow()
            onGetUserShopInfo_thenThrow()
            onGetShopOperational_thenThrow()
            onGetBalance_thenThrow()
            onGetFreeShipping_thenThrow()
            onGetTopAdsKredit_thenThrow()
            onGetFreeShippingRemoteConfigDisabled_thenReturn(
                isFreeShippingEnabled = false,
                isInTransitionPeriod = false
            )

            mViewModel.reloadErrorData()

            verifyGetShopBadgeNotCalled()
            verifyGetShopTotalFollowersNotCalled()
            verifyGetShopOperationalNotCalled()
            verifyGetUserShopInfoNotCalled()
            verifyGetBalanceNotCalled()
            verifyGetFreeShippingNotCalled()
            verifyGetTopAdsKreditNotCalled()
        }

    @Test
    fun `when getAllOtherMenuData and all secondary info success, should swipe with delay`() =
        coroutineTestRule.runBlockingTest {
            onGetShopBadge_thenReturn("")
            onGetShopOperational_thenReturn(
                ShopOperationalData(
                    isShopOpen = true,
                    isShopClosed = false,
                    timeDescriptionRes = R.string.shop_operational_hour_24_hour,
                    timeDescription = "",
                    shopSettingAccess = true
                )
            )
            onGetUserShopInfo_thenReturn(UserShopInfoWrapper(null))
            onGetShopTotalFollowers_thenReturn(100L)
            onGetFreeShipping_thenReturn(
                listOf(
                    ShopInfoFreeShipping.FreeShippingInfo(
                        FreeOngkir(
                            isActive = true
                        )
                    )
                )
            )
            onGetFreeShippingRemoteConfigDisabled_thenReturn(
                isFreeShippingEnabled = false,
                isInTransitionPeriod = false
            )

            mViewModel.getAllOtherMenuData()

            mViewModel.shouldSwipeSecondaryInfo.observeOnce {
                Assert.assertFalse(it)
            }

            advanceTimeBy(1000L)

            mViewModel.shouldSwipeSecondaryInfo.observeOnce {
                Assert.assertTrue(it)
            }
        }

    @Test
    fun `when getAllOtherMenuData but not all secondary info success, should not swipe gently`() =
        coroutineTestRule.runBlockingTest {
            onGetShopBadge_thenThrow()
            onGetShopOperational_thenThrow()
            onGetUserShopInfo_thenThrow()
            onGetShopTotalFollowers_thenThrow()
            onGetFreeShipping_thenThrow()

            mViewModel.getAllOtherMenuData()

            assert(mViewModel.shouldSwipeSecondaryInfo.value == false)
        }

    @Test
    fun `when startToggleTopadsCredit and kredit topads is 0f, should toggle topads topup with delay`() =
        coroutineTestRule.runBlockingTest {
            onGetTopAdsKredit_thenReturn(0f)

            mViewModel.getKreditTopAds()

            mViewModel.startToggleTopadsCredit()

            advanceTimeBy(2000L)

            mViewModel.numberOfTopupToggleCounts.observeOnce {
                Assert.assertTrue(it == 1)
            }

            mViewModel.startToggleTopadsCredit()

            advanceTimeBy(1000L)

            mViewModel.numberOfTopupToggleCounts.observeOnce {
                Assert.assertTrue(it == 2)
            }
        }

    @Test
    fun `when startToggleTopadsCredit and kredit topads is 0f, should toggle topads topup with delay until max limit`() =
        coroutineTestRule.runBlockingTest {
            onGetTopAdsKredit_thenReturn(0f)

            mViewModel.getKreditTopAds()

            mViewModel.startToggleTopadsCredit()

            advanceTimeBy(2000L)

            mViewModel.numberOfTopupToggleCounts.observeOnce {
                Assert.assertTrue(it == 1)
            }

            mViewModel.startToggleTopadsCredit()

            advanceTimeBy(1000L)

            mViewModel.numberOfTopupToggleCounts.observeOnce {
                Assert.assertTrue(it == 2)
            }

            mViewModel.startToggleTopadsCredit()

            advanceTimeBy(1000L)

            mViewModel.numberOfTopupToggleCounts.observeOnce {
                Assert.assertTrue(it == 3)
            }

            mViewModel.startToggleTopadsCredit()

            advanceTimeBy(1000L)

            mViewModel.numberOfTopupToggleCounts.observeOnce {
                Assert.assertTrue(it == 4)
            }

            mViewModel.startToggleTopadsCredit()

            mViewModel.numberOfTopupToggleCounts.observeOnce {
                Assert.assertTrue(it == 4)
            }
        }

    @Test
    fun `when startToggleTopadsCredit, kredit topads is 0f, and job is not completed yet, should not toggle topads topup`() =
        coroutineTestRule.runBlockingTest {
            onGetTopAdsKredit_thenReturn(0f)
            mViewModel.getKreditTopAds()
            mViewModel.startToggleTopadsCredit()
            advanceTimeBy(100L)

            mViewModel.startToggleTopadsCredit()

            Assert.assertTrue(mViewModel.numberOfTopupToggleCounts.value == null)
        }

    @Test
    fun `when startToggleTopadsCredit, kredit topads is 0f, and job is completed, should toggle topads topup`() =
        coroutineTestRule.runBlockingTest {
            onGetTopAdsKredit_thenReturn(0f)
            mViewModel.getKreditTopAds()
            mViewModel.startToggleTopadsCredit()
            advanceTimeBy(3000L)

            mViewModel.startToggleTopadsCredit()

            mViewModel.numberOfTopupToggleCounts.observeOnce {
                Assert.assertTrue(it == 1)
            }
        }

    @Test
    fun `when startToggleTopadsCredit and kredit topads is not 0f, should not toggle topads topup`() =
        coroutineTestRule.runBlockingTest {
            onGetTopAdsKredit_thenReturn(100f)
            mViewModel.getKreditTopAds()
            mViewModel.startToggleTopadsCredit()

            Assert.assertTrue(mViewModel.numberOfTopupToggleCounts.value == null)
        }

    @Test
    fun `when getAllOtherMenuData should cancel toggle topads job`() =
        coroutineTestRule.runBlockingTest {
            onGetTopAdsKredit_thenReturn(0f)
            mViewModel.getKreditTopAds()
            mViewModel.startToggleTopadsCredit()
            advanceTimeBy(100L)

            mViewModel.getAllOtherMenuData()
            advanceTimeBy(3000L)

            Assert.assertTrue(mViewModel.numberOfTopupToggleCounts.value == null)
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
    fun `getShopPeriodType should error`() {
        val error = IllegalStateException()

        coEvery {
            getShopInfoPeriodUseCase.executeOnBackground()
        } throws error

        mViewModel.getShopPeriodType()

        coVerify {
            getShopInfoPeriodUseCase.executeOnBackground()
        }

        assert(mViewModel.shopPeriodType.observeAwaitValue() is Fail)
    }

    @Test
    fun `when getShopOperational success should set live data success`() =
        coroutineTestRule.runBlockingTest {
            val uiModel = ShopOperationalData(
                isShopOpen = true,
                isShopClosed = false,
                timeDescriptionRes = R.string.shop_operational_hour_24_hour,
                timeDescription = "",
                shopSettingAccess = true
            )

            onGetShopOperational_thenReturn(uiModel)

            mViewModel.getShopOperational()

            val expectedResult = SettingResponseState.SettingSuccess(uiModel)

            mViewModel.shopOperationalLiveData
                .verifyStateSuccessEquals(expectedResult)
        }

    @Test
    fun `when getShopOperational error should set live data fail`() =
        coroutineTestRule.runBlockingTest {
            val error = IllegalStateException()

            onGetShopOperational_thenThrow(error)

            mViewModel.getShopOperational()

            val expectedResult = SettingResponseState.SettingError(error)

            mViewModel.shopOperationalLiveData
                .verifyStateErrorEquals(expectedResult)
        }

    @Test
    fun `when getShopBadge success should set live data state success`() =
        coroutineTestRule.runBlockingTest {
            val badgeUrl = "www.abc.com"
            onGetShopBadge_thenReturn(badgeUrl)

            mViewModel.getShopBadge()

            verifyGetShopBadgeCalled()
            val expectedResult = SettingResponseState.SettingSuccess(badgeUrl)
            mViewModel.shopBadgeLiveData.verifyStateSuccessEquals(expectedResult)
        }

    @Test
    fun `when getShopBadge error should set live data state error`() =
        coroutineTestRule.runBlockingTest {
            val error = IllegalStateException()
            onGetShopBadge_thenThrow(error)

            mViewModel.getShopBadge()

            verifyGetShopBadgeCalled()
            val expectedResult = SettingResponseState.SettingError(error)
            mViewModel.shopBadgeLiveData.verifyStateErrorEquals(expectedResult)
        }

    @Test
    fun `when getShopFollowers success should set live data state success`() =
        coroutineTestRule.runBlockingTest {
            val shopFollowers = 10L
            onGetShopTotalFollowers_thenReturn(shopFollowers)

            mViewModel.getShopTotalFollowers()

            verifyGetShopTotalFollowersCalled()
            val expectedResult = SettingResponseState.SettingSuccess(shopFollowers.toString())
            mViewModel.shopTotalFollowersLiveData.verifyStateSuccessEquals(expectedResult)
        }

    @Test
    fun `when getShopFollowers success but result invalide should set live data state error`() =
        coroutineTestRule.runBlockingTest {
            val shopFollowers: Long = -1
            onGetShopTotalFollowers_thenReturn(shopFollowers)

            mViewModel.getShopTotalFollowers()

            verifyGetShopTotalFollowersCalled()
            assert(mViewModel.shopTotalFollowersLiveData.value is SettingResponseState.SettingError)
        }

    @Test
    fun `when getShopFollowers error should set live data state error`() =
        coroutineTestRule.runBlockingTest {
            val error = IllegalStateException()
            onGetShopTotalFollowers_thenThrow(error)

            mViewModel.getShopTotalFollowers()

            verifyGetShopTotalFollowersCalled()
            val expectedResult = SettingResponseState.SettingError(error)
            mViewModel.shopTotalFollowersLiveData.verifyStateErrorEquals(expectedResult)
        }

    @Test
    fun `when getFreeShippingStatus disabled by remote config, should set live data false and empty`() {
        coroutineTestRule.runBlockingTest {
            onGetFreeShippingRemoteConfigDisabled_thenReturn(
                isFreeShippingEnabled = true,
                isInTransitionPeriod = true
            )

            mViewModel.getFreeShippingStatus()

            verifyGetFreeShippingNotCalled()
            val expectedResult = SettingResponseState.SettingSuccess(false to "")
            mViewModel.freeShippingLiveData.verifyStateSuccessEquals(expectedResult)
        }
    }

    @Test
    fun `when getUserShopInfo success should set live data state success`() =
        coroutineTestRule.runBlockingTest {
            val userShopInfoWrapper = UserShopInfoWrapper(ShopType.OfficialStore)
            onGetUserShopInfo_thenReturn(userShopInfoWrapper)

            mViewModel.getUserShopInfo()

            verifyGetUserShopInfoCalled()
            assert((mViewModel.userShopInfoLiveData.value as? SettingResponseState.SettingSuccess)?.data?.userShopInfoWrapper == userShopInfoWrapper)
        }

    @Test
    fun `when getUserShopInfo returns RM status, should set user session data accordingly`() =
            coroutineTestRule.runBlockingTest {
                val userShopInfoWrapper = UserShopInfoWrapper(RegularMerchant.NeedUpgrade)
                onGetUserShopInfo_thenReturn(userShopInfoWrapper)

                mViewModel.getUserShopInfo()

                verifyGetUserShopInfoCalled()
                verifySetIsGoldMerchantCalled(false)
                verifySetIsPowerMerchantIdleCalled(false)
                verifySetIsOfficialStoreCalled(false)
            }

    @Test
    fun `when getUserShopInfo returns PM Inactive status, should set user session data accordingly`() =
            coroutineTestRule.runBlockingTest {
                val userShopInfoWrapper = UserShopInfoWrapper(PowerMerchantStatus.NotActive)
                onGetUserShopInfo_thenReturn(userShopInfoWrapper)

                mViewModel.getUserShopInfo()

                verifyGetUserShopInfoCalled()
                verifySetIsGoldMerchantCalled(false)
                verifySetIsPowerMerchantIdleCalled(true)
                verifySetIsOfficialStoreCalled(false)
            }

    @Test
    fun `when getUserShopInfo returns PM Active status, should set user session data accordingly`() =
            coroutineTestRule.runBlockingTest {
                val userShopInfoWrapper = UserShopInfoWrapper(PowerMerchantStatus.Active)
                onGetUserShopInfo_thenReturn(userShopInfoWrapper)

                mViewModel.getUserShopInfo()

                verifyGetUserShopInfoCalled()
                verifySetIsGoldMerchantCalled(true)
                verifySetIsPowerMerchantIdleCalled(false)
                verifySetIsOfficialStoreCalled(false)
            }

    @Test
    fun `when getUserShopInfo returns OS status, should set user session data accordingly`() =
            coroutineTestRule.runBlockingTest {
                val userShopInfoWrapper = UserShopInfoWrapper(ShopType.OfficialStore)
                onGetUserShopInfo_thenReturn(userShopInfoWrapper)

                mViewModel.getUserShopInfo()

                verifyGetUserShopInfoCalled()
                verifySetIsGoldMerchantCalled(true)
                verifySetIsPowerMerchantIdleCalled(false)
                verifySetIsOfficialStoreCalled(true)
            }

    @Test
    fun `when getUserShopInfo error should set live data state error`() =
        coroutineTestRule.runBlockingTest {
            val error = IllegalStateException()
            onGetUserShopInfo_thenThrow(error)

            mViewModel.getUserShopInfo()

            verifyGetUserShopInfoCalled()
            val expectedResult = SettingResponseState.SettingError(error)
            mViewModel.userShopInfoLiveData.verifyStateErrorEquals(expectedResult)
        }

    @Test
    fun `when getBalanceInfo success should set live data state success`() = runBlocking {
        val uiModel = OthersBalance()
        onGetBalance_thenReturn(uiModel)

        mViewModel.getBalanceInfo()

        verifyGetBalanceCalled()
        assert((mViewModel.balanceInfoLiveData.value as? SettingResponseState.SettingSuccess)?.data == uiModel.totalBalance.orEmpty())
    }

    @Test
    fun `when getBalanceInfo error should set live data state error`() = runBlocking {
        val error = IllegalStateException()
        onGetBalance_thenThrow(error)

        mViewModel.getBalanceInfo()

        verifyGetBalanceCalled()
        val expectedResult = SettingResponseState.SettingError(error)
        mViewModel.balanceInfoLiveData.verifyStateErrorEquals(expectedResult)
    }

    @Test
    fun `when getKreditTopads success should set live data state success`() = runBlocking {
        val topAdsKredit = 100f
        onGetTopAdsKredit_thenReturn(topAdsKredit)

        mViewModel.getKreditTopAds()

        verifyGetTopAdsKreditCalled()
        assert((mViewModel.kreditTopAdsLiveData.value as? SettingResponseState.SettingSuccess)?.data == topAdsKredit.getCurrencyFormatted())
    }

    @Test
    fun `when getKreditTopads error should set live data state error`() = runBlocking {
        val error = IllegalStateException()
        onGetTopAdsKredit_thenThrow(error)

        mViewModel.getKreditTopAds()

        verifyGetTopAdsKreditCalled()
        val expectedResult = SettingResponseState.SettingError(error)
        mViewModel.kreditTopAdsLiveData.verifyStateErrorEquals(expectedResult)
    }

    @Test
    fun `when getAllOtherMenuData top ads topup value then returns true should set live data success`() =
        runBlocking {
            val isTopAdsAutoTopup = true
            onGetTopAdsAutoTopup_thenReturn(true)

            mViewModel.getAllOtherMenuData()

            verifyGetTopAdsAutoTopupCalled()
            val expectedResult = Success(isTopAdsAutoTopup)
            mViewModel.isTopAdsAutoTopupLiveData.verifySuccessEquals(expectedResult)
        }

    @Test
    fun `when getAllOtherMenuData top ads topup value then returns false should set live data success`() =
        runBlocking {
            val isTopAdsAutoTopup = false
            onGetTopAdsAutoTopup_thenReturn(false)

            mViewModel.getAllOtherMenuData()

            verifyGetTopAdsAutoTopupCalled()
            val expectedResult = Success(isTopAdsAutoTopup)
            mViewModel.isTopAdsAutoTopupLiveData.verifySuccessEquals(expectedResult)
        }

    @Test
    fun `when getAllOtherMenuData top ads topup value error should set live data fail`() =
        runBlocking {
            val error = IllegalStateException()
            onGetTopAdsAutoTopup_thenThrow(error)

            mViewModel.getAllOtherMenuData()

            verifyGetTopAdsAutoTopupCalled()
            val expectedResult = Fail(error)
            mViewModel.isTopAdsAutoTopupLiveData.verifyErrorEquals(expectedResult)
        }

    @Test
    fun `when getShopShareInfoData shop share info value success should set live data success`() =
        runBlocking {
            val shopShareInfo = OtherMenuShopShareData()
            onGetShareInfo_thenReturn(shopShareInfo)

            mViewModel.getShopShareInfoData()

            verifyGetShareInfoCalled()
            assert(mViewModel.shopShareInfoLiveData.value == shopShareInfo)
        }

    @Test
    fun `when getShopShareInfoData shop share info value error should set live data null`() =
        runBlocking {
            val error = IllegalStateException()
            onGetShareInfo_thenThrow(error)

            mViewModel.getShopShareInfoData()

            verifyGetShareInfoCalled()
            assert(mViewModel.shopShareInfoLiveData.value == null)
        }

    @Test
    fun `when setErrorStateMapDefaultValue but errorStateMap is already set, should not set values again`() =
        runBlocking {
            onGetShopTotalFollowers_thenThrow()
            onGetUserShopInfo_thenThrow()
            onGetShopOperational_thenThrow()
            onGetBalance_thenThrow()
            onGetTopAdsKredit_thenThrow()
            onGetFreeShipping_thenThrow()
            onGetShopBadge_thenThrow()
            mViewModel.getAllOtherMenuData()
            val currentValue = mViewModel.shouldShowMultipleErrorToaster.value

            mViewModel.setErrorStateMapDefaultValue()

            Assert.assertTrue(mViewModel.shouldShowMultipleErrorToaster.value == currentValue)
        }

    @Test
    fun `when setSuccessStateMapDefaultValue but _secondarySuccessStateMap is already set, should not set values again`() =
        runBlocking {
            mViewModel.setSuccessStateMapDefaultValue()
            val currentValue = mViewModel.shouldSwipeSecondaryInfo.value

            mViewModel.setSuccessStateMapDefaultValue()

            Assert.assertTrue(mViewModel.shouldSwipeSecondaryInfo.value == currentValue)
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

    private suspend fun onGetShopOperational_thenReturn(shopOperational: ShopOperationalData) {
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

    private suspend fun onGetFreeShipping_thenReturn(freeShippingInfo: List<ShopInfoFreeShipping.FreeShippingInfo>) {
        coEvery { getShopFreeShippingInfoUseCase.execute(any()) } returns freeShippingInfo
    }

    private suspend fun onGetFreeShipping_thenThrow(exception: Exception = IllegalStateException()) {
        coEvery { getShopFreeShippingInfoUseCase.execute(any()) } throws exception
    }

    private suspend fun verifyGetFreeShippingCalled(atLeast: Int = 1) {
        coVerify(atLeast = atLeast) { getShopFreeShippingInfoUseCase.execute(any()) }
    }

    private suspend fun verifyGetFreeShippingNotCalled() {
        coVerify(exactly = 0) { getShopFreeShippingInfoUseCase.executeOnBackground() }
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

    private suspend fun onGetShareInfo_thenReturn(shareData: OtherMenuShopShareData) {
        coEvery { shopShareInfoUseCase.execute(any()) } returns shareData
    }

    private suspend fun onGetShareInfo_thenThrow(exception: Exception = IllegalStateException()) {
        coEvery { shopShareInfoUseCase.execute(any()) } throws exception
    }

    private suspend fun verifyGetShareInfoCalled(atLeast: Int = 1) {
        coVerify(atLeast = atLeast) { shopShareInfoUseCase.execute(any()) }
    }

    private fun verifySetIsGoldMerchantCalled(isGoldMerchant: Boolean) {
        verify { userSession.setIsGoldMerchant(isGoldMerchant) }
    }

    private fun verifySetIsPowerMerchantIdleCalled(isPowerMerchantIdle: Boolean) {
        verify { userSession.setIsPowerMerchantIdle(isPowerMerchantIdle) }
    }

    private fun verifySetIsOfficialStoreCalled(isOfficialStore: Boolean) {
        verify { userSession.setIsShopOfficialStore(isOfficialStore) }
    }

    private fun onGetFreeShippingRemoteConfigDisabled_thenReturn(
        isFreeShippingEnabled: Boolean,
        isInTransitionPeriod: Boolean
    ) {
        every {
            remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_FEATURE_DISABLED, true)
        } returns isFreeShippingEnabled
        every {
            remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_TRANSITION_PERIOD, true)
        } returns isInTransitionPeriod
    }

}