package com.tokopedia.shop_settings.viewmodel.shopsettingsinfo

import com.tokopedia.gm.common.data.source.cloud.model.GoldGetPmOsStatus
import com.tokopedia.shop.common.constant.ShopScheduleActionDef
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop.settings.basicinfo.data.CheckShopIsOfficialModel
import com.tokopedia.shop.settings.basicinfo.domain.CheckOfficialStoreTypeUseCase
import com.tokopedia.shop_settings.common.util.LiveDataUtil.observeAwaitValue
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test

@ExperimentalCoroutinesApi
class ShopSettingsInfoViewModelTest : ShopSettingsInfoViewModelTestFixture() {

    @Test
    fun `when validate os merchant type with provided shopId should return success`() {
        runBlocking {
            mockkObject(CheckOfficialStoreTypeUseCase)
            onCheckOsMerchantType_thenReturn()

            val shopId: Int = 123456
            shopSettingsInfoViewModel.validateOsMerchantType(shopId)

            verifySuccessCheckOsMerchantTypeCalled(shopId)

            val expectedValue = Success(CheckShopIsOfficialModel())
            assertTrue(shopSettingsInfoViewModel.checkOsMerchantTypeData.value is Success)
            shopSettingsInfoViewModel.checkOsMerchantTypeData.verifySuccessEquals(expectedValue)
        }
    }


    @Test
    fun `when get shop data with provided shopid and includeos should return success`() {
        runBlocking {
            val shopId: String = "123456"
            val includeOs: Boolean = false
            shopSettingsInfoViewModel.getShopData(shopId, includeOs)

            val shopBasicData = ShopBasicDataModel()
            val goldGetPmOsStatus = GoldGetPmOsStatus()

            val expectedResultShopBasicData = Success(shopBasicData)
            val expectedResultGoldGetPmOsStatus = Success(goldGetPmOsStatus)

            assertTrue(shopSettingsInfoViewModel.shopBasicData.value is Success)
            shopSettingsInfoViewModel.shopBasicData
                    .verifySuccessEquals(expectedResultShopBasicData)

            assertTrue(shopSettingsInfoViewModel.shopStatusData.value is Success)
            shopSettingsInfoViewModel.shopStatusData
                    .verifySuccessEquals(expectedResultGoldGetPmOsStatus)
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
    fun `when reset all live data to be null`() {
        shopSettingsInfoViewModel.resetAllLiveData()

        assertTrue(shopSettingsInfoViewModel.shopBasicData.value == null)
        assertTrue(shopSettingsInfoViewModel.checkOsMerchantTypeData.value == null)
        assertTrue(shopSettingsInfoViewModel.shopStatusData.value == null)
        assertTrue(shopSettingsInfoViewModel.updateScheduleResult.value == null)

    }

}