package com.tokopedia.cart.domain.usecase

import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.cart.data.model.request.RemoveCartRequest
import com.tokopedia.cart.data.model.response.deletecart.Data
import com.tokopedia.cart.data.model.response.deletecart.DeleteCartDataResponse
import com.tokopedia.cart.data.model.response.deletecart.DeleteCartGqlResponse
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.cart.domain.model.cartlist.DeleteCartData
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.mockk
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.observers.AssertableSubscriber
import java.lang.reflect.Type

object DeleteCartUseCaseTest : Spek({

    val graphqlUseCase = mockk<GraphqlUseCase>(relaxed = true)
    val updateCartCounterUseCase = mockk<UpdateCartCounterUseCase>()
    val useCase by memoized { DeleteCartUseCase(updateCartCounterUseCase, graphqlUseCase, TestSchedulers) }

    val params = RequestParams().apply {
        putObject(DeleteCartUseCase.PARAM_REMOVE_CART_REQUEST, RemoveCartRequest())
    }

    Feature("Delete Cart Use Case without promo") {

        lateinit var subscriber: AssertableSubscriber<DeleteCartData>

        Scenario("success") {

            val result = HashMap<Type, Any>()
            result[DeleteCartGqlResponse::class.java] = DeleteCartGqlResponse(
                    DeleteCartDataResponse(status = "OK", data = Data(0))
            )
            val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

            Given("mock response") {
                every { graphqlUseCase.createObservable(any()) } returns Observable.just(gqlResponse)
                every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(0)
            }

            When("create observable") {
                subscriber = useCase.createObservable(params).test()
            }

            Then("value should success with empty message") {
                subscriber.assertValue(DeleteCartData(true, ""))
            }
        }

        Scenario("failure with error messages") {

            val errorMessage = "something went wrong"
            val errorMessage2 = "something went wrong2"

            val result = HashMap<Type, Any>()
            result[DeleteCartGqlResponse::class.java] = DeleteCartGqlResponse(
                    DeleteCartDataResponse(status = "ERROR", errorMessage = arrayListOf(errorMessage, errorMessage2), data = Data(0))
            )
            val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

            Given("mock response") {
                every { graphqlUseCase.createObservable(any()) } returns Observable.just(gqlResponse)
                every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(0)
            }

            When("create observable") {
                subscriber = useCase.createObservable(params).test()
            }

            Then("value should fail with first error message") {
                subscriber.assertValue(DeleteCartData(message = errorMessage))
            }
        }

        Scenario("failure with no error message") {

            val result = HashMap<Type, Any>()
            result[DeleteCartGqlResponse::class.java] = DeleteCartGqlResponse(
                    DeleteCartDataResponse(status = "ERROR", data = Data(0))
            )
            val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

            Given("mock response") {
                every { graphqlUseCase.createObservable(any()) } returns Observable.just(gqlResponse)
                every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(0)
            }

            When("create observable") {
                subscriber = useCase.createObservable(params).test()
            }

            Then("value should fail with empty message") {
                subscriber.assertValue(DeleteCartData(message = ""))
            }
        }
    }
})