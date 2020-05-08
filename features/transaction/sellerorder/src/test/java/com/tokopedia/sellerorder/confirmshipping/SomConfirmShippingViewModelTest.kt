package com.tokopedia.sellerorder.confirmshipping

import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.sellerorder.confirmshipping.data.model.SomConfirmShipping
import com.tokopedia.sellerorder.confirmshipping.data.model.SomCourierList
import com.tokopedia.sellerorder.confirmshipping.presentation.viewmodel.SomConfirmShippingViewModel
import com.tokopedia.sellerorder.detail.data.model.*
import com.tokopedia.sellerorder.detail.presentation.viewmodel.SomDetailViewModel
import com.tokopedia.user.session.UserSession
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

/**
 * Created by fwidjaja on 2020-02-18.
 */
/*
object SomConfirmShippingViewModelTest: Spek({
    InstantTaskExecutorRuleSpek(this)

    val dispatcher = Dispatchers.Unconfined
    val graphqlRepository: GraphqlRepository = mockk(relaxed = true)
    val viewModel = SomConfirmShippingViewModel(dispatcher, graphqlRepository)
    val spy = spyk(viewModel)
    val queryChangeCourier = "query_change_courier"
    val queryCourierList = "query_courier_list"
    val queryConfirmShipping = "query_confirm_shipping"
    val changeCourierSuccessJson = "response_change_courier_success"
    val courierListSuccessJson = "response_courier_list_success"
    val confirmShippingSuccessJson = "response_confirm_shipping_success"
    val gson = Gson()

    Feature("SOM Confirm Shipping") {
        Scenario("Confirm Shipping") {
            Given("Return Success Data") {
                val jsonResponse = this.javaClass.classLoader?.getResourceAsStream(confirmShippingSuccessJson)?.readBytes()?.toString(Charsets.UTF_8)
                val response = gson.fromJson(jsonResponse, SomConfirmShipping.Data::class.java)
                val gqlResponseSuccess = GraphqlResponse(
                        mapOf(SomConfirmShipping.Data::class.java to response),
                        mapOf(SomConfirmShipping.Data::class.java to listOf()), false)
                coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess
            }

            When("Load file GQL-raw mutation mpLogisticConfirmShipping") {
                runBlocking {
                    spy.confirmShipping(queryConfirmShipping)
                }
            }

            Then("Verify Func doConfirmShipping works as expected!") {
                coVerify { spy.confirmShipping(queryConfirmShipping) }
            }
        }

        Scenario("Get Courier List") {
            Given("Return Success Data") {
                val jsonResponse = this.javaClass.classLoader?.getResourceAsStream(courierListSuccessJson)?.readBytes()?.toString(Charsets.UTF_8)
                val response = gson.fromJson(jsonResponse, SomCourierList.Data::class.java)
                val gqlResponseSuccess = GraphqlResponse(
                        mapOf(SomCourierList.Data::class.java to response),
                        mapOf(SomCourierList.Data::class.java to listOf()), false)
                coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess
            }

            When("Load file GQL-raw mutation mpLogisticGetEditShippingForm") {
                runBlocking {
                    spy.getCourierList(queryCourierList)
                }
            }

            Then("Verify Func getCourierList works as expected!") {
                coVerify { spy.getCourierList(queryCourierList) }
            }
        }

        Scenario("Change Courier") {
            Given("Return Success Data") {
                val jsonResponse = this.javaClass.classLoader?.getResourceAsStream(changeCourierSuccessJson)?.readBytes()?.toString(Charsets.UTF_8)
                val response = gson.fromJson(jsonResponse, SomConfirmShipping.Data::class.java)
                val gqlResponseSuccess = GraphqlResponse(
                        mapOf(SomConfirmShipping.Data::class.java to response),
                        mapOf(SomConfirmShipping.Data::class.java to listOf()), false)
                coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess
            }

            When("Load file GQL-raw mutation mpLogisticChangeCourier") {
                runBlocking {
                    spy.changeCourier(queryChangeCourier)
                }
            }

            Then("Verify Func doChangeCourier works as expected!") {
                coVerify { spy.changeCourier(queryChangeCourier) }
            }
        }
    }
})*/
