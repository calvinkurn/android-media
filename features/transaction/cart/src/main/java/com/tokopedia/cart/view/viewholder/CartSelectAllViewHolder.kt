package com.tokopedia.cart.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.CartSelectAllHolderData
import com.tokopedia.cart.databinding.ItemSelectAllBinding
import rx.subscriptions.CompositeSubscription

class CartSelectAllViewHolder(private val binding: ItemSelectAllBinding, val listener: ActionListener?, val compositeSubscription: CompositeSubscription) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        val LAYOUT = R.layout.item_select_all
    }

    fun bind(data: CartSelectAllHolderData) {
        // No-op
    }

}