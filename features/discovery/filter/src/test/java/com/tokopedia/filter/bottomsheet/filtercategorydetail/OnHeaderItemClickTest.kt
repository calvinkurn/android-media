package com.tokopedia.filter.bottomsheet.filtercategorydetail

import org.junit.Test

internal class OnHeaderItemClickTest: FilterCategoryDetailViewModelTestFixtures() {

    @Test
    fun `onHeaderItemClick without any category filter selected`() {
        `Given view model initialized and view created`(getCategoryFilter())

        val clickedHeaderItemPosition = 1 // Any position is fine
        val clickedHeaderItem = headerViewModelList!![clickedHeaderItemPosition]
        `When header item clicked`(clickedHeaderItem)

        `Then assert filter category header view model is selected`(clickedHeaderItemPosition)
        `Then assert content view model list`(clickedHeaderItem.option)
        // position 0 is selected by default, so it should be updated to become unselected
        `Then assert update header item position`(listOf(0, clickedHeaderItemPosition))
    }

    private fun `When header item clicked`(clickedHeaderItem: FilterCategoryLevelOneViewModel) {
        filterCategoryDetailViewModel.onHeaderItemClick(clickedHeaderItem)
    }

    private fun `Then assert update header item position`(expectedUpdatedHeaderItemList: List<Int>) {
        val expectedUpdatedHeaderItemCount = expectedUpdatedHeaderItemList.size
        assert(updatedHeaderPosition.size == expectedUpdatedHeaderItemCount) {
            "Actual updated ${updatedHeaderPosition.size} header item. Expected update $expectedUpdatedHeaderItemCount header item."
        }

        expectedUpdatedHeaderItemList.forEach {
            assert(updatedHeaderPosition.contains(it)) {
                "Header item Position $it should be updated"
            }
        }
    }

    @Test
    fun `onHeaderItemClick with category filter selected`() {
        val categoryFilter = getCategoryFilter()
        val existingCategoryFilterSelectedPosition = 3 // Any existing selected position is fine
        val existingSelectedCategoryFilter = categoryFilter.options[existingCategoryFilterSelectedPosition]
        `Given view model initialized and view created`(getCategoryFilter(), existingSelectedCategoryFilter.value)

        val clickedHeaderItemPosition = 1 // Any position is fine
        val clickedHeaderItem = headerViewModelList!![clickedHeaderItemPosition]
        `When header item clicked`(clickedHeaderItem)

        `Then assert filter category header view model is selected`(clickedHeaderItemPosition)
        `Then assert content view model list`(clickedHeaderItem.option)
        `Then assert update header item position`(listOf(clickedHeaderItemPosition, existingCategoryFilterSelectedPosition))
    }

    @Test
    fun `onHeaderItemClick re-selecting category filter header`() {
        val categoryFilter = getCategoryFilter()
        val existingCategoryFilterSelectedPosition = 1 // Any existing selected position is fine
        val existingSelectedCategoryFilter = categoryFilter.options[existingCategoryFilterSelectedPosition]
        `Given view model initialized and view created`(getCategoryFilter(), existingSelectedCategoryFilter.value)

        val clickedHeaderItem = headerViewModelList!![existingCategoryFilterSelectedPosition]
        `When header item clicked`(clickedHeaderItem)

        `Then assert filter category header view model is selected`(existingCategoryFilterSelectedPosition)
        `Then assert content view model list`(clickedHeaderItem.option)
        `Then assert update header item position`(listOf())
    }
}