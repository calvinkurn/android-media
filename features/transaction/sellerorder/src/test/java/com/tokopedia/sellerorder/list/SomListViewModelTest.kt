package com.tokopedia.sellerorder.list

import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.sellerorder.InstantTaskExecutorRuleSpek
import com.tokopedia.sellerorder.list.data.model.*
import com.tokopedia.sellerorder.list.presentation.viewmodel.SomListViewModel
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
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
    val observerListFilter = mockk<Observer<Result<MutableList<SomListFilter.Data.OrderFilterSom.StatusList>>>>(relaxed = true)
    val queryFilterList = "query_filter_list"
    val queryStatusList = "query_status_list"
    val queryOrderList = "query_order_list"
    val queryTickerList = "query_ticker_list"
    val statusSuccessJson = "response_status_list_success"
    val filterSuccessJson = "response_filter_list_success"
    val orderListSuccessJson = "response_order_list_success"
    val tickerListSuccessJson = "response_ticker_list_success"
    val gson = Gson()
    val paramOrder = SomListOrderParam(
            startDate = "\"15/11/2019\"",
            endDate = "\"15/02/2020\""
    )

    Feature("SOM Order List") {
        Scenario("Success Load Filter") {
            val jsonResponse = this.javaClass.classLoader?.getResourceAsStream(filterSuccessJson)?.readBytes()?.toString(Charsets.UTF_8)
            val response = gson.fromJson(jsonResponse, SomListFilter::class.java)
            Given("Return Success Data") {
                val gqlResponseSuccess = GraphqlResponse(
                        mapOf(SomListFilter::class.java to response),
                        mapOf(SomListFilter::class.java to listOf()), false)
                coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess
            }

            When("Load file GQLraw query filter") {
                runBlocking {
                    viewModel.filterListResult.observeForever(observerListFilter)
                    viewModel.getFilterList(queryFilterList)
                }
            }

            Then("Verify Func loadFilterList returns Success") {
                coVerify { observerListFilter.onChanged(Success(response.data.orderFilterSom.statusList.toMutableList())) }
            }
        }

        /*Scenario("Load Filter Status GQL") {
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
                    spy.loadStatusList(queryStatusList)
                }
            }

            Then("Verify Func loadStatusList works as expected!") {
                coVerify { spy.loadStatusList(queryStatusList) }
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
                    spy.loadOrderList(queryOrderList, paramOrder)
                }
            }

            Then("Verify Func loadOrderList works as expected") {
                coVerify { spy.loadOrderList(queryOrderList, paramOrder) }
            }
        }

        Scenario("Load Ticker GQL") {
            Given("Return Success Data") {
                val jsonResponse = this.javaClass.classLoader?.getResourceAsStream(tickerListSuccessJson)?.readBytes()?.toString(Charsets.UTF_8)
                val response = gson.fromJson(jsonResponse, SomListTicker.Data::class.java)
                val gqlResponseSuccess = GraphqlResponse(
                        mapOf(SomListTicker.Data::class.java to response),
                        mapOf(SomListTicker.Data::class.java to listOf()), false)
                coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess
            }

            When("Load file GQL-raw query ticker_list") {
                runBlocking {
                    spy.loadTickerList(queryTickerList)
                }
            }

            Then("Verify Func loadTickerList works as expected!") {
                coVerify { spy.loadTickerList(queryTickerList) }
            }
        }*/
    }
})