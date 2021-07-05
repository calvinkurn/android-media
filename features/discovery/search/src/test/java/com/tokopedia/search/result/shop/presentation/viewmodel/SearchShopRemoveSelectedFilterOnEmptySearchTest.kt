package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.search.result.isExecuted
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.dynamicFilterModel
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.handphoneOption
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.jakartaOption
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.searchShopModel
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.searchShopModelEmptyList
import com.tokopedia.search.result.stubExecute
import com.tokopedia.search.shouldBe
import com.tokopedia.search.shouldContain
import com.tokopedia.search.shouldNotContain
import org.junit.Test

internal class SearchShopRemoveSelectedFilterOnEmptySearchTest: SearchShopDataViewTestFixtures() {

    @Test
    fun `Remove selected filter with Option's unique id`() {
        `Given view has load first page and get empty result`()

        val initialSearchParameter = searchShopViewModel.getSearchParameter()

        val selectedFilterOptionUniqueId = OptionHelper.constructUniqueId(SearchApiConst.OFFICIAL, "true", "Official Store")
        `When handle remove selected filter`(selectedFilterOptionUniqueId)

        `Then Search Parameter should not contain the selected filter anymore`()
        `Then Search Parameter should still contain other non-selected filter`(initialSearchParameter)
        `Then verify search shop and get dynamic filter API is called twice for load first page and remove filter`()
    }

    private fun `Given view has load first page and get empty result`() {
        searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyList)
        getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
        searchShopViewModel.onViewVisibilityChanged(isViewAdded = true, isViewVisible = true)
    }

    private fun `When handle remove selected filter`(uniqueId: String?) {
        searchShopViewModel.onViewRemoveSelectedFilterAfterEmptySearch(uniqueId)
    }

    private fun `Then Search Parameter should not contain the selected filter anymore`() {
        val searchParameter = searchShopViewModel.getSearchParameter()

        searchParameter shouldNotContain SearchApiConst.OFFICIAL
    }

    private fun `Then Search Parameter should still contain other non-selected filter`(initialSearchParameter: Map<String, Any>) {
        val searchParameter = searchShopViewModel.getSearchParameter()

        initialSearchParameter.filter { it.key != SearchApiConst.OFFICIAL }.forEach { (key, value) ->
            searchParameter[key] shouldBe value
        }
    }

    private fun `Then verify search shop and get dynamic filter API is called twice for load first page and remove filter`() {
        searchShopFirstPageUseCase.isExecuted(2)
        getDynamicFilterUseCase.isExecuted(2)
    }

    @Test
    fun `Remove selected filter with null unique id`() {
        `Given view has load first page and get empty result`()

        val initialSearchParameter = searchShopViewModel.getSearchParameter()

        `When handle remove selected filter`(null)

        `Then Search Parameter should not change`(initialSearchParameter)
    }

    private fun `Then Search Parameter should not change`(initialSearchParameter: Map<String, Any>) {
        val searchParameter = searchShopViewModel.getSearchParameter()

        searchParameter shouldBe initialSearchParameter
    }

    @Test
    fun `Remove selected option from filter with multiple options`() {
        `Given search shop view model with search parameter contains several location filter`()
        `Given view has load first page and get empty result`()

        val initialSearchParameter = searchShopViewModel.getSearchParameter()

        val selectedFilterOptionUniqueId = jakartaOption.uniqueId
        searchShopViewModel.onViewRemoveSelectedFilterAfterEmptySearch(selectedFilterOptionUniqueId)

        `Then Search Parameter should only contain the remaining location filter`()
        `Then Search Parameter should still contain other non-selected location filter`(initialSearchParameter)
        `Then verify search shop and get dynamic filter API is called twice for load first page and remove filter`()
    }

    private fun `Given search shop view model with search parameter contains several location filter`() {
        val searchShopParameterWithLocationFilter = mapOf<String, Any>(
                SearchApiConst.Q to "samsung",
                SearchApiConst.FCITY to "1#2#3"
        )

        searchShopViewModel = createSearchShopViewModel(searchShopParameterWithLocationFilter)
    }

    private fun `Then Search Parameter should only contain the remaining location filter`() {
        val searchParameter = searchShopViewModel.getSearchParameter()

        val remainingLocationFilter = searchParameter[SearchApiConst.FCITY].toString().split(OptionHelper.OPTION_SEPARATOR)

        remainingLocationFilter shouldNotContain "1"
        remainingLocationFilter shouldContain "2"
        remainingLocationFilter shouldContain "3"
    }

    private fun `Then Search Parameter should still contain other non-selected location filter`(initialSearchParameter: Map<String, Any>) {
        val searchParameter = searchShopViewModel.getSearchParameter()

        initialSearchParameter.filter { it.key != SearchApiConst.FCITY }.forEach { (key, value) ->
            searchParameter[key] shouldBe value
        }
    }

    @Test
    fun `Remove selected category filter option`() {
        `Given search shop view model with search parameter contains category filter`()
        `Given view has load first page and get empty result`()

        val initialSearchParameter = searchShopViewModel.getSearchParameter()

        val selectedFilterOptionUniqueId = handphoneOption.uniqueId
        `When handle remove selected filter`(selectedFilterOptionUniqueId)

        `Then Search Parameter should not contain any category filter anymore`()
        `Then Search Parameter should still contain other non-selected category filter`(initialSearchParameter)
        `Then verify search shop and get dynamic filter API is called twice for load first page and remove filter`()
    }

    private fun `Given search shop view model with search parameter contains category filter`() {
        val searchShopParameterWithCategoryFilter = mapOf<String, Any>(
                SearchApiConst.Q to "samsung",
                SearchApiConst.SC to "13,14,15"
        )

        searchShopViewModel = createSearchShopViewModel(searchShopParameterWithCategoryFilter)
    }

    private fun `Then Search Parameter should not contain any category filter anymore`() {
        val searchParameter = searchShopViewModel.getSearchParameter()

        searchParameter shouldNotContain SearchApiConst.SC
    }

    private fun `Then Search Parameter should still contain other non-selected category filter`(initialSearchParameter: Map<String, Any>) {
        val searchParameter = searchShopViewModel.getSearchParameter()

        initialSearchParameter.filter { it.key != SearchApiConst.SC }.forEach { (key, value) ->
            searchParameter[key] shouldBe value
        }
    }

    @Test
    fun `Remove selected price filter option`() {
        `Given search shop view model with search parameter contains Price filter min and max`()
        `Given view has load first page and get empty result`()

        val initialSearchParameter = searchShopViewModel.getSearchParameter()

        val selectedFilterOptionUniqueId = OptionHelper.constructUniqueId(SearchApiConst.PMIN, "", "Filter Harga")
        `When handle remove selected filter`(selectedFilterOptionUniqueId)

        `Then Search Parameter should not contain any price filter`()
        `Then Search Parameter should still contain other non-selected price filter`(initialSearchParameter)
        `Then verify search shop and get dynamic filter API is called twice for load first page and remove filter`()
    }

    private fun `Given search shop view model with search parameter contains Price filter min and max`() {
        val searchShopParameterWithPriceFilter = mapOf(
                SearchApiConst.Q to "samsung",
                SearchApiConst.PMIN to 1300,
                SearchApiConst.PMAX to 1000000
        )

        searchShopViewModel = createSearchShopViewModel(searchShopParameterWithPriceFilter)
    }

    private fun `Then Search Parameter should not contain any price filter`() {
        val searchParameter = searchShopViewModel.getSearchParameter()

        searchParameter shouldNotContain SearchApiConst.PMIN
        searchParameter shouldNotContain SearchApiConst.PMAX
    }

    private fun `Then Search Parameter should still contain other non-selected price filter`(initialSearchParameter: Map<String, Any>) {
        val searchParameter = searchShopViewModel.getSearchParameter()

        initialSearchParameter.filter { it.key != SearchApiConst.PMIN && it.key != SearchApiConst.PMAX }.forEach { (key, value) ->
            searchParameter[key] shouldBe value
        }
    }

    @Test
    fun `Remove selected filter but search shop is not empty search (edge cases)`() {
        val selectedFilterOptionUniqueId = OptionHelper.constructUniqueId(SearchApiConst.OFFICIAL, "true", "Official Store")

        `Given view has load first page and get result`()

        val initialSearchParameter = searchShopViewModel.getSearchParameter()
        `When handle remove selected filter`(selectedFilterOptionUniqueId)

        `Then Search Parameter should not change`(initialSearchParameter)
    }

    private fun `Given view has load first page and get result`() {
        searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
        getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
        searchShopViewModel.onViewVisibilityChanged(isViewAdded = true, isViewVisible = true)
    }
}