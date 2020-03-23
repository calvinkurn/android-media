package com.tokopedia.shop.newproduct.view.viewmodel

import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.ArgumentMatchers.*

@ExperimentalCoroutinesApi
class ShopPageProductListResultViewModelTest : ShopPageProductListViewModelTestFixture() {

    @Test
    fun `check whether response get shop info success is not null`() {
        runBlocking {
            coEvery { getShopInfoUseCase.executeOnBackground() } returns ShopInfo()
            viewModelShopPageProductListResultViewModel.getShop(anyString(), anyString(), anyBoolean())
            verifyGetShopInfoUseCaseCalled()
            assertTrue(viewModelShopPageProductListResultViewModel.shopInfoResp.value is Success<ShopInfo>)
            assertNotNull(viewModelShopPageProductListResultViewModel.shopInfoResp.value)
        }
    }

    @Test
    fun `check whether response get shop info error`() {
        runBlocking {
            coEvery { getShopInfoUseCase.executeOnBackground() } throws Exception()

            viewModelShopPageProductListResultViewModel.getShop(anyString(), anyString(), anyBoolean())

            verify { GQLGetShopInfoUseCase.createParams(anyList(), anyString())  }

            verifyGetShopInfoUseCaseCalled()
            assertTrue(viewModelShopPageProductListResultViewModel.shopInfoResp.value is Fail)
        }
    }

    @Test
    fun `check whether response get shop product success is not null`() {
        runBlocking {
            coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct()
            viewModelShopPageProductListResultViewModel.getShopProduct(
                    anyString(), anyInt(), anyInt(), anyInt(), anyString(), anyString(), anyBoolean()
            )
            verifyGetShopProductUseCaseCalled()
            assertTrue(viewModelShopPageProductListResultViewModel.productResponse.value is Success)
            assertNotNull(viewModelShopPageProductListResultViewModel.productResponse.value)
        }
    }

    @Test
    fun `check whether response get shop product error`() {
        runBlocking {
            coEvery { getShopProductUseCase.executeOnBackground() } throws Exception()
            coEvery { getShopProductUseCase.executeOnBackground().errors.isNotEmpty() }

            viewModelShopPageProductListResultViewModel.getShopProduct(
                    anyString()
            )

            verifyGetShopProductUseCaseCalled()
            assertTrue(viewModelShopPageProductListResultViewModel.productResponse.value is Fail)
        }
    }


    private fun verifyGetShopInfoUseCaseCalled() {
        coVerify { getShopInfoUseCase.executeOnBackground() }
    }

    private fun verifyGetShopProductUseCaseCalled() {
        coVerify { getShopProductUseCase.executeOnBackground() }
    }
}