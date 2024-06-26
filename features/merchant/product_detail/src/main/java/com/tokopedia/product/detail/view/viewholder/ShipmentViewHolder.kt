package com.tokopedia.product.detail.view.viewholder

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.updateLayoutParams
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.rates.P2RatesEstimateData
import com.tokopedia.product.detail.common.getCurrencyFormatted
import com.tokopedia.product.detail.common.utils.extensions.addOnImpressionListener
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductShipmentDataModel
import com.tokopedia.product.detail.data.model.datamodel.ShipmentPlusData
import com.tokopedia.product.detail.databinding.ItemPdpShimmerShipmentBinding
import com.tokopedia.product.detail.databinding.ItemShipmentOldBinding
import com.tokopedia.product.detail.databinding.ItemShipmentOptionBinding
import com.tokopedia.product.detail.databinding.ViewShipmentBinding
import com.tokopedia.product.detail.databinding.ViewShipmentErrorOldBinding
import com.tokopedia.product.detail.databinding.ViewShipmentPlusBinding
import com.tokopedia.product.detail.view.listener.ProductDetailListener
import com.tokopedia.product.detail.view.util.isInflated
import com.tokopedia.product.detail.view.util.renderHtmlBold
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.toPx

class ShipmentViewHolder(
    view: View,
    private val listener: ProductDetailListener
) : AbstractViewHolder<ProductShipmentDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_shipment_old

        private const val TIPS_TYPE = "tips"
        private const val TICKER_INFO_TYPE = "info"
        private const val TICKER_WARNING_TYPE = "warning"
        private const val TICKER_ACTION_APPLINK = "applink"

        private const val SHIPMENT_ICON_PADDING = 16
    }

    private val context = view.context
    private val binding = ItemShipmentOldBinding.bind(view)

    private var componentTrackDataModel: ComponentTrackDataModel? = null

    private val viewMainDelegate = lazy {
        ViewShipmentBinding.bind(
            binding.pdpShipmentStateMain.inflate()
        )
    }

    private val viewErrorDelegate = lazy {
        ViewShipmentErrorOldBinding.bind(
            binding.pdpShipmentStateError.inflate()
        )
    }

    private val viewLoadingDelegate = lazy {
        ItemPdpShimmerShipmentBinding.bind(
            binding.pdpShipmentStateLoading.inflate()
        )
    }

    private val viewMain: ViewShipmentBinding by viewMainDelegate
    private val viewError: ViewShipmentErrorOldBinding by viewErrorDelegate
    private val viewLoading: ItemPdpShimmerShipmentBinding by viewLoadingDelegate
    private val viewShipmentPlus: ViewShipmentPlusBinding by lazyThreadSafetyNone {
        ViewShipmentPlusBinding.bind(viewMain.vsShipmentPlus.inflate())
    }

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
        renderTickers(rates)
        renderTips(rates)
        renderShipmentPlus(element.shipmentPlusData)

        itemView.addOnImpressionListener(
            holder = element.impressHolder,
            holders = listener.getImpressionHolders(),
            name = element.name,
            useHolders = listener.isRemoteCacheableActive()
        ) {
            val componentTrackData = getComponentTrackData(element)
            listener.onImpressComponent(componentTrackData)
            if (rates.isScheduled) {
                listener.onImpressScheduledDelivery(
                    rates.chipsLabel,
                    componentTrackData
                )
            }
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
        val boBadge = rates.boBadge
        val freeOngkirImageUrl = boBadge.imageUrl
        pdpShipmentIcon.showIfWithBlock(freeOngkirImageUrl.isNotEmpty()) {
            updateLayoutParams<MarginLayoutParams> {
                marginEnd = if (boBadge.isUsingPadding) {
                    SHIPMENT_ICON_PADDING.toPx()
                } else {
                    0
                }
            }

            setImageUrl(freeOngkirImageUrl)
            val imageHeight = boBadge.imageHeight
            if (imageHeight > 0) {
                updateLayoutParams {
                    height = imageHeight.toPx()
                }
            }
        }
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

        if (labels.isEmpty()) {
            pdpShipmentCourierLabel2.show()
            val labelStringId = if (element.isTokoNow) {
                R.string.merchant_product_detail_label_selengkapnya
            } else {
                R.string.pdp_shipping_choose_courier_label
            }
            pdpShipmentCourierLabel2.text = context.getString(labelStringId)
        } else {
            pdpShipmentCourierOptions.show()
            pdpShipmentCourierOptions.removeAllViews()
            labels.forEach { label ->
                if (label.isNotEmpty()) {
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
                rates.title,
                labels,
                element.isCod,
                rates.isScheduled,
                getComponentTrackData(element)
            )
        }
    }

    private fun renderTickers(rates: P2RatesEstimateData) = with(viewMain) {
        val tickers = rates.tickers.filter {
            it.color == TICKER_INFO_TYPE ||
                it.color == TICKER_WARNING_TYPE
        }.map {
            TickerData(
                description = it.message,
                type = mapTickerType(it.color),
                title = it.title,
                isFromHtml = true
            )
        }

        pdpShipmentTicker.showIfWithBlock(tickers.isNotEmpty()) {
            addPagerView(
                TickerPagerAdapter(context, tickers),
                tickers
            )
        }
    }

    private fun mapTickerType(type: String): Int {
        return when (type) {
            TICKER_INFO_TYPE -> Ticker.TYPE_ANNOUNCEMENT
            TICKER_WARNING_TYPE -> Ticker.TYPE_WARNING
            else -> Ticker.TYPE_ANNOUNCEMENT
        }
    }

    private fun renderTips(rates: P2RatesEstimateData) = with(viewMain) {
        val tips = rates.tickers.firstOrNull {
            it.color == TIPS_TYPE
        } ?: return

        pdpShipmentTips.showIfWithBlock(tips.message.isNotBlank()) {
            title = tips.title

            val htmlString = HtmlLinkHelper(context, generateHtml(tips.message, tips.link))
            description = htmlString.spannedString ?: ""

            val link = tips.link
            if (link.isNotEmpty()) {
                setOnClickListener {
                    if (tips.action == TICKER_ACTION_APPLINK) {
                        listener.goToApplink(link)
                    } else {
                        listener.goToWebView(link)
                    }
                }
            }
        }
    }

    private fun renderShipmentPlus(shipmentPlus: ShipmentPlusData) {
        if (shipmentPlus.isShow) {
            with(viewShipmentPlus) {
                pdpShipmentPlusBackground.loadImage(shipmentPlus.getBackgroundUrl(context))
                pdpShipmentPlusLogo.loadImage(shipmentPlus.getLogoUrl(context))
                pdpShipmentPlusText.text = HtmlLinkHelper(context, shipmentPlus.text).spannedString
                pdpShipmentPlus.setOnClickListener {
                    listener.onClickShipmentPlusBanner(
                        link = shipmentPlus.actionLink,
                        trackerData = shipmentPlus.trackerData,
                        componentTrackDataModel = componentTrackDataModel
                    )
                }
                pdpShipmentPlus.show()
            }
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

    private fun generateHtml(message: String, link: String): String = with(itemView) {
        return message.replace("{link}", context.getString(R.string.ticker_href_builder, link))
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
            pdpShipmentTitleStrike.hide()
            pdpShipmentTicker.hide()
            pdpShipmentTips.hide()
            if (vsShipmentPlus.isInflated()) {
                viewShipmentPlus.root.hide()
            }
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
