package com.tokopedia.picker.ui.uimodel

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File
import java.util.*

@Parcelize
data class MediaUiModel(
    val id: Long = 0L,
    val name: String = "",
    val path: String = "",
    val uri: Uri? = null,
    val isFromCamera: Boolean = false,
) : Parcelable {

    companion object {
        fun File.captureToMediaUiModel() = MediaUiModel(
            id = System.currentTimeMillis(),
            name = name,
            path = path,
            uri = Uri.parse(path),
            isFromCamera = true,
        )
    }

}