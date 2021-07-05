package com.tokopedia.shop_settings.presenter.shopsettingsetalase

import com.tokopedia.shop.settings.etalase.data.ShopEtalaseUiModel
import com.tokopedia.shop_settings.common.util.LiveDataUtil.observeAwaitValue
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

@ExperimentalCoroutinesApi
class ShopSettingsEtalaseAddEditViewModelTest: ShopSettingsEtalaseTestFixture() {

    @Test
    fun `add etalase should be successful`() {
        runBlocking {
            coEvery {
                addShopEtalaseUseCase.getData(any())
            } returns "success"

            shopSettingsEtalaseAddEditViewModel.saveShopEtalase(ShopEtalaseUiModel())

            coVerify {
                addShopEtalaseUseCase.getData(any())
            }

            val expectedResponse = Success("success")
            val actualResponse = shopSettingsEtalaseAddEditViewModel.saveMessage.observeAwaitValue()

            Assert.assertEquals(expectedResponse, actualResponse)
        }
    }

    @Test
    fun `update etalase should be successful`() {
        runBlocking {
            coEvery {
                updateShopEtalaseUseCase.getData(any())
            } returns "success"

            shopSettingsEtalaseAddEditViewModel.saveShopEtalase(ShopEtalaseUiModel(), true)

            coVerify {
                updateShopEtalaseUseCase.getData(any())
            }

            val expectedResponse = Success("success")
            val actualResponse = shopSettingsEtalaseAddEditViewModel.saveMessage.observeAwaitValue()

            Assert.assertEquals(expectedResponse, actualResponse)
        }
    }

    @Test
    fun `get etalase should be successful`() {
        runBlocking {
            coEvery {
                getShopEtalaseUseCase.executeOnBackground().shopShowcases.result
            } returns arrayListOf()

            shopSettingsEtalaseAddEditViewModel.getShopEtalase()

            coVerify {
                getShopEtalaseUseCase.executeOnBackground()
            }

            val expectedResponse = Success(arrayListOf<ShopEtalaseUiModel>())
            val actualResponse = shopSettingsEtalaseAddEditViewModel.shopEtalase.observeAwaitValue()

            Assert.assertEquals(expectedResponse, actualResponse)
        }
    }

    @Test
    fun `when power merchant is true`() {
        every {
            userSession.isGoldMerchant
        } returns true

        every {
            userSession.isPowerMerchantIdle
        } returns true

        Assert.assertTrue(shopSettingsEtalaseAddEditViewModel.isIdlePowerMerchant())
        Assert.assertTrue(shopSettingsEtalaseAddEditViewModel.isPowerMerchant())
    }
}