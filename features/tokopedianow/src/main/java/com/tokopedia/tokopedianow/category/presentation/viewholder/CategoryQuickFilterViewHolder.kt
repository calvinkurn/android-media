package com.tokopedia.tokopedianow.category.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.filter.common.helper.getSortFilterCount
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryQuickFilterUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowSearchCategoryQuickFilterBinding
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.QuickFilterListener
import com.tokopedia.utils.view.binding.viewBinding

class CategoryQuickFilterViewHolder(
    itemView: View,
    private val quickFilterListener: QuickFilterListener?
): AbstractViewHolder<CategoryQuickFilterUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_search_category_quick_filter
    }

    private var binding: ItemTokopedianowSearchCategoryQuickFilterBinding? by viewBinding()

    override fun bind(filter: CategoryQuickFilterUiModel) {
        binding?.tokoNowSearchCategoryQuickFilter?.apply {
            val sortFilterItemList = filter.itemList.map {
                SortFilterItem(it.title)
            }
            sortFilterItems.removeAllViews()
            sortFilterHorizontalScrollView.scrollX = 0
            addItem(ArrayList(sortFilterItemList))
            parentListener = {
                quickFilterListener?.openFilterPage()
            }
            indicatorCounter = getSortFilterCount(filter.mapParameter)
            sortFilterItemList.forEachIndexed { index, sortFilterItem ->
                val showNewNotification = filter.itemList[index].showNewNotification
                sortFilterItem.refChipUnify.showNewNotification = showNewNotification
            }
        }
    }
}
