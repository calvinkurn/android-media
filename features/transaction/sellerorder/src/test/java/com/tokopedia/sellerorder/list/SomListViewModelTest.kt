package com.tokopedia.sellerorder.list

import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.sellerorder.InstantTaskExecutorRuleSpek
import com.tokopedia.sellerorder.list.data.model.SomListAllFilter
import com.tokopedia.sellerorder.list.data.model.SomListFilter
import com.tokopedia.sellerorder.list.data.model.SomListOrder
import com.tokopedia.sellerorder.list.data.model.SomListOrderParam
import com.tokopedia.sellerorder.list.presentation.viewmodel.SomListViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * Created by fwidjaja on 2020-02-17.
 */
object SomListViewModelTest: Spek({
    InstantTaskExecutorRuleSpek(this)

    val dispatcher = Dispatchers.Unconfined
    val graphqlRepository: GraphqlRepository = mockk(relaxed = true)
    val viewModel = SomListViewModel(dispatcher, graphqlRepository)
    val spy = spyk(viewModel)
    val queryFilterList = "query_filter_list"
    val queryStatusList = "query_status_list"
    val queryOrderList = "query_order_list"
    val statusSuccessJson = "response_status_list_success"
    val filterSuccessJson = "response_filter_list_success"
    val orderListSuccessJson = "response_order_list_success"
    val gson = Gson()
    val paramOrder = SomListOrderParam(
            startDate = "\"15/11/2019\"",
            endDate = "\"15/02/2020\""
    )

    Feature("SOM Order List") {
        Scenario("Load Filter GQL") {
            Given("Return Success Data") {
                val jsonResponse = this.javaClass.classLoader?.getResourceAsStream(filterSuccessJson)?.readBytes()?.toString(Charsets.UTF_8)
                val response = gson.fromJson(jsonResponse, SomListFilter::class.java)
                val gqlResponseSuccess = GraphqlResponse(
                        mapOf(SomListFilter::class.java to response),
                        mapOf(SomListFilter::class.java to listOf()), false)
                coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess
            }

            When("Load file GQLraw query filter") {
                runBlocking {
                    spy.getFilterList(queryFilterList)
                }
            }

            Then("Verify Func getFilterList works as expected!") {
                coVerify { spy.getFilterList(queryFilterList) }
            }
        }

        Scenario("Load Filter Status GQL") {
            Given("Return Success Data") {
                val jsonResponse = this.javaClass.classLoader?.getResourceAsStream(statusSuccessJson)?.readBytes()?.toString(Charsets.UTF_8)
                val response = gson.fromJson(jsonResponse, SomListAllFilter.Data.OrderFilterSomSingle.StatusList::class.java)
                val gqlResponseSuccess = GraphqlResponse(
                        mapOf(SomListAllFilter.Data.OrderFilterSomSingle.StatusList::class.java to response),
                        mapOf(SomListAllFilter.Data.OrderFilterSomSingle.StatusList::class.java to listOf()), false)
                coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess
            }

            When("Load file GQL-raw query status_list") {
                runBlocking {
                    spy.getFilterStatusList(queryStatusList)
                }
            }

            Then("Verify Func getStatusList works as expected!") {
                coVerify { spy.getFilterStatusList(queryStatusList) }
            }
        }

        Scenario("Load Order List GQL") {
            Given("Return Success Data") {
                val jsonResponse = this.javaClass.classLoader?.getResourceAsStream(orderListSuccessJson)?.readBytes()?.toString(Charsets.UTF_8)
                val response = gson.fromJson(jsonResponse, SomListOrder.Data.OrderList::class.java)
                val gqlResponseSuccess = GraphqlResponse(
                        mapOf(SomListOrder.Data.OrderList::class.java to response),
                        mapOf(SomListOrder.Data.OrderList::class.java to listOf()), false)
                coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess
            }

            When("Load file GQL-raw query order_list") {
                runBlocking {
                    spy.getOrderList(queryOrderList, paramOrder)
                }
            }

            Then("Verify Func getStatusList works as expected") {
                coVerify { spy.getOrderList(queryOrderList, paramOrder) }
            }
        }
    }
})