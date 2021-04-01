package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.discovery.common.State
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.*
import com.tokopedia.search.result.stubExecute
import com.tokopedia.search.shouldBe
import org.junit.Test

internal class SearchShopEmptyResultTest: SearchShopDataViewTestFixtures() {

    override fun setUp() { /* no setup required */ }

    @Test
    fun `Search Shop has Empty Result`() {
        `Given search shop view model without filter`()
        `Given search shop API will be successful and return empty search shop list`()

        `When handle view is visible and added`()

        `Then assert search shop state is success and only contains empty search data`()
        `Then should post empty search tracking event`()
        `Then should NOT post shop recommendation item impression tracking event`()
        `Then should NOT post shop recommendation product preview impression tracking event`()
        `Then should NOT post shop item impression tracking event`()
        `Then should NOT post product preview impression tracking event`()
        `Then assert has next page is false`()
        `Then assert quick filter and shimmering is hidden`()
    }

    private fun `Given search shop view model without filter`() {
        val searchParameterWithoutFilter = mapOf(
                SearchApiConst.Q to "samsung"
        )

        searchShopViewModel = createSearchShopViewModel(searchParameterWithoutFilter)
    }

    private fun `Given search shop API will be successful and return empty search shop list`() {
        searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyList)
        getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
    }

    private fun `When handle view is visible and added`() {
        searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
    }

    private fun `Then assert search shop state is success and only contains empty search data`() {
        val searchShopState = searchShopViewModel.getSearchShopLiveData().value

        searchShopState.shouldBeInstanceOf<State.Success<*>>()
        searchShopState.shouldOnlyHaveEmptySearchModel()
        searchShopState.shouldHaveEmptySearchModelWithExpectedIsFilter(false)
    }

    private fun `Then should post empty search tracking event`() {
        val emptySearchTrackingEvent = searchShopViewModel.getEmptySearchTrackingEventLiveData().value

        emptySearchTrackingEvent?.getContentIfNotHandled() shouldBe true
    }

    private fun `Then should NOT post shop recommendation item impression tracking event`() {
        val shopRecommendationItemImpressionTrackingEventLiveData = searchShopViewModel.getShopRecommendationItemImpressionTrackingEventLiveData().value

        val shopRecommendationItemImpressionTracking = shopRecommendationItemImpressionTrackingEventLiveData?.getContentIfNotHandled()
        shopRecommendationItemImpressionTracking shouldBe null
    }

    private fun `Then should NOT post shop recommendation product preview impression tracking event`() {
        val shopRecommendationProductPreviewImpressionTrackingEventLiveData =
                searchShopViewModel.getShopRecommendationProductPreviewImpressionTrackingEventLiveData().value

        val productPreviewImpressionTracking =
                shopRecommendationProductPreviewImpressionTrackingEventLiveData?.getContentIfNotHandled()
        productPreviewImpressionTracking shouldBe null
    }

    private fun `Then should NOT post shop item impression tracking event`() {
        val shopItemImpressionTrackingEventLiveData = searchShopViewModel.getShopItemImpressionTrackingEventLiveData().value

        val shopItemImpressionTracking = shopItemImpressionTrackingEventLiveData?.getContentIfNotHandled()
        shopItemImpressionTracking shouldBe null
    }

    private fun `Then should NOT post product preview impression tracking event`() {
        val productPreviewImpressionTrackingEventLiveData = searchShopViewModel.getProductPreviewImpressionTrackingEventLiveData().value

        val productPreviewImpressionTracking = productPreviewImpressionTrackingEventLiveData?.getContentIfNotHandled()
        productPreviewImpressionTracking shouldBe null
    }

    private fun `Then assert has next page is false`() {
        val hasNextPage = searchShopViewModel.getHasNextPage()

        hasNextPage shouldBe false
    }

    private fun `Then assert quick filter and shimmering is hidden`() {
        searchShopViewModel.getQuickFilterIsVisibleLiveData().value shouldBe false
        searchShopViewModel.getShimmeringQuickFilterIsVisibleLiveData().value shouldBe false
    }

    @Test
    fun `Search Shop has Empty Result with Shop Recommendation has next page`() {
        `Given search shop view model without filter`()
        `Given search shop API will be successful and return empty search shop with recommendation shop has next page`()

        `When handle view is visible and added`()

        `Then assert search shop state is success, contains empty result view, recommendation title, recommendation items, and load more`()
        `Then should post empty search tracking event`()
        `Then should post shop recommendation item impression tracking event`()
        `Then should post shop recommendation product preview impression tracking event`()
        `Then should NOT post shop item impression tracking event`()
        `Then should NOT post product preview impression tracking event`()
        `Then assert has next page is true`()
        `Then assert quick filter and shimmering is hidden`()
    }

    private fun `Given search shop API will be successful and return empty search shop with recommendation shop has next page`() {
        searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyWithRecommendationHasNextPage)
        getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
    }

    private fun `Then assert search shop state is success, contains empty result view, recommendation title, recommendation items, and load more`() {
        val searchShopState = searchShopViewModel.getSearchShopLiveData().value

        searchShopState.shouldBeInstanceOf<State.Success<*>>()
        searchShopState.shouldHaveEmptySearchWithRecommendationAndLoadMore()
        searchShopState.shouldHaveEmptySearchModelWithExpectedIsFilter(false)
    }

    private fun `Then should post shop recommendation item impression tracking event`() {
        val shopRecommendationItemImpressionTrackingEventLiveData = searchShopViewModel.getShopRecommendationItemImpressionTrackingEventLiveData().value

        val shopRecommendationItemImpressionTracking = shopRecommendationItemImpressionTrackingEventLiveData?.getContentIfNotHandled()
        shopRecommendationItemImpressionTracking?.size shouldBe shopItemList.size
    }

    private fun `Then should post shop recommendation product preview impression tracking event`() {
        val shopRecommendationProductPreviewImpressionTrackingEventLiveData =
                searchShopViewModel.getShopRecommendationProductPreviewImpressionTrackingEventLiveData().value

        val productPreviewImpressionTracking =
                shopRecommendationProductPreviewImpressionTrackingEventLiveData?.getContentIfNotHandled()
        productPreviewImpressionTracking?.size shouldBe shopItemList.size * shopItemProductList.size
    }

    private fun `Then assert has next page is true`() {
        val hasNextPage = searchShopViewModel.getHasNextPage()

        hasNextPage shouldBe true
    }

    @Test
    fun `Search Shop has Empty Result with Shop Recommendation without next page`() {
        `Given search shop view model without filter`()
        `Given search shop API will be successful and return empty search shop with recommendation shop without next page`()

        `When handle view is visible and added`()

        `Then assert search shop state is success, contains empty result view, recommendation title, and recommendation items`()
        `Then should post empty search tracking event`()
        `Then should post shop recommendation item impression tracking event`()
        `Then should post shop recommendation product preview impression tracking event`()
        `Then should NOT post shop item impression tracking event`()
        `Then should NOT post product preview impression tracking event`()
        `Then assert has next page is false`()
        `Then assert quick filter and shimmering is hidden`()
    }

    private fun `Given search shop API will be successful and return empty search shop with recommendation shop without next page`() {
        searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyWithRecommendationWithoutNextPage)
        getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
    }

    private fun `Then assert search shop state is success, contains empty result view, recommendation title, and recommendation items`() {
        val searchShopState = searchShopViewModel.getSearchShopLiveData().value

        searchShopState.shouldBeInstanceOf<State.Success<*>>()
        searchShopState.shouldHaveEmptySearchWithRecommendationWithoutLoadMore()
        searchShopState.shouldHaveEmptySearchModelWithExpectedIsFilter(false)
    }

    @Test
    fun `Get Dynamic Filter Successful and Search Shop Has Empty Result`() {
        `Given search shop view model`()
        `Given search shop API will be successful and return empty search shop list`()

        `When handle view is visible and added`()

        `Then assert dynamic filter response event is success (true)`()
        `Then assert search shop state is success and have updated Empty Search Model with Filter Data`()
    }

    private fun `Given search shop view model`() {
        searchShopViewModel = createSearchShopViewModel()
    }

    private fun `Then assert dynamic filter response event is success (true)`() {
        val getDynamicFilterResponseEvent = searchShopViewModel.getDynamicFilterEventLiveData().value

        getDynamicFilterResponseEvent?.getContentIfNotHandled() shouldBe true
    }

    private fun `Then assert search shop state is success and have updated Empty Search Model with Filter Data`() {
        val searchShopState = searchShopViewModel.getSearchShopLiveData().value

        searchShopState.shouldBeInstanceOf<State.Success<*>>()
        searchShopState.shouldOnlyHaveEmptySearchModel()
        searchShopState.shouldHaveEmptySearchModelWithExpectedIsFilter(true)
    }

    @Test
    fun `Get Dynamic Filter Successful and Search Shop Has Empty Result with Recommendation`() {
        `Given search shop view model`()
        `Given search shop API will be successful and return empty search shop with recommendation shop has next page`()

        `When handle view is visible and added`()

        `Then assert dynamic filter response event is success (true)`()
        `Then assert search shop state is success, have updated Empty Search Model with Filter Data, and contains shop recommendation`()
        `Then assert has next page is true`()
    }

    private fun `Then assert search shop state is success, have updated Empty Search Model with Filter Data, and contains shop recommendation`() {
        val searchShopState = searchShopViewModel.getSearchShopLiveData().value

        searchShopState.shouldBeInstanceOf<State.Success<*>>()
        searchShopState.shouldHaveEmptySearchWithRecommendationAndLoadMore()
        searchShopState.shouldHaveEmptySearchModelWithExpectedIsFilter(true)
    }
}