package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.discovery.common.State
import com.tokopedia.search.TestException
import com.tokopedia.search.result.isExecuted
import com.tokopedia.search.result.isNeverExecuted
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.*
import com.tokopedia.search.result.stubExecute
import com.tokopedia.search.shouldBe
import org.junit.Test

internal class SearchShopLoadMoreTest: SearchShopDataViewTestFixtures() {

    @Test
    fun `View load more and visible, and has next page`() {
        `Given view search shop first page and has next page`()
        `Given search more shop API will be successful`()

        `When handle view load more and visible`()

        `Then verify search more shop API is called once`()
    }

    private fun `Given view search shop first page and has next page`() {
        searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
        getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)

        searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
    }

    private fun `Given search more shop API will be successful`() {
        searchShopLoadMoreUseCase.stubExecute().returns(searchMoreShopModel)
    }

    private fun `When handle view load more and visible`() {
        searchShopViewModel.onViewLoadMore(isViewVisible = true)
    }

    private fun `Then verify search more shop API is called once`() {
        searchShopLoadMoreUseCase.isExecuted()
    }

    @Test
    fun `View load more and visible, but does not have next page`() {
        `Given view search shop first page and does not have next page`()

        `When handle view load more and visible`()

        `Then verify search more shop API is never called`()
    }

    private fun `Given view search shop first page and does not have next page`() {
        searchShopFirstPageUseCase.stubExecute().returns(searchShopModelWithoutNextPage)
        getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
        searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
    }

    private fun `Then verify search more shop API is never called`() {
        searchShopLoadMoreUseCase.isNeverExecuted()
    }

    @Test
    fun `View load more but not visible`() {
        `Given view search shop first page and has next page`()

        `When handle view load more but not visible`()

        `Then verify search more shop API is never called`()
    }

    private fun `When handle view load more but not visible`() {
        searchShopViewModel.onViewLoadMore(isViewVisible = false)
    }

    @Test
    fun `View load more twice and visible, but does not have next page after first load more`() {
        `Given view search shop first page and has next page`()
        `Given search more shop API will return data with has next page is false`()

        `When handle view load more twice and visible`()

        `Then verify search more shop API is called once`()
    }

    private fun `Given search more shop API will return data with has next page is false`() {
        searchShopLoadMoreUseCase.stubExecute()
                .returns(searchMoreShopModelWithoutNextPage)
                .andThen(searchMoreShopModel)
    }

    private fun `When handle view load more twice and visible`() {
        searchShopViewModel.onViewLoadMore(isViewVisible = true)
        searchShopViewModel.onViewLoadMore(isViewVisible = true)
    }

    @Test
    fun `Search Shop and Search More Shop Successful`() {
        `Given view search shop first page and has next page`()
        `Given search more shop API will be successful`()

        `When handle view load more and visible`()

        `Then verify search shop state is success and contains data from search shop and search more shop`()
        `Then should post shop item impression tracking event`()
        `Then should post product preview impression tracking event`()
        `Then should NOT post shop recommendation item impression tracking event`()
        `Then should NOT post shop recommendation product preview impression tracking event`()
        `Then verify has next page is true`()
    }


    private fun `Then verify search shop state is success and contains data from search shop and search more shop`() {
        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        val query = searchShopViewModel.getSearchParameterQuery()

        searchShopState.shouldBeInstanceOf<State.Success<*>>()
        searchShopState.shouldHaveCorrectVisitableListWithLoadingMoreViewModel(query)
        searchShopState.shouldHaveShopItemCount(shopItemList.size + moreShopItemList.size)
    }

    private fun `Then should post shop item impression tracking event`() {
        val shopItemImpressionTrackingEventLiveData = searchShopViewModel.getShopItemImpressionTrackingEventLiveData().value

        val shopItemImpressionTracking = shopItemImpressionTrackingEventLiveData?.getContentIfNotHandled()
        shopItemImpressionTracking?.size shouldBe moreShopItemList.size
    }

    private fun `Then should post product preview impression tracking event`() {
        val productPreviewImpressionTrackingEventLiveData = searchShopViewModel.getProductPreviewImpressionTrackingEventLiveData().value

        val productPreviewImpressionTracking = productPreviewImpressionTrackingEventLiveData?.getContentIfNotHandled()
        productPreviewImpressionTracking?.size shouldBe moreShopItemList.size * shopItemProductList.size
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

    private fun `Then verify has next page is true`() {
        val hasNextPage = searchShopViewModel.getHasNextPage()

        hasNextPage shouldBe true
    }

    @Test
    fun `Search Shop Successful and Search More Shop Successful Without Next Page`() {
        `Given view search shop first page and has next page`()
        `Given search more shop API will be successful and return search shop data without next page`()

        `When handle view load more and visible`()

        `Then verify search shop state is success and contains data from search shop and search more shop without loading more view model`()
        `Then should post shop item impression tracking event`()
        `Then should post product preview impression tracking event`()
        `Then should NOT post shop recommendation item impression tracking event`()
        `Then should NOT post shop recommendation product preview impression tracking event`()
        `Then verify has next page is false`()
    }

    private fun `Given search more shop API will be successful and return search shop data without next page`() {
        searchShopLoadMoreUseCase.stubExecute().returns(searchMoreShopModelWithoutNextPage)
    }

    private fun `Then verify search shop state is success and contains data from search shop and search more shop without loading more view model`() {
        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        val query = searchShopViewModel.getSearchParameterQuery()

        searchShopState.shouldBeInstanceOf<State.Success<*>>()
        searchShopState.shouldHaveCorrectVisitableListWithoutLoadingMoreViewModel(query)
        searchShopState.shouldHaveShopItemCount(shopItemList.size + moreShopItemList.size)
    }

    private fun `Then verify has next page is false`() {
        val hasNextPage = searchShopViewModel.getHasNextPage()

        hasNextPage shouldBe false
    }

    @Test
    fun `Search Shop Successful, but Search More Shop Error`() {
        val exception = TestException()

        `Given view search shop first page and has next page`()
        `Given impression tracking already consumed by the View`()
        `Given search more shop API call will fail`(exception)

        `When handle view load more and visible`()

        `Then verify search shop state is error, but still contains data from search shop`()
        `Then verify exception print stack trace is called`(exception)
        `Then should NOT post shop recommendation item impression tracking event`()
        `Then should NOT post shop recommendation product preview impression tracking event`()
        `Then should NOT post shop item impression tracking event`()
        `Then should NOT post product preview impression tracking event`()
        `Then verify has next page is false`()
        `Then assert quick filter is shown`(searchShopModel.getQuickFilterList())
        `Then assert shop list layout is shown`()
    }

    private fun `Given impression tracking already consumed by the View`() {
        searchShopViewModel.getShopItemImpressionTrackingEventLiveData().value?.getContentIfNotHandled()
        searchShopViewModel.getProductPreviewImpressionTrackingEventLiveData().value?.getContentIfNotHandled()
    }

    private fun `Given search more shop API call will fail`(exception: Exception) {
        searchShopLoadMoreUseCase.stubExecute().throws(exception)
    }

    private fun `Then verify search shop state is error, but still contains data from search shop`() {
        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        val query = searchShopViewModel.getSearchParameterQuery()

        searchShopState.shouldBeInstanceOf<State.Error<*>>()
        searchShopState.shouldHaveCorrectVisitableListWithLoadingMoreViewModel(query)
        searchShopState.shouldHaveShopItemCount(shopItemList.size)
    }

    private fun `Then verify exception print stack trace is called`(exception: TestException) {
        exception.isStackTracePrinted shouldBe true
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

    private fun `Then assert shop list layout is shown`() {
        searchShopViewModel.getRefreshLayoutIsVisibleLiveData().value shouldBe true
    }

    @Test
    fun `Search Shop has Empty Result with Recommendation with next page, and Search Shop Load More Successful`() {
        `Given view search shop empty result with recommendation`()
        `Given search more shop API call will be successful and return search shop data in recommendation`()

        `When handle view load more and visible`()

        `Then verify search shop state is success and contains shop recommendation data from search shop and search more shop`()
        `Then should post shop recommendation item impression tracking event`()
        `Then should post shop recommendation product preview impression tracking event`()
        `Then should NOT post shop item impression tracking event`()
        `Then should NOT post product preview impression tracking event`()
        `Then verify has next page is true`()
    }

    private fun `Given view search shop empty result with recommendation` () {
        searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyWithRecommendationHasNextPage)
        getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
        searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
    }

    private fun `Given search more shop API call will be successful and return search shop data in recommendation`() {
        searchShopLoadMoreUseCase.stubExecute().returns(searchMoreShopWithRecommendationHasNextPage)
    }

    private fun `Then verify search shop state is success and contains shop recommendation data from search shop and search more shop`() {
        val searchShopState = searchShopViewModel.getSearchShopLiveData().value

        searchShopState.shouldBeInstanceOf<State.Success<*>>()
        searchShopState.shouldHaveEmptySearchWithRecommendationAndLoadMore()
        searchShopState.shouldHaveShopItemCount(shopItemList.size + moreShopItemList.size)
    }

    private fun `Then should post shop recommendation item impression tracking event`() {
        val shopRecommendationItemImpressionTrackingEventLiveData = searchShopViewModel.getShopRecommendationItemImpressionTrackingEventLiveData().value

        val shopRecommendationItemImpressionTracking = shopRecommendationItemImpressionTrackingEventLiveData?.getContentIfNotHandled()
        shopRecommendationItemImpressionTracking?.size shouldBe moreShopItemList.size
    }

    private fun `Then should post shop recommendation product preview impression tracking event`() {
        val shopRecommendationProductPreviewImpressionTrackingEventLiveData =
                searchShopViewModel.getShopRecommendationProductPreviewImpressionTrackingEventLiveData().value

        val productPreviewImpressionTracking =
                shopRecommendationProductPreviewImpressionTrackingEventLiveData?.getContentIfNotHandled()
        productPreviewImpressionTracking?.size shouldBe moreShopItemList.size * shopItemProductList.size
    }
}