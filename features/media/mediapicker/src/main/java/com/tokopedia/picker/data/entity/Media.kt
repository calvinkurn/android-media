package com.tokopedia.picker.data.entity

import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import com.tokopedia.picker.utils.isVideoFormat
import java.io.File

data class Media(
    val id: Long,
    val name: String,
    val path: String
) {

    private var uriHolder: Uri? = null

    val uri: Uri
        get() {
            return uriHolder ?: let {
                val contentUri = if (isVideoFormat(path)) {
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

    companion object {
        private const val SPECIAL_ID_CAMERA = -321L

        fun File.createFoCameraCaptured() = Media(
            id = SPECIAL_ID_CAMERA,
            name = this.name,
            path = this.path
        )
    }

}