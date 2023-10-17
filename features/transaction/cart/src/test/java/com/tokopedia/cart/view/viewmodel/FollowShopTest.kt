package com.tokopedia.cart.view.viewmodel

import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.cartrevamp.view.uimodel.FollowShopEvent
import com.tokopedia.shop.common.domain.interactor.model.favoriteshop.DataFollowShop
import com.tokopedia.shop.common.domain.interactor.model.favoriteshop.FollowShop
import io.mockk.coEvery
import org.junit.Assert.assertEquals
import org.junit.Test

class FollowShopTest : BaseCartViewModelTest() {

    @Test
    fun `WHEN follow shop success THEN should render success`() {
        // GIVEN
        val dataFollowShop = DataFollowShop().apply {
            followShop = FollowShop().apply {
                isSuccess = true
                message = "Success"
            }
        }

        coEvery { followShopUseCase(any()) } returns dataFollowShop
        coEvery { getCartRevampV4UseCase(any()) } returns CartData()

        // WHEN
        cartViewModel.followShop("1")

        // THEN
        assertEquals(FollowShopEvent.Success(dataFollowShop), cartViewModel.followShopEvent.value)
    }

    @Test
    fun `WHEN follow shop failed THEN should render error`() {
        // GIVEN
        val exception = ResponseErrorException("Failed")

        coEvery { followShopUseCase(any()) } throws exception
        coEvery { getCartRevampV4UseCase(any()) } returns CartData()

        // WHEN
        cartViewModel.followShop("1")

        // THEN
        assertEquals(FollowShopEvent.Failed(exception), cartViewModel.followShopEvent.value)
    }
}
