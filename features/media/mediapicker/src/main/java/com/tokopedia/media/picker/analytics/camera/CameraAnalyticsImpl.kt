@file:Suppress("SameParameterValue")

package com.tokopedia.media.picker.analytics.camera

import com.tokopedia.kotlin.extensions.view.toZeroStringIfNullOrBlank
import com.tokopedia.media.picker.analytics.*
import com.tokopedia.picker.common.cache.PickerCacheManager
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class CameraAnalyticsImpl @Inject constructor(
    private val userSession: UserSessionInterface,
    private val cacheManager: PickerCacheManager
) : CameraAnalytics {

    private val userId: String
        get() = userSession.userId.toZeroStringIfNullOrBlank()

    private val shopId: String
        get() = userSession.shopId.toZeroStringIfNullOrBlank()

    private val sourcePage by lazy {
        cacheManager.get().pageSourceName()
    }

    override fun clickRecord() {
        sendGeneralEvent(
            event = EVENT_CLICK_COMMUNICATION,
            eventAction = ACTION_CLICK_RECORD,
            eventCategory = CATEGORY_MEDIA_CAMERA,
            eventLabel = "$sourcePage - $userId - $shopId"
        )
    }

    override fun clickShutter() {
        sendGeneralEvent(
            event = EVENT_CLICK_COMMUNICATION,
            eventAction = ACTION_CLICK_SHUTTER,
            eventCategory = CATEGORY_MEDIA_CAMERA,
            eventLabel = "$sourcePage - $userId - $shopId",
        )
    }

    override fun clickFlash(flashState: String) {
        sendGeneralEvent(
            event = EVENT_CLICK_COMMUNICATION,
            eventAction = ACTION_CLICK_FLASH,
            eventCategory = CATEGORY_MEDIA_CAMERA,
            eventLabel = "$flashState - $sourcePage - $userId - $shopId",
        )
    }

    override fun clickFlip(cameraState: String) {
        sendGeneralEvent(
            event = EVENT_CLICK_COMMUNICATION,
            eventAction = ACTION_CLICK_FLIP,
            eventCategory = CATEGORY_MEDIA_CAMERA,
            eventLabel = "$cameraState - $sourcePage - $userId - $shopId",
        )
    }

    override fun clickThumbnail() {
        sendGeneralEvent(
            event = EVENT_CLICK_COMMUNICATION,
            eventAction = ACTION_CLICK_THUMBNAIL,
            eventCategory = CATEGORY_MEDIA_CAMERA,
            eventLabel = "$sourcePage - $userId - $shopId",
        )
    }

    override fun clickGalleryTab() {
        sendGeneralEvent(
            event = EVENT_CLICK_COMMUNICATION,
            eventAction = ACTION_CLICK_TAB_GALLERY,
            eventCategory = CATEGORY_MEDIA_CAMERA,
            eventLabel = "$sourcePage - $userId - $shopId",
        )
    }

    override fun maxPhotoLimit() {
        sendGeneralEvent(
            event = EVENT_VIEW_COMMUNICATION,
            eventAction = ACTION_MAX_PHOTO,
            eventCategory = CATEGORY_MEDIA_CAMERA,
            eventLabel = "$sourcePage - $userId - $shopId",
        )
    }

    override fun maxVideoLimit() {
        sendGeneralEvent(
            event = EVENT_VIEW_COMMUNICATION,
            eventAction = ACTION_MAX_VIDEO,
            eventCategory = CATEGORY_MEDIA_CAMERA,
            eventLabel = "$sourcePage - $userId - $shopId",
        )
    }

    override fun recordLowStorage() {
        sendGeneralEvent(
            event = EVENT_VIEW_COMMUNICATION,
            eventAction = ACTION_LOW_STORAGE,
            eventCategory = CATEGORY_MEDIA_CAMERA,
            eventLabel = "$sourcePage - $userId - $shopId",
        )
    }

    private fun sendGeneralEvent(
        event: String,
        eventCategory: String,
        eventAction: String,
        eventLabel: String,
        additionalEvent: Map<String, String> = mapOf()
    ) {
        val generalEvent = mutableMapOf(
            KEY_EVENT to event,
            KEY_EVENT_CATEGORY to eventCategory,
            KEY_EVENT_ACTION to eventAction,
            KEY_EVENT_LABEL to eventLabel,
            KEY_BUSINESS_UNIT to BUSINESS_UNIT,
            KEY_CURRENT_SITE to CURRENT_SITE,
            KEY_USER_ID to userId,
        )

        if (additionalEvent.isNotEmpty()) {
            generalEvent.putAll(additionalEvent)
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(
            generalEvent.toMap()
        )
    }

}
