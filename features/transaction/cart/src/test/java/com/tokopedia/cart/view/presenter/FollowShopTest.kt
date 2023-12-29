package com.tokopedia.cart.view.presenter

import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.shop.common.domain.interactor.model.favoriteshop.DataFollowShop
import com.tokopedia.shop.common.domain.interactor.model.favoriteshop.FollowShop
import io.mockk.coEvery
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Test

class FollowShopTest : BaseCartTest() {

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
        cartListPresenter.followShop("1")

        // THEN
        verifyOrder {
            view.hideProgressLoading()
            view.showToastMessageGreen(dataFollowShop.followShop?.message.orEmpty())
        }
    }

    @Test
    fun `WHEN follow shop failed THEN should render error`() {
        // GIVEN
        val exception = ResponseErrorException("Failed")

        coEvery { followShopUseCase(any()) } throws exception
        coEvery { getCartRevampV4UseCase(any()) } returns CartData()

        // WHEN
        cartListPresenter.followShop("1")

        // THEN
        verifyOrder {
            view.hideProgressLoading()
            view.showToastMessageRed(exception)
        }
    }

    @Test
    fun `WHEN follow shop with view is detached THEN should not render view`() {
        // GIVEN
        val dataFollowShop = DataFollowShop().apply {
            followShop = FollowShop().apply {
                isSuccess = true
                message = "Success"
            }
        }

        coEvery { followShopUseCase(any()) } returns dataFollowShop
        coEvery { getCartRevampV4UseCase(any()) } returns CartData()

        cartListPresenter.detachView()

        // WHEN
        cartListPresenter.followShop("1")

        // THEN
        verify(inverse = true) {
            view.showProgressLoading()
        }
    }
}
