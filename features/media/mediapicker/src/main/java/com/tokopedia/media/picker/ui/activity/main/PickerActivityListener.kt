package com.tokopedia.media.picker.ui.activity.main

import com.tokopedia.media.picker.ui.uimodel.MediaUiModel

interface PickerActivityListener {
    fun tabVisibility(isShown: Boolean)
    fun mediaSelected(): List<MediaUiModel>
}