package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.discovery.common.State
import com.tokopedia.search.TestException
import com.tokopedia.search.result.isExecuted
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.*
import com.tokopedia.search.result.stubExecute
import org.junit.Test

internal class SearchShopRetryTest: SearchShopDataViewTestFixtures() {

    @Test
    fun `Retry Search Shop After Error in Search Shop`() {
        `Given view search shop first page error`()

        `When handle view retry search shop`()

        `Then verify search shop API called twice`()
        `Then verify search shop state success after retry search shop`()
    }

    private fun `Given view search shop first page error`() {
        searchShopFirstPageUseCase.stubExecute()
                .throws(TestException())
                .andThen(searchShopModel)

        getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)

        searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
    }

    private fun `When handle view retry search shop`() {
        searchShopViewModel.onViewClickRetry()
    }

    private fun `Then verify search shop API called twice`() {
        searchShopFirstPageUseCase.isExecuted(2)
    }

    private fun `Then verify search shop state success after retry search shop`() {
        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        val query = searchShopViewModel.getSearchParameterQuery()

        searchShopState.shouldBeInstanceOf<State.Success<*>>()
        searchShopState.shouldHaveCorrectVisitableListWithLoadingMoreViewModel(query)
        searchShopState.shouldHaveShopItemCount(shopItemList.size)
    }

    @Test
    fun `Retry Search Shop After Error in Search More Shop`() {
        `Given view search shop first page successfully, but error during search shop second page`()

        `When handle view retry search shop`()

        `Then verify search shop API called once, and search more shop API called twice`()
        `Then verify search shop state success after retry search more shop`()
    }


    private fun `Given view search shop first page successfully, but error during search shop second page`() {
        searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)

        getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)

        searchShopLoadMoreUseCase.stubExecute()
                .throws(TestException())
                .andThen(searchMoreShopModel)

        searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
        searchShopViewModel.onViewLoadMore(true)
    }

    private fun `Then verify search shop API called once, and search more shop API called twice`() {
        searchShopFirstPageUseCase.isExecuted()
        searchShopLoadMoreUseCase.isExecuted(2)
    }

    private fun `Then verify search shop state success after retry search more shop`() {
        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        val query = searchShopViewModel.getSearchParameterQuery()

        searchShopState.shouldBeInstanceOf<State.Success<*>>()
        searchShopState.shouldHaveCorrectVisitableListWithLoadingMoreViewModel(query)
        searchShopState.shouldHaveShopItemCount(shopItemList.size + moreShopItemList.size)
    }
}