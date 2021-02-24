package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductShipmentDataModel
import com.tokopedia.product.detail.data.model.ratesestimate.P2RatesEstimateData
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.renderHtmlBold
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.LocalLoad
import com.tokopedia.unifycomponents.toPx
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
    private val shipmentOtherContainer: ConstraintLayout? = itemView.findViewById(R.id.container_pdp_shipment_other)
    private val shipmentTokoCabangGroup: Group? = itemView.findViewById(R.id.group_fullfillment)
    private val shipmentLocalLoad: LocalLoad? = itemView.findViewById(R.id.local_load_pdp_shipment)

    private val shipmentTitle: Typography? = itemView.findViewById(R.id.txt_pdp_shipment_title)
    private val shipmentTitleIcon: ImageView? = itemView.findViewById(R.id.ic_pdp_shipment_title)
    private val shipmentDestination: Typography? = itemView.findViewById(R.id.txt_pdp_shipment_destination)
    private val shipmentEstimation: Typography? = itemView.findViewById(R.id.txt_pdp_shipment_estimation)
    private val shipmentFullfillmentImg: ImageView? = itemView.findViewById(R.id.img_ic_fullfillment)
    private val otherCourierTxt: Typography? = itemView.findViewById(R.id.txt_pdp_shipment_other_courier)
    private val shipmentFreeOngkir: ImageView? = itemView.findViewById(R.id.ic_pdp_shipment_bo)
    private val shipmentLabelCod: Label? = itemView.findViewById(R.id.label_pdp_shipment_cod)
    private val shipmentLabelInstant: Label? = itemView.findViewById(R.id.label_pdp_shipment_instant)
    private val shipmentArrow: IconUnify? = itemView.findViewById(R.id.ic_pdp_shipment_arrow_right)

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
                val isError = element.rates.p2RatesError.firstOrNull()?.errorCode ?: 0 != 0
                itemView.addOnImpressionListener(element.impressHolder) {
                    listener.showCoachmark(shipmentTitle)
                }

                hideShipmentLoading()
                renderText(data)
                renderTokoCabang(element.isFullfillment)
                renderOtherSection(data.isSupportInstantCourier, data.subtitle, element.isCod, element.freeOngkirUrl, isError)
            }
        }
    }

    private fun showLocalLoad() = with(itemView) {
        shipmentLoadingContainer?.hide()
        shipmentContentContainer?.hide()
        shipmentLocalLoad?.show()
        shipmentLocalLoad?.progressState = false
        shipmentLocalLoad?.refreshBtn?.setOnClickListener {
            if (!shipmentLocalLoad.progressState) {
                shipmentLocalLoad.progressState = true
                listener.refreshPage()
            }
        }
    }

    private fun renderOtherSection(isInstant: Boolean, subtitle: String, isCod: Boolean, freeOngkirUrl: String, isError: Boolean) = with(itemView) {
        adjustIfError(isError)

        if (!isInstant && freeOngkirUrl.isEmpty() && !isCod && !isError) {
            renderSubtitleGreen()
            shipmentFreeOngkir?.hide()
            shipmentLabelCod?.hide()
            shipmentLabelInstant?.hide()
            return@with
        }

        renderSubtitleNormal(subtitle)

        shipmentFreeOngkir?.shouldShowWithAction(freeOngkirUrl.isNotEmpty() && !isError) {
            shipmentFreeOngkir.loadImage(freeOngkirUrl)
        }
        shipmentLabelCod?.showWithCondition(isCod && !isError)
        shipmentLabelInstant?.showWithCondition(isInstant)
    }

    private fun adjustIfError(isError: Boolean) = with(itemView) {
        if (isError) {
            otherCourierTxt?.setWeight(Typography.REGULAR)
            otherCourierTxt?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
            shipmentOtherContainer?.setOnClickListener(null)
            shipmentArrow?.setOnClickListener(null)
            shipmentArrow?.hide()
            shipmentOtherContainer?.setMargin(0, 4.toPx(), 0, 0)
        } else {
            shipmentOtherContainer?.setOnClickListener {
                listener.openShipmentClickedBottomSheet()
            }
            shipmentArrow?.setOnClickListener {
                listener.openShipmentClickedBottomSheet()
            }
            shipmentArrow?.show()
            shipmentOtherContainer?.setMargin(0, 14.toPx(), 0, 0)
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

    private fun renderTokoCabang(isFullfillment: Boolean) = if (isFullfillment) {
        //TODO
        shipmentFullfillmentImg?.loadImage("https://images.tokopedia.net/img/android/test/pdp - tokocabang@3x.png")
        shipmentTokoCabangGroup?.show()
    } else {
        shipmentTokoCabangGroup?.gone()
    }

    private fun renderText(data: P2RatesEstimateData) = with(itemView) {
        shipmentTitle?.text = data.title
        shipmentTitleIcon?.loadImage(data.icon)
        shipmentDestination?.shouldShowWithAction(data.destination.isNotEmpty()) {
            shipmentDestination.text = data.destination.renderHtmlBold(context)
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