package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.result.isExecuted
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.dynamicFilterModel
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.searchShopModel
import com.tokopedia.search.result.stubExecute
import org.junit.Test

internal class SearchShopHandleViewCreatedTest: SearchShopViewModelTestFixtures() {

    override fun setUp() {
        val searchShopParameterWithActiveTab = mapOf(
                SearchApiConst.Q to "samsung",
                SearchApiConst.OFFICIAL to true,
                SearchApiConst.ACTIVE_TAB to SearchConstant.ActiveTab.SHOP
        )

        searchShopViewModel = createSearchShopViewModel(searchShopParameterWithActiveTab)
    }

    @Test
    fun `View is created and parameter active tab is shop`() {
        `Given search shop API call will be successful`()

        `When handle view created`()

        `Then verify search shop and dynamic filter API is called once`()
    }

    private fun `Given search shop API call will be successful`() {
        searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
        getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
    }

    private fun `When handle view created`() {
        searchShopViewModel.onViewCreated()
    }

    private fun `Then verify search shop and dynamic filter API is called once`() {
        searchShopFirstPageUseCase.isExecuted()
        getDynamicFilterUseCase.isExecuted()
    }

    @Test
    fun `View is created multiple times and parameter active tab is shop`() {
        `Given search shop API call will be successful`()

        `When handle view created multiple times`()

        `Then verify search shop and dynamic filter API is called once`()
    }

    private fun `When handle view created multiple times`() {
        searchShopViewModel.onViewCreated()
        searchShopViewModel.onViewCreated()
        searchShopViewModel.onViewCreated()
    }
}