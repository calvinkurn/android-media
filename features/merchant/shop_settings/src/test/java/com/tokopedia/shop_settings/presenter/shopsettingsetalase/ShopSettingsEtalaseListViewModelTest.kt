package com.tokopedia.shop_settings.presenter.shopsettingsetalase

import com.tokopedia.shop.settings.etalase.data.ShopEtalaseUiModel
import com.tokopedia.shop_settings.common.util.LiveDataUtil.observeAwaitValue
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class ShopSettingsEtalaseListViewModelTest: ShopSettingsEtalaseTestFixture() {

    @Test
    fun `get shop etalase should be successful`() {
        runBlocking {
            coEvery {
                getShopEtalaseUseCase.executeOnBackground().shopShowcases.result
            } returns arrayListOf()

            shopSettingsEtalaseListViewModel.getShopEtalase()

            coVerify {
                getShopEtalaseUseCase.executeOnBackground()
            }

            val expectedResponse = Success(arrayListOf<ShopEtalaseUiModel>())
            val actualResponse = shopSettingsEtalaseListViewModel.shopEtalase.observeAwaitValue()

            assertEquals(expectedResponse, actualResponse)
        }
    }


    @Test
    fun `delete shop etalase should be successful`() {
        runBlocking {
            coEvery {
                deleteShopEtalaseUseCase.getData(any())
            } returns "success"

            shopSettingsEtalaseListViewModel.deleteShopEtalase("123")

            coVerify {
                deleteShopEtalaseUseCase.getData(any())
            }

            val expectedResponse = Success("success")
            val actualResponse = shopSettingsEtalaseListViewModel.deleteMessage.observeAwaitValue()

            assertEquals(expectedResponse, actualResponse)
        }
    }
}