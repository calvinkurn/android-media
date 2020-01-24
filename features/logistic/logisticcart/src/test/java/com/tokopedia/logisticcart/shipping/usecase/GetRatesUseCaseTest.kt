package com.tokopedia.logisticcart.shipping.usecase

import android.content.Context
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticcart.domain.executor.TestSceduler
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationConverter
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.GetRatesCourierRecommendationData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.RatesData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.observers.TestSubscriber

object GetRatesUseCaseTest : Spek({

    val context = mockk<Context>(relaxed = true)
    val converter = mockk<ShippingDurationConverter>(relaxed = true)
    val gql = mockk<GraphqlUseCase>(relaxed = true)
    val scheduler = TestSceduler()

    Feature("Basic gql") {
        val useCase by memoized { GetRatesUseCase(context, converter, gql, scheduler) }
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

    Feature("Get Rates") {
        val useCase by memoized { GetRatesUseCase(context, converter, gql, scheduler) }
        val param by memoized { RatesParam("", "", "", "", "") }
        val tSubscriber = TestSubscriber<ShippingRecommendationData>()

        Scenario("success response") {
            val success = GetRatesCourierRecommendationData().apply { ratesData = RatesData() }
            val mockViewModel = ShippingRecommendationData().apply { blackboxInfo = "test info" }
            Given("gql return success data") {
                every { gql.getExecuteObservable(null) } answers {
                    Observable.just(GraphqlResponse(mapOf(
                            GetRatesCourierRecommendationData::class.java to success
                    ), mapOf(), false))
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

    }

})