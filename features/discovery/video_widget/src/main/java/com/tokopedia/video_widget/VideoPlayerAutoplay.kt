package com.tokopedia.video_widget

import android.content.Context
import android.view.ViewTreeObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.video_widget.util.LayoutManagerUtil
import com.tokopedia.video_widget.util.RecyclerViewUtils.getRecyclerViewLocationAndMeasurement
import com.tokopedia.video_widget.util.RecyclerViewUtils.getViewVisibilityOnRecyclerView
import com.tokopedia.video_widget.util.SimpleExoPlayerUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class VideoPlayerAutoplay(
    private val remoteConfig: RemoteConfig,
    private var context: Context?,
) : CoroutineScope, LifecycleObserver {
    private var productVideoAutoPlayJob: Job? = null
    private var videoPlayer: VideoPlayer? = null

    private var visibleVideoPlayers: List<VideoPlayer> = emptyList()
    private var videoPlayerIterator: Iterator<VideoPlayer>? = null
    private var isPaused = false

    private var firstVisibleItemIndex: Int = -1
    private var lastVisibleItemIndex: Int = -1

    private val hasVisibleViewHolders: Boolean
        get() {
            return isAdapterNotEmpty
                && firstVisibleItemIndex != -1
                && lastVisibleItemIndex != -1
        }

    private val isAutoplayProductVideoEnabled: Boolean by lazy {
        remoteConfig.getBoolean(RemoteConfigKey.ENABLE_MPC_VIDEO_AUTOPLAY, true)
    }

    private var recyclerView: RecyclerView? = null
    private val layoutManager: RecyclerView.LayoutManager?
        get() = recyclerView?.layoutManager

    private val adapterItemCount: Int
        get() = recyclerView?.adapter?.itemCount ?: 0
    private val isAdapterEmpty: Boolean
        get() = adapterItemCount <= 0
    private val isAdapterNotEmpty: Boolean
        get() = !isAdapterEmpty

    private lateinit var masterJob: Job

    override val coroutineContext: CoroutineContext
        get() = masterJob + Dispatchers.Main

    private var mExoPlayerListener: ExoPlayerListener? = null

    private val playerEventListener = object : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            val exoPlayerListener = mExoPlayerListener ?: return
            when (playbackState) {
                Player.STATE_IDLE -> exoPlayerListener.onPlayerIdle()
                Player.STATE_BUFFERING -> exoPlayerListener.onPlayerBuffering()
                Player.STATE_READY -> {
                    if (!playWhenReady) {
                        exoPlayerListener.onPlayerPaused()
                    } else {
                        exoPlayerListener.onPlayerPlaying()
                    }
                }
                Player.STATE_ENDED -> {
                    exoPlayerListener.onPlayerEnded()
                }
            }
        }

        override fun onPlayerError(error: ExoPlaybackException) {
            val exoPlayerListener = mExoPlayerListener ?: return
            exoPlayerListener.onPlayerError(error.toString())
        }
    }

    private var exoPlayer: ExoPlayer? = null

    private val autoPlayScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                startVideoAutoplay()
            }
        }
    }

    private val autoPlayAdapterDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            if (positionStart == 0) {
                recyclerView?.viewTreeObserver
                    ?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            startVideoAutoplay()
                            recyclerView?.viewTreeObserver
                                ?.removeOnGlobalLayoutListener(this)
                        }
                    })
            }
        }
    }

    fun registerLifecycleObserver(lifecycleOwner: LifecycleOwner) {
        if (!isAutoplayProductVideoEnabled) return
        lifecycleOwner.lifecycle.addObserver(this)
    }

    fun setUp(recyclerView: RecyclerView) {
        if (isAutoplayProductVideoEnabled) {
            this.exoPlayer = SimpleExoPlayerUtils.create(context, playerEventListener)
            this.recyclerView = recyclerView
            recyclerView.addOnScrollListener(autoPlayScrollListener)
            recyclerView.adapter?.registerAdapterDataObserver(autoPlayAdapterDataObserver)
        }
    }

    private fun startVideoAutoplay() {
        val currentlyVisibleVideoPlayers = filterVisibleProductVideoPlayer()
        if (currentlyVisibleVideoPlayers != visibleVideoPlayers) {
            visibleVideoPlayers = currentlyVisibleVideoPlayers
            val visibleItemIterable = currentlyVisibleVideoPlayers.iterator()
            videoPlayerIterator = visibleItemIterable
            productVideoAutoPlayJob?.cancel()
            videoPlayer?.stopVideo()
            productVideoAutoPlayJob = launch {
                isPaused = false
                playNextVideo(visibleItemIterable)
            }
        }
    }

    private fun filterVisibleProductVideoPlayer(): List<VideoPlayer> {
        if (isAdapterEmpty) return emptyList()
        firstVisibleItemIndex = LayoutManagerUtil.getFirstVisibleItemIndex(layoutManager, false)
        lastVisibleItemIndex = LayoutManagerUtil.getLastVisibleItemIndex(layoutManager)
        return if (hasVisibleViewHolders) getVisibleViewHolderList() else emptyList()
    }

    private fun getVisibleViewHolderList(): List<VideoPlayer> {
        val visibleVideoPlayerProviders = mutableListOf<VideoPlayerProvider>()
        val (recyclerViewPosition, recyclerViewMeasurement) = getRecyclerViewLocationAndMeasurement(
            recyclerView
        )
        for (index in firstVisibleItemIndex..lastVisibleItemIndex) {
            val viewHolder = recyclerView?.findViewHolderForAdapterPosition(index) ?: continue
            if (viewHolder is VideoPlayerProvider && viewHolder.isAutoplayEnabled) {
                val viewVisibilityPercentage = getViewVisibilityOnRecyclerView(
                    viewHolder.itemView,
                    recyclerViewPosition,
                    recyclerViewMeasurement
                )
                if (viewVisibilityPercentage > VISIBILITY_PERCENTAGE_THRESHOLD) {
                    visibleVideoPlayerProviders.add(viewHolder)
                }
            }
        }
        return visibleVideoPlayerProviders
            .mapNotNull { it.videoPlayer }
            .filter { it.hasVideo }
    }

    private suspend fun playNextVideo(visibleItemIterator: Iterator<VideoPlayer>) {
        if (canPlayNextVideo(visibleItemIterator)) {
            val visibleItem = visibleItemIterator.next()
            videoPlayer = visibleItem
            playVideo(visibleItem, visibleItemIterator)
        } else if (!visibleItemIterator.hasNext()) {
            clearQueue()
        }
    }

    private fun canPlayNextVideo(
        visibleItemIterator: Iterator<VideoPlayer>
    ): Boolean {
        return isActive && !isPaused && visibleItemIterator.hasNext()
    }

    private suspend fun playVideo(
        visibleItem: VideoPlayer,
        visibleItemIterator: Iterator<VideoPlayer>
    ) {
        val exoPlayer = exoPlayer ?: return
        if (visibleItem is ExoPlayerListener) mExoPlayerListener = visibleItem
        visibleItem.playVideo(exoPlayer)
            .filter { state ->
                state is VideoPlayerState.Ended
                    || state is VideoPlayerState.NoVideo
                    || state is VideoPlayerState.Error
            }
            .catch { t ->
                Timber.e(t)
                VideoPlayerState.Error(t.message ?: "Unknown Error")
            }
            .collect {
                mExoPlayerListener = null
                videoPlayer = null
                playNextVideo(visibleItemIterator)
            }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onViewCreated() {
        masterJob = Job()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun resumeVideoAutoplay() {
        val visibleItemIterator = videoPlayerIterator ?: return
        if (isPaused && visibleItemIterator.hasNext()) {
            productVideoAutoPlayJob = launch {
                isPaused = false
                playNextVideo(visibleItemIterator)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pauseVideoAutoplay() {
        if (!isPaused) {
            isPaused = true
            videoPlayer?.stopVideo()
            productVideoAutoPlayJob?.cancel()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onViewDestroyed() {
        unregisterVideoAutoplayAdapterObserver()
        stopVideoAutoplay()
        masterJob.cancel()
        context = null
        exoPlayer = null
    }

    private fun unregisterVideoAutoplayAdapterObserver() {
        if (isAutoplayProductVideoEnabled) {
            try {
                recyclerView?.adapter?.unregisterAdapterDataObserver(autoPlayAdapterDataObserver)
            } catch (t: Throwable) {
                Timber.d(t)
            }
            recyclerView = null
        }
    }

    fun stopVideoAutoplay() {
        if (!isAutoplayProductVideoEnabled) return
        videoPlayer?.stopVideo()
        videoPlayer = null
        productVideoAutoPlayJob?.cancel()
        clearQueue()
    }

    private fun clearQueue() {
        visibleVideoPlayers = emptyList()
        videoPlayerIterator = null
    }

    companion object {
        private const val VISIBILITY_PERCENTAGE_THRESHOLD = 50f
    }
}
