package com.tokopedia.purchase_platform.features.cart.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.purchase_platform.features.cart.data.model.response.ShopGroupSimplifiedGqlResponse
import com.tokopedia.purchase_platform.features.cart.data.model.response.ShopGroupSimplifiedResponse
import com.tokopedia.purchase_platform.features.cart.domain.mapper.CartMapperV3
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.observers.AssertableSubscriber

class GetCartListSimplifiedUseCaseTest : Spek({

    val graphqlUseCase = mockk<GraphqlUseCase>(relaxed = true)
    val cartMapperV3 = mockk<CartMapperV3>()
    val usecase by memoized {
        GetCartListSimplifiedUseCase("query", graphqlUseCase, cartMapperV3)
    }

    every { cartMapperV3.convertToCartItemDataList(any()) } returns CartListData()

    Feature("GetCartListSimplifiedUseCase") {

        Scenario("success") {

            lateinit var subscriber: AssertableSubscriber<CartListData>

            Given("success response") {
                every { graphqlUseCase.createObservable(any()) } returns
                        Observable.just(GraphqlResponse(mapOf(ShopGroupSimplifiedGqlResponse::class.java to ShopGroupSimplifiedGqlResponse(ShopGroupSimplifiedResponse("OK"))), null, false))
            }

            When("create observable") {
                subscriber = usecase.createObservable(RequestParams.EMPTY).test()
            }

            Then("should complete with 1 CartListData") {
                subscriber.assertValueCount(1).assertValue(CartListData()).assertCompleted()
            }
        }

        Scenario("fail") {

            lateinit var subscriber: AssertableSubscriber<CartListData>

            Given("error response with message") {
                every { graphqlUseCase.createObservable(any()) } returns
                        Observable.just(GraphqlResponse(mapOf(ShopGroupSimplifiedGqlResponse::class.java to ShopGroupSimplifiedGqlResponse(ShopGroupSimplifiedResponse("FAIL", errorMessages = arrayListOf("error message", "other message")))), null, false))
            }

            When("create observable") {
                subscriber = usecase.createObservable(RequestParams.EMPTY).test()
            }

            Then("should error with 1 custom message") {
                val onErrorEvents = subscriber.onErrorEvents
                assertEquals(1, onErrorEvents.size)
                assertEquals("error message, other message", (onErrorEvents.first() as ResponseErrorException).message)
            }
        }

        Scenario("unexpected fail") {

            lateinit var subscriber: AssertableSubscriber<CartListData>

            Given("error response without message") {
                every { graphqlUseCase.createObservable(any()) } returns
                        Observable.just(GraphqlResponse(mapOf(ShopGroupSimplifiedGqlResponse::class.java to ShopGroupSimplifiedGqlResponse(ShopGroupSimplifiedResponse("FAIL"))), null, false))
            }

            When("create observable") {
                subscriber = usecase.createObservable(RequestParams.EMPTY).test()
            }

            Then("should error with 1 default message") {
                val onErrorEvents = subscriber.onErrorEvents
                assertEquals(1, onErrorEvents.size)
                assertEquals("Terjadi kesalahan, ulangi beberapa saat lagi", (onErrorEvents.first() as ResponseErrorException).message)
            }
        }
    }
})