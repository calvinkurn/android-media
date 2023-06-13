package com.tokopedia.editshipping.ui.shippingeditor.adapter.viewholder

import com.tokopedia.editshipping.databinding.ItemServiceDetailBinding
import com.tokopedia.editshipping.domain.model.shippingEditor.ServiceDetailsModel
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperDetailVisitable
import com.tokopedia.editshipping.ui.shippingeditor.adapter.ShippingEditorDetailsAdapter

class ServiceDetailsViewHolder(private val binding: ItemServiceDetailBinding) :
    ShippingEditorDetailsAdapter.BaseViewHolder<ShipperDetailVisitable>(binding.root) {
    override fun bind(item: ShipperDetailVisitable, position: Int) {
        if (item is ServiceDetailsModel) {
            binding.tvServiceName.text = item.header
            binding.tvServiceDesc.text = item.description
        }
    }
}
