package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.result.isExecuted
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.dynamicFilterModel
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.searchShopModel
import com.tokopedia.search.result.stubExecute
import com.tokopedia.search.shouldBe
import com.tokopedia.search.utils.convertValuesToString
import org.junit.Test

internal class SearchShopHandleApplyFilterTest: SearchShopDataViewTestFixtures() {

    @Test
    fun `Apply filter with query parameters`() {
        val queryParametersFromFilter = mutableMapOf<String, String>()

        `Given search shop first page and get dynamic filter API will be successful`()
        `Given search shop view visibility changed to visible`()
        `Given query parameters from filter, simulate remove and add filter`(queryParametersFromFilter)

        `When handle view apply filter`(queryParametersFromFilter)

        `Then verify Search Parameter is updated with query params from filter (Except START value)`(queryParametersFromFilter)
        `Then verify Search Parameter START is 0`()
        `Then verify search shop and get dynamic filter API is called twice for load first page and apply filter`()
    }

    private fun `Given search shop first page and get dynamic filter API will be successful`() {
        searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
        getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
    }

    private fun `Given search shop view visibility changed to visible`() {
        searchShopViewModel.onViewVisibilityChanged(isViewAdded = true, isViewVisible = true)
    }

    private fun `Given query parameters from filter, simulate remove and add filter`(queryParametersFromFilter: MutableMap<String, String>) {
        queryParametersFromFilter.putAll(searchShopViewModel.getSearchParameter().convertValuesToString())
        queryParametersFromFilter.remove(SearchApiConst.OFFICIAL)
        queryParametersFromFilter[SearchApiConst.FCITY] = "1,2,3"
    }

    private fun `When handle view apply filter`(queryParametersFromFilter: Map<String, String>?) {
        searchShopViewModel.onViewApplyFilter(queryParametersFromFilter)
    }

    private fun `Then verify Search Parameter is updated with query params from filter (Except START value)`(queryParametersFromFilter: Map<String, String>) {
        val searchParameterWithoutStart = searchShopViewModel.getSearchParameter().toMutableMap()
        searchParameterWithoutStart.remove(SearchApiConst.START)
        val queryParametersFromFilterWithoutStart = queryParametersFromFilter.toMutableMap()
        queryParametersFromFilterWithoutStart.remove(SearchApiConst.START)

        searchParameterWithoutStart shouldBe queryParametersFromFilterWithoutStart
    }

    private fun `Then verify Search Parameter START is 0`() {
        val searchParameter = searchShopViewModel.getSearchParameter()

        searchParameter[SearchApiConst.START] shouldBe 0
    }

    private fun `Then verify search shop and get dynamic filter API is called twice for load first page and apply filter`() {
        searchShopFirstPageUseCase.isExecuted(2)
        getDynamicFilterUseCase.isExecuted(2)
    }

    @Test
    fun `Apply filter with null query parameters`() {

        `Given search shop first page and get dynamic filter API will be successful`()
        `Given search shop view visibility changed to visible`()

        val initialSearchParameter = searchShopViewModel.getSearchParameter()

        `When handle view apply filter`(null)
        `Then verify Search Parameter does not get updated`(initialSearchParameter)
    }

    private fun `Then verify Search Parameter does not get updated`(expectedSearchParameter: Map<String, Any>) {
        val searchParameter = searchShopViewModel.getSearchParameter()

        searchParameter shouldBe expectedSearchParameter
    }
}