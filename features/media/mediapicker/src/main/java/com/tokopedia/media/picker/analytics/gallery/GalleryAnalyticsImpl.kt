package com.tokopedia.media.picker.analytics.gallery

import com.tokopedia.media.picker.analytics.ACTION_ALBUM
import com.tokopedia.media.picker.analytics.ACTION_CLICK_CLOSE
import com.tokopedia.media.picker.analytics.ACTION_CLICK_DROPDOWN
import com.tokopedia.media.picker.analytics.ACTION_CLICK_MEDIA
import com.tokopedia.media.picker.analytics.ACTION_CLICK_NEXT
import com.tokopedia.media.picker.analytics.ACTION_CLICK_TAB_CAMERA
import com.tokopedia.media.picker.analytics.ACTION_CLICK_THUMBNAIL
import com.tokopedia.media.picker.analytics.ACTION_MAX_IMAGE
import com.tokopedia.media.picker.analytics.ACTION_MAX_IMAGE_SIZE
import com.tokopedia.media.picker.analytics.ACTION_MAX_VIDEO
import com.tokopedia.media.picker.analytics.ACTION_MAX_VIDEO_DURATION
import com.tokopedia.media.picker.analytics.ACTION_MAX_VIDEO_SIZE
import com.tokopedia.media.picker.analytics.ACTION_MIN_DURATION
import com.tokopedia.media.picker.analytics.ACTION_MIN_RESOLUTION
import com.tokopedia.media.picker.analytics.BUSINESS_UNIT
import com.tokopedia.media.picker.analytics.CATEGORY_MEDIA_GALLERY
import com.tokopedia.media.picker.analytics.CURRENT_SITE
import com.tokopedia.media.picker.analytics.EVENT_CLICK_COMMUNICATION
import com.tokopedia.media.picker.analytics.EVENT_VIEW_COMMUNICATION
import com.tokopedia.media.picker.analytics.KEY_BUSINESS_UNIT
import com.tokopedia.media.picker.analytics.KEY_CURRENT_SITE
import com.tokopedia.media.picker.analytics.KEY_EVENT
import com.tokopedia.media.picker.analytics.KEY_EVENT_ACTION
import com.tokopedia.media.picker.analytics.KEY_EVENT_CATEGORY
import com.tokopedia.media.picker.analytics.KEY_EVENT_LABEL
import com.tokopedia.media.picker.analytics.KEY_USER_ID
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class GalleryAnalyticsImpl @Inject constructor(
    private val userSession: UserSessionInterface
) : GalleryAnalytics {

    private val userId: String
        get() = userSession.userId

    private val shopId: String
        get() = userSession.shopId?: ""

    override fun selectGalleryItem(entryPoint: String) {
        sendGeneralEvent(
            event = EVENT_CLICK_COMMUNICATION,
            eventAction = ACTION_CLICK_MEDIA,
            eventCategory = CATEGORY_MEDIA_GALLERY,
            eventLabel = "$entryPoint - $userId - $shopId",
        )
    }

    override fun clickNextButton(entryPoint: String) {
        sendGeneralEvent(
            event = EVENT_CLICK_COMMUNICATION,
            eventAction = ACTION_CLICK_NEXT,
            eventCategory = CATEGORY_MEDIA_GALLERY,
            eventLabel = "$entryPoint - $userId - $shopId",
        )
    }

    override fun clickCloseButton(entryPoint: String) {
        sendGeneralEvent(
            event = EVENT_CLICK_COMMUNICATION,
            eventAction = ACTION_CLICK_CLOSE,
            eventCategory = CATEGORY_MEDIA_GALLERY,
            eventLabel = "$entryPoint - $userId - $shopId",
        )
    }

    override fun clickDropDown(entryPoint: String) {
        sendGeneralEvent(
            event = EVENT_CLICK_COMMUNICATION,
            eventAction = ACTION_CLICK_DROPDOWN,
            eventCategory = CATEGORY_MEDIA_GALLERY,
            eventLabel = "$entryPoint - $userId - $shopId",
        )
    }

    override fun clickGalleryThumbnail(entryPoint: String) {
        sendGeneralEvent(
            event = EVENT_CLICK_COMMUNICATION,
            eventAction = ACTION_CLICK_THUMBNAIL,
            eventCategory = CATEGORY_MEDIA_GALLERY,
            eventLabel = "$entryPoint - $userId - $shopId",
        )
    }

    override fun clickCameraTab(entryPoint: String) {
        sendGeneralEvent(
            event = EVENT_CLICK_COMMUNICATION,
            eventAction = ACTION_CLICK_TAB_CAMERA,
            eventCategory = CATEGORY_MEDIA_GALLERY,
            eventLabel = "$entryPoint - $userId - $shopId",
        )
    }

    override fun galleryMaxPhotoLimit(entryPoint: String) {
        sendGeneralEvent(
            event = EVENT_VIEW_COMMUNICATION,
            eventAction = ACTION_MAX_IMAGE,
            eventCategory = CATEGORY_MEDIA_GALLERY,
            eventLabel = "$entryPoint - $userId - $shopId",
        )
    }

    override fun galleryMaxVideoLimit(entryPoint: String) {
        sendGeneralEvent(
            event = EVENT_VIEW_COMMUNICATION,
            eventAction = ACTION_MAX_VIDEO,
            eventCategory = CATEGORY_MEDIA_GALLERY,
            eventLabel = "$entryPoint - $userId - $shopId",
        )
    }

    override fun maxVideoDuration(entryPoint: String) {
        sendGeneralEvent(
            event = EVENT_VIEW_COMMUNICATION,
            eventAction = ACTION_MAX_VIDEO_DURATION,
            eventCategory = CATEGORY_MEDIA_GALLERY,
            eventLabel = "$entryPoint - $userId - $shopId",
        )
    }

    override fun maxImageSize(entryPoint: String) {
        sendGeneralEvent(
            event = EVENT_VIEW_COMMUNICATION,
            eventAction = ACTION_MAX_IMAGE_SIZE,
            eventCategory = CATEGORY_MEDIA_GALLERY,
            eventLabel = "$entryPoint - $userId - $shopId",
        )
    }

    override fun maxVideoSize(entryPoint: String) {
        sendGeneralEvent(
            event = EVENT_VIEW_COMMUNICATION,
            eventAction = ACTION_MAX_VIDEO_SIZE,
            eventCategory = CATEGORY_MEDIA_GALLERY,
            eventLabel = "$entryPoint - $userId - $shopId",
        )
    }

    override fun clickAlbumFolder(entryPoint: String, albumName: String) {
        sendGeneralEvent(
            event = EVENT_CLICK_COMMUNICATION,
            eventAction = ACTION_ALBUM,
            eventCategory = CATEGORY_MEDIA_GALLERY,
            eventLabel = "$albumName - $entryPoint - $userId - $shopId",
        )
    }

    override fun minVideoDuration(entryPoint: String) {
        sendGeneralEvent(
            event = EVENT_VIEW_COMMUNICATION,
            eventAction = ACTION_MIN_DURATION,
            eventCategory = CATEGORY_MEDIA_GALLERY,
            eventLabel = "$entryPoint - $userId - $shopId",
        )
    }

    override fun minImageResolution(entryPoint: String) {
        sendGeneralEvent(
            event = EVENT_VIEW_COMMUNICATION,
            eventAction = ACTION_MIN_RESOLUTION,
            eventCategory = CATEGORY_MEDIA_GALLERY,
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