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
    var cropBound: EditorCropRectModel? = null
    @IgnoredOnParcel
    var rotateData: EditorRotateModel? = null

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

        // crop bound
        val rectHeight = parcel.readInt()
        val rectWidth = parcel.readInt()
        val rectOffsetX = parcel.readInt()
        val rectOffsetY = parcel.readInt()
        val scale = parcel.readFloat()
        val croppedUrl = parcel.readString()
        val translateX = parcel.readFloat()
        val translateY = parcel.readFloat()

        if (rectHeight != -1 && rectWidth != -1 && rectOffsetX != -1 && rectOffsetY != -1) {
            val cropRect = EditorCropRectModel(
                rectOffsetX,
                rectOffsetY,
                rectWidth,
                rectHeight,
                scale,
                croppedUrl ?: "",
                translateX,
                translateY
            )

            cropBound = cropRect
        }

        // rotate data
        val rotateDegree = parcel.readFloat()
        val scaleX = parcel.readFloat()
        val scaleY = parcel.readFloat()
        val leftRectPos = parcel.readInt()
        val topRectPos = parcel.readInt()
        val rightRectPos = parcel.readInt()
        val bottomRectPos = parcel.readInt()
        val orientationChangeNumber = parcel.readInt()

        if (rotateDegree != 0f ||
            orientationChangeNumber != 0 ||
            scaleX != DEFAULT_NULL_VALUE_ROTATE_SCALE
        ) {
            rotateData = EditorRotateModel(
                rotateDegree,
                scaleX,
                scaleY,
                leftRectPos,
                topRectPos,
                rightRectPos,
                bottomRectPos,
                orientationChangeNumber
            )
        }
    }

    // used only for remove background
    fun clearValue(){
        brightnessValue = null
        contrastValue = null
        watermarkMode = null
        rotateData?.apply {
            rotateDegree = 0f
            orientationChangeNumber = 0
            scaleX = 1f
        }
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

            // crop bound
            parcel.writeInt(cropBound?.imageHeight ?: -1)
            parcel.writeInt(cropBound?.imageWidth ?: -1)
            parcel.writeInt(cropBound?.offsetX ?: -1)
            parcel.writeInt(cropBound?.offsetY ?: -1)
            parcel.writeFloat(cropBound?.scale ?: -1f)
            parcel.writeString(cropBound?.croppedUrl ?: "")
            parcel.writeFloat(cropBound?.translateX ?: 0f)
            parcel.writeFloat(cropBound?.translateY ?: 0f)

            // rotate
            parcel.writeFloat(rotateData?.rotateDegree ?: 0f)
            parcel.writeFloat(rotateData?.scaleX ?: DEFAULT_NULL_VALUE_ROTATE_SCALE)
            parcel.writeFloat(rotateData?.scaleY ?: DEFAULT_NULL_VALUE_ROTATE_SCALE)
            parcel.writeInt(rotateData?.leftRectPos ?: 0)
            parcel.writeInt(rotateData?.topRectPos ?: 0)
            parcel.writeInt(rotateData?.rightRectPos ?: 0)
            parcel.writeInt(rotateData?.bottomRectPos ?: 0)
            parcel.writeInt(rotateData?.orientationChangeNumber ?: 0)
        }

        private const val DEFAULT_NULL_VALUE_ROTATE_SCALE = -2f
    }
}