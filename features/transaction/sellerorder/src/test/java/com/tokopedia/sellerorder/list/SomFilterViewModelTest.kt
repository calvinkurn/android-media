package com.tokopedia.sellerorder.list

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
