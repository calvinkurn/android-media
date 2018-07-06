package com.tokopedia.product.edit.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.tokopedia.product.edit.R
import java.util.regex.Matcher
import java.util.regex.Pattern

class YoutubeUtil(val context: Context) {

    val VIDEO_ID_INDEX = 1
    private val YOUTUBE_REGEX = "^(?:https?:\\/\\/)?(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*"
    private val compiledPattern = Pattern.compile(YOUTUBE_REGEX, Pattern.CASE_INSENSITIVE)
    private var youtubeUrl: String? = null
    private lateinit var matcher: Matcher

    companion object {
        fun playYoutubeVideo(context:Context, youtubeID: String){
            val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$youtubeID"))
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=$youtubeID"))
            try {
                context.startActivity(appIntent)
            } catch (e: ActivityNotFoundException) {
                context.startActivity(webIntent)
            }
        }
    }

    fun isValidYoutubeUrl(string: String): Boolean {
        youtubeUrl = string.trim { it <= ' ' }
        matcher = compiledPattern.matcher(youtubeUrl)
        return matcher.find()
    }

    fun getVideoID(): String {
        return matcher.group(VIDEO_ID_INDEX)
    }
}