package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.InstantTaskExecutorRuleSpek
import com.tokopedia.search.TestException
import com.tokopedia.search.result.common.State
import com.tokopedia.search.result.isExecuted
import com.tokopedia.search.result.isNeverExecuted
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.*
import com.tokopedia.search.result.stubExecute
import com.tokopedia.search.shouldBe
import com.tokopedia.usecase.coroutines.UseCase
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

internal class HandleViewLoadMoreTest: Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Handle View Scroll to Load More") {
        createTestInstance()

        Scenario("View load more and visible, and has next page") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val searchShopLoadMoreUseCase by memoized<UseCase<SearchShopModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("view search shop first page and has next page") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
                searchShopLoadMoreUseCase.stubExecute().returns(searchMoreShopModel)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            When("handle view load more and visible") {
                searchShopViewModel.onViewLoadMore(isViewVisible =true)
            }

            Then("verify search more shop API is called once") {
                searchShopLoadMoreUseCase.isExecuted()
            }
        }

        Scenario("View load more and visible, but does not have next page") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val searchShopLoadMoreUseCase by memoized<UseCase<SearchShopModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("view search shop first page and does not have next page") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelWithoutNextPage)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            When("handle view load more and visible") {
                searchShopViewModel.onViewLoadMore(isViewVisible = true)
            }

            Then("verify search more shop API is never called") {
                searchShopLoadMoreUseCase.isNeverExecuted()
            }
        }

        Scenario("View load more but not visible") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val searchShopLoadMoreUseCase by memoized<UseCase<SearchShopModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("view has loaded first page and has next page") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            When("handle view load more but not visible") {
                searchShopViewModel.onViewLoadMore(isViewVisible = false)
            }

            Then("verify search more shop API is never called") {
                searchShopLoadMoreUseCase.isNeverExecuted()
            }
        }

        Scenario("View load more twice and visible, but does not have next page after first load more") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val searchShopLoadMoreUseCase by memoized<UseCase<SearchShopModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("view search shop first page and has next page") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Given("search more shop API will return data with has next page is false") {
                searchShopLoadMoreUseCase.stubExecute()
                        .returns(searchMoreShopModelWithoutNextPage)
                        .andThen(searchMoreShopModel)
            }

            When("handle view load more twice and visible") {
                searchShopViewModel.onViewLoadMore(isViewVisible = true)
                searchShopViewModel.onViewLoadMore(isViewVisible = true)
            }

            Then("verify search more shop API is only called once") {
                searchShopLoadMoreUseCase.isExecuted()
            }
        }
    }

    Feature("Search Shop Load More") {
        createTestInstance()

        Scenario("Search Shop and Search More Shop Successful") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val searchShopLoadMoreUseCase by memoized<UseCase<SearchShopModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("view search shop first page successfully") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Given("search more shop API call will be successful and return search shop data") {
                searchShopLoadMoreUseCase.stubExecute().returns(searchMoreShopModel)
            }

            When("handle view load more and visible") {
                searchShopViewModel.onViewLoadMore(true)
            }

            Then("assert search shop state is success and contains data from search shop and search more shop") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value
                val query = searchShopViewModel.getSearchParameterQuery()

                searchShopState.shouldBeInstanceOf<State.Success<*>>()
                searchShopState.shouldHaveCorrectVisitableListWithLoadingMoreViewModel(query)
                searchShopState.shouldHaveShopItemCount(shopItemList.size + moreShopItemList.size)
            }

            Then("should post shop item impression tracking event") {
                val shopItemImpressionTrackingEventLiveData = searchShopViewModel.getShopItemImpressionTrackingEventLiveData().value

                val shopItemImpressionTracking = shopItemImpressionTrackingEventLiveData?.getContentIfNotHandled()
                shopItemImpressionTracking?.size shouldBe moreShopItemList.size
            }

            Then("should post product preview impression tracking event") {
                val productPreviewImpressionTrackingEventLiveData = searchShopViewModel.getProductPreviewImpressionTrackingEventLiveData().value

                val productPreviewImpressionTracking = productPreviewImpressionTrackingEventLiveData?.getContentIfNotHandled()
                productPreviewImpressionTracking?.size shouldBe moreShopItemList.size * shopItemProductList.size
            }

            Then("assert has next page is true") {
                val hasNextPage = searchShopViewModel.getHasNextPage()

                hasNextPage shouldBe true
            }
        }

        Scenario("Search Shop Successful and Search More Shop Successful Without Next Page") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val searchShopLoadMoreUseCase by memoized<UseCase<SearchShopModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("view search shop first page successfully") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Given("search more shop API call will be successful and return search shop data without next page") {
                searchShopLoadMoreUseCase.stubExecute().returns(searchMoreShopModelWithoutNextPage)
            }

            When("handle view load more and visible") {
                searchShopViewModel.onViewLoadMore(true)
            }

            Then("assert search shop state is success and contains data from search shop and search more shop without loading more view model") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value
                val query = searchShopViewModel.getSearchParameterQuery()

                searchShopState.shouldBeInstanceOf<State.Success<*>>()
                searchShopState.shouldHaveCorrectVisitableListWithoutLoadingMoreViewModel(query)
                searchShopState.shouldHaveShopItemCount(shopItemList.size + moreShopItemList.size)
            }

            Then("should post shop item impression tracking event") {
                val shopItemImpressionTrackingEventLiveData = searchShopViewModel.getShopItemImpressionTrackingEventLiveData().value

                val shopItemImpressionTracking = shopItemImpressionTrackingEventLiveData?.getContentIfNotHandled()
                shopItemImpressionTracking?.size shouldBe moreShopItemList.size
            }

            Then("should post product preview impression tracking event") {
                val productPreviewImpressionTrackingEventLiveData = searchShopViewModel.getProductPreviewImpressionTrackingEventLiveData().value

                val productPreviewImpressionTracking = productPreviewImpressionTrackingEventLiveData?.getContentIfNotHandled()
                productPreviewImpressionTracking?.size shouldBe moreShopItemList.size * shopItemProductList.size
            }

            Then("assert has next page is false") {
                val hasNextPage = searchShopViewModel.getHasNextPage()

                hasNextPage shouldBe false
            }
        }

        Scenario("Search Shop Successful, but Search More Shop Error") {
            val exception = TestException()
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val searchShopLoadMoreUseCase by memoized<UseCase<SearchShopModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("view search shop first page successfully") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Given("search more shop API call will fail") {
                searchShopLoadMoreUseCase.stubExecute().throws(exception)
            }

            When("handle view load more and visible") {
                searchShopViewModel.onViewLoadMore(true)
            }

            Then("assert search shop state is error, but still contains data from search shop") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value
                val query = searchShopViewModel.getSearchParameterQuery()

                searchShopState.shouldBeInstanceOf<State.Error<*>>()
                searchShopState.shouldHaveCorrectVisitableListWithLoadingMoreViewModel(query)
                searchShopState.shouldHaveShopItemCount(shopItemList.size)
            }

            Then("assert exception print stack trace is called") {
                exception.isStackTracePrinted shouldBe true
            }

            Then("assert has next page is false") {
                val hasNextPage = searchShopViewModel.getHasNextPage()

                hasNextPage shouldBe false
            }
        }
    }
})