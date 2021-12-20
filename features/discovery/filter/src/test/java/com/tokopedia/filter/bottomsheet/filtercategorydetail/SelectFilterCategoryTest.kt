package com.tokopedia.filter.bottomsheet.filtercategorydetail

import com.tokopedia.filter.bottomsheet.getCategoryFilter
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.testutils.jsonToObject
import org.junit.Test

internal class SelectFilterCategoryTest: FilterCategoryDetailViewModelTestFixtures() {

    @Test
    fun `Level Three Filter Category Selected`() {
        `Given view model initialized and view created`(getCategoryFilter(), "")

        val clickedLevelThreeViewModel = contentViewModelList!![1].levelThreeCategoryViewModelList.find { !it.isSelected }!!
        `When Category Level Three Selected`(clickedLevelThreeViewModel, true)

        val expectedPositionToUpdate = listOf(1)
        `Then assert click to select level three view model`(clickedLevelThreeViewModel, true, expectedPositionToUpdate)
    }

    private fun `When Category Level Three Selected`(clickedLevelThreeViewModel: FilterCategoryLevelThreeViewModel, isChecked: Boolean) {
        filterCategoryDetailViewModel.onFilterCategoryClicked(clickedLevelThreeViewModel)
    }

    private fun `Then assert click to select level three view model`(
            clickedLevelThreeViewModel: FilterCategoryLevelThreeViewModel,
            isChecked: Boolean,
            expectedContentUpdatedPosition: List<Int>
    ) {
        `Then assert selected filter value is updated`(clickedLevelThreeViewModel.levelThreeCategory.value)
        `Then assert clicked level three selected state`(clickedLevelThreeViewModel, isChecked)
        `Then assert other level three view model is not selected`(clickedLevelThreeViewModel)
        `Then assert content update position`(expectedContentUpdatedPosition)
        `Then assert button save visibility`(true)
        `Then assert button reset visibility`(true)
    }

    private fun `Then assert selected filter value is updated`(expectedSelectedCategoryFilterValue: String) {
        assert(filterCategoryDetailViewModel.selectedCategoryFilterValue == expectedSelectedCategoryFilterValue) {
            "Selected Category filter value should be \"$expectedSelectedCategoryFilterValue\"."
        }
    }

    private fun `Then assert clicked level three selected state`(clickedLevelThreeViewModel: FilterCategoryLevelThreeViewModel, isChecked: Boolean) {
        assert(clickedLevelThreeViewModel.isSelected == isChecked) {
            "Clicked Level Three View Model ${clickedLevelThreeViewModel.levelThreeCategory.name} should be $isChecked."
        }
    }

    private fun `Then assert other level three view model is not selected`(clickedLevelThreeViewModel: FilterCategoryLevelThreeViewModel) {
        contentViewModelList!!.forEach { levelTwoViewModel ->
            levelTwoViewModel.assertNotSelectedLevelThree(clickedLevelThreeViewModel)
        }
    }

    private fun FilterCategoryLevelTwoViewModel.assertNotSelectedLevelThree(clickedLevelThreeViewModel: FilterCategoryLevelThreeViewModel) {
        levelThreeCategoryViewModelList.forEach { levelThreeViewModel ->
            if (levelThreeViewModel != clickedLevelThreeViewModel) {
                assert(!levelThreeViewModel.isSelected) {
                    "Level Three Category View Model ${levelThreeViewModel.levelThreeCategory.name} is selected should be false."
                }
            }
        }
    }

    private fun `Then assert content update position`(expectedUpdatedPosition: List<Int>) {
        assert(expectedUpdatedPosition == updateContentInPosition) {
            "Content position updated: $updateContentInPosition, expected: $expectedUpdatedPosition."
        }
    }

    private fun `Then assert button save visibility`(expectedIsVisible: Boolean) {
        assert(filterCategoryDetailViewModel.isButtonSaveVisibleLiveData.value == expectedIsVisible) {
            "Button Save should be visible"
        }
    }

    @Test
    fun `Level Three Filter Category Selected and then replaced with another`() {
        `Given view model initialized and view created`(getCategoryFilter(), "")

        val clickedLevelThreeViewModel = contentViewModelList!![1].levelThreeCategoryViewModelList.find { !it.isSelected }!!
        `When Category Level Three Selected`(clickedLevelThreeViewModel, true)

        val anotherClickedLevelThreeViewModel = contentViewModelList!![3].levelThreeCategoryViewModelList.find { !it.isSelected }!!
        `When Category Level Three Selected`(anotherClickedLevelThreeViewModel, true)

        val expectedContentUpdatedPosition = listOf(1, 1, 3)
        `Then assert click to select level three view model`(anotherClickedLevelThreeViewModel, true, expectedContentUpdatedPosition)
    }

    @Test
    fun `Level Two Filter Category Selected`() {
        `Given view model initialized and view created`(getCategoryFilter(), "")

        val clickedLevelTwoViewModel = contentViewModelList!![0]
        `When Category Level Two Clicked`(clickedLevelTwoViewModel, true)

        `Then assert Level Two Filter Category Selected`(clickedLevelTwoViewModel, listOf(0))
    }

    private fun `When Category Level Two Clicked`(clickedFilterCategoryLevelTwoViewModel: FilterCategoryLevelTwoViewModel, isChecked: Boolean) {
        filterCategoryDetailViewModel.onFilterCategoryClicked(clickedFilterCategoryLevelTwoViewModel)
    }

    private fun `Then assert Level Two Filter Category Selected`(clickedLevelTwoViewModel: FilterCategoryLevelTwoViewModel, expectedContentUpdatedPosition: List<Int>) {
        `Then assert selected filter value is updated`(clickedLevelTwoViewModel.levelTwoCategory.value)
        `Then assert clicked level two selected state`(clickedLevelTwoViewModel, true)
        `Then assert other selectable level two is not selected`(clickedLevelTwoViewModel)
        `Then assert content update position`(expectedContentUpdatedPosition)
        `Then assert button save visibility`(true)
        `Then assert button reset visibility`(true)
    }

    private fun `Then assert clicked level two selected state`(clickedLevelTwoViewModel: FilterCategoryLevelTwoViewModel, isChecked: Boolean) {
        assert(clickedLevelTwoViewModel.isSelectedOrExpanded == isChecked) {
            "Clicked Level Two View Model ${clickedLevelTwoViewModel.levelTwoCategory.name} should be $isChecked."
        }
    }

    private fun `Then assert other selectable level two is not selected`(clickedLevelTwoViewModel: FilterCategoryLevelTwoViewModel) {
        contentViewModelList!!.forEach { levelTwoViewModel ->
            levelTwoViewModel.assertNotSelected(clickedLevelTwoViewModel)
        }
    }

    private fun FilterCategoryLevelTwoViewModel.assertNotSelected(clickedLevelTwoViewModel: FilterCategoryLevelTwoViewModel) {
        if (isSelectable && this != clickedLevelTwoViewModel) {
            assert(!isSelectedOrExpanded) {
                "Selectable Level Two Category View Model ${levelTwoCategory.name} is selected should be false."
            }
        }
    }

    @Test
    fun `Level Two Filter Category Selected and then replaced with another`() {
        val categoryFilterMultipleLevelTwo = "dynamic-filter-model-multiple-filter-level-two-category.json".jsonToObject<DynamicFilterModel>().getCategoryFilter()
        `Given view model initialized and view created`(categoryFilterMultipleLevelTwo, "")

        val clickedLevelTwoViewModel = contentViewModelList!![0]
        `When Category Level Two Clicked`(clickedLevelTwoViewModel, true)

        val anotherClickedLevelTwoViewModel = contentViewModelList!![1]
        `When Category Level Two Clicked`(anotherClickedLevelTwoViewModel, true)

        val expectedUpdatedPosition = listOf(0, 0, 1)
        `Then assert Level Two Filter Category Selected`(anotherClickedLevelTwoViewModel, expectedUpdatedPosition)
    }

    @Test
    fun `Click Level Two and then Level Three Filter Category`() {
        `Given view model initialized and view created`(getCategoryFilter(), "")

        val clickedLevelTwoViewModel = contentViewModelList!![0]
        `When Category Level Two Clicked`(clickedLevelTwoViewModel, true)

        val clickedLevelThreeViewModel = contentViewModelList!![1].levelThreeCategoryViewModelList[0]
        `When Category Level Three Selected`(clickedLevelThreeViewModel, true)

        `Then assert selected filter value is updated`(clickedLevelThreeViewModel.levelThreeCategory.value)
        `Then assert clicked level two selected state`(clickedLevelTwoViewModel, false)
        `Then assert clicked level three selected state`(clickedLevelThreeViewModel, true)
        `Then assert no other selectable level two or level three filter category is selected`(clickedLevelTwoViewModel, clickedLevelThreeViewModel)
        `Then assert content update position`(listOf(0, 0, 1))
        `Then assert button save visibility`(true)
        `Then assert button reset visibility`(true)
    }

    private fun `Then assert no other selectable level two or level three filter category is selected`(
            clickedLevelTwoViewModel: FilterCategoryLevelTwoViewModel,
            clickedLevelThreeViewModel: FilterCategoryLevelThreeViewModel
    ) {
        contentViewModelList!!.forEach { levelTwoViewModel ->
            levelTwoViewModel.assertNotSelected(clickedLevelTwoViewModel)
            levelTwoViewModel.assertNotSelectedLevelThree(clickedLevelThreeViewModel)
        }
    }

    @Test
    fun `Click Level Three and then Level Two Filter Category`() {
        `Given view model initialized and view created`(getCategoryFilter(), "")

        val clickedLevelThreeViewModel = contentViewModelList!![1].levelThreeCategoryViewModelList[0]
        `When Category Level Three Selected`(clickedLevelThreeViewModel, true)

        val clickedLevelTwoViewModel = contentViewModelList!![0]
        `When Category Level Two Clicked`(clickedLevelTwoViewModel, true)

        `Then assert selected filter value is updated`(clickedLevelTwoViewModel.levelTwoCategory.value)
        `Then assert clicked level two selected state`(clickedLevelTwoViewModel, true)
        `Then assert clicked level three selected state`(clickedLevelThreeViewModel, false)
        `Then assert no other selectable level two or level three filter category is selected`(clickedLevelTwoViewModel, clickedLevelThreeViewModel)
        `Then assert content update position`(listOf(1, 1, 0))
        `Then assert button save visibility`(true)
        `Then assert button reset visibility`(true)
    }
}