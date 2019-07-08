package com.tokopedia.checkout.view.feature.cartlist.viewholder

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.feature.cartlist.ActionListener
import com.tokopedia.checkout.view.feature.cartlist.adapter.CartRecentViewAdapter
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartRecentViewHolderData
import kotlinx.android.synthetic.main.item_cart_recent_view.view.*

/**
 * Created by Irfan Khoirul on 2019-06-15.
 */

class CartRecentViewViewHolder(val view: View, val listener: ActionListener) : RecyclerView.ViewHolder(view) {

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
        itemView.rv_recent_view.scrollToPosition(element.lastFocussPosition)
    }

}