package com.tokopedia.affiliate.feature.explore.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.explore.view.adapter.FilterAdapter
import com.tokopedia.affiliate.feature.explore.view.viewmodel.FilterListViewModel
import kotlinx.android.synthetic.main.item_explore_filter.view.*

/**
 * @author by milhamj on 11/03/19.
 */
class FilterViewHolder(itemView: View,
                       private val filterListener: FilterAdapter.OnFilterClickedListener)
    : AbstractViewHolder<FilterListViewModel>(itemView) {

    private val filterAdapter: FilterAdapter by lazy {
        FilterAdapter(filterListener, R.layout.item_explore_filter_child)
    }

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.item_explore_filter
    }

    override fun bind(element: FilterListViewModel?) {
        if (element == null) {
            return
        }

        filterAdapter.setList(element.filters)
        itemView.filterRv.adapter = filterAdapter
    }
}