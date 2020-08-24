package com.tokopedia.shop.analytic

import com.tokopedia.shop.analytic.ShopPageTrackingConstant.*
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.trackingoptimizer.TrackingQueue


class ShopPageTrackingShopSearchProduct(
        trackingQueue: TrackingQueue
) : ShopPageTracking(trackingQueue) {

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

    fun clickAutocompleteInternalShopPage(isOwner: Boolean, keyword: String, customDimensionShopPage: CustomDimensionShopPage) {
        sendGeneralEvent(
                CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                String.format(SHOP_SEARCH_PRODUCT_CLICK_ETALASE_AUTOCOMPLETE, keyword),
                keyword,
                customDimensionShopPage
        )
    }

    fun clickAutocompleteInternalShopPageProductEmpty(isOwner: Boolean, keyword: String, customDimensionShopPage: CustomDimensionShopPage) {
        sendGeneralEvent(
                CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                String.format(SHOP_SEARCH_PRODUCT_CLICK_ETALASE_AUTOCOMPLETE_EMPTY, keyword),
                keyword,
                customDimensionShopPage
        )
    }

    fun clickAutocompleteExternalShopPage(isOwner: Boolean, keyword: String, customDimensionShopPage: CustomDimensionShopPage) {
        sendGeneralEvent(
                CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                String.format(SHOP_SEARCH_PRODUCT_CLICK_GLOBAL_SEARCH, keyword),
                "",
                customDimensionShopPage
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

    fun shopPageProductSearchResult(isOwner: Boolean, keyword: String, customDimensionShopPage: CustomDimensionShopPage) {
        sendGeneralEvent(
                CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                SEARCH,
                keyword,
                customDimensionShopPage
        )
    }

    fun shopPageProductSearchNoResult(isOwner: Boolean, keyword: String, customDimensionShopPage: CustomDimensionShopPage) {
        sendGeneralEvent(
                CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                SEARCH_NO_RESULT,
                keyword,
                customDimensionShopPage
        )
    }
}
