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

    fun isVideoFile(filePath: String?): Boolean {
        if (filePath.isNullOrEmpty()) return false
        return filePath.endsWith(".mp4", ignoreCase = true)
    }

    fun createPhotosDataFromInternalFile(file: File): PhotosData {
        if (file.isDirectory) throw Exception("Got folder instead of file")
        return PhotosData(
            Uri.fromFile(file),
            createdDate = getCreateAtForInternalFile(file)
        )
    }

    fun getVideoMetaData(filePath: String, context: Context): VideoMetaData {
        val isVideoFile = filePath.endsWith(".mp4")
        if (isVideoFile) {
            val file = File(filePath)
            val duration = file.getMediaDuration(context)
            if (duration != null && duration >= 1) {
                return VideoMetaData(true, duration)
            }
        }
        return VideoMetaData(false, 0)
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
                } else if (isVideoFile(filePath)) {
                    val videoMetaData = getVideoMetaData(it.absolutePath, context)

                    if (videoMetaData.isSupported) {
                        val duration = it.getMediaDuration(context)
                        if (duration != null && duration >= 1) {
                            val videoData = createVideosDataFromInternalFile(it, videoMetaData.duration, queryConfiguration.videoMaxDuration)
                            photosDataList.add(videoData)
                        }
                    }
                }
            }
        }
        return photosDataList
    }

    @Throws(Exception::class)
    fun createVideosDataFromInternalFile(file: File, duration: Long, maxDuration:Long): VideoData {
        if (file.isDirectory) throw Exception("Got folder instead of file")
        return VideoData(
            Uri.fromFile(file),
            getCreateAtForInternalFile(file),
            VideoUtil.getFormattedDurationText(duration),
            VideoUtil.isVideoWithinLimit(duration, maxDuration)
        )
    }

    fun getCreateAtForInternalFile(file: File):Long{
        return file.lastModified()/1000L
    }
}