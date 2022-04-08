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
import com.tokopedia.picker.common.ParamCacheManager
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class GalleryAnalyticsImpl @Inject constructor(
    private val userSession: UserSessionInterface,
    private val paramCacheManager: ParamCacheManager
) : GalleryAnalytics {

    private val userId: String
        get() = if (userSession.userId.isNullOrEmpty()) "0" else userSession.userId

    private val shopId: String
        get() = if (userSession.shopId.isNullOrEmpty()) "0" else userSession.shopId

    private val sourcePage: String
        get() = paramCacheManager.get().pageSourceName()

    override fun selectGalleryItem() {
        sendGeneralEvent(
            event = EVENT_CLICK_COMMUNICATION,
            eventAction = ACTION_CLICK_MEDIA,
            eventCategory = CATEGORY_MEDIA_GALLERY,
            eventLabel = "$sourcePage - $userId - $shopId",
        )
    }

    override fun clickNextButton() {
        sendGeneralEvent(
            event = EVENT_CLICK_COMMUNICATION,
            eventAction = ACTION_CLICK_NEXT,
            eventCategory = CATEGORY_MEDIA_GALLERY,
            eventLabel = "$sourcePage - $userId - $shopId",
        )
    }

    override fun clickCloseButton() {
        sendGeneralEvent(
            event = EVENT_CLICK_COMMUNICATION,
            eventAction = ACTION_CLICK_CLOSE,
            eventCategory = CATEGORY_MEDIA_GALLERY,
            eventLabel = "$sourcePage - $userId - $shopId",
        )
    }

    override fun clickDropDown() {
        sendGeneralEvent(
            event = EVENT_CLICK_COMMUNICATION,
            eventAction = ACTION_CLICK_DROPDOWN,
            eventCategory = CATEGORY_MEDIA_GALLERY,
            eventLabel = "$sourcePage - $userId - $shopId",
        )
    }

    override fun clickGalleryThumbnail() {
        sendGeneralEvent(
            event = EVENT_CLICK_COMMUNICATION,
            eventAction = ACTION_CLICK_THUMBNAIL,
            eventCategory = CATEGORY_MEDIA_GALLERY,
            eventLabel = "$sourcePage - $userId - $shopId",
        )
    }

    override fun clickCameraTab() {
        sendGeneralEvent(
            event = EVENT_CLICK_COMMUNICATION,
            eventAction = ACTION_CLICK_TAB_CAMERA,
            eventCategory = CATEGORY_MEDIA_GALLERY,
            eventLabel = "$sourcePage - $userId - $shopId",
        )
    }

    override fun galleryMaxPhotoLimit() {
        sendGeneralEvent(
            event = EVENT_VIEW_COMMUNICATION,
            eventAction = ACTION_MAX_IMAGE,
            eventCategory = CATEGORY_MEDIA_GALLERY,
            eventLabel = "$sourcePage - $userId - $shopId",
        )
    }

    override fun galleryMaxVideoLimit() {
        sendGeneralEvent(
            event = EVENT_VIEW_COMMUNICATION,
            eventAction = ACTION_MAX_VIDEO,
            eventCategory = CATEGORY_MEDIA_GALLERY,
            eventLabel = "$sourcePage - $userId - $shopId",
        )
    }

    override fun maxVideoDuration() {
        sendGeneralEvent(
            event = EVENT_VIEW_COMMUNICATION,
            eventAction = ACTION_MAX_VIDEO_DURATION,
            eventCategory = CATEGORY_MEDIA_GALLERY,
            eventLabel = "$sourcePage - $userId - $shopId",
        )
    }

    override fun maxImageSize() {
        sendGeneralEvent(
            event = EVENT_VIEW_COMMUNICATION,
            eventAction = ACTION_MAX_IMAGE_SIZE,
            eventCategory = CATEGORY_MEDIA_GALLERY,
            eventLabel = "$sourcePage - $userId - $shopId",
        )
    }

    override fun maxVideoSize() {
        sendGeneralEvent(
            event = EVENT_VIEW_COMMUNICATION,
            eventAction = ACTION_MAX_VIDEO_SIZE,
            eventCategory = CATEGORY_MEDIA_GALLERY,
            eventLabel = "$sourcePage - $userId - $shopId",
        )
    }

    override fun clickAlbumFolder(albumName: String) {
        sendGeneralEvent(
            event = EVENT_CLICK_COMMUNICATION,
            eventAction = ACTION_ALBUM,
            eventCategory = CATEGORY_MEDIA_GALLERY,
            eventLabel = "$albumName - $sourcePage - $userId - $shopId",
        )
    }

    override fun minVideoDuration() {
        sendGeneralEvent(
            event = EVENT_VIEW_COMMUNICATION,
            eventAction = ACTION_MIN_DURATION,
            eventCategory = CATEGORY_MEDIA_GALLERY,
            eventLabel = "$sourcePage - $userId - $shopId",
        )
    }

    override fun minImageResolution() {
        sendGeneralEvent(
            event = EVENT_VIEW_COMMUNICATION,
            eventAction = ACTION_MIN_RESOLUTION,
            eventCategory = CATEGORY_MEDIA_GALLERY,
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