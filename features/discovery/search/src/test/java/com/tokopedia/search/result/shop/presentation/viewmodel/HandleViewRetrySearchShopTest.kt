package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.InstantTaskExecutorRuleSpek
import com.tokopedia.search.TestException
import com.tokopedia.search.result.common.State
import com.tokopedia.search.result.isExecuted
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.*
import com.tokopedia.search.result.stubExecute
import com.tokopedia.usecase.coroutines.UseCase
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

internal class HandleViewRetrySearchShopTest: Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Handle View Retry Search Shop") {
        createTestInstance()

        Scenario("Retry Search Shop After Error in Search Shop") {
            val exception = TestException()
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("view search shop first page error") {
                searchShopFirstPageUseCase.stubExecute()
                        .throws(exception)
                        .andThen(searchShopModel)

                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)

                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            When("handle view retry search shop") {
                searchShopViewModel.onViewClickRetry()
            }

            Then("verify search shop API called twice") {
                searchShopFirstPageUseCase.isExecuted(2)
            }

            Then("assert search shop state success after retry") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value
                val query = searchShopViewModel.getSearchParameterQuery()

                searchShopState.shouldBeInstanceOf<State.Success<*>>()
                searchShopState.shouldHaveCorrectVisitableListWithLoadingMoreViewModel(query)
                searchShopState.shouldHaveShopItemCount(shopItemList.size)
            }
        }

        Scenario("Retry Search Shop After Error in Search More Shop") {
            val exception = TestException()
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val searchShopLoadMoreUseCase by memoized<UseCase<SearchShopModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("view search shop first page successfully, but error during search shop second page") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)

                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)

                searchShopLoadMoreUseCase.stubExecute()
                        .throws(exception)
                        .andThen(searchMoreShopModel)

                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
                searchShopViewModel.onViewLoadMore(true)
            }

            When("handle view retry search more shop") {
                searchShopViewModel.onViewClickRetry()
            }

            Then("verify search shop API called once, and search more shop API called twice") {
                searchShopFirstPageUseCase.isExecuted()
                searchShopLoadMoreUseCase.isExecuted(2)
            }

            Then("assert search shop state success after retry") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value
                val query = searchShopViewModel.getSearchParameterQuery()

                searchShopState.shouldBeInstanceOf<State.Success<*>>()
                searchShopState.shouldHaveCorrectVisitableListWithLoadingMoreViewModel(query)
                searchShopState.shouldHaveShopItemCount(shopItemList.size + moreShopItemList.size)
            }
        }
    }
})