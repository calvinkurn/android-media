package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.discovery.common.EventObserver
import com.tokopedia.discovery.common.State
import com.tokopedia.search.TestException
import com.tokopedia.search.result.isExecuted
import com.tokopedia.search.result.isNeverExecuted
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.*
import com.tokopedia.search.result.stubExecute
import com.tokopedia.search.shouldBe
import com.tokopedia.search.utils.createSearchShopDefaultQuickFilter
import org.junit.Test

internal class SearchShopFirstPageTest: SearchShopDataViewTestFixtures() {

    private var searchShopFirstPagePerformanceMonitoringIsStarted = false
    private var searchShopFirstPagePerformanceMonitoringIsEnded = false
    private val searchShopFirstPagePerformanceMonitoringEventObserver = EventObserver<Boolean> {
        when(it) {
            true -> searchShopFirstPagePerformanceMonitoringIsStarted = true
            false -> searchShopFirstPagePerformanceMonitoringIsEnded = true
        }
    }

    @Test
    fun `Search Shop First Page Successful`() {
        `Given search shop API call will be successful`()
        `Given search shop first page performance monitoring observer`()

        `When handle view is visible and added`()

        `Then assert search shop performance monitoring is started and ended`()
        `Then assert search shop page first page is successful`()
        `Then assert has next page is true`()
        `Then assert get dynamic filter API called once`()
        `Then assert quick filter is shown`(searchShopModel.getQuickFilterList())
    }

    private fun `Given search shop first page performance monitoring observer`() {
        searchShopViewModel.getSearchShopFirstPagePerformanceMonitoringEventLiveData().observeForever(
                searchShopFirstPagePerformanceMonitoringEventObserver
        )
    }

    private fun `When handle view is visible and added`() {
        searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
    }

    private fun `Then assert search shop performance monitoring is started and ended`() {
        searchShopFirstPagePerformanceMonitoringIsStarted shouldBe true
        searchShopFirstPagePerformanceMonitoringIsEnded shouldBe true
    }

    private fun `Then assert search shop page first page is successful`() {
        `Then assert search shop state is success and contains search shop data`()
        `Then assert successful search shop tracking`()
    }

    private fun `Then assert search shop state is success and contains search shop data`() {
        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        val query = searchShopViewModel.getSearchParameterQuery()

        searchShopState.shouldBeInstanceOf<State.Success<*>>()
        searchShopState.shouldHaveCorrectVisitableListWithLoadingMoreViewModel(query)
        searchShopState.shouldHaveShopItemCount(shopItemList.size)
    }

    private fun `Then assert successful search shop tracking`() {
        `Then should post shop item impression tracking event`()
        `Then should post product preview impression tracking event`()
        `Then should not post empty search tracking event`()
    }

    private fun `Then should post shop item impression tracking event`() {
        val shopItemImpressionTrackingEventLiveData = searchShopViewModel.getShopItemImpressionTrackingEventLiveData().value

        val shopItemImpressionTracking = shopItemImpressionTrackingEventLiveData?.getContentIfNotHandled()
        shopItemImpressionTracking?.size shouldBe shopItemList.size
    }

    private fun `Then should post product preview impression tracking event`() {
        val productPreviewImpressionTrackingEventLiveData = searchShopViewModel.getProductPreviewImpressionTrackingEventLiveData().value

        val productPreviewImpressionTracking = productPreviewImpressionTrackingEventLiveData?.getContentIfNotHandled()
        productPreviewImpressionTracking?.size shouldBe shopItemList.size * shopItemProductList.size
    }

    private fun `Then should not post empty search tracking event`() {
        val emptySearchTrackingEvent = searchShopViewModel.getEmptySearchTrackingEventLiveData().value

        emptySearchTrackingEvent?.getContentIfNotHandled() shouldBe null
    }

    private fun `Then assert has next page is true`() {
        val hasNextPage = searchShopViewModel.getHasNextPage()

        hasNextPage shouldBe true
    }

    private fun `Then assert get dynamic filter API called once`() {
        getDynamicFilterUseCase.isExecuted()
    }

    @Test
    fun `Search Shop First Page Successful Without Next Page`() {
        `Given search shop API call will be successful and return search shop data without next page`()

        `When handle view is visible and added`()

        `Then assert search shop state is success and contains search shop data without loading more view model`()
        `Then assert successful search shop tracking`()
        `Then assert has next page is false`()
        `Then assert quick filter is shown`(searchShopModelWithoutNextPage.getQuickFilterList())
    }

    private fun `Given search shop API call will be successful and return search shop data without next page`() {
        searchShopFirstPageUseCase.stubExecute().returns(searchShopModelWithoutNextPage)
        getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
    }

    private fun `Then assert search shop state is success and contains search shop data without loading more view model`() {
        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        val query = searchShopViewModel.getSearchParameterQuery()

        searchShopState.shouldBeInstanceOf<State.Success<*>>()
        searchShopState.shouldHaveCorrectVisitableListWithoutLoadingMoreViewModel(query)
        searchShopState.shouldHaveShopItemCount(shopItemList.size)
    }

    private fun `Then assert has next page is false`() {
        val hasNextPage = searchShopViewModel.getHasNextPage()

        hasNextPage shouldBe false
    }

    @Test
    fun `Search Shop First Page Successful Without CPM`() {
        `Given search shop API call will be successful and return search shop data without CPM`()

        `When handle view is visible and added`()

        `Then assert search shop state is success and contains search shop data without CPM`()
        `Then assert successful search shop tracking`()
        `Then assert has next page is true`()
        `Then assert quick filter is shown`(searchShopModelWithoutCpm.getFilterList())
    }

    private fun `Given search shop API call will be successful and return search shop data without CPM`() {
        searchShopFirstPageUseCase.stubExecute().returns(searchShopModelWithoutCpm)
        getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
    }

    private fun `Then assert search shop state is success and contains search shop data without CPM`() {
        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        val query = searchShopViewModel.getSearchParameterQuery()

        searchShopState.shouldBeInstanceOf<State.Success<*>>()
        searchShopState.shouldHaveCorrectVisitableListWithoutCpmViewModel()
        searchShopState.shouldHaveShopItemCount(shopItemList.size)
    }

    @Test
    fun `Search Shop First Page Successful Without Valid CPM Shop`() {
        `Given search shop API call will be successful and return search shop data without Valid CPM Shop`()

        `When handle view is visible and added`()

        `Then assert search shop state is success and contains search shop data without CPM Shop`()
        `Then assert successful search shop tracking`()
        `Then assert has next page is true`()
        `Then assert quick filter is shown`(searchShopModelWithoutValidCpmShop.getFilterList())
    }

    private fun `Given search shop API call will be successful and return search shop data without Valid CPM Shop`() {
        searchShopFirstPageUseCase.stubExecute().returns(searchShopModelWithoutValidCpmShop)
        getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
    }

    private fun `Then assert search shop state is success and contains search shop data without CPM Shop`() {
        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        val query = searchShopViewModel.getSearchParameterQuery()

        searchShopState.shouldBeInstanceOf<State.Success<*>>()
        searchShopState.shouldHaveCorrectVisitableListWithoutCpmViewModel()
        searchShopState.shouldHaveShopItemCount(shopItemList.size)
    }

    @Test
    fun `Search Shop First Page Error`() {
        val exception = TestException()

        `Given search shop API call will fail`(exception)

        `When handle view is visible and added`()

        `Then assert exception print stack trace is called`(exception)
        `Then assert search shop state is error with no data`()
        `Then assert has next page is false`()
        `Then assert get dynamic filter API never called`()
        `Then assert all view is hidden`()
    }

    private fun `Given search shop API call will fail`(exception: Exception) {
        searchShopFirstPageUseCase.stubExecute().throws(exception)
    }

    private fun `Then assert exception print stack trace is called`(exception: TestException) {
        exception.isStackTracePrinted shouldBe true
    }

    private fun `Then assert search shop state is error with no data`() {
        val searchShopState = searchShopViewModel.getSearchShopLiveData().value

        searchShopState.shouldBeInstanceOf<State.Error<*>>()
        searchShopState.shouldBeNullOrEmpty()
    }

    private fun `Then assert get dynamic filter API never called`() {
        getDynamicFilterUseCase.isNeverExecuted()
    }

    private fun `Then assert all view is hidden`() {
        searchShopViewModel.getQuickFilterIsVisibleLiveData().value shouldBe false
        searchShopViewModel.getShimmeringQuickFilterIsVisibleLiveData().value shouldBe false
        searchShopViewModel.getRefreshLayoutIsVisibleLiveData().value shouldBe false
    }

    @Test
    fun `Search Shop First Page Successful with empty quick filter`() {
        `Given search shop API call will be successful and return search shop data with empty quick filter`()

        `When handle view is visible and added`()

        `Then assert search shop page first page is successful`()
        `Then assert has next page is true`()
        `Then assert get dynamic filter API called once`()
        `Then assert quick filter is shown`(createSearchShopDefaultQuickFilter().filter)
    }

    private fun `Given search shop API call will be successful and return search shop data with empty quick filter`() {
        searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyQuickFilter)
        getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
    }
}