package com.tokopedia.shop.setting.view.viewmodel

import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.mockito.ArgumentMatchers.*

@ExperimentalCoroutinesApi
class ShopPageSettingViewModelTest : ShopPageSettingViewModelTestFixture() {

    private val DEFAULT_SHOP_FIELDS_MOCK = listOf("core", "favorite", "assets", "shipment",
            "last_active", "location", "terms", "allow_manage",
            "is_owner", "other-goldos", "status", "is_open", "closed_info", "create_info")

    @Test
    fun when_get_shop_success_should_response_is_not_null() {

        runBlocking {

            coEvery { getShopInfoUseCase.executeOnBackground() } returns ShopInfo()

            viewModel.getShop(anyString(),
                    anyString(), false)

            verify { GQLGetShopInfoUseCase.createParams(anyList(), anyString(), DEFAULT_SHOP_FIELDS_MOCK) }

            coVerify { gqlRepository.getReseponse(mutableListOf(getShopInfoUseCase.request)) }

            coVerify { getShopInfoUseCase.executeOnBackground() }


            Assert.assertTrue(viewModel.shopInfoResp.value is Success)
            Assert.assertNotNull(viewModel.shopInfoResp.value)
        }
    }

    @Test
    fun when_get_shop_fail__should_response_is_null() {
        runBlocking {
            coEvery { getShopInfoUseCase.executeOnBackground() } throws Exception()
            viewModel.getShop(anyString(),
                    anyString(), false)

            verify { GQLGetShopInfoUseCase.createParams(anyList(), anyString(), DEFAULT_SHOP_FIELDS_MOCK) }

            coVerify { gqlRepository.getReseponse(mutableListOf(getShopInfoUseCase.request)) }

            coVerify { getShopInfoUseCase.executeOnBackground() }

            Assert.assertTrue(viewModel.shopInfoResp.value is Fail)
            Assert.assertNotNull(viewModel.shopInfoResp.value)
        }
    }

}