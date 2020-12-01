package com.tokopedia.logisticcart.shipping.usecase

import android.content.Context
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticcart.domain.executor.TestSceduler
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationConverter
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.RatesApiGqlResponse
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.RatesData
import com.tokopedia.network.exception.MessageErrorException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.Assert.assertEquals
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.observers.TestSubscriber
import java.lang.reflect.Type

object GetRatesApiUseCaseTest : Spek({
    val context = mockk<Context>(relaxed = true)
    val converter = mockk<ShippingDurationConverter>(relaxed = true)
    val gql = mockk<GraphqlUseCase>(relaxed = true)
    val scheduler = TestSceduler()

    Feature("Basic gql") {

        val useCase by memoized { GetRatesApiUseCase(context, converter, gql, scheduler) }
        val param by memoized { RatesParam("", "", "", "", "") }

        Scenario("execute") {

            When("execute") {
                useCase.execute(param)
            }

            Then("gql should execute in order") {
                verifySequence {
                    gql.clearRequest()
                    gql.addRequest(any())
                    gql.getExecuteObservable(any())
                }
            }

        }

        Scenario("unsubscribe") {
            When("unsubscribe") {
                useCase.unsubscribe()
            }

            Then("gql should call unsubscribe") {
                verify {
                    gql.unsubscribe()
                }
            }
        }

    }

    Feature("Get Rates Api") {
        val useCase by memoized { GetRatesApiUseCase(context, converter, gql, scheduler) }
        val param by memoized { RatesParam("", "", "", "", "") }
        val tSubscriber by memoized { TestSubscriber<ShippingRecommendationData>() }

        Scenario("success response") {
            val success = RatesApiGqlResponse(RatesData())
            val mockViewModel = ShippingRecommendationData().apply { blackboxInfo = "test info" }

            val result = HashMap<Type, Any>()
            result[RatesApiGqlResponse::class.java] = success
            val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

            Given("gql return success data") {
                every { gql.getExecuteObservable(any()) } answers {
                    Observable.just(gqlResponse)
                }
                every { converter.convertModel(any()) } answers { mockViewModel }
            }

            When("execute") {
                useCase.execute(param).subscribe(tSubscriber)
            }

            Then("converter should be called") {
                verify { converter.convertModel(any()) }
            }

            Then("complete with no error") {
                tSubscriber.assertNoErrors()
                tSubscriber.assertCompleted()
            }

            Then("emit correct instance") {
                assertEquals(mockViewModel, tSubscriber.onNextEvents[0])
            }
        }

        Scenario("error response") {

            val errorMsg = "Error Graphql"
            val errorGql = GraphqlError().apply { message = errorMsg }

            val errors = HashMap<Type, List<GraphqlError>>()
            errors[RatesApiGqlResponse::class.java] = listOf(errorGql)
            val gqlResponse = GraphqlResponse(HashMap<Type, Any?>(), errors, false)

            Given("gql return error") {
                every { gql.getExecuteObservable(any()) } answers {
                    Observable.just(gqlResponse)
                }
            }

            When("execute") {
                useCase.execute(param).subscribe(tSubscriber)
            }

            Then("error with message error exception occured") {
                tSubscriber.assertError(MessageErrorException::class.java)
            }
        }

    }
})