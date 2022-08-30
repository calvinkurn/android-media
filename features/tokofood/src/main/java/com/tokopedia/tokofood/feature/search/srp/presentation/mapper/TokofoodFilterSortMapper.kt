package com.tokopedia.tokofood.feature.search.srp.presentation.mapper

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Sort
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.tokofood.feature.search.srp.presentation.uimodel.TokofoodFilterItemUiModel
import com.tokopedia.tokofood.feature.search.srp.presentation.uimodel.TokofoodSortFilterItemUiModel
import com.tokopedia.tokofood.feature.search.srp.presentation.uimodel.TokofoodSortItemUiModel
import javax.inject.Inject

class TokofoodFilterSortMapper @Inject constructor(@ApplicationContext private val context: Context) {

    fun getQuickSortFilterUiModels(dataValue: DataValue): List<TokofoodSortFilterItemUiModel> {
        val filterItems = dataValue.filter.map(::convertToFilterItemUiModel)
        val sortItems = dataValue.sort.run(::convertToSortItemUiModel)
        return filterItems + sortItems
    }

    private fun convertToFilterItemUiModel(filter: Filter): TokofoodFilterItemUiModel {
        return TokofoodFilterItemUiModel(
            sortFilterItem = filter.convertToSortFilterItem(),
            isSelected = false,
            filter = filter
        )
    }

    private fun Filter.convertToSortFilterItem(): SortFilterItem {
        return SortFilterItem(chipName)
    }

    private fun convertToSortItemUiModel(sortList: List<Sort>): TokofoodSortFilterItemUiModel {
        return TokofoodSortItemUiModel(
            sortFilterItem = getSortingSortFilterItem(),
            isSelected = false,
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

}