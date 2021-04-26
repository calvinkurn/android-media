package com.tokopedia.shop.score.performance.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import com.tokopedia.shop.score.R
import com.tokopedia.youtubeutils.common.YoutubePlayerConstant

class ShopPerformanceYoutubeActivity : AppCompatActivity(), YouTubePlayer.OnInitializedListener {

    companion object {
        private const val EXTRA_YOUTUBE_VIDEO_ID_SHOP_PAGE = "EXTRA_YOUTUBE_VIDEO_ID_SHOP_PERFORMANCE"
        private const val FULL_SCREEN_CONTROL_FLAGS_LANDSCAPE = 1

        fun createIntent(context: Context, videoUrl: String) = Intent(context, ShopPerformanceYoutubeActivity::class.java).apply {
            putExtra(EXTRA_YOUTUBE_VIDEO_ID_SHOP_PAGE, videoUrl)
        }
    }

    private var toolbar: Toolbar? = null
    private var isFullScreen = false
    private var videoUrl: String? = null
    private var youtubePlayerScreen: YouTubePlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_performance_page_youtube_player)
        window?.decorView?.setBackgroundColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N700))

        toolbar = findViewById(R.id.toolbarVideo)

        setupActionBar()

        videoUrl = intent?.getStringExtra(EXTRA_YOUTUBE_VIDEO_ID_SHOP_PAGE)

        val youtubePlayerFragment = supportFragmentManager.findFragmentById(R.id.youtube_player_fragment) as? YouTubePlayerFragment
        youtubePlayerFragment?.initialize(YoutubePlayerConstant.GOOGLE_API_KEY, this)
    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider?, player: YouTubePlayer?, p2: Boolean) {
        player?.let {
            youtubePlayerScreen = it
            it.fullscreenControlFlags = FULL_SCREEN_CONTROL_FLAGS_LANDSCAPE
            it.setOnFullscreenListener { b ->
                isFullScreen = b
            }
            playVideo()
        }
    }

    override fun onInitializationFailure(provider: YouTubePlayer.Provider?, youTubeInitializationResult: YouTubeInitializationResult?) {}

    override fun onBackPressed() {
        super.onBackPressed()
        youtubePlayerScreen?.release()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        youtubePlayerScreen?.release()
        super.onDestroy()
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setDisplayShowTitleEnabled(true)
            setHomeAsUpIndicator(com.tokopedia.videoplayer.R.drawable.ic_video_close)
            title = getString(R.string.close_youtube)
        }
    }

    private fun playVideo() {
        youtubePlayerScreen?.cueVideo(videoUrl)
    }
}