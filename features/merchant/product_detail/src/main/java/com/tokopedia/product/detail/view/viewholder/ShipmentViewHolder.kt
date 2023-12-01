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
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.isInflated
import com.tokopedia.product.detail.view.util.renderHtmlBold
import com.tokopedia.sdui.SDUIManager
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.toPx
import org.json.JSONObject

class ShipmentViewHolder(
    view: View,
    private val listener: DynamicProductDetailListener
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

    private val sduiManager = lazy {
        SDUIManager().apply {
            initSDUI(context)
        }
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
        createAndAddSDUIView()

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

    private fun createAndAddSDUIView(){
        val ongkirJsonStr = "{\"templates\":{\"header\":{\"type\":\"container\",\"orientation\":\"horizontal\",\"paddings\":{\"top\":12,\"left\":12,\"bottom\":12},\"items\":[{\"type\":\"container\",\"orientation\":\"vertical\",\"items\":[{\"type\":\"text\",\"\$text\":\"header_title\",\"font_size\":20,\"font_weight\":\"bold\"},{\"type\":\"text\",\"\$text\":\"header_subtitle\",\"font_size\":16}]},{\"type\":\"text\",\"\$text\":\"header_see_more\",\"text_alignment_horizontal\":\"right\",\"margins\":{\"right\":12},\"width\":{\"type\":\"wrap_content\"},\"font_size\":16,\"text_color\":\"#008000\",\"actions\":[{\"log_id\":\"see_more_clicked\",\"\$url\":\"see_more_url\"}]}]},\"campaign_banner\":{\"type\":\"container\",\"width\":{\"type\":\"match_parent\"},\"height\":{\"type\":\"fixed\",\"value\":180},\"paddings\":{\"top\":8,\"left\":8,\"right\":8,\"bottom\":8},\"items\":[{\"type\":\"image\",\"width\":{\"type\":\"match_parent\"},\"height\":{\"type\":\"fixed\",\"value\":180},\"\$image_url\":\"promotional_banner\",\"actions\":[{\"log_id\":\"promotional_banner_clicked\",\"\$url\":\"promotional_banner_url\"}]}]},\"product_card\":{\"type\":\"container\",\"width\":{\"type\":\"fixed\",\"value\":130},\"height\":{\"type\":\"wrap_content\"},\"background\":[{\"type\":\"solid\",\"color\":\"#FFFFFF\"}],\"border\":{\"corner_radius\":8,\"has_shadow\":true,\"shadow\":{\"alpha\":2,\"blur\":2,\"color\":\"#0E000000\"},\"stroke\":{\"color\":\"#0E000000\",\"unit\":\"div-stroke\",\"width\":1}},\"paddings\":{\"bottom\":16},\"actions\":[{\"log_id\":\"product_card_clicked\",\"\$url\":\"product_card_url\"}],\"items\":[{\"type\":\"image\",\"width\":{\"type\":\"fixed\",\"value\":130},\"height\":{\"type\":\"fixed\",\"value\":130},\"\$image_url\":\"product_image\"},{\"type\":\"text\",\"\$text\":\"product_name\",\"margins\":{\"top\":8,\"left\":8,\"right\":8},\"font_size\":12,\"font_weight\":\"medium\",\"max_lines\":2},{\"type\":\"text\",\"\$text\":\"product_price\",\"margins\":{\"top\":4,\"left\":8,\"right\":8},\"font_size\":14,\"font_weight\":\"bold\",\"max_lines\":2},{\"type\":\"container\",\"orientation\":\"horizontal\",\"content_alignment_vertical\":\"center\",\"width\":{\"type\":\"wrap_content\"},\"margins\":{\"top\":4,\"left\":8,\"right\":8},\"items\":[{\"type\":\"image\",\"width\":{\"type\":\"fixed\",\"value\":20},\"height\":{\"type\":\"fixed\",\"value\":20},\"\$image_url\":\"shop_badge\"},{\"type\":\"text\",\"\$text\":\"shop_name\",\"font_size\":12,\"font_weight\":\"medium\",\"width\":{\"type\":\"wrap_content\"}}]}]}},\"card\":{\"log_id\":\"recom_widget_campaign\",\"states\":[{\"state_id\":0,\"div\":{\"type\":\"container\",\"orientation\":\"vertical\",\"height\":{\"type\":\"wrap_content\"},\"items\":[{\"type\":\"header\",\"header_title\":\"Kejar Diskon Spesial\",\"header_subtitle\":\"11.11 Campaign\",\"header_see_more\":\"Lihat Semua\",\"see_more_url\":\"div-action://route?applink=https://www.tokopedia.com/rekomendasi/6797291084/d?ref=pdp_1_os&product_ids=6797291084\"},{\"type\":\"campaign_banner\",\"promotional_banner\":\"https://mir-s3-cdn-cf.behance.net/project_modules/1400/1b23c832616941.568cab27a6aad.jpg\",\"promotional_banner_url\":\"div-action://route?applink=https://www.tokopedia.com/discovery/deals\"},{\"type\":\"gallery\",\"height\":{\"type\":\"fixed\",\"value\":250},\"width\":{\"type\":\"match_parent\"},\"item_spacing\":12,\"margins\":{\"left\":12,\"right\":12},\"items\":[{\"type\":\"product_card\",\"product_image\":\"https://images.tokopedia.net/img/cache/900/VqbcmM/2023/2/15/ab8be3ca-5cf2-48ee-9f5a-0037f79a7df6.jpg\",\"product_name\":\"Apple iPhone 13 Garansi Resmi - 128GB 256GB 512GB - 128 gb, MIDNIGHT BLACK\",\"product_price\":\"Rp11.489.000\",\"shop_badge\":\"https://images.tokopedia.net/img/ikRAny/2022/9/13/b0a5bec3-cf77-4701-8daa-0260a8f5076c.png\",\"shop_name\":\"iSmile Official Store\",\"product_card_url\":\"div-action://route?applink=https://www.tokopedia.com/ismileofficial/apple-iphone-13-garansi-resmi-128gb-256gb-512gb-128-gb-midnight-black\"},{\"type\":\"product_card\",\"product_image\":\"https://images.tokopedia.net/img/cache/900/VqbcmM/2022/10/29/e2aa67f3-baa1-4f16-8f03-0c27ee8a4735.png\",\"product_name\":\"Apple iPhone 14 Pro Garansi Resmi - 128GB 256GB 512GB 1TB - 128GB, Space Black\",\"product_price\":\"Rp17.279.000\",\"shop_badge\":\"https://images.tokopedia.net/img/ikRAny/2022/9/13/b0a5bec3-cf77-4701-8daa-0260a8f5076c.png\",\"shop_name\":\"iSmile Official Store\",\"product_card_url\":\"div-action://route?applink=https://www.tokopedia.com/ismileofficial/apple-iphone-14-pro-garansi-resmi-128gb-256gb-512gb-1tb-128gb-space-black\"},{\"type\":\"product_card\",\"product_image\":\"https://images.tokopedia.net/img/cache/900/VqbcmM/2022/10/28/692ed302-4f92-41d2-9b0a-a02097167a36.png\",\"product_name\":\"Apple iPhone 14 Plus Garansi Resmi - 128GB 256GB 512GB - 256GB, Red\",\"product_price\":\"Rp17.089.000\",\"shop_badge\":\"https://images.tokopedia.net/img/ikRAny/2022/9/13/b0a5bec3-cf77-4701-8daa-0260a8f5076c.png\",\"shop_name\":\"iSmile Official Store\",\"product_card_url\":\"div-action://route?applink=https://www.tokopedia.com/ismileofficial/apple-iphone-14-plus-garansi-resmi-128gb-256gb-512gb-256gb-red\"},{\"type\":\"product_card\",\"product_image\":\"https://images.tokopedia.net/img/cache/900/VqbcmM/2023/8/25/8a31bb36-5817-4db5-8fc9-103a38cf27aa.png\",\"product_name\":\"iPhone 14 Promax Garansi Resmi - Promo 128GB, Deep Purple\",\"product_price\":\"Rp18.818.000\",\"shop_badge\":\"https://images.tokopedia.net/img/ikRAny/2022/9/13/b0a5bec3-cf77-4701-8daa-0260a8f5076c.png\",\"shop_name\":\"PT Pratama Sntra Semesta\",\"product_card_url\":\"div-action://route?applink=https://www.tokopedia.com/ptpratamasemesta/iphone-14-promax-garansi-resmi-promo-128gb-deep-purple\"},{\"type\":\"product_card\",\"product_image\":\"https://images.tokopedia.net/img/cache/900/VqbcmM/2023/9/8/b82d3e15-a2e9-460c-bc96-c43a882f62ba.jpg\",\"product_name\":\"Iphone 14 128 GB Garansi Resmi Ibox (INSTAN AREA MALANG)\",\"product_price\":\"Rp13.499.000\",\"shop_badge\":\"https://images.tokopedia.net/img/goldmerchant/pm_activation/badge/Power%20Merchant%20Pro.png\",\"shop_name\":\"Meteor Cell Official Store\",\"product_card_url\":\"div-action://route?applink=https://www.tokopedia.com/meteorcell/iphone-14-128-gb-garansi-resmi-ibox-instan-area-malang\"}]}]}}]}}"
        val ongkirJson = JSONObject(ongkirJsonStr)
        val templateJson = ongkirJson.optJSONObject("templates")
        val cardJson = ongkirJson.getJSONObject("card")
        sduiManager.value.createView(context, templateJson, "divKitView", cardJson)
        binding.divkitViewContainer.removeAllViews()
        binding.divkitViewContainer.addView(sduiManager.value.createView(context,
            templateJson, "divKitView", cardJson))
    }

    private fun getComponentTrackData(
        element: ProductShipmentDataModel?
    ) = ComponentTrackDataModel(
        element?.type ?: "",
        element?.name ?: "",
        adapterPosition + 1
    )
}
