package com.tokopedia.media.picker.ui.uimodel

import android.content.Context
import android.net.Uri
import android.os.Parcelable
import com.tokopedia.media.picker.utils.extractVideoDuration
import com.tokopedia.media.picker.utils.isVideoFormat
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class MediaUiModel(
    val id: Long = 0L,
    val name: String = "",
    val path: String = "",
    val uri: Uri? = null,
    val isFromCamera: Boolean = false,
) : Parcelable {

    fun isVideo() = isVideoFormat(path)

    fun isVideoDurationValid(context: Context): Boolean {
        if (!isVideo()) return false

        val extractDuration = extractVideoDuration(context, path)?: 0
        return extractDuration >= VIDEO_DURATION_MINIMUM
    }

    companion object {
        const val VIDEO_DURATION_MINIMUM = 3000

        fun File.toUiModel() = MediaUiModel(
            id = System.currentTimeMillis(),
            name = name,
            path = path,
            uri = Uri.parse(path),
            isFromCamera = true,
        )
    }

}