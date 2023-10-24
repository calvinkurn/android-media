package com.tokopedia.product.detail.component.shipment

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewStub
import android.widget.LinearLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ShipmentPlusData
import com.tokopedia.product.detail.databinding.ItemShipmentBinding
import com.tokopedia.product.detail.databinding.ViewShipmentErrorBinding
import com.tokopedia.product.detail.databinding.ViewShipmentFailedBinding
import com.tokopedia.product.detail.databinding.ViewShipmentInfoBinding
import com.tokopedia.product.detail.databinding.ViewShipmentPlusBinding
import com.tokopedia.product.detail.databinding.ViewShipmentSuccessBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.renderHtmlBold
import com.tokopedia.product.detail.view.viewholder.ProductDetailPageViewHolder
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ShipmentViewHolder(
    view: View,
    private val listener: DynamicProductDetailListener
) : ProductDetailPageViewHolder<ShipmentUiModel>(view) {
    companion object {
        val LAYOUT = R.layout.item_shipment

        private const val TIPS_TYPE = "tips"
        private const val TICKER_INFO_TYPE = "info"
        private const val TICKER_WARNING_TYPE = "warning"
        private const val TICKER_ACTION_APPLINK = "applink"
    }

    private val context = view.context
    private val binding = ItemShipmentBinding.bind(view)

    private val viewSuccess = ShipmentView(binding.pdpShipmentStateSuccess)
    private val viewError = ShipmentView(binding.pdpShipmentStateError)
    private val viewLoading = ShipmentView(binding.pdpShipmentStateLoading)
    private val viewFailed = ShipmentView(binding.pdpShipmentStateFailed)

    override fun bind(element: ShipmentUiModel) {
        val componentTrackDataModel = getComponentTrackData(element)
        element.state.render(componentTrackDataModel)
    }

    private fun ShipmentUiModel.State.render(componentTrackDataModel: ComponentTrackDataModel) {
        when (this) {
            is ShipmentUiModel.Success -> render(componentTrackDataModel)
            is ShipmentUiModel.Loading -> render()
            is ShipmentUiModel.Error -> render(componentTrackDataModel)
            is ShipmentUiModel.Failed -> render()
        }
    }

    private fun ShipmentUiModel.Success.render(componentTrackDataModel: ComponentTrackDataModel) {
        ViewShipmentSuccessBinding.bind(viewSuccess.view).apply(this, componentTrackDataModel)

        viewSuccess.show()
        viewError.hide()
        viewLoading.hide()
        viewFailed.hide()
    }

    private fun ShipmentUiModel.Error.render(componentTrackDataModel: ComponentTrackDataModel) {
        ViewShipmentErrorBinding.bind(viewError.view).apply(this, componentTrackDataModel)

        viewSuccess.hide()
        viewError.show()
        viewLoading.hide()
        viewFailed.hide()
    }

    private fun ShipmentUiModel.Loading.render() {
        viewLoading.view

        viewSuccess.hide()
        viewError.hide()
        viewLoading.show()
        viewFailed.hide()
    }

    private fun ShipmentUiModel.Failed.render() {
        ViewShipmentFailedBinding.bind(viewFailed.view).apply()

        viewSuccess.hide()
        viewError.hide()
        viewLoading.hide()
        viewError.show()
    }

    private fun ViewShipmentSuccessBinding.apply(
        data: ShipmentUiModel.Success,
        componentTrackDataModel: ComponentTrackDataModel
    ) {
        val logo = data.logo
        pdpShipmentHeaderLogo.showIfWithBlock(logo.isNotEmpty()) {
            setImageUrl(logo)
        }

        val title = data.title
        pdpShipmentHeaderPrice.showIfWithBlock(title.isNotEmpty()) {
            text = title
        }

        val slashPrice = data.slashPrice
        pdpShipmentHeaderSlashPrice.showIfWithBlock(slashPrice.isNotEmpty()) {
            text = slashPrice
            paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

        val background = data.background
        pdpShipmentBackground.showIfWithBlock(background.isNotEmpty()) {
            setImageUrl(background)
        }

        val body = data.body
        pdpShipmentContainerBody.showIfWithBlock(body.isNotEmpty()) {
            removeAllViews()
            apply(body)
        }

        root.setOnClickListener {
            listener.openShipmentClickedBottomSheet(
                title = data.title,
                chipsLabel = data.labels,
                isCod = data.isCod,
                isScheduled = data.isScheduled,
                componentTrackDataModel = componentTrackDataModel
            )
        }

        val tickers = data.tickers.filter {
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

        val tips = data.tickers.firstOrNull {
            it.color == TIPS_TYPE
        } ?: return

        pdpShipmentTips.showIfWithBlock(tips.message.isNotBlank()) {
            this.title = tips.title

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

        renderShipmentPlus(vsShipmentPlus, data.shipmentPlus, componentTrackDataModel)
    }

    private fun renderShipmentPlus(
        vsShipmentPlus: ViewStub,
        shipmentPlus: ShipmentPlusData,
        componentTrackDataModel: ComponentTrackDataModel
    ) {
        if (shipmentPlus.isShow) {
            val viewShipmentPlus = ViewShipmentPlusBinding.bind(vsShipmentPlus.inflate())

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

    private fun mapTickerType(type: String): Int {
        return when (type) {
            TICKER_INFO_TYPE -> Ticker.TYPE_ANNOUNCEMENT
            TICKER_WARNING_TYPE -> Ticker.TYPE_WARNING
            else -> Ticker.TYPE_ANNOUNCEMENT
        }
    }

    private fun generateHtml(message: String, link: String): String = with(itemView) {
        return message.replace("{link}", context.getString(R.string.ticker_href_builder, link))
    }

    private fun LinearLayout.apply(body: List<ShipmentUiModel.Info>) {
        val context = context ?: return
        body.forEach { info ->
            val view = ViewShipmentInfoBinding.inflate(LayoutInflater.from(context))
            view.apply(info)
            addView(view.root)
        }
    }

    private fun ViewShipmentInfoBinding.apply(data: ShipmentUiModel.Info) {
        val logo = data.logo
        pdpShipmentInfoLogo.showIfWithBlock(logo > -1) {
            setImage(logo)
        }

        val text = data.text
        pdpShipmentInfoText.showIfWithBlock(text.isNotEmpty()) {
            this.text = text
        }
    }

    private fun ViewShipmentErrorBinding.apply(
        data: ShipmentUiModel.Error,
        componentTrackDataModel: ComponentTrackDataModel
    ) {
        val title = data.title
        pdpShipmentErrorTitle.showIfWithBlock(title.isNotEmpty()) {
            text = title
        }

        val subtitle = data.subtitle
        pdpShipmentErrorSubtitle.showIfWithBlock(subtitle.isNotEmpty()) {
            text = subtitle.renderHtmlBold(
                context = context,
                boldColor = unifyprinciplesR.color.Unify_GN500
            )
        }

        val background = data.background
        pdpShipmentBackground.showIfWithBlock(background.isNotEmpty()) {
            setImageUrl(background)
        }

        root.setOnClickListener {
            listener.clickShippingComponentError(
                errorCode = data.errorCode,
                title = title,
                componentTrackDataModel = componentTrackDataModel
            )
        }
    }

    private fun ViewShipmentFailedBinding.apply() {
        pdpShipmentLocalLoad.progressState = false
        pdpShipmentLocalLoad.refreshBtn?.setOnClickListener {
            if (!pdpShipmentLocalLoad.progressState) {
                pdpShipmentLocalLoad.progressState = true
                listener.refreshPage()
            }
        }
    }

    private fun getError() = lazy {
        binding.pdpShipmentStateError.inflate()
    }

    private fun getFailed() = lazy {
        binding.pdpShipmentStateFailed.inflate()
    }

    data class ShipmentView(
        val viewStub: ViewStub
    ) {
        private val inflateDelegate = lazy { viewStub.inflate() }
        val view: View by inflateDelegate

        fun hide() {
            if (inflateDelegate.isInitialized()) view.hide()
        }

        fun show() {
            if (inflateDelegate.isInitialized()) view.show()
        }
    }
}
