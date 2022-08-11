package com.tokopedia.media.editor.ui.uimodel

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
    var orientationChangeNumber: Int
){
    fun isRotate(): Boolean{
        return (scaleX != 0f && scaleX != 1f ) || rotateDegree != 0f || orientationChangeNumber != 0
    }

    fun isCrop(): Boolean{
        return imageWidth != 0 && imageHeight != 0
    }
}