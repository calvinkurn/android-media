package com.tokopedia.filter.bottomsheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.Event
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.bottomsheet.filter.FilterViewModel
import com.tokopedia.filter.bottomsheet.filter.OptionViewModel
import com.tokopedia.filter.bottomsheet.keywordfilter.KeywordFilterDataView
import com.tokopedia.filter.bottomsheet.pricefilter.PriceFilterViewModel
import com.tokopedia.filter.bottomsheet.pricefilter.PriceOptionViewModel
import com.tokopedia.filter.bottomsheet.filter.pricerangecheckbox.PriceRangeFilterCheckboxDataView
import com.tokopedia.filter.bottomsheet.sort.SortItemViewModel
import com.tokopedia.filter.bottomsheet.sort.SortViewModel
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.data.Sort
import com.tokopedia.filter.common.helper.toMapParam
import com.tokopedia.filter.newdynamicfilter.analytics.FilterEventTracking
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.track.TrackAppUtils
import kotlin.math.max

internal class SortFilterBottomSheetViewModel {

    companion object {
        private const val SORT_VIEW_POSITION = 0
        private const val SHOW_KNOB_MINIMUM_SECTION = 3
        const val MAX_OPTION_SIZE = 5
    }

    private var mutableMapParameter = mutableMapOf<String, String>()
    val mapParameter: Map<String, String>
        get() = mutableMapParameter

    var selectedSortName: String = ""
        private set

    private val selectedSortMap = mutableMapOf<String, String>()

    private var dynamicFilterModel: DynamicFilterModel? = null

    var showKnob = true
        private set

    private val sortFilterListMutableLiveData = MutableLiveData<List<Visitable<SortFilterBottomSheetTypeFactory>>>()
    val sortFilterListLiveData: LiveData<List<Visitable<SortFilterBottomSheetTypeFactory>>>
        get() = sortFilterListMutableLiveData

    private val updateViewInPositionEventMutableLiveData = MutableLiveData<Event<Int>>()
    val updateViewInPositionEventLiveData: LiveData<Event<Int>>
        get() = updateViewInPositionEventMutableLiveData

    private val isLoadingMutableLiveData = MutableLiveData<Boolean>()
    val isLoadingLiveData: LiveData<Boolean>
        get() = isLoadingMutableLiveData

    private val isButtonResetVisibleMutableLiveData = MutableLiveData<Boolean>()
    val isButtonResetVisibleLiveData: LiveData<Boolean>
        get() = isButtonResetVisibleMutableLiveData

    private val isViewExpandedMutableLiveData = MutableLiveData<Boolean>()
    val isViewExpandedLiveData: LiveData<Boolean>
        get() = isViewExpandedMutableLiveData

    private val trackPriceRangeClickEventMutableLiveData = MutableLiveData<Event<Map<String, Any>>>()
    val trackPriceRangeClickEventLiveData: LiveData<Event<Map<String, Any>>>
        get() = trackPriceRangeClickEventMutableLiveData

    private val isLoadingForDynamicFilterMutableLiveData = MutableLiveData<Boolean>()
    val isLoadingForDynamicFilterLiveData: LiveData<Boolean>
        get() = isLoadingForDynamicFilterMutableLiveData

    private val sortFilterList = mutableListOf<Visitable<SortFilterBottomSheetTypeFactory>>()
    private val filterController = FilterController()
    private val sortApplyFilterMap = mutableMapOf<String, String>()
    private val originalFilterViewState = mutableSetOf<String>()
    private var originalSortValue = ""
    private var originalKeyword = ""

    fun init(mapParameter: Map<String, String>, dynamicFilterModel: DynamicFilterModel?) {
        this.mutableMapParameter = mapParameter.toMutableMap()

        initializeDynamicFilterModel(dynamicFilterModel)
    }

    private fun initializeDynamicFilterModel(dynamicFilterModel: DynamicFilterModel?) {
        this.dynamicFilterModel = dynamicFilterModel

        showKnob = determineShouldShowKnob(dynamicFilterModel)

        filterController.initFilterController(mutableMapParameter, dynamicFilterModel?.data?.filter)
        originalFilterViewState.addAll(filterController.filterViewStateSet)
        originalSortValue = getSelectedSortValue()
        originalKeyword = getKeyword()
    }

    private fun determineShouldShowKnob(dynamicFilterModel: DynamicFilterModel?): Boolean {
        dynamicFilterModel ?: return true

        val hasSort = dynamicFilterModel.data.sort.isNotEmpty()
        val sectionCount = (dynamicFilterModel.data.filter.size + if (hasSort) 1 else 0)

        return sectionCount > SHOW_KNOB_MINIMUM_SECTION
    }

    private fun getSelectedSortValue(): String {
        return dynamicFilterModel?.let {
            mapParameter[it.getSortKey()] ?: it.defaultSortValue
        } ?: ""
    }

    private fun getKeyword() = mutableMapParameter[SearchApiConst.Q] ?: ""

    fun getSelectedFilterMap(): Map<String, String> {
        return filterController.getActiveFilterMap()
    }

    fun getSelectedCategoryFilterValue() = filterController.getFilterValue(SearchApiConst.SC)

    fun onViewCreated() {
        if (this.dynamicFilterModel == null) {
            isLoadingForDynamicFilterMutableLiveData.value = true
            return
        }

        this.dynamicFilterModel?.let { processSortFilterList(it) }
    }

    fun lateInitDynamicFilterModel(dynamicFilterModel: DynamicFilterModel) {
        initializeDynamicFilterModel(dynamicFilterModel)
        processSortFilterList(dynamicFilterModel)
    }

    private fun processSortFilterList(dynamicFilterModel: DynamicFilterModel) {
        val dynamicFilterModelData = dynamicFilterModel.data
        processSortData(dynamicFilterModelData)
        processFilterData(dynamicFilterModelData)

        if (sortFilterList.isEmpty()) return

        isLoadingForDynamicFilterMutableLiveData.value = false
        sortFilterListMutableLiveData.value = sortFilterList
        isButtonResetVisibleMutableLiveData.value = isButtonResetVisible()
    }

    private fun isButtonResetVisible() = filterController.isFilterActive() || isSortNotDefault()

    private fun isSortNotDefault(): Boolean {
        return getSelectedSortValue() != dynamicFilterModel?.defaultSortValue ?: ""
    }

    private fun processSortData(dynamicFilterModelData: DataValue) {
        val sortList = dynamicFilterModelData.sort
        if (sortList.isEmpty()) return

        val sortItemViewModelList = mutableListOf<SortItemViewModel>()

        sortList.forEach { sort ->
            val sortItemViewModel = createSortItemViewModel(sort)

            if (sortItemViewModel.isSelected)
                sortItemViewModelList.add(0, sortItemViewModel)
            else
                sortItemViewModelList.add(sortItemViewModel)
        }

        sortFilterList.add(SortViewModel(sortItemViewModelList))
    }

    private fun createSortItemViewModel(sort: Sort): SortItemViewModel {
        val sortItemViewModel = SortItemViewModel(sort)

        sortItemViewModel.isSelected = getIsSortSelected(sort)

        return sortItemViewModel
    }

    private fun getIsSortSelected(sort: Sort): Boolean = mutableMapParameter[sort.key] == sort.value

    private fun processFilterData(dynamicFilterModelData: DataValue) {
        val filterList = dynamicFilterModelData.filter

        filterList.forEach { filter ->
            when {
                filter.isPriceFilter -> sortFilterList.add(createPriceFilterViewModel(filter))
                filter.isKeywordFilter -> sortFilterList.add(createKeywordFilterDataView(filter))
                filter.isPriceRangeCheckboxFilter -> sortFilterList.add(createPriceRangeFilterCheckbox(filter))
                else -> sortFilterList.add(createFilterViewModel(filter))
            }
        }
    }

    private fun createPriceRangeFilterCheckbox(priceFilter: Filter): PriceRangeFilterCheckboxDataView {
        return PriceRangeFilterCheckboxDataView(
            filter = priceFilter,
            optionViewModelList = priceFilter.options.map {
                val option = it.apply {
                    updateInputState()
                }
                OptionViewModel(
                    option = option
                ).apply {
                    isSelected = option.inputState.toBoolean()
                }
            }.toMutableList(),
        )
    }

    private fun createPriceFilterViewModel(priceFilter: Filter): PriceFilterViewModel {
        val minPriceFilterTitle = priceFilter.options.find { it.isMinPriceOption }?.name ?: ""
        val maxPriceFilterTitle = priceFilter.options.find { it.isMaxPriceOption }?.name ?: ""
        val minPriceFilterValue = getMinPriceFilterValue()
        val maxPriceFilterValue = getMaxPriceFilterValue()

        return PriceFilterViewModel(
                priceFilter,
                minPriceFilterTitle,
                maxPriceFilterTitle,
                minPriceFilterValue,
                maxPriceFilterValue,
                createPriceRangeOptionViewModelList(priceFilter.options)
        )
    }

    private fun getMinPriceFilterValue(): String {
        return mapParameter[SearchApiConst.PMIN] ?: ""
    }

    private fun getMaxPriceFilterValue(): String {
        return mapParameter[SearchApiConst.PMAX] ?: ""
    }

    private fun createPriceRangeOptionViewModelList(optionList: List<Option>): MutableList<PriceOptionViewModel> {
        return optionList
                .filter { it.isPriceRange }
                .mapIndexed { index, option -> createPriceOptionViewModel(option, index + 1) }
                .toMutableList()
    }

    private fun createPriceOptionViewModel(option: Option, position: Int): PriceOptionViewModel {
        return PriceOptionViewModel(option, position).also {
            it.isSelected = option.valMin == getMinPriceFilterValue() && option.valMax == getMaxPriceFilterValue()
        }
    }

    private fun createKeywordFilterDataView(filter: Filter) =
        KeywordFilterDataView(filter = filter)

    private fun createFilterViewModel(filter: Filter): FilterViewModel {
        val optionViewModelMutableList = mutableListOf<OptionViewModel>()
        processOptionList(filter.options, optionViewModelMutableList, true)

        val hasSeeAllButton = filter.options.any { !it.isPopular } || filter.isCategoryFilter

        return FilterViewModel(filter, hasSeeAllButton, optionViewModelMutableList)
                .apply { sortBySelectedAndName() }
    }

    private fun processOptionList(
        optionList: List<Option>,
        optionViewModelList: MutableList<OptionViewModel>,
        willSortOptionList: Boolean
    ) {
        val levelTwoCategoryList = mutableListOf<Option>()
        val levelThreeCategoryList = mutableListOf<Option>()

        val selectedOrPopularOptionList = mutableListOf<Option>()

        optionList.forEach { option ->
            option.updateInputState()
            selectedOrPopularOptionList.addIfSelectedOrPopular(option)

            processCategoryLevelOptions(option, levelTwoCategoryList, levelThreeCategoryList)
        }

        levelTwoCategoryList.forEach { option ->
            option.updateInputState()
            selectedOrPopularOptionList.addIfSelectedOrPopular(option)
        }

        levelThreeCategoryList.forEach { option ->
            option.updateInputState()
            selectedOrPopularOptionList.addIfSelectedOrPopular(option)
        }

        val optionListForViewModel = if (willSortOptionList) {
            createOptionListForViewModel(selectedOrPopularOptionList)
        } else {
            selectedOrPopularOptionList
        }

        optionListForViewModel.forEach {
            optionViewModelList.add(createOptionViewModel(it))
        }
    }

    private fun createOptionListForViewModel(selectedOrPopularOptionList: List<Option>): List<Option> {
        val selectedOption = selectedOrPopularOptionList.filter { it.inputState.toBoolean() }.toMutableList()
        val hasSelectedOption = selectedOption.size > 0

        return if (hasSelectedOption) {
            selectedOption.sortBy { it.name }

            val remainingSlotForUnselectedOption = max(MAX_OPTION_SIZE - selectedOption.size, 0)
            val unSelectedOption = selectedOrPopularOptionList.filter { !it.inputState.toBoolean() }.take(remainingSlotForUnselectedOption)

            selectedOption + unSelectedOption
        } else {
            selectedOrPopularOptionList
        }
    }

    private fun Option.updateInputState() {
        inputState = filterController.getFilterViewState(this).toString()
    }

    private fun MutableList<Option>.addIfSelectedOrPopular(option: Option) {
        if (option.inputState.toBoolean() || option.isPopular) {
            add(option)
        }
    }

    private fun createOptionViewModel(option: Option): OptionViewModel {
        return OptionViewModel(option).also {
            it.isSelected = option.inputState.toBoolean()
        }
    }

    private fun processCategoryLevelOptions(option: Option, levelTwoCategoryList: MutableList<Option>, levelThreeCategoryList: MutableList<Option>) {
        val currentLevelTwoCategoryList = option.levelTwoCategoryList
        val currentLevelThreeCategoryList = currentLevelTwoCategoryList.map { it.levelThreeCategoryList }.flatten()

        levelTwoCategoryList.addAll(currentLevelTwoCategoryList.map { it.asOption() })
        levelThreeCategoryList.addAll(currentLevelThreeCategoryList.map { it.asOption() })
    }

    private fun FilterRefreshable.sortBySelectedAndName() {
        val sortedOptionViewModelList = optionViewModelList.filter { it.isSelected }.toMutableList()
        sortedOptionViewModelList.sortBy { it.option.name }

        sortedOptionViewModelList.addAll(optionViewModelList.filter { !it.isSelected })

        optionViewModelList = sortedOptionViewModelList
    }

    fun onOptionClick(filterRefreshable: FilterRefreshable, optionViewModel: OptionViewModel) {
        applyFilter(filterRefreshable, optionViewModel)
    }

    private fun applyFilter(filterRefreshable: FilterRefreshable, optionViewModel: OptionViewModel) {
        val isCleanUpExistingFilterWithSameKey = optionViewModel.option.isCategoryOption || optionViewModel.option.isTypeRadio

        if (isCleanUpExistingFilterWithSameKey)
            filterRefreshable.optionViewModelList.unSelectNotClickedOption(optionViewModel)

        val isSelected = !optionViewModel.isSelected
        filterController.setFilter(optionViewModel.option, isSelected, isCleanUpExistingFilterWithSameKey)
        val sortFilterIndexSet : MutableSet<Int> = mutableSetOf()
        updateSortFilterList(listOf(optionViewModel.option), sortFilterIndexSet)
        refreshMapParameter()
        notifyViewOnApplyFilter(sortFilterIndexSet.toList())
    }

    private fun updateSortFilterList(
        filterOptionList: List<Option>,
        sortFilterIndexSet: MutableSet<Int>
    ) {
        sortFilterList.forEachIndexed { index, visitable ->
            if(visitable is FilterRefreshable && visitable.filter.options.isNotEmpty()) {
                val needUpdate = visitable.filter.getFlattenedOptions().any { option ->
                    option in filterOptionList
                }
                if(needUpdate) {
                    visitable.refreshOptionList()
                    sortFilterIndexSet.add(index)
                }
            }
        }
    }

    private fun refreshMapParameter() {
        mutableMapParameter.clear()
        mutableMapParameter.putAll(filterController.getParameter())
        mutableMapParameter.putAll(selectedSortMap)
        mutableMapParameter[SearchApiConst.ORIGIN_FILTER] = SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE
    }

    private fun List<OptionViewModel>.unSelectNotClickedOption(clickedOptionViewModel: OptionViewModel) {
        filter { it != clickedOptionViewModel && it.isSelected }.map { it.isSelected = false }
    }

    private fun notifyViewOnApplyFilter(visitableIndexList: List<Int>) {
        visitableIndexList.forEach { index ->
            updateViewInPositionEventMutableLiveData.value = Event(index)
        }
        isLoadingMutableLiveData.value = getIsLoading()
        isButtonResetVisibleMutableLiveData.value = isButtonResetVisible()
        isViewExpandedMutableLiveData.value = true
    }

    private fun notifyViewOnApplyFilter(visitable: Visitable<*>) {
        notifyViewOnApplyFilter(listOf(sortFilterList.indexOf(visitable)))
    }

    private fun getIsLoading() = isFilterChanged() || isSortChanged() || isKeywordChanged()

    private fun isKeywordChanged() = originalKeyword != getKeyword()

    private fun isFilterChanged() = originalFilterViewState != filterController.filterViewStateSet

    private fun isSortChanged() = originalSortValue != getSelectedSortValue()

    fun onPriceRangeOptionClick(priceFilterViewModel: PriceFilterViewModel, priceOptionViewModel: PriceOptionViewModel) {
        val isApplyFilter = !priceOptionViewModel.isSelected

        val minPrice = if (isApplyFilter) priceOptionViewModel.option.valMin else ""
        val maxPrice = if (isApplyFilter) priceOptionViewModel.option.valMax else ""

        togglePriceRangeFilter(priceFilterViewModel, minPrice, maxPrice)

        modifyPriceFilterView(priceFilterViewModel, minPrice, maxPrice)
        trackPriceRangeClick(priceOptionViewModel)
        notifyViewOnApplyFilter(priceFilterViewModel)
    }

    private fun togglePriceRangeFilter(priceFilterViewModel: PriceFilterViewModel, minPrice: String, maxPrice: String) {
        applyPriceFilter(priceFilterViewModel, Option.KEY_PRICE_MIN, minPrice)
        applyPriceFilter(priceFilterViewModel, Option.KEY_PRICE_MAX, maxPrice)
        refreshMapParameter()
    }

    private fun applyPriceFilter(priceFilterViewModel: PriceFilterViewModel, priceFilterKey: String, priceValue: String) {
        val priceOption = priceFilterViewModel.priceFilter.options.find { it.key == priceFilterKey }
                ?: return

        priceOption.value = priceValue

        filterController.setFilter(priceOption, isFilterApplied = priceValue != "", isCleanUpExistingFilterWithSameKey = true)
    }

    private fun modifyPriceFilterView(priceFilterViewModel: PriceFilterViewModel, minPrice: String, maxPrice: String) {
        priceFilterViewModel.minPriceFilterValue = minPrice
        priceFilterViewModel.maxPriceFilterValue = maxPrice

        priceFilterViewModel.updateSelectedPriceRangeOptionViewModel()
    }

    private fun PriceFilterViewModel.updateSelectedPriceRangeOptionViewModel() {
        priceRangeOptionViewModelList.forEach {
            val isSelected = it.option.valMin == minPriceFilterValue && it.option.valMax == maxPriceFilterValue
            it.isSelected = isSelected
        }
    }

    private fun trackPriceRangeClick(priceOptionViewModel: PriceOptionViewModel) {
        val dataLayer = TrackAppUtils.gtmData(
                FilterEventTracking.Event.CLICK_FILTER,
                FilterEventTracking.Category.FILTER_JOURNEY,
                createPriceRangeClickTrackingEventAction(priceOptionViewModel),
                priceOptionViewModel.isSelected.toString()
        )

        trackPriceRangeClickEventMutableLiveData.value = Event(dataLayer)
    }

    private fun createPriceRangeClickTrackingEventAction(priceOptionViewModel: PriceOptionViewModel): String {
        return String.format(
                FilterEventTracking.Action.CLICK_FILTER_PRICE_RANGE,
                priceOptionViewModel.option.valMin.toInt(), priceOptionViewModel.option.valMax.toInt(), priceOptionViewModel.position
        )
    }

    fun onMinPriceFilterEdited(priceFilterViewModel: PriceFilterViewModel, minPriceValue: Int) {
        onPriceFilterEdited(priceFilterViewModel, Option.KEY_PRICE_MIN, minPriceValue) {
            priceFilterViewModel.minPriceFilterValue = it
        }
    }

    private fun onPriceFilterEdited(
        priceFilterViewModel: PriceFilterViewModel,
        priceFilterKey: String,
        priceValue: Int,
        updatePriceFilterViewModel: (String) -> Unit,
    ) {
        val minMaxPriceRange = priceFilterViewModel.getMinMaxPriceRange()
        val normalizedMinPriceValue = priceValue.coerceToString(minMaxPriceRange)

        applyPriceFilter(priceFilterViewModel, priceFilterKey, normalizedMinPriceValue)
        refreshMapParameter()

        updatePriceFilterViewModel(priceValue.toString())
        priceFilterViewModel.updateSelectedPriceRangeOptionViewModel()

        isButtonResetVisibleMutableLiveData.value = isButtonResetVisible()
    }

    private fun PriceFilterViewModel.getMinMaxPriceRange(): IntRange {
        val minMaxOption = priceFilter.options.find { it.isMinMaxRangePriceOption }
        val valMin = minMaxOption?.valMin?.toIntOrNull() ?: 0
        val valMax = minMaxOption?.valMax?.toIntOrNull() ?: Int.MAX_VALUE

        return valMin..valMax
    }

    private fun Int.coerceToString(range: IntRange) =
        if (this !in range) "" else this.toString()

    fun onMaxPriceFilterEdited(priceFilterViewModel: PriceFilterViewModel, maxPriceValue: Int) {
        onPriceFilterEdited(priceFilterViewModel, Option.KEY_PRICE_MAX, maxPriceValue) {
            priceFilterViewModel.maxPriceFilterValue = it
        }
    }

    fun onSortItemClick(sortItemViewModel: SortItemViewModel) {
        val appliedSort = getAppliedSort(sortItemViewModel)

        applySort(appliedSort)

        notifyViewOnSortItemClick()
    }

    private fun getAppliedSort(sortItemViewModel: SortItemViewModel): SortItemViewModel {
        if (sortItemViewModel.isSelected) {
            val sortViewModel = sortFilterList[SORT_VIEW_POSITION]

            if (sortViewModel is SortViewModel)
                return sortViewModel.getDefaultSortItemViewModel() ?: sortItemViewModel
        }

        return sortItemViewModel
    }

    private fun SortViewModel.getDefaultSortItemViewModel() = sortItemViewModelList.find { it.isDefault() }

    private fun SortItemViewModel.isDefault() = sort.value == dynamicFilterModel?.defaultSortValue

    private fun applySort(sortItemViewModel: SortItemViewModel) {
        sortItemViewModel.isSelected = true

        val selectedSort = sortItemViewModel.sort

        sortApplyFilterMap.replaceWithMap(selectedSort.applyFilter.toMapParam())

        mutableMapParameter[SearchApiConst.ORIGIN_FILTER] = SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_SORT_PAGE
        mutableMapParameter[selectedSort.key] = selectedSort.value

        selectedSortName = selectedSort.name
        selectedSortMap[selectedSort.key] = selectedSort.value

        val sortViewModel = sortFilterList[SORT_VIEW_POSITION]
        if (sortViewModel is SortViewModel) {
            sortViewModel.unSelectNotClickedSort(sortItemViewModel)
        }
    }

    private fun MutableMap<String, String>.replaceWithMap(applyFilterMap: Map<String, String>) {
        clear()
        putAll(applyFilterMap)
    }

    private fun SortViewModel.unSelectNotClickedSort(clickedSort: SortItemViewModel) {
        sortItemViewModelList.filter { it != clickedSort }.map { it.isSelected = false }
    }

    private fun notifyViewOnSortItemClick() {
        updateViewInPositionEventMutableLiveData.value = Event(SORT_VIEW_POSITION)
        isLoadingMutableLiveData.value = getIsLoading()
        isButtonResetVisibleMutableLiveData.value = isButtonResetVisible()
        isViewExpandedMutableLiveData.value = true
    }

    fun getSelectedSortMap(): Map<String, String> {
        return selectedSortMap
    }

    fun resetSortAndFilter() {
        sortFilterList.forEachIndexed(this::resetSortFilterItem)

        resetParameters()

        isButtonResetVisibleMutableLiveData.value = false
        isLoadingMutableLiveData.value = getIsLoading()
        isViewExpandedMutableLiveData.value = true
    }

    private fun resetSortFilterItem(index: Int, visitable: Visitable<*>) {
        when (visitable) {
            is SortViewModel -> visitable.reset(index)
            is PriceFilterViewModel -> visitable.reset(index)
            is FilterRefreshable -> visitable.reset(index)
        }
    }

    private fun SortViewModel.reset(sortIndex: Int) {
        val isSortNotDefault = isSortNotDefault()

        if (isSortNotDefault) {
            sortItemViewModelList.forEach {
                it.isSelected = it.sort.value == dynamicFilterModel?.defaultSortValue ?: ""
            }

            updateViewInPositionEventMutableLiveData.value = Event(sortIndex)
        }
    }

    private fun PriceFilterViewModel.reset(filterIndex: Int) {
        var shouldUpdate = false

        if (minPriceFilterValue != "") {
            shouldUpdate = true
            minPriceFilterValue = ""
        }

        if (maxPriceFilterValue != "") {
            shouldUpdate = true
            maxPriceFilterValue = ""
        }

        priceRangeOptionViewModelList.forEach {
            if (it.isSelected) {
                shouldUpdate = true
                it.isSelected = false
            }
        }

        if (shouldUpdate) updateViewInPositionEventMutableLiveData.value = Event(filterIndex)
    }

    private fun FilterRefreshable.reset(filterIndex: Int) {
        var shouldUpdate = false

        optionViewModelList.forEach {
            if (it.isSelected) {
                shouldUpdate = true
                it.isSelected = false
                it.option.inputState = false.toString()
            }
        }

        if (shouldUpdate) updateViewInPositionEventMutableLiveData.value = Event(filterIndex)
    }

    private fun resetParameters() {
        resetFilterParameter()
        resetSortParameter()
        resetMapParameter()
    }

    private fun resetFilterParameter() {
        filterController.resetAllFilters()
    }

    private fun resetSortParameter() {
        selectedSortMap.clear()
        selectedSortName = ""
        sortApplyFilterMap.clear()
    }

    private fun resetMapParameter() {
        mutableMapParameter.clear()
        mutableMapParameter.putAll(filterController.getParameter())

        dynamicFilterModel?.run {
            if (hasSort()) mutableMapParameter[getSortKey()] = defaultSortValue
        }
    }

    fun applySortFilter() {
        if (!mutableMapParameter.contains(SearchApiConst.PMIN) && sortApplyFilterMap.isNotEmpty()) {
            mutableMapParameter.putAll(sortApplyFilterMap)
        }
    }

    fun onApplyFilterFromDetailPage(filterViewModel: FilterViewModel, optionList: List<Option>?) {
        optionList ?: return

        applyFilterFromDetailPage(filterViewModel, optionList)
    }

    private fun applyFilterFromDetailPage(filterViewModel: FilterViewModel, optionList: List<Option>) {
        filterViewModel.filter.options = optionList

        filterController.setFilter(filterViewModel.filter.options)
        val sortFilterIndexSet = mutableSetOf<Int>()
        updateSortFilterList(optionList, sortFilterIndexSet)
        refreshMapParameter()
        notifyViewOnApplyFilter(sortFilterIndexSet.toList())
    }

    private fun FilterRefreshable.refreshOptionList() {
        val newOptionViewModelList = mutableListOf<OptionViewModel>()
        processOptionList(filter.options, newOptionViewModelList, willSortOptionList)

        optionViewModelList.clear()
        optionViewModelList.addAll(newOptionViewModelList)

        if (willSortOptionList) {
            sortBySelectedAndName()
        }
    }

    fun onApplyCategoryFilterFromDetailPage(categoryFilterViewModel: FilterViewModel, selectedCategoryFilterValue: String) {
        val currentSelectedCategoryFilter = filterController.getFilterValue(SearchApiConst.SC)
        val categoryFilterValue = if (selectedCategoryFilterValue.isNotEmpty()) selectedCategoryFilterValue else currentSelectedCategoryFilter

        val selectedFilterOption = getCategoryFilterAsOption(categoryFilterViewModel.filter, categoryFilterValue) ?: return

        applyFilterFromCategoryDetailPage(categoryFilterViewModel, selectedFilterOption, selectedCategoryFilterValue.isNotEmpty())

        notifyViewOnApplyFilter(categoryFilterViewModel)
    }

    private fun getCategoryFilterAsOption(categoryFilter: Filter, selectedCategoryFilterValue: String): Option? {
        categoryFilter.options.forEach { levelOne ->
            if (levelOne.value == selectedCategoryFilterValue) return levelOne
            else {
                levelOne.levelTwoCategoryList.forEach { levelTwo ->
                    if (levelTwo.value == selectedCategoryFilterValue) return levelTwo.asOption()
                    else {
                        levelTwo.levelThreeCategoryList.forEach { levelThree ->
                            if (levelThree.value == selectedCategoryFilterValue) return levelThree.asOption()
                        }
                    }
                }
            }
        }

        return null
    }

    private fun applyFilterFromCategoryDetailPage(categoryFilterViewModel: FilterViewModel, selectedFilterOption: Option, isFilterApplied: Boolean) {
        filterController.setFilter(selectedFilterOption, isFilterApplied = isFilterApplied, isCleanUpExistingFilterWithSameKey = true)
        refreshMapParameter()

        categoryFilterViewModel.refreshOptionList()
    }

    fun onPriceTextOutOfFocus() {
        isLoadingMutableLiveData.value = true
    }

    fun onChangeKeywordFilter(keywordFilterDataView: KeywordFilterDataView) {
        mutableMapParameter[SearchApiConst.Q] = keywordFilterDataView.generateKeyword()
        filterController.refreshMapParameter(mapParameter)

        isLoadingMutableLiveData.value = getIsLoading()
    }
}
