package com.tokopedia.productcard.helper.autoplay

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard.helper.ProductVideoPlayer
import com.tokopedia.productcard.helper.VideoPlayerState
import com.tokopedia.productcard.utils.LayoutManagerUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber

class ProductVideoAutoplayHelper<T, R : T>(
    scope: CoroutineScope
) : CoroutineScope by scope {
    private var productVideoAutoPlayJob: Job? = null
    private var productVideoPlayer: ProductVideoPlayer? = null

    fun startVideoAutoplay(
        recyclerView: RecyclerView?,
        layoutManager: RecyclerView.LayoutManager?,
        itemList: List<T>?,
        filter: (List<T>) -> List<R>
    ) {
        productVideoAutoPlayJob?.cancel()
        val firstVisibleItemIndex = LayoutManagerUtil.getFirstVisibleItemIndex(layoutManager)
        val lastCompleteVisibleItemIndex = LayoutManagerUtil.getLastVisibleItemIndex(layoutManager)
        if (!itemList.isNullOrEmpty()
            && firstVisibleItemIndex != -1
            && lastCompleteVisibleItemIndex != -1
        ) {
            val subList = itemList.subList(
                firstVisibleItemIndex,
                lastCompleteVisibleItemIndex + 1
            )
            val visibleItems : List<R> = filter(subList)
            val visibleItemIterable = visibleItems.iterator()
            productVideoAutoPlayJob = launch {
                playNextVideo(visibleItemIterable, itemList, recyclerView)
            }
        }
    }

    fun stopVideoAutoplay() {
        productVideoPlayer?.stopVideo()
        productVideoAutoPlayJob?.cancel()
    }

    private suspend fun playNextVideo(
        visibleItemIterator: Iterator<R>,
        visitableList: List<T>,
        recyclerView: RecyclerView?
    ) {
        if (isActive && visibleItemIterator.hasNext()) {
            val visibleItem = visibleItemIterator.next()
            val index = visitableList.indexOf(visibleItem)
            if (index == -1) return
            val viewHolder = recyclerView?.findViewHolderForAdapterPosition(index) ?: return
            if (viewHolder is ProductVideoPlayer && viewHolder.hasProductVideo) {
                productVideoPlayer = viewHolder
                viewHolder.playVideo()
                    .filter { state ->
                        state is VideoPlayerState.Ended
                                || state is VideoPlayerState.NoVideo
                                || state is VideoPlayerState.Error
                    }
                    .catch { t -> Timber.e(t) }
                    .collect {
                        productVideoPlayer = null
                        if (isActive && visibleItemIterator.hasNext()) {
                            playNextVideo(visibleItemIterator, visitableList, recyclerView)
                        }
                    }
            } else if (isActive && visibleItemIterator.hasNext()) {
                playNextVideo(visibleItemIterator, visitableList, recyclerView)
            }
        }
    }
}