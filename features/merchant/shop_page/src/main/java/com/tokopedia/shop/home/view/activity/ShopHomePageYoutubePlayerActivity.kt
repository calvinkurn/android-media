package com.tokopedia.shop.home.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ActivityShopHomePageYoutubePlayerBinding
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.youtubeutils.common.YoutubePlayerConstant

/**
 * Created by rizqiaryansa on 2020-03-02.
 */

class ShopHomePageYoutubePlayerActivity : AppCompatActivity(), YouTubePlayer.OnInitializedListener {

    companion object {
        private const val EXTRA_YOUTUBE_VIDEO_ID_SHOP_PAGE = "EXTRA_YOUTUBE_VIDEO_ID_SHOP_PAGE"
        private const val FULL_SCREEN_CONTROL_FLAGS_LANDSCAPE = 1

        fun createIntent(context: Context, videoUrl: String) = Intent(context, ShopHomePageYoutubePlayerActivity::class.java).apply {
            putExtra(EXTRA_YOUTUBE_VIDEO_ID_SHOP_PAGE, videoUrl)
        }
    }

    private var toolbar: Toolbar? = null
    private var isFullScreen = false
    private var videoUrl: String? = null
    private var youtubePlayerScreen: YouTubePlayer? = null
    private val viewBinding: ActivityShopHomePageYoutubePlayerBinding? by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_home_page_youtube_player)

        toolbar = viewBinding?.toolbarVideo

        setupActionBar()

        videoUrl = intent.getStringExtra(EXTRA_YOUTUBE_VIDEO_ID_SHOP_PAGE)

        val youtubePlayerFragment = supportFragmentManager.findFragmentById(R.id.youtube_fragment) as YouTubePlayerSupportFragment
        youtubePlayerFragment.initialize(YoutubePlayerConstant.GOOGLE_API_KEY, this)
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
            setHomeAsUpIndicator(R.drawable.ic_video_player_close)
            title = getString(R.string.close_youtube)
        }
    }

    private fun playVideo() {
        youtubePlayerScreen?.cueVideo(videoUrl)
    }
}
