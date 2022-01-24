package com.tokopedia.picker.ui.activity.main

import com.tokopedia.picker.ui.uimodel.MediaUiModel

interface PickerActivityListener {
    fun tabVisibility(isShown: Boolean)
    fun mediaSelected(): List<MediaUiModel>
}