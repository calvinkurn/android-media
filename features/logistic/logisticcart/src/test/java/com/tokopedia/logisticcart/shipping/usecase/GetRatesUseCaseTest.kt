package com.tokopedia.logisticcart.shipping.usecase

import android.content.Context
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationConverter
import com.tokopedia.logisticcart.shipping.model.RatesParam
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object GetRatesUseCaseTest : Spek({

    val context = mockk<Context>(relaxed = true)
    val converter = mockk<ShippingDurationConverter>(relaxed = true)
    val gql = mockk<GraphqlUseCase>(relaxed = true)

    Feature("Basic gql") {

        val useCase by memoized { GetRatesUseCase(context, converter, gql) }
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

})