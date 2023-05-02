package com.tokopedia.product.addedit.shipment.presentation.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.logisticCommon.data.model.ShipperListCPLModel
import com.tokopedia.product.addedit.R
import com.tokopedia.unifyprinciples.Typography

class ShipmentAdapter : RecyclerView.Adapter<ShipmentAdapter.ShipmentViewHolder>() {

    private val shipmentCPLitem = mutableListOf<ShipperListCPLModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShipmentViewHolder {
        return ShipmentViewHolder(parent.inflateLayout(R.layout.item_add_edit_shipment))
    }

    override fun onBindViewHolder(holder: ShipmentViewHolder, position: Int) {
        holder.bindData(shipmentCPLitem[position])
    }

    override fun getItemCount(): Int {
        return shipmentCPLitem.size
    }

    fun updateData(data: List<ShipperListCPLModel>) {
        shipmentCPLitem.clear()
        shipmentCPLitem.addAll(data)
        notifyDataSetChanged()
    }

    inner class ShipmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val shipmentItemName = itemView.findViewById<Typography>(R.id.shipment_name)
        private val shipmentItemCategory = itemView.findViewById<Typography>(R.id.shipment_category)

        fun bindData(data: ShipperListCPLModel) {
            shipmentItemName.text = data.header
            shipmentItemCategory.text = data.getActiveServiceName()
        }
    }
}
