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
    var rotateValue: Float? = null,
    var removeBackgroundUrl: String? = null
) : Parcelable {
    @IgnoredOnParcel
    var cropBound: EditorCropRectModel? = null

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString(),
        shouldNull(parcel.readFloat()),
        shouldNull(parcel.readFloat()),
        shouldNull(parcel.readInt()),
        shouldNull(parcel.readFloat()),
        parcel.readString()
    ) {
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
            parcel.writeFloat(rotateValue ?: -1f)
            parcel.writeString(removeBackgroundUrl)

            // crop bound
            cropBound?.let {
                parcel.writeInt(it.imageHeight)
                parcel.writeInt(it.imageWidth)
                parcel.writeInt(it.offsetX)
                parcel.writeInt(it.offsetY)
            }
        }
    }
}