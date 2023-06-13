package com.tokopedia.imagepicker_insta.mediaImporter

import android.content.Context
import android.net.Uri
import com.tokopedia.imagepicker_insta.models.*
import com.tokopedia.imagepicker_insta.util.FileUtil.getMediaDuration
import com.tokopedia.imagepicker_insta.util.StorageUtil
import com.tokopedia.imagepicker_insta.util.VideoUtil
import java.io.File

interface MediaImporter {

    fun isImageFile(filePath: String?): Boolean {
        if (filePath.isNullOrEmpty()) return false
        return (filePath.endsWith(".jpg", ignoreCase = true) ||
                filePath.endsWith(".jpeg", ignoreCase = true) ||
                filePath.endsWith(".png", ignoreCase = true) ||
                filePath.endsWith(".webP", ignoreCase = true))
    }

    fun createPhotosDataFromInternalFile(file: File): PhotosData {
        if (file.isDirectory) throw Exception("Got folder instead of file")
        return PhotosData(
            Uri.fromFile(file),
            createdDate = getCreateAtForInternalFile(file)
        )
    }

    suspend fun importMediaFromInternalDir(context: Context, queryConfiguration: QueryConfiguration): List<Asset> {
        val directory = File(context.filesDir, StorageUtil.INTERNAL_FOLDER_NAME)
        val photosDataList = arrayListOf<Asset>()
        if (directory.isDirectory) {
            val files = directory.listFiles()
            files?.sortByDescending { getCreateAtForInternalFile(it) }
            files?.forEach {
                val filePath = it.absolutePath

                if (isImageFile(filePath)) {
                    val photoData = createPhotosDataFromInternalFile(it)
                    photosDataList.add(photoData)
                }
            }
        }
        return photosDataList
    }

    fun getCreateAtForInternalFile(file: File):Long{
        return file.lastModified()/1000L
    }
}
