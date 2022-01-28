package com.tokopedia.picker.ui

import android.annotation.SuppressLint
import com.tokopedia.picker.common.PickerPageType
import java.io.File

@SuppressLint("ResponseFieldAnnotation")
data class PickerParam(
    var limit: Int = 5,
    var maxVideoDuration: Int = 30000, // in interval, 30 sec
    var isFolderMode: Boolean = true,
    var isIncludeVideo: Boolean = true,
    var isOnlyVideo: Boolean = false,
    var isIncludeAnimation: Boolean = false,
    var isMultipleSelection: Boolean = true,
    var excludedImages: List<File> = emptyList(),
    var pageType: Int = PickerPageType.COMMON,
    var cameraRatio: CameraRatio = CameraRatio.Full
) {

    fun isCommonPageType() = pageType == PickerPageType.COMMON

    fun ratioIsSquare() = cameraRatio == CameraRatio.Square

}

sealed class CameraRatio {
    object Square: CameraRatio()
    object Full: CameraRatio()
}