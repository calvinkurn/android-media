package com.tokopedia.editshipping.ui.shippingeditor.adapter.viewholder

import android.view.View
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.domain.model.shippingEditor.FeatureDetailsModel
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperDetailVisitable
import com.tokopedia.editshipping.ui.shippingeditor.adapter.ShippingEditorDetailsAdapter
import com.tokopedia.unifyprinciples.Typography

class FeatureDetailsViewHolder(itemView: View) : ShippingEditorDetailsAdapter.BaseViewHolder<ShipperDetailVisitable>(itemView) {

    private val tvFeatureHeader = itemView.findViewById<Typography>(R.id.tv_feature_name)
    private val tvFeatureDesc = itemView.findViewById<Typography>(R.id.tv_feature_desc)

    override fun bind(item: ShipperDetailVisitable, position: Int) {
        if (item is FeatureDetailsModel) {
            tvFeatureHeader.text = item.header
            tvFeatureDesc.text = item.description
        }
    }
}