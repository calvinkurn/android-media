package com.tokopedia.checkout.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemShipmentAddOnSummaryBinding
import com.tokopedia.checkout.view.uimodel.ShipmentAddOnSummaryModel

class ShipmentAddOnSummaryViewHolder(private val binding: ItemShipmentAddOnSummaryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(element: ShipmentAddOnSummaryModel) {
        with(binding) {
            tvShipmentSummaryAddOnWording.text = element.wording
            tvShipmentSummaryAddOnValue.text = element.priceLabel
        }
    }

    companion object {
        val LAYOUT = R.layout.item_shipment_add_on_summary
    }
}
