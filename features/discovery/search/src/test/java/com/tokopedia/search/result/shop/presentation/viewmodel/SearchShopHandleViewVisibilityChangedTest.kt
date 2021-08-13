package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.result.isNeverExecuted
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.dynamicFilterModel
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.searchShopModel
import com.tokopedia.search.result.stubExecute
import org.junit.Test

internal class SearchShopHandleViewVisibilityChangedTest: SearchShopDataViewTestFixtures() {

    @Test
    fun `View is visible and added`() {
        `Given search shop API call will be successful`()

        `When handle view is visible and added`()

        `Then verify search shop and dynamic filter API is called once`()
    }

    private fun `When handle view is visible and added`() {
        searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
    }

    @Test
    fun `View is not yet visible, or not yet added`() {
        `Given search shop API call will be successful`()

        `When handle view not visible or not added`()

        `Then verify search shop API is never called`()
    }

    private fun `When handle view not visible or not added`() {
        searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = false)
        searchShopViewModel.onViewVisibilityChanged(isViewVisible = false, isViewAdded = true)
        searchShopViewModel.onViewVisibilityChanged(isViewVisible = false, isViewAdded = false)
    }

    private fun `Then verify search shop API is never called`() {
        searchShopFirstPageUseCase.isNeverExecuted()
    }

    @Test
    fun `View is visible and added more than once`() {
        `Given search shop API call will be successful`()

        `When handle view is visible and added, and then not visible, then visible again`()

        `Then verify search shop and dynamic filter API is called once`()
    }

    private fun `When handle view is visible and added, and then not visible, then visible again`() {
        searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
        searchShopViewModel.onViewVisibilityChanged(isViewVisible = false, isViewAdded = true)
        searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
    }

    @Test
    fun `View is created with parameter active tab is shop, and then view is visible`() {
        `Given search shop view model with parameter has active tab key = shop`()
        `Given view is already created`()

        `When handle view is visible and added`()

        `Then verify search shop and dynamic filter API is called once`()
    }

    private fun `Given search shop view model with parameter has active tab key = shop`() {
        val searchShopParameterWithActiveTab = mapOf(
                SearchApiConst.Q to "samsung",
                SearchApiConst.OFFICIAL to true,
                SearchApiConst.ACTIVE_TAB to SearchConstant.ActiveTab.SHOP
        )

        searchShopViewModel = createSearchShopViewModel(searchShopParameterWithActiveTab)
    }

    private fun `Given view is already created`() {
        searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
        getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)

        searchShopViewModel.onViewCreated()
    }
}