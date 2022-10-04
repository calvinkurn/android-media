package com.tokopedia.media.editor.ui.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditorWatermarkUiModel(
    var watermarkType: Int,
    var textColorDark: Boolean
) : Parcelable