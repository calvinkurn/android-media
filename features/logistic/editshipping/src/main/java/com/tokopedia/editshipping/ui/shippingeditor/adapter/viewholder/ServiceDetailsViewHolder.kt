package com.tokopedia.editshipping.ui.shippingeditor.adapter.viewholder

import android.view.View
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.domain.model.shippingEditor.ServiceDetailsModel
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperDetailVisitable
import com.tokopedia.editshipping.ui.shippingeditor.adapter.ShippingEditorDetailsAdapter
import com.tokopedia.unifyprinciples.Typography

class ServiceDetailsViewHolder(itemView: View): ShippingEditorDetailsAdapter.BaseViewHolder<ShipperDetailVisitable>(itemView) {

    private val tvServiceHeader = itemView.findViewById<Typography>(R.id.tv_service_name)
    private val tvServiceDesc = itemView.findViewById<Typography>(R.id.tv_service_desc)

    override fun bind(item: ShipperDetailVisitable, position: Int) {
        if (item is ServiceDetailsModel) {
            tvServiceHeader.text = item.header
            tvServiceDesc.text = item.description
        }
    }
}