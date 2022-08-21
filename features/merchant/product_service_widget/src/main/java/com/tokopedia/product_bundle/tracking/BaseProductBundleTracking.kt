package com.tokopedia.product_bundle.tracking

import android.os.Bundle
import android.text.TextUtils
import com.tokopedia.common.ProductServiceWidgetConstant.TrackerId.ADD_TO_CART_MULTIPLE_BUNDLING
import com.tokopedia.product_bundle.common.data.model.uimodel.AddToCartDataResult
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
    }

    var gtmTracker: ContextAnalytics? = null

    private fun initializeTracker(): ContextAnalytics {
        if (gtmTracker == null) {
            gtmTracker = TrackApp.getInstance().gtm
        }
        return gtmTracker!!
    }

    fun trackBundleOptionClick(label: String, productId: String) {
        initializeTracker().sendBundleClickEvent(
            EA_BUNDLE_OPTION,
            label,
            productId
        )
    }

    fun trackPreviewProductClick(label: String, productId: String) {
        initializeTracker().sendBundleClickEvent(
            EA_PREVIEW_PRODUCT,
            label,
            productId
        )
    }

    fun trackSelectVariantClick(label: String, productId: String) {
        initializeTracker().sendBundleClickEvent(
            EA_SELECT_VARIANT,
            label,
            productId
        )
    }

    fun trackBuyClick(
            userId: String,
            label: String,
            productId: String,
            atcResult: AddToCartDataResult,
            source: String,
            bundleName: String,
            bundleType: String,
            bundlePrice: Long
    ) {
        initializeTracker().sendBundleAtcClickEvent(
                userId = userId,
                productId = productId,
                label = label,
                atcResult = atcResult,
                source = source,
                bundleName = bundleName,
                bundleType = bundleType,
                bundlePrice = bundlePrice
        )

//        initializeTracker().sendBundleClickEvent(
//            EA_BUY_BUNDLE,
//            label,
//            productId
//        )
    }

    fun trackBackClick(label: String, productId: String) {
        initializeTracker().sendBundleClickEvent(
            EA_BACK,
            label,
            productId
        )
    }

    private fun ContextAnalytics.sendBundleClickEvent(
        action: String,
        label: String,
        productId: String) {

        val map: Map<String, String> = mutableMapOf(
            KEY_EVENT to VALUE_EVENT,
            KEY_ACTION to action,
            KEY_CATEGORY to VALUE_EVENT_CATEGORY,
            KEY_LABEL to label,
            KEY_BUSINESS_UNIT to VALUE_BUSINESS_UNIT,
            KEY_CURRENT_SITE to VALUE_CURRENT_SITE,
            KEY_PRODUCT_ID to productId
        )
        sendGeneralEvent(map)
    }

    private fun ContextAnalytics.sendBundleAtcClickEvent(
            userId: String,
            productId: String,
            label: String,
            atcResult: AddToCartDataResult,
            source: String,
            bundleName: String,
            bundleType: String,
            bundlePrice: Long
    ) {
        val bundle = Bundle()
        val itemBundle = arrayListOf<Bundle>()
        val shopId = atcResult.requestParams.shopId
        atcResult.responseResult.data.forEachIndexed { position, productDataModel ->
            itemBundle.add(
                    getItemsBundlingAtc(
                            bundleId = atcResult.requestParams.bundleId,
                            cartId = productDataModel.cartId,
                            bundleName = bundleName,
                            bundlePrice = bundlePrice,
                            quantity = productDataModel.quantity.toString(),
                            shopId = shopId,
                            bundleType = bundleType,
                            source = source
                    )
            )
        }

        bundle.putString(TrackAppUtils.EVENT, VALUE_EVENT_ADD_TO_CART)
        bundle.putString(TrackAppUtils.EVENT_ACTION, EA_BUY_BUNDLE)
        bundle.putString(TrackAppUtils.EVENT_CATEGORY, VALUE_EVENT_CATEGORY)
        bundle.putString(TrackAppUtils.EVENT_LABEL, label)
        bundle.putString(KEY_TRACKER_ID, ADD_TO_CART_MULTIPLE_BUNDLING)
        bundle.putString(KEY_BUSINESS_UNIT, VALUE_BUSINESS_UNIT)
        bundle.putString(KEY_CURRENT_SITE, VALUE_CURRENT_SITE)
        bundle.putParcelableArrayList(KEY_ITEMS, itemBundle)
        bundle.putString(KEY_PRODUCT_ID, productId)
        bundle.putString(KEY_SHOP_ID, shopId)
        bundle.putString(KEY_USER_ID, userId)

        sendEnhanceEcommerceEvent(VALUE_ADD_TO_CART, bundle)
    }

    private fun getItemsBundlingAtc(
            bundleId: String,
            cartId: String,
            bundleName: String,
            bundlePrice: Long,
            quantity: String,
            shopId: String,
            bundleType: String,
            source: String
    ): Bundle {
        var _valueBundleType = ""
        if (bundleType == VALUE_MULTIPLE_BUNDLING) {
            _valueBundleType = MULTIPLE_TYPE
        } else {
            _valueBundleType = SINGLE_TYPE
        }
        return Bundle().apply {
            putString(KEY_CATEGORY_ID, VALUE_NONE_OTHER)
            putString(KEY_DIMENSION_117, bundleType)
            putString(KEY_DIMENSION_118, bundleId)
            putString(KEY_DIMENSION_40, joinDash("/$source", PRODUCT_BUNDLING, _valueBundleType ))
            putString(KEY_DIMENSION_45, cartId)
            putString(KEY_DIMENSION_87, source)
            putString(KEY_ITEM_BRAND, VALUE_NONE_OTHER)
            putString(KEY_ITEM_CATEGORY, VALUE_NONE_OTHER)
            putString(KEY_ITEM_ID, bundleId)
            putString(KEY_ITEM_NAME, bundleName)
            putString(KEY_ITEM_VARIANT, VALUE_NONE_OTHER)
            putLong(KEY_PRICE, bundlePrice)
            putString(KEY_QUANTITY, quantity)
            putString(KEY_SHOP_ID, shopId)
            putString(KEY_SHOP_NAME, VALUE_NONE)
            putString(KEY_SHOP_TYPE, VALUE_NONE)
        }
    }

    private fun joinDash(vararg s: String?): String {
        return TextUtils.join(" - ", s)
    }

//    {
//  "event": "add_to_cart",
//  "eventAction": "click - beli paket",
//  "eventCategory": "bundling selection page",
//  "eventLabel": "bundling_id:{{bundling_id}}; bundling_type:{{bundling_type}};",
//  "trackerId": "20010",
//  "businessUnit": "{businessUnit}",
//  "currentSite": "{currentSite}",
//  "items": [
//    {
//      "category_id": "{product_category_id}",
//      "dimension117": "{bundling_type}",
//      "dimension118": "{bundling_id}",
//      "dimension40": "/{source} - product bundling - {bundling_type}",
//      "dimension45": "{cart_id}",
//      "dimension87": "{source}",
//      "item_brand": "{product_brand}",
//      "item_category": "{category_name}",
//      "item_id": "{product_id}",
//      "item_name": "{product_name}",
//      "item_variant": "{product_variant}",
//      "price": "{product_price}",
//      "quantity": "{quantity}",
//      "shop_id": "{shop_id}",
//      "shop_name": "{shop_name}",
//      "shop_type": "{shop_type}"
//    }
//  ],
//  "productId": "{product_id}",
//  "shopId": "{shop_id}",
//  "userId": "{user_id}"
//}

}