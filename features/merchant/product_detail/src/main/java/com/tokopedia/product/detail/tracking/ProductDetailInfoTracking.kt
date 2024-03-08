package com.tokopedia.product.detail.tracking

import com.tokopedia.product.detail.common.ProductTrackingConstant
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductInfoP1
import com.tokopedia.product.detail.data.model.datamodel.product_detail_info.ProductDetailInfoAnnotationTrackData
import com.tokopedia.product.detail.data.util.TrackingUtil
import com.tokopedia.track.constant.TrackerConstant

/**
 * Created by yovi.putra on 28/04/23"
 * Project name: tokopedia-app-wg
 **/

object ProductDetailInfoTracking {

    fun onClickAnnotationGeneric(
        trackDataModel: ProductDetailInfoAnnotationTrackData,
        productInfo: ProductInfoP1?
    ) {
        doTracking(
            component = trackDataModel,
            productInfo = productInfo,
            event = ProductTrackingConstant.PDP.EVENT_CLICK_PG,
            action = "click - annotation information on detail produk",
            trackerId = "43296"
        )
    }

    fun onImpressionAnnotationGeneric(
        trackDataModel: ProductDetailInfoAnnotationTrackData,
        productInfo: ProductInfoP1?
    ) {
        doTracking(
            component = trackDataModel,
            productInfo = productInfo,
            event = ProductTrackingConstant.PDP.EVENT_VIEW_PG_IRIS,
            action = "impression - annotation information on detail produk",
            trackerId = "43295"
        )
    }

    private fun doTracking(
        component: ProductDetailInfoAnnotationTrackData,
        productInfo: ProductInfoP1?,
        event: String,
        action: String,
        trackerId: String
    ) {
        val label = "annotation_key:${component.key};annotation_value:${component.value}"
        val shopId = productInfo?.basic?.shopID.orEmpty()
        val userId = component.userId

        TrackingUtil.addClickEvent(
            productInfo = productInfo,
            trackDataModel = component.parentTrackData,
            action = action,
            trackerId = trackerId,
            eventLabel = label,
            modifier = {
                it[TrackerConstant.EVENT] = event
                it[TrackerConstant.USERID] = userId
                it[TrackerConstant.SHOP_ID] = shopId
            }
        )
    }
}
