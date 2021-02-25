package com.tokopedia.product.estimasiongkir.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.view.util.boldOrLinkText
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingHeaderDataModel
import com.tokopedia.product.estimasiongkir.view.bottomsheet.ProductDetailShippingListener
import com.tokopedia.unifycomponents.HtmlLinkHelper

/**
 * Created by Yehezkiel on 25/01/21
 */
class ProductShippingHeaderViewHolder(view: View,
                                      private val listener: ProductDetailShippingListener,
                                      private val chooseAddressListener: ChooseAddressWidget.ChooseAddressWidgetListener) : AbstractViewHolder<ProductShippingHeaderDataModel>(view) {
    companion object {
        val LAYOUT = R.layout.item_product_shipping_header
    }
    private val txtShippingFrom: com.tokopedia.unifyprinciples.Typography? = itemView.findViewById(R.id.txt_shipping_header_from)
    private val txtShippingTo: ChooseAddressWidget? = itemView.findViewById(R.id.txt_shipping_header_to)
    private val txtFreeOngkirPrice: com.tokopedia.unifyprinciples.Typography? = itemView.findViewById(R.id.txt_pdp_shipping_bo_price)
    private val txtFreeOngkirEstimation: com.tokopedia.unifyprinciples.Typography? = itemView.findViewById(R.id.txt_pdp_shipping_bo_estimation)
    private val txtWeight: com.tokopedia.unifyprinciples.Typography? = itemView.findViewById(R.id.txt_pdp_shipping_weight)
    private val txtTokoCabang: com.tokopedia.unifyprinciples.Typography? = itemView.findViewById(R.id.txt_shipping_tokocabang)
    private val icTokoCabang: IconUnify? = itemView.findViewById(R.id.pdp_ic_location_from)
    private val imgFreeOngkir: ImageView? = itemView.findViewById(R.id.img_pdp_shipping_bo)


    override fun bind(element: ProductShippingHeaderDataModel) {
        txtShippingTo?.bindChooseAddress(chooseAddressListener)

        renderShipmentTrack(element.shippingFrom, element.isFulfillment)
        renderBo(element.isFreeOngkir, element.freeOngkirEstimation, element.freeOngkirImageUrl, element.freeOngkirPrice)
        renderWeight(element.weight)
    }

    private fun renderWeight(weightFormatted: String) = with(itemView) {
        txtWeight?.text = HtmlLinkHelper(context, context.getString(R.string.pdp_shipping_weight_builder, weightFormatted)).spannedString
    }

    private fun renderBo(freeOngkir: Boolean, freeOngkirEstimation: String, freeOngkirImageUrl: String, freeOngkirPrice: String) = with(itemView) {
        txtFreeOngkirPrice?.shouldShowWithAction(freeOngkir && freeOngkirPrice.isNotEmpty()) {
            txtFreeOngkirPrice.text = freeOngkirPrice
        }
        imgFreeOngkir?.shouldShowWithAction(freeOngkir && freeOngkirImageUrl.isNotEmpty()) {
            imgFreeOngkir.loadImage(freeOngkirImageUrl)
        }
        txtFreeOngkirEstimation?.shouldShowWithAction(freeOngkirEstimation.isNotEmpty()) {
            txtFreeOngkirEstimation.text = freeOngkirEstimation
        }
    }

    private fun renderShipmentTrack(shippingFrom: String, isFullfillment: Boolean) = with(itemView) {

        if (isFullfillment) {
            icTokoCabang?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pdp_tokocabang))
        } else {
            icTokoCabang?.setImage(IconUnify.LOCATION)
        }

        txtShippingFrom?.text = context.getString(R.string.pdp_shipping_from_builder, shippingFrom).boldOrLinkText(false, context, shippingFrom to {})
    }
}