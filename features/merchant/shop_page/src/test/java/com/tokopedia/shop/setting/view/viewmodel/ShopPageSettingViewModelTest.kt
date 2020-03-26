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

    @Test
    fun when_get_shop_success_should_response_is_not_null() {
        runBlocking {

            coEvery { getShopInfoUseCase.executeOnBackground() } returns ShopInfo()

            viewModel.getShop(anyString(),
                    anyString(), anyBoolean())

            verify { GQLGetShopInfoUseCase.createParams(anyList(), anyString(), anyList(), anyString()) }

            verifyGqlGetShopInfoUseCaseCaseCalled()

            Assert.assertTrue(viewModel.shopInfoResp.value is Success)
            Assert.assertNotNull(viewModel.shopInfoResp.value)
        }
    }

    @Test
    fun when_get_shop_fail__should_response_is_null() {

        coEvery { getShopInfoUseCase.executeOnBackground() } throws Exception()
        viewModel.getShop(anyString(),
                anyString(), anyBoolean())

        verify { GQLGetShopInfoUseCase.createParams(anyList(), anyString(), anyList(), anyString()) }

        verifyGqlGetShopInfoUseCaseCaseCalled()

        Assert.assertTrue(viewModel.shopInfoResp.value is Fail)
        Assert.assertNotNull(viewModel.shopInfoResp.value)
    }

    private fun verifyGqlGetShopInfoUseCaseCaseCalled() {
        coVerify { getShopInfoUseCase.executeOnBackground() }
    }
}