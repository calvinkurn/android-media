package com.tokopedia.product.estimasiongkir.view.adapter.viewholder

import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.util.boldOrLinkText
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingHeaderDataModel
import com.tokopedia.product.estimasiongkir.view.bottomsheet.ProductDetailShippingListener
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.toDp

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
    private val txtTokoNow: com.tokopedia.unifyprinciples.Typography? = itemView.findViewById(R.id.txt_pdp_shipping_tokonow)
    private val txtWeight: com.tokopedia.unifyprinciples.Typography? = itemView.findViewById(R.id.txt_pdp_shipping_weight)
    private val txtTokoCabang: com.tokopedia.unifyprinciples.Typography? = itemView.findViewById(R.id.txt_shipping_tokocabang)
    private val icTokoCabang: IconUnify? = itemView.findViewById(R.id.pdp_ic_location_from)
    private val imgFreeOngkir: ImageView? = itemView.findViewById(R.id.img_pdp_shipping_bo)
    private val icShippingLine: View? = itemView.findViewById(R.id.pdp_shipping_header_separator)

    override fun bind(element: ProductShippingHeaderDataModel) {
        txtShippingTo?.bindChooseAddress(chooseAddressListener)

        renderTokoCabang(element)
        renderBo(element.shouldShowFreeOngkir(), element.freeOngkirEstimation, element.freeOngkirImageUrl, element.freeOngkirPrice, element.shouldShowTxtTokoNow(), element.freeOngkirTokoNowText)
        renderWeight(element.weight)
    }

    private fun renderTokoCabang(element: ProductShippingHeaderDataModel) = with(itemView) {
        if (element.boType == ProductDetailCommonConstant.BO_TOKONOW) {
            icShippingLine?.setMargin(0, 0, 0, 0)
            txtShippingFrom?.text = context.getString(R.string.pdp_shipping_from_builder, element.tokoCabangTitle).boldOrLinkText(false, context, element.tokoCabangTitle to {})
            renderGeneralContentTokoCabang(element.tokoCabangContent)
        } else if (element.isFulfillment && element.boType == ProductDetailCommonConstant.BEBAS_ONGKIR_EXTRA) {
            icShippingLine?.setMargin(0, 20.toDp(), 0, 20.toDp())
            renderGeneralContentTokoCabang(element.tokoCabangContent, element.freeOngkirImageUrl, element.uspTokoCabangImgUrl)
            txtShippingFrom?.text = HtmlLinkHelper(context, context.getString(R.string.pdp_bold_html_builder, element.tokoCabangTitle)).spannedString
            icTokoCabang?.loadImage(element.tokoCabangIcon)
        } else {
            icShippingLine?.setMargin(0, 0, 0, 0)
            txtTokoCabang?.hide()
            icTokoCabang?.setImage(IconUnify.LOCATION)
            txtShippingFrom?.text = context.getString(R.string.pdp_shipping_from_builder, element.shippingFrom).boldOrLinkText(false, context, element.shippingFrom to {})
        }
    }

    private fun renderGeneralContentTokoCabang(tokoCabangContent: String, freeOngkirImageUrl: String = "", uspTokoCabangImgUrl: String = "") = with(itemView) {
        txtTokoCabang?.shouldShowWithAction(tokoCabangContent.isNotEmpty()) {
            val linkHelper = HtmlLinkHelper(context, tokoCabangContent)
            txtTokoCabang.text = linkHelper.spannedString
            txtTokoCabang.movementMethod = LinkMovementMethod.getInstance()
            linkHelper.urlList.getOrNull(0)?.onClick = {
                listener.openUspBottomSheet(freeOngkirImageUrl, uspTokoCabangImgUrl)
            }
        }
    }

    private fun renderWeight(weightFormatted: String) = with(itemView) {
        txtWeight?.text = HtmlLinkHelper(context, context.getString(R.string.pdp_shipping_weight_builder, weightFormatted)).spannedString
    }

    private fun renderBo(isFreeOngkir: Boolean, freeOngkirEstimation: String, freeOngkirImageUrl: String, freeOngkirPrice: String, shouldShowTxtTokoNow: Boolean, freeOngkirTokoNowText: String) = with(itemView) {
        if (shouldShowTxtTokoNow) {
            renderBoTokoNow(shouldShowTxtTokoNow, freeOngkirEstimation, freeOngkirPrice, freeOngkirTokoNowText)
        } else {
            renderBoNormal(isFreeOngkir, freeOngkirEstimation, freeOngkirImageUrl, freeOngkirPrice)
        }
    }

    private fun renderBoNormal(isFreeOngkir: Boolean, freeOngkirEstimation: String, freeOngkirImageUrl: String, freeOngkirPrice: String) = with(itemView) {
        txtFreeOngkirPrice?.shouldShowWithAction(isFreeOngkir && freeOngkirPrice.isNotEmpty()) {
            txtFreeOngkirPrice.text = freeOngkirPrice
        }

        imgFreeOngkir?.shouldShowWithAction(isFreeOngkir && freeOngkirImageUrl.isNotEmpty()) {
            imgFreeOngkir.loadImage(freeOngkirImageUrl)
        }
        txtFreeOngkirEstimation?.shouldShowWithAction(isFreeOngkir && freeOngkirEstimation.isNotEmpty()) {
            txtFreeOngkirEstimation.text = freeOngkirEstimation
        }

        txtTokoNow?.hide()
    }

    private fun renderBoTokoNow(shouldShowTxtTokoNow: Boolean, freeOngkirEstimation: String, freeOngkirPrice: String, freeOngkirTokoNowText: String) = with(itemView) {
        txtFreeOngkirPrice?.shouldShowWithAction(shouldShowTxtTokoNow && freeOngkirPrice.isNotEmpty()) {
            txtFreeOngkirPrice.text = freeOngkirPrice
        }

        txtTokoNow?.shouldShowWithAction(freeOngkirTokoNowText.isNotEmpty()) {
            txtTokoNow.text = freeOngkirTokoNowText
        }

        imgFreeOngkir?.hide()

        txtFreeOngkirEstimation?.shouldShowWithAction(shouldShowTxtTokoNow && freeOngkirEstimation.isNotEmpty()) {
            txtFreeOngkirEstimation.text = freeOngkirEstimation
        }
    }

}