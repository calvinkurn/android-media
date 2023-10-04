package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticcart.databinding.ItemProductShipmentDetailBinding
import com.tokopedia.logisticcart.shipping.model.ProductShipmentDetailModel
import com.tokopedia.logisticcart.R as logisticcartR

class ProductShipmentDetailViewHolder(val binding: ItemProductShipmentDetailBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        @JvmStatic
        val LAYOUT = 4
    }

    fun bindData(data: ProductShipmentDetailModel) {
        if (!data.originCity.isNullOrEmpty() && !data.weight.isNullOrEmpty()) {
            val text = binding.root.context.getString(
                logisticcartR.string.checkout_product_shipment_detail_view_holder_description_template,
                data.originCity,
                data.weight
            )
            binding.tvWeightOrigin.text = text
            binding.groupShipmentDetail.visible()
        } else {
            binding.groupShipmentDetail.gone()
        }

        if (data.preOrderModel != null && data.preOrderModel.display) {
            binding.tvPreorderInfo.text = "${data.preOrderModel.header} (${data.preOrderModel.label})"
            binding.groupPreorder.visible()
        } else {
            binding.groupPreorder.gone()
        }
    }
}
