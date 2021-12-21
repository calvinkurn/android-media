package com.tokopedia.picker.data.param

import android.annotation.SuppressLint
import java.io.File

@SuppressLint("ResponseFieldAnnotation")
data class ConfigLoaderParam(
    var isFolderMode: Boolean = true,
    var isIncludeVideo: Boolean = false,
    var isOnlyVideo: Boolean = false,
    var isIncludeAnimation: Boolean = false,
    var excludedImages: List<File> = emptyList(),
)