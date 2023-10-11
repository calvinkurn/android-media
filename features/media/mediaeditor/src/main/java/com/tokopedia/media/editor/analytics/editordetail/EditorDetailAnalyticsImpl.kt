package com.tokopedia.media.editor.analytics.editordetail

import com.tokopedia.kotlin.extensions.view.toZeroStringIfNullOrBlank
import com.tokopedia.media.editor.analytics.ACTION_CLICK_ADD_LOGO
import com.tokopedia.media.editor.analytics.ACTION_VIEW_LOGO_LOAD_RETRY
import com.tokopedia.media.editor.analytics.ACTION_CLICK_SAVE
import com.tokopedia.media.editor.analytics.ACTION_CLICK_TEXT_BACKGROUND
import com.tokopedia.media.editor.analytics.ACTION_CLICK_TEXT_FREE
import com.tokopedia.media.editor.analytics.ACTION_CLICK_TEXT_TEMPLATE
import com.tokopedia.media.editor.analytics.ACTION_ROTATION_FLIP
import com.tokopedia.media.editor.analytics.ACTION_ROTATION_ROTATE
import com.tokopedia.media.editor.analytics.BUSINESS_UNIT
import com.tokopedia.media.editor.analytics.CURRENT_SITE
import com.tokopedia.media.editor.analytics.EVENT
import com.tokopedia.media.editor.analytics.EVENT_CATEGORY
import com.tokopedia.media.editor.analytics.EVENT_VIEW
import com.tokopedia.media.editor.analytics.KEY_BUSINESS_UNIT
import com.tokopedia.media.editor.analytics.KEY_CURRENT_SITE
import com.tokopedia.media.editor.analytics.KEY_EVENT
import com.tokopedia.media.editor.analytics.KEY_EVENT_ACTION
import com.tokopedia.media.editor.analytics.KEY_EVENT_CATEGORY
import com.tokopedia.media.editor.analytics.KEY_EVENT_LABEL
import com.tokopedia.media.editor.analytics.KEY_TRACKER_ID
import com.tokopedia.media.editor.analytics.KEY_USER_ID
import com.tokopedia.media.editor.analytics.TRACKER_ID_VIEW_LOGO_LOAD_RETRY
import com.tokopedia.media.editor.analytics.TRACKER_ID_CLICK_LOGO_UPLOAD
import com.tokopedia.media.editor.analytics.TRACKER_ID_CLICK_SAVE
import com.tokopedia.media.editor.analytics.TRACKER_ID_CLICK_TEXT_BACKGROUND
import com.tokopedia.media.editor.analytics.TRACKER_ID_CLICK_TEXT_FREE
import com.tokopedia.media.editor.analytics.TRACKER_ID_CLICK_TEXT_TEMPLATE
import com.tokopedia.media.editor.analytics.TRACKER_ID_ROTATION_FLIP
import com.tokopedia.media.editor.analytics.TRACKER_ID_ROTATION_ROTATE
import com.tokopedia.picker.common.cache.PickerCacheManager
import com.tokopedia.picker.common.types.EditorToolType
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class EditorDetailAnalyticsImpl @Inject constructor(
    private val userSession: UserSessionInterface,
    private val cacheManager: PickerCacheManager
) : EditorDetailAnalytics {
    private val userId: String
        get() = userSession.userId.toZeroStringIfNullOrBlank()

    private val shopId: String
        get() = userSession.shopId.toZeroStringIfNullOrBlank()

    private val pageSource: String
        get() = cacheManager.get().pageSourceName()

    override fun clickRotationRotate() {
        sendGeneralEvent(
            ACTION_ROTATION_ROTATE,
            "$pageSource - $userId - $shopId",
            TRACKER_ID_ROTATION_ROTATE
        )
    }

    override fun clickRotationFlip() {
        sendGeneralEvent(
            ACTION_ROTATION_FLIP,
            "$pageSource - $userId - $shopId",
            TRACKER_ID_ROTATION_FLIP
        )
    }

    override fun clickAddLogoUpload(logoState: String) {
        sendGeneralEvent(
            ACTION_CLICK_ADD_LOGO,
            "$pageSource - $userId - $shopId - ${logoState.lowercase()}",
            TRACKER_ID_CLICK_LOGO_UPLOAD
        )
    }

    // add logo failed to load shop logo
    override fun viewAddLogoLoadRetry() {
        sendGeneralEvent(
            ACTION_VIEW_LOGO_LOAD_RETRY,
            "$pageSource - $userId - $shopId",
            TRACKER_ID_VIEW_LOGO_LOAD_RETRY,
            eventName = EVENT_VIEW
        )
    }

    override fun clickAddTextFreeText() {
        sendGeneralEvent(
            ACTION_CLICK_TEXT_FREE,
            "$pageSource - $userId - $shopId",
            TRACKER_ID_CLICK_TEXT_FREE
        )
    }

    override fun clickAddTextBackgroundText() {
        sendGeneralEvent(
            ACTION_CLICK_TEXT_BACKGROUND,
            "$pageSource - $userId - $shopId",
            TRACKER_ID_CLICK_TEXT_BACKGROUND
        )
    }

    override fun clickAddTextTemplate() {
        sendGeneralEvent(
            ACTION_CLICK_TEXT_TEMPLATE,
            "$pageSource - $userId - $shopId",
            TRACKER_ID_CLICK_TEXT_TEMPLATE
        )
    }

    override fun clickSave(
        editorToolType: Int,
        editorText: String,
        brightnessValue: Int,
        contrastValue: Int,
        cropText: String,
        rotateValue: Int,
        watermarkText: String,
        removeBackgroundText: String,
        addLogoValue: String,
        addTextValue: String
    ) {
        val toolDetail = when(editorToolType) {
            EditorToolType.BRIGHTNESS -> "{$brightnessValue}"
            EditorToolType.CONTRAST -> "{$contrastValue}"
            EditorToolType.ROTATE -> "{$rotateValue}"
            EditorToolType.CROP -> cropText
            EditorToolType.REMOVE_BACKGROUND -> removeBackgroundText
            EditorToolType.WATERMARK -> watermarkText
            EditorToolType.ADD_LOGO -> addLogoValue
            EditorToolType.ADD_TEXT -> addTextValue
            else -> ""
        }

        sendGeneralEvent(
            ACTION_CLICK_SAVE,
            "$pageSource - $userId - $shopId - $editorText - $toolDetail",
            TRACKER_ID_CLICK_SAVE
        )
    }

    private fun sendGeneralEvent(
        eventAction: String,
        eventLabel: String,
        trackerID: String,
        additionalEvent: Map<String, String> = mapOf(),
        eventName: String? = null
    ) {
        val generalEvent = mutableMapOf(
            KEY_EVENT to (eventName ?: EVENT),
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
