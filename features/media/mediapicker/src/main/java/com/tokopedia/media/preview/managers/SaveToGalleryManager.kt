package com.tokopedia.media.preview.managers

import android.content.Context
import android.os.Environment
import com.tokopedia.picker.common.utils.isVideoFormat
import com.tokopedia.utils.file.PublicFolderUtil
import java.io.File

interface SaveToGalleryManager {
    fun dispatch(filePath: String): File?
}

class SaveToGalleryManagerImpl constructor(
    private val context: Context
) : SaveToGalleryManager {

    override fun dispatch(filePath: String): File? {
        val file = File(filePath)

        return PublicFolderUtil.putFileToPublicFolder(
            context = context,
            localFile = file,
            mimeType = mimeType(filePath),
            outputFileName = fileName(file.name),
            directory = Environment.DIRECTORY_DOWNLOADS
        ).first
    }

    private fun mimeType(filePath: String): String {
        return if (isVideoFormat(filePath)) {
            MIME_VIDEO_TYPE
        } else {
            MIME_IMAGE_TYPE
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