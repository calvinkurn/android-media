package com.tokopedia.media.editor.ui.uimodel

import android.graphics.Bitmap

data class ProcessedBitmapModel(
    val originalBitmap: Bitmap,
    val offsetX: Int,
    val offsetY: Int,
    val imageWidth: Int,
    val imageHeight: Int,
    val finalRotationDegree: Float,
    val sliderValue: Float,
    val rotateNumber: Int,
    val data: EditorDetailUiModel?,
    val translateX: Float,
    val translateY: Float,
    val imageScale: Float,
    val isRotate: Boolean,
    val isCrop: Boolean,
    val scaleX: Float,
    val scaleY: Float,
    val isNormalizeY: Boolean = false
)
