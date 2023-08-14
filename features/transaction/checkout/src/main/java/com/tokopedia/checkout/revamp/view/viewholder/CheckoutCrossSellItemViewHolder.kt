package com.tokopedia.checkout.revamp.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.databinding.ItemCheckoutCrossSellItemBinding
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapterListener
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellItem
import com.tokopedia.kotlin.extensions.view.dpToPx

class CheckoutCrossSellItemViewHolder(private val binding: ItemCheckoutCrossSellItemBinding, private val listener: CheckoutAdapterListener) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(checkoutCrossSellItem: CheckoutCrossSellItem, parentWidth: Int) {
        val expectedWidth = parentWidth - 50.dpToPx(binding.root.context.resources.displayMetrics)
        binding.root.layoutParams.width = expectedWidth
        CheckoutCrossSellItemView.renderCrossSellItem(checkoutCrossSellItem, binding, listener)
    }
}
