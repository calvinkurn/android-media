package com.tokopedia.chatbot.util

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import java.io.File

object VideoUtil {

    fun findVideoLength(context: Context?, filePath: String): Long {
        var videoLength: Long = 0
        return try {
            val uri = Uri.parse(filePath)
            MediaPlayer.create(context, uri).also {
                videoLength = it.duration.toLong()
                it.reset()
                it.release()
            }
            videoLength
        } catch (e : Exception) {
            videoLength
        }

        return videoLength
    }

    fun findVideoSize(videoFile: File) : String {
        return try {
            videoFile.length() / 1024
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

}