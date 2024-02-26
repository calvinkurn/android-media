package com.tokopedia.cart.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartBuyAgainViewAllBinding
import com.tokopedia.cart.view.ActionListener

class CartBuyAgainViewAllViewHolder(
    private val binding: ItemCartBuyAgainViewAllBinding,
    private val listener: ActionListener?
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        val LAYOUT = R.layout.item_cart_buy_again_view_all
    }

    fun bind() {
        binding.root.setOnClickListener {

        }
//            setImageProductViewHintListener(
//                element,
//                object : ViewHintListener {
//                    override fun onViewHint() {
//                        actionListener?.onRecentViewProductImpression(element)
//                    }
//                }
//            )

//            setOnClickListener {
//                actionListener?.onRecentViewProductClicked(element.id)
//            }
//            setAddToCartOnClickListener {
//                actionListener?.onButtonAddToCartClicked(element)
//            }
    }
}
