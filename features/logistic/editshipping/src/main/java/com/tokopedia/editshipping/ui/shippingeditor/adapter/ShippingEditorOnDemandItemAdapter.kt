package com.tokopedia.editshipping.ui.shippingeditor.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.editshipping.R
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.logisticCommon.data.entity.shippingeditor.OnDemandModel
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography

class ShippingEditorOnDemandItemAdapter(private val listener: ShippingEditorItemAdapterListener): RecyclerView.Adapter<ShippingEditorOnDemandItemAdapter.ShippingEditorOnDemandViewHolder>() {

    private var shipperOnDemandModel = mutableListOf<OnDemandModel>()

    private var shipperProductChild: ShipperProductItemAdapter? = null

    interface ShippingEditorItemAdapterListener {
            fun onShipperInfoClicked()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShippingEditorOnDemandViewHolder {
        return ShippingEditorOnDemandViewHolder(parent.inflateLayout(R.layout.item_shipping_editor_card), listener)
    }

    override fun getItemCount(): Int {
        return shipperOnDemandModel.size
    }

    override fun onBindViewHolder(holder: ShippingEditorOnDemandViewHolder, position: Int) {
        holder.binData(shipperOnDemandModel[position])
    }

    fun updateData(data: List<OnDemandModel>) {
        shipperOnDemandModel.addAll(data)
        notifyDataSetChanged()
    }

    fun clearData() {
        shipperOnDemandModel.clear()
        notifyDataSetChanged()
    }

    inner class ShippingEditorOnDemandViewHolder(itemView: View, private val listener: ShippingEditorItemAdapterListener): RecyclerView.ViewHolder(itemView), ShipperProductItemAdapter.ShipperProductOnDemandItemListener {
        private val shipmentItemImage = itemView.findViewById<ImageView>(R.id.img_shipment_item)
        private val shipmentName = itemView.findViewById<Typography>(R.id.shipment_name)
        private val shipmentItemCb = itemView.findViewById<CheckboxUnify>(R.id.cb_shipment_item)
        private val shipmentCategory = itemView.findViewById<Typography>(R.id.shipment_category)
        private val shipmentProductRv = itemView.findViewById<RecyclerView>(R.id.shipment_item_list)


        fun binData(data: OnDemandModel) {
            setItemData(data)
        }

        private fun setItemData(data: OnDemandModel) {
            val shipperName = data.shipperProduct
            var sb = StringBuilder()

            shipmentItemImage?.let {
                ImageHandler.loadImageFitCenter(itemView.context, it, data.image)
            }
            shipmentName.text = data.shipperName
            shipmentItemCb.isChecked = data.isActive

            for (x in shipperName.indices) {
                sb.append(shipperName[x].shipperProductName).append(" | ")
            }

            shipmentCategory.text = sb.substring(0, sb.length - 2)

            shipperProductChild = ShipperProductItemAdapter(this@ShippingEditorOnDemandViewHolder)
            shipmentProductRv.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = shipperProductChild
            }

            shipperProductChild?.addData(data.shipperProduct)


        }

        override fun onShipperProductItemClicked() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        /* private fun setListener(data: OnDemandModel) {
            shipmentItemCb.setOnCheckedChangeListener { _, isChecked ->
                listener.onShipperInfoClicked()
            }
        }*/
    }

}