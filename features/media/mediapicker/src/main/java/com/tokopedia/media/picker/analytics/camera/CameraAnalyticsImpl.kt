@file:Suppress("SameParameterValue")

package com.tokopedia.media.picker.analytics.camera

import com.tokopedia.media.picker.analytics.*
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class CameraAnalyticsImpl @Inject constructor(
    private val userSession: UserSessionInterface
) : CameraAnalytics {

    private val userId: String
        get() = userSession.userId

    private val shopId: String
        get() = userSession.shopId?: ""

    override fun clickRecord(entryPoint: String) {
        sendGeneralEvent(
            event = EVENT_CLICK_COMMUNICATION,
            eventAction = ACTION_CLICK_RECORD,
            eventCategory = CATEGORY_MEDIA_CAMERA,
            eventLabel = "$entryPoint - $userId - $shopId"
        )
    }

    override fun clickShutter(entryPoint: String) {
        sendGeneralEvent(
            event = EVENT_CLICK_COMMUNICATION,
            eventAction = ACTION_CLICK_SHUTTER,
            eventCategory = CATEGORY_MEDIA_CAMERA,
            eventLabel = "$entryPoint - $userId - $shopId",
        )
    }

    override fun clickFlash(entryPoint: String, flashState: String) {
        sendGeneralEvent(
            event = EVENT_CLICK_COMMUNICATION,
            eventAction = ACTION_CLICK_FLASH,
            eventCategory = CATEGORY_MEDIA_CAMERA,
            eventLabel = "$flashState - $entryPoint - $userId - $shopId",
        )
    }

    override fun clickFlip(entryPoint: String, cameraState: String) {
        sendGeneralEvent(
            event = EVENT_CLICK_COMMUNICATION,
            eventAction = ACTION_CLICK_FLIP,
            eventCategory = CATEGORY_MEDIA_CAMERA,
            eventLabel = "$cameraState - $entryPoint - $userId - $shopId",
        )
    }

    override fun clickThumbnail(entryPoint: String) {
        sendGeneralEvent(
            event = EVENT_CLICK_COMMUNICATION,
            eventAction = ACTION_CLICK_THUMBNAIL,
            eventCategory = CATEGORY_MEDIA_CAMERA,
            eventLabel = "$entryPoint - $userId - $shopId",
        )
    }

    override fun clickGalleryTab(entryPoint: String) {
        sendGeneralEvent(
            event = EVENT_CLICK_COMMUNICATION,
            eventAction = ACTION_CLICK_TAB_GALLERY,
            eventCategory = CATEGORY_MEDIA_CAMERA,
            eventLabel = "$entryPoint - $userId - $shopId",
        )
    }

    override fun maxPhotoLimit(entryPoint: String) {
        sendGeneralEvent(
            event = EVENT_VIEW_COMMUNICATION,
            eventAction = ACTION_MAX_PHOTO,
            eventCategory = CATEGORY_MEDIA_CAMERA,
            eventLabel = "$entryPoint - $userId - $shopId",
        )
    }

    override fun maxVideoLimit(entryPoint: String) {
        sendGeneralEvent(
            event = EVENT_VIEW_COMMUNICATION,
            eventAction = ACTION_MAX_VIDEO,
            eventCategory = CATEGORY_MEDIA_CAMERA,
            eventLabel = "$entryPoint - $userId - $shopId",
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