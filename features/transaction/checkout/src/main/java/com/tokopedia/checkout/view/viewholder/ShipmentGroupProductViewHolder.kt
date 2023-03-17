package com.tokopedia.checkout.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel

class ShipmentGroupProductViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    companion object {

        @JvmField
        val LAYOUT = R.layout.item_shipment_product

        private const val VIEW_ALPHA_ENABLED = 1.0f
        private const val VIEW_ALPHA_DISABLED = 0.5f
    }

    fun bind(shipmentCartItemModel: ShipmentCartItemModel) {
    }
}
