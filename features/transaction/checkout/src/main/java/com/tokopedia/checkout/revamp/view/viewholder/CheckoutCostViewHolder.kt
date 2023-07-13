package com.tokopedia.checkout.revamp.view.viewholder

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutCostBinding
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCostModel

class CheckoutCostViewHolder(
    private val binding: ItemCheckoutCostBinding,
    private val layoutInflater: LayoutInflater
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(cost: CheckoutCostModel) {
    }

    companion object {
        val VIEW_TYPE = R.layout.item_checkout_cost
    }
}
