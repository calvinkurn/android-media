package com.tokopedia.deals.search.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.deals.R
import com.tokopedia.deals.search.listener.DealsSearchListener
import com.tokopedia.deals.search.model.visitor.RecentModel
import com.tokopedia.deals.search.ui.adapter.DealsLastSeenAdapter

class RecentViewHolder (itemView: View, dealsSearchListener: DealsSearchListener): AbstractViewHolder<RecentModel>(itemView) {

    private val lastSeenRv: RecyclerView = itemView.findViewById(R.id.rv_last_seen)
    private val lastSeenAdapter = DealsLastSeenAdapter(dealsSearchListener)

    init {
        lastSeenRv.adapter = lastSeenAdapter
        lastSeenRv.layoutManager = ChipsLayoutManager.newBuilder(itemView.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()
    }

    override fun bind(element: RecentModel?) {
        element?.let {
            lastSeenAdapter.brandList = it.items
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_deals_last_seen_list_search
    }

}