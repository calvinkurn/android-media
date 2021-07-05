package com.tokopedia.cart.view.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartRecentViewBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.adapter.recentview.CartRecentViewAdapter
import com.tokopedia.cart.view.decorator.CartHorizontalItemDecoration
import com.tokopedia.cart.view.uimodel.CartRecentViewHolderData

/**
 * Created by Irfan Khoirul on 2019-06-15.
 */

class CartRecentViewViewHolder(private val binding: ItemCartRecentViewBinding, val listener: ActionListener?) : RecyclerView.ViewHolder(binding.root) {

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
        binding.rvRecentView.layoutManager = layoutManager
        binding.rvRecentView.adapter = recentViewAdapter
        val itemDecorationCount = binding.rvRecentView.itemDecorationCount
        if (itemDecorationCount > 0) {
            binding.rvRecentView.removeItemDecorationAt(0)
        }
        binding.rvRecentView.addItemDecoration(CartHorizontalItemDecoration())
        binding.rvRecentView.scrollToPosition(element.lastFocussPosition)
        if (!element.hasSentImpressionAnalytics) {
            listener?.onRecentViewImpression()
            element.hasSentImpressionAnalytics = true
        }
    }

}