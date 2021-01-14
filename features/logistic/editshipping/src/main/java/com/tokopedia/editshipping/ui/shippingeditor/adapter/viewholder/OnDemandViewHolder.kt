package com.tokopedia.editshipping.ui.shippingeditor.adapter.viewholder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.ui.shippingeditor.ShippingEditorListItemAdapter
import com.tokopedia.logisticCommon.data.entity.shippingeditor.OnDemandModel
import com.tokopedia.unifyprinciples.Typography

class OnDemandViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private var shipmentItemImage: ImageView? = null
    private var shipmentName: Typography? = null

    init {
        shipmentItemImage = itemView.findViewById(R.id.img_shipment_item)
        shipmentName = itemView.findViewById(R.id.shipment_name)
    }

    fun bind(data: OnDemandModel, listener: ShippingEditorListItemAdapter.ShippingEditorItemAdapterListener) {
        shipmentName?.text = data.name
    }
}