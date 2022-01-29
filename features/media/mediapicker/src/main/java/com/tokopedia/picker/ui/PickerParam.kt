package com.tokopedia.picker.ui

import android.annotation.SuppressLint
import com.tokopedia.picker.common.PickerPageType
import java.io.File

@SuppressLint("ResponseFieldAnnotation")
data class PickerParam(
    private var maxPhoto: Int = 4,
    private var maxVideo: Int = 1,
    private var maxVideoDuration: Int = 30000, // in interval, 30 sec
    private var isIncludeVideo: Boolean = true,
    private var isOnlyVideo: Boolean = false,
    private var isIncludeAnimation: Boolean = false,
    private var isMultipleSelection: Boolean = true,
    private var excludedMedias: List<File> = emptyList(),
    private var pageType: Int = PickerPageType.COMMON,
    private var cameraRatio: CameraRatio = CameraRatio.Full
) {

    fun isMultipleSelectionType() = isMultipleSelection

    fun isIncludeVideoFile() = isIncludeVideo

    fun isOnlyVideoFile() = isOnlyVideo

    fun isIncludeGifFile() = isIncludeAnimation

    fun excludeMedias() = excludedMedias

    fun maxVideoCount() = maxVideo

    fun limitOfMedia() = maxPhoto + maxVideo

    fun maxVideoDuration() = maxVideoDuration

    fun isCommonPageType() = pageType == PickerPageType.COMMON

    fun ratioIsSquare() = cameraRatio == CameraRatio.Square

}

sealed class CameraRatio {
    object Square: CameraRatio()
    object Full: CameraRatio()
}