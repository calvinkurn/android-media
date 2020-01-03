package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.InstantTaskExecutorRuleSpek
import com.tokopedia.search.TestException
import com.tokopedia.search.result.common.EventObserver
import com.tokopedia.search.result.common.State
import com.tokopedia.search.result.isExecuted
import com.tokopedia.search.result.isNeverExecuted
import com.tokopedia.search.result.presentation.presenter.localcache.SearchLocalCacheHandler
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.*
import com.tokopedia.search.result.stubExecute
import com.tokopedia.search.shouldBe
import com.tokopedia.usecase.coroutines.UseCase
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

internal class HandleViewVisibilityChangedTest: Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Handle View Visibility Changed") {
        createTestInstance()

        Scenario("View is visible and added") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("search shop API call will be successful") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("verify search shop and dynamic filter API is called once") {
                searchShopFirstPageUseCase.isExecuted()
                getDynamicFilterUseCase.isExecuted()
            }
        }

        Scenario("View is not yet visible, or not yet added") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("search shop API call will be successful") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
            }

            When("handle view not visible or not added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = false)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = false, isViewAdded = true)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = false, isViewAdded = false)
            }

            Then("verify search shop API is never called") {
                searchShopFirstPageUseCase.isNeverExecuted()
            }
        }

        Scenario("View is visible and added more than once") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("search shop API call will be successful") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            When("handle view is visible and added, and then not visible, then visible again") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = false, isViewAdded = true)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("verify search shop and dynamic filter API is only called once") {
                searchShopFirstPageUseCase.isExecuted()
                getDynamicFilterUseCase.isExecuted()
            }
        }

        Scenario("View is created with parameter active tab is ${ SearchConstant.ActiveTab.SHOP }, and then view is visible") {
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

            Given("view is already created") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)

                searchShopViewModel.onViewCreated()
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("verify search shop and dynamic filter API is called once") {
                searchShopFirstPageUseCase.isExecuted()
                getDynamicFilterUseCase.isExecuted()
            }
        }
    }

    Feature("Search Shop First Page") {
        createTestInstance()

        Scenario("Search Shop First Page Successful") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
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

            Given("search shop API call will be successful and return search shop data") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            Given("search shop first page performance monitoring observer") {
                searchShopViewModel.getSearchShopFirstPagePerformanceMonitoringEventLiveData().observeForever(
                        searchShopFirstPagePerformanceMonitoringEventObserver
                )
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert search shop performance monitoring is started and ended") {
                searchShopFirstPagePerformanceMonitoringIsStarted shouldBe true
                searchShopFirstPagePerformanceMonitoringIsEnded shouldBe true
            }

            Then("assert search shop state is success and contains search shop data") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value
                val query = searchShopViewModel.getSearchParameterQuery()

                searchShopState.shouldBeInstanceOf<State.Success<*>>()
                searchShopState.shouldHaveCorrectVisitableListWithLoadingMoreViewModel(query)
                searchShopState.shouldHaveShopItemCount(shopItemList.size)
            }

            Then("should post shop item impression tracking event") {
                val shopItemImpressionTrackingEventLiveData = searchShopViewModel.getShopItemImpressionTrackingEventLiveData().value

                val shopItemImpressionTracking = shopItemImpressionTrackingEventLiveData?.getContentIfNotHandled()
                shopItemImpressionTracking?.size shouldBe shopItemList.size
            }

            Then("should post product preview impression tracking event") {
                val productPreviewImpressionTrackingEventLiveData = searchShopViewModel.getProductPreviewImpressionTrackingEventLiveData().value

                val productPreviewImpressionTracking = productPreviewImpressionTrackingEventLiveData?.getContentIfNotHandled()
                productPreviewImpressionTracking?.size shouldBe shopItemList.size * shopItemProductList.size
            }

            Then("should not post empty search tracking event") {
                val emptySearchTrackingEvent = searchShopViewModel.getEmptySearchTrackingEventLiveData().value

                emptySearchTrackingEvent?.getContentIfNotHandled() shouldBe null
            }

            Then("assert has next page is true") {
                val hasNextPage = searchShopViewModel.getHasNextPage()

                hasNextPage shouldBe true
            }
        }

        Scenario("Search Shop First Page Successful Without Next Page") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("search shop API call will be successful and return search shop data without next page") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelWithoutNextPage)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert search shop state is success and contains search shop data without loading more view model") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value
                val query = searchShopViewModel.getSearchParameterQuery()

                searchShopState.shouldBeInstanceOf<State.Success<*>>()
                searchShopState.shouldHaveCorrectVisitableListWithoutLoadingMoreViewModel(query)
                searchShopState.shouldHaveShopItemCount(shopItemList.size)
            }

            Then("should post shop item impression tracking event") {
                val shopItemImpressionTrackingEventLiveData = searchShopViewModel.getShopItemImpressionTrackingEventLiveData().value

                val shopItemImpressionTracking = shopItemImpressionTrackingEventLiveData?.getContentIfNotHandled()
                shopItemImpressionTracking?.size shouldBe shopItemList.size
            }

            Then("should post product preview impression tracking event") {
                val productPreviewImpressionTrackingEventLiveData = searchShopViewModel.getProductPreviewImpressionTrackingEventLiveData().value

                val productPreviewImpressionTracking = productPreviewImpressionTrackingEventLiveData?.getContentIfNotHandled()
                productPreviewImpressionTracking?.size shouldBe shopItemList.size * shopItemProductList.size
            }

            Then("should not post empty search tracking event") {
                val emptySearchTrackingEvent = searchShopViewModel.getEmptySearchTrackingEventLiveData().value

                emptySearchTrackingEvent?.getContentIfNotHandled() shouldBe null
            }

            Then("assert has next page is false") {
                val hasNextPage = searchShopViewModel.getHasNextPage()

                hasNextPage shouldBe false
            }
        }

        Scenario("Search Shop First Page Successful Without CPM") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("search shop API call will be successful and return search shop data without CPM") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelWithoutCpm)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert search shop state is success and contains search shop data without CPM") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value
                val query = searchShopViewModel.getSearchParameterQuery()

                searchShopState.shouldBeInstanceOf<State.Success<*>>()
                searchShopState.shouldHaveCorrectVisitableListWithoutCpmViewModel(query)
                searchShopState.shouldHaveShopItemCount(shopItemList.size)
            }

            Then("should post shop item impression tracking event") {
                val shopItemImpressionTrackingEventLiveData = searchShopViewModel.getShopItemImpressionTrackingEventLiveData().value

                val shopItemImpressionTracking = shopItemImpressionTrackingEventLiveData?.getContentIfNotHandled()
                shopItemImpressionTracking?.size shouldBe shopItemList.size
            }

            Then("should post product preview impression tracking event") {
                val productPreviewImpressionTrackingEventLiveData = searchShopViewModel.getProductPreviewImpressionTrackingEventLiveData().value

                val productPreviewImpressionTracking = productPreviewImpressionTrackingEventLiveData?.getContentIfNotHandled()
                productPreviewImpressionTracking?.size shouldBe shopItemList.size * shopItemProductList.size
            }

            Then("should not post empty search tracking event") {
                val emptySearchTrackingEvent = searchShopViewModel.getEmptySearchTrackingEventLiveData().value

                emptySearchTrackingEvent?.getContentIfNotHandled() shouldBe null
            }

            Then("assert has next page is true") {
                val hasNextPage = searchShopViewModel.getHasNextPage()

                hasNextPage shouldBe true
            }
        }

        Scenario("Search Shop First Page Successful Without Valid CPM Shop") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("search shop API call will be successful and return search shop data without Valid CPM Shop") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelWithoutValidCpmShop)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert search shop state is success and contains search shop data without CPM Shop") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value
                val query = searchShopViewModel.getSearchParameterQuery()

                searchShopState.shouldBeInstanceOf<State.Success<*>>()
                searchShopState.shouldHaveCorrectVisitableListWithoutCpmViewModel(query)
                searchShopState.shouldHaveShopItemCount(shopItemList.size)
            }

            Then("should post shop item impression tracking event") {
                val shopItemImpressionTrackingEventLiveData = searchShopViewModel.getShopItemImpressionTrackingEventLiveData().value

                val shopItemImpressionTracking = shopItemImpressionTrackingEventLiveData?.getContentIfNotHandled()
                shopItemImpressionTracking?.size shouldBe shopItemList.size
            }

            Then("should post product preview impression tracking event") {
                val productPreviewImpressionTrackingEventLiveData = searchShopViewModel.getProductPreviewImpressionTrackingEventLiveData().value

                val productPreviewImpressionTracking = productPreviewImpressionTrackingEventLiveData?.getContentIfNotHandled()
                productPreviewImpressionTracking?.size shouldBe shopItemList.size * shopItemProductList.size
            }

            Then("should not post empty search tracking event") {
                val emptySearchTrackingEvent = searchShopViewModel.getEmptySearchTrackingEventLiveData().value

                emptySearchTrackingEvent?.getContentIfNotHandled() shouldBe null
            }

            Then("assert has next page is true") {
                val hasNextPage = searchShopViewModel.getHasNextPage()

                hasNextPage shouldBe true
            }
        }

        Scenario("Search Shop First Page Error") {
            val exception = TestException()
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("search shop API call will fail") {
                searchShopFirstPageUseCase.stubExecute().throws(exception)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert exception print stack trace is called") {
                exception.isStackTracePrinted shouldBe true
            }

            Then("assert search shop state is error with no data") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value

                searchShopState.shouldBeInstanceOf<State.Error<*>>()
                searchShopState.shouldBeNullOrEmpty()
            }

            Then("assert has next page is false") {
                val hasNextPage = searchShopViewModel.getHasNextPage()

                hasNextPage shouldBe false
            }
        }
    }

    Feature("Get Dynamic Filter After Search Shop First Page Successful") {
        createTestInstance()

        Scenario("Search Shop First Page Successful") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("search shop API will be successful and return search shop data") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert get dynamic filter API called once") {
                getDynamicFilterUseCase.isExecuted()
            }
        }

        Scenario("Search Shop First Page Error") {
            val exception = TestException()
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("search shop API will be successful and return search shop data") {
                searchShopFirstPageUseCase.stubExecute().throws(exception)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert exception print stack trace is called") {
                exception.isStackTracePrinted shouldBe true
            }

            Then("assert get dynamic filter API never called") {
                getDynamicFilterUseCase.isNeverExecuted()
            }
        }
    }

    Feature("Get Dynamic Filter") {
        createTestInstance()

        Scenario("Get Dynamic Filter Successful") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val searchLocalCacheHandler by memoized<SearchLocalCacheHandler>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("dynamic filter API will be successful") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert save dynamic filter is executed") {
                verify(exactly = 1) {
                    searchLocalCacheHandler.saveDynamicFilterModelLocally(
                            SearchShopViewModel.SCREEN_SEARCH_PAGE_SHOP_TAB, dynamicFilterModel)
                }
            }

            Then("assert dynamic filter response event is success (true)") {
                val getDynamicFilterResponseEvent = searchShopViewModel.getDynamicFilterEventLiveData().value

                getDynamicFilterResponseEvent?.getContentIfNotHandled() shouldBe true
            }
        }

        Scenario("Get Dynamic Filter Failed") {
            val exception = TestException()
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val searchLocalCacheHandler by memoized<SearchLocalCacheHandler>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("dynamic filter API will fail") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().throws(exception)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert exception print stack trace is called") {
                exception.isStackTracePrinted shouldBe true
            }

            Then("assert save dynamic filter is not executed") {
                verify(exactly = 0) {
                    searchLocalCacheHandler.saveDynamicFilterModelLocally(any(), any())
                }
            }

            Then("assert dynamic filter response event is failed (false)") {
                val getDynamicFilterResponseEvent = searchShopViewModel.getDynamicFilterEventLiveData().value

                getDynamicFilterResponseEvent?.getContentIfNotHandled() shouldBe false
            }
        }
    }

    Feature("Search Shop Empty Result") {
        createTestInstance()

        Scenario("Search Shop has Empty Result") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                val searchParameterWithoutFilter = mapOf(
                        SearchApiConst.Q to "samsung"
                )
                searchShopViewModel = createSearchShopViewModel(searchParameterWithoutFilter)
            }

            Given("search shop API will be successful and return empty search shop list") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyList)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert search shop state is success and only contains empty search data") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value

                searchShopState.shouldBeInstanceOf<State.Success<*>>()
                searchShopState.shouldOnlyHaveEmptySearchModel()
                searchShopState.shouldHaveEmptySearchModelWithExpectedIsFilter(false)
            }

            Then("should post empty search tracking event") {
                val emptySearchTrackingEvent = searchShopViewModel.getEmptySearchTrackingEventLiveData().value

                emptySearchTrackingEvent?.getContentIfNotHandled() shouldBe true
            }

            Then("assert has next page is false") {
                val hasNextPage = searchShopViewModel.getHasNextPage()

                hasNextPage shouldBe false
            }
        }

        Scenario("Get Dynamic Filter Successful and Search Shop Has Empty Result") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val searchLocalCacheHandler by memoized<SearchLocalCacheHandler>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("search shop API will be successful and return empty search shop list") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyList)
            }

            Given("dynamic filter API will be successful") {
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert save dynamic filter is executed") {
                verify(exactly = 1) {
                    searchLocalCacheHandler.saveDynamicFilterModelLocally(
                            SearchShopViewModel.SCREEN_SEARCH_PAGE_SHOP_TAB, dynamicFilterModel)
                }
            }

            Then("assert dynamic filter response event is success (true)") {
                val getDynamicFilterResponseEvent = searchShopViewModel.getDynamicFilterEventLiveData().value

                getDynamicFilterResponseEvent?.getContentIfNotHandled() shouldBe true
            }

            Then("assert search shop state is success and have updated Empty Search Model with Filter Data") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value

                searchShopState.shouldBeInstanceOf<State.Success<*>>()
                searchShopState.shouldOnlyHaveEmptySearchModel()
                searchShopState.shouldHaveEmptySearchModelWithExpectedIsFilter(true)
            }
        }

        Scenario("Search Shop has Empty Result with Shop Recommendation") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                val searchParameterWithoutFilter = mapOf(
                        SearchApiConst.Q to "samsung"
                )
                searchShopViewModel = createSearchShopViewModel(searchParameterWithoutFilter)
            }

            Given("search shop API will be successful and return empty search shop with recommendation shop") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyWithRecommendation)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert search shop state is success, contains empty result view, recommendation title, and recommendation items") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value

                searchShopState.shouldBeInstanceOf<State.Success<*>>()
                searchShopState.shouldHaveEmptySearchWithRecommendation()
                searchShopState.shouldHaveEmptySearchModelWithExpectedIsFilter(false)
            }

            Then("should post empty search tracking event") {
                val emptySearchTrackingEvent = searchShopViewModel.getEmptySearchTrackingEventLiveData().value

                emptySearchTrackingEvent?.getContentIfNotHandled() shouldBe true
            }

            Then("assert has next page is false") {
                val hasNextPage = searchShopViewModel.getHasNextPage()

                hasNextPage shouldBe false
            }
        }

        Scenario("Get Dynamic Filter Successful and Search Shop Has Empty Result with Recommendation") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val searchLocalCacheHandler by memoized<SearchLocalCacheHandler>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("search shop API will be successful and return empty search shop with recommendation") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyWithRecommendation)
            }

            Given("dynamic filter API will be successful") {
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert save dynamic filter is executed") {
                verify(exactly = 1) {
                    searchLocalCacheHandler.saveDynamicFilterModelLocally(
                            SearchShopViewModel.SCREEN_SEARCH_PAGE_SHOP_TAB, dynamicFilterModel)
                }
            }

            Then("assert dynamic filter response event is success (true)") {
                val getDynamicFilterResponseEvent = searchShopViewModel.getDynamicFilterEventLiveData().value

                getDynamicFilterResponseEvent?.getContentIfNotHandled() shouldBe true
            }

            Then("assert search shop state is success and have updated Empty Search Model with Filter Data") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value

                searchShopState.shouldBeInstanceOf<State.Success<*>>()
                searchShopState.shouldHaveEmptySearchWithRecommendation()
                searchShopState.shouldHaveEmptySearchModelWithExpectedIsFilter(true)
            }
        }
    }
})