package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.tokopedia.play.view.fragment.PlayYouTubePlayerFragment
import com.tokopedia.play_common.state.PlayVideoState
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 31/07/20
 */
class YouTubeViewComponent(
        container: ViewGroup,
        @IdRes idRes: Int,
        fragmentManager: FragmentManager,
        private val listener: Listener
) : ViewComponent(container, idRes) {

    private val youtubeFragment =
            fragmentManager.findFragmentByTag(YOUTUBE_FRAGMENT_TAG) as? PlayYouTubePlayerFragment ?: PlayYouTubePlayerFragment().also {
                fragmentManager.beginTransaction()
                        .replace(rootView.id, it, YOUTUBE_FRAGMENT_TAG)
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
            youTubePlayer?.doSafe { setOnFullscreenListener(onFullscreenListener) }
        }

        override fun onVideoEnded() {
            listener.onVideoStateChanged(this@YouTubeViewComponent, PlayVideoState.Ended)
        }

        override fun onError(reason: YouTubePlayer.ErrorReason?) {
            listener.onVideoStateChanged(this@YouTubeViewComponent, PlayVideoState.Error(Throwable("Reason: ${reason?.name ?: YouTubePlayer.ErrorReason.UNKNOWN}")))
        }
    }

    private val playbackEventListener = object : YouTubePlayer.PlaybackEventListener {
        override fun onSeekTo(p0: Int) {
        }

        override fun onBuffering(p0: Boolean) {
            listener.onVideoStateChanged(this@YouTubeViewComponent, PlayVideoState.Buffering)
        }

        override fun onPlaying() {
            listener.onVideoStateChanged(this@YouTubeViewComponent, PlayVideoState.Playing)
        }

        override fun onStopped() {
            listener.onVideoStateChanged(this@YouTubeViewComponent, PlayVideoState.Pause)
        }

        override fun onPaused() {
            listener.onVideoStateChanged(this@YouTubeViewComponent, PlayVideoState.Pause)
        }
    }

    private val onFullscreenListener = object : YouTubePlayer.OnFullscreenListener {
        override fun onFullscreen(isFullscreen: Boolean) {
            if (isFullscreen) listener.onEnterFullscreen(this@YouTubeViewComponent)
            else listener.onExitFullscreen(this@YouTubeViewComponent)
        }
    }

    fun setYouTubeId(youtubeId: String) {
        setYouTubeId(youtubeId, mStartMillis, mShouldPlayOnReady)
    }

    private fun setYouTubeId(youtubeId: String, startMillis: Int, playOnReady: Boolean) {
        if (mVideoId == youtubeId) return

        if (youTubePlayer == null) initYouTubePlayer()

        mVideoId = youtubeId
        mStartMillis = startMillis
        mShouldPlayOnReady = playOnReady
        playYouTubeFromId(youtubeId, startMillis)
    }

    fun release() {
        mVideoId = null
        pause()
        youTubePlayer?.doSafe { release() }
        youTubePlayer = null
    }

    fun setFullScreenButton(isFullscreen: Boolean) {
        this.isFullscreen = isFullscreen
        youTubePlayer?.doSafe { setFullscreen(isFullscreen) }
    }

    fun play() {
        youTubePlayer?.doSafe { play() }
    }

    fun pause() {
        youTubePlayer?.doSafe { pause() }
    }

    fun getPlayer(): YouTubePlayer? = youTubePlayer

    private fun initYouTubePlayer() {
        youtubeFragment.initialize(object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(provider: YouTubePlayer.Provider?, player: YouTubePlayer?, wasRestored: Boolean) {
                player?.let {
                    youTubePlayer = configYouTubePlayer(it)
                    mVideoId?.let { videoId -> playYouTubeFromId(videoId, mStartMillis) }
                }
            }

            override fun onInitializationFailure(provider: YouTubePlayer.Provider?, error: YouTubeInitializationResult?) {
                listener.onInitFailure(this@YouTubeViewComponent, error ?: YouTubeInitializationResult.UNKNOWN_ERROR)
            }
        })
    }

    private fun playYouTubeFromId(youtubeId: String, startMillis: Int) {
        youTubePlayer?.doSafe { loadVideo(youtubeId, startMillis) }
    }

    private fun configYouTubePlayer(player: YouTubePlayer) : YouTubePlayer {
        player.setPlayerStateChangeListener(playerStateChangedListener)
        player.setPlaybackEventListener(playbackEventListener)
        player.fullscreenControlFlags = YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT
        player.setFullscreen(isFullscreen)
        return player
    }

    private fun getCurrentPosition(): Int {
        return youTubePlayer?.doSafe { currentTimeMillis } ?: 0
    }

    private fun isPlaying(): Boolean {
        return youTubePlayer?.doSafe { isPlaying } ?: true
    }

    /**
     * Lifecycle Event
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        release()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        mStartMillis = getCurrentPosition()
        mShouldPlayOnReady = isPlaying()
        release()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        mVideoId?.let { setYouTubeId(it, mStartMillis, mShouldPlayOnReady) }
    }

    companion object {
        private const val YOUTUBE_FRAGMENT_TAG = "fragment_youtube"
    }

    interface Listener {
        fun onInitFailure(view: YouTubeViewComponent, result: YouTubeInitializationResult)

        fun onEnterFullscreen(view: YouTubeViewComponent)
        fun onExitFullscreen(view: YouTubeViewComponent)

        fun onVideoStateChanged(view: YouTubeViewComponent, state: PlayVideoState)
    }

    private fun <R: Any?> YouTubePlayer.doSafe(doHandler: YouTubePlayer.() -> R): R? {
        return try { doHandler() } catch (e: Throwable) { null }
    }
}