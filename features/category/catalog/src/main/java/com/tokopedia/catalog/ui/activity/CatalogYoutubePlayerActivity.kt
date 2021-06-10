package com.tokopedia.catalog.ui.activity


import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.tokopedia.catalog.R
import com.tokopedia.keys.Keys
import kotlinx.android.synthetic.main.activity_catalog_youtube_player.*

class CatalogYoutubePlayerActivity: YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener,
        YouTubePlayer.PlayerStateChangeListener{

    private var selectedIndex: Int = 0
    private var videoUrls = listOf<String>()

    lateinit var youtubePlayerScreen: YouTubePlayer

    companion object {
        private const val EXTRA_YOUTUBE_VIDEO_DATA = "EXTRA_YOUTUBE_VIDEO_DATA"
        private const val EXTRA_YOUTUBE_VIDEO_INDEX = "EXTRA_YOUTUBE_VIDEO_INDEX"

        fun createIntent(context: Context, videoUrls: List<String>, selectedIndex: Int)
                = Intent(context, CatalogYoutubePlayerActivity::class.java).apply {
            putExtra(EXTRA_YOUTUBE_VIDEO_INDEX, selectedIndex)
            putExtra(EXTRA_YOUTUBE_VIDEO_DATA, videoUrls.toTypedArray())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog_youtube_player)

        selectedIndex = intent.getIntExtra(EXTRA_YOUTUBE_VIDEO_INDEX, 0)
        videoUrls = intent.getStringArrayExtra(EXTRA_YOUTUBE_VIDEO_DATA).asList()
        youtube_player_main.initialize(Keys.AUTH_GOOGLE_YOUTUBE_API_KEY, this)
    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider?, player: YouTubePlayer?, p2: Boolean) {
        player?.let {
            youtubePlayerScreen = it
            if (videoUrls.size > 1){
                it.setPlayerStateChangeListener(this)
            } else {
                it.setFullscreen(true)
                it.setShowFullscreenButton(true)
            }
            playVideoAt(selectedIndex)
        }

    }

    private fun playVideoAt(selectedIndex: Int) {
        if (::youtubePlayerScreen.isInitialized) {
            try {
                val splitted = videoUrls[selectedIndex].split("v=")
                youtubePlayerScreen.loadVideo(splitted[1])
                this.selectedIndex = selectedIndex
            } catch (e: Throwable) {
            }
        }
    }

    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {}

    override fun onBackPressed() {
        super.onBackPressed()
        if (::youtubePlayerScreen.isInitialized)
            youtubePlayerScreen.release()
    }

    override fun onDestroy() {
        if (::youtubePlayerScreen.isInitialized)
            youtubePlayerScreen.release()
        super.onDestroy()
    }

    override fun onAdStarted() {}

    override fun onLoading() {}

    override fun onVideoStarted() {}

    override fun onLoaded(p0: String?) {

    }

    override fun onVideoEnded() {
        if (selectedIndex + 1 < videoUrls.size){
            playVideoAt(selectedIndex + 1)
        }
    }

    override fun onError(p0: YouTubePlayer.ErrorReason?) {}
}