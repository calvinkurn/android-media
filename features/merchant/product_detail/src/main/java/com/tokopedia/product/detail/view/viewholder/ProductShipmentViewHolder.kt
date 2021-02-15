package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductShipmentDataModel
import com.tokopedia.product.detail.data.model.ratesestimate.P2RatesEstimateData
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Yehezkiel on 10/02/21
 */
class ProductShipmentViewHolder(view: View, private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductShipmentDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_shipment
    }

    private val shipmentLoadingContainer: ConstraintLayout? = itemView.findViewById(R.id.pdp_shimmer_shipment_container)
    private val shipmentContentContainer: ConstraintLayout? = itemView.findViewById(R.id.pdp_shipment_container)
    private val shipmentTitle: Typography? = itemView.findViewById(R.id.txt_pdp_shipment_title)
    private val shipmentSubtitle: Typography? = itemView.findViewById(R.id.txt_pdp_shipment_subtitle)
    private val shipmentEstimation: Typography? = itemView.findViewById(R.id.txt_pdp_shipment_estimation)
    private val staticTxtKhusus: Typography? = itemView.findViewById(R.id.txt_pdp_shipment_khusus)
    private val shipmentFullfillment: Typography? = itemView.findViewById(R.id.txt_pdp_shipment_fullfillment)
    private val otherCourierTxt: Typography? = itemView.findViewById(R.id.txt_pdp_shipment_other_courier)
    private val shipmentFreeOngkir: ImageView? = itemView.findViewById(R.id.ic_pdp_shipment_bo)
    private val shipmentLabelCod: ImageView? = itemView.findViewById(R.id.label_pdp_shipment_cod)
    private val shipmentLabelInstant: ImageView? = itemView.findViewById(R.id.label_pdp_shipment_instant)

    override fun bind(element: ProductShipmentDataModel) {
        val data = element.rates
        if (data.title.isEmpty()) {
            showShipmentLoading()
        } else {
            hideShipmentLoading()
        }

        renderText(data)
        renderTokoCabang(element.isFullfillment)
        renderOtherSection(data.isSupportInstantCourier, data.subtitle, element.isCod, element.isFullfillment, element.freeOngkirUrl)
    }

    private fun renderOtherSection(isInstant: Boolean, subtitle: String, isCod: Boolean, isFullfillment: Boolean, freeOngkirUrl: String) = with(itemView) {
        if (!isInstant && freeOngkirUrl.isEmpty() && !isCod && !isFullfillment) {
            renderSubtitleGreen()
            shipmentFreeOngkir?.hide()
            shipmentLabelCod?.hide()
            shipmentLabelInstant?.hide()
            return@with
        }

        renderSubtitleNormal(subtitle)

        if (freeOngkirUrl.isNotEmpty()) {
            shipmentFreeOngkir?.loadImage(freeOngkirUrl)
            shipmentFreeOngkir?.show()
        } else {
            shipmentFreeOngkir?.hide()
        }

        if (isCod) {
            shipmentLabelCod?.show()
        } else {
            shipmentLabelCod?.hide()
        }

        if (isFullfillment) {
            shipmentLabelInstant?.show()
        } else {
            shipmentLabelInstant?.hide()
        }
    }

    private fun renderSubtitleGreen() = with(itemView) {
        otherCourierTxt?.text = context.getString(R.string.pdp_shipping_choose_courier_label)
        otherCourierTxt?.setType(Typography.BOLD)
        otherCourierTxt?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
        otherCourierTxt?.show()
    }

    private fun renderSubtitleNormal(subtitle: String) = with(itemView) {
        otherCourierTxt?.text = subtitle
        otherCourierTxt?.setType(Typography.REGULAR)
        otherCourierTxt?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
        otherCourierTxt?.show()
    }

    private fun renderTokoCabang(isFullfillment: Boolean) = with(itemView) {
        if (isFullfillment) {
            staticTxtKhusus?.show()
            shipmentFullfillment?.show()
        } else {
            staticTxtKhusus?.gone()
            shipmentFullfillment?.gone()
        }
    }

    private fun renderText(data: P2RatesEstimateData) = with(itemView) {
        shipmentTitle?.text = data.title
        shipmentSubtitle?.text = HtmlLinkHelper(context, data.destination).spannedString
        shipmentEstimation?.text = data.etaText

    }

    private fun showShipmentLoading() = with(itemView) {
        shipmentContentContainer?.hide()
        shipmentLoadingContainer?.show()
    }

    private fun hideShipmentLoading() = with(itemView) {
        shipmentLoadingContainer?.hide()
        shipmentContentContainer?.show()
    }
}