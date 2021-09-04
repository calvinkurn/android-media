package com.tokopedia.imagepicker_insta.mediaImporter

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.util.SparseArray
import com.tokopedia.imagepicker_insta.models.Asset
import com.tokopedia.imagepicker_insta.models.ImageAdapterData
import com.tokopedia.imagepicker_insta.models.MediaImporterData
import com.tokopedia.imagepicker_insta.models.VideoData
import com.tokopedia.imagepicker_insta.util.CursorUtil
import com.tokopedia.imagepicker_insta.util.FileUtil
import com.tokopedia.imagepicker_insta.util.StorageUtil
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

class VideoImporter : MediaImporter {

    override fun importMediaFromInternalDir(context: Context): List<Asset> {
        val file = File(context.filesDir, StorageUtil.INTERNAL_FOLDER_NAME)
        val videoDataList = arrayListOf<VideoData>()
        if (file.isDirectory) {
            file.listFiles()?.forEach {
                val filePath = it.absolutePath
                val isFileSupported = (filePath.endsWith(".mp4"))

                if (isFileSupported) {
                    val duration = it.getMediaDuration(context)
                    if(duration!=null && duration>=1) {
                        val videoData = VideoData(
                            filePath,
                            StorageUtil.INTERNAL_FOLDER_NAME,
                            Uri.fromFile(it),
                            it.lastModified(),
                            getFormattedDurationText(duration)
                        )
                        videoDataList.add(videoData)
                    }
                }
            }
        }
        return videoDataList
    }

    fun getFormattedDurationText(durationInMillis: Long): String {
        val seconds = TimeUnit.MILLISECONDS.toSeconds(durationInMillis)
        val secondText = if (seconds < 10) "0$seconds" else seconds % 60
        val minutes = TimeUnit.MILLISECONDS.toMinutes(durationInMillis)
        val minuteText = if (minutes < 10) "0$minutes" else minutes
        return "$minuteText:$secondText"
    }

    fun File.getMediaDuration(context: Context): Long? {
        if (!exists()) return 0
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, Uri.parse(absolutePath))
        val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        retriever.release()
        return duration?.toLong()
    }

    override fun importMedia(context: Context): MediaImporterData {
        val videoNames = SparseArray<String>()
        val videoCursor = CursorUtil.getVideoCursor(context, "", null)
        val videosOnPhone = SparseArray<JSONObject>()
        val data = iterateVideoCursor(videoCursor, videosOnPhone, videoNames)

        return data
    }

    protected fun iterateVideoCursor(
        cur: Cursor?,
        videosOnPhone: SparseArray<JSONObject>,
        photoNames: SparseArray<String>
    ): MediaImporterData {
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
                                    videosOnPhone.put(index.toInt(), item)
                                    photoNames.put(index.toInt(), name)

                                    val folderName: String = FileUtil.getFolderName(name)
                                    item.put(PhotoImporter.FOLDER_KEY, folderName)
                                }

                                val folderName = item[PhotoImporter.FOLDER_KEY] as String

                                val videosData = VideoData(
                                    filePath = name,
                                    folderName = folderName,
                                    uri = ContentUris.withAppendedId(
                                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                        index
                                    ),
                                    _createdDate = dateLong,
                                    durationText = getFormattedDurationText(duration)
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
