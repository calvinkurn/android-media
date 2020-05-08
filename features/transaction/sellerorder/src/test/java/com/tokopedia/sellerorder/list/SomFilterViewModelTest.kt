package com.tokopedia.sellerorder.list

import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.sellerorder.list.data.model.SomListAllFilter
import com.tokopedia.sellerorder.list.data.model.SomListFilter
import com.tokopedia.sellerorder.list.data.model.SomListOrder
import com.tokopedia.sellerorder.list.data.model.SomListOrderParam
import com.tokopedia.sellerorder.list.presentation.viewmodel.SomFilterViewModel
import com.tokopedia.sellerorder.list.presentation.viewmodel.SomListViewModel
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
object SomFilterViewModelTest: {
    InstantTaskExecutorRuleSpek(this)

    val dispatcher = Dispatchers.Unconfined
    val graphqlRepository: GraphqlRepository = mockk(relaxed = true)
    val viewModel = SomFilterViewModel(dispatcher, graphqlRepository)
    val spy = spyk(viewModel)
    val queryFilterPageList = "query_filter_page_list"
    val filterPageSuccessJson = "response_filter_page_success"
    val gson = Gson()

    Feature("SOM Filter Page") {
        Scenario("Load Filter Page GQL") {
            Given("Return Success Data") {
                val jsonResponse = this.javaClass.classLoader?.getResourceAsStream(filterPageSuccessJson)?.readBytes()?.toString(Charsets.UTF_8)
                val response = gson.fromJson(jsonResponse, SomListAllFilter.Data::class.java)
                val gqlResponseSuccess = GraphqlResponse(
                        mapOf(SomListAllFilter.Data::class.java to response),
                        mapOf(SomListAllFilter.Data::class.java to listOf()), false)
                coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess
            }

            When("Load file GQL-raw query filter") {
                runBlocking {
                    spy.loadSomFilterData(queryFilterPageList)
                }
            }

            Then("Verify Func getFilterList works as expected!") {
                coVerify { spy.loadSomFilterData(queryFilterPageList) }
            }
        }
    }
})*/
