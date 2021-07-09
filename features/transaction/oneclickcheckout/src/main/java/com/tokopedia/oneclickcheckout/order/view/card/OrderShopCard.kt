package com.tokopedia.oneclickcheckout.order.view.card

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.databinding.CardOrderShopBinding
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.model.OrderShop
import com.tokopedia.unifycomponents.ticker.Ticker
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
            tvShopName.text = orderShop.shopName
            if (orderShop.shopBadge.isNotEmpty()) {
                ivShopBadge.setImageUrl(orderShop.shopBadge)
                ivShopBadge.visible()
                ivShopBadge.contentDescription = root.context.getString(com.tokopedia.purchase_platform.common.R.string.pp_cd_image_shop_badge_with_shop_type, orderShop.shopTypeName.toLowerCase(Locale.ROOT))
            } else {
                ivShopBadge.gone()
            }
            if (orderShop.cityName.isNotEmpty()) {
                if (orderShop.isFulfillment && orderShop.fulfillmentBadgeUrl.isNotEmpty()) {
                    iuImageFulfill.setImageUrl(orderShop.fulfillmentBadgeUrl)
                    iuImageFulfill.visible()
                } else {
                    iuImageFulfill.gone()
                }
                tvShopLocation.text = orderShop.cityName
                tvShopLocation.visible()
            } else {
                tvShopLocation.gone()
                iuImageFulfill.gone()
            }

            if (orderShop.freeOngkirImg.isNotEmpty()) {
                iuFreeShipping.let {
                    it.setImageUrl(orderShop.freeOngkirImg)
                    it.visible()
                }
                val contentDescriptionStringResource = if (orderShop.isFreeOngkirExtra) {
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

            renderShopError()
        }
    }

    private fun renderShopError() {
        binding.apply {
            if (shop.errors.isNotEmpty()) {
                tickerOrderShop.tickerType = Ticker.TYPE_ERROR
                tickerOrderShop.setHtmlDescription(shop.errors.first())
                tickerOrderShop.visible()
                occCustomTickerError.gone()
            } else if (shop.overweight > 0) {
                tickerOrderShop.tickerType = Ticker.TYPE_WARNING
                tickerOrderShop.setHtmlDescription(shop.maximumWeightWording)
                tickerOrderShop.visible()
                occCustomTickerError.gone()
            } else if (shop.firstProductErrorIndex > -1 && shop.unblockingErrorMessage.isNotBlank()) {
                occCustomTickerDescription.text = shop.unblockingErrorMessage
                occCustomTickerAction.setOnClickListener {
                    listener.onClickLihatProductError(shop.firstProductErrorIndex)
                }
                tickerOrderShop.gone()
                occCustomTickerError.visible()
            } else {
                tickerOrderShop.gone()
                occCustomTickerError.gone()
            }
        }
    }

    interface OrderShopCardListener {

        fun onClickLihatProductError(index: Int)
    }
}