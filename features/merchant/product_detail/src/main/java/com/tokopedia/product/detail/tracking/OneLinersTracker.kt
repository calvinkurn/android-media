package com.tokopedia.product.detail.tracking

import com.tokopedia.product.detail.common.ProductTrackingConstant.Category.PDP
import com.tokopedia.product.detail.common.ProductTrackingConstant.PDP.EVENT_CLICK_PG
import com.tokopedia.product.detail.common.ProductTrackingConstant.TrackerId.TRACKER_ID_CLICK_INFORMATION_STOCK_ASSURANCE
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.CURRENT_SITE
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

/**
 * Created by yovi.putra on 19/10/22"
 * Project name: android-tokopedia-core
 **/

object OneLinersTracker {

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
                TRACKER_ID to TRACKER_ID_CLICK_INFORMATION_STOCK_ASSURANCE,
                BUSINESS_UNIT to CURRENT_SITE,
                TrackerConstant.CURRENT_SITE to PDP
            ),
            productInfo = productInfo,
            componentTrackDataModel = component,
            elementName = action
        )
    }
}
