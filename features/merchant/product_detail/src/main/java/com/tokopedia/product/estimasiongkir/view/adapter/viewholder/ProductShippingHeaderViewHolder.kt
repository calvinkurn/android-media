package com.tokopedia.product.estimasiongkir.view.adapter.viewholder

import android.graphics.Paint
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.getCurrencyFormatted
import com.tokopedia.product.detail.databinding.ViewFreeShippingEtaBinding
import com.tokopedia.product.detail.view.util.boldOrLinkText
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingHeaderDataModel
import com.tokopedia.product.estimasiongkir.data.model.v3.FreeShipping
import com.tokopedia.product.estimasiongkir.view.bottomsheet.ProductDetailShippingListener
import com.tokopedia.product.share.ekstensions.layoutInflater
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Yehezkiel on 25/01/21
 */
class ProductShippingHeaderViewHolder(view: View,
                                      private val listener: ProductDetailShippingListener,
                                      private val chooseAddressListener: ChooseAddressWidget.ChooseAddressWidgetListener) : AbstractViewHolder<ProductShippingHeaderDataModel>(view) {
    companion object {
        val LAYOUT = R.layout.item_product_shipping_header

        private const val MIN_MULTI_ETAS = 1
    }

    private val txtShippingFrom: Typography? = itemView.findViewById(R.id.txt_shipping_header_from)
    private val txtShippingTo: ChooseAddressWidget? = itemView.findViewById(R.id.txt_shipping_header_to)
    private val txtFreeOngkirPrice: Typography? = itemView.findViewById(R.id.txt_pdp_shipping_bo_price)
    private val txtFreeOngkirEstimation: Typography? = itemView.findViewById(R.id.txt_pdp_shipping_bo_estimation)
    private val txtTokoNow: Typography? = itemView.findViewById(R.id.txt_pdp_shipping_tokonow)
    private val txtWeight: Typography? = itemView.findViewById(R.id.txt_pdp_shipping_weight)
    private val txtTokoCabang: Typography? = itemView.findViewById(R.id.txt_shipping_tokocabang)
    private val icTokoCabang: IconUnify? = itemView.findViewById(R.id.pdp_ic_location_from)
    private val imgFreeOngkir: ImageView? = itemView.findViewById(R.id.img_pdp_shipping_bo)
    private val icShippingLine: View? = itemView.findViewById(R.id.pdp_shipping_header_separator)
    private val dividerFreeOngkir: View? = itemView.findViewById(R.id.div_pdp_shipping_bo)
    private val txtFreeOngkirPriceOriginal: Typography? = itemView.findViewById(R.id.txt_pdp_shipping_bo_price_original)
    private val txtFreeOngkirDesc: Typography? = itemView.findViewById(R.id.txt_pdp_shipping_bo_desc)
    private val viewFreeOngkirEtas: LinearLayout? = itemView.findViewById(R.id.view_pdp_shipping_bo_etas)

    override fun bind(element: ProductShippingHeaderDataModel) {
        txtShippingTo?.bindChooseAddress(chooseAddressListener)

        renderTokoCabang(element)
        renderBo(
            element.shouldShowFreeOngkir(),
            element.freeOngkirEstimation,
            element.freeOngkirImageUrl,
            element.freeOngkirPrice,
            element.shouldShowTxtTokoNow(),
            element.freeOngkirTokoNowText,
            element.freeOngkirDesc,
            element.isFreeOngkirQuotaEmpty,
            element.freeOngkirEtas
        )
        renderWeight(element.weight)
    }

    private fun renderTokoCabang(element: ProductShippingHeaderDataModel) = with(itemView) {
        if (element.boType == ProductDetailCommonConstant.BO_TOKONOW || element.boType == ProductDetailCommonConstant.BO_TOKONOW_15) {
            icShippingLine?.setMargin(0, 0, 0, 0)
            txtShippingFrom?.text = context.getString(R.string.pdp_shipping_from_builder, element.tokoCabangTitle).boldOrLinkText(false, context, element.tokoCabangTitle to {})
            renderGeneralContentTokoCabang(element.tokoCabangContent)
        } else if (shouldRenderFulFillment(element)) {
            icShippingLine?.setMargin(0, 20.toDp(), 0, 20.toDp())
            renderGeneralContentTokoCabang(element.tokoCabangContent, element.uspTokoCabangImgUrl)
            txtShippingFrom?.text = HtmlLinkHelper(context, context.getString(R.string.pdp_bold_html_builder, element.tokoCabangTitle)).spannedString
            icTokoCabang?.loadImage(element.tokoCabangIcon)
        } else {
            icShippingLine?.setMargin(0, 0, 0, 0)
            txtTokoCabang?.hide()
            icTokoCabang?.setImage(IconUnify.LOCATION)
            txtShippingFrom?.text = context.getString(R.string.pdp_shipping_from_builder, element.shippingFrom).boldOrLinkText(false, context, element.shippingFrom to {})
        }
    }

    private fun shouldRenderFulFillment(element: ProductShippingHeaderDataModel): Boolean {
        val boTypes = listOf(
            ProductDetailCommonConstant.BEBAS_ONGKIR_NORMAL,
            ProductDetailCommonConstant.BEBAS_ONGKIR_EXTRA,
            ProductDetailCommonConstant.BO_PLUS,
            ProductDetailCommonConstant.BO_PLUS_DT
        )
        return element.isFulfillment && boTypes.contains(element.boType)
    }

    private fun renderGeneralContentTokoCabang(tokoCabangContent: String, uspTokoCabangImgUrl: String = "") = with(itemView) {
        txtTokoCabang?.shouldShowWithAction(tokoCabangContent.isNotEmpty()) {
            val linkHelper = HtmlLinkHelper(context, tokoCabangContent)
            txtTokoCabang.text = linkHelper.spannedString
            txtTokoCabang.movementMethod = LinkMovementMethod.getInstance()
            linkHelper.urlList.getOrNull(0)?.onClick = {
                listener.openUspBottomSheet(uspTokoCabangImgUrl)
            }
        }
    }

    private fun renderWeight(weightFormatted: String) = with(itemView) {
        txtWeight?.text = HtmlLinkHelper(context, context.getString(R.string.pdp_shipping_weight_builder, weightFormatted)).spannedString
    }

    private fun renderBo(
        isFreeOngkir: Boolean,
        freeOngkirEstimation: String,
        freeOngkirImageUrl: String,
        freeOngkirPrice: String,
        shouldShowTxtTokoNow: Boolean,
        freeOngkirTokoNowText: String,
        freeOngkirDesc: String,
        isFreeOngkirQuotaEmpty: Boolean,
        freeOngkirEtas: List<FreeShipping.Eta>
    ) {
        if (shouldShowTxtTokoNow) {
            renderBoTokoNow(shouldShowTxtTokoNow, freeOngkirEstimation, freeOngkirPrice, freeOngkirTokoNowText)
        } else {
            renderBoNormal(isFreeOngkir, freeOngkirImageUrl, freeOngkirDesc, isFreeOngkirQuotaEmpty, freeOngkirEtas)
        }
    }

    private fun renderBoNormal(
        isFreeOngkir: Boolean,
        freeOngkirImageUrl: String,
        freeOngkirDesc: String,
        isFreeOngkirQuotaEmpty: Boolean,
        freeOngkirEtas: List<FreeShipping.Eta>
    ) = with(itemView) {

        imgFreeOngkir?.shouldShowWithAction(isFreeOngkir && freeOngkirImageUrl.isNotEmpty()) {
            imgFreeOngkir.loadImage(freeOngkirImageUrl)
        }

        txtFreeOngkirDesc?.showIfWithBlock(isFreeOngkir && freeOngkirDesc.isNotEmpty()) {
            text = freeOngkirDesc
        }

        dividerFreeOngkir?.showWithCondition(isFreeOngkir)

        txtTokoNow?.hide()

        if (freeOngkirEtas.size > MIN_MULTI_ETAS) renderMultipleBoEta(
            freeOngkirEtas,
            isFreeOngkir,
            isFreeOngkirQuotaEmpty
        )
        else renderSingleBoEta(freeOngkirEtas.firstOrNull(), isFreeOngkir, isFreeOngkirQuotaEmpty)
    }

    private fun renderSingleBoEta(
        freeOngkirEta: FreeShipping.Eta?,
        isFreeOngkir: Boolean,
        isFreeOngkirQuotaEmpty: Boolean
    ) {
        if (freeOngkirEta == null) return
        val freeOngkirPrice = freeOngkirEta.shippingPrice
        val freeOngkirEstimation = freeOngkirEta.etaText
        val freeOngkirPriceOriginal = freeOngkirEta.rawShippingRate

        txtFreeOngkirPrice?.shouldShowWithAction(isFreeOngkir && !isFreeOngkirQuotaEmpty && freeOngkirPrice.isNotEmpty()) {
            txtFreeOngkirPrice.text = freeOngkirPrice
        }
        txtFreeOngkirEstimation?.shouldShowWithAction(isFreeOngkir && freeOngkirEstimation.isNotEmpty()) {
            txtFreeOngkirEstimation.text = freeOngkirEstimation
        }

        txtFreeOngkirPriceOriginal?.showIfWithBlock(isFreeOngkir && !isFreeOngkirQuotaEmpty && freeOngkirPriceOriginal > Int.ZERO) {
            text = freeOngkirPriceOriginal.getCurrencyFormatted()
            paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    private fun renderMultipleBoEta(
        freeOngkirEtas: List<FreeShipping.Eta>,
        isFreeOngkir: Boolean,
        isFreeOngkirQuotaEmpty: Boolean
    ) {
        viewFreeOngkirEtas?.removeAllViews()

        freeOngkirEtas.forEach { eta ->
            val etaView = ViewFreeShippingEtaBinding.inflate(itemView.context.layoutInflater)

            etaView.pdpFreeShippingEtaText.showIfWithBlock(isFreeOngkir && eta.etaText.isNotEmpty()) {
                text = eta.etaText
            }

            etaView.pdpFreeShippingOriginalPrice.showIfWithBlock(isFreeOngkir && !isFreeOngkirQuotaEmpty && eta.rawShippingRate > Int.ZERO) {
                text = eta.rawShippingRate.getCurrencyFormatted()
                paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }

            etaView.pdpFreeShippingPrice.showIfWithBlock(isFreeOngkir && !isFreeOngkirQuotaEmpty && eta.shippingPrice.isNotEmpty()) {
                text = eta.shippingPrice
            }

            viewFreeOngkirEtas?.addView(etaView.root)
        }
    }

    private fun renderBoTokoNow(shouldShowTxtTokoNow: Boolean, freeOngkirEstimation: String, freeOngkirPrice: String, freeOngkirTokoNowText: String) = with(itemView) {

        dividerFreeOngkir?.show()

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