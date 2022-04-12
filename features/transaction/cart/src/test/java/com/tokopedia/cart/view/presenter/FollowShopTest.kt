package com.tokopedia.cart.view.presenter

import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.shop.common.domain.interactor.model.favoriteshop.DataFollowShop
import com.tokopedia.shop.common.domain.interactor.model.favoriteshop.FollowShop
import com.tokopedia.usecase.RequestParams
import io.mockk.*
import org.junit.Test
import rx.Observable

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

        every { followShopUseCase.buildRequestParams(any()) } returns RequestParams.create()
        every { followShopUseCase.createObservable(any()) } returns Observable.just(dataFollowShop)
        coEvery { getCartRevampV3UseCase.setParams(any(), any()) } just Runs
        coEvery { getCartRevampV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartData) -> Unit>().invoke(CartData())
        }

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

        every { followShopUseCase.buildRequestParams(any()) } returns RequestParams.create()
        every { followShopUseCase.createObservable(any()) } returns Observable.error(exception)
        coEvery { getCartRevampV3UseCase.setParams(any(), any()) } just Runs
        coEvery { getCartRevampV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartData) -> Unit>().invoke(CartData())
        }

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

        every { followShopUseCase.buildRequestParams(any()) } returns RequestParams.create()
        every { followShopUseCase.createObservable(any()) } returns Observable.just(dataFollowShop)
        coEvery { getCartRevampV3UseCase.setParams(any(), any()) } just Runs
        coEvery { getCartRevampV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartData) -> Unit>().invoke(CartData())
        }

        cartListPresenter.detachView()

        // WHEN
        cartListPresenter.followShop("1")

        // THEN
        verify(inverse = true) {
            view.showProgressLoading()
        }
    }

}