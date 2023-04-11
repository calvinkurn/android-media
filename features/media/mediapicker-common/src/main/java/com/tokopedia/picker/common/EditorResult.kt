package com.tokopedia.picker.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditorResult(
    val originalPaths: List<String> = emptyList(),
    val editedImages: List<String> = emptyList()
) : Parcelable