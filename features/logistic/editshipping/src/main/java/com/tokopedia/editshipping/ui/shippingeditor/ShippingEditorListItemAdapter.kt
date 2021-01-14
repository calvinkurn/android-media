package com.tokopedia.editshipping.ui.shippingeditor

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.editshipping.R
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.logisticCommon.data.entity.shippingeditor.OnDemandModel
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography

class ShippingEditorListItemAdapter(private val listener: ShippingEditorItemAdapterListener): RecyclerView.Adapter<ShippingEditorListItemAdapter.ShippingEditorViewHolder>() {

    var shipperOnDemandModel = mutableListOf<OnDemandModel>()

    interface ShippingEditorItemAdapterListener {
            fun onShipperInfoClicked()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShippingEditorViewHolder {
        return ShippingEditorViewHolder(parent.inflateLayout(R.layout.item_shipping_editor_card), listener)
    }

    override fun getItemCount(): Int {
        return shipperOnDemandModel.size
    }

    override fun onBindViewHolder(holder: ShippingEditorViewHolder, position: Int) {
        holder.binData(shipperOnDemandModel[position])
    }


    inner class ShippingEditorViewHolder(itemView: View, private val listener: ShippingEditorItemAdapterListener): RecyclerView.ViewHolder(itemView) {

        private val shipmentItemImage = itemView.findViewById<ImageView>(R.id.img_shipment_item)
        private val shipmentName = itemView.findViewById<Typography>(R.id.shipment_name)
        private val shipmentItemCb = itemView.findViewById<CheckboxUnify>(R.id.cb_shipment_item)


        fun binData(data: OnDemandModel) {
            setItemData(data)
            setListener(data)
        }

        private fun setItemData(data: OnDemandModel){
            shipmentName.text = data.name
            if (data.isActive) {
                shipmentItemCb.isChecked = true
            }
        }

        private fun setListener(data: OnDemandModel) {

        }
    }

}