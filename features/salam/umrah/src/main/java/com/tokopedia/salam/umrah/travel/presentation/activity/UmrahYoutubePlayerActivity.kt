package com.tokopedia.salam.umrah.travel.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import com.tokopedia.salam.umrah.R

class UmrahYoutubePlayerActivity: AppCompatActivity() {

    private var videoUrl : String = ""

    companion object{
        const val EXTRA_YOUTUBE_URL = "EXTRA_YOUTUBE_VIDEO_URL"

        fun createIntent(context: Context, videoUrl: String) =
                Intent(context, UmrahYoutubePlayerActivity::class.java).apply {
                    putExtra(EXTRA_YOUTUBE_URL, videoUrl)
                }
    }

    override fun onCreate(p0: Bundle?) {
        super.onCreate(p0)
        setContentView(R.layout.activity_umrah_youtube_player)
        videoUrl = intent.getStringExtra(EXTRA_YOUTUBE_URL)
        val fragment = fragmentManager.findFragmentById(R.id.youtubeFragment) as YouTubePlayerFragment
        fragment.initialize(getString(R.string.UMRAH_GOOGLE_API_KEY),
                object : YouTubePlayer.OnInitializedListener {
                    override fun onInitializationSuccess(provider: YouTubePlayer.Provider,
                                                         youTubePlayer: YouTubePlayer, b: Boolean) {
                        // do any work here to cue video, play video, etc.
                        youTubePlayer.setFullscreen(true)
                        youTubePlayer.setShowFullscreenButton(true)
                        youTubePlayer.loadVideo(videoUrl)
                    }

                    override fun onInitializationFailure(provider: YouTubePlayer.Provider,
                                                         youTubeInitializationResult: YouTubeInitializationResult) {
                    }
                })
    }
}