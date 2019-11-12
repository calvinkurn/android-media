package com.tokopedia.atc_common.domain.usecase

import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.data.model.response.AddToCartGqlResponse
import com.tokopedia.atc_common.data.model.response.AddToCartResponse
import com.tokopedia.atc_common.domain.mapper.AddToCartDataMapper
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.observers.AssertableSubscriber

/**
 * Created by Irfan Khoirul on 2019-11-12.
 */

class AddToCartUseCaseTest : Spek({
    val graphqlUseCase = mockk<GraphqlUseCase>(relaxed = true)
    val addToCartdataMapper = mockk<AddToCartDataMapper>()
    val addToCartUseCase by memoized {
        AddToCartUseCase("mock_query", graphqlUseCase, addToCartdataMapper)
    }

    every { addToCartdataMapper.mapAddToCartResponse(any()) } returns AddToCartDataModel()

    Feature("AddToCartUseCase") {
        lateinit var subscriber: AssertableSubscriber<AddToCartDataModel>
        lateinit var onErrorEvents: List<Throwable>
        lateinit var requestParam: RequestParams

        Scenario("success") {

            Given("success response") {
                every { graphqlUseCase.createObservable(any()) } returns
                        Observable.just(GraphqlResponse(mapOf(AddToCartGqlResponse::class.java to AddToCartGqlResponse(AddToCartResponse())), null, false))
            }

            When("create observable") {
                requestParam = RequestParams.create()
                requestParam.putObject("REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST", AddToCartRequestParams())
                subscriber = addToCartUseCase.createObservable(requestParam).test()
            }

            Then("should has 1 value") {
                subscriber.assertValueCount(1)
            }

            Then("value should be empty AddToCartDataModel") {
                subscriber.assertValue(AddToCartDataModel())
            }

            Then("should complete") {
                subscriber.assertCompleted()
            }
        }
    }
})