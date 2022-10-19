package com.tokopedia.media.editor.domain.param

import android.annotation.SuppressLint
import android.graphics.Bitmap
import com.tokopedia.media.editor.data.repository.WatermarkType
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel

@SuppressLint("ParamFieldAnnotation")
data class WatermarkUseCaseParam(
    val source: Bitmap,
    val type: WatermarkType,
    val shopNameParam: String,
    val isThumbnail: Boolean,
    val element: EditorDetailUiModel? = null,
    val useStorageColor: Boolean
)