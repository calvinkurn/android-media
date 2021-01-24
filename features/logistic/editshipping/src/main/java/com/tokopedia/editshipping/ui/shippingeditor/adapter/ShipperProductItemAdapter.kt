package com.tokopedia.editshipping.ui.shippingeditor.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperProductModel
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography

class ShipperProductItemAdapter(private val listener: ShipperProductOnDemandItemListener): RecyclerView.Adapter<ShipperProductItemAdapter.ShipperProductOnDemandViewHolder>() {

    private var shipperProduct = mutableListOf<ShipperProductModel>()

    interface ShipperProductOnDemandItemListener {
        fun onShipperProductChecked(shipperId: String, isChecked: Boolean)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShipperProductOnDemandViewHolder {
        return ShipperProductOnDemandViewHolder(parent.inflateLayout(R.layout.item_shipper_product_name))
    }

    override fun getItemCount(): Int {
        return shipperProduct.size
    }

    override fun onBindViewHolder(holder: ShipperProductOnDemandViewHolder, position: Int) {
        holder.bindData(shipperProduct[position])
    }

    fun addData(data: List<ShipperProductModel>) {
        shipperProduct.addAll(data)
        notifyDataSetChanged()
    }

    fun clearData() {
        shipperProduct.clear()
        notifyDataSetChanged()
    }

    fun uncheckAll() {
        shipperProduct.forEach {
            it.isActive = false
        }
        notifyDataSetChanged()
    }

    fun checkAll() {
        shipperProduct.forEach {
            it.isActive = true
        }
        notifyDataSetChanged()
    }

    inner class ShipperProductOnDemandViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val shipperProductName = itemView.findViewById<Typography>(R.id.shipper_product_name)
        private val shipperProductCb = itemView.findViewById<CheckboxUnify>(R.id.shipper_product_cb)
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
                listener.onShipperProductChecked(data.shipperProductId, isChecked)
            }
        }
    }
}