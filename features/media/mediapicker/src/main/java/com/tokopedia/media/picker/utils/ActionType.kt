package com.tokopedia.media.picker.utils

import com.tokopedia.media.common.uimodel.MediaUiModel

sealed class ActionType {
    class Add(
        val data: List<MediaUiModel>,
        val media: MediaUiModel,
        val error: String?
    ): ActionType()

    class Remove(
        val data: List<MediaUiModel>,
        val mediaToRemove: MediaUiModel,
        val error: String?
    ): ActionType()

    class Reorder(
        val data: List<MediaUiModel>,
        val error: String?
    ): ActionType()
}
