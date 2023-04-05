package com.tokopedia.editshipping.ui.shippingeditor.adapter.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.editshipping.databinding.ItemShipperDetailBinding
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperDetailVisitable
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperDetailsModel
import com.tokopedia.editshipping.ui.shippingeditor.adapter.ShipperDetailsChildAdapter
import com.tokopedia.editshipping.ui.shippingeditor.adapter.ShippingEditorDetailsAdapter
import com.tokopedia.media.loader.loadImageFitCenter

class ShipperDetailsViewHolder(private val binding: ItemShipperDetailBinding) :
    ShippingEditorDetailsAdapter.BaseViewHolder<ShipperDetailVisitable>(binding.root) {
    private fun setChildData(data: ShipperDetailsModel) {
        val shipperDetailsChild = ShipperDetailsChildAdapter()
        binding.rvShipperChild.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = shipperDetailsChild
        }
        shipperDetailsChild.addData(data.shipperProduct)
    }

    override fun bind(item: ShipperDetailVisitable, position: Int) {
        if (item is ShipperDetailsModel) {
            binding.imgShipperDetail.loadImageFitCenter(item.image)
            setChildData(item)
        }
    }
}
