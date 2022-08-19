package com.tokopedia.media.editor.ui.uimodel

import android.os.Parcelable
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
    var isAutoCrop: Boolean = false
): Parcelable {
    companion object{
        fun getEmptyEditorCropRotateModel(): EditorCropRotateModel{
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
                isAutoCrop = false
            )
        }
    }
}