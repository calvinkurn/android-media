package com.tokopedia.imagepicker_insta.mediaImporter

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.tokopedia.imagepicker_insta.models.Asset
import com.tokopedia.imagepicker_insta.models.ImageAdapterData
import com.tokopedia.imagepicker_insta.models.MediaImporterData
import com.tokopedia.imagepicker_insta.models.PhotosData
import com.tokopedia.imagepicker_insta.util.CursorUtil
import com.tokopedia.imagepicker_insta.util.FileUtil
import com.tokopedia.imagepicker_insta.util.StorageUtil
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.io.File

class PhotoImporter : MediaImporter {
    companion object {
        const val ALL = "Recents"
        const val FOLDER_KEY = "nw_st"
    }

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
            file.absolutePath,
            StorageUtil.INTERNAL_FOLDER_NAME,
            Uri.fromFile(file),
            createdDate = getCreateAtForInternalFile(file)
        )
    }

    override suspend fun importMediaFromInternalDir(context: Context): ArrayList<Asset> {
        val directory = File(context.filesDir, StorageUtil.INTERNAL_FOLDER_NAME)
        val photosDataList = arrayListOf<Asset>()
        if (directory.isDirectory) {
            directory.listFiles()?.forEach {
                val filePath = it.absolutePath
                val isFileSupported = isImageFile(filePath)

                if (isFileSupported) {
                    val photoData = createPhotosDataFromInternalFile(it)
                    photosDataList.add(photoData)
                }
            }
        }
        return photosDataList
    }

    override suspend fun importMedia(context: Context): MediaImporterData {
        val photoCursor = CursorUtil.getPhotoCursor(context, "", null)
        val data = iteratePhotoCursor(photoCursor)

        return data
    }

    protected fun iteratePhotoCursor(cur: Cursor?): MediaImporterData {
        val photosList = arrayListOf<Asset>()
        val folders = hashSetOf<String>()

        if (cur != null && cur.count > 0) {
            try {
                if (cur.moveToFirst()) {
                    val dateTakenIndex = cur.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN)
                    val dateAddedIndex = cur.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)
                    val pathIndex = cur.getColumnIndex(MediaStore.Images.Media.DATA)
                    val id = cur.getColumnIndex(MediaStore.Images.Media._ID)
                    val bytes = cur.getColumnIndex(MediaStore.Images.Media.SIZE)
                    do {
                        try {
                            val item = JSONObject()
                            val name = cur.getString(pathIndex)
                            val numBytes = cur.getLong(bytes)
                            val numBytesKB = numBytes / 1024 // skip photos below 10 KB in size
                            val index = cur.getLong(id)

                            //Set default values
                            item.put(FOLDER_KEY, "")
                            val dateAdded = cur.getLong(dateAddedIndex)

                            if (isImageFile(name) && numBytesKB > 10) {

                                val dateLong = getDate(dateAdded, 0L, name)
                                if (dateLong != 0L) {

                                    val folderName: String = FileUtil.getFolderName(name)
                                    item.put(FOLDER_KEY, folderName)
                                }

                                val folderName = item[FOLDER_KEY] as String
                                val photosData = PhotosData(
                                    assetPath = name,
                                    folder = folderName,
                                    contentUri = ContentUris.withAppendedId(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        index
                                    ),
                                    createdDate = dateLong
                                )
                                folders.add(folderName)
                                photosList.add(photosData)
                            }

                        } catch (e: JSONException) {
                            Timber.e(e)
                        }

                    } while (cur.moveToNext())
                }
            } finally {
                cur.close()
            }
        }

        val imageAdapterDataList = ArrayList<ImageAdapterData>()
        photosList.forEach {
            imageAdapterDataList.add(ImageAdapterData(it))
        }
        return MediaImporterData(imageAdapterDataList, folders)
    }
}
