package com.tokopedia.imagepicker_insta.util

import android.media.ExifInterface
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object FileUtil {

    fun getFolderName(path: String?): String {
        if (path == null || path.isEmpty()) {
            return "Others"
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


    @Throws(IOException::class)
    fun getDateTaken(path: String?): Long {
        var ret: Long = 0
        if(!path.isNullOrEmpty()) {
            val exif = ExifInterface(path)
            val dateTime = exif.getAttribute(ExifInterface.TAG_DATETIME)
            ret = parseDateTaken(dateTime)
        }
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