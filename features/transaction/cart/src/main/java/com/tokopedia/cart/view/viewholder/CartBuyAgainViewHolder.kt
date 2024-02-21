package com.tokopedia.cart.view.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartBuyAgainBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.adapter.buyagain.CartBuyAgainAdapter
import com.tokopedia.cart.view.decorator.CartHorizontalItemDecoration
import com.tokopedia.cart.view.uimodel.CartBuyAgainHolderData
import com.tokopedia.kotlin.extensions.view.dpToPx

class CartBuyAgainViewHolder(
    private val binding: ItemCartBuyAgainBinding,
    private val listener: ActionListener?
) : RecyclerView.ViewHolder(binding.root) {

    var buyAgainAdapter: CartBuyAgainAdapter? = null

    companion object {
        val LAYOUT = R.layout.item_cart_buy_again
    }

    fun bind(element: CartBuyAgainHolderData) {
        if (buyAgainAdapter == null) {
            buyAgainAdapter = CartBuyAgainAdapter(listener)
        }
        buyAgainAdapter?.buyAgainList = element.buyAgainList
        val layoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
        binding.rvRecentView.layoutManager = layoutManager
        binding.rvRecentView.adapter = buyAgainAdapter
        val itemDecorationCount = binding.rvRecentView.itemDecorationCount
        if (itemDecorationCount > 0) {
            binding.rvRecentView.removeItemDecorationAt(0)
        }
        val paddingStart = 12.dpToPx(itemView.resources.displayMetrics)
        val padding = 16.dpToPx(itemView.resources.displayMetrics)
        binding.rvRecentView.addItemDecoration(CartHorizontalItemDecoration(paddingStart, padding))
//        if (!element.hasSentImpressionAnalytics) {
//            listener?.onRecentViewImpression()
//            element.hasSentImpressionAnalytics = true
//        }
    }
}

