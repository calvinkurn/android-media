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
    val data: EditorDetailUiModel? = null,
    val translateX: Float = 0f,
    val translateY: Float = 0f,
    val imageScale: Float = 0f,
    val isRotate: Boolean = false,
    val isCrop: Boolean = false,
    val scaleX: Float = 1f,
    val scaleY: Float = 1f,
    val isNormalizeY: Boolean = false
)
