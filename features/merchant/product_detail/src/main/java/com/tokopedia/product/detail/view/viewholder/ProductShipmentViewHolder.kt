package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.rates.FulfillmentData
import com.tokopedia.product.detail.common.data.model.rates.P2RatesEstimateData
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductShipmentDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.renderHtmlBold
import com.tokopedia.unifycomponents.HtmlLinkHelper
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

    private val shipmentSeparator: View? = itemView.findViewById(R.id.pdp_shipment_separator)
    private val shipmentTitle: Typography? = itemView.findViewById(R.id.txt_pdp_shipment_title)
    private val shipmentDestination: Typography? = itemView.findViewById(R.id.txt_pdp_shipment_destination)
    private val shipmentEstimation: Typography? = itemView.findViewById(R.id.txt_pdp_shipment_estimation)
    private val shipmentFulfillmentLabel: Typography? = itemView.findViewById(R.id.txt_pdp_shipment_khusus)
    private val shipmentFullfillmentImg: ImageView? = itemView.findViewById(R.id.img_ic_fullfillment)
    private val shipmentFulfillmentText: Typography? = itemView.findViewById(R.id.txt_pdp_shipment_fullfillment)
    private val otherCourierTxt: Typography? = itemView.findViewById(R.id.txt_pdp_shipment_other_courier)
    private val shipmentLabelCod: Label? = itemView.findViewById(R.id.label_pdp_shipment_cod)
    private val shipmentLabelInstant: Label? = itemView.findViewById(R.id.label_pdp_shipment_instant)
    private val shipmentArrow: IconUnify? = itemView.findViewById(R.id.ic_pdp_shipment_arrow_right)

    private var componentTrackDataModel: ComponentTrackDataModel? = null

    override fun bind(element: ProductShipmentDataModel) {
        val data = element.rates

        if (element.isTokoNow) {
            shipmentSeparator?.visibility = View.GONE
        }

        when {
            element.shouldShowShipmentError -> {
                // data rates not found
                showLocalLoad()
            }
            data.title.isEmpty() -> {
                // still render from p1,rates is'nt hit yet
                showShipmentLoading()
            }
            data.p2RatesError.isNotEmpty() -> {
                hideShipmentLoading()
                renderShipmentError(data.title, data.subtitle, data.p2RatesError.firstOrNull()?.errorCode
                        ?: 0)
            }
            else -> {
                // receive rates data
                if (componentTrackDataModel == null) {
                    componentTrackDataModel = getComponentTrackData(element)
                }

                itemView.addOnImpressionListener(element.impressHolder) {
                    listener.onImpressComponent(getComponentTrackData(element))
                }

                renderShipmentSuccess(element)
            }
        }
    }

    private fun renderShipmentError(title: String, subtitle: String, errorCode: Int) = with(itemView) {
        adjustUiError()

        shipmentTitle?.text = title
        otherCourierTxt?.text = HtmlLinkHelper(context, subtitle).spannedString

        shipmentOtherContainer?.setOnClickListener(null)
        otherCourierTxt?.setOnClickListener {
            listener.clickShippingComponentError(errorCode, title, componentTrackDataModel)
        }

        otherCourierTxt?.show()
        hideLabelAndBo()
        shipmentDestination?.hide()
        shipmentEstimation?.hide()
        shipmentTokoCabangGroup?.hide()
    }

    private fun renderShipmentSuccess(element: ProductShipmentDataModel) = with(itemView) {
        hideShipmentLoading()
        renderText(element.rates, element.localDestination)
        renderTokoCabang(element.isFullfillment, element.rates.fulfillmentData)
        renderOtherSection(element)
    }

    private fun renderOtherSection(element: ProductShipmentDataModel) = with(itemView) {
        val rates = element.rates
        val usedLabel = mutableListOf<String>()

        val labels = element.rates.chipsLabel
        if (labels.isEmpty()) {
            renderSubtitleGreen(element.isTokoNow)
            hideLabelAndBo()
            shipmentArrow?.hide()
        } else {
            renderSubtitleNormal(rates.subtitle)

            val labelViews = listOf(shipmentLabelInstant, shipmentLabelCod)
            labelViews.forEachIndexed { index, view ->
                val label = labels.getOrElse(index) { "" }
                view?.showIfWithBlock(label.isNotEmpty()) {
                    setLabel(label)
                    usedLabel += label
                }
            }
        }

        adjustUiSuccess(rates.title, usedLabel, element.isCod)
    }

    private fun renderSubtitleGreen(isTokoNow: Boolean) = with(itemView) {
        otherCourierTxt?.text = if (isTokoNow) {
            context.getString(R.string.merchant_product_detail_label_selengkapnya)
        } else {
            context.getString(R.string.pdp_shipping_choose_courier_label)
        }
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

    private fun renderTokoCabang(isFullfillment: Boolean, fulfillmentData: FulfillmentData) = if (isFullfillment) {
        shipmentFulfillmentLabel?.text = fulfillmentData.prefix
        shipmentFullfillmentImg?.loadImage(fulfillmentData.icon)
        shipmentFulfillmentText?.text = fulfillmentData.description
        shipmentTokoCabangGroup?.show()
    } else {
        shipmentTokoCabangGroup?.gone()
    }

    private fun renderText(data: P2RatesEstimateData, localDestination: String) = with(itemView) {
        val destination = if (localDestination.isEmpty()) data.destination else context.getString(R.string.pdp_shipping_to_builder, localDestination)
        shipmentTitle?.text = data.title
        shipmentDestination?.shouldShowWithAction(destination.isNotEmpty()) {
            shipmentDestination.text = destination.renderHtmlBold(context)
        }
        shipmentEstimation?.shouldShowWithAction(data.etaText.isNotEmpty()) {
            shipmentEstimation.text = data.etaText
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

    private fun adjustUiError() = with(itemView) {
        otherCourierTxt?.setWeight(Typography.REGULAR)
        otherCourierTxt?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
        shipmentArrow?.setOnClickListener(null)
        shipmentArrow?.hide()
        shipmentOtherContainer?.setMargin(0, 5.toPx(), 0, 0)
        shipmentOtherContainer?.setPadding(0, 0, 20.toPx(), 0)
    }

    private fun adjustUiSuccess(title: String, chipsLabel: List<String>, isCod: Boolean) = with(itemView) {
        val clickListener = commonClickListener(title, chipsLabel, isCod)
        otherCourierTxt?.setOnClickListener(clickListener)
        shipmentOtherContainer?.setOnClickListener(clickListener)
        shipmentArrow?.setOnClickListener(clickListener)

        otherCourierTxt?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
        shipmentArrow?.show()
        shipmentOtherContainer?.setMargin(0, 14.toPx(), 0, 0)
        shipmentOtherContainer?.setPadding(0, 0, 0, 0)
    }

    private fun hideLabelAndBo() = with(itemView) {
        shipmentLabelCod?.hide()
        shipmentLabelInstant?.hide()
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

    private fun commonClickListener(title: String, chipsLabel: List<String>, isCod: Boolean): View.OnClickListener {
        return View.OnClickListener {
            listener.openShipmentClickedBottomSheet(title, chipsLabel, isCod, componentTrackDataModel)
        }
    }

    private fun getComponentTrackData(element: ProductShipmentDataModel?) = ComponentTrackDataModel(element?.type
            ?: "",
            element?.name ?: "", adapterPosition + 1)
}