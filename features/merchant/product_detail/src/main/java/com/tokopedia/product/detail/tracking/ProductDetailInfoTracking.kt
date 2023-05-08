package com.tokopedia.product.detail.tracking

import com.tokopedia.product.detail.common.ProductTrackingConstant
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.util.TrackingUtil
import com.tokopedia.track.constant.TrackerConstant

/**
 * Created by yovi.putra on 28/04/23"
 * Project name: tokopedia-app-wg
 **/

object ProductDetailInfoTracking {

    fun onClickAnnotationGeneric(
        trackDataModel: ComponentTrackDataModel,
        productInfo: DynamicProductInfoP1?,
        key: String,
        userId: String
    ) {
        doTracking(
            component = trackDataModel,
            productInfo = productInfo,
            event = ProductTrackingConstant.PDP.EVENT_CLICK_PG,
            action = "click - annotation lihat panduan ukuran on pdp",
            trackerId = "43296",
            userId = userId
        )
    }

    fun onImpressionAnnotationGeneric(
        trackDataModel: ComponentTrackDataModel,
        productInfo: DynamicProductInfoP1?,
        userId: String
    ) {
        doTracking(
            component = trackDataModel,
            productInfo = productInfo,
            event = ProductTrackingConstant.PDP.EVENT_VIEW_PG_IRIS,
            action = "impression - annotation lihat panduan ukuran on pdp",
            trackerId = "43295",
            userId = userId
        )
    }

    private fun doTracking(
        component: ComponentTrackDataModel,
        productInfo: DynamicProductInfoP1?,
        event: String,
        action: String,
        trackerId: String,
        userId: String
    ) {
        val productId = productInfo?.basic?.productID.orEmpty()
        val label = "Product_ID : $productId"
        val category = "product detail category"
        val user = "$userId User ID: $userId"

        TrackingUtil.addClickEvent(
            productInfo = productInfo,
            trackDataModel = component,
            action = action,
            trackerId = trackerId,
            eventLabel = label,
            modifier = {
                it[TrackerConstant.EVENT] = event
                it[TrackerConstant.EVENT_CATEGORY] = category
                it[TrackerConstant.USERID] = user
            }
        )
    }
}
