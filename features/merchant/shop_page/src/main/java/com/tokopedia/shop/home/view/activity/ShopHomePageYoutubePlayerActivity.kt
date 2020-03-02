package com.tokopedia.shop.home.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import com.tokopedia.shop.R

/**
 * Created by rizqiaryansa on 2020-03-02.
 */

class ShopHomePageYoutubePlayerActivity: AppCompatActivity(), YouTubePlayer.OnInitializedListener {

    companion object {
        private const val EXTRA_YOUTUBE_VIDEO_ID_SHOP_PAGE = "EXTRA_YOUTUBE_VIDEO_ID_SHOP_PAGE"

        fun createIntent(context: Context, videoUrls: String) = Intent(context, ShopHomePageYoutubePlayerActivity::class.java).apply {
            putExtra(EXTRA_YOUTUBE_VIDEO_ID_SHOP_PAGE, videoUrls)
        }
    }

    private var isFullScreen = false
    private var videoUrls: String? = null
    private lateinit var youtubePlayerScreen: YouTubePlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_home_page_youtube_player)

        videoUrls = intent.getStringExtra(EXTRA_YOUTUBE_VIDEO_ID_SHOP_PAGE)

        val frag = supportFragmentManager.findFragmentById(R.id.youtube_player_main_shop_page) as YouTubePlayerSupportFragment
        frag.initialize(getString(R.string.GOOGLE_API_KEY), this)
    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider?, player: YouTubePlayer?, p2: Boolean) {
        player?.let {
            youtubePlayerScreen = it
            it.fullscreenControlFlags = 1
            it.setOnFullscreenListener { b ->
                isFullScreen = b
            }
            playVideo()
        }
    }

    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {}

    override fun onBackPressed() {
        super.onBackPressed()
        if (::youtubePlayerScreen.isInitialized)
            youtubePlayerScreen.release()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::youtubePlayerScreen.isInitialized)
            youtubePlayerScreen.release()
    }

    private fun playVideo() {
        youtubePlayerScreen.loadVideo(videoUrls)
    }
}