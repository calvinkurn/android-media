package com.tokopedia.editshipping.ui.shippingeditor.adapter.viewholder

import com.tokopedia.editshipping.databinding.ItemFeatureDetailBinding
import com.tokopedia.editshipping.domain.model.shippingEditor.FeatureDetailsModel
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperDetailVisitable
import com.tokopedia.editshipping.ui.shippingeditor.adapter.ShippingEditorDetailsAdapter

class FeatureDetailsViewHolder(private val binding: ItemFeatureDetailBinding) :
    ShippingEditorDetailsAdapter.BaseViewHolder<ShipperDetailVisitable>(binding.root) {

    override fun bind(item: ShipperDetailVisitable, position: Int) {
        if (item is FeatureDetailsModel) {
            binding.tvFeatureName.text = item.header
            binding.tvFeatureDesc.text = item.description
        }
    }
}
