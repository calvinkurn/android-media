package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticcart.R
import com.tokopedia.logisticcart.shipping.model.PreOrderModel
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_po_duration.view.*

class PreOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val tvPreOrder = itemView.findViewById<Typography>(R.id.tv_pre_order)
    private val lblPreOrder = itemView.findViewById<Label>(R.id.lbl_pre_order)

    companion object {
        @JvmStatic
        val LAYOUT = R.layout.item_po_duration
    }

    fun bindData(data: PreOrderModel) {
        if (data.display) {
            tvPreOrder.visibility = View.VISIBLE
            lblPreOrder.visibility = View.VISIBLE
            tvPreOrder.text = data.header
            lblPreOrder.text = data.label
        }
    }
}