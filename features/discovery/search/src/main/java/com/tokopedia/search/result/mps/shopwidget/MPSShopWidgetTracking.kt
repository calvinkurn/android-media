package com.tokopedia.search.result.mps.shopwidget

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.search.analytics.SearchEventTracking.Category.Companion.SEARCH_RESULT
import com.tokopedia.search.analytics.SearchEventTracking.Companion.BUSINESS_UNIT
import com.tokopedia.search.analytics.SearchEventTracking.Companion.CURRENT_SITE
import com.tokopedia.search.analytics.SearchEventTracking.Companion.SEARCH
import com.tokopedia.search.analytics.SearchEventTracking.Companion.TOKOPEDIA_MARKETPLACE
import com.tokopedia.search.analytics.SearchEventTracking.ECommerce.Companion.ACTION_FIELD
import com.tokopedia.search.analytics.SearchEventTracking.ECommerce.Companion.CLICK
import com.tokopedia.search.analytics.SearchEventTracking.ECommerce.Companion.CURRENCY_CODE
import com.tokopedia.search.analytics.SearchEventTracking.ECommerce.Companion.ECOMMERCE
import com.tokopedia.search.analytics.SearchEventTracking.ECommerce.Companion.IDR
import com.tokopedia.search.analytics.SearchEventTracking.ECommerce.Companion.IMPRESSIONS
import com.tokopedia.search.analytics.SearchEventTracking.ECommerce.Companion.LIST
import com.tokopedia.search.analytics.SearchEventTracking.ECommerce.Companion.PRODUCTS
import com.tokopedia.search.analytics.SearchEventTracking.Event.Companion.PRODUCT_CLICK
import com.tokopedia.search.analytics.SearchEventTracking.Event.Companion.PRODUCT_VIEW
import com.tokopedia.search.analytics.SearchTracking
import com.tokopedia.search.analytics.SearchTrackingConstant
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL
import com.tokopedia.trackingoptimizer.TrackingQueue

object MPSShopWidgetTracking {

    private const val IMPRESSION_CAROUSEL_PRODUCT = "impression - carousel product"
    private const val CLICK_CAROUSEL_PRODUCT = "click - carousel product"
    private const val SHOP_CARD_MPS = "shop_card_mps"

    fun impressionShopWidgetProduct(
        trackingQueue: TrackingQueue,
        list: ArrayList<Any>,
        keywords: String,
        shopId: String,
    ) {
        val map = DataLayer.mapOf(
            EVENT, PRODUCT_VIEW,
            EVENT_CATEGORY, SEARCH_RESULT,
            EVENT_ACTION, IMPRESSION_CAROUSEL_PRODUCT,
            EVENT_LABEL, "$keywords - $SHOP_CARD_MPS - $shopId",
            CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
            BUSINESS_UNIT, SEARCH,
            ECOMMERCE, DataLayer.mapOf(
                CURRENCY_CODE, IDR,
                IMPRESSIONS, list,
            )
        )

        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    fun clickShopWidgetProduct(
        productData: Any,
        componentId: String,
        keywords: String,
        shopId: String,
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                EVENT, PRODUCT_CLICK,
                EVENT_CATEGORY, SEARCH_RESULT,
                EVENT_ACTION, CLICK_CAROUSEL_PRODUCT,
                EVENT_LABEL, "$keywords - $SHOP_CARD_MPS - $shopId",
                CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
                BUSINESS_UNIT, SEARCH,
                ECOMMERCE, DataLayer.mapOf(
                    CLICK, DataLayer.mapOf(
                        ACTION_FIELD, DataLayer.mapOf(
                            LIST, "/searchmps - organic - component: $componentId"
                        ),
                        PRODUCTS, DataLayer.listOf(productData)
                    )
                ),
            )
        )
    }
}
