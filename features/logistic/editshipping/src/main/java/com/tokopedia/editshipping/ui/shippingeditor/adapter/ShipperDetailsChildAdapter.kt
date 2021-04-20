package com.tokopedia.editshipping.ui.shippingeditor.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperProductDetailsModel
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.unifyprinciples.Typography

class ShipperDetailsChildAdapter : RecyclerView.Adapter<ShipperDetailsChildAdapter.ShipperDetailsChildViewHolder>() {

    private var shipperDetailsChild = mutableListOf<ShipperProductDetailsModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShipperDetailsChildViewHolder {
        return ShipperDetailsChildViewHolder(parent.inflateLayout(R.layout.item_shipper_detail_child))
    }

    override fun getItemCount(): Int {
        return shipperDetailsChild.size
    }

    override fun onBindViewHolder(holder: ShipperDetailsChildViewHolder, position: Int) {
        holder.bindData(shipperDetailsChild[position])
    }

    fun addData(data: List<ShipperProductDetailsModel>) {
        shipperDetailsChild.addAll(data)
        notifyDataSetChanged()
    }


    inner class ShipperDetailsChildViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val tvDetailShipperName = itemView.findViewById<Typography>(R.id.tv_detail_shipper_feature_name)
        private val tvDetailShipperDesc = itemView.findViewById<Typography>(R.id.tv_shipper_feature_desc)

        fun bindData(data: ShipperProductDetailsModel) {
            tvDetailShipperName.text = data.name
            tvDetailShipperDesc.text = data.description
        }
    }



}