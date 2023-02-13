package com.tokopedia.picker.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditorImageSource(
    val originalPaths: List<String> = emptyList()
) : Parcelable