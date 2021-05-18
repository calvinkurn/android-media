package com.tokopedia.tokomart.searchcategory.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.searchcategory.presentation.listener.QuickFilterListener
import com.tokopedia.tokomart.searchcategory.presentation.model.QuickFilterDataView

class QuickFilterViewHolder(
        itemView: View,
        private val quickFilterListener: QuickFilterListener,
): AbstractViewHolder<QuickFilterDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokomart_search_category_quick_filter
    }

    private var unifySortFilter: SortFilter? = null

    init {
        unifySortFilter = itemView.findViewById<SortFilter?>(R.id.tokomartSearchCategoryQuickFilter)
    }

    override fun bind(element: QuickFilterDataView?) {
        element ?: return

        unifySortFilter?.sortFilterItems?.removeAllViews()
        unifySortFilter?.sortFilterHorizontalScrollView?.scrollX = 0
        unifySortFilter?.addItem(ArrayList(element.quickFilterItemList.map { it.sortFilterItem }))
        unifySortFilter?.parentListener = {
            quickFilterListener.openFilterPage()
        }
        setNewNotification(element)
    }

    private fun setNewNotification(element: QuickFilterDataView) {
        element.quickFilterItemList.forEach {
            it.sortFilterItem.refChipUnify.showNewNotification = it.option.isNew
        }
    }
}