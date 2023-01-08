package com.tokopedia.filter.bottomsheet

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.filter.bottomsheet.filter.FilterViewModel
import com.tokopedia.filter.bottomsheet.filter.OptionViewModel
import com.tokopedia.filter.bottomsheet.pricefilter.PriceFilterViewModel
import com.tokopedia.filter.bottomsheet.pricefilter.PriceOptionViewModel
import com.tokopedia.filter.bottomsheet.filter.pricerangecheckbox.PriceRangeFilterCheckboxDataView
import com.tokopedia.filter.bottomsheet.sort.SortItemViewModel
import com.tokopedia.filter.bottomsheet.sort.SortViewModel
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter

internal fun List<Visitable<*>>.getAnyUnselectedSort(): SortItemViewModel {
    val allSortList = findAndReturn<SortViewModel>()!!.sortItemViewModelList
    return allSortList.find {
        // Choose any unselected sort
        !it.isSelected
    }!!
}

internal inline fun <reified T> List<Visitable<*>>.findAndReturn(): T? {
    for (element in this) if (element is T) return element
    return null
}

internal fun FilterViewModel.isSelectedOptionSorted() =
        optionViewModelList.asSequence().zipWithNext { currentOption, nextOption ->
            isCurrentOptionSortedWithNextOption(currentOption, nextOption)
        }.all { it }

private fun isCurrentOptionSortedWithNextOption(currentOption: OptionViewModel, nextOption: OptionViewModel): Boolean {
    return when {
        currentOption.isSelected < nextOption.isSelected -> false
        currentOption.isSelected && nextOption.isSelected ->
            currentOption.option.name <= nextOption.option.name
        else -> true
    }
}

internal fun List<Visitable<*>>.findFilterViewModel(filter: Filter): FilterViewModel? {
    return find { it is FilterViewModel && it.filter == filter } as? FilterViewModel
}

internal fun List<Visitable<*>>.findPriceRangeFilterCheckboxDataView(filter: Filter): PriceRangeFilterCheckboxDataView? {
    return find { it is PriceRangeFilterCheckboxDataView && it.filter == filter } as? PriceRangeFilterCheckboxDataView
}

internal fun FilterViewModel.getAnyUnselectedFilter(): OptionViewModel {
    return optionViewModelList.find {
        !it.isSelected
    }!!
}

internal fun List<Visitable<*>>.getUnselectedSortWithApplyFilter(): SortItemViewModel {
    val allSortList = findAndReturn<SortViewModel>()!!.sortItemViewModelList
    return allSortList.find {
        // Choose any unselected sort with apply filter
        !it.isSelected && it.sort.applyFilter.isNotEmpty()
    }!!
}

internal fun PriceFilterViewModel.getUnselectedPriceRangeOption(): PriceOptionViewModel {
    return priceRangeOptionViewModelList.find { !it.isSelected }!!
}

internal fun DynamicFilterModel.getCategoryFilter(): Filter {
    return this.data.filter.find { filter ->
        filter.isCategoryFilter
    }!!
}

internal fun Filter.getPopularOptionsCount(): Int {
    return options.count { it.isPopular } +
            options.map { it.levelTwoCategoryList }.flatten().count { it.isPopular } +
            options.map { it.levelTwoCategoryList.map { it.levelThreeCategoryList } }.flatten().flatten().count { it.isPopular }
}