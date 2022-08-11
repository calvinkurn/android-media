package com.tokopedia.product.share.tracker

import com.tokopedia.product.share.ekstensions.ProductShareConstant
import com.tokopedia.product.share.ekstensions.ProductShareConstant.EVENT_ACTION_CLICK_ACCESS_PHOTO_MEDIA_AND_FILES
import com.tokopedia.product.share.ekstensions.ProductShareConstant.EVENT_ACTION_CLICK_CHANNEL_SCREENSHOT_SHARE_BOTTOMSHEET
import com.tokopedia.product.share.ekstensions.ProductShareConstant.EVENT_ACTION_CLICK_CHANNEL_SHARE_BOTTOMSHEET
import com.tokopedia.product.share.ekstensions.ProductShareConstant.EVENT_ACTION_SCREENSHOT_SHARE_BOTTOMSHEET
import com.tokopedia.product.share.ekstensions.ProductShareConstant.EVENT_ACTION_SHARE_BOTTOMSHEET
import com.tokopedia.product.share.ekstensions.ProductShareConstant.EVENT_ACTION_VIEW_SCREENSHOT_SHARE_BOTTOMSHEET
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
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet.Companion.CUSTOM_SHARE_SHEET
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet.Companion.SCREENSHOT_SHARE_SHEET

/**
 * Created by Yehezkiel on 05/08/21
 */
object ProductShareTracking {

    fun onClickChannelWidgetClicked(type: Int, channel: String, userId: String, productId: String,
                                    campaignId: String, bundleId: String) {
        if (type == CUSTOM_SHARE_SHEET) {
            onClickNormalShareChannel(userId, productId, channel, campaignId, bundleId)
        } else {
            onClickScreenshotShareChannel(userId, productId, channel, campaignId, bundleId)
        }
    }

    fun onCloseShareWidgetClicked(
        type: Int, userId: String, productId: String,
        campaignId: String, bundleId: String
    ) {
        if (type == CUSTOM_SHARE_SHEET) {
            onCloseNormalShareClicked(userId, productId, campaignId, bundleId)
        } else {
            onCloseScreenshotShareClicked(userId, productId, campaignId, bundleId)
        }
    }

    fun onImpressShareWidget(type: Int, userId: String, productId: String,
                             campaignId: String, bundleId: String) {
        val eventAction = if (type == SCREENSHOT_SHARE_SHEET)
            EVENT_ACTION_VIEW_SCREENSHOT_SHARE_BOTTOMSHEET
        else EVENT_ACTION_VIEW_SHARE_BOTTOMSHEET

        val mapEvent = TrackAppUtils.gtmData(
            EVENT_VIEW_IRIS_PDP_SHARING,
            EVENT_CATEGORY_PDP_SHARING,
            eventAction,
            UniversalShareBottomSheet.getUserType()+" - "+productId+" - "+campaignId+" - "+bundleId,
        )
        mapEvent.appendDefaultTracker(userId, productId)
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun onClickAccessPhotoMediaAndFiles(userId: String, productId: String, label: String) {
        val mapEvent = TrackAppUtils.gtmData(
            EVENT_CLICK_PDP_SHARING,
            EVENT_CATEGORY_PDP_SHARING,
            EVENT_ACTION_CLICK_ACCESS_PHOTO_MEDIA_AND_FILES,
            label+" - "+productId
        )
        mapEvent.appendDefaultTracker(userId, productId)
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    private fun onClickNormalShareChannel(userId: String, productId: String, channel: String,
                                          campaignId: String, bundleId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                EVENT_CLICK_PDP_SHARING,
                EVENT_CATEGORY_PDP_SHARING,
                EVENT_ACTION_CLICK_CHANNEL_SHARE_BOTTOMSHEET,
                channel+" - "+UniversalShareBottomSheet.getUserType()+" - "+productId+" - "+campaignId+" - "
                        +bundleId+" - "+UniversalShareBottomSheet.Companion.KEY_IMAGE_DEFAULT)
        mapEvent.appendDefaultTracker(userId, productId)
        mapEvent[ProductShareConstant.TRACKER_ID] = ProductShareConstant.TRACKER_ID_CLICK_SHARING_CHANNEL
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    private fun onClickScreenshotShareChannel(userId: String, productId: String, channel: String,
                                              campaignId: String, bundleId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                EVENT_CLICK_PDP_SHARING,
                EVENT_CATEGORY_PDP_SHARING,
                EVENT_ACTION_CLICK_CHANNEL_SCREENSHOT_SHARE_BOTTOMSHEET,
            channel+" - "+UniversalShareBottomSheet.getUserType()+" - "+productId+" - "+campaignId+" - "+bundleId)
        mapEvent.appendDefaultTracker(userId, productId)
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    private fun onCloseNormalShareClicked(userId: String, productId: String,
                                          campaignId: String, bundleId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                EVENT_CLICK_PDP_SHARING,
                EVENT_CATEGORY_PDP_SHARING,
                EVENT_ACTION_SHARE_BOTTOMSHEET,
            UniversalShareBottomSheet.getUserType()+" - "+productId+" - "+campaignId+" - "+bundleId)

        mapEvent.appendDefaultTracker(userId, productId)
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    private fun onCloseScreenshotShareClicked(userId: String, productId: String,
                                              campaignId: String, bundleId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                EVENT_CLICK_PDP_SHARING,
                EVENT_CATEGORY_PDP_SHARING,
                EVENT_ACTION_SCREENSHOT_SHARE_BOTTOMSHEET,
            UniversalShareBottomSheet.getUserType()+" - "+productId+" - "+campaignId+" - "+bundleId)

        mapEvent.appendDefaultTracker(userId, productId)
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    private fun MutableMap<String, Any>.appendDefaultTracker(userId: String, productId: String) {
        this[KEY_BUSINESS_UNIT_SHARING] = VALUE_BUSINESS_UNIT_SHARING
        this[KEY_CURRENT_SITE_SHARING] = VALUE_CURRENT_SITE
        this[KEY_PRODUCT_ID_SHARING] = productId
        this[KEY_USER_ID_SHARING] = userId.ifBlank { 0 }
    }
}