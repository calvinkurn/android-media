package com.tokopedia.media.editor.ui.uimodel

import android.os.Parcelable
import com.tokopedia.picker.common.ImageRatioType
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditorCropRotateUiModel(
    var offsetX: Int = 0,
    var offsetY: Int = 0,
    var imageWidth: Int = 0,
    var imageHeight: Int = 0,
    var scale: Float = 0f,
    var translateX: Float = 0f,
    var translateY: Float = 0f,
    var scaleX: Float = 1f,
    var scaleY: Float = 1f,
    var rotateDegree: Float = 0f,
    var orientationChangeNumber: Int = 0,
    var isRotate: Boolean = false,
    var isCrop: Boolean = false,
    var isAutoCrop: Boolean = false,
    var croppedSourceWidth: Int = 0,
    var cropRatio: Pair<Int, Int> = EMPTY_RATIO
) : Parcelable {

    // cropRatio.first = ratio width || cropRatio.second = ratio height
    fun getRatio(): Float? {
        return if (imageWidth == 0 && imageHeight == 0) null else cropRatio.first.toFloat() / cropRatio.second
    }

    companion object {
        val EMPTY_RATIO = Pair(0, 0)
    }
}
