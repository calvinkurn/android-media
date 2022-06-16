package com.tokopedia.shop_widget.common.analytic

import android.text.TextUtils
import com.tokopedia.shop_widget.common.analytic.model.CustomDimensionShopWidget
import com.tokopedia.shop_widget.common.analytic.model.CustomDimensionShopWidgetAttribution
import com.tokopedia.shop_widget.common.analytic.model.CustomDimensionShopWidgetProduct
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.HashMap

open class ShopWidgetTracker(val trackingQueue: TrackingQueue) {

    protected fun sendEvent(event: String?, category: String?, action: String?, label: String?,
                          customDimensionShopWidget: CustomDimensionShopWidget?) {
        val eventMap = createMap(event, category, action, label, customDimensionShopWidget)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventMap)
    }

    protected fun joinDash(vararg s: String?): String {
        return TextUtils.join(" - ", s)
    }

    fun sendAllTrackingQueue() {
        trackingQueue.sendAll()
    }

    private fun createMap(event: String?, category: String?, action: String?, label: String?,
                          customDimensionShopWidget: CustomDimensionShopWidget?): HashMap<String?, Any?> {
        val eventMap = HashMap<String?, Any?>()
        eventMap[ShopWidgetTrackingConstant.EVENT] = event
        eventMap[ShopWidgetTrackingConstant.EVENT_CATEGORY] = category
        eventMap[ShopWidgetTrackingConstant.EVENT_ACTION] = action
        eventMap[ShopWidgetTrackingConstant.EVENT_LABEL] = label
        if (customDimensionShopWidget != null) {
            addCustomDimension(eventMap, customDimensionShopWidget)
            if (customDimensionShopWidget is CustomDimensionShopWidgetProduct) {
                eventMap[ShopWidgetTrackingConstant.PRODUCT_ID] = customDimensionShopWidget.productId
            }
            if (customDimensionShopWidget is CustomDimensionShopWidgetAttribution) {
                eventMap[ShopWidgetTrackingConstant.ATTRIBUTION] = customDimensionShopWidget.attribution
            }
        }
        return eventMap
    }

    private fun addCustomDimension(eventMap: HashMap<String?, Any?>,
                                   customDimensionShopWidget: CustomDimensionShopWidget) {
        eventMap[ShopWidgetTrackingConstant.SHOP_ID] = customDimensionShopWidget.shopId
        eventMap[ShopWidgetTrackingConstant.SHOP_TYPE] = customDimensionShopWidget.shopType
        eventMap[ShopWidgetTrackingConstant.PAGE_TYPE] = ShopWidgetTrackingConstant.SHOPPAGE
    }
}