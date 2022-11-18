package com.tokopedia.product.detail.tracking

import com.tokopedia.product.detail.common.ProductTrackingConstant.Category.PDP
import com.tokopedia.product.detail.common.ProductTrackingConstant.PDP.EVENT_CLICK_PG
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.CURRENT_SITE
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.KEY_CURRENT_SITE
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.util.TrackingUtil
import com.tokopedia.product.detail.tracking.TrackingConstant.Hit.TRACKER_ID
import com.tokopedia.track.constant.TrackerConstant
import com.tokopedia.track.constant.TrackerConstant.BUSINESS_UNIT
import com.tokopedia.track.constant.TrackerConstant.EVENT
import com.tokopedia.track.constant.TrackerConstant.EVENT_ACTION
import com.tokopedia.track.constant.TrackerConstant.EVENT_CATEGORY
import com.tokopedia.track.constant.TrackerConstant.EVENT_LABEL
import com.tokopedia.trackingoptimizer.TrackingQueue

/**
 * Created by yovi.putra on 19/10/22"
 * Project name: android-tokopedia-core
 **/

object OneLinersTracking {

    fun clickInformationButton(
        component: ComponentTrackDataModel,
        productInfo: DynamicProductInfoP1?,
        eventLabel: String
    ) {
        val action = "click - information button on oneliner component"

        TrackingUtil.addComponentTracker(
            mutableMapOf(
                EVENT to EVENT_CLICK_PG,
                EVENT_ACTION to action,
                EVENT_CATEGORY to PDP,
                EVENT_LABEL to eventLabel,
                TRACKER_ID to "38045",
                BUSINESS_UNIT to PDP,
                TrackerConstant.CURRENT_SITE to CURRENT_SITE
            ),
            productInfo = productInfo,
            componentTrackDataModel = component,
            elementName = action
        )
    }


    /**
     * 26-10-2021
     * Only triggered when using
     * oneliner - stock assurance
     */
    fun onImpression(
        trackingQueue: TrackingQueue?,
        componentTrackDataModel: ComponentTrackDataModel,
        productInfo: DynamicProductInfoP1?,
        userId: String,
        lcaWarehouseId: String,
        label: String
    ) {
        val productId = productInfo?.basic?.productID ?: ""
        val mapEvent = TrackingUtil.createCommonImpressionTracker(
            productInfo = productInfo,
            componentTrackDataModel = componentTrackDataModel,
            userId = userId,
            lcaWarehouseId = lcaWarehouseId,
            customAction = "view - pdp oneliner component",
            customCreativeName = "",
            customItemName = "product detail page - $productId",
            customLabel = "",
            customPromoCode = "",
            customItemId = "text:$label"
        )?.apply {
            put(TRACKER_ID, "18022")
            put(KEY_BUSINESS_UNIT, PDP)
            put(KEY_CURRENT_SITE, CURRENT_SITE)
        }

        trackingQueue?.putEETracking(mapEvent as HashMap<String, Any>)
    }
}
