package com.tokopedia.cart.old.view.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartRecentViewBinding
import com.tokopedia.cart.old.view.ActionListener
import com.tokopedia.cart.old.view.adapter.recentview.CartRecentViewAdapter
import com.tokopedia.cart.old.view.decorator.CartHorizontalItemDecoration
import com.tokopedia.cart.old.view.uimodel.CartRecentViewHolderData

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
        val padding = itemView.context?.resources?.getDimension(R.dimen.dp_16)?.toInt() ?: 0
        binding.rvRecentView.addItemDecoration(CartHorizontalItemDecoration(padding, padding))
        binding.rvRecentView.scrollToPosition(element.lastFocussPosition)
        if (!element.hasSentImpressionAnalytics) {
            listener?.onRecentViewImpression()
            element.hasSentImpressionAnalytics = true
        }
    }

}