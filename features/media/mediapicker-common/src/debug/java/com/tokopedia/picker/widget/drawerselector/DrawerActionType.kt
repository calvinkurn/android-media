package com.tokopedia.picker.widget.drawerselector

import com.tokopedia.picker.common.uimodel.MediaUiModel

sealed class DrawerActionType {
    class Add(
        val data: List<MediaUiModel>,
        val media: MediaUiModel,
        val error: String?
    ): DrawerActionType()

    class Remove(
        val data: List<MediaUiModel>,
        val mediaToRemove: MediaUiModel,
        val error: String?
    ): DrawerActionType()

    class Reorder(
        val data: List<MediaUiModel>,
        val error: String?
    ): DrawerActionType()
}
