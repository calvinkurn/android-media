package com.tokopedia.media.editor.analytics.editordetail

import com.tokopedia.kotlin.extensions.view.toZeroStringIfNullOrBlank
import com.tokopedia.media.editor.analytics.ACTION_CLICK_SAVE
import com.tokopedia.media.editor.analytics.ACTION_ROTATION_FLIP
import com.tokopedia.media.editor.analytics.ACTION_ROTATION_ROTATE
import com.tokopedia.media.editor.analytics.BUSINESS_UNIT
import com.tokopedia.media.editor.analytics.CURRENT_SITE
import com.tokopedia.media.editor.analytics.EVENT
import com.tokopedia.media.editor.analytics.EVENT_CATEGORY
import com.tokopedia.media.editor.analytics.KEY_BUSINESS_UNIT
import com.tokopedia.media.editor.analytics.KEY_CURRENT_SITE
import com.tokopedia.media.editor.analytics.KEY_EVENT
import com.tokopedia.media.editor.analytics.KEY_EVENT_ACTION
import com.tokopedia.media.editor.analytics.KEY_EVENT_CATEGORY
import com.tokopedia.media.editor.analytics.KEY_EVENT_LABEL
import com.tokopedia.media.editor.analytics.KEY_TRACKER_ID
import com.tokopedia.media.editor.analytics.KEY_USER_ID
import com.tokopedia.media.editor.analytics.TRACKER_ID_CLICK_SAVE
import com.tokopedia.media.editor.analytics.TRACKER_ID_ROTATION_FLIP
import com.tokopedia.media.editor.analytics.TRACKER_ID_ROTATION_ROTATE
import com.tokopedia.media.editor.data.repository.WatermarkType
import com.tokopedia.media.editor.ui.component.RemoveBackgroundToolUiComponent
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.media.editor.utils.ParamCacheManager
import com.tokopedia.media.editor.utils.getToolEditorText
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface

class EditorDetailAnalyticsImpl(
    private val userSession: UserSessionInterface,
    private val cacheManager: ParamCacheManager
) : EditorDetailAnalytics {
    private val userId: String
        get() = userSession.userId.toZeroStringIfNullOrBlank()

    private val shopId: String
        get() = userSession.shopId.toZeroStringIfNullOrBlank()

    private val pageSource: String
        get() = cacheManager.getPickerParam().pageSourceName()

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

    override fun clickSave(
        editorText: String,
        brightnessValue: Int,
        contrastValue: Int,
        cropText: String,
        rotateValue: Int,
        watermarkText: String,
        removeBackgroundText: String
    ) {

        val historyList = "{$brightnessValue}, " +
                "{$contrastValue}, " +
                "{$rotateValue}, " +
                "{$cropText}, " +
                "{$removeBackgroundText}, " +
                "{$watermarkText}"

        sendGeneralEvent(
            ACTION_CLICK_SAVE,
            "$pageSource - $userId - $shopId - $editorText - $historyList",
            TRACKER_ID_CLICK_SAVE
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