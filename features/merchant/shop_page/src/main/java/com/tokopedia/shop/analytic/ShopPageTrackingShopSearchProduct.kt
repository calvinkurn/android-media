package com.tokopedia.shop.analytic

import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SHOP_PAGE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_TOP_NAV
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.LABEL_SHOP_SEARCH_PRODUCT_KEYWORD_VALUE_PAGEURL
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_SEARCH_PRODUCT_CLICK_GLOBAL_SEARCH
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_SEARCH_PRODUCT_CLICK_PRODUCT_AUTOCOMPLETE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TOP_NAV
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VALUE_PRODUCT
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.trackingoptimizer.TrackingQueue


class ShopPageTrackingShopSearchProduct(
        trackingQueue: TrackingQueue
) : ShopPageTracking(trackingQueue) {

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

}
