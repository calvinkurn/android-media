package com.tokopedia.orderhistory.analytic

import android.content.Context
import android.os.Bundle
import com.tokopedia.abstraction.processor.ProductListClickBundler
import com.tokopedia.abstraction.processor.ProductListClickProduct
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.atc_common.domain.analytics.AddToCartExternalAnalytics
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.orderhistory.data.Product
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
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

    object Event {
        const val ATC = "add_to_cart"
    }

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
                product.categoryId,
                "",
                null,
                product.priceInt,
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

    fun trackSuccessDoBuy(
            product: Product,
            data: DataModel
    ) {
        val dimen83 = if (product.hasFreeShipping) {
            AddToCartExternalAnalytics.EE_VALUE_BEBAS_ONGKIR
        } else {
            AddToCartExternalAnalytics.EE_VALUE_NONE_OTHER
        }
        val itemBundle = Bundle().apply {
            putString(
                    AddToCartExternalAnalytics.EE_PARAM_ITEM_ID,
                    setValueOrDefault(data.productId.toString())
            )
            putString(
                    AddToCartExternalAnalytics.EE_PARAM_ITEM_NAME,
                    setValueOrDefault(product.name)
            )
            putString(
                    AddToCartExternalAnalytics.EE_PARAM_ITEM_BRAND,
                    setValueOrDefault("")
            )
            putString(
                    AddToCartExternalAnalytics.EE_PARAM_ITEM_CATEGORY,
                    setValueOrDefault("")
            )
            putString(
                    AddToCartExternalAnalytics.EE_PARAM_ITEM_VARIANT,
                    setValueOrDefault("")
            )
            putString(
                    AddToCartExternalAnalytics.EE_PARAM_SHOP_ID,
                    setValueOrDefault(data.shopId.toString())
            )
            putString(
                    AddToCartExternalAnalytics.EE_PARAM_SHOP_NAME,
                    setValueOrDefault("")
            )
            putString(
                    AddToCartExternalAnalytics.EE_PARAM_SHOP_TYPE, setValueOrDefault("")
            )
            putString(
                    AddToCartExternalAnalytics.EE_PARAM_CATEGORY_ID,
                    setValueOrDefault(product.categoryId)
            )
            putInt(AddToCartExternalAnalytics.EE_PARAM_QUANTITY, product.minOrder)
            putDouble(AddToCartExternalAnalytics.EE_PARAM_PRICE, product.priceInt)
            putString(AddToCartExternalAnalytics.EE_PARAM_PICTURE, product.imageUrl)
            putString(AddToCartExternalAnalytics.EE_PARAM_URL, product.productUrl)
            putString(AddToCartExternalAnalytics.EE_PARAM_DIMENSION_38, setValueOrDefault(""))
            putString(
                    AddToCartExternalAnalytics.EE_PARAM_DIMENSION_45,
                    setValueOrDefault(data.cartId)
            )
            putString(AddToCartExternalAnalytics.EE_PARAM_DIMENSION_83, dimen83)
            putString("dimension40", "/chat - buy again")
        }
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT, Event.ATC)
            putString(TrackAppUtils.EVENT_CATEGORY, Category.CHAT_DETAIL)
            putString(TrackAppUtils.EVENT_ACTION, product.buyEventAction)
            putString(TrackAppUtils.EVENT_LABEL, "")
            putParcelableArrayList(
                    AddToCartExternalAnalytics.EE_VALUE_ITEMS, arrayListOf(itemBundle)
            )
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                Event.ATC, eventDataLayer
        )
    }

    private fun setValueOrDefault(value: String): String? {
        return if (value.isEmpty()) {
            AddToCartExternalAnalytics.EE_VALUE_NONE_OTHER
        } else value
    }
}