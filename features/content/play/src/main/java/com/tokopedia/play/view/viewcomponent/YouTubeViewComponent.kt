package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.tokopedia.play.util.video.state.BufferSource
import com.tokopedia.play.util.video.state.PlayViewerVideoState
import com.tokopedia.play.view.fragment.PlayYouTubePlayerFragment
import com.tokopedia.play_common.viewcomponent.ViewComponent
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by jegul on 31/07/20
 */
class YouTubeViewComponent(
        private val container: ViewGroup,
        @IdRes idRes: Int,
        private val fragmentManager: FragmentManager,
        private val dataSource: DataSource,
        private val listener: Listener
) : ViewComponent(container, idRes) {

    private var isAlreadyInit: AtomicBoolean = AtomicBoolean(false)

    private var youtubeFragment: PlayYouTubePlayerFragment? = null

    private var youTubePlayer: YouTubePlayer? = null
    private var mVideoId: String? = null
    private var mStartMillis: Int = 0
    private var mShouldPlayOnReady = true

    private var mIsFullscreen = false
    private var mIsInternalOrientationChanged = false

    private val playerStateChangedListener = object : YouTubePlayer.PlayerStateChangeListener {
        override fun onAdStarted() {
        }

        override fun onLoading() {
        }

        override fun onVideoStarted() {
        }

        override fun onLoaded(videoId: String?) {
            if (mShouldPlayOnReady && dataSource.isEligibleToPlay(this@YouTubeViewComponent)) play() else pause()
            youTubePlayer?.doSafe { setOnFullscreenListener(onFullscreenListener) }
        }

        override fun onVideoEnded() {
            listener.onVideoStateChanged(this@YouTubeViewComponent, PlayViewerVideoState.End)
        }

        override fun onError(reason: YouTubePlayer.ErrorReason?) {
            listener.onVideoStateChanged(this@YouTubeViewComponent, PlayViewerVideoState.Error(Throwable("Reason: ${reason?.name ?: YouTubePlayer.ErrorReason.UNKNOWN}")))
        }
    }

    private val playbackEventListener = object : YouTubePlayer.PlaybackEventListener {
        override fun onSeekTo(p0: Int) {
        }

        override fun onBuffering(p0: Boolean) {
            listener.onVideoStateChanged(this@YouTubeViewComponent, PlayViewerVideoState.Buffer(BufferSource.Viewer))
        }

        override fun onPlaying() {
            listener.onVideoStateChanged(this@YouTubeViewComponent, PlayViewerVideoState.Play)
        }

        override fun onStopped() {
            listener.onVideoStateChanged(this@YouTubeViewComponent, PlayViewerVideoState.Pause)
        }

        override fun onPaused() {
            listener.onVideoStateChanged(this@YouTubeViewComponent, PlayViewerVideoState.Pause)
        }
    }

    private val onFullscreenListener = object : YouTubePlayer.OnFullscreenListener {
        override fun onFullscreen(isFullscreen: Boolean) {
            mIsInternalOrientationChanged = true

            if (isFullscreen) listener.onEnterFullscreen(this@YouTubeViewComponent)
            else listener.onExitFullscreen(this@YouTubeViewComponent)
        }
    }

    fun safeInit() = synchronized(this) {
        if (isAlreadyInit.get()) return@synchronized
        isAlreadyInit.compareAndSet(false, true)

        youtubeFragment = fragmentManager.findFragmentByTag(YOUTUBE_FRAGMENT_TAG) as? PlayYouTubePlayerFragment ?: getPlayYouTubePlayerFragment().also {
            fragmentManager.beginTransaction()
                    .replace(rootView.id, it, YOUTUBE_FRAGMENT_TAG)
                    .commit()
        }
    }

    fun safeRelease() = synchronized(this) {
        if (!isAlreadyInit.get()) return@synchronized
        isAlreadyInit.compareAndSet(true, false)

        release()

        fragmentManager.findFragmentByTag(YOUTUBE_FRAGMENT_TAG)?.let { fragment ->
            fragmentManager.beginTransaction()
                    .remove(fragment)
                    .commit()
        }

        youtubeFragment = null
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
        pause()
        youTubePlayer?.doSafe { release() }
        youTubePlayer = null
    }

    fun setIsFullScreen(isFullscreen: Boolean) {
        this.mIsFullscreen = isFullscreen

        if (!mIsInternalOrientationChanged) {
            youTubePlayer?.doSafe { setFullscreen(isFullscreen) }
        }
        mIsInternalOrientationChanged = false
    }

    fun play() {
        youTubePlayer?.doSafe { play() }
    }

    fun pause() {
        youTubePlayer?.doSafe { pause() }
    }

    fun getPlayer(): YouTubePlayer? = youTubePlayer

    private fun initYouTubePlayer() {
        youtubeFragment?.let {
            it.initialize(object : YouTubePlayer.OnInitializedListener {
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
    }

    private fun playYouTubeFromId(youtubeId: String, startMillis: Int) {
        youTubePlayer?.doSafe { loadVideo(youtubeId, startMillis) }
    }

    private fun configYouTubePlayer(player: YouTubePlayer) : YouTubePlayer {
        player.setPlayerStateChangeListener(playerStateChangedListener)
        player.setPlaybackEventListener(playbackEventListener)
        player.fullscreenControlFlags = YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT
        player.setFullscreen(mIsFullscreen)
        return player
    }

    private fun getCurrentPosition(): Int {
        return youTubePlayer?.doSafe { currentTimeMillis } ?: 0
    }

    private fun isPlaying(): Boolean {
        return youTubePlayer?.doSafe { isPlaying } ?: true
    }

    private fun getPlayYouTubePlayerFragment(): PlayYouTubePlayerFragment {
        val fragmentFactory = fragmentManager.fragmentFactory
        return fragmentFactory.instantiate(container.context.classLoader, PlayYouTubePlayerFragment::class.java.name) as PlayYouTubePlayerFragment
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
        safeInit()
        val videoId = mVideoId
        if (videoId != null) {
            mVideoId = null
            setYouTubeId(videoId, mStartMillis, mShouldPlayOnReady)
        }
    }

    companion object {
        private const val YOUTUBE_FRAGMENT_TAG = "fragment_youtube"
    }

    interface Listener {
        fun onInitFailure(view: YouTubeViewComponent, result: YouTubeInitializationResult)

        fun onEnterFullscreen(view: YouTubeViewComponent)
        fun onExitFullscreen(view: YouTubeViewComponent)

        fun onVideoStateChanged(view: YouTubeViewComponent, state: PlayViewerVideoState)
    }

    interface DataSource {

        fun isEligibleToPlay(view: YouTubeViewComponent): Boolean
    }

    private fun <R: Any?> YouTubePlayer.doSafe(doHandler: YouTubePlayer.() -> R): R? {
        return try { doHandler() } catch (e: Throwable) { null }
    }
}