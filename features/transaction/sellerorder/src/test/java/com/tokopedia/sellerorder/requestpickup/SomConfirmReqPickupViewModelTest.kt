package com.tokopedia.sellerorder.requestpickup

import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.sellerorder.list.data.model.*
import com.tokopedia.sellerorder.list.presentation.viewmodel.SomListViewModel
import com.tokopedia.sellerorder.requestpickup.data.model.SomConfirmReqPickup
import com.tokopedia.sellerorder.requestpickup.data.model.SomConfirmReqPickupParam
import com.tokopedia.sellerorder.requestpickup.data.model.SomProcessReqPickup
import com.tokopedia.sellerorder.requestpickup.data.model.SomProcessReqPickupParam
import com.tokopedia.sellerorder.requestpickup.presentation.viewmodel.SomConfirmReqPickupViewModel
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
object SomConfirmReqPickupViewModelTest: Spek({
    InstantTaskExecutorRuleSpek(this)

    val dispatcher = Dispatchers.Unconfined
    val graphqlRepository: GraphqlRepository = mockk(relaxed = true)
    val viewModel = SomConfirmReqPickupViewModel(dispatcher, graphqlRepository)
    val spy = spyk(viewModel)
    val queryConfirmReqPickup = "query_confirm_req_pickup"
    val queryProcessReqPickup = "query_req_pickup"
    val confirmReqPickupSuccessJson = "response_confirm_req_pickup_success"
    val reqPickupSuccessJson = "response_req_pickup_success"
    val gson = Gson()
    val orderId = "438036336"
    val order = SomConfirmReqPickupParam.Order(orderId = orderId)
    val arrayListOrder: ArrayList<SomConfirmReqPickupParam.Order> = arrayListOf()
    arrayListOrder.add(order)
    val confirmReqPickupParam = SomConfirmReqPickupParam(orderList = arrayListOrder)
    val processReqPickupParam = SomProcessReqPickupParam(orderId = orderId)

    Feature("SOM Confirm Request Pickup") {
        Scenario("Load Confirm Request Pickup GQL") {
            Given("Return Success Data") {
                val jsonResponse = this.javaClass.classLoader?.getResourceAsStream(confirmReqPickupSuccessJson)?.readBytes()?.toString(Charsets.UTF_8)
                val response = gson.fromJson(jsonResponse, SomConfirmReqPickup.Data::class.java)
                val gqlResponseSuccess = GraphqlResponse(
                        mapOf(SomConfirmReqPickup.Data::class.java to response),
                        mapOf(SomConfirmReqPickup.Data::class.java to listOf()), false)
                coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess
            }

            When("Load file GQL-raw mutation Confirm Request Pickup") {
                runBlocking {
                    spy.loadConfirmRequestPickup(queryConfirmReqPickup, confirmReqPickupParam)
                }
            }

            Then("Verify Func loadConfirmRequestPickup works as expected!") {
                coVerify { spy.loadConfirmRequestPickup(queryConfirmReqPickup, confirmReqPickupParam) }
            }
        }

        Scenario("Process Request Pickup") {
            Given("Return Success Data") {
                val jsonResponse = this.javaClass.classLoader?.getResourceAsStream(reqPickupSuccessJson)?.readBytes()?.toString(Charsets.UTF_8)
                val response = gson.fromJson(jsonResponse, SomProcessReqPickup::class.java)
                val gqlResponseSuccess = GraphqlResponse(
                        mapOf(SomProcessReqPickup::class.java to response),
                        mapOf(SomProcessReqPickup::class.java to listOf()), false)
                coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess
            }

            When("Load file GQL-raw mutation Request Pickup") {
                runBlocking {
                    spy.processRequestPickup(queryProcessReqPickup, processReqPickupParam)
                }
            }

            Then("Verify Func processRequestPickup works as expected!") {
                coVerify { spy.processRequestPickup(queryProcessReqPickup, processReqPickupParam) }
            }
        }
    }
})*/
