package com.tokopedia.checkout.revamp.view.viewholder

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ViewItemShipmentCostDetailsBinding
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCostModel

class CheckoutCostViewHolder(
    private val binding: ViewItemShipmentCostDetailsBinding,
    private val layoutInflater: LayoutInflater
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(cost: CheckoutCostModel) {
    }

    companion object {
        val VIEW_TYPE = R.layout.view_item_shipment_cost_details
    }
}
