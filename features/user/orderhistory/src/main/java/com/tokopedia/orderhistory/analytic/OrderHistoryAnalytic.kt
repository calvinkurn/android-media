package com.tokopedia.orderhistory.analytic

import android.content.Context
import com.tokopedia.abstraction.processor.ProductListClickBundler
import com.tokopedia.abstraction.processor.ProductListClickProduct
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.orderhistory.data.Product
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashSet

class OrderHistoryAnalytic @Inject constructor() {

    private val EVENT_NAME = "event"
    private val EVENT_CATEGORY = "eventCategory"
    private val EVENT_ACTION = "eventAction"
    private val EVENT_LABEL = "eventLabel"
    private val USER_ID = "userId"
    private val ECOMMERCE = "ecommerce"
    private val ITEM_LIST = "item_list"

    object Name {
        const val PRODUCT_PREVIEW = "productView"
    }

    object Category {
        const val CHAT_DETAIL = "chat detail"
    }

    object Action {
        const val VIEW_PRODUCT_HISTORY = "view on product history order"
        const val CLICK_PRODUCT_HISTORY = "click on product history order"
    }

    private var seenProduct = HashSet<String>()
    private val from = "/chat - buy again"

    fun eventSeenProductAttachment(product: Product, session: UserSessionInterface, position: Int) {
        if (!seenProduct.add(product.productId)) return
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        EVENT_NAME, Name.PRODUCT_PREVIEW,
                        EVENT_CATEGORY, Category.CHAT_DETAIL,
                        EVENT_ACTION, Action.VIEW_PRODUCT_HISTORY,
                        EVENT_LABEL, "buyer",
                        USER_ID, session.userId,
                        ITEM_LIST, from,
                        ECOMMERCE, DataLayer.mapOf(
                                "currencyCode", "IDR",
                                "impressions", DataLayer.listOf(
                                        DataLayer.mapOf(
                                                "name", product.name,
                                                "id", product.productId,
                                                "price", product.priceInt,
                                                "brand", "",
                                                "category", product.categoryId,
                                                "position", position,
                                                "dimension40", from
                                        )
                                )
                        )
                )
        )
    }

    fun eventClickProduct(context: Context?, product: Product, position: Int) {
        val products = ArrayList<ProductListClickProduct>()
        val topChatProduct = ProductListClickProduct(
                product.productId,
                product.name,
                product.categoryId.toString(),
                "",
                null,
                product.priceInt.toDouble(),
                null,
                from,
                position.toLong(),
                HashMap()
        )
        products.add(topChatProduct)

        val bundle = ProductListClickBundler.getBundle(
                from,
                products,
                Category.CHAT_DETAIL,
                Action.CLICK_PRODUCT_HISTORY,
                ProductListClickBundler.KEY,
                null,
                null,
                null
        )

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                ProductListClickBundler.KEY, bundle
        )
    }

}