package com.tokopedia.orderhistory.analytic

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.orderhistory.data.Product
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class OrderHistoryAnalytic @Inject constructor() {

    private val EVENT_NAME = "event"
    private val EVENT_CATEGORY = "eventCategory"
    private val EVENT_ACTION = "eventAction"
    private val EVENT_LABEL = "eventLabel"
    private val USER_ID = "userId"
    private val ECOMMERCE = "ecommerce"

    object Name {
        const val PRODUCT_PREVIEW = "productView"
    }

    object Category {
        const val CHAT_DETAIL = "chat detail"
    }

    object Action {
        const val VIEW_PRODUCT_PRODUCT_HISTORY = "view on product history order"
    }

    private var seenProduct = HashSet<String>()

    fun eventSeenProductAttachment(product: Product, session: UserSessionInterface, position: Int) {
        if (!seenProduct.add(product.productId)) return
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        EVENT_NAME, Name.PRODUCT_PREVIEW,
                        EVENT_CATEGORY, Category.CHAT_DETAIL,
                        EVENT_ACTION, Action.VIEW_PRODUCT_PRODUCT_HISTORY,
                        EVENT_LABEL, "buyer",
                        USER_ID, session.userId,
                        ECOMMERCE, DataLayer.mapOf(
                                "currencyCode", "IDR",
                                "impressions", DataLayer.listOf(
                                        DataLayer.mapOf(
                                                "name", product.name,
                                                "id", product.productId,
                                                "price", product.priceInt,
                                                "brand", "",
                                                "category", product.categoryId,
                                                "list", "/chat",
                                                "position", position,
                                                "dimension40", "/chat"
                                        )
                                )
                        )
                )
        )
    }

}