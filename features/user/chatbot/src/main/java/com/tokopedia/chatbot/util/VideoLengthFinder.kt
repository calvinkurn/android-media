package com.tokopedia.chatbot.util

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

object VideoLengthFinder {

    fun findVideoLength(context: Context?, filePath: String): Long {
        var videoLength: Long = 0

        var uri = Uri.parse(filePath)
        MediaPlayer.create(context, uri).also {
            videoLength = it.duration.toLong()
            it.reset()
            it.release()
        }
        return videoLength
    }

}