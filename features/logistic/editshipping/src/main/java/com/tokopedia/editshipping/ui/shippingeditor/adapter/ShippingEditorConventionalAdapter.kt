package com.tokopedia.editshipping.ui.shippingeditor.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.editshipping.R
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.logisticCommon.data.entity.shippingeditor.ConventionalModel
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography

class ShippingEditorConventionalAdapter() : RecyclerView.Adapter<ShippingEditorConventionalAdapter.ShippingEditorConventionalViewHolder>(){

    var shipperConventionalModel = mutableListOf<ConventionalModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShippingEditorConventionalViewHolder {
        return ShippingEditorConventionalViewHolder(parent.inflateLayout(R.layout.item_shipping_editor_card))
    }

    override fun getItemCount(): Int {
        return shipperConventionalModel.size
    }

    override fun onBindViewHolder(holder: ShippingEditorConventionalViewHolder, position: Int) {
        holder.binData(shipperConventionalModel[position])
    }

    inner class ShippingEditorConventionalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val shipmentItemImage = itemView.findViewById<ImageView>(R.id.img_shipment_item)
        private val shipmentName = itemView.findViewById<Typography>(R.id.shipment_name)
        private val shipmentItemCb = itemView.findViewById<CheckboxUnify>(R.id.cb_shipment_item)


        fun binData(data: ConventionalModel) {
            setItemData(data)
        }

        private fun setItemData(data: ConventionalModel){
            shipmentItemImage?.let {
                ImageHandler.loadImageFitCenter(itemView.context, it, data.image)
            }
            shipmentName.text = data.name
            shipmentItemCb.isChecked = data.isActive
        }
    }

}