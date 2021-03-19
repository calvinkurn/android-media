package com.tokopedia.editshipping.ui.shippingeditor.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperProductModel
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography

class ShipperProductItemAdapter : RecyclerView.Adapter<ShipperProductItemAdapter.ShipperProductOnDemandViewHolder>() {

    private var shipperProduct = mutableListOf<ShipperProductModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShipperProductOnDemandViewHolder {
        return ShipperProductOnDemandViewHolder(parent.inflateLayout(R.layout.item_shipper_product_name))
    }

    override fun getItemCount(): Int {
        return shipperProduct.size
    }

    override fun onBindViewHolder(holder: ShipperProductOnDemandViewHolder, position: Int) {
        holder.bindData(shipperProduct[position])
    }

    override fun onViewRecycled(holder: ShipperProductOnDemandViewHolder) {
        super.onViewRecycled(holder)
        holder.shipperProductCb.setOnCheckedChangeListener(null)
    }

    fun addData(data: List<ShipperProductModel>) {
        shipperProduct.clear()
        shipperProduct.addAll(data)
        notifyDataSetChanged()
    }

    fun updateChecked(checked: Boolean) {
        shipperProduct.forEach {
            it.isActive = checked
        }
        notifyDataSetChanged()
    }

    inner class ShipperProductOnDemandViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val shipperProductName = itemView.findViewById<Typography>(R.id.shipper_product_name)
        val shipperProductCb = itemView.findViewById<CheckboxUnify>(R.id.shipper_product_cb)
        private val divider = itemView.findViewById<View>(R.id.divider_shipment)

        fun bindData(data: ShipperProductModel) {
            setItemData(data)
        }

        private fun setItemData(data: ShipperProductModel) {
            val lastItem = shipperProduct.last()
            shipperProductName.text = data.shipperProductName
            shipperProductCb.isChecked = data.isActive

            if (data == lastItem) {
                divider.visibility = View.GONE
            }

            shipperProductCb?.setOnCheckedChangeListener { _, isChecked ->
                data.isActive = isChecked
            }
        }
    }
}