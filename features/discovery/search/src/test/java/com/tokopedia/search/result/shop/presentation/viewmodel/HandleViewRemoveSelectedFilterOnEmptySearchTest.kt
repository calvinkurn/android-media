package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.search.InstantTaskExecutorRuleSpek
import com.tokopedia.search.result.isExecuted
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.*
import com.tokopedia.search.result.stubExecute
import com.tokopedia.search.shouldBe
import com.tokopedia.search.shouldContain
import com.tokopedia.search.shouldNotContain
import com.tokopedia.usecase.coroutines.UseCase
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

internal class HandleViewRemoveSelectedFilterOnEmptySearchTest: Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Handle View Remove Selected Filter after Empty Search") {
        createTestInstance()

        Scenario("Remove selected filter with Option's unique id") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val selectedFilterOptionUniqueId = OptionHelper.constructUniqueId(SearchApiConst.OFFICIAL, "true", "Official Store")
            lateinit var initialSearchParameter: Map<String, Any>

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("search shop API will be successful and return empty search shop list") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyList)
            }

            Given("dynamic filter API call will be successful") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyList)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            Given("view get search shop first page and get dynamic filter successfully") {
                searchShopViewModel.onViewVisibilityChanged(isViewAdded = true, isViewVisible = true)
            }

            Given("retrieve initial search parameter") {
                initialSearchParameter = searchShopViewModel.getSearchParameter()
            }

            When("handle remove selected filter") {
                searchShopViewModel.onViewRemoveSelectedFilterAfterEmptySearch(selectedFilterOptionUniqueId)
            }

            Then("Search Parameter should not contain the selected filter anymore") {
                val searchParameter = searchShopViewModel.getSearchParameter()

                searchParameter shouldNotContain SearchApiConst.OFFICIAL
            }

            Then("Search Parameter should still contain other non-selected filter") {
                val searchParameter = searchShopViewModel.getSearchParameter()

                initialSearchParameter.filter { it.key != SearchApiConst.OFFICIAL }.forEach { (key, value) ->
                    searchParameter[key] shouldBe value
                }
            }

            Then("verify search shop and get dynamic filter API is called twice for load first page and remove filter") {
                searchShopFirstPageUseCase.isExecuted(2)
                getDynamicFilterUseCase.isExecuted(2)
            }
        }

        Scenario("Remove selected filter with null unique id") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            lateinit var initialSearchParameter: Map<String, Any>

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("search shop API will be successful and return empty search shop list") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyList)
            }

            Given("dynamic filter API call will be successful") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyList)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            Given("view get search shop first page and get dynamic filter successfully") {
                searchShopViewModel.onViewVisibilityChanged(isViewAdded = true, isViewVisible = true)
            }

            Given("retrieve initial search parameter") {
                initialSearchParameter = searchShopViewModel.getSearchParameter()
            }

            When("handle remove selected filter") {
                searchShopViewModel.onViewRemoveSelectedFilterAfterEmptySearch(null)
            }

            Then("Search Parameter should not change") {
                val searchParameter = searchShopViewModel.getSearchParameter()

                searchParameter shouldBe initialSearchParameter
            }
        }

        Scenario("Remove selected option from filter with multiple options") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val selectedFilterOptionUniqueId = jakartaOption.uniqueId
            lateinit var initialSearchParameter: Map<String, Any>

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model with search parameter contains location filter (${SearchApiConst.FCITY} = 1,2,3)") {
                val searchShopParameterWithLocationFilter = mapOf<String, Any>(
                        SearchApiConst.Q to "samsung",
                        SearchApiConst.FCITY to "1,2,3"
                )

                searchShopViewModel = createSearchShopViewModel(searchShopParameterWithLocationFilter)
            }

            Given("search shop API will be successful and return empty search shop list") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyList)
            }

            Given("dynamic filter API call will be successful") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyList)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            Given("view get search shop first page and get dynamic filter successfully") {
                searchShopViewModel.onViewVisibilityChanged(isViewAdded = true, isViewVisible = true)
            }

            Given("retrieve initial search parameter") {
                initialSearchParameter = searchShopViewModel.getSearchParameter()
            }

            When("handle remove selected filter") {
                searchShopViewModel.onViewRemoveSelectedFilterAfterEmptySearch(selectedFilterOptionUniqueId)
            }

            Then("Search Parameter should only contain the remaining location filter (${SearchApiConst.FCITY} = 2,3)") {
                val searchParameter = searchShopViewModel.getSearchParameter()

                val remainingLocationFilter = searchParameter[SearchApiConst.FCITY].toString().split(OptionHelper.VALUE_SEPARATOR)

                remainingLocationFilter shouldNotContain "1"
                remainingLocationFilter shouldContain "2"
                remainingLocationFilter shouldContain "3"
            }

            Then("Search Parameter should still contain other non-selected filter") {
                val searchParameter = searchShopViewModel.getSearchParameter()

                initialSearchParameter.filter { it.key != SearchApiConst.FCITY }.forEach { (key, value) ->
                    searchParameter[key] shouldBe value
                }
            }

            Then("verify search shop and get dynamic filter API is called twice for load first page and remove filter") {
                searchShopFirstPageUseCase.isExecuted(2)
                getDynamicFilterUseCase.isExecuted(2)
            }
        }

        Scenario("Remove selected category filter option") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val selectedFilterOptionUniqueId = handphoneOption.uniqueId
            lateinit var initialSearchParameter: Map<String, Any>

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model with search parameter contains category filter (${SearchApiConst.SC} = 13,14,15))") {
                val searchShopParameterWithCategoryFilter = mapOf<String, Any>(
                        SearchApiConst.Q to "samsung",
                        SearchApiConst.SC to "13,14,15"
                )

                searchShopViewModel = createSearchShopViewModel(searchShopParameterWithCategoryFilter)
            }

            Given("search shop API will be successful and return empty search shop list") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyList)
            }

            Given("dynamic filter API call will be successful") {
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            Given("view get search shop first page and get dynamic filter successfully") {
                searchShopViewModel.onViewVisibilityChanged(isViewAdded = true, isViewVisible = true)
            }

            Given("retrieve initial search parameter") {
                initialSearchParameter = searchShopViewModel.getSearchParameter()
            }

            When("handle remove selected filter") {
                searchShopViewModel.onViewRemoveSelectedFilterAfterEmptySearch(selectedFilterOptionUniqueId)
            }

            Then("Search Parameter should not contain any category filter anymore") {
                val searchParameter = searchShopViewModel.getSearchParameter()

                searchParameter shouldNotContain SearchApiConst.SC
            }

            Then("Search Parameter should still contain other non-selected filter") {
                val searchParameter = searchShopViewModel.getSearchParameter()

                initialSearchParameter.filter { it.key != SearchApiConst.SC }.forEach { (key, value) ->
                    searchParameter[key] shouldBe value
                }
            }

            Then("verify search shop and get dynamic filter API is called twice for load first page and remove filter") {
                searchShopFirstPageUseCase.isExecuted(2)
                getDynamicFilterUseCase.isExecuted(2)
            }
        }

        Scenario("Remove selected price filter option") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val selectedFilterOptionUniqueId = OptionHelper.constructUniqueId(SearchApiConst.PMIN, "", "Filter Harga")
            lateinit var initialSearchParameter: Map<String, Any>

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model with search parameter contains Price filter (${SearchApiConst.PMIN} and ${SearchApiConst.PMAX})") {
                val searchShopParameterWithPriceFilter = mapOf(
                        SearchApiConst.Q to "samsung",
                        SearchApiConst.PMIN to 1300,
                        SearchApiConst.PMAX to 1000000
                )

                searchShopViewModel = createSearchShopViewModel(searchShopParameterWithPriceFilter)
            }

            Given("search shop API will be successful and return empty search shop list") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyList)
            }

            Given("dynamic filter API call will be successful") {
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            Given("view get search shop first page and get dynamic filter successfully") {
                searchShopViewModel.onViewVisibilityChanged(isViewAdded = true, isViewVisible = true)
            }

            Given("retrieve initial search parameter") {
                initialSearchParameter = searchShopViewModel.getSearchParameter()
            }

            When("handle remove selected filter") {
                searchShopViewModel.onViewRemoveSelectedFilterAfterEmptySearch(selectedFilterOptionUniqueId)
            }

            Then("Search Parameter should not contain any price filter") {
                val searchParameter = searchShopViewModel.getSearchParameter()

                searchParameter shouldNotContain SearchApiConst.PMIN
                searchParameter shouldNotContain SearchApiConst.PMAX
            }

            Then("Search Parameter should still contain other non-selected filter") {
                val searchParameter = searchShopViewModel.getSearchParameter()

                initialSearchParameter.filter { it.key != SearchApiConst.PMIN && it.key != SearchApiConst.PMAX }.forEach { (key, value) ->
                    searchParameter[key] shouldBe value
                }
            }

            Then("verify search shop and get dynamic filter API is called twice for load first page and remove filter") {
                searchShopFirstPageUseCase.isExecuted(2)
                getDynamicFilterUseCase.isExecuted(2)
            }
        }

        Scenario("Remove selected filter but search shop is not empty search") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val selectedFilterOptionUniqueId = OptionHelper.constructUniqueId(SearchApiConst.OFFICIAL, "true", "Official Store")
            lateinit var initialSearchParameter: Map<String, Any>

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("search shop API will be successful and return data") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
            }

            Given("dynamic filter API call will be successful and return data") {
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            Given("view get search shop first page and get dynamic filter successfully") {
                searchShopViewModel.onViewVisibilityChanged(isViewAdded = true, isViewVisible = true)
            }

            Given("retrieve initial search parameter") {
                initialSearchParameter = searchShopViewModel.getSearchParameter()
            }

            When("handle remove selected filter") {
                searchShopViewModel.onViewRemoveSelectedFilterAfterEmptySearch(selectedFilterOptionUniqueId)
            }

            Then("Search Parameter should not change. Remove selected filter only applicable during empty search") {
                val searchParameter = searchShopViewModel.getSearchParameter()

                searchParameter shouldBe initialSearchParameter
            }
        }
    }
})