package com.tokopedia.salam.umrah.travel.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.play.core.splitcompat.SplitCompat
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.tokopedia.keys.Keys
import com.tokopedia.salam.umrah.R
import kotlinx.android.synthetic.main.activity_umrah_youtube_player.*

class UmrahYoutubePlayerActivity: YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener,
YouTubePlayer.PlayerStateChangeListener{

    lateinit var youtubePlayerScreen : YouTubePlayer
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
        umrah_youtube_player.initialize(Keys.AUTH_GOOGLE_YOUTUBE_API_KEY, this)
    }

    override fun onAdStarted() {

    }

    override fun onError(p0: YouTubePlayer.ErrorReason?) {

    }

    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {

    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider?, player: YouTubePlayer?, p2: Boolean) {
        player?.let {
            youtubePlayerScreen = it
            it.setFullscreen(true)
            it.setShowFullscreenButton(true)
            playVideo()
        }
    }

    private fun playVideo(){
        youtubePlayerScreen.loadVideo(videoUrl)
    }

    override fun onLoaded(p0: String?) {

    }

    override fun onLoading() {

    }

    override fun onVideoEnded() {

    }

    override fun onVideoStarted() {

    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(::youtubePlayerScreen.isInitialized)
            youtubePlayerScreen.release()
    }

    override fun onDestroy() {
        if(::youtubePlayerScreen.isInitialized)
            youtubePlayerScreen.release()
        super.onDestroy()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }
}