package com.tokopedia.product_bundle.tracking

import com.tokopedia.track.TrackApp
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

        // Values constant
        private const val VALUE_EVENT = "clickBundling"
        private const val VALUE_EVENT_CATEGORY = "bundling selection page"
        private const val VALUE_BUSINESS_UNIT = "physical goods"
        private const val VALUE_CURRENT_SITE = "tokopediamarketplace"

        // Event actions
        private const val EA_BUNDLE_OPTION = "click - bundle option"
        private const val EA_PREVIEW_PRODUCT = "click - lihat product"
        private const val EA_SELECT_VARIANT = "click - choose product variant"
        private const val EA_BUY_BUNDLE = "click - beli paket"
        private const val EA_BACK = "click - back"
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

    fun trackBuyClick(label: String, productId: String) {
        initializeTracker().sendBundleClickEvent(
            EA_BUY_BUNDLE,
            label,
            productId
        )
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
}