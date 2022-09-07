package com.tokopedia.media.editor.ui.uimodel

import android.os.Parcelable
import com.tokopedia.media.editor.ui.uimodel.EditorCropRotateModel.Companion.getEmptyEditorCropRotateModel
import com.tokopedia.picker.common.types.EditorToolType
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditorDetailUiModel(
    val originalUrl: String = "",

    @EditorToolType
    val editorToolType: Int = EditorToolType.NONE,

    var resultUrl: String? = null,

    var brightnessValue: Float? = null,
    var contrastValue: Float? = null,
    var watermarkMode: Int? = null,
    var removeBackgroundUrl: String? = null,
    var isContrastExecuteFirst: Boolean? = null,
    var cropRotateValue: EditorCropRotateModel = getEmptyEditorCropRotateModel(),
    var originalImageRatio: Float = 1f
) : Parcelable {
    // used only for remove background
    fun clearValue() {
        brightnessValue = null
        contrastValue = null
        watermarkMode = null
        cropRotateValue = getEmptyEditorCropRotateModel()
    }

    fun isToolBrightness(): Boolean {
        return editorToolType == EditorToolType.BRIGHTNESS
    }

    fun isToolContrast(): Boolean {
        return editorToolType == EditorToolType.CONTRAST
    }

    fun isToolRotate(): Boolean {
        return editorToolType == EditorToolType.ROTATE
    }

    fun isToolRemoveBackground(): Boolean {
        return editorToolType == EditorToolType.REMOVE_BACKGROUND
    }

    fun isToolCrop(): Boolean {
        return editorToolType == EditorToolType.CROP
    }

    fun isToolWatermark(): Boolean {
        return editorToolType == EditorToolType.WATERMARK
    }
}