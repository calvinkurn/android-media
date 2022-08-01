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
    ) {

        // crop bound
        val rectHeight = parcel.readInt()
        val rectWidth = parcel.readInt()
        val rectOffsetX = parcel.readInt()
        val rectOffsetY = parcel.readInt()

        if (rectHeight != 0 &&
            rectWidth != 0 &&
            rectOffsetX != 0 &&
            rectOffsetY != 0
        ) {
            val cropRect = EditorCropRectModel(
                rectHeight,
                rectWidth,
                rectOffsetX,
                rectOffsetY
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

        if (rotateDegree != 0f || orientationChangeNumber != 0) {
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
        rotateData = null
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

            // crop bound
            parcel.writeInt(cropBound?.imageHeight ?: 0)
            parcel.writeInt(cropBound?.imageWidth ?: 0)
            parcel.writeInt(cropBound?.offsetX ?: 0)
            parcel.writeInt(cropBound?.offsetY ?: 0)
//            cropBound?.let {
//                parcel.writeInt(it.imageHeight)
//                parcel.writeInt(it.imageWidth)
//                parcel.writeInt(it.offsetX)
//                parcel.writeInt(it.offsetY)
//            }

            // rotate
            parcel.writeFloat(rotateData?.rotateDegree ?: 0f)
            parcel.writeFloat(rotateData?.scaleX ?: 0f)
            parcel.writeFloat(rotateData?.scaleY ?: 0f)
            parcel.writeInt(rotateData?.leftRectPos ?: 0)
            parcel.writeInt(rotateData?.topRectPos ?: 0)
            parcel.writeInt(rotateData?.rightRectPos ?: 0)
            parcel.writeInt(rotateData?.bottomRectPos ?: 0)
            parcel.writeInt(rotateData?.orientationChangeNumber ?: 0)
//            rotateData?.let {
//                parcel.writeFloat(it.rotateDegree)
//                parcel.writeFloat(it.scaleX)
//                parcel.writeFloat(it.imageRatio)
//            }
        }
    }
}