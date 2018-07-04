package com.tokopedia.product.edit.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.tokopedia.product.edit.R
import java.util.regex.Pattern

class YoutubeUtil(val context: Context) {

    val VIDEO_ID_INDEX = 1
    private val YOUTUBE_REGEX = "^(?:https?:\\/\\/)?(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*"
    private val compiledPattern = Pattern.compile(YOUTUBE_REGEX, Pattern.CASE_INSENSITIVE)
    private var youtubeUrl: String? = null

    companion object {
        @JvmStatic
        fun playYoutubeVideo(context:Context, youtubeID: String){

            val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + youtubeID))
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + youtubeID))
            try {
                context.startActivity(appIntent)
            } catch (e: ActivityNotFoundException) {
                context.startActivity(webIntent)
            }

        }

        @JvmStatic
        fun playYoutubeVideoURL(context:Context, youtubeURL: String){
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeURL))
            context.startActivity(webIntent)
        }
    }

    fun setYoutubeUrl(youtubeUrl: String?) {
        if (youtubeUrl == null || youtubeUrl.isEmpty()) {
            throw IllegalArgumentException(context.getString(R.string.product_error_no_video_url_name))
        }

        this.youtubeUrl = youtubeUrl.trim { it <= ' ' }
    }

    private fun isValidYoutubeUrl(): Pair<Boolean, String> {
        val matcher = compiledPattern.matcher(youtubeUrl)
        return if (matcher.find()) {
            Pair(true, matcher.group(VIDEO_ID_INDEX))
        } else {
            Pair(false, context.getString(R.string.product_invalid_video_url))
        }
    }

    fun saveVideoID(): String {
        val validYoutubeUrl = isValidYoutubeUrl()
        return if (validYoutubeUrl.model1!!) {
            validYoutubeUrl.model2!!
        } else {
            throw IllegalArgumentException(validYoutubeUrl.model2)
        }
    }

    class Pair<E, F> {
        var model1: E? = null
        var model2: F? = null

        constructor() {
            this.model1 = null
            this.model2 = null
        }

        constructor(model1: E, model2: F) {
            this.model1 = model1
            this.model2 = model2
        }

        override fun toString(): String {
            return "Pair{" +
                    "model1=" + model1 +
                    ", model2=" + model2 +
                    '}'.toString()
        }
    }
}