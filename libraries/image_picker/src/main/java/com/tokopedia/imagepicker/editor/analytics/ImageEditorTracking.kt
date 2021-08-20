package com.tokopedia.imagepicker.editor.analytics

import com.tokopedia.track.TrackApp
import java.util.HashMap

object ImageEditorTracking {
    val tracker = TrackApp.getInstance().gtm

    @JvmStatic
    fun onSaveEditImage(label: String, userId: String) {
        tracker.sendGeneralEvent(createEventMap(ImageEditorTrackingConstant.EVENT,
            ImageEditorTrackingConstant.EVENT_CATEGORY, ImageEditorTrackingConstant.EVENT_ACTION,
            label, userId
        ))
    }

    private fun createEventMap(event: String, category: String, action: String,
                               label: String, userId: String): HashMap<String, Any> {
        val eventMap = HashMap<String, Any>()
        eventMap[ImageEditorTrackingConstant.EVENT_KEY] = event
        eventMap[ImageEditorTrackingConstant.CATEGORY_KEY] = category
        eventMap[ImageEditorTrackingConstant.ACTION_KEY] = action
        eventMap[ImageEditorTrackingConstant.LABEL_KEY] = label
        eventMap[ImageEditorTrackingConstant.BUSINESS_UNIT_KEY] =
                ImageEditorTrackingConstant.BUSINESS_UNIT
        eventMap[ImageEditorTrackingConstant.CURRENT_SITE_KEY] =
                ImageEditorTrackingConstant.CURRENT_SITE
        eventMap[ImageEditorTrackingConstant.USER_ID_KEY] = userId
        return eventMap
    }
}