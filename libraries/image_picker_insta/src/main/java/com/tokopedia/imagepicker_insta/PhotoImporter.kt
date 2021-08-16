package com.tokopedia.imagepicker_insta

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import android.util.SparseArray
import com.tokopedia.imagepicker_insta.fragment.MainFragment
import com.tokopedia.imagepicker_insta.models.Asset
import com.tokopedia.imagepicker_insta.models.PhotosData
import com.tokopedia.imagepicker_insta.models.PhotosImporterData
import com.tokopedia.imagepicker_insta.util.CursorUtil
import com.tokopedia.imagepicker_insta.util.FileUtil
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class PhotoImporter {
companion object{
    const val ALL = "All"
}
    fun importPhotos(context: Context): PhotosImporterData {
        val photoNames = SparseArray<String>()
        val photoCursor = CursorUtil.getPhotoCursor(context,"",null)
        val photosOnPhone = SparseArray<JSONObject>()
        val data = iteratePhotoCursor(photoCursor, photosOnPhone,photoNames)

        return data
    }

    protected fun iteratePhotoCursor(cur: Cursor?,
                                     photosOnPhone: SparseArray<JSONObject>,
                                     photoNames:SparseArray<String>
    ):PhotosImporterData {
        val photosList = arrayListOf<Asset>()
        val folders = hashSetOf<String>()

        if (cur != null && cur.count > 0) {
            try {
                if (cur.moveToFirst()) {
                    val date = cur.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)
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
                            item.put("nw_st", "")
                            item.put("mt", "")
                            item.put("_ne", "")

                            if (Photo.validName(name) && numBytesKB > 10) {
                                var dateLong = cur.getLong(date)
                                if (dateLong == 0L) {
                                    try {
                                        dateLong = FileUtil.getDateTaken(name)
                                    } catch (e: IOException) {
                                        e.printStackTrace()
                                    }
                                }
                                if (dateLong != 0L) {

//                                    item.put("sa", TimeUtil.getISODate(dateLong))
                                    item.put("i", cur.getInt(id))
                                    item.put("b", numBytes)
                                    photosOnPhone.put(index.toInt(), item)
                                    photoNames.put(index.toInt(), name)
                                    val isOther: Boolean = Photo.isOther(name)
                                    if (isOther) {
                                        item.put("_ne", States.networkType.others.value())
                                    } else {
                                        item.put("_ne", States.networkType.contacts.value())
                                    }
                                    val folderName: String = FileUtil.getFolderName(name)
                                    item.put("nw_st", folderName)
                                    var mediaType: String = States.mediaType.camera.value()
                                    val isRecording = name.toLowerCase().contains("record")
                                    val isGif = name.toLowerCase().endsWith("gif")
                                    val rotate: Int = FileUtil.getCameraPhotoOrientation(name)
                                    if (isGif) {
                                        mediaType = States.mediaType.gif.value()
                                    } else if (rotate == 1) {
                                        mediaType = States.mediaType.portrait.value()
                                    } else if (rotate == 2) {
                                        mediaType = States.mediaType.landscape.value()
                                    } else if (isRecording) {
                                        mediaType = States.mediaType.recording.value()
                                    } else if (isOther) {
                                        mediaType = States.mediaType.others.value()
                                    }
                                    item.put("nw_st", folderName)
                                    item.put("mt", mediaType)
                                }
                            }

                            val folderName = item["nw_st"] as String
                            val photosData = PhotosData(filePath = name,
                            folderName = folderName,
                            mediaType = item["mt"] as String,
                                uri =  ContentUris.withAppendedId(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    index
                                )
                            )
                            folders.add(folderName)
                            photosList.add(photosData)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    } while (cur.moveToNext())
                }
            } finally {
                cur.close()
            }
        }
        val tempFoldersList = ArrayList(folders)
        tempFoldersList.add(0, PhotoImporter.ALL)
        return PhotosImporterData(tempFoldersList,photosList,null)
    }

    protected fun locationValid(latitude: Double, longitude: Double): Boolean? {
        return validLocationPoint(latitude) && validLocationPoint(longitude)
    }

    protected fun validLocationPoint(item: Double): Boolean {
        return !isDoubleEqual(item, 0.toDouble()) && item <= 180 && item >= -180
    }

    fun isDoubleEqual(x1: Double, x2: Double): Boolean {
        return x1.compareTo(x2) == 0
    }
}

class States{
    enum class mediaType(private val value: String) {
        camera("camera"), portrait("portrait"), landscape("landscape"), panorama("panorama"), slowmo("slowmo"), recording("recording"), gif("gif"), others("others"), collage(
            "collage"
        ),
        boomerang("boomerang"), video("video"), wallpaper("wallpaper");

        fun value(): String {
            return value
        }
    }

    enum class networkType(private val value: Int) {
        contacts(0), facebook(1), google(2), instagram(3), twitter(4), dropbox(5), others(6);

        fun value(): Int {
            return value
        }
    }
}

