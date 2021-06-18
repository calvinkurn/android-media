package com.tokopedia.oneclickcheckout.order.view.card

import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.databinding.CardOrderShopBinding
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.model.OrderShop
import java.util.*

class OrderShopCard(private val view: CardOrderShopBinding, private val orderSummaryAnalytics: OrderSummaryAnalytics) {

    private lateinit var shop: OrderShop

    fun setShop(orderShop: OrderShop, freeOngkirImg: String, freeOngkirExtra: Boolean) {
        view.apply {
            tvShopName.text = orderShop.shopName
            if (orderShop.shopBadge.isNotEmpty()) {
                ivShop.setImageUrl(orderShop.shopBadge)
                ivShop.visible()
                ivShop.contentDescription = root.context.getString(com.tokopedia.purchase_platform.common.R.string.pp_cd_image_shop_badge_with_shop_type, orderShop.shopTypeName.toLowerCase(Locale.ROOT))
            } else {
                ivShop.gone()
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
            val error = orderShop.errors.firstOrNull()
            if (error?.isNotEmpty() == true) {
                labelError.setLabel(error)
                labelError.visible()
            } else {
                labelError.gone()
            }

            // TODO: 16/06/21 free ongkir image
            if (freeOngkirImg.isNotEmpty()) {
                iuFreeShipping.let {
                    it.setImageUrl(freeOngkirImg)
                    it.visible()
                }
                val contentDescriptionStringResource = if (freeOngkirExtra) {
                    com.tokopedia.purchase_platform.common.R.string.pp_cd_image_badge_boe
                } else {
                    com.tokopedia.purchase_platform.common.R.string.pp_cd_image_badge_bo
                }
                iuFreeShipping.contentDescription = view.root.context.getString(contentDescriptionStringResource)
                separatorFreeShipping.visible()
            } else {
                iuFreeShipping.gone()
                separatorFreeShipping.gone()
            }
        }
        this.shop = orderShop
    }
}