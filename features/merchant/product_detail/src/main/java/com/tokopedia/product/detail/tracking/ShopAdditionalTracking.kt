package com.tokopedia.product.detail.tracking

import com.tokopedia.product.detail.common.ProductTrackingConstant
import com.tokopedia.product.detail.common.ProductTrackingConstant.Category.PDP
import com.tokopedia.product.detail.common.ProductTrackingConstant.PDP.EVENT_CLICK_PG
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.util.TrackingUtil
import com.tokopedia.product.detail.tracking.TrackingConstant.Hit.SHOP_ID
import com.tokopedia.product.detail.tracking.TrackingConstant.Hit.TRACKER_ID
import com.tokopedia.product.detail.tracking.TrackingConstant.Hit.USER_ID
import com.tokopedia.track.constant.TrackerConstant
import com.tokopedia.track.constant.TrackerConstant.BUSINESS_UNIT
import com.tokopedia.track.constant.TrackerConstant.EVENT
import com.tokopedia.track.constant.TrackerConstant.EVENT_ACTION
import com.tokopedia.track.constant.TrackerConstant.EVENT_CATEGORY
import com.tokopedia.track.constant.TrackerConstant.EVENT_LABEL

/**
 * Created by yovi.putra on 27/10/22"
 * Project name: android-tokopedia-core
 **/

object ShopAdditionalTracking {

    fun clickLearnButton(
        component: ComponentTrackDataModel,
        productInfo: DynamicProductInfoP1?,
        userId: String,
        eventLabel: String
    ) {
        val action = "click - pelajari on dilayani tokopedia component"

        TrackingUtil.addComponentTracker(
            mutableMapOf(
                EVENT to EVENT_CLICK_PG,
                EVENT_ACTION to action,
                EVENT_CATEGORY to PDP,
                EVENT_LABEL to eventLabel,
                TRACKER_ID to "38259",
                BUSINESS_UNIT to PDP,
                SHOP_ID to productInfo?.basic?.shopID.orEmpty(),
                USER_ID to userId,
                TrackerConstant.CURRENT_SITE to ProductTrackingConstant.Tracking.CURRENT_SITE
            ),
            productInfo = productInfo,
            componentTrackDataModel = component,
            elementName = action
        )
    }
}
