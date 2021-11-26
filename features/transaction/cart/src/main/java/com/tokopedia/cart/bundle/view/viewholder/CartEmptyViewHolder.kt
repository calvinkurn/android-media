package com.tokopedia.cart.bundle.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemEmptyCartBundleBinding
import com.tokopedia.cart.bundle.view.ActionListener
import com.tokopedia.cart.bundle.view.uimodel.CartEmptyHolderData

/**
 * Created by Irfan Khoirul on 2019-05-29.
 */

class CartEmptyViewHolder(private val binding: ItemEmptyCartBundleBinding, val listener: ActionListener?): RecyclerView.ViewHolder(binding.root) {

    companion object {
        val LAYOUT = R.layout.item_empty_cart_bundle
    }

    fun bind(data: CartEmptyHolderData) {
        binding.cartEmptyState.apply {
            setTitle(data.title)
            setDescription(data.desc)
            setImageUrl(data.imgUrl)
            setPrimaryCTAText(data.btnText)
            setPrimaryCTAClickListener { listener?.onClickShopNow() }
        }
    }

}