package com.tokopedia.product.addedit.shipment.presentation.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.logisticCommon.data.model.ShipperCPLModel
import com.tokopedia.product.addedit.R
import com.tokopedia.unifyprinciples.Typography
import kotlin.text.StringBuilder

class ShipmentAdapter : RecyclerView.Adapter<ShipmentAdapter.ShipmentViewHolder>() {

    private val shipmentCPLitem = mutableListOf<ShipperCPLModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShipmentViewHolder {
        return ShipmentViewHolder(parent.inflateLayout(R.layout.item_add_edit_shipment))
    }

    override fun onBindViewHolder(holder: ShipmentViewHolder, position: Int) {
        holder.bindData(shipmentCPLitem[position])
    }

    override fun getItemCount(): Int {
        return shipmentCPLitem.size
    }

    fun updateData(data: List<ShipperCPLModel>) {
        shipmentCPLitem.clear()
        shipmentCPLitem.addAll(data)
        notifyDataSetChanged()
    }

    inner class ShipmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val shipmentItemImage = itemView.findViewById<ImageView>(R.id.img_shipment_item)
        private val shipmentItemName = itemView.findViewById<Typography>(R.id.shipment_name)
        private val shipmentItemCategory = itemView.findViewById<Typography>(R.id.shipment_category)

        fun bindData(data: ShipperCPLModel) {
            val shipperProduct = data.shipperProduct
            val stringBuilder = StringBuilder()

            for (x in shipperProduct.indices) {
                if (!shipperProduct[x].uiHidden) {
                    stringBuilder.append(shipperProduct[x].shipperProductName).append(", ")
                }
            }

            if (stringBuilder.isNotEmpty()) {
                shipmentItemImage?.let {
                    ImageHandler.loadImageFitCenter(itemView.context, it, data.logo)
                }
                shipmentItemName.text = data.shipperName
                shipmentItemCategory.text = stringBuilder.substring(0, stringBuilder.length - 2)
            }

        }
    }
}