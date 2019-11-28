package com.tokopedia.purchase_platform.features.cart.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.purchase_platform.common.domain.schedulers.TestSchedulers
import com.tokopedia.purchase_platform.features.cart.data.model.response.ShopGroupSimplifiedGqlResponse
import com.tokopedia.purchase_platform.features.cart.data.model.response.ShopGroupSimplifiedResponse
import com.tokopedia.purchase_platform.features.cart.domain.mapper.CartMapperV3
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData
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
        GetCartListSimplifiedUseCase("query", graphqlUseCase, cartMapperV3, TestSchedulers)
    }

    every { cartMapperV3.convertToCartItemDataList(any()) } returns CartListData()

    Feature("GetCartListSimplifiedUseCase") {

        lateinit var subscriber: AssertableSubscriber<CartListData>
        lateinit var onErrorEvents: List<Throwable>

        Scenario("success") {

            Given("success response") {
                every { graphqlUseCase.createObservable(any()) } returns
                        Observable.just(GraphqlResponse(mapOf(ShopGroupSimplifiedGqlResponse::class.java to ShopGroupSimplifiedGqlResponse(ShopGroupSimplifiedResponse("OK"))), null, false))
            }

            When("create observable") {
                subscriber = usecase.createObservable().test()
            }

            Then("should has 1 value") {
                subscriber.assertValueCount(1)
            }

            Then("value should be empty CartListData") {
                subscriber.assertValue(CartListData())
            }

            Then("should complete") {
                subscriber.assertCompleted()
            }
        }

        Scenario("fail") {

            val errorMessages = listOf("error message", "other message")

            Given("error response with message") {
                every { graphqlUseCase.createObservable(any()) } returns
                        Observable.just(GraphqlResponse(mapOf(ShopGroupSimplifiedGqlResponse::class.java to ShopGroupSimplifiedGqlResponse(ShopGroupSimplifiedResponse("FAIL", errorMessages = errorMessages))), null, false))
            }

            When("create observable") {
                subscriber = usecase.createObservable().test()
            }

            Then("should has 1 error") {
                onErrorEvents = subscriber.onErrorEvents
                assertEquals(1, onErrorEvents.size)
            }

            Then("should contains custom error message") {
                assertEquals(errorMessages.joinToString(), (onErrorEvents.first() as ResponseErrorException).message)
            }
        }

        Scenario("unexpected fail") {

            Given("error response without message") {
                every { graphqlUseCase.createObservable(any()) } returns
                        Observable.just(GraphqlResponse(mapOf(ShopGroupSimplifiedGqlResponse::class.java to ShopGroupSimplifiedGqlResponse(ShopGroupSimplifiedResponse("FAIL"))), null, false))
            }

            When("create observable") {
                subscriber = usecase.createObservable().test()
            }

            Then("should has 1 error") {
                onErrorEvents = subscriber.onErrorEvents
                assertEquals(1, onErrorEvents.size)
            }

            Then("should contains custom error message") {
                assertEquals("Terjadi kesalahan, ulangi beberapa saat lagi", (onErrorEvents.first() as ResponseErrorException).message)
            }
        }
    }
})