package com.tokopedia.media.editor.ui.uimodel

data class EditorCropRectModel(
    val offsetX: Int,
    val offsetY: Int,
    val imageWidth: Int,
    val imageHeight: Int,
    val scale: Float,
    var croppedUrl: String,
    var translateX: Float,
    var translateY: Float
)