package com.tokopedia.product.detail.tracking

import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.util.TrackingUtil

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
        val trackerId = "41543"

        TrackingUtil.addClickEvent(
            productInfo = productInfo,
            trackDataModel = trackDataModel,
            action = action,
            trackerId = trackerId,
            eventLabel = eventLabel
        )
    }
}
