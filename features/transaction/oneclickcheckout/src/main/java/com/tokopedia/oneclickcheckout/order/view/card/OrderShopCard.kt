package com.tokopedia.oneclickcheckout.order.view.card

import android.view.View
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.model.OrderShop
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography
import java.util.*

class OrderShopCard(private val view: View, private val orderSummaryAnalytics: OrderSummaryAnalytics) {

    private lateinit var shop: OrderShop

    private val tvShopLocation by lazy { view.findViewById<Typography>(R.id.tv_shop_location) }
    private val tvShopName by lazy { view.findViewById<Typography>(R.id.tv_shop_name) }
    private val ivShop by lazy { view.findViewById<ImageUnify>(R.id.iv_shop) }
    private val iuImageFulfillment by lazy { view.findViewById<ImageUnify>(R.id.iu_image_fulfill) }
    private val iuFreeShipping by lazy { view.findViewById<ImageUnify>(R.id.iu_free_shipping) }
    private val separatorFreeShipping by lazy { view.findViewById<Typography>(R.id.separator_free_shipping) }
    private val labelError by lazy { view.findViewById<Label>(R.id.label_error) }

    fun setShop(orderShop: OrderShop) {
        tvShopName?.text = orderShop.shopName
        if (orderShop.shopBadge.isNotEmpty()) {
            ivShop?.setImageUrl(orderShop.shopBadge)
            ivShop?.visible()
            ivShop?.contentDescription = view.context.getString(com.tokopedia.purchase_platform.common.R.string.pp_cd_image_shop_badge_with_shop_type, orderShop.shopTypeName.toLowerCase(Locale.ROOT))
        } else {
            ivShop?.gone()
        }
        if (orderShop.cityName.isNotEmpty()) {
            if (orderShop.isFulfillment && orderShop.fulfillmentBadgeUrl.isNotEmpty()) {
                iuImageFulfillment?.setImageUrl(orderShop.fulfillmentBadgeUrl)
                iuImageFulfillment?.visible()
            } else {
                iuImageFulfillment?.gone()
            }
            tvShopLocation?.text = orderShop.cityName
            tvShopLocation?.visible()
        } else {
            tvShopLocation?.gone()
            iuImageFulfillment?.gone()
        }
        val error = orderShop.errors.firstOrNull()
        if (error?.isNotEmpty() == true) {
            labelError.setLabel(error)
            labelError.visible()
        } else {
            labelError.gone()
        }

        // TODO: 16/06/21 free ongkir image
//        if (product.freeOngkirImg.isNotEmpty()) {
//            iuFreeShipping?.let {
//                it.setImageUrl(product.freeOngkirImg)
//                it.visible()
//            }
//            val contentDescriptionStringResource = if (product.isFreeOngkirExtra) {
//                com.tokopedia.purchase_platform.common.R.string.pp_cd_image_badge_boe
//            } else {
//                com.tokopedia.purchase_platform.common.R.string.pp_cd_image_badge_bo
//            }
//            iuFreeShipping?.contentDescription = view.context.getString(contentDescriptionStringResource)
//            separatorFreeShipping?.visible()
//        } else {
//            iuFreeShipping?.gone()
//            separatorFreeShipping?.gone()
//        }

        this.shop = orderShop
    }
}