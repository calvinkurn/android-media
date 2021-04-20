package com.tokopedia.imagepicker.common

import android.os.Parcelable
import com.tokopedia.imagepicker.common.ImagePickerEditorBuilder.Companion.getDefaultEditor
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageEditorBuilder(
        var imageUrls: ArrayList<String>,
        var imageDescriptions: ArrayList<String>? = arrayListOf(),
        var minResolution: Int = DEFAULT_MIN_RESOLUTION,
        var imageEditActionType: Array<ImageEditActionType> = getDefaultEditor(),
        var defaultRatio: ImageRatioType = ImageRatioType.ORIGINAL,
        var isCirclePreview: Boolean = false,
        var maxFileSize: Int = DEFAULT_MAX_IMAGE_SIZE_IN_KB,
        var ratioOptionList: ArrayList<ImageRatioType> = arrayListOf(ImageRatioType.ORIGINAL),
        var belowMinResolutionErrorMessage: String = "",
        var imageTooLargeErrorMessage: String = "",
        var recheckSizeAfterResize: Boolean = false,
        var convertToWebp: Boolean = false
) : Parcelable