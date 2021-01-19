package com.tokopedia.editshipping.ui.shippingeditor.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.editshipping.R
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.logisticCommon.data.entity.shippingeditor.ShipperProductModel
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography

class ShipperProductItemAdapter(private val listener: ShipperProductOnDemandItemListener): RecyclerView.Adapter<ShipperProductItemAdapter.ShipperProductOnDemandViewHolder>() {

    private var shipperProductOnDemandModel = mutableListOf<ShipperProductModel>()

    interface ShipperProductOnDemandItemListener {
        fun onShipperProductItemClicked()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShipperProductOnDemandViewHolder {
        return ShipperProductOnDemandViewHolder(parent.inflateLayout(R.layout.item_shipper_product_name))
    }

    override fun getItemCount(): Int {
        return shipperProductOnDemandModel.size
    }

    override fun onBindViewHolder(holder: ShipperProductOnDemandViewHolder, position: Int) {
        holder.bindData(shipperProductOnDemandModel[position])
    }

    fun addData(data: List<ShipperProductModel>) {
        shipperProductOnDemandModel.addAll(data)
        notifyDataSetChanged()
    }

    fun clearData() {
        shipperProductOnDemandModel.clear()
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
            val lastItem = shipperProductOnDemandModel.last()
            shipperProductName.text = data.shipperProductName
            shipperProductCb.isChecked = data.isActive

            if (data == lastItem) {
                divider.visibility = View.GONE
            }
        }
    }
}