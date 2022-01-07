package com.tokopedia.imagepicker.media_picker.utils

import android.media.MediaMetadataRetriever
import android.webkit.MimeTypeMap
import java.text.SimpleDateFormat
import java.util.*

object MediaUtils {

    /* check file from given path is image or video, if image will return true, otherwise false */
    fun isImage(path: String): Boolean {
        val extension = MimeTypeMap.getFileExtensionFromUrl(path)
        val mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            ?: throw Exception("mime can't be null")
        return mime.contains("image")
    }

    /* get duration if the given path is video */
    fun getDurationVideoInMillis(path: String): Long {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(path)
        val time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        retriever.release()
        return time?.toLong() ?: 0
    }

    /* convert duration in millis to formar mm:ss */
    fun Long.convertMsToTimeFormat(): String {
        val date = Date(this)
        val formatter = SimpleDateFormat("mm:ss", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        return formatter.format(date)
    }

}