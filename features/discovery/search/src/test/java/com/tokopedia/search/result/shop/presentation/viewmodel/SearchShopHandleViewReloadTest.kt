package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.discovery.common.EventObserver
import com.tokopedia.discovery.common.State
import com.tokopedia.search.result.isExecuted
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.*
import com.tokopedia.search.result.stubExecute
import com.tokopedia.search.shouldBe
import org.junit.Test

internal class SearchShopHandleViewReloadTest: SearchShopDataViewTestFixtures() {

    private var searchShopFirstPagePerformanceMonitoringIsStarted = false
    private var searchShopFirstPagePerformanceMonitoringIsEnded = false
    private val searchShopFirstPagePerformanceMonitoringEventObserver = EventObserver<Boolean> {
        when(it) {
            true -> searchShopFirstPagePerformanceMonitoringIsStarted = true
            false -> searchShopFirstPagePerformanceMonitoringIsEnded = true
        }
    }

    @Test
    fun `Reload Search Shop After Search Shop and Search More Shop`() {
        `Given view search shop first and second page successfully`()
        `Given search shop first page performance monitoring observer`()

        `When handle view reload search shop`()

        `Then verify search shop API is called twice and search more shop API is called once`()
        `Then verify dynamic filter API is called twice`()
        `Then assert search shop performance monitoring is started and ended`()
        `Then assert search shop state success after reload`()
    }

    private fun `Given view search shop first and second page successfully`() {
        searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
        getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
        searchShopLoadMoreUseCase.stubExecute().returns(searchMoreShopModel)

        searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
        searchShopViewModel.onViewLoadMore(isViewVisible = true)
    }

    private fun `Given search shop first page performance monitoring observer`() {
        searchShopViewModel.getSearchShopFirstPagePerformanceMonitoringEventLiveData().observeForever(
                searchShopFirstPagePerformanceMonitoringEventObserver
        )
    }

    private fun `When handle view reload search shop`() {
        searchShopViewModel.onViewReloadData()
    }

    private fun `Then verify search shop API is called twice and search more shop API is called once`() {
        searchShopFirstPageUseCase.isExecuted(2)
        searchShopLoadMoreUseCase.isExecuted()
    }

    private fun `Then verify dynamic filter API is called twice`() {
        getDynamicFilterUseCase.isExecuted(2)
    }

    private fun `Then assert search shop performance monitoring is started and ended`() {
        searchShopFirstPagePerformanceMonitoringIsStarted shouldBe true
        searchShopFirstPagePerformanceMonitoringIsEnded shouldBe true
    }

    private fun `Then assert search shop state success after reload`() {
        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        val query = searchShopViewModel.getSearchParameterQuery()

        searchShopState.shouldBeInstanceOf<State.Success<*>>()
        searchShopState.shouldHaveCorrectVisitableListWithLoadingMoreViewModel(query)
        searchShopState.shouldHaveShopItemCount(shopItemList.size)
    }

    @Test
    fun `Reload Search Shop After Search Shop First Page Gives Empty Result`() {
        `Given view search shop first page successfully with empty result`()

        `When handle view reload search shop`()

        `Then verify search shop API is called twice`()
        `Then verify dynamic filter API is called twice`()
        `Then assert search shop state success after reload`()
    }

    private fun `Given view search shop first page successfully with empty result`() {
        searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyList).andThen(searchShopModel)
        getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)

        searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
    }

    private fun `Then verify search shop API is called twice`() {
        searchShopFirstPageUseCase.isExecuted(2)
    }
}