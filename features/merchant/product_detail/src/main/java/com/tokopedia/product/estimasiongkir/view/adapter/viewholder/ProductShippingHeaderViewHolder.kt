package com.tokopedia.product.estimasiongkir.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.view.util.boldOrLinkText
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingHeaderDataModel
import com.tokopedia.unifycomponents.HtmlLinkHelper

/**
 * Created by Yehezkiel on 25/01/21
 */
class ProductShippingHeaderViewHolder(view: View) : AbstractViewHolder<ProductShippingHeaderDataModel>(view) {
    companion object {
        val LAYOUT = R.layout.item_product_shipping_header
    }

    private val txtShippingFrom: com.tokopedia.unifyprinciples.Typography? = itemView.findViewById(R.id.txt_shipping_header_from)
    private val txtShippingTo: com.tokopedia.unifyprinciples.Typography? = itemView.findViewById(R.id.txt_shipping_header_to)
    private val labelFreeOngkir: com.tokopedia.unifyprinciples.Typography? = itemView.findViewById(R.id.label_pdp_shipping_bo)
    private val txtFreeOngkirEstimation: com.tokopedia.unifyprinciples.Typography? = itemView.findViewById(R.id.txt_pdp_shipping_bo_estimation)
    private val txtWeight: com.tokopedia.unifyprinciples.Typography? = itemView.findViewById(R.id.txt_pdp_shipping_weight)
    private val imgFreeOngkir: ImageView? = itemView.findViewById(R.id.img_pdp_shipping_bo)

    override fun bind(element: ProductShippingHeaderDataModel) {
        renderShipmentTrack(element.shippingTo, element.shippingFrom)
        renderBo(element.isFreeOngkir, element.freeOngkirEstimation)
        renderWeight(element.weight)
    }

    private fun renderWeight(weightFormatted: String) = with(itemView) {
        txtWeight?.text = HtmlLinkHelper(context, context.getString(R.string.pdp_shipping_weight_builder, weightFormatted)).spannedString
    }

    private fun renderBo(freeOngkir: Boolean, freeOngkirEstimation: String) = with(itemView) {
        labelFreeOngkir?.showWithCondition(freeOngkir)
        imgFreeOngkir?.showWithCondition(freeOngkir)
        txtFreeOngkirEstimation?.shouldShowWithAction(freeOngkirEstimation.isNotEmpty()) {
            txtFreeOngkirEstimation.text = freeOngkirEstimation
        }
    }

    private fun renderShipmentTrack(shippingTo: String, shippingFrom: String) = with(itemView) {
        txtShippingTo?.text = context.getString(R.string.pdp_shipping_to_builder, shippingTo).boldOrLinkText(false, context, shippingTo to {})
        txtShippingFrom?.text = context.getString(R.string.pdp_shipping_from_builder, shippingFrom).boldOrLinkText(false, context, shippingFrom to {})
    }
}