package com.tokopedia.cartrevamp.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemSelectAllBinding
import com.tokopedia.cartrevamp.view.ActionListener
import com.tokopedia.cartrevamp.view.uimodel.CartSelectedAmountHolderData
import rx.subscriptions.CompositeSubscription

class CartSelectedAmountViewHolder(private val binding: ItemSelectAllBinding, val listener: ActionListener?, val compositeSubscription: CompositeSubscription) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        val LAYOUT = R.layout.item_selected_amount
    }

    fun bind(data: CartSelectedAmountHolderData) {
        binding
    }
}
