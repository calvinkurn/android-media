package com.tokopedia.picker.ui

import android.annotation.SuppressLint
import java.io.File

@SuppressLint("ResponseFieldAnnotation")
data class PickerParam(
    var limit: Int = 5,
    var isFolderMode: Boolean = true,
    var isIncludeVideo: Boolean = true,
    var isOnlyVideo: Boolean = false,
    var isIncludeAnimation: Boolean = false,
    var isMultipleSelection: Boolean = true,
    var excludedImages: List<File> = emptyList(),
    var cameraRatio: CameraRatio = CameraRatio.Full
)

sealed class CameraRatio {
    object Square: CameraRatio()
    object Full: CameraRatio()
}