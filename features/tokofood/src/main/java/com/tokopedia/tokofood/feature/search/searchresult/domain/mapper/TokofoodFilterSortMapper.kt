package com.tokopedia.tokofood.feature.search.searchresult.domain.mapper

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Sort
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.TokofoodFilterItemUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.TokofoodQuickSortUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.TokofoodSortFilterItemUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.TokofoodSortItemUiModel
import javax.inject.Inject

class TokofoodFilterSortMapper @Inject constructor(@ApplicationContext private val context: Context) {

    fun getQuickSortFilterUiModels(dataValue: DataValue): List<TokofoodSortFilterItemUiModel> {
        val filterItems = dataValue.filter.map(::convertToFilterItemUiModel)
        val sortItems = dataValue.sort.run(::convertToSortItemUiModel)
        return filterItems + sortItems
    }

    fun getAppliedSortFilterUiModels(searchParameters: SearchParameter,
                                     uiModels: List<TokofoodSortFilterItemUiModel>): List<TokofoodSortFilterItemUiModel> {

        // TODO: Divide into methods
        return uiModels.map { item ->
            when(item) {
                is TokofoodFilterItemUiModel -> {
                    val updatedFilter = getFilterFromSearchParameter(searchParameters, item)
                    val selectedCount = updatedFilter.options.count { it.inputState == true.toString() }
                    val selectedKey =
                        if (selectedCount > Int.ZERO) {
                            updatedFilter.options.firstOrNull()?.key
                        } else {
                            null
                        }
                    item.copy(
                        filter = updatedFilter,
                        totalSelectedOptions = selectedCount,
                        selectedKey = selectedKey
                    )
                }
                is TokofoodSortItemUiModel -> {
                    val updatedSort = getSelectedSortFromSearchParameter(searchParameters, item)
                    val selectedCount =
                        if (updatedSort == null) {
                            Int.ZERO
                        } else {
                            Int.ONE
                        }
                    val selectedKey =
                        if (selectedCount > Int.ZERO) {
                            updatedSort?.key
                        } else {
                            null
                        }
                    item.copy(
                        selectedSort = updatedSort,
                        totalSelectedOptions = selectedCount,
                        selectedKey = selectedKey
                    )
                }
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

    private fun convertToFilterItemUiModel(filter: Filter): TokofoodFilterItemUiModel {
        return TokofoodFilterItemUiModel(
            sortFilterItem = filter.convertToSortFilterItem(),
            selectedKey = null,
            totalSelectedOptions = filter.options.count { it.inputState == true.toString() },
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
        return SortFilterItem(getSortChipTitle())
    }

    private fun getSortChipTitle(): String {
        return try {
            context.getString(com.tokopedia.tokofood.R.string.search_srp_sort_title)
        } catch (ex: Exception) {
            String.EMPTY
        }
    }

    private fun getFilterFromSearchParameter(searchParameters: SearchParameter,
                                             uiModel: TokofoodFilterItemUiModel): Filter {
        return uiModel.filter.options.firstOrNull()?.key?.let { key ->
            val valueList = searchParameters.get(key).split(OPTION_SEPARATOR)
            val newFilter = uiModel.filter.apply {
                options = options.onEach { option ->
                    if (valueList.contains(option.value)) {
                        option.inputState = true.toString()
                    } else {
                        option.inputState = false.toString()
                    }
                }
            }
            newFilter
        } ?: uiModel.filter
    }

    private fun getSelectedSortFromSearchParameter(searchParameters: SearchParameter,
                                                   uiModel: TokofoodSortItemUiModel): Sort? {
        return uiModel.sortList.firstOrNull()?.key?.let { key ->
            val valueList = searchParameters.get(key).split(OPTION_SEPARATOR)
            return valueList.firstOrNull()?.let { selectedSortValue ->
                uiModel.sortList.find { it.value == selectedSortValue }
            }
        }
    }

    companion object {
        const val OPTION_SEPARATOR = "#"
    }

}