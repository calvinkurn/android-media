package com.tokopedia.media.editor.ui.uimodel

import android.os.Parcelable
import com.tokopedia.picker.common.ImageRatioType
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditorCropRotateModel(
    var offsetX: Int,
    var offsetY: Int,
    var imageWidth: Int,
    var imageHeight: Int,
    var scale: Float,
    var translateX: Float,
    var translateY: Float,
    var scaleX: Float,
    var scaleY: Float,
    var rotateDegree: Float,
    var orientationChangeNumber: Int,
    var isRotate: Boolean,
    var isCrop: Boolean,
    var isAutoCrop: Boolean = false,
    var croppedSourceWidth: Int,
    var cropRatio: Pair<Int, Int>
) : Parcelable {

    // cropRatio.first = ratio width || cropRatio.second = ratio height
    fun getRatio(): Float? {
        return if (imageWidth == 0 && imageHeight == 0) null else cropRatio.first.toFloat() / cropRatio.second
    }

    companion object {
        fun getEmptyEditorCropRotateModel(): EditorCropRotateModel {
            return EditorCropRotateModel(
                0,
                0,
                0,
                0,
                0f,
                0f,
                0f,
                0f,
                0f,
                0f,
                0,
                isRotate = false,
                isCrop = false,
                isAutoCrop = false,
                0,
                Pair(1, 1)
            )
        }
    }
}