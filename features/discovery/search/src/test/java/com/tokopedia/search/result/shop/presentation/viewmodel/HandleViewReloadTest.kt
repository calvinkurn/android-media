package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.discovery.common.EventObserver
import com.tokopedia.discovery.common.State
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.InstantTaskExecutorRuleSpek
import com.tokopedia.search.result.isExecuted
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.*
import com.tokopedia.search.result.stubExecute
import com.tokopedia.search.shouldBe
import com.tokopedia.usecase.coroutines.UseCase
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

internal class HandleViewReloadTest: Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Handle View Reload Search Shop") {
        createTestInstance()

        Scenario("Reload Search Shop After Search Shop and Search More Shop") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val searchShopLoadMoreUseCase by memoized<UseCase<SearchShopModel>>()
            var searchShopFirstPagePerformanceMonitoringIsStarted = false
            var searchShopFirstPagePerformanceMonitoringIsEnded = false
            val searchShopFirstPagePerformanceMonitoringEventObserver = EventObserver<Boolean> {
                when(it) {
                    true -> searchShopFirstPagePerformanceMonitoringIsStarted = true
                    false -> searchShopFirstPagePerformanceMonitoringIsEnded = true
                }
            }

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("view search shop first and second page successfully") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
                searchShopLoadMoreUseCase.stubExecute().returns(searchMoreShopModel)

                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
                searchShopViewModel.onViewLoadMore(isViewVisible = true)
            }

            Given("search shop first page performance monitoring observer") {
                searchShopViewModel.getSearchShopFirstPagePerformanceMonitoringEventLiveData().observeForever(
                        searchShopFirstPagePerformanceMonitoringEventObserver
                )
            }

            When("handle view reload search shop") {
                searchShopViewModel.onViewReloadData()
            }

            Then("verify search shop API is called twice and search more shop API is called once") {
                searchShopFirstPageUseCase.isExecuted(2)
                searchShopLoadMoreUseCase.isExecuted()
            }

            Then("verify dynamic filter API is called once") {
                getDynamicFilterUseCase.isExecuted(2)
            }

            Then("assert search shop performance monitoring is started and ended") {
                searchShopFirstPagePerformanceMonitoringIsStarted shouldBe true
                searchShopFirstPagePerformanceMonitoringIsEnded shouldBe true
            }

            Then("assert search shop state success after reload") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value
                val query = searchShopViewModel.getSearchParameterQuery()

                searchShopState.shouldBeInstanceOf<State.Success<*>>()
                searchShopState.shouldHaveCorrectVisitableListWithLoadingMoreViewModel(query)
                searchShopState.shouldHaveShopItemCount(shopItemList.size)
            }
        }

        Scenario("Reload Search Shop After Search Shop First Page Gives Empty Result") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("view search shop first page successfully with empty result") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyList).andThen(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)

                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            When("handle view reload search shop") {
                searchShopViewModel.onViewReloadData()
            }

            Then("verify search shop API is called twice and search more shop API is called once") {
                searchShopFirstPageUseCase.isExecuted(2)
            }

            Then("verify dynamic filter API is called once") {
                getDynamicFilterUseCase.isExecuted(2)
            }

            Then("assert search shop state success after reload") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value
                val query = searchShopViewModel.getSearchParameterQuery()

                searchShopState.shouldBeInstanceOf<State.Success<*>>()
                searchShopState.shouldHaveCorrectVisitableListWithLoadingMoreViewModel(query)
                searchShopState.shouldHaveShopItemCount(shopItemList.size)
            }
        }
    }
})