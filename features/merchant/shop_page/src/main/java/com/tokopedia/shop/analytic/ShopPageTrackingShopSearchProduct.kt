package com.tokopedia.shop.analytic

import com.tokopedia.shop.analytic.ShopPageTrackingConstant.*
import com.tokopedia.trackingoptimizer.TrackingQueue


class ShopPageTrackingShopSearchProduct(
        trackingQueue: TrackingQueue
) : ShopPageTrackingUser(trackingQueue) {

    fun clickCartButton(pageName: String, keyword: String) {
        sendEvent(
                CLICK_TOP_NAV,
                String.format(TOP_NAV, pageName),
                SHOP_SEARCH_PRODUCT_CLICK_CART_BUTTON,
                keyword,
                null
        )
    }

    fun clickShareButton(pageName: String, keyword: String) {
        sendEvent(
                CLICK_TOP_NAV,
                String.format(TOP_NAV, pageName),
                SHOP_SEARCH_PRODUCT_CLICK_SHARE_BUTTON,
                keyword,
                null
        )
    }

    fun clickManualSearch(pageName: String, keyword: String, pageUrl: String) {
        sendEvent(
                CLICK_TOP_NAV,
                String.format(TOP_NAV, pageName),
                SHOP_SEARCH_PRODUCT_CLICK_SEARCH,
                String.format(LABEL_SHOP_SEARCH_PRODUCT_KEYWORD_PAGEURL, keyword, pageUrl),
                null
        )
    }

    fun clickAutocompleteInternalShopPage(pageName: String, keyword: String, pageUrl: String) {
        sendEvent(
                CLICK_TOP_NAV,
                String.format(TOP_NAV, pageName),
                SHOP_SEARCH_PRODUCT_CLICK_ETALASE_AUTOCOMPLETE,
                String.format(LABEL_SHOP_SEARCH_PRODUCT_KEYWORD_PAGEURL, keyword, pageUrl),
                null
        )
    }

    fun clickAutocompleteExternalShopPage(pageName: String, keyword: String, pageUrl: String) {
        sendEvent(
                CLICK_TOP_NAV,
                String.format(TOP_NAV, pageName),
                SHOP_SEARCH_PRODUCT_CLICK_GLOBAL_SEARCH,
                String.format(LABEL_SHOP_SEARCH_PRODUCT_KEYWORD_PAGEURL, keyword, pageUrl),
                null
        )
    }

    fun clickAutocompleteProducts(pageName: String, keyword: String, pageUrl: String) {
        sendEvent(
                CLICK_TOP_NAV,
                String.format(TOP_NAV, pageName),
                SHOP_SEARCH_PRODUCT_CLICK_PRODUCT_AUTOCOMPLETE,
                String.format(LABEL_SHOP_SEARCH_PRODUCT_KEYWORD_VALUE_PAGEURL, keyword, VALUE_PRODUCT, pageUrl),
                null
        )
    }
}
