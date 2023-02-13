package com.tokopedia.media.editor.data.entity

import android.annotation.SuppressLint
import com.tokopedia.media.editor.ui.uimodel.EditorWatermarkUiModel

@SuppressLint("EntityFieldAnnotation")
data class EditorDetailEntity(
    var watermarkModeEntityData: EditorWatermarkUiModel?
)