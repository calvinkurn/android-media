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
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowSearchCategoryQuickFilterBinding
import com.tokopedia.utils.view.binding.viewBinding

class CategoryQuickFilterViewHolder(
    itemView: View,
    private val listener: CategoryQuickFilterListener?
): AbstractViewHolder<CategoryQuickFilterUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_search_category_quick_filter

        private const val ONE_OPTION_COUNT = 1
    }

    private var binding: ItemTokopedianowSearchCategoryQuickFilterBinding? by viewBinding()

    override fun bind(quickFilter: CategoryQuickFilterUiModel) {
        binding?.tokoNowSearchCategoryQuickFilter?.apply {
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

    private fun openL3FilterPage(filter: Filter) {
        listener?.openL3FilterPage(filter)
    }

    interface CategoryQuickFilterListener {
        fun openFilterPage()
        fun openL3FilterPage(filter: Filter)
        fun applyFilter(filter: Filter, option: Option)
    }
}
