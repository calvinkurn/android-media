package com.tokopedia.product.detail.view.viewholder

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.rates.P2RatesEstimateData
import com.tokopedia.product.detail.common.getCurrencyFormatted
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductShipmentDataModel
import com.tokopedia.product.detail.databinding.ItemPdpShimmerShipmentBinding
import com.tokopedia.product.detail.databinding.ItemShipmentBinding
import com.tokopedia.product.detail.databinding.ItemShipmentOptionBinding
import com.tokopedia.product.detail.databinding.ViewShipmentBinding
import com.tokopedia.product.detail.databinding.ViewShipmentErrorBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.renderHtmlBold
import com.tokopedia.unifycomponents.HtmlLinkHelper

class ShipmentViewHolder(
    view: View,
    private val listener: DynamicProductDetailListener
) : AbstractViewHolder<ProductShipmentDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_shipment
    }

    private val context = view.context
    private val binding = ItemShipmentBinding.bind(view)

    private var componentTrackDataModel: ComponentTrackDataModel? = null

    private val viewMainDelegate = lazy {
        ViewShipmentBinding.bind(
            binding.pdpShipmentStateMain.inflate()
        )
    }

    private val viewErrorDelegate = lazy {
        ViewShipmentErrorBinding.bind(
            binding.pdpShipmentStateError.inflate()
        )
    }

    private val viewLoadingDelegate = lazy {
        ItemPdpShimmerShipmentBinding.bind(
            binding.pdpShipmentStateLoading.inflate()
        )
    }

    private val viewMain: ViewShipmentBinding by viewMainDelegate
    private val viewError: ViewShipmentErrorBinding by viewErrorDelegate
    private val viewLoading: ItemPdpShimmerShipmentBinding by viewLoadingDelegate

    override fun bind(element: ProductShipmentDataModel) {
        val rates = element.rates

        componentTrackDataModel = getComponentTrackData(element)

        binding.pdpShipmentSeparator.showWithCondition(!element.isTokoNow)

        when {
            rates.title.isEmpty() -> {
                loading(true)
            }
            element.shouldShowShipmentError -> {
                loading(false)
                loadErrorState()
            }
            rates.p2RatesError.isNotEmpty() -> {
                loading(false)
                loadShipmentErrorState(rates)
            }
            else -> {
                loading(false)
                loadShipmentState(element, rates)
            }
        }
    }

    private fun loading(isLoading: Boolean) {
        if (isLoading) {
            viewLoading.root.show()
            if (viewMainDelegate.isInitialized()) viewMain.root.hide()
            if (viewErrorDelegate.isInitialized()) viewError.root.hide()
        } else if (viewLoadingDelegate.isInitialized()) viewLoading.root.hide()
    }

    private fun loadShipmentErrorState(rates: P2RatesEstimateData) = with(viewMain) {
        initialState()

        val title = rates.title
        val errorCode = rates.p2RatesError.firstOrNull()?.errorCode ?: 0

        pdpShipmentTitle.text = title
        pdpShipmentRatesError.show()
        pdpShipmentRatesError.text = HtmlLinkHelper(context, rates.subtitle).spannedString
        pdpShipmentRatesError.setOnClickListener {
            listener.clickShippingComponentError(errorCode, title, componentTrackDataModel)
        }
    }

    private fun loadShipmentState(
        element: ProductShipmentDataModel,
        rates: P2RatesEstimateData
    ) = with(viewMain) {
        initialState()
        pdpShipmentTitle.text = rates.title

        renderBo(element, rates)
        renderShipment(element, rates)
        renderCourier(element, rates)

        itemView.addOnImpressionListener(element.impressHolder) {
            listener.onImpressComponent(getComponentTrackData(element))
        }
    }

    private fun renderBo(
        element: ProductShipmentDataModel,
        rates: P2RatesEstimateData
    ) = with(viewMain) {
        val originalShippingRate = rates.originalShippingRate
        pdpShipmentTitleStrike.showIfWithBlock(rates.originalShippingRate > 0) {
            text = originalShippingRate.getCurrencyFormatted()
            paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }
        val freeOngkirImageUrl = element.freeOngkirUrl
        pdpShipmentIcon.showIfWithBlock(
            !rates.hasUsedBenefit && !element.isFullfillment && freeOngkirImageUrl.isNotEmpty()
        ) { setImageUrl(freeOngkirImageUrl) }
        if (element.isFullfillment) {
            pdpShipmentGroupTc.show()
            pdpShipmentTcLabel.text = rates.fulfillmentData.prefix
            pdpShipmentTcIcon.setImageUrl(rates.fulfillmentData.icon)
            pdpShipmentTcValue.text = rates.fulfillmentData.description
        } else {
            val subtitle = rates.shippingCtxDesc
            pdpShipmentSubtitle.showIfWithBlock(subtitle.isNotEmpty()) {
                text = subtitle.renderHtmlBold(
                    context = context,
                    boldColor = com.tokopedia.unifyprinciples.R.color.Unify_NN600
                )
            }
        }
    }

    private fun renderShipment(
        element: ProductShipmentDataModel,
        rates: P2RatesEstimateData
    ) = with(viewMain) {
        val destination = rates.destination
        pdpShipmentDestination.showIfWithBlock(destination.isNotEmpty()) {
            text = destination.renderHtmlBold(
                context = context,
                boldColor = com.tokopedia.unifyprinciples.R.color.Unify_NN600
            )
        }

        pdpShipmentLine.showWithCondition(destination.isNotEmpty())

        val estimation = rates.etaText
        pdpShipmentEstimation.showIfWithBlock(estimation.isNotEmpty()) {
            text = rates.etaText.renderHtmlBold(
                context = context,
                boldColor = com.tokopedia.unifyprinciples.R.color.Unify_NN600
            )
        }
    }

    private fun renderCourier(
        element: ProductShipmentDataModel,
        rates: P2RatesEstimateData
    ) = with(viewMain) {
        val labels = rates.chipsLabel
        var usedLabels = emptyList<String>()

        if (labels.isEmpty()) {
            pdpShipmentCourierLabel2.show()
            val labelStringId = if (element.isTokoNow)
                R.string.merchant_product_detail_label_selengkapnya
            else R.string.pdp_shipping_choose_courier_label
            pdpShipmentCourierLabel2.text = context.getString(labelStringId)
        } else {
            pdpShipmentCourierOptions.show()
            pdpShipmentCourierOptions.removeAllViews()
            labels.forEach { label ->
                if(label.isNotEmpty()){
                    val itemBinding = ItemShipmentOptionBinding.inflate(LayoutInflater.from(context))
                    itemBinding.pdpShipmentCourierOption.setLabel(label)
                    pdpShipmentCourierOptions.addView(itemBinding.root)
                }
            }
            pdpShipmentCourierLabel1.text = rates.subtitle

            pdpShipmentCourierLabel1.show()
            pdpShipmentCourierArrow.show()
        }

        setOnClickOnViews(
            listOf(
                pdpShipmentCourierLabel1,
                pdpShipmentCourierOptions,
                pdpShipmentCourierArrow,
                pdpShipmentCourierLabel2
            )
        ) {
            listener.openShipmentClickedBottomSheet(
                rates.title, usedLabels, element.isCod, componentTrackDataModel
            )
        }
    }

    private fun loadErrorState() = with(viewError) {
        root.show()
        pdpShipmentLocalLoad.apply {
            progressState = false
            refreshBtn?.setOnClickListener {
                if (!progressState) {
                    progressState = true
                    listener.refreshPage()
                }
            }
        }
    }

    private fun setOnClickOnViews(views: List<View>, clickListener: View.OnClickListener) {
        for (view in views) {
            view.setOnClickListener(clickListener)
        }
    }

    /**
     * Hide view with conditional visibility,
     * which means the view is not mandatory.
     */
    private fun initialState() {
        with(viewMain) {
            root.show()
            pdpShipmentIcon.hide()
            pdpShipmentSubtitle.hide()
            pdpShipmentGroupTc.hide()
            pdpShipmentDestination.hide()
            pdpShipmentEstimation.hide()
            pdpShipmentCourierOptions.hide()
            pdpShipmentCourierLabel1.hide()
            pdpShipmentCourierArrow.hide()
            pdpShipmentCourierLabel2.hide()
            pdpShipmentRatesError.hide()
        }
    }

    private fun getComponentTrackData(
        element: ProductShipmentDataModel?
    ) = ComponentTrackDataModel(
        element?.type ?: "",
        element?.name ?: "",
        adapterPosition + 1
    )

}
