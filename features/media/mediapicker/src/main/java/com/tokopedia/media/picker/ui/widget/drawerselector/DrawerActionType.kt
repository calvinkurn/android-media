package com.tokopedia.media.picker.ui.widget.drawerselector

import com.tokopedia.media.common.uimodel.MediaUiModel

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
