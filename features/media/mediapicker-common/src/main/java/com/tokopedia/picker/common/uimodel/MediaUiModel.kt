package com.tokopedia.picker.common.uimodel

import android.net.Uri
import android.os.Parcelable
import com.tokopedia.picker.common.utils.wrapper.PickerFile
import kotlinx.parcelize.Parcelize

@Parcelize
open class MediaUiModel(
    val id: Long = 0L,
    val file: PickerFile? = null,
    var duration: Int = 0,
    val uri: Uri? = null,

    /*
    * this data come from camera tab,
    * the media file is removable.
    * */
    var isCacheFile: Boolean = false,
    var sourcePath: String? = file?.absolutePath
) : Parcelable {

    fun getSingleFilePath(): String {
        return file?.path ?: ""
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return other is MediaUiModel &&
                id == other.id &&
                file == other.file &&
                uri == other.uri &&
                isCacheFile == other.isCacheFile &&
                sourcePath == other.sourcePath
    }

    override fun hashCode(): Int {
        var hashCode = id.hashCode()
        hashCode = 5 * hashCode + file.hashCode()
        hashCode = 5 * hashCode + uri.hashCode()
        hashCode = 5 * hashCode + isCacheFile.hashCode()
        hashCode = 5 * hashCode + sourcePath.hashCode()
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

        fun PickerFile.toRemovableUiModel() = toUiModel().also {
            it.isCacheFile = true
        }
    }
}
