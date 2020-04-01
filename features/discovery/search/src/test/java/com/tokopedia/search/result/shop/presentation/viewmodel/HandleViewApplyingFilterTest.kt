package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.InstantTaskExecutorRuleSpek
import com.tokopedia.search.result.isExecuted
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.dynamicFilterModel
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.searchShopModel
import com.tokopedia.search.result.stubExecute
import com.tokopedia.search.shouldBe
import com.tokopedia.search.utils.convertValuesToString
import com.tokopedia.usecase.coroutines.UseCase
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

internal class HandleViewApplyingFilterTest: Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Handle View Applying Filter") {
        createTestInstance()

        Scenario("Apply filter with query parameters") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val queryParametersFromFilter = mutableMapOf<String, String>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("view get search shop first page and get dynamic filter successfully") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            Given("view get search shop first page and get dynamic filter successfully") {
                searchShopViewModel.onViewVisibilityChanged(isViewAdded = true, isViewVisible = true)
            }

            Given("query parameters from filter, simulate remove and add filter") {
                queryParametersFromFilter.putAll(searchShopViewModel.getSearchParameter().convertValuesToString())
                queryParametersFromFilter.remove(SearchApiConst.OFFICIAL)
                queryParametersFromFilter[SearchApiConst.FCITY] = "1,2,3"
            }

            When("handle view applying filter") {
                searchShopViewModel.onViewApplyFilter(queryParametersFromFilter)
            }

            Then("Search Parameter should be updated with query params from filter (Except START value)") {
                val searchParameterWithoutStart = searchShopViewModel.getSearchParameter().toMutableMap()
                searchParameterWithoutStart.remove(SearchApiConst.START)
                val queryParametersFromFilterWithoutStart = queryParametersFromFilter.toMutableMap()
                queryParametersFromFilterWithoutStart.remove(SearchApiConst.START)

                searchParameterWithoutStart shouldBe queryParametersFromFilterWithoutStart
            }

            Then("assert Search Parameter START parameter is 0") {
                val searchParameter = searchShopViewModel.getSearchParameter()

                searchParameter[SearchApiConst.START] shouldBe 0
            }

            Then("verify search shop and get dynamic filter API is called twice for load first page and apply filter") {
                searchShopFirstPageUseCase.isExecuted(2)
                getDynamicFilterUseCase.isExecuted(2)
            }
        }

        Scenario("Apply filter with null query parameters") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            lateinit var initialSearchParameter: Map<String, Any>
            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("search shop and dynamic filter API call will be successful") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            Given("view get search shop first page and get dynamic filter successfully") {
                searchShopViewModel.onViewVisibilityChanged(isViewAdded = true, isViewVisible = true)
            }

            Given("retrieve initial search parameter") {
                initialSearchParameter = searchShopViewModel.getSearchParameter()
            }

            When("handle view applying filter") {
                searchShopViewModel.onViewApplyFilter(null)
            }

            Then("Search Parameter does not get updated") {
                val searchParameter = searchShopViewModel.getSearchParameter()

                searchParameter shouldBe initialSearchParameter
            }
        }
    }
})