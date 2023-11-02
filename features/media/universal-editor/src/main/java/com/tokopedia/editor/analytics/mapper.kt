package com.tokopedia.editor.analytics

import com.tokopedia.picker.common.utils.MediaFile

private const val IMAGE_TEXT = "image"
private const val VIDEO_TEXT = "video"

private const val UNIQUE_ID_EMPTY = "0"

fun getMediaTypeString(file: MediaFile) = if (file.isImage()) IMAGE_TEXT else VIDEO_TEXT

// convert map data to tracker format
// map -> value1, value2
fun Map<String, String>.toImmersiveTrackerData(): String {
    return if (this.isEmpty()) {
        UNIQUE_ID_EMPTY
    } else {
        var resultString = ""
        this.toSortedMap().values.forEach {
            resultString += "${it},"
        }

        resultString.dropLast(1)
    }
}
