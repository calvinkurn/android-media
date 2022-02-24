package com.tokopedia.productcard.video

import android.view.ViewTreeObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard.utils.LayoutManagerUtil
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
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

class ProductVideoAutoplay(
    private val remoteConfig: RemoteConfig,
) : CoroutineScope, LifecycleObserver {
    private var productVideoAutoPlayJob: Job? = null
    private var productVideoPlayer: ProductVideoPlayer? = null

    private var visibleVideoPlayers: List<ProductVideoPlayer> = emptyList()
    private var videoPlayerIterator: Iterator<ProductVideoPlayer>? = null
    private var isPaused = false

    private val isAutoplayProductVideoEnabled: Boolean by lazy {
        remoteConfig.getBoolean(RemoteConfigKey.ENABLE_MPC_VIDEO_AUTOPLAY, true)
    }

    private var recyclerView: RecyclerView? = null
    private val layoutManager: RecyclerView.LayoutManager?
        get() = recyclerView?.layoutManager

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
        if (currentlyVisibleVideoPlayers != visibleVideoPlayers) {
            visibleVideoPlayers = currentlyVisibleVideoPlayers
            val visibleItemIterable = currentlyVisibleVideoPlayers.iterator()
            videoPlayerIterator = visibleItemIterable
            productVideoAutoPlayJob = launch {
                isPaused = false
                playNextVideo(visibleItemIterable)
            }
        }
    }

    private fun filterVisibleProductVideoPlayer(): List<ProductVideoPlayer> {
        val itemCount = recyclerView?.adapter?.itemCount ?: return emptyList()
        val firstVisibleItemIndex = LayoutManagerUtil.getFirstVisibleItemIndex(layoutManager, false)
        val lastVisibleItemIndex = LayoutManagerUtil.getLastVisibleItemIndex(layoutManager)
        if (itemCount > 0
            && firstVisibleItemIndex != -1
            && lastVisibleItemIndex != -1
        ) {
            return getVisibleViewHolderList(
                firstVisibleItemIndex,
                lastVisibleItemIndex
            )
        }
        return emptyList()
    }

    private fun getVisibleViewHolderList(
        firstVisibleItemIndex: Int,
        lastVisibleItemIndex: Int,
    ): List<ProductVideoPlayer> {
        val visibleVideoPlayerProviders = mutableListOf<ProductVideoPlayerProvider>()
        for (index in firstVisibleItemIndex..lastVisibleItemIndex) {
            val viewHolder = recyclerView?.findViewHolderForAdapterPosition(index) ?: continue
            if (viewHolder is ProductVideoPlayerProvider) {
                visibleVideoPlayerProviders.add(viewHolder)
            }
        }
        return visibleVideoPlayerProviders
            .mapNotNull {
                it.productVideoPlayer
            }
            .filter { it.hasProductVideo }
    }

    private suspend fun playNextVideo(visibleItemIterator: Iterator<ProductVideoPlayer>) {
        if (canPlayNextVideo(visibleItemIterator)) {
            val visibleItem = visibleItemIterator.next()
            productVideoPlayer = visibleItem
            playVideo(visibleItem, visibleItemIterator)
        } else if (!visibleItemIterator.hasNext()) {
            clearQueue()
        }
    }

    private fun canPlayNextVideo(
        visibleItemIterator: Iterator<ProductVideoPlayer>
    ) : Boolean {
        return isActive && !isPaused && visibleItemIterator.hasNext()
    }

    private suspend fun playVideo(
        visibleItem : ProductVideoPlayer,
        visibleItemIterator: Iterator<ProductVideoPlayer>
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
                productVideoPlayer = null
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
            productVideoPlayer?.stopVideo()
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
        }
        recyclerView = null
    }

    fun stopVideoAutoplay() {
        productVideoPlayer?.stopVideo()
        productVideoPlayer = null
        productVideoAutoPlayJob?.cancel()
        clearQueue()
    }

    private fun clearQueue() {
        visibleVideoPlayers = emptyList()
        videoPlayerIterator = null
    }
}