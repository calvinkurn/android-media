package com.tokopedia.imagepicker_insta.mediaImporter

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.SparseArray
import com.tokopedia.imagepicker_insta.Photo
import com.tokopedia.imagepicker_insta.models.*
import com.tokopedia.imagepicker_insta.util.CursorUtil
import com.tokopedia.imagepicker_insta.util.FileUtil
import com.tokopedia.imagepicker_insta.util.StorageUtil
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.io.File
import java.io.IOException

class PhotoImporter:MediaImporter{
    companion object {
        const val ALL = "All"
        const val FOLDER_KEY = "nw_st"
    }

    override fun importMediaFromInternalDir(context: Context): ArrayList<Asset> {
        val file = File(context.filesDir, StorageUtil.INTERNAL_FOLDER_NAME)
        val photosDataList = arrayListOf<Asset>()
        if (file.isDirectory) {
            file.listFiles()?.forEach {
                val filePath = it.absolutePath
                val isFileSupported = (filePath.endsWith(".jpg") ||
                        filePath.endsWith(".jpeg") ||
                        filePath.endsWith(".png") ||
                        filePath.endsWith(".webP"))

                if (isFileSupported) {
                    val photoData = PhotosData(
                        filePath,
                        StorageUtil.INTERNAL_FOLDER_NAME,
                        Uri.fromFile(it),
                        it.lastModified()
                    )
                    photosDataList.add(photoData)
                }
            }
        }
        return photosDataList
    }

    override fun importMedia(context: Context): MediaImporterData {
        val photoNames = SparseArray<String>()
        val photoCursor = CursorUtil.getPhotoCursor(context, "", null)
        val photosOnPhone = SparseArray<JSONObject>()
        val data = iteratePhotoCursor(photoCursor, photosOnPhone, photoNames)

        return data
    }

    protected fun iteratePhotoCursor(
        cur: Cursor?,
        photosOnPhone: SparseArray<JSONObject>,
        photoNames: SparseArray<String>
    ): MediaImporterData {
        val photosList = arrayListOf<Asset>()
        val folders = hashSetOf<String>()

        if (cur != null && cur.count > 0) {
            try {
                if (cur.moveToFirst()) {
                    val dateTakenIndex = cur.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN)
                    val dateAddedIndex = cur.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)
                    val path = cur.getColumnIndex(MediaStore.Images.Media.DATA)
                    val id = cur.getColumnIndex(MediaStore.Images.Media._ID)
                    val bytes = cur.getColumnIndex(MediaStore.Images.Media.SIZE)
                    do {
                        try {
                            val item = JSONObject()
                            val name = cur.getString(path)
                            val numBytes = cur.getLong(bytes)
                            val numBytesKB = numBytes / 1024 // skip photos below 10 KB in size
                            val index = cur.getLong(id)

                            //Set default values
                            item.put(FOLDER_KEY, "")
                            val dateAdded = cur.getLong(dateAddedIndex)
                            val dateTaken = cur.getLong(dateTakenIndex)


                            if (Photo.validName(name) && numBytesKB > 10) {

                                val dateLong = getDate(dateAdded, 0L, name)
                                if (dateLong != 0L) {
                                    photosOnPhone.put(index.toInt(), item)
                                    photoNames.put(index.toInt(), name)

                                    val folderName: String = FileUtil.getFolderName(name)
                                    item.put(FOLDER_KEY, folderName)
                                }

                                val folderName = item[FOLDER_KEY] as String
                                val photosData = PhotosData(
                                    filePath = name,
                                    folderName = folderName,
                                    uri = ContentUris.withAppendedId(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        index
                                    ),
                                    _createdDate = dateLong
                                )
                                folders.add(folderName)
                                photosList.add(photosData)
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
        photosList.forEach {
            imageAdapterDataList.add(ImageAdapterData(it))
        }
        return MediaImporterData(imageAdapterDataList, folders)
    }
}
