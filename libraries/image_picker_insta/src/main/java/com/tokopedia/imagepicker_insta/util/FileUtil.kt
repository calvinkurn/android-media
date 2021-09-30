package com.tokopedia.imagepicker_insta.util

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import timber.log.Timber
import java.io.File

object FileUtil {

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
}