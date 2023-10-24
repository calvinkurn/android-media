package com.tokopedia.editor.analytics.image.placement

import com.tokopedia.editor.analytics.BUSINESS_UNIT
import com.tokopedia.editor.analytics.CURRENT_SITE
import com.tokopedia.editor.analytics.EVENT_CATEGORY
import com.tokopedia.editor.analytics.EVENT_CLICK_COMMUNICATION
import com.tokopedia.editor.analytics.EVENT_SAVE_ON_DETAIL
import com.tokopedia.editor.analytics.FIELD_BUSINESS_UNIT
import com.tokopedia.editor.analytics.FIELD_CURRENT_SITE
import com.tokopedia.editor.analytics.FIELD_EVENT
import com.tokopedia.editor.analytics.FIELD_EVENT_ACTION
import com.tokopedia.editor.analytics.FIELD_EVENT_CATEGORY
import com.tokopedia.editor.analytics.FIELD_EVENT_LABEL
import com.tokopedia.editor.analytics.FIELD_TRACKER_ID
import com.tokopedia.editor.analytics.TRACKER_ID_SAVE_ON_DETAIL
import com.tokopedia.editor.analytics.getMediaTypeString
import com.tokopedia.editor.analytics.toImmersiveTrackerData
import com.tokopedia.editor.ui.main.EditorParamFetcher
import com.tokopedia.track.TrackApp
import javax.inject.Inject

class ImagePlacementAnalyticsImpl @Inject constructor(
    private val paramFetcher: EditorParamFetcher
) : ImagePlacementAnalytics {
    private var pageSourceString: String = ""
    private var editorType: String = ""
    private var customTrackerData: Map<String, String> = mapOf()

    override fun editSaveClick() {
        getParamValue()

        // editVariant is filled with 0 since we didn't sent crop detail on tracker
        val eventLabel = "$editorType - $pageSourceString - $EDITOR_TOOL_NAME - 0 - ${customTrackerData.toImmersiveTrackerData()}"
        sendGeneralEvent(
            eventAction = EVENT_SAVE_ON_DETAIL,
            eventLabel = eventLabel,
            trackerID = TRACKER_ID_SAVE_ON_DETAIL
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

    private fun getParamValue() {
        paramFetcher.get().let {
            pageSourceString = it.pageSource.value
            editorType = getMediaTypeString(it.firstFile)
            customTrackerData = it.trackerExtra
        }
    }

    companion object {
        private const val EDITOR_TOOL_NAME = "crop"
    }
}
