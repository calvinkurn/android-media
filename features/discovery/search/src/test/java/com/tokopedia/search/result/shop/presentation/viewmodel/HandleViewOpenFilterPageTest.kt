package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.InstantTaskExecutorRuleSpek
import com.tokopedia.search.TestException
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.dynamicFilterModel
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.searchShopModel
import com.tokopedia.search.result.stubExecute
import com.tokopedia.search.shouldBe
import com.tokopedia.usecase.coroutines.UseCase
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

internal class HandleViewOpenFilterPageTest: Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Handle View Open Filter Page") {
        createTestInstance()

        Scenario("Open Filter Page successfully") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("view get dynamic filter successfully") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)

                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            When("handle view open filter page") {
                searchShopViewModel.onViewOpenFilterPage()
            }

            Then("should post event success open filter page") {
                val openFilterPageEvent = searchShopViewModel.getOpenFilterPageEventLiveData().value

                openFilterPageEvent?.getContentIfNotHandled() shouldBe true
            }
        }

        Scenario("Open Filter Page but Filter Data does not exists") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("view get dynamic filter successfully without filter data") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)

                val dynamicFilterModelWithoutFilterData = DynamicFilterModel()
                dynamicFilterModelWithoutFilterData.data = DataValue()
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModelWithoutFilterData)

                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            When("handle view open filter page") {
                searchShopViewModel.onViewOpenFilterPage()
            }

            Then("should show error message indicating no filter data exists") {
                val openFilterPageEvent = searchShopViewModel.getOpenFilterPageEventLiveData().value

                openFilterPageEvent?.getContentIfNotHandled() shouldBe false
            }
        }

        Scenario("Open Filter Page after Get Dynamic Filter Successful and then Failed") {
            val exception = TestException()
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("view get dynamic filter successfully") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)

                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Given("view get dynamic filter failed on second try") {
                getDynamicFilterUseCase.stubExecute().throws(exception)

                searchShopViewModel.onViewReloadData()
            }

            When("handle view open filter page") {
                searchShopViewModel.onViewOpenFilterPage()
            }

            Then("should show error message indicating no filter data exists") {
                val openFilterPageEvent = searchShopViewModel.getOpenFilterPageEventLiveData().value

                openFilterPageEvent?.getContentIfNotHandled() shouldBe false
            }
        }
    }
})