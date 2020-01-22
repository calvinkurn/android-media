package com.tokopedia.purchase_platform.features.cart.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.domain.schedulers.TestSchedulers
import com.tokopedia.purchase_platform.features.cart.data.model.request.RemoveCartRequest
import com.tokopedia.purchase_platform.features.cart.data.model.response.deletecart.Data
import com.tokopedia.purchase_platform.features.cart.data.model.response.deletecart.DeleteCartDataResponse
import com.tokopedia.purchase_platform.features.cart.data.model.response.deletecart.DeleteCartGqlResponse
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.DeleteCartData
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.mockk
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.observers.AssertableSubscriber

class DeleteCartUseCaseTest : Spek({

    val clearCacheAutoApplyStackUseCase = mockk<ClearCacheAutoApplyStackUseCase>()
    val graphqlUseCase = mockk<GraphqlUseCase>(relaxed = true)
    val useCase by memoized { DeleteCartUseCase(clearCacheAutoApplyStackUseCase, graphqlUseCase, TestSchedulers) }

    val params = RequestParams().apply {
        putObject(DeleteCartUseCase.PARAM_REMOVE_CART_REQUEST, RemoveCartRequest())
        putObject(DeleteCartUseCase.PARAM_TO_BE_REMOVED_PROMO_CODES, ArrayList<String>())
    }

    Feature("Delete Cart Use Case without promo") {

        lateinit var subscriber: AssertableSubscriber<DeleteCartData>

        Scenario("success") {

            Given("mock response") {
                every { graphqlUseCase.createObservable(any()) } returns Observable.just(GraphqlResponse(mapOf(DeleteCartGqlResponse::class.java to DeleteCartGqlResponse(
                        DeleteCartDataResponse(status = "OK", data = Data(0))
                )), null, false))
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

            Given("mock response") {
                every { graphqlUseCase.createObservable(any()) } returns Observable.just(GraphqlResponse(mapOf(DeleteCartGqlResponse::class.java to DeleteCartGqlResponse(
                        DeleteCartDataResponse(status = "ERROR", errorMessage = arrayListOf(errorMessage, errorMessage2), data = Data(0))
                )), null, false))
            }

            When("create observable") {
                subscriber = useCase.createObservable(params).test()
            }

            Then("value should fail with first error message") {
                subscriber.assertValue(DeleteCartData(message = errorMessage))
            }
        }

        Scenario("failure with no error message") {

            Given("mock response") {
                every { graphqlUseCase.createObservable(any()) } returns Observable.just(GraphqlResponse(mapOf(DeleteCartGqlResponse::class.java to DeleteCartGqlResponse(
                        DeleteCartDataResponse(status = "ERROR", data = Data(0))
                )), null, false))
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