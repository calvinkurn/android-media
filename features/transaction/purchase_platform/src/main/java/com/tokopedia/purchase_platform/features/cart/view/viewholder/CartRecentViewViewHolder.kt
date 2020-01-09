package com.tokopedia.purchase_platform.features.cart.view.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.cart.view.ActionListener
import com.tokopedia.purchase_platform.features.cart.view.adapter.CartRecentViewAdapter
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartRecentViewHolderData
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
        itemView.rv_recent_view.scrollToPosition(element.lastFocussPosition)
    }

}