package com.tokopedia.shop.score.performance.presentation.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import com.tokopedia.youtubeutils.activity.YoutubePlayerActivity

class ShopPerformanceYoutubeActivity: YoutubePlayerActivity() {

    private var videoUrl = ""

    override fun getVideoUrl(): String {
        videoUrl = intent?.getStringExtra(EXTRA_YOUTUBE_VIDEO_URL) ?: ""
        return videoUrl
    }

    companion object {
        private const val YOUTUBE_DEFAULT_PREFIX = "https://www.youtube.com/watch?v="
        const val EXTRA_YOUTUBE_VIDEO_URL = "EXTRA_YOUTUBE_VIDEO_URL"

        fun createInstance(context: Context?, videoId: String): Intent {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(YOUTUBE_DEFAULT_PREFIX + videoId)
                intent
            } else {
                val intent = Intent(context, ShopPerformanceYoutubeActivity::class.java)
                intent.putExtra(EXTRA_YOUTUBE_VIDEO_URL, videoId)
                intent
            }
        }
    }
}