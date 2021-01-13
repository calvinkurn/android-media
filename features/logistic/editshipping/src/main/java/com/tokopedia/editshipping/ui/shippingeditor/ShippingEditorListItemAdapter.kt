package com.tokopedia.editshipping.ui.shippingeditor

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.editshipping.R
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.logisticCommon.data.entity.shippingeditor.ShipperListModel

class ShippingEditorListItemAdapter(private val listener: ShippingEditorItemAdapterListener): RecyclerView.Adapter<ShippingEditorListItemAdapter.ShippingEditorViewHolder>() {

    var shipperList = mutableListOf<ShipperListModel>()

    interface ShippingEditorItemAdapterListener {
            fun onShipperInfoClicker()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShippingEditorViewHolder {
        return ShippingEditorViewHolder(parent.inflateLayout(R.layout.item_shipping_editor_card), listener)
    }

    override fun getItemCount(): Int {
        return shipperList.size
    }

    override fun onBindViewHolder(holder: ShippingEditorViewHolder, position: Int) {
        holder.binData(shipperList[position])
    }


    inner class ShippingEditorViewHolder(itemView: View, private val listener: ShippingEditorItemAdapterListener): RecyclerView.ViewHolder(itemView) {

        fun binData(data: ShipperListModel) {
            with(itemView) {

            }
        }
    }

}