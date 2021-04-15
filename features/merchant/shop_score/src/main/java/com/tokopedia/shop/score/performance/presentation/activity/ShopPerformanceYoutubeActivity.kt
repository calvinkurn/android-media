package com.tokopedia.shop.score.performance.presentation.activity

import android.content.Context
import android.content.Intent
import com.tokopedia.youtubeutils.activity.YoutubePlayerActivity

class ShopPerformanceYoutubeActivity : YoutubePlayerActivity() {

    private var videoUrl = ""

    override fun getVideoUrl(): String {
        videoUrl = intent?.getStringExtra(EXTRA_YOUTUBE_VIDEO_URL) ?: ""
        return videoUrl
    }

    companion object {
        const val EXTRA_YOUTUBE_VIDEO_URL = "EXTRA_YOUTUBE_VIDEO_URL"

        fun createInstance(context: Context?, videoId: String): Intent {
            val intent = Intent(context, ShopPerformanceYoutubeActivity::class.java)
            intent.putExtra(EXTRA_YOUTUBE_VIDEO_URL, videoId)
            return intent
        }
    }
}