package com.tokopedia.media.editor.analytics.editorhome

import com.tokopedia.kotlin.extensions.view.toZeroStringIfNullOrBlank
import com.tokopedia.media.editor.analytics.*
import com.tokopedia.picker.common.cache.PickerCacheManager
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class EditorHomeAnalyticsImpl @Inject constructor(
    private val userSession: UserSessionInterface,
    private val cacheManager: PickerCacheManager
) : EditorHomeAnalytics {
    private val userId: String
        get() = userSession.userId.toZeroStringIfNullOrBlank()

    private val shopId: String
        get() = userSession.shopId.toZeroStringIfNullOrBlank()

    private val pageSource: String
        get() = cacheManager.get().pageSourceName()

    override fun clickUpload() {
        sendGeneralEvent(
            ACTION_CLICK_UPLOAD,
            "$pageSource - $userId - $shopId",
            TRACKER_ID_CLICK_UPLOAD
        )
    }

    override fun clickBackButton() {
        sendGeneralEvent(
            ACTION_CLICK_BACK,
            "$pageSource - $userId - $shopId",
            TRACKER_ID_CLICK_BACK
        )
    }

    override fun clickBrightness() {
        sendGeneralEvent(
            ACTION_CLICK_BRIGHTNESS,
            "$pageSource - $userId - $shopId",
            TRACKER_ID_CLICK_BRIGHTNESS
        )
    }

    override fun clickContrast() {
        sendGeneralEvent(
            ACTION_CLICK_CONTRAST,
            "$pageSource - $userId - $shopId",
            TRACKER_ID_CLICK_CONTRAST
        )
    }

    override fun clickCrop() {
        sendGeneralEvent(
            ACTION_CLICK_CROP,
            "$pageSource - $userId - $shopId",
            TRACKER_ID_CLICK_CROP
        )
    }

    override fun clickRotate() {
        sendGeneralEvent(
            ACTION_CLICK_ROTATE,
            "$pageSource - $userId - $shopId",
            TRACKER_ID_CLICK_ROTATE
        )
    }

    override fun clickRemoveBackground() {
        sendGeneralEvent(
            ACTION_CLICK_REMOVE_BACKGROUND,
            "$pageSource - $userId - $shopId",
            TRACKER_ID_CLICK_REMOVE_BACKGROUND
        )
    }

    override fun clickWatermark() {
        sendGeneralEvent(
            ACTION_CLICK_WATERMARK,
            "$pageSource - $userId - $shopId",
            TRACKER_ID_CLICK_WATERMARK
        )
    }

    override fun clickAddLogo() {
        sendGeneralEvent(
            ACTION_CLICK_ADD_LOGO,
            "$pageSource - $userId - $shopId",
            TRACKER_ID_CLICK_ADD_LOGO
        )
    }

    override fun autoCropProcessTime(loadTime: Long, fileNumber: Int) {
        sendGeneralEvent(
            ACTION_AUTO_CROP_TIME,
            "$pageSource - $userId - $shopId - $loadTime - $fileNumber",
            TRACKER_ID_AUTO_CROP_TIME
        )
    }

    private fun sendGeneralEvent(
        eventAction: String,
        eventLabel: String,
        trackerID: String,
        additionalEvent: Map<String, String> = mapOf()
    ) {
        val generalEvent = mutableMapOf(
            KEY_EVENT to EVENT,
            KEY_EVENT_ACTION to eventAction,
            KEY_EVENT_CATEGORY to EVENT_CATEGORY,
            KEY_EVENT_LABEL to eventLabel,
            KEY_TRACKER_ID to trackerID,
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
