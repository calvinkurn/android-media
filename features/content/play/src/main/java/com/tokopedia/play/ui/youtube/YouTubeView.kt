package com.tokopedia.play.ui.youtube

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView
import com.tokopedia.play.view.fragment.PlayYouTubePlayerFragment
import com.tokopedia.play_common.state.PlayVideoState

/**
 * Created by jegul on 27/04/20
 */
class YouTubeView(
        container: ViewGroup,
        fragmentManager: FragmentManager,
        private val listener: Listener
) : UIView(container) {

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_youtube, container, true)
                    .findViewById(R.id.fl_youtube_player)

    private val youtubeFragment =
            fragmentManager.findFragmentByTag(YOUTUBE_FRAGMENT_TAG) as? PlayYouTubePlayerFragment ?: PlayYouTubePlayerFragment().also {
                fragmentManager.beginTransaction()
                        .replace(view.id, it, YOUTUBE_FRAGMENT_TAG)
                        .commit()
            }

    private var youTubePlayer: YouTubePlayer? = null
    private var mVideoId: String? = null
    private var mStartMillis: Int = 0
    private var mShouldPlayOnReady = true

    private var isFullscreen = false

    private val playerStateChangedListener = object : YouTubePlayer.PlayerStateChangeListener {
        override fun onAdStarted() {
        }

        override fun onLoading() {
        }

        override fun onVideoStarted() {
        }

        override fun onLoaded(videoId: String?) {
            if (mShouldPlayOnReady) play() else pause()
            youTubePlayer?.setOnFullscreenListener(onFullscreenListener)
        }

        override fun onVideoEnded() {
            listener.onVideoStateChanged(this@YouTubeView, PlayVideoState.Ended)
        }

        override fun onError(reason: YouTubePlayer.ErrorReason?) {
            listener.onVideoStateChanged(this@YouTubeView, PlayVideoState.Error(Throwable("Reason: ${reason?.name ?: YouTubePlayer.ErrorReason.UNKNOWN}")))
        }
    }

    private val playbackEventListener = object : YouTubePlayer.PlaybackEventListener {
        override fun onSeekTo(p0: Int) {
        }

        override fun onBuffering(p0: Boolean) {
            listener.onVideoStateChanged(this@YouTubeView, PlayVideoState.Buffering)
        }

        override fun onPlaying() {
            listener.onVideoStateChanged(this@YouTubeView, PlayVideoState.Playing)
        }

        override fun onStopped() {
            listener.onVideoStateChanged(this@YouTubeView, PlayVideoState.Pause)
        }

        override fun onPaused() {
            listener.onVideoStateChanged(this@YouTubeView, PlayVideoState.Pause)
        }
    }

    private val onFullscreenListener = object : YouTubePlayer.OnFullscreenListener {
        override fun onFullscreen(isFullscreen: Boolean) {
            if (isFullscreen) listener.onEnterFullscreen(this@YouTubeView)
            else listener.onExitFullscreen(this@YouTubeView)
        }
    }

    override val containerId: Int = view.id

    override fun show() {
        view.show()
    }

    override fun hide() {
        view.hide()
    }

    internal fun setYouTubeId(youtubeId: String, startMillis: Int, playOnReady: Boolean) {
        if (mVideoId == youtubeId) return

        if (youTubePlayer == null) initYouTubePlayer()

        mVideoId = youtubeId
        mStartMillis = startMillis
        mShouldPlayOnReady = playOnReady
        playYouTubeFromId(youtubeId, startMillis)
    }

    internal fun release() {
        mVideoId = null
        youTubePlayer?.release()
        youTubePlayer = null
    }

    internal fun getCurrentPosition(): Int {
        return youTubePlayer?.currentTimeMillis ?: 0
    }

    internal fun setFullScreenButton(isFullscreen: Boolean) {
        this.isFullscreen = isFullscreen
        youTubePlayer?.setFullscreen(isFullscreen)
    }

    internal fun isPlaying(): Boolean? {
        return youTubePlayer?.isPlaying
    }

    internal fun play() {
        youTubePlayer?.play()
    }

    internal fun pause() {
        youTubePlayer?.pause()
    }

    internal fun getPlayer(): YouTubePlayer? = youTubePlayer

    private fun initYouTubePlayer() {
        youtubeFragment.initialize(object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(provider: YouTubePlayer.Provider?, player: YouTubePlayer?, wasRestored: Boolean) {
                player?.let {
                    youTubePlayer = configYouTubePlayer(it)
                    mVideoId?.let { videoId -> playYouTubeFromId(videoId, mStartMillis) }
                }
            }

            override fun onInitializationFailure(provider: YouTubePlayer.Provider?, error: YouTubeInitializationResult?) {
                listener.onInitFailure(this@YouTubeView, error ?: YouTubeInitializationResult.UNKNOWN_ERROR)
            }
        })
    }

    private fun playYouTubeFromId(youtubeId: String, startMillis: Int) {
        youTubePlayer?.loadVideo(youtubeId, startMillis)
    }

    private fun configYouTubePlayer(player: YouTubePlayer) : YouTubePlayer {
        player.setPlayerStateChangeListener(playerStateChangedListener)
        player.setPlaybackEventListener(playbackEventListener)
        player.fullscreenControlFlags = YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT
        player.setFullscreen(isFullscreen)
        return player
    }

    companion object {
        private const val YOUTUBE_FRAGMENT_TAG = "fragment_youtube"
    }

    interface Listener {
        fun onInitFailure(view: YouTubeView, result: YouTubeInitializationResult)

        fun onEnterFullscreen(view: YouTubeView)
        fun onExitFullscreen(view: YouTubeView)

        fun onVideoStateChanged(view: YouTubeView, state: PlayVideoState)
    }
}