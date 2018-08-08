package com.tokopedia.product.manage.item.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import java.util.regex.Matcher
import java.util.regex.Pattern

class YoutubeUtil {

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

        fun convertYoutubeTimeFormattoHHMMSS(youtubetime: String?): String {
            var result: String
            val pattern = "^P(?:(\\d+)D)?T(?:(\\d+)H)?(?:(\\d+)M)?(?:(\\d+))S?$"
            val r = Pattern.compile(pattern)

            val m = r.matcher(youtubetime)
            if (m.find()) {
                val d = m.group(1)
                var hh = m.group(2)
                var mm: String? = m.group(3)
                var ss: String? = m.group(4)
                mm = if (mm != null) mm else "0"
                ss = if (ss != null) ss else "0"
                result = String.format("%02d:%02d", Integer.parseInt(mm), Integer.parseInt(ss))

                if (hh != null) {
                    if(d!=null){
                        hh = ((d.toInt() * 24) + hh.toInt()).toString()
                    }
                    result = "$hh:$result"
                } else if(d!=null){
                    hh = "0"
                    hh = ((d.toInt() * 24) + hh.toInt()).toString()
                    result = "$hh:$result"
                }
            } else {
                result = "00:00"
            }
            return result
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