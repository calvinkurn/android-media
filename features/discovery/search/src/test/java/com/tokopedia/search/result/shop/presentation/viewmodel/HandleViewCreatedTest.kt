package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.InstantTaskExecutorRuleSpek
import com.tokopedia.search.result.isExecuted
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.dynamicFilterModel
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.searchShopModel
import com.tokopedia.search.result.stubExecute
import com.tokopedia.usecase.coroutines.UseCase
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

internal class HandleViewCreatedTest: Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Handle View Created") {
        createTestInstance()

        Scenario("View is created and parameter active tab is ${ SearchConstant.ActiveTab.SHOP }") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model with parameter has active tab key = shop") {
                val searchShopParameterWithActiveTab = mapOf(
                        SearchApiConst.Q to "samsung",
                        SearchApiConst.OFFICIAL to true,
                        SearchApiConst.ACTIVE_TAB to SearchConstant.ActiveTab.SHOP
                )

                searchShopViewModel = createSearchShopViewModel(searchShopParameterWithActiveTab)
            }

            Given("search shop API call will be successful") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            When("handle view created") {
                searchShopViewModel.onViewCreated()
            }

            Then("verify search shop and dynamic filter API is called once") {
                searchShopFirstPageUseCase.isExecuted()
                getDynamicFilterUseCase.isExecuted()
            }
        }

        Scenario("View is created multiple times and parameter active tab is ${ SearchConstant.ActiveTab.SHOP }") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model with parameter has active tab key = shop") {
                val searchShopParameterWithActiveTab = mapOf(
                        SearchApiConst.Q to "samsung",
                        SearchApiConst.OFFICIAL to true,
                        SearchApiConst.ACTIVE_TAB to SearchConstant.ActiveTab.SHOP
                )

                searchShopViewModel = createSearchShopViewModel(searchShopParameterWithActiveTab)
            }

            Given("search shop API call will be successful") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            When("handle view created multiple times") {
                searchShopViewModel.onViewCreated()
                searchShopViewModel.onViewCreated()
                searchShopViewModel.onViewCreated()
            }

            Then("verify search shop and dynamic filter API is called once") {
                searchShopFirstPageUseCase.isExecuted()
                getDynamicFilterUseCase.isExecuted()
            }
        }
    }
})