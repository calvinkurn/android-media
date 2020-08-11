package com.tokopedia.kol.feature.post.view.activity

import android.os.Bundle
import com.tokopedia.youtubeutils.activity.YoutubePlayerActivity

private const val DEFAULT_YOUTUBE_URL = ""
class KolPostYouTubeActivity : YoutubePlayerActivity() {
    private var youtubeUrl: String = DEFAULT_YOUTUBE_URL

    override fun getVideoUrl(): String {
        return youtubeUrl
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getDataFromIntent()
        super.onCreate(savedInstanceState)
    }

    private fun getDataFromIntent() {
        intent.data?.let {
            youtubeUrl = it.lastPathSegment ?: DEFAULT_YOUTUBE_URL
        }
    }
}