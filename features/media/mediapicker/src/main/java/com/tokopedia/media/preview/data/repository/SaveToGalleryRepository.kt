package com.tokopedia.media.preview.data.repository

import android.content.Context
import android.os.Environment
import com.tokopedia.picker.common.utils.wrapper.PickerFile
import com.tokopedia.picker.common.utils.wrapper.PickerFile.Companion.asPickerFile
import com.tokopedia.utils.file.PublicFolderUtil
import java.io.File

interface SaveToGalleryRepository {
    fun dispatch(filePath: String): File?
}

class SaveToGalleryRepositoryImpl constructor(
    private val context: Context
) : SaveToGalleryRepository {

    override fun dispatch(filePath: String): File? {
        val file = filePath.asPickerFile()

        return PublicFolderUtil.putFileToPublicFolder(
            context = context,
            localFile = file,
            mimeType = mimeType(file),
            outputFileName = fileName(file.name),
            directory = Environment.DIRECTORY_DOWNLOADS
        ).first
    }

    private fun mimeType(file: PickerFile): String {
        return when {
            file.isVideo() -> MIME_VIDEO_TYPE
            file.isImage() -> MIME_IMAGE_TYPE
            else -> ""
        }
    }

    private fun fileName(name: String): String {
        val currentTime = System.currentTimeMillis()
        return "${FILE_NAME_PREFIX}_${currentTime}_$name"
    }

    companion object {
        private const val FILE_NAME_PREFIX = "Tkpd_"

        private const val MIME_VIDEO_TYPE = "video/mp4"
        private const val MIME_IMAGE_TYPE = "image/jpeg"
    }

}