package com.tokopedia.media.editor.ui.uimodel

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.media.editor.utils.shouldNull
import com.tokopedia.picker.common.types.EditorToolType
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
@SuppressLint("ParamFieldAnnotation")
data class EditorDetailUiModel(
    val originalUrl: String = "",

    @EditorToolType
    val editorToolType: Int = EditorToolType.NONE,

    var resultUrl: String? = null,

    var brightnessValue: Float? = null,
    var contrastValue: Float? = null,
    var watermarkMode: Int? = null,
    var removeBackgroundUrl: String? = null,
    var isContrastExecuteFirst: Int? = null,
) : Parcelable {
    @IgnoredOnParcel
    var cropRotateValue: EditorCropRotateModel = getEmptyEditorCropRotateModel()

    constructor(parcel: Parcel) : this(
        originalUrl = parcel.readString() ?: "",
        editorToolType = parcel.readInt(),
        resultUrl = parcel.readString(),
        brightnessValue = shouldNull(parcel.readFloat()),
        contrastValue = shouldNull(parcel.readFloat()),
        watermarkMode = shouldNull(parcel.readInt()),
        removeBackgroundUrl = parcel.readString(),
        isContrastExecuteFirst = parcel.readInt()
    ) {
        cropRotateValue.offsetX = parcel.readInt()
        cropRotateValue.offsetY = parcel.readInt()
        cropRotateValue.imageWidth = parcel.readInt()
        cropRotateValue.imageHeight = parcel.readInt()
        cropRotateValue.scale = parcel.readFloat()
        cropRotateValue.translateX = parcel.readFloat()
        cropRotateValue.translateY = parcel.readFloat()
        cropRotateValue.scaleX = parcel.readFloat()
        cropRotateValue.scaleY = parcel.readFloat()
        cropRotateValue.rotateDegree = parcel.readFloat()
        cropRotateValue.orientationChangeNumber = parcel.readInt()
        cropRotateValue.isRotate = parcel.readInt() == 1
        cropRotateValue.isCrop = parcel.readInt() == 1
        cropRotateValue.isAutoCrop = parcel.readInt() == 1
    }

    // used only for remove background
    fun clearValue(){
        brightnessValue = null
        contrastValue = null
        watermarkMode = null
        cropRotateValue = getEmptyEditorCropRotateModel()
    }

    private fun getEmptyEditorCropRotateModel(): EditorCropRotateModel{
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

    fun isToolBrightness(): Boolean{
        return editorToolType == EditorToolType.BRIGHTNESS
    }

    fun isToolContrast(): Boolean{
        return editorToolType == EditorToolType.CONTRAST
    }

    fun isToolRotate(): Boolean{
        return editorToolType == EditorToolType.ROTATE
    }

    fun isToolWatermark(): Boolean{
        return editorToolType == EditorToolType.WATERMARK
    }

    fun isToolRemoveBackground(): Boolean{
        return editorToolType == EditorToolType.REMOVE_BACKGROUND
    }

    fun isToolCrop(): Boolean{
        return editorToolType == EditorToolType.CROP
    }

    companion object : Parceler<EditorDetailUiModel> {
        override fun create(parcel: Parcel): EditorDetailUiModel {
            return EditorDetailUiModel(parcel)
        }

        override fun EditorDetailUiModel.write(parcel: Parcel, flags: Int) {
            parcel.writeString(originalUrl)
            parcel.writeInt(editorToolType)
            parcel.writeString(resultUrl)
            parcel.writeFloat(brightnessValue ?: -1f)
            parcel.writeFloat(contrastValue ?: -1f)
            parcel.writeInt(watermarkMode ?: -1)
            parcel.writeString(removeBackgroundUrl)
            parcel.writeInt(isContrastExecuteFirst ?: 0)

            parcel.writeInt(cropRotateValue.offsetX)
            parcel.writeInt(cropRotateValue.offsetY)
            parcel.writeInt(cropRotateValue.imageWidth)
            parcel.writeInt(cropRotateValue.imageHeight)
            parcel.writeFloat(cropRotateValue.scale)
            parcel.writeFloat(cropRotateValue.translateX)
            parcel.writeFloat(cropRotateValue.translateY)
            parcel.writeFloat(cropRotateValue.scaleX)
            parcel.writeFloat(cropRotateValue.scaleY)
            parcel.writeFloat(cropRotateValue.rotateDegree)
            parcel.writeInt(cropRotateValue.orientationChangeNumber)
            parcel.writeInt(if (cropRotateValue.isRotate) 1 else 0)
            parcel.writeInt(if (cropRotateValue.isCrop) 1 else 0)
            parcel.writeInt(if (cropRotateValue.isAutoCrop) 1 else 0)
        }

        private const val DEFAULT_NULL_VALUE_ROTATE_SCALE = -2f
    }
}