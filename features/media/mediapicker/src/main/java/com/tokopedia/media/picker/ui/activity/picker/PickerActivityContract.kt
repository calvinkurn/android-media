package com.tokopedia.media.picker.ui.activity.picker

import com.tokopedia.media.picker.ui.activity.picker.listeners.ToasterListener
import com.tokopedia.media.picker.ui.activity.picker.listeners.ValidationListener
import com.tokopedia.picker.common.uimodel.MediaUiModel

interface PickerActivityContract : ValidationListener, ToasterListener {
    /**
     * Handling visibility of parent tab bottom for camera recording state
     */
    fun parentTabIsShownAs(isShown: Boolean)

    /**
     * Get selected media if the picker is multiple selection mode
     */
    fun mediaSelected(): List<MediaUiModel>

    fun onGetVideoDuration(media: MediaUiModel): Int
    fun onCameraThumbnailClicked()
    fun onEmptyStateActionClicked()
}