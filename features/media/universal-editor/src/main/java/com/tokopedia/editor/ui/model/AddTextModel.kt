package com.tokopedia.editor.ui.model

data class AddTextModel (
    val deltaX: Float = 0f,
    val deltaY: Float = 0f,
    val deltaScale: Float = 0f,
    val deltaAngle: Float = 0f,
    val pivotX: Float = 0f,
    val pivotY: Float = 0f,
    val minScale: Float = 0f,
    val maxScale: Float = 0f
)
