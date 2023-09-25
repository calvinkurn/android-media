package com.tokopedia.editor.analytics.main.editor

import com.tokopedia.editor.analytics.BUSINESS_UNIT
import com.tokopedia.editor.analytics.CURRENT_SITE
import com.tokopedia.editor.analytics.EVENT_ADJUST_TOOL_CLICK
import com.tokopedia.editor.analytics.EVENT_BACK_CLICK
import com.tokopedia.editor.analytics.EVENT_CATEGORY
import com.tokopedia.editor.analytics.EVENT_CLICK_COMMUNICATION
import com.tokopedia.editor.analytics.EVENT_FINISH_CLICK
import com.tokopedia.editor.analytics.EVENT_TEXT_TOOL_CLICK
import com.tokopedia.editor.analytics.FIELD_BUSINESS_UNIT
import com.tokopedia.editor.analytics.FIELD_CURRENT_SITE
import com.tokopedia.editor.analytics.FIELD_EVENT
import com.tokopedia.editor.analytics.FIELD_EVENT_ACTION
import com.tokopedia.editor.analytics.FIELD_EVENT_CATEGORY
import com.tokopedia.editor.analytics.FIELD_EVENT_LABEL
import com.tokopedia.editor.analytics.FIELD_TRACKER_ID
import com.tokopedia.editor.analytics.TRACKER_ID_ADJUST_TOOL_CLICK
import com.tokopedia.editor.analytics.TRACKER_ID_BACK_CLICK
import com.tokopedia.editor.analytics.TRACKER_ID_FINISH_CLICK
import com.tokopedia.editor.analytics.TRACKER_ID_TEXT_TOOL_CLICK
import com.tokopedia.editor.analytics.getMediaTypeString
import com.tokopedia.editor.analytics.toImmersiveTrackerData
import com.tokopedia.editor.ui.main.EditorParamFetcher
import com.tokopedia.track.TrackApp
import javax.inject.Inject

class MainEditorAnalyticsImpl @Inject constructor(
    paramFetcher: EditorParamFetcher
) : MainEditorAnalytics {
    /**
     * {editor_type} with : video/image
     * {page_source} with the upload source: add product/ add story etc
     * {shop_id} with the shop id, fill in with 0 if there were none
     * {unique_id} with the unique identifier, fill in with 0 if the page does not provide
     * {edit_tool} with theediting tool used (detail page where the simpan button is), e.g. teks, crop, trim, etc
     * {edit_variant} with the tool variant used (if any), e.g. for teks {edit_variant} is normal / highlight
     * {active_tools} = the active tools when clicking Lanjut, e.g. teks, mute, crop, etc. Value is numeric in array {1,3,4, ...} where the number is mapped to a specific tools
     */
    private val pageSourceString: String
    private val editorType: String
    private val customTrackerData: Map<String, String>

    init {
        paramFetcher.get().let {
            pageSourceString = it.pageSource.value
            editorType = getMediaTypeString(it.firstFile)
            customTrackerData = it.trackerExtra
        }
    }

    override fun toolTextClick() {
        val eventLabel = "$editorType - $pageSourceString - ${customTrackerData.toImmersiveTrackerData()}"
        sendGeneralEvent(
            eventAction = EVENT_TEXT_TOOL_CLICK,
            eventLabel = eventLabel,
            trackerID = TRACKER_ID_TEXT_TOOL_CLICK,
        )
    }

    override fun toolAdjustCropClick() {
        val eventLabel = "$editorType - $pageSourceString - ${customTrackerData.toImmersiveTrackerData()}"
        sendGeneralEvent(
            eventAction = EVENT_ADJUST_TOOL_CLICK,
            eventLabel = eventLabel,
            trackerID = TRACKER_ID_ADJUST_TOOL_CLICK,
        )
    }

    override fun finishPageClick(
        hasText: Boolean,
        isMute: Boolean,
        isCropped: Boolean
    ) {
        var activeTool = ""
        if (hasText) {
            activeTool += "$TOOL_TEXT,"
        }

        if (isCropped) {
            activeTool += "$TOOL_CROP,"
        }

        if (isMute) {
            activeTool += "$TOOL_MUTE,"
        }

        // remove ',' on last index
        activeTool = if (activeTool.isNotEmpty()) {
            activeTool.dropLast(1)
        } else {
            "$TOOL_EMPTY"
        }

        val eventLabel = "$editorType - $pageSourceString - $activeTool - ${customTrackerData.toImmersiveTrackerData()}"
        sendGeneralEvent(
            eventAction = EVENT_FINISH_CLICK,
            eventLabel = eventLabel,
            trackerID = TRACKER_ID_FINISH_CLICK
        )
    }

    override fun backPageClick() {
        val eventLabel = "$editorType - $pageSourceString - ${customTrackerData.toImmersiveTrackerData()}"
        sendGeneralEvent(
            eventAction = EVENT_BACK_CLICK,
            eventLabel = eventLabel,
            trackerID = TRACKER_ID_BACK_CLICK
        )
    }

    private fun sendGeneralEvent(
        eventAction: String,
        eventLabel: String,
        trackerID: String,
        additionalEvent: Map<String, String> = mapOf()
    ) {
        val generalEvent = mutableMapOf(
            FIELD_EVENT to EVENT_CLICK_COMMUNICATION,
            FIELD_EVENT_ACTION to eventAction,
            FIELD_EVENT_CATEGORY to EVENT_CATEGORY,
            FIELD_EVENT_LABEL to eventLabel,
            FIELD_TRACKER_ID to trackerID,
            FIELD_BUSINESS_UNIT to BUSINESS_UNIT,
            FIELD_CURRENT_SITE to CURRENT_SITE
        )

        if (additionalEvent.isNotEmpty()) {
            generalEvent.putAll(additionalEvent)
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(
            generalEvent.toMap()
        )
    }

    companion object {
        /**
         * DA mapper
         * 0. Empty - no edit
         * 1. Text
         * 2. Crop
         * 3. Mute
         */
        private const val TOOL_TEXT = 1
        private const val TOOL_CROP = 2
        private const val TOOL_MUTE = 3

        private const val TOOL_EMPTY = 0
    }
}
