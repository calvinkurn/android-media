package com.tokopedia.product.detail.tracking

import com.tokopedia.product.detail.tracking.TrackingConstant.Hit
import com.tokopedia.product.detail.tracking.TrackingConstant.Value
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue

object PageErrorTracking {

    private const val ACTION_IMPRESSION = "impression"
    private const val ACTION_CLICK_HOMEPAGE = "click - kembali ke homepage"
    private const val CATEGORY_404_NOT_FOUND = "404 not found"

    fun impressPageNotFound(data: PageErrorTracker){
        val mapEvent = hashMapOf<String, Any>(
            Hit.EVENT to Value.VIEW_PG,
            Hit.EVENT_ACTION to ACTION_IMPRESSION,
            Hit.EVENT_CATEGORY to CATEGORY_404_NOT_FOUND,
            Hit.EVENT_LABEL to "PDP - ${data.deeplink} - ${data.finalProductId}",
            Hit.TRACKER_ID to "33122",
            Hit.BUSINESS_UNIT to Value.PRODUCT_DETAIL_PAGE,
            Hit.CURRENT_SITE to Value.TOKOPEDIA_MARKETPLACE,
            Hit.PRODUCT_ID to data.finalProductId
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun clickBackToHomepage(data: PageErrorTracker) {
        val mapEvent = hashMapOf<String, Any>(
            Hit.EVENT to Value.CLICK_PG,
            Hit.EVENT_ACTION to ACTION_CLICK_HOMEPAGE,
            Hit.EVENT_CATEGORY to CATEGORY_404_NOT_FOUND,
            Hit.EVENT_LABEL to "PDP - ${data.deeplink} - ${data.finalProductId}",
            Hit.TRACKER_ID to "33123",
            Hit.BUSINESS_UNIT to Value.PRODUCT_DETAIL_PAGE,
            Hit.CURRENT_SITE to Value.TOKOPEDIA_MARKETPLACE,
            Hit.PRODUCT_ID to data.finalProductId
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

}