package com.tokopedia.shop_settings.viewmodel.shopsettingsinfo

import com.tokopedia.gm.common.data.source.local.model.PMStatusUiModel
import com.tokopedia.shop.common.constant.ShopScheduleActionDef
import com.tokopedia.shop.common.domain.interactor.GqlGetIsShopOsUseCase
import com.tokopedia.shop.common.graphql.data.isshopofficial.GetIsShopOfficialStore
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop_settings.common.util.LiveDataUtil.observeAwaitValue
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test

@ExperimentalCoroutinesApi
class ShopSettingsInfoViewModelTest : ShopSettingsInfoViewModelTestFixture() {

    @Test
    fun `when get shop data with provided shopid and includeos should return success`() {
        runBlocking {
            val shopId: String = "123456"
            val includeOs: Boolean = false
            val sampleShopBadgeValue = "shop_badge"
            val shopBasicData = ShopBasicDataModel()
            val pmOsStatus = PMStatusUiModel()
            val shopInfo = ShopInfo(
                    goldOS = ShopInfo.GoldOS(badge = sampleShopBadgeValue)
            )
            coEvery {
                getShopInfoUseCase.executeOnBackground()
            } returns shopInfo

            coEvery {
                getShopBasicDataUseCase.getData(any())
            } returns shopBasicData

            coEvery {
                getShopStatusUseCase.executeOnBackground()
            } returns pmOsStatus

            shopSettingsInfoViewModel.getShopData(shopId, includeOs)
            val expectedResultShopBadget = Success(sampleShopBadgeValue)
            val expectedResultShopBasicData = Success(shopBasicData)
            val expectedResultGoldGetPmOsStatus = Success(pmOsStatus)

            assertTrue(shopSettingsInfoViewModel.shopInfoData.value is Success)
            shopSettingsInfoViewModel.shopInfoData
                    .verifySuccessEquals(expectedResultShopBadget)

            assertTrue(shopSettingsInfoViewModel.shopBasicData.value is Success)
            shopSettingsInfoViewModel.shopBasicData
                    .verifySuccessEquals(expectedResultShopBasicData)

            assertTrue(shopSettingsInfoViewModel.shopStatusData.value is Success)
            shopSettingsInfoViewModel.shopStatusData
                    .verifySuccessEquals(expectedResultGoldGetPmOsStatus)
        }
    }

    @Test
    fun `when validate os merchant type with provided shopId should return success`() {
        runBlocking {
            mockkObject(GqlGetIsShopOsUseCase)

            coEvery { checkOsMerchantUseCase.executeOnBackground() } returns GetIsShopOfficialStore()

            val shopId: Int = 123456
            shopSettingsInfoViewModel.validateOsMerchantType(shopId)

            val expectedValue = Success(GetIsShopOfficialStore())
            assertTrue(shopSettingsInfoViewModel.checkOsMerchantTypeData.value is Success)
            shopSettingsInfoViewModel.checkOsMerchantTypeData.verifySuccessEquals(expectedValue)
        }
    }

    @Test
    fun `when update shop schedule with provided action open closeNow false should return success`() {
        val action: Int = ShopScheduleActionDef.OPEN
        val closeNow: Boolean = false
        val closeStart: String = ""
        val closeEnd: String = ""
        val closeNote: String = ""

        every {
            updateShopScheduleUseCase.getData(any())
        } returns "test string response"

        shopSettingsInfoViewModel.updateShopSchedule(action, closeNow, closeStart, closeEnd, closeNote)

        val isSuccessSubscribe = shopSettingsInfoViewModel.updateScheduleResult.observeAwaitValue()

        assertTrue(isSuccessSubscribe is Success)
    }

    @Test
    fun `when get shop data with provided shopid and includeos should return fail`() {
        runBlocking {
            val shopId: String = "123456"
            val includeOs: Boolean = false

            coEvery {
                getShopInfoUseCase.executeOnBackground()
            } throws Exception()

            shopSettingsInfoViewModel.getShopData(shopId, includeOs)

            assertTrue(shopSettingsInfoViewModel.shopInfoData.value is Fail)
        }
    }

    @Test
    fun `when get shop basic data with provided shopid and includeos should return fail`() {
        runBlocking {
            val shopId: String = "123456"
            val includeOs: Boolean = false

            coEvery {
                getShopBasicDataUseCase.getData(any())
            } throws Exception()

            shopSettingsInfoViewModel.getShopData(shopId, includeOs)

            assertTrue(shopSettingsInfoViewModel.shopBasicData.value is Fail)
        }
    }

    @Test
    fun `when get shop status with provided shopid and includeos should return fail`() {
        runBlocking {
            val shopId: String = "123456"
            val includeOs: Boolean = false

            coEvery {
                getShopStatusUseCase.executeOnBackground()
            } throws Exception()

            shopSettingsInfoViewModel.getShopData(shopId, includeOs)

            assertTrue(shopSettingsInfoViewModel.shopStatusData.value is Fail)
        }
    }

    @Test
    fun `when update shop schedule with provided action open closeNow false should return fail`() {
        val action: Int = ShopScheduleActionDef.OPEN
        val closeNow: Boolean = false
        val closeStart: String = ""
        val closeEnd: String = ""
        val closeNote: String = ""

        every {
            updateShopScheduleUseCase.getData(any())
        } throws Exception()

        shopSettingsInfoViewModel.updateShopSchedule(action, closeNow, closeStart, closeEnd, closeNote)

        val isSuccessSubscribe = shopSettingsInfoViewModel.updateScheduleResult.observeAwaitValue()

        assertTrue(isSuccessSubscribe is Fail)
    }

    @Test
    fun `when validate os merchant type with provided shopId should return fail`() {
        runBlocking {
            mockkObject(GqlGetIsShopOsUseCase)

            coEvery { checkOsMerchantUseCase.executeOnBackground() } throws Exception()

            val shopId: Int = 123456
            shopSettingsInfoViewModel.validateOsMerchantType(shopId)

            assertTrue(shopSettingsInfoViewModel.checkOsMerchantTypeData.value is Fail)
        }
    }

    @Test
    fun `when reset all live data to be null`() {
        shopSettingsInfoViewModel.resetAllLiveData()

        assertTrue(shopSettingsInfoViewModel.shopBasicData.value == null)
        assertTrue(shopSettingsInfoViewModel.checkOsMerchantTypeData.value == null)
        assertTrue(shopSettingsInfoViewModel.shopStatusData.value == null)
        assertTrue(shopSettingsInfoViewModel.updateScheduleResult.value == null)
        assertTrue(shopSettingsInfoViewModel.shopInfoData.value == null)

    }

}