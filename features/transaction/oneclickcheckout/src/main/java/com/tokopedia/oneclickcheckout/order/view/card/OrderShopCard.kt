package com.tokopedia.oneclickcheckout.order.view.card

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.databinding.CardOrderShopBinding
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.model.OrderShop
import com.tokopedia.oneclickcheckout.order.view.model.OrderShop.Companion.MAXIMUM_WEIGHT_WORDING_REPLACE_KEY
import com.tokopedia.unifycomponents.ticker.Ticker
import java.text.NumberFormat
import java.util.*

class OrderShopCard(private val binding: CardOrderShopBinding,
                    private val listener: OrderShopCardListener,
                    private val orderSummaryAnalytics: OrderSummaryAnalytics) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        const val VIEW_TYPE = 2
    }

    private var shop: OrderShop = OrderShop()

    fun setShop(orderShop: OrderShop) {
        this.shop = orderShop
        binding.apply {
            tvShopName.text = shop.shopName
            if (shop.shopBadge.isNotEmpty()) {
                ivShopBadge.setImageUrl(shop.shopBadge)
                ivShopBadge.visible()
                ivShopBadge.contentDescription = root.context.getString(com.tokopedia.purchase_platform.common.R.string.pp_cd_image_shop_badge_with_shop_type, orderShop.shopTypeName.lowercase())
            } else {
                ivShopBadge.gone()
            }
            renderShopInfo()
            renderShopError()
        }
    }

    private fun renderShopInfo() {
        binding.apply {
            if (shop.cityName.isNotEmpty()) {
                if (shop.isFulfillment && shop.fulfillmentBadgeUrl.isNotEmpty()) {
                    iuImageFulfill.setImageUrl(shop.fulfillmentBadgeUrl)
                    iuImageFulfill.visible()
                } else {
                    iuImageFulfill.gone()
                }
                tvShopLocation.text = shop.cityName
                tvShopLocation.visible()
            } else {
                tvShopLocation.gone()
                iuImageFulfill.gone()
            }

            if (shop.preOrderLabel.isNotBlank()) {
                lblPreOrder.text = shop.preOrderLabel
                lblPreOrder.visible()
                separatorPreOrder.visible()
            } else {
                lblPreOrder.gone()
                separatorPreOrder.gone()
            }

            if (shop.shopAlertMessage.isNotBlank()) {
                lblAlertMessage.text = shop.shopAlertMessage
                lblAlertMessage.visible()
                separatorAlertMessage.visible()
            } else {
                lblAlertMessage.gone()
                separatorAlertMessage.gone()
            }

            if (shop.freeOngkirImg.isNotEmpty()) {
                iuFreeShipping.let {
                    it.setImageUrl(shop.freeOngkirImg)
                    it.visible()
                }
                val contentDescriptionStringResource = if (shop.isFreeOngkirExtra) {
                    com.tokopedia.purchase_platform.common.R.string.pp_cd_image_badge_boe
                } else {
                    com.tokopedia.purchase_platform.common.R.string.pp_cd_image_badge_bo
                }
                iuFreeShipping.contentDescription = binding.root.context.getString(contentDescriptionStringResource)
                separatorFreeShipping.visible()
            } else {
                iuFreeShipping.gone()
                separatorFreeShipping.gone()
            }
        }
    }

    private fun renderShopError() {
        binding.apply {
            if (shop.isError) {
                tickerOrderShop.tickerType = Ticker.TYPE_ERROR
                tickerOrderShop.setHtmlDescription(shop.errors.first())
                tickerOrderShop.visible()
                occCustomTickerError.gone()
                if (!shop.hasTriggerViewErrorOrderLevelTicker) {
                    orderSummaryAnalytics.eventViewErrorOrderLevelTicker(shop.shopId.toString(), shop.errors.first())
                    shop.hasTriggerViewErrorOrderLevelTicker = true
                }
                shop.hasTriggerViewOverweightTicker = false
            } else if (shop.overweight > 0) {
                tickerOrderShop.tickerType = Ticker.TYPE_WARNING
                val overweightString = NumberFormat.getNumberInstance(Locale("in", "id")).format(shop.overweight / OrderShop.WEIGHT_KG_DIVIDER)
                tickerOrderShop.setHtmlDescription(shop.maximumWeightWording.replace(MAXIMUM_WEIGHT_WORDING_REPLACE_KEY, overweightString))
                tickerOrderShop.visible()
                occCustomTickerError.gone()
                if (!shop.hasTriggerViewOverweightTicker) {
                    orderSummaryAnalytics.eventViewOverweightTicker(shop.shopId.toString())
                    shop.hasTriggerViewOverweightTicker = true
                }
                shop.hasTriggerViewErrorOrderLevelTicker = false
            } else if (shop.firstProductErrorIndex > -1 && shop.unblockingErrorMessage.isNotBlank()) {
                occCustomTickerDescription.text = shop.unblockingErrorMessage
                occCustomTickerAction.setOnClickListener {
                    listener.onClickLihatProductError(shop.firstProductErrorIndex)
                }
                tickerOrderShop.gone()
                occCustomTickerError.visible()
                if (!shop.hasTriggerViewErrorOrderLevelTicker) {
                    orderSummaryAnalytics.eventViewErrorOrderLevelTicker(shop.shopId.toString(), shop.unblockingErrorMessage)
                    shop.hasTriggerViewErrorOrderLevelTicker = true
                }
                shop.hasTriggerViewOverweightTicker = false
            } else if (!shop.shouldValidateWeight() && shop.shopTicker.isNotBlank()) {
                tickerOrderShop.tickerType = Ticker.TYPE_WARNING
                tickerOrderShop.setHtmlDescription(shop.shopTicker)
                tickerOrderShop.visible()
                occCustomTickerError.gone()
                shop.hasTriggerViewErrorOrderLevelTicker = false
                shop.hasTriggerViewOverweightTicker = false
            } else {
                tickerOrderShop.gone()
                occCustomTickerError.gone()
                shop.hasTriggerViewErrorOrderLevelTicker = false
                shop.hasTriggerViewOverweightTicker = false
            }
        }
    }

    interface OrderShopCardListener {

        fun onClickLihatProductError(index: Int)
    }
}