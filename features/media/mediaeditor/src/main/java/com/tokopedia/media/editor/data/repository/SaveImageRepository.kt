package com.tokopedia.media.editor.data.repository

import android.content.Context
import android.graphics.Bitmap
import com.tokopedia.media.editor.utils.getEditorSaveFolderPath
import com.tokopedia.picker.common.utils.wrapper.PickerFile
import com.tokopedia.picker.common.utils.wrapper.PickerFile.Companion.asPickerFile
import com.tokopedia.utils.file.FileUtil
import com.tokopedia.utils.file.PublicFolderUtil
import com.tokopedia.utils.image.ImageProcessingUtil
import java.io.File
import javax.inject.Inject

interface SaveImageRepository {
    fun saveToCache(
        context: Context,
        bitmapParam: Bitmap,
        filename: String? = null
    ): File?

    fun clearEditorCache()
    fun saveToGallery(context: Context, imageList: List<String>): List<String>
}

class SaveImageRepositoryImpl @Inject constructor() : SaveImageRepository {
    override fun saveToCache(
        context: Context,
        bitmapParam: Bitmap,
        filename: String?
    ): File? {
        return ImageProcessingUtil.writeImageToTkpdPath(
            bitmapParam,
            Bitmap.CompressFormat.PNG,
            getEditorSaveFolderPath()
        )
    }

    override fun clearEditorCache() {
        FileUtil.deleteFolder(
            FileUtil.getTokopediaInternalDirectory(getEditorSaveFolderPath()).absolutePath
        )
    }

    override fun saveToGallery(context: Context, imageList: List<String>): List<String> {
        val galleryResultPath: MutableList<String> = mutableListOf()

        imageList.forEach {
            val file = it.asPickerFile()

            galleryResultPath.add(
                PublicFolderUtil.putFileToPublicFolder(
                    context = context,
                    localFile = file,
                    mimeType = mimeType(file),
                    outputFileName = fileName(file.name)
                ).first?.path ?: ""
            )
        }

        return galleryResultPath
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