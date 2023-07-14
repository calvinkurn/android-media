package com.tokopedia.checkout.revamp.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutPromoBinding
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel

class CheckoutPromoViewHolder(private val binding: ItemCheckoutPromoBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(promo: LastApplyUiModel) {
    }

    companion object {
        val VIEW_TYPE = R.layout.item_checkout_promo
    }
}
