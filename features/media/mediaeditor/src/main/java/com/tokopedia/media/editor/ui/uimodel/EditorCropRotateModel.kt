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

    fun compareValue(data: EditorCropRotateModel): Boolean {
        return data.offsetX == offsetX
                && data.offsetY == offsetY
                && data.imageWidth == imageWidth
                && data.imageHeight == imageHeight
                && data.scale == scale
                && data.translateX == translateX
                && data.translateY == translateY
                && data.scaleX == scaleX
                && data.scaleY == scaleY
                && data.rotateDegree == rotateDegree
                && data.orientationChangeNumber == orientationChangeNumber
                && data.isRotate == isRotate
                && data.isCrop == isCrop
                && data.isAutoCrop == isAutoCrop
    }

    fun getRatio(): Float?{
        return if(imageWidth == 0 && imageHeight == 0) null else imageWidth.toFloat() / imageHeight
    }

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