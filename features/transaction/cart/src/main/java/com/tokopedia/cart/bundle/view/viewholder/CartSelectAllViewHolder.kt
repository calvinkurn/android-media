package com.tokopedia.cart.bundle.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.bundle.view.ActionListener
import com.tokopedia.cart.bundle.view.uimodel.CartSelectAllHolderData
import com.tokopedia.cart.databinding.ItemSelectAllBundleBinding
import rx.subscriptions.CompositeSubscription

class CartSelectAllViewHolder(private val binding: ItemSelectAllBundleBinding, val listener: ActionListener?, val compositeSubscription: CompositeSubscription) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        val LAYOUT = R.layout.item_select_all_bundle
    }

    fun bind(data: CartSelectAllHolderData) {
        // No-op
    }

}