package com.tokopedia.product.detail.common

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

/**
 * Created by Yehezkiel on 17/05/21
 */
object ProductTrackingCommon {

    fun eventSeeBottomSheetOvo(title: String, productId: String, userId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                "${ProductTrackingConstant.Action.CLICK_SEE_BOTTOMSHEET_OVO} $title",
                ProductTrackingConstant.Label.EMPTY_LABEL)
        mapEvent[ProductTrackingConstant.Tracking.KEY_PRODUCT_ID] = productId
        mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
        mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId != "0").toString()
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun eventTopupBottomSheetOvo(title: String, buttonTitle: String, productId: String, userId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                "${ProductTrackingConstant.Action.CLICK} - $buttonTitle ${ProductTrackingConstant.Action.CLICK_TOPUP_BOTTOMSHEET_OVO} $title",
                ProductTrackingConstant.Label.EMPTY_LABEL)
        addComponentOvoTracker(mapEvent, productId, userId)
    }

    fun addComponentOvoTracker(mapEvent: MutableMap<String, Any>, productId: String, userId: String) {
        mapEvent[ProductTrackingConstant.Tracking.KEY_PRODUCT_ID] = productId
        mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
        mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId != "0").toString()
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }
}