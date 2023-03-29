package com.tokopedia.media.picker.data.entity

import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import com.tokopedia.picker.common.utils.wrapper.PickerFile

data class Media(
    val id: Long,
    val file: PickerFile,
    var videoLength: Int = 0
) {

    private var uriHolder: Uri? = null

    val name: String
        get() = file.name

    val path: String
        get() = file.path

    val uri: Uri
        get() {
            return uriHolder ?: let {
                val contentUri = if (file.isVideo()) {
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else {
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }

                ContentUris.withAppendedId(contentUri, id).also {
                    uriHolder = it
                }
            }
        }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other == null || javaClass != other.javaClass -> return false
            else -> {
                val media = other as Media
                media.path.equals(path, ignoreCase = true)
            }
        }
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + path.hashCode()
        result = 31 * result + (uriHolder?.hashCode() ?: 0)
        return result
    }

}
