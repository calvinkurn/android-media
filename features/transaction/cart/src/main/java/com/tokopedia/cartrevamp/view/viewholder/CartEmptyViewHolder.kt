package com.tokopedia.cartrevamp.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemEmptyCartBinding
import com.tokopedia.cartrevamp.view.ActionListener
import com.tokopedia.cartrevamp.view.uimodel.CartEmptyHolderData

/**
 * Created by Irfan Khoirul on 2019-05-29.
 */

class CartEmptyViewHolder(private val binding: ItemEmptyCartBinding, val listener: ActionListener?) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        val LAYOUT = R.layout.item_empty_cart
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
