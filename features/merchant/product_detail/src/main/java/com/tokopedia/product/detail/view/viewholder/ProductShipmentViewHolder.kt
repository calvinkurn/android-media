package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductShipmentDataModel
import com.tokopedia.product.detail.data.model.ratesestimate.P2RatesEstimateData
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.boldOrLinkText
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.LocalLoad
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
    private val shipmentLocalLoad: LocalLoad? = itemView.findViewById(R.id.local_load_pdp_shipment)
    private val shipmentSeparator: View? = itemView.findViewById(R.id.pdp_shipment_separator)

    private val shipmentTitle: Typography? = itemView.findViewById(R.id.txt_pdp_shipment_title)
    private val shipmentDestination: Typography? = itemView.findViewById(R.id.txt_pdp_shipment_destination)
    private val shipmentEstimation: Typography? = itemView.findViewById(R.id.txt_pdp_shipment_estimation)
    private val staticTxtKhusus: Typography? = itemView.findViewById(R.id.txt_pdp_shipment_khusus)
    private val shipmentFullfillment: Typography? = itemView.findViewById(R.id.txt_pdp_shipment_fullfillment)
    private val otherCourierTxt: Typography? = itemView.findViewById(R.id.txt_pdp_shipment_other_courier)
    private val shipmentFreeOngkir: ImageView? = itemView.findViewById(R.id.ic_pdp_shipment_bo)
    private val shipmentLabelCod: Label? = itemView.findViewById(R.id.label_pdp_shipment_cod)
    private val shipmentLabelInstant: Label? = itemView.findViewById(R.id.label_pdp_shipment_instant)

    override fun bind(element: ProductShipmentDataModel) {
        val data = element.rates

        when {
            element.shouldShowShipmentError -> {
                // data rates not found
                showLocalLoad()
            }
            data.title.isEmpty() -> {
                // still render from p1,rates is'nt hit yet
                showShipmentLoading()
            }
            else -> {
                // receive rates data
                itemView.setOnClickListener {
                    listener.openShipmentClickedBottomSheet()
                }
                hideShipmentLoading()
                renderText(data)
                renderTokoCabang(element.isFullfillment)
                renderOtherSection(data.isSupportInstantCourier, data.subtitle, element.isCod, element.isFullfillment, element.freeOngkirUrl)
            }
        }
    }

    private fun showLocalLoad() = with(itemView) {
        shipmentLoadingContainer?.hide()
        shipmentContentContainer?.hide()
        shipmentLocalLoad?.show()
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
        otherCourierTxt?.setWeight(Typography.BOLD)
        otherCourierTxt?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
        otherCourierTxt?.show()
    }

    private fun renderSubtitleNormal(subtitle: String) = with(itemView) {
        otherCourierTxt?.text = subtitle
        otherCourierTxt?.setWeight(Typography.REGULAR)
        otherCourierTxt?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
        otherCourierTxt?.show()
    }

    private fun renderTokoCabang(isFullfillment: Boolean) = with(itemView) {
        if (isFullfillment) {
            shipmentFullfillment?.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.ic_pdp_tokocabang), null, null, null)
            staticTxtKhusus?.show()
            shipmentFullfillment?.show()
        } else {
            staticTxtKhusus?.gone()
            shipmentFullfillment?.gone()
        }
    }

    private fun renderText(data: P2RatesEstimateData) = with(itemView) {
        shipmentTitle?.text = data.title
        shipmentDestination?.shouldShowWithAction(data.destination.isNotEmpty()) {
            shipmentDestination.text = context.getString(R.string.pdp_shipping_to_simple_builder, data.destination)
                    .boldOrLinkText(false, context, Pair(data.destination, {}))
        }
        shipmentEstimation?.shouldShowWithAction(data.etaText.isNotEmpty()) {
            shipmentEstimation.text = data.etaText
        }
    }

    private fun showShipmentLoading() = with(itemView) {
        shipmentLocalLoad?.hide()
        shipmentContentContainer?.hide()
        shipmentLoadingContainer?.show()
    }

    private fun hideShipmentLoading() = with(itemView) {
        shipmentLocalLoad?.hide()
        shipmentLoadingContainer?.hide()
        shipmentContentContainer?.show()
    }

}