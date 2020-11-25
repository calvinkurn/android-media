package com.tokopedia.cart.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.cart.data.model.request.UpdateCartRequest
import com.tokopedia.cart.data.model.response.updatecart.Data
import com.tokopedia.cart.data.model.response.updatecart.UpdateCartDataResponse
import com.tokopedia.cart.data.model.response.updatecart.UpdateCartGqlResponse
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.cart.domain.model.updatecart.UpdateCartData
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.mockk
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.observers.AssertableSubscriber
import java.lang.reflect.Type

object UpdateCartUseCaseTest : Spek({

    val graphqlUseCase = mockk<GraphqlUseCase>(relaxed = true)
    val useCase by memoized { UpdateCartUseCase(graphqlUseCase, TestSchedulers) }

    val params = RequestParams().apply {
        putObject(UpdateCartUseCase.PARAM_UPDATE_CART_REQUEST, arrayListOf(UpdateCartRequest()))
    }

    Feature("Update Cart Use Case") {

        lateinit var subscriber: AssertableSubscriber<UpdateCartData>

        Scenario("success") {

            val response = UpdateCartDataResponse(data = Data(status = true), status = "OK")

            val result = HashMap<Type, Any>()
            result[UpdateCartGqlResponse::class.java] = UpdateCartGqlResponse(response)
            val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

            Given("mock response") {
                every { graphqlUseCase.createObservable(any()) } returns Observable.just(gqlResponse)
            }

            When("create observable") {
                subscriber = useCase.createObservable(params).test()
            }

            Then("value should success and has empty message") {
                subscriber.assertValue(UpdateCartData(isSuccess = true, message = ""))
            }
        }

        Scenario("failure with error messages") {

            val errorMessage = "Something went wrong"

            val result = HashMap<Type, Any>()
            result[UpdateCartGqlResponse::class.java] = UpdateCartGqlResponse(
                    UpdateCartDataResponse(data = Data(status = false, error = errorMessage), status = "ERROR"))
            val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

            Given("mock response") {
                every { graphqlUseCase.createObservable(any()) } returns Observable.just(gqlResponse)
            }

            When("create observable") {
                subscriber = useCase.createObservable(params).test()
            }

            Then("value should fail and has first error message") {
                subscriber.assertValue(UpdateCartData(isSuccess = false, message = errorMessage))
            }
        }

        Scenario("failure with no error message") {

            val result = HashMap<Type, Any>()
            result[UpdateCartGqlResponse::class.java] = UpdateCartGqlResponse(
                    UpdateCartDataResponse(data = Data(status = false), status = "ERROR"))
            val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

            Given("mock response") {
                every { graphqlUseCase.createObservable(any()) } returns Observable.just(gqlResponse)
            }

            When("create observable") {
                subscriber = useCase.createObservable(params).test()
            }

            Then("value should fail and has empty message") {
                subscriber.assertValue(UpdateCartData(isSuccess = false, message = ""))
            }
        }
    }
})