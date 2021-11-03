package com.tokopedia.cart.view.presenter

import com.google.gson.Gson
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import io.mockk.*
import org.junit.Test
import rx.Observable

class GetCartListTest : BaseCartTest() {

    @Test
    fun `WHEN initial load cart list success THEN should render success`() {
        // GIVEN
        val cartData = CartData()

        coEvery { getCartRevampV3UseCase.setParams(any(), any()) } just Runs
        coEvery { getCartRevampV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartData) -> Unit>().invoke(cartData)
        }

        every { updateCartCounterUseCase.createObservable(any()) } answers { Observable.just(1) }

        // WHEN
        cartListPresenter?.processInitialGetCartData("", true, false)

        // THEN
        verifyOrder {
            view.renderLoadGetCartDataFinish()
            view.renderInitialGetCartListDataSuccess(cartData)
            view.stopCartPerformanceTrace()
        }
    }

    @Test
    fun `WHEN initial load cart list failed THEN should render error`() {
        // GIVEN
        val exception = ResponseErrorException("Terjadi kesalahan pada server. Ulangi beberapa saat lagi")

        val recommendationWidgetStringData = """
                {
                    "recommendationItemList": []
                }
            """.trimIndent()

        val response = mutableListOf<RecommendationWidget>().apply {
            val recommendationWidget = Gson().fromJson(recommendationWidgetStringData, RecommendationWidget::class.java)
            add(recommendationWidget)
        }

        coEvery { getCartRevampV3UseCase.setParams(any(), any()) } just Runs
        coEvery { getCartRevampV3UseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(exception)
        }
        every { getRecentViewUseCase.createObservable(any()) } answers { Observable.just(response) }

        every { updateCartCounterUseCase.createObservable(any()) } answers { Observable.just(1) }

        // WHEN
        cartListPresenter?.processInitialGetCartData("", true, true)

        // THEN
        verify {
            view.renderErrorInitialGetCartListData(exception)
            view.stopCartPerformanceTrace()
        }
    }

    @Test
    fun `WHEN reload cart list success THEN should render success`() {
        // GIVEN
        val cartData = CartData()

        coEvery { getCartRevampV3UseCase.setParams(any(), any()) } just Runs
        coEvery { getCartRevampV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartData) -> Unit>().invoke(cartData)
        }

        every { updateCartCounterUseCase.createObservable(any()) } answers { Observable.just(1) }

        // WHEN
        cartListPresenter?.processInitialGetCartData("", false, false)

        // THEN
        verify {
            view.renderLoadGetCartDataFinish()
            view.renderInitialGetCartListDataSuccess(cartData)
            view.stopCartPerformanceTrace()
        }
    }

    @Test
    fun `WHEN reload cart list failed THEN should render error`() {
        // GIVEN
        val exception = ResponseErrorException("Terjadi kesalahan pada server. Ulangi beberapa saat lagi")

        val recommendationWidgetStringData = """
                {
                    "recommendationItemList": []
                }
            """.trimIndent()

        val response = mutableListOf<RecommendationWidget>().apply {
            val recommendationWidget = Gson().fromJson(recommendationWidgetStringData, RecommendationWidget::class.java)
            add(recommendationWidget)
        }

        coEvery { getCartRevampV3UseCase.setParams(any(), any()) } just Runs
        coEvery { getCartRevampV3UseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(exception)
        }
        every { getRecentViewUseCase.createObservable(any()) } answers { Observable.just(response) }

        every { updateCartCounterUseCase.createObservable(any()) } answers { Observable.just(1) }

        // WHEN
        cartListPresenter?.processInitialGetCartData("", false, true)

        // THEN
        verify {
            view.renderErrorInitialGetCartListData(exception)
            view.stopCartPerformanceTrace()
        }
    }
}