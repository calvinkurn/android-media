package com.tokopedia.product.detail.data.util

import com.tokopedia.product.detail.common.ProductTrackingConstant
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

/**
 * Created by Yehezkiel on 08/12/20
 */
object ProductVideoDetailTracking {

    fun eventClickVideoDetailVolume(productId: String, shopType: String, shopId: String, userId: String, isMute: Boolean) {
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_MUTE_VIDEO,
                ProductTrackingConstant.Label.VIDEO_STATE + isMute.toString())

        mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
        mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()
        mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_TYPE] = shopType
        mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = shopId
        mapEvent[ProductTrackingConstant.Tracking.KEY_PRODUCT_ID] = productId

        mapEvent[ProductTrackingConstant.Tracking.KEY_LAYOUT] = "layout:;catName:;catId:;"
        mapEvent[ProductTrackingConstant.Tracking.KEY_COMPONENT] = ""

        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun eventCLickMinimizeVideo(productId: String, shopType: String, shopId: String, userId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_FULLSCREEN_VIDEO,
                ProductTrackingConstant.Label.VIDEO_STATE + false.toString())

        mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
        mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()
        mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_TYPE] = shopType
        mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = shopId
        mapEvent[ProductTrackingConstant.Tracking.KEY_PRODUCT_ID] = productId

        mapEvent[ProductTrackingConstant.Tracking.KEY_LAYOUT] = "layout:;catName:;catId:;"
        mapEvent[ProductTrackingConstant.Tracking.KEY_COMPONENT] = ""

        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }
}