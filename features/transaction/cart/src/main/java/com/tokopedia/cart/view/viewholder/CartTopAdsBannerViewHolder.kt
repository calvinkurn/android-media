package com.tokopedia.cart.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartTopadsBannerBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.CartTopAdsBannerData

class CartTopAdsBannerViewHolder(private val binding: ItemCartTopadsBannerBinding, val listener: ActionListener?) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        val LAYOUT = R.layout.item_cart_topads_banner
    }

    fun bind(data: CartTopAdsBannerData) {

    }

}