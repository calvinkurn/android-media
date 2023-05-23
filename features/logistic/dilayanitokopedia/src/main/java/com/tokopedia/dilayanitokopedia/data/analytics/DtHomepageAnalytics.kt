package com.tokopedia.dilayanitokopedia.data.analytics

import android.os.Bundle
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by irpan on 22/05/23.
 */
object DtHomepageAnalytics : BaseTrackerConst() {

    private const val EVENT_IMPRESS_PRODUCT_CARD_DT = "view_item_list"
    private const val ACTION_IMPRESSION_PRODUCT_CARD_DT = "impression - product cards dt"

    private const val EVENT_CLICK_PRODUCT_CARD_DT = "select_content"
    private const val ACTION_CLICK_PRODUCT_CARD_DT = "click - product cards dt"

    private const val CLICK_PRODUCT_TRACKER_ID = "43344"
    private const val IMPRESS_PRODUCT_TRACKER_ID = "42969"

    private const val BUSINESS_UNIT_LOGISTIC_FULFILLMENT = "Logistic | Fulfillment"
    private const val CATEGORY_LOGISTIC_FULFILLMENT = "homepage dilayani tokopedia"

    private const val KEY_USER_ID = "userId"
    private const val KEY_BUSINESS_UNIT = "businessUnit"
    private const val KEY_TRACKER_ID = "trackerId"
    private const val KEY_CURRENT_SITE = "currentSite"
    private const val KEY_ITEMS = "items"
    private const val KEY_INDEX = "index"
    private const val KEY_ITEM_ID = "item_id"
    private const val KEY_ITEM_NAME = "item_name"
    private const val KEY_ITEM_LIST = "item_list"
    private const val KEY_ITEM_VARIANT = "item_variant"
    private const val KEY_ITEM_BRAND = "item_brand"
    private const val KEY_PRICE = "price"
    private const val KEY_ITEM_CATEGORY = "item_category"
    private const val KEY_DIMENSION_40 = "dimension40"

    private const val DIMENSION40 = "/dilayanitokopedia - home - product card"

    private const val BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE = "tokopediamarketplace"

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/3827
    // Tracker ID: 42969
    fun sendImpressionProductCardsDtEvent(
        userSession: UserSessionInterface,
        product: ProductCardAnalyticsModel?
    ) {
        impressProductCardTracker(userSession, product)
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/3827
    // Tracker ID: 43344
    fun sendClickProductCardsDtEvent(
        userSession: UserSessionInterface,
        product: ProductCardAnalyticsModel?
    ) {
        trackClickProductCard(
            userSession,
            product
        )
    }

    private fun trackClickProductCard(userSession: UserSessionInterface, product: ProductCardAnalyticsModel?) {
        val items = arrayListOf(
            productItemDataLayer(
                index = product?.index.orEmpty(),
                productId = product?.productId.orEmpty(),
                productName = product?.productName.orEmpty(),
                price = product?.price?.filter { it.isDigit() }.toLongOrZero(),
                productBrand = product?.productBrand.orEmpty(),
                productCategory = product?.productCategory.orEmpty(),
                productVariant = product?.productVariant.orEmpty()
            )
        )

        val itemList = "/product - dilayani tokopedia"
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_CLICK_PRODUCT_CARD_DT,
            action = ACTION_CLICK_PRODUCT_CARD_DT,
            label = "",
            trackerId = CLICK_PRODUCT_TRACKER_ID
        ).apply {
            putString(KEY_ITEM_LIST, itemList)
            putParcelableArrayList(KEY_ITEMS, items)
            putString(KEY_USER_ID, userSession.userId)
        }

        getTracker().sendEnhanceEcommerceEvent(EVENT_CLICK_PRODUCT_CARD_DT, dataLayer)
    }

    private fun impressProductCardTracker(userSession: UserSessionInterface, product: ProductCardAnalyticsModel?) {
        val items = arrayListOf(
            productItemDataLayer(
                index = product?.index.orEmpty(),
                productId = product?.productId.orEmpty(),
                productName = product?.productName.orEmpty(),
                price = product?.price?.filter { it.isDigit() }.toLongOrZero(),
                productBrand = product?.productBrand.orEmpty(),
                productCategory = product?.productCategory.orEmpty()
            )
        )

        val itemList = "/product - dilayani tokopedia"
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_IMPRESS_PRODUCT_CARD_DT,
            action = ACTION_IMPRESSION_PRODUCT_CARD_DT,
            label = "",
            trackerId = IMPRESS_PRODUCT_TRACKER_ID
        ).apply {
            putString(KEY_ITEM_LIST, itemList)
            putParcelableArrayList(KEY_ITEMS, items)
            putString(KEY_USER_ID, userSession.userId)
        }

        getTracker().sendEnhanceEcommerceEvent(EVENT_IMPRESS_PRODUCT_CARD_DT, dataLayer)
    }

    private fun getMarketplaceDataLayer(event: String, action: String, label: String = "", trackerId: String): Bundle {
        return Bundle().apply {
            putString(TrackAppUtils.EVENT, event)
            putString(TrackAppUtils.EVENT_ACTION, action)
            putString(TrackAppUtils.EVENT_CATEGORY, CATEGORY_LOGISTIC_FULFILLMENT)
            putString(TrackAppUtils.EVENT_LABEL, label)
            putString(KEY_TRACKER_ID, trackerId)
            putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT_LOGISTIC_FULFILLMENT)
            putString(KEY_CURRENT_SITE, BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
        }
    }

    private fun productItemDataLayer(
        index: String = "",
        productId: String = "",
        productName: String = "",
        price: Long = 0L,
        productBrand: String = "",
        productCategory: String = "",
        productVariant: String = ""
    ): Bundle {
        return Bundle().apply {
            if (index.isNotBlank()) {
                putString(KEY_INDEX, index)
            }
            putString(KEY_ITEM_BRAND, productBrand)
            putString(KEY_ITEM_CATEGORY, productCategory)
            putString(KEY_ITEM_ID, productId)
            putString(KEY_ITEM_NAME, productName)
            putString(KEY_ITEM_VARIANT, productVariant)
            putString(KEY_DIMENSION_40, DIMENSION40)
            putLong(KEY_PRICE, price)
        }
    }
}
