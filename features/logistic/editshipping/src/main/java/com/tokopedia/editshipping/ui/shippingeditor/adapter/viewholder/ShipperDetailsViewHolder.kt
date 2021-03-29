package com.tokopedia.editshipping.ui.shippingeditor.adapter.viewholder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperDetailVisitable
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperDetailsModel
import com.tokopedia.editshipping.ui.shippingeditor.adapter.ShipperDetailsChildAdapter
import com.tokopedia.editshipping.ui.shippingeditor.adapter.ShippingEditorDetailsAdapter

class ShipperDetailsViewHolder(itemView: View) : ShippingEditorDetailsAdapter.BaseViewHolder<ShipperDetailVisitable>(itemView){

    private val imageShipperDetail = itemView.findViewById<ImageView>(R.id.img_shipper_detail)
    private val shipperDetailRv = itemView.findViewById<RecyclerView>(R.id.rv_shipper_child)

    private fun setChildData(data: ShipperDetailsModel) {
        val shipperDetailsChild = ShipperDetailsChildAdapter()
        shipperDetailRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = shipperDetailsChild
        }
        shipperDetailsChild.addData(data.shipperProduct)
    }

    override fun bind(item: ShipperDetailVisitable, position: Int) {
        if (item is ShipperDetailsModel) {
            ImageHandler.loadImageFitCenter(itemView.context, imageShipperDetail, item.image)
            setChildData(item)
        }
    }

}