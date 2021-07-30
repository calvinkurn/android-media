package com.tokopedia.imagepicker_insta.util

import android.media.ExifInterface
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object FileUtil {

    private val DCIM_PATH = (Environment.getExternalStorageDirectory().absolutePath
            + "/" + Environment.DIRECTORY_DCIM)
    val IMAGE_PATH = "$DCIM_PATH/"

    fun getFolderName(path: String?): String {
        if (path == null || path.isEmpty()) {
            return "Others"
        }
        if (path.endsWith("gif")) {
            return "GIF"
        } else if (path.toLowerCase().contains("whatsapp")) {
            return "WhatsApp"
        } else if (path.toLowerCase().contains("instagram")) {
            return "Instagram"
        } else if (path.toLowerCase().contains("facebook")) {
            return "Facebook"
        } else if (path.toLowerCase().contains("download")) {
            return "Download"
        } else if (path.toLowerCase().contains("screenshot")) {
            return "Screenshots"
        } else if (path.toLowerCase().contains("hike")) {
            return "Hike"
        } else if (path.toLowerCase().contains("shareit")) {
            return "Share It"
        } else if (path.toLowerCase().contains("gmail")) {
            return "Gmail"
        } else if (path.toLowerCase().contains("record")) {
            return "Recordings"
        }
        val file = File(path)
        if (file.exists()) {
            val parent = file.parentFile
            if (parent != null && parent.isDirectory) {
                val folderName = parent.name
                return if (folderName == "/") {
                    "Others"
                } else folderName
            }
        }
        return "Others"
    }

    fun getCameraPhotoOrientation(imagePath: String?): Int {
        var rotate = 0
        try {
//            context.getContentResolver().notifyChange(imageUri, null);
            val imageFile = File(imagePath)
            if (!imageFile.exists()) {
                return -1
            }
            val exif = ExifInterface(imageFile.absolutePath)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            val width = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0)
            val height = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
                ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
                ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
            }
            Log.d("RotateImage", "Exif orientation: $orientation")
            Log.d("RotateImage", "Rotate value: $rotate")
            if (rotate == 90 || rotate == 270) {
                Log.d("RotateImage", "Portrait Image")
                return 1
            } else if (rotate == 0) {
                return if (width > height) {
                    Log.d("RotateImage", "Landscape Image")
                    2
                } else {
                    Log.d("RotateImage", "Portrait Image 2")
                    1
                }
            }
            return -1
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return rotate
    }

    fun getDownloadPath(): String {
        val dirpath: String = IMAGE_PATH
        val dir = File(dirpath)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dirpath
    }
    @Throws(IOException::class)
    fun getDateTaken(path: String?): Long {
        var ret: Long = 0
        val exif = ExifInterface(path)
        val dateTime = exif.getAttribute(ExifInterface.TAG_DATETIME)
        ret = parseDateTaken(dateTime)
        return ret
    }

    private fun parseDateTaken(dateTime: String?): Long {
        var ret: Long = 0
        val dateParser: SimpleDateFormat = SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.US)
        dateParser.timeZone = TimeZone.getTimeZone("GMT")
        try {
            ret = dateParser.parse(dateTime).time
        } catch (e: Exception) {

        }
        return ret
    }
}