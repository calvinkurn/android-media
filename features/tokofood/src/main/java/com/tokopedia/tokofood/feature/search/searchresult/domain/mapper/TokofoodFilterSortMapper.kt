package com.tokopedia.tokofood.feature.search.searchresult.domain.mapper

import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Sort
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.PriceRangeChipUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.PriceRangeFilterCheckboxItemUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.TokofoodFilterItemUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.TokofoodQuickSortUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.TokofoodSortFilterItemUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.TokofoodSortItemUiModel
import javax.inject.Inject

class TokofoodFilterSortMapper @Inject constructor() {

    fun getQuickSortFilterUiModels(dataValue: DataValue): List<TokofoodSortFilterItemUiModel> {
        val sortItem = dataValue.sort.run(::convertToSortItemUiModel)
        val filterItems = dataValue.filter.map(::convertToFilterItemUiModel)
        return listOf(sortItem) + filterItems
    }

    fun getAppliedSortFilterUiModels(searchParameters: HashMap<String, String>,
                                     uiModels: List<TokofoodSortFilterItemUiModel>): List<TokofoodSortFilterItemUiModel> {
        return uiModels.map { item ->
            when(item) {
                is TokofoodFilterItemUiModel -> getAppliedFilterItemUiModel(item, searchParameters)
                is TokofoodSortItemUiModel -> getAppliedSortItemUiModel(item, searchParameters)
                else -> item
            }
        }
    }

    fun getQuickSortUiModels(sortList: List<Sort>,
                             selectedSortValue: String): List<TokofoodQuickSortUiModel> {
        return sortList.map {
            TokofoodQuickSortUiModel(
                name = it.name,
                key = it.key,
                value = it.value,
                isSelected = it.value == selectedSortValue
            )
        }
    }

    fun getQuickFilterPriceRangeUiModels(filter: Filter): PriceRangeChipUiModel {
        val uiModels =  filter.options.map {
            PriceRangeFilterCheckboxItemUiModel(it).apply {
                isSelected = it.inputState.toBoolean()
            }
        }
        return PriceRangeChipUiModel(uiModels, filter.subTitle)
    }

    fun getCurrentSortKey(uiModels: List<TokofoodSortFilterItemUiModel>?): String {
        return uiModels?.filterIsInstance<TokofoodSortItemUiModel>()
            ?.firstOrNull()?.sortList?.firstOrNull()?.key.orEmpty()
    }

    private fun convertToFilterItemUiModel(filter: Filter): TokofoodFilterItemUiModel {
        return TokofoodFilterItemUiModel(
            sortFilterItem = filter.convertToSortFilterItem(),
            selectedKey = null,
            totalSelectedOptions = filter.options.count { it.inputState.toBoolean() },
            filter = filter
        )
    }

    private fun Filter.convertToSortFilterItem(): SortFilterItem {
        return SortFilterItem(title)
    }

    private fun convertToSortItemUiModel(sortList: List<Sort>): TokofoodSortFilterItemUiModel {
        return TokofoodSortItemUiModel(
            sortFilterItem = getSortingSortFilterItem(),
            totalSelectedOptions = Int.ZERO,
            selectedKey = null,
            sortList = sortList
        )
    }

    private fun getSortingSortFilterItem(): SortFilterItem {
        return SortFilterItem(String.EMPTY)
    }

    private fun getAppliedFilterItemUiModel(item: TokofoodFilterItemUiModel,
                                            searchParameters: HashMap<String, String>): TokofoodFilterItemUiModel {
        val updatedFilter = getFilterFromSearchParameter(searchParameters, item)
        val (selectedCount, selectedKey) = updatedFilter.getSelectedCountAndKey()
        return item.copy(
            filter = updatedFilter,
            totalSelectedOptions = selectedCount,
            selectedKey = selectedKey
        )
    }

    private fun getAppliedSortItemUiModel(item: TokofoodSortItemUiModel,
                                          searchParameters: HashMap<String, String>): TokofoodSortItemUiModel {
        val updatedSort = getSelectedSortFromSearchParameter(searchParameters, item)
        val (selectedCount, selectedKey) = updatedSort.getSelectedCountAndKey()
        return item.copy(
            selectedSort = updatedSort,
            totalSelectedOptions = selectedCount,
            selectedKey = selectedKey
        )
    }

    private fun getFilterFromSearchParameter(searchParameters: HashMap<String, String>,
                                             uiModel: TokofoodFilterItemUiModel): Filter {
        return uiModel.filter.options.firstOrNull()?.key?.let { key ->
            val valueList = searchParameters[key]?.split(OPTION_SEPARATOR)
            val newFilter = uiModel.filter.apply {
                options = options.onEach { option ->
                    if (valueList?.contains(option.value) == true) {
                        option.inputState = true.toString()
                    } else {
                        option.inputState = false.toString()
                    }
                }
            }
            newFilter
        } ?: uiModel.filter
    }

    private fun getSelectedSortFromSearchParameter(searchParameters: HashMap<String, String>,
                                                   uiModel: TokofoodSortItemUiModel): Sort? {
        return uiModel.sortList.firstOrNull()?.key?.let { key ->
            val valueList = searchParameters[key]?.split(OPTION_SEPARATOR)
            return valueList?.firstOrNull()?.let { selectedSortValue ->
                uiModel.sortList.find { it.value == selectedSortValue }
            }
        }
    }

    private fun Filter.getSelectedCountAndKey(): Pair<Int, String?> {
        val selectedCount =
            options.count { it.inputState.toBoolean() }
        val selectedKey =
            if (selectedCount > Int.ZERO) {
                options.firstOrNull()?.key
            } else {
                null
            }
        return selectedCount to selectedKey
    }

    private fun Sort?.getSelectedCountAndKey(): Pair<Int, String?> {
        val selectedCount =
            if (this == null) {
                Int.ZERO
            } else {
                Int.ONE
            }
        val selectedKey =
            if (selectedCount > Int.ZERO) {
                this?.key
            } else {
                null
            }
        return selectedCount to selectedKey
    }

    companion object {
        const val OPTION_SEPARATOR = "#"
    }

}
