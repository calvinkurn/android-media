package com.tokopedia.picker.common.uimodel

import android.net.Uri
import android.os.Parcelable
import com.tokopedia.picker.common.utils.wrapper.PickerFile
import kotlinx.parcelize.Parcelize

@Parcelize
open class MediaUiModel(
    val id: Long = 0L,
    val file: PickerFile? = null,
    val videoLength: Int = 0,
    val uri: Uri? = null,

    /*
    * this data come from camera tab,
    * the media file is deletable.
    * */
    var isFromPickerCamera: Boolean = false,
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return other is MediaUiModel &&
                id == other.id &&
                file == other.file &&
                uri == other.uri &&
                isFromPickerCamera == other.isFromPickerCamera
    }

    override fun hashCode(): Int {
        var hashCode = id.hashCode()
        hashCode = 5 * hashCode + file.hashCode()
        hashCode = 5 * hashCode + uri.hashCode()
        hashCode = 5 * hashCode + isFromPickerCamera.hashCode()
        return hashCode
    }

    companion object {
        fun MutableList<MediaUiModel>.safeRemove(
            media: MediaUiModel
        ): List<MediaUiModel> {
            val index = indexOf(media)
            if (index != -1) removeAt(index)
            return this
        }

        fun PickerFile.toUiModel() = MediaUiModel(
            id = System.currentTimeMillis(),
            uri = Uri.parse(path),
            file = this,
        )

        fun PickerFile.cameraToUiModel() = toUiModel().also {
            it.isFromPickerCamera = true
        }
    }

}
