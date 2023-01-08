package com.tokopedia.product_bundle.tracking

import android.os.Bundle
import android.text.TextUtils
import com.tokopedia.product_bundle.multiple.presentation.model.ProductDetailBundleTracker
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.ContextAnalytics

abstract class BaseProductBundleTracking {

    companion object {
        // Key constant
        private const val KEY_EVENT = "event"
        private const val KEY_ACTION = "eventAction"
        private const val KEY_CATEGORY = "eventCategory"
        private const val KEY_LABEL = "eventLabel"
        private const val KEY_BUSINESS_UNIT = "businessUnit"
        private const val KEY_CURRENT_SITE = "currentSite"
        private const val KEY_PRODUCT_ID = "productId"
        private const val KEY_TRACKER_ID = "trackerId"
        private const val KEY_ITEMS = "items"
        private const val KEY_SHOP_ID = "shopId"
        private const val KEY_USER_ID = "userId"
        private const val KEY_CATEGORY_ID = "category_id"
        private const val KEY_DIMENSION_117 = "dimension117"
        private const val KEY_DIMENSION_118 = "dimension118"
        private const val KEY_DIMENSION_40 = "dimension40"
        private const val KEY_DIMENSION_45 = "dimension45"
        private const val KEY_DIMENSION_87 = "dimension87"
        private const val KEY_ITEM_BRAND = "item_brand"
        private const val KEY_ITEM_CATEGORY = "item_category"
        private const val KEY_ITEM_VARIANT = "item_variant"
        private const val KEY_ITEM_ID = "item_id"
        private const val KEY_ITEM_NAME = "item_name"
        private const val KEY_PRICE = "price"
        private const val KEY_QUANTITY = "quantity"
        private const val KEY_SHOP_NAME = "shop_name"
        private const val KEY_SHOP_TYPE = "shopType"

        // Values constant
        private const val VALUE_EVENT = "clickBundling"
        private const val VALUE_EVENT_CATEGORY = "bundling selection page"
        private const val VALUE_BUSINESS_UNIT = "physical goods"
        private const val VALUE_CURRENT_SITE = "tokopediamarketplace"
        private const val VALUE_ADD_TO_CART = "addToCart"
        private const val VALUE_EVENT_ADD_TO_CART = "add_to_cart"
        private const val VALUE_NONE = "none"
        private const val VALUE_NONE_OTHER = "none / other"

        // Event actions
        private const val EA_BUNDLE_OPTION = "click - bundle option"
        private const val EA_PREVIEW_PRODUCT = "click - lihat product"
        private const val EA_SELECT_VARIANT = "click - choose product variant"
        private const val EA_BUY_BUNDLE = "click - beli paket"
        private const val EA_BACK = "click - back"

        private const val MULTIPLE_TYPE = "multiple"
        private const val SINGLE_TYPE = "single"
        private const val PRODUCT_BUNDLING = "product bundling"
        const val VALUE_MULTIPLE_BUNDLING = "multiple bundling"
        const val VALUE_SINGLE_BUNDLING = "single bundling"
    }

    var gtmTracker: ContextAnalytics? = null

    private fun initializeTracker(): ContextAnalytics {
        if (gtmTracker == null) {
            gtmTracker = TrackApp.getInstance().gtm
        }
        return gtmTracker!!
    }

    fun trackBundleOptionClick(label: String, productId: String, trackerId: String) {
        initializeTracker().sendBundleClickEvent(
            EA_BUNDLE_OPTION,
            label,
            productId,
            trackerId
        )
    }

    fun trackPreviewProductClick(label: String, productId: String, trackerId: String) {
        initializeTracker().sendBundleClickEvent(
            EA_PREVIEW_PRODUCT,
            label,
            productId,
            trackerId
        )
    }

    fun trackSelectVariantClick(label: String, productId: String, trackerId: String) {
        initializeTracker().sendBundleClickEvent(
            EA_SELECT_VARIANT,
            label,
            productId,
            trackerId
        )
    }

    fun trackBuyClick(
            userId: String,
            label: String,
            productIds: String,
            source: String,
            bundleType: String,
            trackerId: String,
            productDetails: List<ProductDetailBundleTracker>,
            bundleId: String,
            shopId: String
    ) {
        initializeTracker().sendBundleAtcClickEvent(
                userId = userId,
                productIds = productIds,
                label = label,
                source = source,
                bundleType = bundleType,
                trackerId = trackerId,
                productDetails = productDetails,
                bundleId = bundleId,
                shopId = shopId
        )
    }

    fun trackBackClick(label: String, productId: String, trackerId: String) {
        initializeTracker().sendBundleClickEvent(
            EA_BACK,
            label,
            productId,
            trackerId
        )
    }

    private fun ContextAnalytics.sendBundleClickEvent(
        action: String,
        label: String,
        productId: String,
        trackerId: String) {

        val map: Map<String, String> = mutableMapOf(
            KEY_EVENT to VALUE_EVENT,
            KEY_ACTION to action,
            KEY_CATEGORY to VALUE_EVENT_CATEGORY,
            KEY_LABEL to label,
            KEY_BUSINESS_UNIT to VALUE_BUSINESS_UNIT,
            KEY_CURRENT_SITE to VALUE_CURRENT_SITE,
            KEY_PRODUCT_ID to productId,
            KEY_TRACKER_ID to trackerId
        )
        sendGeneralEvent(map)
    }

    private fun ContextAnalytics.sendBundleAtcClickEvent(
            userId: String,
            productIds: String,
            label: String,
            source: String,
            bundleType: String,
            trackerId: String,
            productDetails: List<ProductDetailBundleTracker>,
            bundleId: String,
            shopId: String
    ) {
        val bundle = Bundle()
        val itemBundle = arrayListOf<Bundle>()
        productDetails.forEachIndexed { _, productDetailMultipleBundleTracker ->
            itemBundle.add(
                    getItemsBundlingAtc(
                            bundleId = bundleId,
                            cartId = productDetailMultipleBundleTracker.cartId,
                            productName = productDetailMultipleBundleTracker.productName,
                            productPrice = productDetailMultipleBundleTracker.productPrice,
                            quantity = productDetailMultipleBundleTracker.quantity.toString(),
                            shopId = shopId,
                            bundleType = bundleType,
                            source = source,
                            productId = productDetailMultipleBundleTracker.productId
                    )
            )
        }

        bundle.putString(TrackAppUtils.EVENT, VALUE_EVENT_ADD_TO_CART)
        bundle.putString(TrackAppUtils.EVENT_ACTION, EA_BUY_BUNDLE)
        bundle.putString(TrackAppUtils.EVENT_CATEGORY, VALUE_EVENT_CATEGORY)
        bundle.putString(TrackAppUtils.EVENT_LABEL, label)
        bundle.putString(KEY_TRACKER_ID, trackerId)
        bundle.putString(KEY_BUSINESS_UNIT, VALUE_BUSINESS_UNIT)
        bundle.putString(KEY_CURRENT_SITE, VALUE_CURRENT_SITE)
        bundle.putParcelableArrayList(KEY_ITEMS, itemBundle)
        bundle.putString(KEY_PRODUCT_ID, productIds)
        bundle.putString(KEY_SHOP_ID, shopId)
        bundle.putString(KEY_USER_ID, userId)

        sendEnhanceEcommerceEvent(VALUE_ADD_TO_CART, bundle)
    }

    private fun getItemsBundlingAtc(
            bundleId: String,
            cartId: String,
            productName: String,
            productPrice: String,
            quantity: String,
            shopId: String,
            bundleType: String,
            source: String,
            productId: String
    ): Bundle {
        var _valueBundleType = ""
        if (bundleType == VALUE_MULTIPLE_BUNDLING) {
            _valueBundleType = MULTIPLE_TYPE
        } else {
            _valueBundleType = SINGLE_TYPE
        }
        return Bundle().apply {
            putString(KEY_CATEGORY_ID, VALUE_NONE_OTHER)
            putString(KEY_DIMENSION_117, _valueBundleType)
            putString(KEY_DIMENSION_118, bundleId)
            putString(KEY_DIMENSION_40, joinDash("/$source", PRODUCT_BUNDLING, _valueBundleType ))
            putString(KEY_DIMENSION_45, cartId)
            putString(KEY_DIMENSION_87, source)
            putString(KEY_ITEM_BRAND, VALUE_NONE_OTHER)
            putString(KEY_ITEM_CATEGORY, VALUE_NONE_OTHER)
            putString(KEY_ITEM_ID, productId)
            putString(KEY_ITEM_NAME, productName)
            putString(KEY_ITEM_VARIANT, VALUE_NONE_OTHER)
            putString(KEY_PRICE, productPrice)
            putString(KEY_QUANTITY, quantity)
            putString(KEY_SHOP_ID, shopId)
            putString(KEY_SHOP_NAME, VALUE_NONE)
            putString(KEY_SHOP_TYPE, VALUE_NONE)
        }
    }

    private fun joinDash(vararg s: String?): String {
        return TextUtils.join(" - ", s)
    }
}
