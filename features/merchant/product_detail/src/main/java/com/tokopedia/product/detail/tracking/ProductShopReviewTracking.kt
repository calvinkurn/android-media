package com.tokopedia.product.detail.tracking

import com.tokopedia.product.detail.common.ProductTrackingConstant
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.util.TrackingUtil
import com.tokopedia.track.constant.TrackerConstant

/**
 * Created by yovi.putra on 25/01/23"
 * Project name: android-tokopedia-core
 **/
object ProductShopReviewTracking {

    fun onItemClicked(
        productInfo: DynamicProductInfoP1?,
        trackDataModel: ComponentTrackDataModel?,
        eventLabel: String
    ) {
        val action = "click - shop review - see all"

        TrackingUtil.addComponentTracker(
            mutableMapOf(
                TrackerConstant.EVENT to ProductTrackingConstant.PDP.EVENT_CLICK_PG,
                TrackerConstant.EVENT_ACTION to action,
                TrackerConstant.EVENT_CATEGORY to ProductTrackingConstant.Category.PDP,
                TrackerConstant.EVENT_LABEL to eventLabel,
                TrackingConstant.Hit.TRACKER_ID to "41543",
                TrackerConstant.BUSINESS_UNIT to ProductTrackingConstant.Category.PDP,
                TrackerConstant.CURRENT_SITE to ProductTrackingConstant.Tracking.CURRENT_SITE
            ),
            productInfo = productInfo,
            componentTrackDataModel = trackDataModel,
            elementName = action
        )
    }
}
