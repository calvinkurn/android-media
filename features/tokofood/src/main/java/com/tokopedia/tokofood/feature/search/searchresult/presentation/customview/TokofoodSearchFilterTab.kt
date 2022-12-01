package com.tokopedia.tokofood.feature.search.searchresult.presentation.customview

import android.content.Context
import android.view.ViewTreeObserver
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.data.Sort
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.tokofood.common.presentation.listener.TokofoodScrollChangedListener
import com.tokopedia.tokofood.common.util.TokofoodExt.addAndReturnImpressionListener
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.TokofoodFilterItemUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.TokofoodSortFilterItemUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.TokofoodSortItemUiModel
import com.tokopedia.unifycomponents.ChipsUnify

class TokofoodSearchFilterTab(
    private val sortFilter: SortFilter,
    private val context: Context?,
    private val listener: Listener
): TokofoodScrollChangedListener {

    private val currentQuickFilter = mutableListOf<TokofoodSortFilterItemUiModel>()
    private val prefixChipImpressHolder = ImpressHolder()

    private var scrollChangedListenerList: MutableList<ViewTreeObserver.OnScrollChangedListener> = mutableListOf()

    init {
        initSortFilter()
    }

    override fun onScrollChangedListenerAdded(onScrollChangedListener: ViewTreeObserver.OnScrollChangedListener) {
        scrollChangedListenerList.add(onScrollChangedListener)
    }

    fun setQuickFilter(items: List<TokofoodSortFilterItemUiModel>) {
        currentQuickFilter.clear()
        currentQuickFilter.addAll(items)
        with(sortFilter) {
            val sortFilterItemList = items.mapNotNull(::mapSortFilterUiModelToSortFilterItems)
            sortFilterItems.removeAllViews()
            if (sortFilterItemList.isNotEmpty()) {
                visible()
                sortFilterHorizontalScrollView.scrollX = Int.ZERO
                addItem(sortFilterItemList as ArrayList)
                chipItems?.forEach {
                    it.updateSelectedRef = { _,_,_,_,_ -> }
                }
                textView?.text = context.getString(com.tokopedia.tokofood.R.string.search_srp_filter_chip_title)
            }
        }
        removeScrollListeners()
        setOnImpressionListeners()
    }

    fun removeListener() {
        removeScrollListeners()
        sortFilter.removeScrollListener()
    }

    private fun initSortFilter() {
        sortFilter.gone()
        setSortFilterClickListener()
    }

    private fun setSortFilterClickListener() {
        sortFilter.parentListener = {
            listener.onOpenFullFilterBottomSheet()
        }
    }

    private fun mapSortFilterUiModelToSortFilterItems(uiModel: TokofoodSortFilterItemUiModel): SortFilterItem? {
        return when(uiModel) {
            is TokofoodSortItemUiModel -> uiModel.getSortFilterItem()
            is TokofoodFilterItemUiModel -> uiModel.getSortFilterItem()
            else -> null
        }
    }

    private fun TokofoodSortItemUiModel.getSortFilterItem(): SortFilterItem? {
        return sortFilterItem.takeIf { sortList.isNotEmpty() }?.also { item ->
            if (sortList.size > Int.ONE) {
                item.listener = {
                    listener.onOpenQuickFilterBottomSheet(sortList)
                }
                item.chevronListener = {
                    listener.onOpenQuickFilterBottomSheet(sortList)
                }
            } else {
                val sort = sortList.firstOrNull()
                if (sort == null) {
                    item.listener = {}
                } else {
                    item.listener =  {
                        toggleActiveChip(item, sort)
                    }
                }
            }
            item.setActive(selectedSort != null)
            item.title = context?.getString(com.tokopedia.tokofood.R.string.search_srp_sort_title).orEmpty()
        }
    }

    private fun TokofoodFilterItemUiModel.getSortFilterItem(): SortFilterItem? {
        return sortFilterItem.takeIf { filter.options.isNotEmpty() }?.also { item ->
            if (filter.options.size > Int.ONE) {
                item.listener = {
                    listener.onOpenQuickFilterBottomSheet(
                        filter,
                        item.type == ChipsUnify.TYPE_SELECTED
                    )
                }
                item.chevronListener = {
                    listener.onOpenQuickFilterBottomSheet(
                        filter,
                        item.type == ChipsUnify.TYPE_SELECTED
                    )
                }
            } else {
                val filterOption = filter.options.firstOrNull()
                if (filterOption == null) {
                    item.listener = {}
                } else {
                    item.listener = {
                        toggleActiveChip(item, filter)
                    }
                }
            }
            item.setActive(filter)
        }
    }

    private fun SortFilterItem.setActive(filter: Filter) {
        val isActive = filter.options.any { it.inputState.toBoolean() }
        setSelected(isActive)
    }

    private fun SortFilterItem.setActive(isSortSelected: Boolean) {
        setSelected(isSortSelected)
    }

    private fun toggleActiveChip(sortFilterItem: SortFilterItem, sort: Sort) {
        val isSelected: Boolean = sortFilterItem.type == ChipsUnify.TYPE_NORMAL
        listener.onSelectSortChip(sort, isSelected)
    }

    private fun toggleActiveChip(sortFilterItem: SortFilterItem, filter: Filter) {
        val isSelected: Boolean = sortFilterItem.type == ChipsUnify.TYPE_NORMAL
        filter.run {
            options.firstOrNull()?.inputState = isSelected.toString()
        }
        listener.onSelectFilterChip(filter, isSelected)
    }

    private fun SortFilterItem.setSelected(isSelected: Boolean) {
        type =
            if (isSelected) {
                ChipsUnify.TYPE_SELECTED
            } else {
                ChipsUnify.TYPE_NORMAL
            }
    }

    private fun setOnImpressionListeners() {
        setFullOnImpressionListener()
        setSortOnImpressionListener()
        setMiniChipsOnImpressionListener()
    }

    private fun setFullOnImpressionListener() {
        sortFilter.sortFilterPrefix.addAndReturnImpressionListener(prefixChipImpressHolder, this) {
            listener.onImpressCompleteFilterChip()
        }
    }

    private fun setSortOnImpressionListener() {
        (currentQuickFilter.getOrNull(Int.ZERO) as? TokofoodSortItemUiModel)?.let { sortUiModel ->
            sortFilter.sortFilterItems.getChildAt(Int.ONE)?.let { sortChip ->
                sortChip.addAndReturnImpressionListener(sortUiModel, this) {
                    listener.onImpressSortChip(sortUiModel.sortList)
                }
            }
        }
    }

    private fun setMiniChipsOnImpressionListener() {
        currentQuickFilter.forEachIndexed { index, uiModel ->
            (uiModel as? TokofoodFilterItemUiModel)?.let { filterUiModel ->
                sortFilter.sortFilterItems.getChildAt(index + Int.ONE)?.addAndReturnImpressionListener(filterUiModel, this) {
                    listener.onImpressFilterChip(filterUiModel.filter.options)
                }
            }
        }
    }

    private fun removeScrollListeners() {
        scrollChangedListenerList.forEach {
            sortFilter.viewTreeObserver?.removeOnScrollChangedListener(it)
        }
        scrollChangedListenerList.clear()
    }

    interface Listener {
        fun onOpenFullFilterBottomSheet()
        fun onOpenQuickFilterBottomSheet(sortList: List<Sort>)
        fun onOpenQuickFilterBottomSheet(filter: Filter, isSelected: Boolean)
        fun onSelectSortChip(sort: Sort, isSelected: Boolean)
        fun onSelectFilterChip(filter: Filter, isSelected: Boolean)
        fun onImpressCompleteFilterChip()
        fun onImpressSortChip(sorts: List<Sort>)
        fun onImpressFilterChip(options: List<Option>)
    }

}
