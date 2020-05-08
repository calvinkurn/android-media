package com.tokopedia.sellerorder.detail

import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
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
object SomDetailViewModelTest: Spek({
    InstantTaskExecutorRuleSpek(this)

    val dispatcher = Dispatchers.Unconfined
    val graphqlRepository: GraphqlRepository = mockk(relaxed = true)
    val userSession: UserSession = mockk(relaxed = true)
    val viewModel = SomDetailViewModel(dispatcher, graphqlRepository, userSession)
    val spy = spyk(viewModel)
    val queryOrderDetailPageList = "query_filter_page_list"
    val queryAcceptOrder = "query_accept_order"
    val queryRejectOrder = "query_reject_order"
    val queryEditAwb = "query_edit_awb"
    val orderDetailPageSuccessJson = "response_order_detail_success"
    val acceptOrderSuccessJson = "response_accept_order_success"
    val rejectReasonsSuccessJson = "response_reject_reasons_success"
    val rejectOrderSuccessJson = "response_reject_order_success"
    val editAwbSuccessJson = "response_edit_awb_success"
    val shopId = "1479278"
    val orderId = "397293266"
    val orderIdNewOrder = "437997831"
    val gson = Gson()
    val paramRejectOrder = SomRejectRequest(
        orderId = orderIdNewOrder,
        reason = "lalala",
        rCode = "2",
        listPrd = "14263814~14263815",
        closeEnd = "17/05/2017",
        userId = "12345",
        mobile = "1",
        lang = "id",
        ignorePenalty = "false"
    )

    Feature("SOM Detail Order Page") {
        Scenario("Load Detail Order Page GQL") {
            Given("Return Success Data") {
                val jsonResponse = this.javaClass.classLoader?.getResourceAsStream(orderDetailPageSuccessJson)?.readBytes()?.toString(Charsets.UTF_8)
                val response = gson.fromJson(jsonResponse, SomDetailOrder.Data::class.java)
                val gqlResponseSuccess = GraphqlResponse(
                        mapOf(SomDetailOrder.Data::class.java to response),
                        mapOf(SomDetailOrder.Data::class.java to listOf()), false)
                coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess
            }

            When("Load file GQL-raw query GetSOMDetail") {
                runBlocking {
                    spy.loadDetailOrder(queryOrderDetailPageList, orderId)
                }
            }

            Then("Verify Func getDetailOrder works as expected!") {
                coVerify { spy.loadDetailOrder(queryOrderDetailPageList, orderId) }
            }
        }

        Scenario("Accept Order") {
            Given("Return Success Data") {
                val jsonResponse = this.javaClass.classLoader?.getResourceAsStream(acceptOrderSuccessJson)?.readBytes()?.toString(Charsets.UTF_8)
                val response = gson.fromJson(jsonResponse, SomAcceptOrder.Data::class.java)
                val gqlResponseSuccess = GraphqlResponse(
                        mapOf(SomAcceptOrder.Data::class.java to response),
                        mapOf(SomAcceptOrder.Data::class.java to listOf()), false)
                coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess
            }

            When("Load file GQL-raw mutation Accept Order") {
                runBlocking {
                    spy.acceptOrder(queryAcceptOrder, orderIdNewOrder, shopId)
                }
            }

            Then("Verify Func acceptOrder works as expected!") {
                coVerify { spy.acceptOrder(queryAcceptOrder, orderIdNewOrder, shopId) }
            }
        }

        Scenario("Get Reject Reasons") {
            Given("Return Success Data") {
                val jsonResponse = this.javaClass.classLoader?.getResourceAsStream(rejectReasonsSuccessJson)?.readBytes()?.toString(Charsets.UTF_8)
                val response = gson.fromJson(jsonResponse, SomReasonRejectData.Data::class.java)
                val gqlResponseSuccess = GraphqlResponse(
                        mapOf(SomReasonRejectData.Data::class.java to response),
                        mapOf(SomReasonRejectData.Data::class.java to listOf()), false)
                coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess
            }

            When("Load file GQL-raw query SOMRejectReason") {
                runBlocking {
                    spy.getRejectReasons(queryAcceptOrder)
                }
            }

            Then("Verify Func doGetRejectReasons works as expected!") {
                coVerify { spy.getRejectReasons(queryAcceptOrder) }
            }
        }

        Scenario("Reject Order") {
            Given("Return Success Data") {
                val jsonResponse = this.javaClass.classLoader?.getResourceAsStream(rejectOrderSuccessJson)?.readBytes()?.toString(Charsets.UTF_8)
                val response = gson.fromJson(jsonResponse, SomRejectOrder.Data::class.java)
                val gqlResponseSuccess = GraphqlResponse(
                        mapOf(SomRejectOrder.Data::class.java to response),
                        mapOf(SomRejectOrder.Data::class.java to listOf()), false)
                coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess
            }

            When("Load file GQL-raw mutation RejectOrder") {
                runBlocking {
                    spy.rejectOrder(queryRejectOrder, paramRejectOrder)
                }
            }

            Then("Verify Func doRejectOrder works as expected!") {
                coVerify { spy.rejectOrder(queryRejectOrder, paramRejectOrder) }
            }
        }

        Scenario("Edit AWB") {
            Given("Return Success Data") {
                val jsonResponse = this.javaClass.classLoader?.getResourceAsStream(editAwbSuccessJson)?.readBytes()?.toString(Charsets.UTF_8)
                val response = gson.fromJson(jsonResponse, SomEditAwbResponse.Data::class.java)
                val gqlResponseSuccess = GraphqlResponse(
                        mapOf(SomEditAwbResponse.Data::class.java to response),
                        mapOf(SomEditAwbResponse.Data::class.java to listOf()), false)
                coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess
            }

            When("Load file GQL-raw mutation Edit Awb") {
                runBlocking {
                    spy.editAwb(queryEditAwb)
                }
            }

            Then("Verify Func doEditAwb works as expected!") {
                coVerify { spy.editAwb(queryEditAwb) }
            }
        }
    }
})*/
