package com.tokopedia.product.detail.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.tokopedia.keys.Keys
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.common.data.model.product.YoutubeVideo
import com.tokopedia.product.detail.databinding.ActivityProductYoutubePlayerBinding
import com.tokopedia.product.detail.view.adapter.YoutubeThumbnailAdapter
import timber.log.Timber

class ProductYoutubePlayerActivity: YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener,
    YouTubePlayer.PlayerStateChangeListener{

    private var selectedIndex: Int = 0
    private var videoUrls = listOf<String>()

    lateinit var youtubePlayerScreen: YouTubePlayer
    private var binding: ActivityProductYoutubePlayerBinding? = null

    companion object {
        private const val EXTRA_YOUTUBE_VIDEO_DATA = "EXTRA_YOUTUBE_VIDEO_DATA"
        private const val EXTRA_YOUTUBE_VIDEO_INDEX = "EXTRA_YOUTUBE_VIDEO_INDEX"

        fun createIntent(context: Context, videoUrls: List<String>, selectedIndex: Int)
                = Intent(context, ProductYoutubePlayerActivity::class.java).apply {
                        putExtra(EXTRA_YOUTUBE_VIDEO_INDEX, selectedIndex)
                        putExtra(EXTRA_YOUTUBE_VIDEO_DATA, videoUrls.toTypedArray())
                    }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductYoutubePlayerBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        selectedIndex = intent.getIntExtra(EXTRA_YOUTUBE_VIDEO_INDEX, 0)
        videoUrls = intent.getStringArrayExtra(EXTRA_YOUTUBE_VIDEO_DATA)?.asList() ?: listOf()

        binding?.youtubeList?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        if (videoUrls.size > 1){
            binding?.youtubeList?.adapter = YoutubeThumbnailAdapter(videoUrls.map { YoutubeVideo(url = it) }.toMutableList()){ _, index -> playVideoAt(index)
            }
        }
        binding?.youtubePlayerMain?.initialize(Keys.AUTH_GOOGLE_YOUTUBE_API_KEY, this)
    }

    override fun onSaveInstanceState(p0: Bundle) {
        /**
         * this is comment refer to https://issuetracker.google.com/issues/35172585#comment32
         * when onCreate, KEY_PLAYER_VIEW_STATE start from scratch
         */
        // super.onSaveInstanceState(p0)
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
                youtubePlayerScreen.loadVideo(videoUrls[selectedIndex])
                this.selectedIndex = selectedIndex
            } catch (_: Throwable) {
            }
        }
    }

    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v=" + videoUrls[selectedIndex])))
            finish()
        } catch (e: Throwable) {
            Timber.d(e)
        }
    }

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
        binding?.youtubeList?.visible()
    }

    override fun onVideoEnded() {
        if (selectedIndex + 1 < videoUrls.size){
            playVideoAt(selectedIndex + 1)
        }
    }

    override fun onError(p0: YouTubePlayer.ErrorReason?) {}
}