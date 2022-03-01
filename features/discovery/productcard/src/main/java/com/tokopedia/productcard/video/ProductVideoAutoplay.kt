package com.tokopedia.productcard.video

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard.utils.LayoutManagerUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber

class ProductVideoAutoplay<T, R : T>(
    scope: CoroutineScope
) : CoroutineScope by scope, LifecycleObserver {
    private var productVideoAutoPlayJob: Job? = null
    private var productVideoPlayer: ProductVideoPlayer? = null

    private var visibleVideoPlayers: List<ProductVideoPlayer> = emptyList()
    private var videoPlayerIterator: Iterator<ProductVideoPlayer>? = null
    private var isPaused = false

    fun startVideoAutoplay(
        recyclerView: RecyclerView?,
        layoutManager: RecyclerView.LayoutManager?,
        itemList: List<T>?,
        filter: (List<T>) -> List<R>
    ) {
        productVideoAutoPlayJob?.cancel()
        val firstVisibleItemIndex = LayoutManagerUtil.getFirstVisibleItemIndex(layoutManager, false)
        val lastCompleteVisibleItemIndex = LayoutManagerUtil.getLastVisibleItemIndex(layoutManager)
        if (!itemList.isNullOrEmpty()
            && firstVisibleItemIndex != -1
            && lastCompleteVisibleItemIndex != -1
        ) {
            val subList = try {
                val lastIndex = lastCompleteVisibleItemIndex + 1
                val itemListSize = itemList.size
                if(itemListSize > firstVisibleItemIndex && lastIndex <= itemListSize) {
                    itemList.subList(firstVisibleItemIndex, lastIndex)
                } else {
                    emptyList()
                }
            } catch (t: Throwable) {
                Timber.e(t)
                emptyList()
            }
            val visibleItems : List<R> = filter(subList)
            val currentlyVisibleVideoPlayers = visibleItems.map {
                val index = itemList.indexOf(it)
                if (index == -1) return
                recyclerView?.findViewHolderForAdapterPosition(index)
            }
                .filterIsInstance<ProductVideoPlayer>()
            if(currentlyVisibleVideoPlayers != visibleVideoPlayers) {
                visibleVideoPlayers = currentlyVisibleVideoPlayers
                val visibleItemIterable = currentlyVisibleVideoPlayers.iterator()
                videoPlayerIterator = visibleItemIterable
                productVideoAutoPlayJob = launch {
                    isPaused = false
                    playNextVideo(visibleItemIterable)
                }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
     fun resumeVideoAutoplay() {
        val visibleItemIterator = videoPlayerIterator ?: return
        if(isPaused && visibleItemIterator.hasNext()) {
            productVideoAutoPlayJob = launch {
                isPaused = false
                playNextVideo(visibleItemIterator)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pauseVideoAutoplay() {
        if(!isPaused) {
            isPaused = true
            productVideoPlayer?.stopVideo()
            productVideoAutoPlayJob?.cancel()
        }
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun stopVideoAutoplay() {
        productVideoPlayer?.stopVideo()
        productVideoPlayer = null
        productVideoAutoPlayJob?.cancel()
        clearQueue()
    }

    private suspend fun playNextVideo(visibleItemIterator: Iterator<ProductVideoPlayer>) {
        if (isActive && !isPaused && visibleItemIterator.hasNext()) {
            val visibleItem = visibleItemIterator.next()
            if (visibleItem.hasProductVideo) {
                productVideoPlayer = visibleItem
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
                        if (isActive && !isPaused && visibleItemIterator.hasNext()) {
                            playNextVideo(visibleItemIterator)
                        } else if(!visibleItemIterator.hasNext()) {
                            clearQueue()
                        }
                    }
            } else if (isActive && !isPaused && visibleItemIterator.hasNext()) {
                playNextVideo(visibleItemIterator)
            } else if(!visibleItemIterator.hasNext()) {
                clearQueue()
            }
        } else if(!visibleItemIterator.hasNext()) {
            clearQueue()
        }
    }

    private fun clearQueue() {
        visibleVideoPlayers = emptyList()
        videoPlayerIterator = null
    }
}