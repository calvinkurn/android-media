package com.tokopedia.filter.bottomsheet

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.bottomsheet.filter.FilterViewModel
import com.tokopedia.filter.bottomsheet.filter.OptionViewModel
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Option
import org.junit.After
import org.junit.Before
import org.junit.Rule

internal abstract class SortFilterBottomSheetViewModelTestFixtures {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    protected val sortFilterBottomSheetViewModel = SortFilterBottomSheetViewModel()

    protected var sortFilterList: List<Visitable<*>>? = null
    private val sortFilterListObserver = Observer<List<Visitable<*>>> {
        sortFilterList = it
    }

    @Before
    fun setUp() {
        sortFilterBottomSheetViewModel.sortFilterListLiveData.observeForever(sortFilterListObserver)
    }

    @After
    fun tearDown() {
        sortFilterBottomSheetViewModel.sortFilterListLiveData.removeObserver(sortFilterListObserver)
    }

    protected fun `Then assert option view model list selected state`(
            filterViewModel: FilterViewModel,
            selectedOptionList: List<Option>,
            expectedOptionViewModelListSize: Int
    ) {
        selectedOptionList.forEach { expectedOption ->
            assert(filterViewModel.optionViewModelList.any { it.option == expectedOption }) {
                "Option view model list should contain option \"${expectedOption.uniqueId}\"."
            }
        }

        val actualOptionViewModelListSize = filterViewModel.optionViewModelList.size
        assert(actualOptionViewModelListSize == expectedOptionViewModelListSize) {
            "Option View Model List size is $actualOptionViewModelListSize, expected size is $expectedOptionViewModelListSize."
        }

        filterViewModel.optionViewModelList.forEach {
            `Then assert option selected state`(it, selectedOptionList.contains(it.option))
        }
    }

    protected fun `Then assert option selected state`(optionViewModel: OptionViewModel, expectedIsSelected: Boolean) {
        assert(optionViewModel.option.inputState.toBoolean() == expectedIsSelected) {
            "Option ${optionViewModel.option.name} inputState should be $expectedIsSelected"
        }

        assert(optionViewModel.isSelected == expectedIsSelected) {
            "Option ${optionViewModel.option.name} isSelected should be $expectedIsSelected"
        }
    }

    protected fun `Then assert button reset visibility`(expectedIsVisible: Boolean) {
        val actualButtonResetIsVisible = sortFilterBottomSheetViewModel.isButtonResetVisibleLiveData.value

        assert(actualButtonResetIsVisible == expectedIsVisible) {
            "Button reset isVisible should be $expectedIsVisible"
        }
    }

    protected fun `Then assert option is sorted`(filterViewModel: FilterViewModel) {
        assert(filterViewModel.isSelectedOptionSorted()) {
            "Filter ${filterViewModel.filter.title} Option's should be sorted based on selected and name"
        }
    }

    protected fun createMapParameter(): Map<String, String> = mutableMapOf<String, String>().also {
        it[SearchApiConst.Q] = "samsung"
        it[SearchApiConst.OFFICIAL] = true.toString() // Filter Official Store
        it[SearchApiConst.OB] = 23.toString() // Sorted by Best Match
        it[SearchApiConst.FCITY] = "174,175,176,177,178,179#165" // Filter by DKI Jakarta and Bandung
    }

    protected fun `Given SortFilterBottomSheet view is already created`(
            mapParameter: Map<String, String>, dynamicFilterModel: DynamicFilterModel
    ) {
        sortFilterBottomSheetViewModel.init(mapParameter, dynamicFilterModel)
        sortFilterBottomSheetViewModel.onViewCreated()
    }

    protected fun `Then assert sort filter view is updated`(sortFilterViewPosition: Int?) {
        val updateViewInPosition = sortFilterBottomSheetViewModel.updateViewInPositionEventLiveData.value?.getContentIfNotHandled()

        assert(updateViewInPosition == sortFilterViewPosition) {
            "Update view position is $updateViewInPosition, should be $sortFilterViewPosition."
        }
    }

    protected fun `Then assert button apply is shown with loading`() {
        val buttonApplyIsLoading = sortFilterBottomSheetViewModel.isLoadingLiveData.value

        assert(buttonApplyIsLoading == true) {
            "Button apply should be shown with loading"
        }
    }

    protected fun `Then assert button apply is not shown`() {
        val buttonApplyIsLoading = sortFilterBottomSheetViewModel.isLoadingLiveData.value

        assert(buttonApplyIsLoading == false) {
            "Button apply should not be shown"
        }
    }

    protected fun `Then assert button apply visibility`(expectedIsVisible: Boolean) {
        if (expectedIsVisible)
            `Then assert button apply is shown with loading`()
        else
            `Then assert button apply is not shown`()
    }

    protected fun `Then assert map parameter is as expected`(expectedMapParameter: Map<String, String>) {
        val actualMapParameter = sortFilterBottomSheetViewModel.mapParameter

        assert(actualMapParameter == expectedMapParameter) {
            "Map parameter is $actualMapParameter. Expected is: $expectedMapParameter."
        }
    }

    protected fun `Then assert selected filter map is as expected`(expectedSelectedFilterMap: Map<String, String>) {
        val actualSelectedFilterMapParameter = sortFilterBottomSheetViewModel.getSelectedFilterMap()

        assert(actualSelectedFilterMapParameter == expectedSelectedFilterMap) {
            "Selected Filter Map is $actualSelectedFilterMapParameter. Expected is $expectedSelectedFilterMap."
        }
    }

    protected fun `Then assert selected sort map is as expected`(expectedSelectedSortMap: Map<String, String>) {
        val selectedSortMap = sortFilterBottomSheetViewModel.getSelectedSortMap()

        assert(selectedSortMap == expectedSelectedSortMap) {
            "Selected Sort Map is $selectedSortMap. Expected is $expectedSelectedSortMap."
        }
    }

    protected fun `Then assert selected apply filter map is as expected`(expectedApplyFilterMap: Map<String, String>) {
        val applyFilterMap = sortFilterBottomSheetViewModel.getSortAutoFilterMap()

        assert(applyFilterMap == expectedApplyFilterMap) {
            "Selected Sort Map is $applyFilterMap. Expected is $expectedApplyFilterMap."
        }
    }

    protected fun `Then assert selected sort name`(expectedSelectedSortName: String) {
        val actualSelectedSortName = sortFilterBottomSheetViewModel.selectedSortName

        assert(actualSelectedSortName == expectedSelectedSortName) {
            "Selected Sort Name is \"$actualSelectedSortName\". Expected: \"$expectedSelectedSortName\""
        }
    }

    protected fun `Then assert filter view is expanded`() {
        val isViewExpandedLiveData = sortFilterBottomSheetViewModel.isViewExpandedLiveData.value

        assert(isViewExpandedLiveData == true) {
            "Filter view should be expanded"
        }
    }
}
