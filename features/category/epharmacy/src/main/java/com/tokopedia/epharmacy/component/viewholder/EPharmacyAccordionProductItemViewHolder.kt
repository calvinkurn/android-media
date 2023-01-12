package com.tokopedia.epharmacy.component.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.component.model.EPharmacyAccordionProductDataModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifyprinciples.Typography

class EPharmacyAccordionProductItemViewHolder(val view: View) : AbstractViewHolder<EPharmacyAccordionProductDataModel>(view) {

    private val productText = view.findViewById<Typography>(R.id.lbl_PAP_productName)
    private val productQuantity = view.findViewById<Typography>(R.id.lbl_PAP_productWeight)
    private val productImageUnify = view.findViewById<ImageView>(R.id.product_image)

    companion object {
        val LAYOUT = R.layout.epharmacy_accordion_product_view_item
    }

    override fun bind(element: EPharmacyAccordionProductDataModel?) {
        renderProductData(element?.product)
    }

    private fun renderProductData(product: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product?) {
        productText.text = product?.name ?: ""
        productQuantity.text = java.lang.String.format(
            itemView.context.getString(R.string.epharmacy_quantity_weight_text),
            product?.quantity ?: "",
            product?.productTotalWeightFmt ?: ""
        )
        productImageUnify.loadImage(product?.productImage)
    }
}
