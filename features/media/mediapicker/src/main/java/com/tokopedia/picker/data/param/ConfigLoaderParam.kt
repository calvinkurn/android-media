package com.tokopedia.picker.data.param

import android.annotation.SuppressLint
import java.io.File

@SuppressLint("ResponseFieldAnnotation")
data class ConfigLoaderParam(
    var limit: Int = 2,
    var isFolderMode: Boolean = true,
    var isIncludeVideo: Boolean = true,
    var isOnlyVideo: Boolean = false,
    var isIncludeAnimation: Boolean = false,
    var excludedImages: List<File> = emptyList(),
)