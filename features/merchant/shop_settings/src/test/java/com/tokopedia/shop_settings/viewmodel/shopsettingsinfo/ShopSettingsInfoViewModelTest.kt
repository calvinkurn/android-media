package com.tokopedia.shop_settings.viewmodel.shopsettingsinfo

import com.tokopedia.gm.common.data.source.cloud.model.GoldGetPmOsStatus
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop.settings.basicinfo.data.CheckShopIsOfficialModel
import com.tokopedia.shop.settings.basicinfo.domain.CheckOfficialStoreTypeUseCase
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ShopSettingsInfoViewModelTest : ShopSettingsInfoViewModelTestFixture() {

    @Test
    fun `when detach view should unsubscribe use case`() {
        shopSettingsInfoViewModel.detachView()

        verifyUnsubscribeUseCase()
    }

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
            shopSettingsInfoViewModel.checkOsMerchantTypeData.verifyValueEquals(expectedValue)
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

            val expectedResult_shopBasicData = Success(shopBasicData)
            val expectedResult_goldGetPmOsStatus = Success(goldGetPmOsStatus)

            assertTrue(shopSettingsInfoViewModel.shopBasicData.value is Success)
            shopSettingsInfoViewModel.shopBasicData
                    .verifySuccessEquals(expectedResult_shopBasicData)

            assertTrue(shopSettingsInfoViewModel.shopStatusData.value is Success)
            shopSettingsInfoViewModel.shopStatusData
                    .verifySuccessEquals(expectedResult_goldGetPmOsStatus)
        }
    }

//    @Test
//    fun `when update shop schedule with provided action open closeNow false should return success`() {
//        runBlocking {
//            val action: Int = ShopScheduleActionDef.OPEN
//            val closeNow: Boolean = false
//            val closeStart: String = ""
//            val closeEnd: String = ""
//            val closeNote: String = ""
//            shopSettingsInfoViewModel.updateShopSchedule(action, closeNow, closeStart, closeEnd, closeNote)
//
//            verifySuccessUpdateShopScheduleCalled(action, closeNow, closeStart, closeEnd, closeNote)
//
//            // val updateShopSchedule: String = "Berhasil memperbarui Status Toko"
//            val expectedValue = Success(String)
//
//            assertTrue(shopSettingsInfoViewModel.updateScheduleResult.value is Success)
//            shopSettingsInfoViewModel.updateScheduleResult
//                    .verifySuccessEquals(expectedValue)
//        }
//    }


//    // UseCase
//    @Test
//    fun `when_get_shop_data_with_provided_shopid_and_includeos_should_return_success`() {
//        runBlocking {
//            val requestParams = RequestParams()
//            val shopBasicData = ShopBasicDataModel()
//            val goldGetPmOsStatus = GoldGetPmOsStatus()
//
//            onGetShopBasicInfo_thenReturn(shopBasicData)
//            onGetShopStatus_thenReturn(goldGetPmOsStatus)
//
//            val getShopBasicInfo = getShopBasicDataUseCase.createObservable(requestParams).test()
//            val getShopStatus = getShopStatusUseCase.createObservable(requestParams).test()
//
//            verifyAllUseCaseCalled()
//
//            getShopBasicInfo.assertNoErrors()
//                    .assertCompleted()
//                    .assertValue(shopBasicData)
//
//            getShopStatus.assertNoErrors()
//                    .assertCompleted()
//                    .assertValue(goldGetPmOsStatus)
//        }
//    }

}