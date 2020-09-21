package com.tokopedia.cart.domain.usecase

import com.tokopedia.cart.data.model.response.shopgroupsimplified.ShopGroupSimplifiedGqlResponse
import com.tokopedia.cart.data.model.response.shopgroupsimplified.ShopGroupSimplifiedResponse
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.cart.domain.mapper.CartSimplifiedMapper
import com.tokopedia.cart.domain.model.cartlist.CartListData
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.observers.AssertableSubscriber
import java.lang.reflect.Type

object GetCartListSimplifiedUseCaseTest : Spek({

    val graphqlUseCase = mockk<GraphqlUseCase>(relaxed = true)
    val cartMapperV3 = mockk<CartSimplifiedMapper>()
    val usecase by memoized {
        GetCartListSimplifiedUseCase(graphqlUseCase, cartMapperV3, TestSchedulers)
    }

    every { cartMapperV3.convertToCartItemDataList(any()) } returns CartListData()

    Feature("GetCartListSimplifiedUseCase") {

        lateinit var subscriber: AssertableSubscriber<CartListData>
        lateinit var onErrorEvents: List<Throwable>

        Scenario("success") {

            val result = HashMap<Type, Any>()
            result[ShopGroupSimplifiedGqlResponse::class.java] = ShopGroupSimplifiedGqlResponse(ShopGroupSimplifiedResponse("OK"))
            val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

            Given("success response") {
                every { graphqlUseCase.createObservable(any()) } returns
                        Observable.just(gqlResponse)
            }

            When("create observable") {
                subscriber = usecase.createObservable(null).test()
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

            val result = HashMap<Type, Any>()
            result[ShopGroupSimplifiedGqlResponse::class.java] = ShopGroupSimplifiedGqlResponse(ShopGroupSimplifiedResponse("FAIL", errorMessages = errorMessages))
            val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

            Given("error response with message") {
                every { graphqlUseCase.createObservable(any()) } returns
                        Observable.just(gqlResponse)
            }

            When("create observable") {
                subscriber = usecase.createObservable(null).test()
            }

            Then("should has 1 error") {
                onErrorEvents = subscriber.onErrorEvents
                assertEquals(1, onErrorEvents.size)
            }

            Then("should contains custom error message") {
                assertEquals(errorMessages.joinToString(), (onErrorEvents.first() as CartResponseErrorException).message)
            }
        }

        Scenario("unexpected fail") {

            val result = HashMap<Type, Any>()
            result[ShopGroupSimplifiedGqlResponse::class.java] = ShopGroupSimplifiedGqlResponse(ShopGroupSimplifiedResponse("FAIL"))
            val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

            Given("error response without message") {
                every { graphqlUseCase.createObservable(any()) } returns
                        Observable.just(gqlResponse)
            }

            When("create observable") {
                subscriber = usecase.createObservable(null).test()
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