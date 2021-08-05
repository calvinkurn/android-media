package com.tokopedia.product.share.tracker

import com.tokopedia.product.share.ekstensions.ProductShareConstant.EVENT_ACTION_CLICK_CHANNEL_SCREENSHOT_SHARE_BOTTOMSHEET
import com.tokopedia.product.share.ekstensions.ProductShareConstant.EVENT_ACTION_CLICK_CHANNEL_SHARE_BOTTOMSHEET
import com.tokopedia.product.share.ekstensions.ProductShareConstant.EVENT_ACTION_SCREENSHOT_SHARE_BOTTOMSHEET
import com.tokopedia.product.share.ekstensions.ProductShareConstant.EVENT_ACTION_SHARE_BOTTOMSHEET
import com.tokopedia.product.share.ekstensions.ProductShareConstant.EVENT_ACTION_VIEW_SHARE_BOTTOMSHEET
import com.tokopedia.product.share.ekstensions.ProductShareConstant.EVENT_CATEGORY_PDP_SHARING
import com.tokopedia.product.share.ekstensions.ProductShareConstant.EVENT_CLICK_PDP_SHARING
import com.tokopedia.product.share.ekstensions.ProductShareConstant.EVENT_VIEW_IRIS_PDP_SHARING
import com.tokopedia.product.share.ekstensions.ProductShareConstant.KEY_BUSINESS_UNIT_SHARING
import com.tokopedia.product.share.ekstensions.ProductShareConstant.KEY_CURRENT_SITE_SHARING
import com.tokopedia.product.share.ekstensions.ProductShareConstant.KEY_PRODUCT_ID_SHARING
import com.tokopedia.product.share.ekstensions.ProductShareConstant.KEY_USER_ID_SHARING
import com.tokopedia.product.share.ekstensions.ProductShareConstant.VALUE_BUSINESS_UNIT_SHARING
import com.tokopedia.product.share.ekstensions.ProductShareConstant.VALUE_CURRENT_SITE
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

/**
 * Created by Yehezkiel on 05/08/21
 */
object ProductShareTracking {

    fun onClickChannelWidgetClicked(type: Int, channel: String, userId: String, productId: String) {
        if (type == 1) {
            onClickNormalShareChannel(userId, productId, channel)
        } else {
            onClickScreenshotShareChannel(userId, productId, channel)
        }
    }

    fun onCloseShareWidgetClicked(type: Int, userId: String, productId: String) {
        if (type == 1) {
            onCloseNormalShareClicked(userId, productId)
        } else {
            onCloseScreenshotShareClicked(userId, productId)
        }
    }

    fun onImpressShareWidget(userId: String, productId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                EVENT_VIEW_IRIS_PDP_SHARING,
                EVENT_CATEGORY_PDP_SHARING,
                EVENT_ACTION_VIEW_SHARE_BOTTOMSHEET,
                "")
        mapEvent.appendDefaultTracker(userId, productId)
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    private fun onClickNormalShareChannel(userId: String, productId: String, channel: String) {
        val mapEvent = TrackAppUtils.gtmData(
                EVENT_CLICK_PDP_SHARING,
                EVENT_CATEGORY_PDP_SHARING,
                EVENT_ACTION_CLICK_CHANNEL_SHARE_BOTTOMSHEET,
                channel)
        mapEvent.appendDefaultTracker(userId, productId)
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    private fun onClickScreenshotShareChannel(userId: String, productId: String, channel: String) {
        val mapEvent = TrackAppUtils.gtmData(
                EVENT_CLICK_PDP_SHARING,
                EVENT_CATEGORY_PDP_SHARING,
                EVENT_ACTION_CLICK_CHANNEL_SCREENSHOT_SHARE_BOTTOMSHEET,
                channel)
        mapEvent.appendDefaultTracker(userId, productId)
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    private fun onCloseNormalShareClicked(userId: String, productId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                EVENT_CLICK_PDP_SHARING,
                EVENT_CATEGORY_PDP_SHARING,
                EVENT_ACTION_SHARE_BOTTOMSHEET,
                "")

        mapEvent.appendDefaultTracker(userId, productId)
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    private fun onCloseScreenshotShareClicked(userId: String, productId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                EVENT_CLICK_PDP_SHARING,
                EVENT_CATEGORY_PDP_SHARING,
                EVENT_ACTION_SCREENSHOT_SHARE_BOTTOMSHEET,
                "")

        mapEvent.appendDefaultTracker(userId, productId)
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    private fun MutableMap<String, Any>.appendDefaultTracker(userId: String, productId: String) {
        this[KEY_BUSINESS_UNIT_SHARING] = VALUE_BUSINESS_UNIT_SHARING
        this[KEY_CURRENT_SITE_SHARING] = VALUE_CURRENT_SITE
        this[KEY_PRODUCT_ID_SHARING] = productId
        this[KEY_USER_ID_SHARING] = userId
    }
}