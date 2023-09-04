package com.tokopedia.search.result.product.filter.bottomsheetfilter

import com.tokopedia.filter.common.data.OptionHolder
import com.tokopedia.filter.common.data.IOption
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.SortModel
import com.tokopedia.filter.common.data.Sort

fun DynamicFilterModel.findSelectedSort(searchParams: HashMap<String, String>): Sort? {
    val selectedSortFromQuery = this.getSelectedSortFromParamQuery(searchParams)
    val selectedDefault = this.getDefaultSelectedSort()
    return selectedSortFromQuery ?: selectedDefault
}

private fun DynamicFilterModel.getSelectedSortFromParamQuery(searchParams: HashMap<String, String>): Sort? {
    val selectedSorting: Sort?
    val listSort = this.data.sort
    selectedSorting = listSort.find {
        searchParams[this.getSortKey()] == it.value
    }
    return selectedSorting
}

private fun DynamicFilterModel.getDefaultSelectedSort(): Sort? {
    return this.data.sort.find { it.value == this.defaultSortValue }
}

fun List<Sort>?.generalDetailOptionsMapper(title: String = "", selectedSort: Sort?): OptionHolder {
    this?.filter { it.value == selectedSort?.value }?.map { it.inputState= "true"  }
    return SortModel(this ?: emptyList(), title)
}

fun List<IOption>.toSelectedSort(): Sort? {
    return this.firstOrNull{ it.inputState.toBoolean() } as? Sort
}
