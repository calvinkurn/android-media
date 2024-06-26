package com.tokopedia.tokopedianow.category.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.helper.getSortFilterCount
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryQuickFilterUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategorySortFilterItemUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowCategoryQuickFilterBinding
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.view.binding.viewBinding

class CategoryQuickFilterViewHolder(
    itemView: View,
    private val listener: CategoryQuickFilterListener?,
    private val tracker: CategoryQuickFilterTrackerListener? = null
): AbstractViewHolder<CategoryQuickFilterUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_category_quick_filter

        private const val ONE_OPTION_COUNT = 1
    }

    private var binding: ItemTokopedianowCategoryQuickFilterBinding? by viewBinding()

    override fun bind(quickFilter: CategoryQuickFilterUiModel) {
        binding?.sortFilter?.apply {
            val sortFilterItemList = quickFilter.itemList.map {
                val filter = it.filter
                val options = filter.options
                val sortFilterItem = SortFilterItem(filter.title, it.chipType)
                sortFilterItem.typeUpdated = false

                if (options.count() == ONE_OPTION_COUNT) {
                    val option = options.first()
                    sortFilterItem.listener = {
                        listener?.applyFilter(filter, option)
                    }
                } else {
                    val listener = {
                        openL3FilterPage(filter)
                    }
                    sortFilterItem.chevronListener = listener
                    sortFilterItem.listener = listener
                }
                sortFilterItem
            }
            sortFilterItems.removeAllViews()
            sortFilterHorizontalScrollView.scrollX = 0
            addItem(ArrayList(sortFilterItemList))
            parentListener = {
                listener?.openFilterPage()
            }
            indicatorCounter = getSortFilterCount(quickFilter.mapParameter)
            sortFilterItemList.forEachIndexed { index, sortFilterItem ->
                val showNewNotification = quickFilter.itemList[index].showNewNotification
                sortFilterItem.refChipUnify.showNewNotification = showNewNotification
            }
        }
    }

    override fun bind(quickFilter: CategoryQuickFilterUiModel, payloads: MutableList<Any>) {
        if (payloads.firstOrNull() != null) {
            binding?.sortFilter?.apply {
                val chipItemList = chipItems.orEmpty()

                if(chipItemList.isNotEmpty()) {
                    chipItemList.forEachIndexed { index, filterItem ->
                        val item = quickFilter.itemList[index]
                        val filter = item.filter
                        filterItem.title = filter.title
                        filterItem.refChipUnify.chipType = item.chipType
                        trackActiveFilterImpression(item)
                    }
                    indicatorCounter = getSortFilterCount(quickFilter.mapParameter)
                } else {
                    bind(quickFilter)
                }
            }
        }
    }

    private fun trackActiveFilterImpression(item: CategorySortFilterItemUiModel) {
        val filter = item.filter
        val options = filter.options
        val isActive = item.chipType == ChipsUnify.TYPE_SELECTED
        if (options.count() == ONE_OPTION_COUNT && isActive) {
            val option = options.first()
            trackQuickFilterChipImpression(option, true)
        } else {
            options.filter { it.name == filter.title }.forEach { option ->
                trackQuickFilterChipImpression(option, true)
            }
        }
    }

    private fun openL3FilterPage(filter: Filter) {
        listener?.openL3FilterPage(filter)
    }

    private fun trackQuickFilterChipImpression(option: Option, isActive: Boolean) {
        tracker?.onImpressQuickFilterChip(option, isActive)
    }

    interface CategoryQuickFilterListener {
        fun openFilterPage()
        fun openL3FilterPage(filter: Filter)
        fun applyFilter(filter: Filter, option: Option)
    }

    interface CategoryQuickFilterTrackerListener {
        fun onImpressQuickFilterChip(option: Option, isActive: Boolean)
    }
}
