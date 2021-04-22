package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.filter.common.data.Option
import com.tokopedia.search.result.isExecuted
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.presentation.viewmodel.SearchShopViewModel.QuickFilterTrackingData
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.searchShopQuickFilterModel
import com.tokopedia.search.shouldBe
import org.junit.Test

internal class SearchShopClickQuickFilterTest: SearchShopDataViewTestFixtures() {

    @Test
    fun `Click to Apply Quick Filter`() {
        `Given search shop API call will be successful`()
        `Given search shop view is visible and added`()

        val clickedOptionIndex = 1
        val clickedOption = searchShopQuickFilterModel.getOptionList()[clickedOptionIndex]
        `When click quick filter`(clickedOptionIndex)

        `Then assert search parameter contains the clicked option`(clickedOption)
        `Then assert search shop and get dynamic filter is executed twice`()
        `Then assert click quick filter tracking data`(QuickFilterTrackingData(clickedOption, true))
    }

    private fun `Given search shop view is visible and added`() {
        searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
    }

    private fun SearchShopModel.FilterSort.getOptionList() = data.filter.map { it.options }.flatten()

    private fun `When click quick filter`(index: Int) {
        val sortFilterItemList = searchShopViewModel.getSortFilterItemListLiveData().value
                ?: throw AssertionError("Sort Filter Item List is null")

        sortFilterItemList[index].listener()
    }

    private fun `Then assert search parameter contains the clicked option`(clickedOption: Option) {
        val searchParameter = searchShopViewModel.getSearchParameter()
        searchParameter[clickedOption.key] shouldBe clickedOption.value
    }

    private fun `Then assert search shop and get dynamic filter is executed twice`() {
        searchShopFirstPageUseCase.isExecuted(2)
        getDynamicFilterUseCase.isExecuted(2)
    }

    private fun `Then assert click quick filter tracking data`(quickFilterTrackingData: QuickFilterTrackingData?) {
        searchShopViewModel.getClickQuickFilterTrackingEventLiveData().value?.getContentIfNotHandled() shouldBe quickFilterTrackingData
    }

    @Test
    fun `Click to un-apply Quick Filter`() {
        `Given search shop API call will be successful`()
        `Given search shop view is visible and added`()

        val clickedOptionIndex = 0
        val clickedOption = searchShopQuickFilterModel.getOptionList()[clickedOptionIndex]
        `When click quick filter`(clickedOptionIndex)

        `Then assert search parameter does not contain the clicked option`(clickedOption)
        `Then assert search shop and get dynamic filter is executed twice`()
        `Then assert click quick filter tracking data`(QuickFilterTrackingData(clickedOption, false))
    }

    private fun `Then assert search parameter does not contain the clicked option`(clickedOption: Option) {
        val searchParameter = searchShopViewModel.getSearchParameter()
        searchParameter[clickedOption.key] shouldBe null
    }
}