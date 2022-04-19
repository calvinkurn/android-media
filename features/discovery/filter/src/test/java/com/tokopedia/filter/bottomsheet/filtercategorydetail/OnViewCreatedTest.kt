package com.tokopedia.filter.bottomsheet.filtercategorydetail

import com.tokopedia.filter.bottomsheet.getCategoryFilter
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.testutils.jsonToObject
import org.junit.Test

internal class OnViewCreatedTest : FilterCategoryDetailViewModelTestFixtures() {

    @Test
    fun `onViewCreated after initialized with null category filter`() {
        `Given initialized view model`(null)

        `When view created`()

        `Then assert view does not show anything`()
    }

    @Test
    fun `onViewCreated after initialized with empty option category filter`() {
        val categoryFilter = "dynamic-filter-model-filter-no-option.json".jsonToObject<DynamicFilterModel>().getCategoryFilter()
        `Given initialized view model`(categoryFilter)

        `When view created`()

        `Then assert view does not show anything`()
    }

    private fun `Then assert view does not show anything`() {
        assert(headerViewModelList == null) {
            "Header view model list should be null"
        }

        assert(contentViewModelList == null) {
            "Content view model list should be null"
        }
    }

    @Test
    fun `onViewCreated after initialized with category filter`() {
        val categoryFilter = getCategoryFilter()
        `Given initialized view model`(categoryFilter)

        `When view created`()

        `Then assert header view model list`(categoryFilter)
        `Then assert filter category header view model is selected`(0)
        `Then assert content view model list`(categoryFilter.options[0])
        `Then assert button reset visibility`(false)
    }

    private fun `When view created`() {
        filterCategoryDetailViewModel.onViewCreated()
    }

    private fun `Then assert header view model list`(categoryFilter: Filter) {
        val headerViewModelList = this.headerViewModelList ?: throw AssertionError("Header view model should not be null")

        val expectedHeaderSize = categoryFilter.options.size
        val actualHeaderSize = headerViewModelList.size
        assert(actualHeaderSize == expectedHeaderSize) {
            "Header view model list size is $actualHeaderSize, expected is $expectedHeaderSize."
        }

        headerViewModelList.forEachIndexed { index, headerViewModel ->
            val expectedOption = categoryFilter.options[index]
            val actualOption = headerViewModel.option

            assert(actualOption == expectedOption) {
                "Header view model Option index $index is \"${actualOption.uniqueId}\", expected is \"${expectedOption.uniqueId}\"."
            }
        }
    }

    @Test
    fun `onViewCreated after initialized with selected level one category filter`() {
        val categoryFilter = getCategoryFilter()
        // Any position should be fine as long as it is selecting level one
        val selectedCategoryFilterPosition = 1
        val selectedCategoryFilterValue = categoryFilter.options[selectedCategoryFilterPosition].value

        `Test category filter selected header view model`(
                categoryFilter,
                selectedCategoryFilterValue,
                selectedCategoryFilterPosition,
                0,
                -1
        )

    }

    private fun `Test category filter selected header view model`(
            categoryFilter: Filter,
            selectedCategoryFilterValue: String,
            selectedCategoryFilterPosition: Int,
            selectedLevelTwoCategoryPosition: Int,
            selectedLevelThreeCategoryPosition: Int
    ) {
        `Given initialized view model`(categoryFilter, selectedCategoryFilterValue)

        `When view created`()

        `Then assert filter category header view model is selected`(selectedCategoryFilterPosition)
        `Then assert filter category content view model is selected`(selectedLevelTwoCategoryPosition, selectedLevelThreeCategoryPosition)
        `Then assert button reset visibility`(true)
    }

    private fun `Then assert filter category content view model is selected`(
            selectedLevelTwoCategoryPosition: Int, selectedLevelThreeCategoryPosition: Int
    ) {
        contentViewModelList!!.forEachIndexed { indexLevelTwo, levelTwoCategoryViewModel ->
            val expectedLevelTwoIsSelected = indexLevelTwo == selectedLevelTwoCategoryPosition
            levelTwoCategoryViewModel.assertIsSelected(expectedLevelTwoIsSelected, indexLevelTwo, selectedLevelThreeCategoryPosition)
        }
    }

    private fun FilterCategoryLevelTwoViewModel.assertIsSelected(
            expectedLevelTwoIsSelected: Boolean, indexLevelTwo: Int, selectedLevelThreeCategoryPosition: Int
    ) {
        val actualLevelTwoIsSelected = isSelectedOrExpanded
        assert(actualLevelTwoIsSelected == expectedLevelTwoIsSelected) {
            "Level Two Category view model index $indexLevelTwo isSelected should be $expectedLevelTwoIsSelected."
        }

        levelThreeCategoryViewModelList.forEachIndexed { indexLevelThree, levelThreeCategoryViewModel ->
            val expectedLevelThreeIsSelected = expectedLevelTwoIsSelected && indexLevelThree == selectedLevelThreeCategoryPosition
            levelThreeCategoryViewModel.assertIsSelected(expectedLevelThreeIsSelected, indexLevelThree)
        }
    }

    private fun FilterCategoryLevelThreeViewModel.assertIsSelected(expectedLevelThreeIsSelected: Boolean, indexLevelThree: Int) {
        val actualLevelThreeIsSelected = isSelected
        assert(actualLevelThreeIsSelected == expectedLevelThreeIsSelected) {
            "Level Three Category view model index $indexLevelThree isSelected should be $expectedLevelThreeIsSelected."
        }
    }

    @Test
    fun `onViewCreated after initialized with selected level two category filter`() {
        val categoryFilter = getCategoryFilter()
        // Any position should be fine as long as it is selecting level two
        val selectedCategoryFilterPosition = 0
        val selectedLevelTwoCategoryPosition = 3
        val selectedCategoryFilterValue =
                categoryFilter.options[selectedCategoryFilterPosition]
                        .levelTwoCategoryList[selectedLevelTwoCategoryPosition].value

        `Test category filter selected header view model`(
                categoryFilter,
                selectedCategoryFilterValue,
                selectedCategoryFilterPosition,
                selectedLevelTwoCategoryPosition,
                0
        )
    }

    @Test
    fun `onViewCreated after initialized with selected level three category filter`() {
        val categoryFilter = getCategoryFilter()
        // Any position should be fine as long as it is selecting level three
        val selectedCategoryFilterPosition = 0
        val selectedLevelTwoCategoryPosition = 3
        val selectedLevelThreeCategoryPosition = 2
        val selectedCategoryFilterValue =
                categoryFilter.options[selectedCategoryFilterPosition]
                        .levelTwoCategoryList[selectedLevelTwoCategoryPosition]
                        .levelThreeCategoryList[selectedLevelThreeCategoryPosition].value

        `Test category filter selected header view model`(
                categoryFilter,
                selectedCategoryFilterValue,
                selectedCategoryFilterPosition,
                selectedLevelTwoCategoryPosition,
                selectedLevelThreeCategoryPosition
        )
    }
}