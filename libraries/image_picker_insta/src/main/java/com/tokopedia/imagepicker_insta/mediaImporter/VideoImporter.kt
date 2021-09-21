package com.tokopedia.imagepicker_insta.mediaImporter

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.util.SparseArray
import com.tokopedia.imagepicker_insta.models.*
import com.tokopedia.imagepicker_insta.util.CursorUtil
import com.tokopedia.imagepicker_insta.util.FileUtil
import com.tokopedia.imagepicker_insta.util.StorageUtil
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit

class VideoImporter : MediaImporter {

    companion object {
        val DURATION_MAX_LIMIT = 59
    }

    fun getViewMetaData(filePath: String, context: Context): VideoMetaData {
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

    @Throws(Exception::class)
    fun createVideosDataFromInternalFile(file: File, duration: Long): VideoData {
        if (file.isDirectory) throw Exception("Got folder instead of file")
        return VideoData(
            file.absolutePath,
            StorageUtil.INTERNAL_FOLDER_NAME,
            Uri.fromFile(file),
            getCreateAtForInternalFile(file),
            getFormattedDurationText(duration),
            isVideoWithinLimit(duration)
        )
    }

    override fun importMediaFromInternalDir(context: Context): List<Asset> {
        val directory = File(context.filesDir, StorageUtil.INTERNAL_FOLDER_NAME)
        val videoDataList = arrayListOf<VideoData>()
        if (directory.isDirectory) {
            directory.listFiles()?.forEach {
                val videoMetaData = getViewMetaData(it.absolutePath, context)

                if (videoMetaData.isSupported) {
                    val duration = it.getMediaDuration(context)
                    if (duration != null && duration >= 1) {
                        val videoData = createVideosDataFromInternalFile(it, videoMetaData.duration)
                        videoDataList.add(videoData)
                    }
                }
            }
        }
        return videoDataList
    }

    fun getFormattedDurationText(durationInMillis: Long): String {
        val seconds = TimeUnit.MILLISECONDS.toSeconds(durationInMillis) % 60
        val secondText = if (seconds < 10) "0$seconds" else seconds
        val minutes = TimeUnit.MILLISECONDS.toMinutes(durationInMillis)
        val minuteText = if (minutes < 10) "0$minutes" else minutes
        return "$minuteText:$secondText"
    }

    fun isVideoWithinLimit(durationInMillis: Long): Boolean {
        return (durationInMillis / 1000) <= DURATION_MAX_LIMIT
    }

    fun File.getMediaDuration(context: Context): Long? {
        try {
            if (!exists()) return 0
            if (length() == 0L) return 0
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(context, Uri.parse(absolutePath))
            val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            retriever.release()
            return duration?.toLong()
        } catch (th: Throwable) {
            Timber.e(th)
            return null
        }

    }

    override fun importMedia(context: Context): MediaImporterData {
        val videoCursor = CursorUtil.getVideoCursor(context, "", null)
        val data = iterateVideoCursor(videoCursor)
        return data
    }

    protected fun iterateVideoCursor(cur: Cursor?): MediaImporterData {
        val videosList = arrayListOf<Asset>()
        val folders = hashSetOf<String>()

        if (cur != null && cur.count > 0) {
            try {
                if (cur.moveToFirst()) {
                    val dateTakenIndex = cur.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN)
                    val dateAddedIndex = cur.getColumnIndex(MediaStore.Video.Media.DATE_ADDED)
                    val path = cur.getColumnIndex(MediaStore.Video.Media.DATA)
                    val id = cur.getColumnIndex(MediaStore.Video.Media._ID)
                    val bytes = cur.getColumnIndex(MediaStore.Video.Media.SIZE)
                    val durationIndex = cur.getColumnIndex(MediaStore.Video.Media.DURATION)
                    do {
                        try {
                            val item = JSONObject()
                            val name = cur.getString(path)
                            val numBytes = cur.getLong(bytes)
                            val numBytesKB = numBytes / 1024 // skip videos below 10 KB in size
                            val index = cur.getLong(id)
                            val duration = cur.getLong(durationIndex)

                            //Set default values
                            item.put(PhotoImporter.FOLDER_KEY, "")
                            val dateAdded = cur.getLong(dateAddedIndex)
                            val dateTaken = cur.getLong(dateTakenIndex)

                            if (numBytesKB > 10) {

                                val dateLong = getDate(dateAdded, 0L, name)
                                if (dateLong != 0L) {

                                    val folderName: String = FileUtil.getFolderName(name)
                                    item.put(PhotoImporter.FOLDER_KEY, folderName)
                                }

                                val folderName = item[PhotoImporter.FOLDER_KEY] as String

                                val videosData = VideoData(
                                    assetPath = name,
                                    folder = folderName,
                                    contentUri = ContentUris.withAppendedId(
                                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                        index
                                    ),
                                    createdDate = dateLong,
                                    durationText = getFormattedDurationText(duration),
                                    canBeSelected = isVideoWithinLimit(duration)
                                )
                                folders.add(folderName)
                                videosList.add(videosData)
                            }

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    } while (cur.moveToNext())
                }
            } finally {
                cur.close()
            }
        }

        val imageAdapterDataList = ArrayList<ImageAdapterData>()
        videosList.forEach {
            imageAdapterDataList.add(ImageAdapterData(it))
        }
        return MediaImporterData(imageAdapterDataList, folders)
    }
}
