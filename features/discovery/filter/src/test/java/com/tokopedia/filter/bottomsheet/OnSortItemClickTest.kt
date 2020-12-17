package com.tokopedia.filter.bottomsheet

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.bottomsheet.sort.SortItemViewModel
import com.tokopedia.filter.bottomsheet.sort.SortViewModel
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.helper.toMapParam
import com.tokopedia.filter.testutils.jsonToObject
import org.junit.Test

internal class OnSortItemClickTest: SortFilterBottomSheetViewModelTestFixtures() {

    @Test
    fun `onSortItemClicked with given SortItemViewModel to apply sort`() {
        val mapParameter = createMapParameter()
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        `Given SortFilterBottomSheet view is already created`(mapParameter, dynamicFilterModel)

        val clickedSortItemViewModel = this.sortFilterList!!.getAnyUnselectedSort()
        `When a Sort Item is clicked and applied`(clickedSortItemViewModel)

        val expectedMapParameter = mapParameter.toMutableMap().also {
            it[clickedSortItemViewModel.sort.key] = clickedSortItemViewModel.sort.value
            it[SearchApiConst.ORIGIN_FILTER] = SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_SORT_PAGE
        }

        `Then assert sort item click`(
                expectedMapParameter,
                mapOf(clickedSortItemViewModel.sort.key to clickedSortItemViewModel.sort.value),
                clickedSortItemViewModel.sort.name,
                clickedSortItemViewModel
        )
    }

    private fun `When a Sort Item is clicked and applied`(sortItemViewModel: SortItemViewModel) {
        sortFilterBottomSheetViewModel.onSortItemClick(sortItemViewModel)
        sortFilterBottomSheetViewModel.applySortFilter()
    }

    private fun `Then assert sort item click`(
            expectedMapParameter: Map<String, String>,
            expectedSelectedSortMap: Map<String, String>,
            expectedSelectedSortName: String,
            clickedSortItemViewModel: SortItemViewModel,
            expectedResetButtonVisibility: Boolean = true,
            expectedIsButtonApplyVisible: Boolean = true
    ) {
        `Then assert map parameter is as expected`(expectedMapParameter)
        `Then assert selected sort map is as expected`(expectedSelectedSortMap)
        `Then assert selected sort name`(expectedSelectedSortName)
        `Then assert UI changes for apply sort`(clickedSortItemViewModel, expectedResetButtonVisibility, expectedIsButtonApplyVisible)
        `Then assert filter view is expanded`()
    }

    private fun `Then assert UI changes for apply sort`(
            clickedSortItemViewModel: SortItemViewModel,
            expectedResetButtonVisibility: Boolean = true,
            expectedIsButtonApplyVisible: Boolean = true
    ) {
        `Then assert only the clicked sort item view model is selected`(clickedSortItemViewModel)
        `Then assert sort filter view is updated`(0)
        `Then assert button apply visibility`(expectedIsButtonApplyVisible)
        `Then assert button reset visibility`(expectedResetButtonVisibility)
    }

    private fun `Then assert only the clicked sort item view model is selected`(sortItemViewModel: SortItemViewModel) {
        assert(sortItemViewModel.isSelected) {
            "Sort Item View Model \"${sortItemViewModel.sort.name}\" should be selected."
        }

        this.sortFilterList!!.findAndReturn<SortViewModel>()!!.sortItemViewModelList.forEach {
            if (it != sortItemViewModel) {
                assert(!it.isSelected) {
                    "Sort Item View Model \"${it.sort.name}\" should NOT be selected."
                }
            }
        }
    }

    @Test
    fun `onSortItemClicked with given SortItemViewModel to apply sort and apply filter from sort`() {
        val mapParameter = createMapParameter()
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        `Given SortFilterBottomSheet view is already created`(mapParameter, dynamicFilterModel)

        val clickedSortItemViewModel = this.sortFilterList!!.getUnselectedSortWithApplyFilter()
        `When a Sort Item is clicked and applied`(clickedSortItemViewModel)

        val expectedMapParameter = mapParameter.toMutableMap().also {
            it[clickedSortItemViewModel.sort.key] = clickedSortItemViewModel.sort.value
            it.putAll(clickedSortItemViewModel.sort.applyFilter.toMapParam())
        }

        `Then assert sort item click`(
                expectedMapParameter,
                mapOf(clickedSortItemViewModel.sort.key to clickedSortItemViewModel.sort.value),
                clickedSortItemViewModel.sort.name,
                clickedSortItemViewModel
        )
    }

    @Test
    fun `onSortItemClicked for sort with apply filter and then sort without apply filter`() {
        val mapParameter = createMapParameter()
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        `Given SortFilterBottomSheet view is already created`(mapParameter, dynamicFilterModel)

        val clickedSortItemViewModelWithApplyFilter = this.sortFilterList!!.getUnselectedSortWithApplyFilter()
        sortFilterBottomSheetViewModel.onSortItemClick(clickedSortItemViewModelWithApplyFilter)

        val clickedSortItemViewModelWithoutApplyFilter = this.sortFilterList!!.getAnyUnselectedSort()
        sortFilterBottomSheetViewModel.onSortItemClick(clickedSortItemViewModelWithoutApplyFilter)

        sortFilterBottomSheetViewModel.applySortFilter()

        val expectedMapParameter = mapParameter.toMutableMap().also {
            it[clickedSortItemViewModelWithoutApplyFilter.sort.key] = clickedSortItemViewModelWithoutApplyFilter.sort.value
            it[SearchApiConst.ORIGIN_FILTER] = SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_SORT_PAGE
        }

        `Then assert sort item click`(
                expectedMapParameter,
                mapOf(clickedSortItemViewModelWithoutApplyFilter.sort.key to clickedSortItemViewModelWithoutApplyFilter.sort.value),
                clickedSortItemViewModelWithoutApplyFilter.sort.name,
                clickedSortItemViewModelWithoutApplyFilter,
                expectedIsButtonApplyVisible = mapParameter[SearchApiConst.OB] != clickedSortItemViewModelWithoutApplyFilter.sort.value
        )
    }

    @Test
    fun `onSortItemClicked to default sort should not show reset button`() {
        val mapParameter = mapOf(SearchApiConst.OB to "3")
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
                .apply { defaultSortValue = "9" }

        `Given SortFilterBottomSheet view is already created`(mapParameter, dynamicFilterModel)

        val clickedDefaultSortViewModel = this.sortFilterList!!.findAndReturn<SortViewModel>()!!.sortItemViewModelList.find {
            it.sort.value == dynamicFilterModel.defaultSortValue
        }!!

        sortFilterBottomSheetViewModel.onSortItemClick(clickedDefaultSortViewModel)
        sortFilterBottomSheetViewModel.applySortFilter()

        val expectedMapParameter = mapParameter.toMutableMap().also {
            it[clickedDefaultSortViewModel.sort.key] = clickedDefaultSortViewModel.sort.value
            it[SearchApiConst.ORIGIN_FILTER] = SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_SORT_PAGE
        }

        `Then assert sort item click`(
                expectedMapParameter,
                mapOf(clickedDefaultSortViewModel.sort.key to clickedDefaultSortViewModel.sort.value),
                clickedDefaultSortViewModel.sort.name,
                clickedDefaultSortViewModel,
                expectedResetButtonVisibility = false
        )
    }

    @Test
    fun `onSortItemClicked to return to initial sort value should not show button apply`() {
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        val originalSortValue = dynamicFilterModel.data.sort.find { it.value != dynamicFilterModel.defaultSortValue }!!.value
        val mapParameter = mapOf(SearchApiConst.Q to "samsung", SearchApiConst.OB to originalSortValue)
        `Given SortFilterBottomSheet view is already created`(mapParameter, dynamicFilterModel)

        val clickedSortItemViewModel = this.sortFilterList!!.getAnyUnselectedSort()
        sortFilterBottomSheetViewModel.onSortItemClick(clickedSortItemViewModel)

        val originalSortItemViewModel = this.sortFilterList!!.findAndReturn<SortViewModel>()!!.sortItemViewModelList.find {
            it.sort.value == originalSortValue
        }!!
        sortFilterBottomSheetViewModel.onSortItemClick(originalSortItemViewModel)

        val expectedMapParameter = mapParameter.toMutableMap().also {
            it[clickedSortItemViewModel.sort.key] = originalSortItemViewModel.sort.value
            it[SearchApiConst.ORIGIN_FILTER] = SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_SORT_PAGE
        }

        `Then assert sort item click`(
                expectedMapParameter,
                mapOf(originalSortItemViewModel.sort.key to originalSortItemViewModel.sort.value),
                originalSortItemViewModel.sort.name,
                originalSortItemViewModel,
                expectedResetButtonVisibility = true,
                expectedIsButtonApplyVisible = false
        )
    }

    @Test
    fun `onSortItemClicked twice with same SortItemViewModel should revert to default sort`() {
        val mapParameter = mapOf(SearchApiConst.Q to "samsung")
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
                .apply { defaultSortValue = "9" }

        `Given SortFilterBottomSheet view is already created`(mapParameter, dynamicFilterModel)

        `When click same sort item twice`()

        val defaultSortItemViewModel = this.sortFilterList!!.findAndReturn<SortViewModel>()!!.sortItemViewModelList.find {
            it.sort.value == dynamicFilterModel.defaultSortValue
        }!!

        val expectedMapParameter = mapParameter.toMutableMap().also {
            it[defaultSortItemViewModel.sort.key] = defaultSortItemViewModel.sort.value
            it[SearchApiConst.ORIGIN_FILTER] = SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_SORT_PAGE
        }

        `Then assert sort item click`(
                expectedMapParameter,
                mapOf(defaultSortItemViewModel.sort.key to defaultSortItemViewModel.sort.value),
                defaultSortItemViewModel.sort.name,
                defaultSortItemViewModel,
                expectedResetButtonVisibility = false,
                expectedIsButtonApplyVisible = false
        )
    }

    private fun `When click same sort item twice`() {
        val clickedSortItemViewModel = this.sortFilterList!!.getAnyUnselectedSort()

        sortFilterBottomSheetViewModel.onSortItemClick(clickedSortItemViewModel)
        sortFilterBottomSheetViewModel.onSortItemClick(clickedSortItemViewModel)
    }
}