package com.tokopedia.product.detail.component.shipment

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewStub
import android.widget.LinearLayout
import androidx.core.view.updateLayoutParams
import androidx.viewbinding.ViewBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.databinding.ItemShipmentBinding
import com.tokopedia.product.detail.databinding.ViewShipmentErrorBinding
import com.tokopedia.product.detail.databinding.ViewShipmentFailedBinding
import com.tokopedia.product.detail.databinding.ViewShipmentInfoBinding
import com.tokopedia.product.detail.databinding.ViewShipmentLoadingBinding
import com.tokopedia.product.detail.databinding.ViewShipmentPlusBinding
import com.tokopedia.product.detail.databinding.ViewShipmentSuccessBinding
import com.tokopedia.product.detail.view.listener.ProductDetailListener
import com.tokopedia.product.detail.view.viewholder.ProductDetailPageViewHolder
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.toPx

class ShipmentViewHolder(
    view: View,
    private val listener: ProductDetailListener
) : ProductDetailPageViewHolder<ShipmentUiModel>(view) {
    companion object {
        val LAYOUT = R.layout.item_shipment

        private const val TICKER_ACTION_APPLINK = "applink"
    }

    private val context = view.context
    private val binding = ItemShipmentBinding.bind(view)

    private val viewSuccess = ShipmentView(binding.pdpShipmentStateSuccess) {
        ViewShipmentSuccessBinding.bind(it)
    }
    private val viewError = ShipmentView(binding.pdpShipmentStateError) {
        ViewShipmentErrorBinding.bind(it)
    }
    private val viewLoading = ShipmentView(binding.pdpShipmentStateLoading) {
        ViewShipmentLoadingBinding.bind(it)
    }
    private val viewFailed = ShipmentView(binding.pdpShipmentStateFailed) {
        ViewShipmentFailedBinding.bind(it)
    }

    private val viewPlus: ShipmentView<ViewShipmentPlusBinding> by lazyThreadSafetyNone {
        ShipmentView(viewSuccess.binding.pdpShipmentPlus) { ViewShipmentPlusBinding.bind(it) }
    }

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
        viewSuccess.binding.apply(this, componentTrackDataModel)

        viewSuccess.show()
        viewError.hide()
        viewLoading.hide()
        viewFailed.hide()
    }

    private fun ShipmentUiModel.Error.render(componentTrackDataModel: ComponentTrackDataModel) {
        viewError.binding.apply(this, componentTrackDataModel)

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
        viewFailed.binding.apply()

        viewSuccess.hide()
        viewError.hide()
        viewLoading.hide()
        viewFailed.show()
    }

    private fun ViewShipmentSuccessBinding.apply(
        data: ShipmentUiModel.Success,
        componentTrackDataModel: ComponentTrackDataModel
    ) {
        val logo = data.logo
        pdpShipmentHeaderLogo.showIfWithBlock(logo.isNotEmpty()) {
            setImageUrl(logo)

            val logoHeight = data.logoHeight
            if (logoHeight > 0) {
                updateLayoutParams {
                    height = logoHeight.toPx()
                }
            }
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

        val tickers = data.tickers
        pdpShipmentTicker.showIfWithBlock(tickers.isNotEmpty()) {
            addPagerView(
                TickerPagerAdapter(context, tickers),
                tickers
            )
        }

        pdpShipmentTips.showIfWithBlock(data.tips?.message?.isNotBlank() == true) {
            val tips = data.tips ?: return@showIfWithBlock

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

        renderShipmentPlus(data.shipmentPlus, componentTrackDataModel)
    }

    private fun renderShipmentPlus(
        shipmentPlus: ShipmentUiModel.ShipmentPlusData,
        componentTrackDataModel: ComponentTrackDataModel
    ) {
        if (shipmentPlus.isShow) {
            with(viewPlus.binding) {
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
            text = HtmlLinkHelper(context, subtitle).spannedString
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

    class ShipmentView<T : ViewBinding>(
        private val viewStub: ViewStub,
        private val inflater: (View) -> T
    ) {
        private val inflateDelegate = lazyThreadSafetyNone { viewStub.inflate() }
        val view: View by inflateDelegate

        val binding: T by lazyThreadSafetyNone {
            inflater.invoke(view)
        }

        fun hide() {
            if (inflateDelegate.isInitialized()) view.hide()
        }

        fun show() {
            if (inflateDelegate.isInitialized()) view.show()
        }
    }
}
