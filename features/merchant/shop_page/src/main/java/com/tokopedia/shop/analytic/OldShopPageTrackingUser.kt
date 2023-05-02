package com.tokopedia.shop.analytic

import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ATTRIBUTION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT_ACTION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT_CATEGORY
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT_LABEL
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEMS_PAGE_TYPE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEMS_PRODUCT_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEMS_SHOP_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEMS_SHOP_TYPE
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageAttribution
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageProduct
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.*

open class OldShopPageTrackingUser(
    protected val trackingQueue: TrackingQueue
) {

    protected fun sendEvent(
        event: String?,
        category: String?,
        action: String?,
        label: String?,
        customDimensionShopPage: CustomDimensionShopPage?
    ) {
        val eventMap = createMap(event, category, action, label, customDimensionShopPage)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventMap)
    }

    fun sendAllTrackingQueue() {
        trackingQueue.sendAll()
    }

    protected fun createMap(
        event: String?,
        category: String?,
        action: String?,
        label: String?,
        customDimensionShopPage: CustomDimensionShopPage?
    ): HashMap<String?, Any?> {
        val eventMap = HashMap<String?, Any?>()
        eventMap[EVENT] = event
        eventMap[EVENT_CATEGORY] = category
        eventMap[EVENT_ACTION] = action
        eventMap[EVENT_LABEL] = label
        if (customDimensionShopPage != null) {
            addCustomDimension(eventMap, customDimensionShopPage)
            if (customDimensionShopPage is CustomDimensionShopPageProduct) {
                eventMap[ITEMS_PRODUCT_ID] = customDimensionShopPage.productId
            }
            if (customDimensionShopPage is CustomDimensionShopPageAttribution) {
                eventMap[ATTRIBUTION] = customDimensionShopPage.attribution
            }
        }
        return eventMap
    }


    private fun addCustomDimension(
        eventMap: HashMap<String?, Any?>,
        customDimensionShopPage: CustomDimensionShopPage
    ) {
        eventMap[ITEMS_SHOP_ID] = customDimensionShopPage.shopId
        eventMap[ITEMS_SHOP_TYPE] = customDimensionShopPage.shopType
        eventMap[ITEMS_PAGE_TYPE] = ShopPageTracking.SHOPPAGE
    }




    companion object {
        const val SHOP_PAGE = "Shop page"
    }
}
