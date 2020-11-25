package com.tokopedia.cart.view.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.adapter.recentview.CartRecentViewAdapter
import com.tokopedia.cart.view.decorator.CartHorizontalItemDecoration
import com.tokopedia.cart.view.uimodel.CartRecentViewHolderData
import kotlinx.android.synthetic.main.item_cart_recent_view.view.*

/**
 * Created by Irfan Khoirul on 2019-06-15.
 */

class CartRecentViewViewHolder(val view: View, val listener: ActionListener?) : RecyclerView.ViewHolder(view) {

    var recentViewAdapter: CartRecentViewAdapter? = null

    companion object {
        val LAYOUT = R.layout.item_cart_recent_view
    }

    fun bind(element: CartRecentViewHolderData) {
        if (recentViewAdapter == null) {
            recentViewAdapter = CartRecentViewAdapter(listener)
        }
        recentViewAdapter?.recentViewItemHoldeDataList = element.recentViewList
        val layoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
        itemView.rv_recent_view.layoutManager = layoutManager
        itemView.rv_recent_view.adapter = recentViewAdapter
        val itemDecorationCount = itemView.rv_recent_view.itemDecorationCount
        if (itemDecorationCount > 0) {
            itemView.rv_recent_view.removeItemDecorationAt(0)
        }
        itemView.rv_recent_view.addItemDecoration(CartHorizontalItemDecoration())
        itemView.rv_recent_view.scrollToPosition(element.lastFocussPosition)
        if (!element.hasSentImpressionAnalytics) {
            listener?.onRecentViewImpression()
            element.hasSentImpressionAnalytics = true
        }
    }

}