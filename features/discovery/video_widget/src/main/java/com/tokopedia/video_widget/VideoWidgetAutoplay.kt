package com.tokopedia.video_widget

import android.view.ViewTreeObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.video_widget.util.LayoutManagerUtil
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

class VideoWidgetAutoplay(
    private val remoteConfig: RemoteConfig,
) : CoroutineScope, LifecycleObserver {
    private var productVideoAutoPlayJob: Job? = null
    private var videoWidgetPlayer: VideoWidgetPlayer? = null

    private var visibleVideoWidgetPlayers: List<VideoWidgetPlayer> = emptyList()
    private var videoWidgetPlayerIterator: Iterator<VideoWidgetPlayer>? = null
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
        if(!isAutoplayProductVideoEnabled) return
        lifecycleOwner.lifecycle.addObserver(this)
    }

    fun setUp(recyclerView: RecyclerView) {
        if (isAutoplayProductVideoEnabled) {
            this.recyclerView = recyclerView
            recyclerView.addOnScrollListener(autoPlayScrollListener)
            recyclerView.adapter?.registerAdapterDataObserver(autoPlayAdapterDataObserver)
        }
    }

    private fun startVideoAutoplay() {
        productVideoAutoPlayJob?.cancel()
        val currentlyVisibleVideoPlayers = filterVisibleProductVideoPlayer()
        if (currentlyVisibleVideoPlayers != visibleVideoWidgetPlayers) {
            visibleVideoWidgetPlayers = currentlyVisibleVideoPlayers
            val visibleItemIterable = currentlyVisibleVideoPlayers.iterator()
            videoWidgetPlayerIterator = visibleItemIterable
            productVideoAutoPlayJob = launch {
                isPaused = false
                playNextVideo(visibleItemIterable)
            }
        }
    }

    private fun filterVisibleProductVideoPlayer(): List<VideoWidgetPlayer> {
        if(isAdapterEmpty) return emptyList()
        firstVisibleItemIndex = LayoutManagerUtil.getFirstVisibleItemIndex(layoutManager, false)
        lastVisibleItemIndex = LayoutManagerUtil.getLastVisibleItemIndex(layoutManager)
        return if (hasVisibleViewHolders) getVisibleViewHolderList() else emptyList()
    }

    private fun getVisibleViewHolderList(): List<VideoWidgetPlayer> {
        val visibleVideoPlayerProviders = mutableListOf<VideoWidgetPlayerProvider>()
        for (index in firstVisibleItemIndex..lastVisibleItemIndex) {
            val viewHolder = recyclerView?.findViewHolderForAdapterPosition(index) ?: continue
            if (viewHolder is VideoWidgetPlayerProvider) {
                visibleVideoPlayerProviders.add(viewHolder)
            }
        }
        return visibleVideoPlayerProviders
            .mapNotNull { it.videoWidgetPlayer }
            .filter { it.hasVideo }
    }

    private suspend fun playNextVideo(visibleItemIterator: Iterator<VideoWidgetPlayer>) {
        if (canPlayNextVideo(visibleItemIterator)) {
            val visibleItem = visibleItemIterator.next()
            videoWidgetPlayer = visibleItem
            playVideo(visibleItem, visibleItemIterator)
        } else if (!visibleItemIterator.hasNext()) {
            clearQueue()
        }
    }

    private fun canPlayNextVideo(
        visibleItemIterator: Iterator<VideoWidgetPlayer>
    ) : Boolean {
        return isActive && !isPaused && visibleItemIterator.hasNext()
    }

    private suspend fun playVideo(
        visibleItem : VideoWidgetPlayer,
        visibleItemIterator: Iterator<VideoWidgetPlayer>
    ) {
        visibleItem.playVideo()
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
                videoWidgetPlayer = null
                playNextVideo(visibleItemIterator)
            }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onViewCreated() {
        masterJob = Job()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun resumeVideoAutoplay() {
        val visibleItemIterator = videoWidgetPlayerIterator ?: return
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
            videoWidgetPlayer?.stopVideo()
            productVideoAutoPlayJob?.cancel()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onViewDestroyed() {
        unregisterVideoAutoplayAdapterObserver()
        stopVideoAutoplay()
        masterJob.cancel()
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
        if(!isAutoplayProductVideoEnabled) return
        videoWidgetPlayer?.stopVideo()
        videoWidgetPlayer = null
        productVideoAutoPlayJob?.cancel()
        clearQueue()
    }

    private fun clearQueue() {
        visibleVideoWidgetPlayers = emptyList()
        videoWidgetPlayerIterator = null
    }
}