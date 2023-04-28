package com.tokopedia.product.detail.tracking

import com.tokopedia.product.detail.common.ProductTrackingConstant
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.util.TrackingUtil
import com.tokopedia.track.constant.TrackerConstant
import com.tokopedia.trackingoptimizer.TrackingQueue

/**
 * Created by yovi.putra on 28/04/23"
 * Project name: tokopedia-app-wg
 **/

object ProductDetailInfoTracking {

    fun onClickAnnotationGeneric(
        component: ComponentTrackDataModel,
        productInfo: DynamicProductInfoP1?,
        key: String
    ) {
        val action = "click - information button on oneliner component"

        TrackingUtil.addComponentTracker(
            mutableMapOf(
                TrackerConstant.EVENT to ProductTrackingConstant.PDP.EVENT_CLICK_PG,
                TrackerConstant.EVENT_ACTION to action,
                TrackerConstant.EVENT_CATEGORY to ProductTrackingConstant.Category.PDP,
                TrackerConstant.EVENT_LABEL to key,
                TrackingConstant.Hit.TRACKER_ID to "38045",
                TrackerConstant.BUSINESS_UNIT to ProductTrackingConstant.Category.PDP,
                TrackerConstant.CURRENT_SITE to ProductTrackingConstant.Tracking.CURRENT_SITE
            ),
            productInfo = productInfo,
            componentTrackDataModel = component,
            elementName = action
        )
    }

    fun onImpressionAnnotationGeneric(
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
            put(TrackingConstant.Hit.TRACKER_ID, "18022")
            put(
                ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT,
                ProductTrackingConstant.Category.PDP
            )
            put(
                ProductTrackingConstant.Tracking.KEY_CURRENT_SITE,
                ProductTrackingConstant.Tracking.CURRENT_SITE
            )
        }

        trackingQueue?.putEETracking(mapEvent as HashMap<String, Any>)
    }
}
