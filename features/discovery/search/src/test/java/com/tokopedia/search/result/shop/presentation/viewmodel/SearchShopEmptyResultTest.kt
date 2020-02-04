package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.discovery.common.State
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.InstantTaskExecutorRuleSpek
import com.tokopedia.search.result.presentation.presenter.localcache.SearchLocalCacheHandler
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.*
import com.tokopedia.search.result.stubExecute
import com.tokopedia.search.shouldBe
import com.tokopedia.usecase.coroutines.UseCase
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

internal class SearchShopEmptyResultTest: Spek({

    InstantTaskExecutorRuleSpek(this)

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

            Then("should NOT post shop recommendation item impression tracking event") {
                val shopRecommendationItemImpressionTrackingEventLiveData = searchShopViewModel.getShopRecommendationItemImpressionTrackingEventLiveData().value

                val shopRecommendationItemImpressionTracking = shopRecommendationItemImpressionTrackingEventLiveData?.getContentIfNotHandled()
                shopRecommendationItemImpressionTracking shouldBe null
            }

            Then("should NOT post shop recommendation product preview impression tracking event") {
                val shopRecommendationProductPreviewImpressionTrackingEventLiveData =
                        searchShopViewModel.getShopRecommendationProductPreviewImpressionTrackingEventLiveData().value

                val productPreviewImpressionTracking =
                        shopRecommendationProductPreviewImpressionTrackingEventLiveData?.getContentIfNotHandled()
                productPreviewImpressionTracking shouldBe null
            }

            Then("should NOT post shop item impression tracking event") {
                val shopItemImpressionTrackingEventLiveData = searchShopViewModel.getShopItemImpressionTrackingEventLiveData().value

                val shopItemImpressionTracking = shopItemImpressionTrackingEventLiveData?.getContentIfNotHandled()
                shopItemImpressionTracking shouldBe null
            }

            Then("should NOT post product preview impression tracking event") {
                val productPreviewImpressionTrackingEventLiveData = searchShopViewModel.getProductPreviewImpressionTrackingEventLiveData().value

                val productPreviewImpressionTracking = productPreviewImpressionTrackingEventLiveData?.getContentIfNotHandled()
                productPreviewImpressionTracking shouldBe null
            }

            Then("assert has next page is false") {
                val hasNextPage = searchShopViewModel.getHasNextPage()

                hasNextPage shouldBe false
            }

            Then("assert bottom navigation visibility event is false (hidden)") {
                val bottomNavigationVisibilityEventLiveData = searchShopViewModel.getBottomNavigationVisibilityEventLiveData().value

                val bottomNavigationVisibilityEvent = bottomNavigationVisibilityEventLiveData?.getContentIfNotHandled()
                bottomNavigationVisibilityEvent shouldBe false
            }
        }

        Scenario("Search Shop has Empty Result with Shop Recommendation has next page") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                val searchParameterWithoutFilter = mapOf(
                        SearchApiConst.Q to "samsung"
                )
                searchShopViewModel = createSearchShopViewModel(searchParameterWithoutFilter)
            }

            Given("search shop API will be successful and return empty search shop with recommendation shop has next page") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyWithRecommendationHasNextPage)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert search shop state is success, contains empty result view, recommendation title, and recommendation items") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value

                searchShopState.shouldBeInstanceOf<State.Success<*>>()
                searchShopState.shouldHaveEmptySearchWithRecommendationAndLoadMore()
                searchShopState.shouldHaveEmptySearchModelWithExpectedIsFilter(false)
            }

            Then("should post empty search tracking event") {
                val emptySearchTrackingEvent = searchShopViewModel.getEmptySearchTrackingEventLiveData().value

                emptySearchTrackingEvent?.getContentIfNotHandled() shouldBe true
            }

            Then("should post shop recommendation item impression tracking event") {
                val shopRecommendationItemImpressionTrackingEventLiveData = searchShopViewModel.getShopRecommendationItemImpressionTrackingEventLiveData().value

                val shopRecommendationItemImpressionTracking = shopRecommendationItemImpressionTrackingEventLiveData?.getContentIfNotHandled()
                shopRecommendationItemImpressionTracking?.size shouldBe shopItemList.size
            }

            Then("should post shop recommendation product preview impression tracking event") {
                val shopRecommendationProductPreviewImpressionTrackingEventLiveData =
                        searchShopViewModel.getShopRecommendationProductPreviewImpressionTrackingEventLiveData().value

                val productPreviewImpressionTracking =
                        shopRecommendationProductPreviewImpressionTrackingEventLiveData?.getContentIfNotHandled()
                productPreviewImpressionTracking?.size shouldBe shopItemList.size * shopItemProductList.size
            }

            Then("should NOT post shop item impression tracking event") {
                val shopItemImpressionTrackingEventLiveData = searchShopViewModel.getShopItemImpressionTrackingEventLiveData().value

                val shopItemImpressionTracking = shopItemImpressionTrackingEventLiveData?.getContentIfNotHandled()
                shopItemImpressionTracking shouldBe null
            }

            Then("should NOT post product preview impression tracking event") {
                val productPreviewImpressionTrackingEventLiveData = searchShopViewModel.getProductPreviewImpressionTrackingEventLiveData().value

                val productPreviewImpressionTracking = productPreviewImpressionTrackingEventLiveData?.getContentIfNotHandled()
                productPreviewImpressionTracking shouldBe null
            }

            Then("assert has next page is true") {
                val hasNextPage = searchShopViewModel.getHasNextPage()

                hasNextPage shouldBe true
            }

            Then("assert bottom navigation visibility event is false (hidden)") {
                val bottomNavigationVisibilityEventLiveData = searchShopViewModel.getBottomNavigationVisibilityEventLiveData().value

                val bottomNavigationVisibilityEvent = bottomNavigationVisibilityEventLiveData?.getContentIfNotHandled()
                bottomNavigationVisibilityEvent shouldBe false
            }
        }

        Scenario("Search Shop has Empty Result with Shop Recommendation without next page") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                val searchParameterWithoutFilter = mapOf(
                        SearchApiConst.Q to "samsung"
                )
                searchShopViewModel = createSearchShopViewModel(searchParameterWithoutFilter)
            }

            Given("search shop API will be successful and return empty search shop with recommendation shop without next page") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyWithRecommendationWithoutNextPage)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert search shop state is success, contains empty result view, recommendation title, and recommendation items") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value

                searchShopState.shouldBeInstanceOf<State.Success<*>>()
                searchShopState.shouldHaveEmptySearchWithRecommendationWithoutLoadMore()
                searchShopState.shouldHaveEmptySearchModelWithExpectedIsFilter(false)
            }

            Then("should post empty search tracking event") {
                val emptySearchTrackingEvent = searchShopViewModel.getEmptySearchTrackingEventLiveData().value

                emptySearchTrackingEvent?.getContentIfNotHandled() shouldBe true
            }

            Then("should post shop recommendation item impression tracking event") {
                val shopRecommendationItemImpressionTrackingEventLiveData = searchShopViewModel.getShopRecommendationItemImpressionTrackingEventLiveData().value

                val shopRecommendationItemImpressionTracking = shopRecommendationItemImpressionTrackingEventLiveData?.getContentIfNotHandled()
                shopRecommendationItemImpressionTracking?.size shouldBe shopItemList.size
            }

            Then("should post shop recommendation product preview impression tracking event") {
                val shopRecommendationProductPreviewImpressionTrackingEventLiveData =
                        searchShopViewModel.getShopRecommendationProductPreviewImpressionTrackingEventLiveData().value

                val productPreviewImpressionTracking =
                        shopRecommendationProductPreviewImpressionTrackingEventLiveData?.getContentIfNotHandled()
                productPreviewImpressionTracking?.size shouldBe shopItemList.size * shopItemProductList.size
            }

            Then("should NOT post shop item impression tracking event") {
                val shopItemImpressionTrackingEventLiveData = searchShopViewModel.getShopItemImpressionTrackingEventLiveData().value

                val shopItemImpressionTracking = shopItemImpressionTrackingEventLiveData?.getContentIfNotHandled()
                shopItemImpressionTracking shouldBe null
            }

            Then("should NOT post product preview impression tracking event") {
                val productPreviewImpressionTrackingEventLiveData = searchShopViewModel.getProductPreviewImpressionTrackingEventLiveData().value

                val productPreviewImpressionTracking = productPreviewImpressionTrackingEventLiveData?.getContentIfNotHandled()
                productPreviewImpressionTracking shouldBe null
            }

            Then("assert has next page is false") {
                val hasNextPage = searchShopViewModel.getHasNextPage()

                hasNextPage shouldBe false
            }

            Then("assert bottom navigation visibility event is false (hidden)") {
                val bottomNavigationVisibilityEventLiveData = searchShopViewModel.getBottomNavigationVisibilityEventLiveData().value

                val bottomNavigationVisibilityEvent = bottomNavigationVisibilityEventLiveData?.getContentIfNotHandled()
                bottomNavigationVisibilityEvent shouldBe false
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

        Scenario("Get Dynamic Filter Successful and Search Shop Has Empty Result with Recommendation") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val searchLocalCacheHandler by memoized<SearchLocalCacheHandler>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("search shop API will be successful and return empty search shop with recommendation") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyWithRecommendationHasNextPage)
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
                searchShopState.shouldHaveEmptySearchWithRecommendationAndLoadMore()
                searchShopState.shouldHaveEmptySearchModelWithExpectedIsFilter(true)
            }

            Then("assert has next page is true") {
                val hasNextPage = searchShopViewModel.getHasNextPage()

                hasNextPage shouldBe true
            }
        }
    }
})