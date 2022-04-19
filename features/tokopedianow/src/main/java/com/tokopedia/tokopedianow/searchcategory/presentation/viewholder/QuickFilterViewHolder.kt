package com.tokopedia.tokopedianow.searchcategory.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.filter.common.helper.getSortFilterCount
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowSearchCategoryQuickFilterBinding
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.QuickFilterListener
import com.tokopedia.tokopedianow.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.utils.view.binding.viewBinding

class QuickFilterViewHolder(
        itemView: View,
        private val quickFilterListener: QuickFilterListener,
): AbstractViewHolder<QuickFilterDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_search_category_quick_filter
    }

    private var binding: ItemTokopedianowSearchCategoryQuickFilterBinding? by viewBinding()

    override fun bind(element: QuickFilterDataView?) {
        element ?: return

        binding?.tokoNowSearchCategoryQuickFilter?.apply {
            sortFilterItems.removeAllViews()
            sortFilterHorizontalScrollView.scrollX = 0
            addItem(ArrayList(element.quickFilterItemList.map { it.sortFilterItem }))
            parentListener = {
                quickFilterListener.openFilterPage()
            }
            indicatorCounter = getSortFilterCount(element.mapParameter)
        }
        setNewNotification(element)
    }

    private fun setNewNotification(element: QuickFilterDataView) {
        element.quickFilterItemList.forEach {
            val showNewNotification = it.filter.options.firstOrNull()?.isNew ?: false
            it.sortFilterItem.refChipUnify.showNewNotification = showNewNotification
        }
    }
}