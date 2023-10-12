package com.tokopedia.checkout.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemShipmentAddOnSubtotalBinding
import com.tokopedia.checkout.view.uimodel.ShipmentAddOnSubtotalModel

class ShipmentAddOnSubtotalViewHolder(private val binding: ItemShipmentAddOnSubtotalBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(element: ShipmentAddOnSubtotalModel) {
        with(binding) {
            tvShipmentSubtotalAddOnWording.text = element.wording
            tvShipmentSubtotalAddOnValue.text = element.priceLabel
        }
    }

    companion object {
        val LAYOUT = R.layout.item_shipment_add_on_subtotal
    }
}
