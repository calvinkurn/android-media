package com.tokopedia.chatbot.util

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import java.io.File

object VideoUtil {

    private const val KEY_DURATION = MediaMetadataRetriever.METADATA_KEY_DURATION

    fun retrieveVideoLength(context: Context?, filePath: String): Long {
        val uri = Uri.parse(filePath)

        return try {
            with(MediaMetadataRetriever()) {
                setDataSource(context, uri)
                val durationData = extractMetadata(KEY_DURATION)

                release()

                durationData.toLongOrZero()
            }
        } catch (e: Throwable) {
            0
        }
    }

    fun findVideoSize(videoFile: File) : String {
        return try {
            videoFile.length() / SIZE_KB
        } catch (e: Exception) {
            0
        }.toString()
    }

    fun findVideoExtension(videoFile: File) : String {
        return try {
            videoFile.extension
        } catch (e : Exception) {
            ""
        }
    }

    const val SIZE_KB = 1024

}