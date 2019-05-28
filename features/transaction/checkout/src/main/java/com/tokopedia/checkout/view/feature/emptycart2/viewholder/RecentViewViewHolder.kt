package com.tokopedia.checkout.view.feature.emptycart2.viewholder

import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.feature.emptycart2.ActionListener
import com.tokopedia.checkout.view.feature.emptycart2.adapter.RecentViewAdapter
import com.tokopedia.checkout.view.feature.emptycart2.uimodel.RecentViewUiModel
import kotlinx.android.synthetic.main.item_checkout_procuct_recent_view.view.*

/**
 * Created by Irfan Khoirul on 2019-05-20.
 */

class RecentViewViewHolder(val view: View, val listener: ActionListener, val itemWidth: Int) : AbstractViewHolder<RecentViewUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_checkout_procuct_recent_view
    }

    override fun bind(element: RecentViewUiModel) {
        itemView.tv_last_seen_see_all.setOnClickListener {
            listener.onShowAllRecentView();
        }
        setupRecyclerView(element)
    }

    private fun setupRecyclerView(element: RecentViewUiModel) {
        val recentViewAdapter = RecentViewAdapter(listener, itemWidth)
        recentViewAdapter.setData(element.recentViewItems)
        val gridLayoutManager = GridLayoutManager(itemView.context, 2)
        itemView.rv_last_seen.layoutManager = gridLayoutManager
        itemView.rv_last_seen.adapter = recentViewAdapter
        recentViewAdapter.notifyDataSetChanged()
    }

}