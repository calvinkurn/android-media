package com.tokopedia.editshipping.ui.shippingeditor

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ShippingEditorItemAdapter(private val listener: ShippingEditorItemAdapterListener): RecyclerView.Adapter<ShippingEditorItemAdapter.ShippingEditorViewHolder>() {

    interface ShippingEditorItemAdapterListener {
            fun onShipperInfoClicker()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShippingEditorViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: ShippingEditorViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    inner class ShippingEditorViewHolder(itemView: View, private val listener: ShippingEditorItemAdapterListener): RecyclerView.ViewHolder(itemView) {

    }

}