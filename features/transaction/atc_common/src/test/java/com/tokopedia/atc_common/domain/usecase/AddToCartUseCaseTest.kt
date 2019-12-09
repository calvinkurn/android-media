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
import io.mockk.verifySequence
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
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
        lateinit var requestParam: RequestParams
        val REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST = "REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST";

        Scenario("use case run successfully") {

            Given("given api response") {
                every { graphqlUseCase.createObservable(any()) } returns
                        Observable.just(GraphqlResponse(mapOf(AddToCartGqlResponse::class.java to AddToCartGqlResponse(
                                AddToCartResponse())), null, false))
            }

            When("create observable") {
                requestParam = RequestParams.create()
                requestParam.putObject(REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, AddToCartRequestParams())
                subscriber = addToCartUseCase.createObservable(requestParam).test()
            }

            Then("request param is not empty") {
                assertTrue(requestParam.parameters.isNotEmpty())
            }

            Then("request param has field $REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST") {
                assertNotNull(requestParam.getObject(REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST))
            }

            Then("should run sequence task") {
                verifySequence {
                    graphqlUseCase.clearRequest()
                    graphqlUseCase.addRequest(any())
                    graphqlUseCase.createObservable(any())
                }
            }

            Then("should not give error") {
                subscriber.assertNoErrors()
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