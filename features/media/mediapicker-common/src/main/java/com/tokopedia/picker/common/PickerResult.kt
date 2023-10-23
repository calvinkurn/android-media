package com.tokopedia.picker.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PickerResult(
    // the collection of files both of images and videos
    val originalPaths: List<String> = emptyList(),

    // the collection of videos
    val videoFiles: List<String> = emptyList(),

    // the collection of images (only) has compressed
    val compressedImages: List<String> = emptyList(),

    // (old/legacy): if picker using editor feature.
    val editedImages: List<String> = emptyList(),

    // if picker using editor feature (nit: universal editor)
    val editedPaths: List<String> = emptyList(),

    // list of selected of included media from picker param
    val selectedIncludeMedia: List<String> = emptyList()
) : Parcelable
