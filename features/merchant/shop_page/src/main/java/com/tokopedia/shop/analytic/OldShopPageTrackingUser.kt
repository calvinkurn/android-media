package com.tokopedia.shop.analytic

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
        eventMap[OldShopPageTrackingConstant.EVENT] = event
        eventMap[OldShopPageTrackingConstant.EVENT_CATEGORY] = category
        eventMap[OldShopPageTrackingConstant.EVENT_ACTION] = action
        eventMap[OldShopPageTrackingConstant.EVENT_LABEL] = label
        if (customDimensionShopPage != null) {
            addCustomDimension(eventMap, customDimensionShopPage)
            if (customDimensionShopPage is CustomDimensionShopPageProduct) {
                eventMap[OldShopPageTrackingConstant.PRODUCT_ID] = customDimensionShopPage.productId
            }
            if (customDimensionShopPage is CustomDimensionShopPageAttribution) {
                eventMap[OldShopPageTrackingConstant.ATTRIBUTION] = customDimensionShopPage.attribution
            }
        }
        return eventMap
    }


    private fun addCustomDimension(
        eventMap: HashMap<String?, Any?>,
        customDimensionShopPage: CustomDimensionShopPage
    ) {
        eventMap[OldShopPageTrackingConstant.SHOP_ID] = customDimensionShopPage.shopId
        eventMap[OldShopPageTrackingConstant.SHOP_TYPE] = customDimensionShopPage.shopType
        eventMap[OldShopPageTrackingConstant.PAGE_TYPE] = SHOPPAGE
    }




    companion object {
        const val SHOPPAGE = "/shoppage"
        const val SHOP_PAGE = "Shop page"
    }
}
