package com.tokopedia.checkout.revamp.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemShipmentGroupFooterBinding
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel

class CheckoutOrderViewHolder(private val binding: ItemShipmentGroupFooterBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(order: CheckoutOrderModel) {
    }

    companion object {
        val VIEW_TYPE = R.layout.item_shipment_group_footer
    }
}
