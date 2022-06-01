package com.tokopedia.shop_settings.viewmodel.shopsettingsinfo

import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHour
import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHoursListResponse
import com.tokopedia.shop.settings.basicinfo.data.SetShopOperationalHoursResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import java.lang.Exception

@ExperimentalCoroutinesApi
class ShopSetOperationalHoursViewModelTest : ShopSetOperationalHoursViewModelTestFixture() {

    @Test
    fun `getShopOperationalHoursList should return Success`() {
        coEvery {
            gqlGetShopOperationalHoursListUseCase.executeOnBackground()
        } returns ShopOperationalHoursListResponse()

        shopSetOperationalHoursViewModel.getOperationalHoursList("123")

        coVerify { gqlGetShopOperationalHoursListUseCase.executeOnBackground() }
        assertTrue(shopSetOperationalHoursViewModel.shopOperationalHoursListData.value is Success)
    }

    @Test
    fun `getShopOperationalHoursList should return Fail`() {
        coEvery {
            gqlGetShopOperationalHoursListUseCase.executeOnBackground()
        } throws Exception()

        shopSetOperationalHoursViewModel.getOperationalHoursList("123")

        coVerify { gqlGetShopOperationalHoursListUseCase.executeOnBackground() }
        assertTrue(shopSetOperationalHoursViewModel.shopOperationalHoursListData.value is Fail)
    }

    @Test
    fun `setShopOperationalHours should return Success`() {
        coEvery {
            setShopOperationalHoursUseCase.executeOnBackground()
        } returns SetShopOperationalHoursResponse()

        shopSetOperationalHoursViewModel.updateOperationalHoursList("123", listOf(ShopOperationalHour()))

        coVerify { setShopOperationalHoursUseCase.executeOnBackground() }
        assertTrue(shopSetOperationalHoursViewModel.setShopOperationalHoursData.value is Success)
    }

    @Test
    fun `shopSetOperationalHours should return Fail`() {
        coEvery {
            setShopOperationalHoursUseCase.executeOnBackground()
        } throws Exception()

        shopSetOperationalHoursViewModel.updateOperationalHoursList("123", listOf(ShopOperationalHour()))

        coVerify { setShopOperationalHoursUseCase.executeOnBackground() }
        assertTrue(shopSetOperationalHoursViewModel.setShopOperationalHoursData.value is Fail)
    }
}