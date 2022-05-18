package com.tokopedia.filter.bottomsheet.filtercategorydetail

import org.junit.Test

internal class ResetFilterCategoryTest: FilterCategoryDetailViewModelTestFixtures() {
    @Test
    fun `onResetCategoryFilter with category filter selected`() {
        val categoryFilter = getCategoryFilter()
        val existingCategoryFilterSelectedPosition = 1 // Any existing selected position is fine
        val existingSelectedCategoryFilter = categoryFilter.options[existingCategoryFilterSelectedPosition]
        `Given view model initialized and view created`(getCategoryFilter(), existingSelectedCategoryFilter.value)

        `When Reset Button Clicked`()

        `Then assert selected filter value is empty`()
        `Then assert button reset visibility`(false)
        `Then assert button save visibility`(true)
    }

    @Test
    fun `Reset Selected Level Two Category Filter`() {
        `Given view model initialized and view created`(getCategoryFilter(), "")

        val clickedLevelTwoViewModel = contentViewModelList!![0]
        `When Category Level Two Clicked`(clickedLevelTwoViewModel)
        `When Reset Button Clicked`()

        `Then assert selected filter value is empty`()
        `Then assert clicked level two not selected`(clickedLevelTwoViewModel)
        `Then assert other selectable level two is not selected`()
        `Then assert content update position`(listOf(0,0))
        `Then assert button save visibility`(false)
        `Then assert button reset visibility`(false)
    }

    private fun `When Category Level Two Clicked`(clickedFilterCategoryLevelTwoViewModel: FilterCategoryLevelTwoViewModel) {
        filterCategoryDetailViewModel.onFilterCategoryClicked(clickedFilterCategoryLevelTwoViewModel)
    }

    private fun `Then assert clicked level two not selected`(clickedLevelTwoViewModel: FilterCategoryLevelTwoViewModel) {
        assert(!clickedLevelTwoViewModel.isSelectedOrExpanded) {
            "Clicked Level Two View Model ${clickedLevelTwoViewModel.levelTwoCategory.name} should be false."
        }
    }

    private fun `Then assert other selectable level two is not selected`() {
        contentViewModelList!!.forEach { levelTwoViewModel ->
            levelTwoViewModel.assertNotSelected()
        }
    }

    private fun FilterCategoryLevelTwoViewModel.assertNotSelected() {
        if (isSelectable) {
            assert(!isSelectedOrExpanded) {
                "Selectable Level Two Category View Model ${levelTwoCategory.name} is selected should be false."
            }
        }
    }

    private fun `When Reset Button Clicked`() {
        filterCategoryDetailViewModel.onResetButtonClicked()
    }

    @Test
    fun `Select Level Three Category Filter then click reset button`() {
        `Given view model initialized and view created`(getCategoryFilter(), "")

        val clickedLevelThreeViewModel = contentViewModelList!![1].levelThreeCategoryViewModelList.find { !it.isSelected }!!
        `When Category Level Three Selected`(clickedLevelThreeViewModel)
        `When Reset Button Clicked`()

        `Then assert selected filter value is empty`()
        `Then assert clicked level three not selected`(clickedLevelThreeViewModel)
        `Then assert content update position`(listOf(1, 1))
        `Then assert button save visibility`(false)
        `Then assert button reset visibility`(false)
    }

    private fun `When Category Level Three Selected`(clickedLevelThreeViewModel: FilterCategoryLevelThreeViewModel) {
        filterCategoryDetailViewModel.onFilterCategoryClicked(clickedLevelThreeViewModel)
    }

    private fun `Then assert selected filter value is empty`() {
        assert(filterCategoryDetailViewModel.selectedCategoryFilterValue == "") {
            "Selected Category filter value should be \"\"."
        }
    }

    private fun `Then assert clicked level three not selected`(clickedLevelThreeViewModel: FilterCategoryLevelThreeViewModel) {
        assert(!clickedLevelThreeViewModel.isSelected) {
            "Clicked Level Three View Model ${clickedLevelThreeViewModel.levelThreeCategory.name} should be false."
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
}