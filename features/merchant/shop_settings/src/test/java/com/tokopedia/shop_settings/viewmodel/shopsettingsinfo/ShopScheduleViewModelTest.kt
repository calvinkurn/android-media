package com.tokopedia.shop_settings.viewmodel.shopsettingsinfo

import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop_settings.common.util.LiveDataUtil.observeAwaitValue
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import io.mockk.verify
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test

@ExperimentalCoroutinesApi
class ShopScheduleViewModelTest: ShopSettingsInfoViewModelTestFixture() {

    @Test
    fun `when get shop basic data should return success`() {
        runBlocking {
            val result = Success(ShopBasicDataModel())

            every {
                getShopBasicDataUseCase.getData(any())
            } returns ShopBasicDataModel()

            shopScheduleViewModel.getShopBasicData()

            val getShopBasicData = shopScheduleViewModel.shopBasicData.observeAwaitValue()
            assertTrue(getShopBasicData is Success)
            assertTrue(shopScheduleViewModel.shopBasicData.value == result)
        }
    }

    @Test
    fun `when update shop schedule should return success`() {
        runBlocking {
            val result = Success("success")

            every {
                updateShopScheduleUseCase.getData(any())
            } returns "success"


            shopScheduleViewModel.updateShopSchedule(3, true, "", "", "")

            val updateShopSchedule = shopScheduleViewModel.message.observeAwaitValue()
            assertTrue(updateShopSchedule is Success)
            assertTrue(shopScheduleViewModel.message.value == result)
        }
    }
}