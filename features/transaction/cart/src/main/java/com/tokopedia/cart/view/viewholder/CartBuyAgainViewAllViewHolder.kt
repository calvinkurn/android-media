package com.tokopedia.cart.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartBuyAgainViewAllBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.CartBuyAgainViewAllData

class CartBuyAgainViewAllViewHolder(
    private val binding: ItemCartBuyAgainViewAllBinding,
    private val listener: ActionListener?
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        val LAYOUT = R.layout.item_cart_buy_again_view_all
    }

    fun bind(data: CartBuyAgainViewAllData) {
        binding.root.setOnClickListener {
            listener?.onShowAllItemBuyAgain(data.showAllAppLink, false)
        }
    }
}
