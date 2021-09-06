package com.tokopedia.imagepicker.editor.analytics

import com.tokopedia.track.TrackApp
import java.util.HashMap

object ImageEditorTracking {
    val tracker = TrackApp.getInstance().gtm

    @JvmStatic
    fun onSaveEditImage(label: String, pageType: String) {
        tracker.sendGeneralEvent(createEventMap(ImageEditorTrackingConstant.EVENT,
            ImageEditorTrackingConstant.EVENT_CATEGORY, ImageEditorTrackingConstant.EVENT_ACTION,
            label, pageType))
    }

    private fun createEventMap(event: String, category: String, action: String,
                               label: String, pageType: String): HashMap<String, Any> {
        val eventMap = HashMap<String, Any>()
        eventMap[ImageEditorTrackingConstant.EVENT_KEY] = event
        eventMap[ImageEditorTrackingConstant.CATEGORY_KEY] = category
        eventMap[ImageEditorTrackingConstant.ACTION_KEY] = action
        eventMap[ImageEditorTrackingConstant.LABEL_KEY] = label
        eventMap[ImageEditorTrackingConstant.BUSINESS_UNIT_KEY] =
                ImageEditorTrackingConstant.BUSINESS_UNIT
        eventMap[ImageEditorTrackingConstant.CURRENT_SITE_KEY] =
                ImageEditorTrackingConstant.CURRENT_SITE
        eventMap[ImageEditorTrackingConstant.PAGE_TYPE_KEY] = pageType
        return eventMap
    }
}