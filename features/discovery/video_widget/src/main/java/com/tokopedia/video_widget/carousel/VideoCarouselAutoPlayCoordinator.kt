package com.tokopedia.video_widget.carousel

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.video_widget.util.LayoutManagerUtil
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VideoCarouselAutoPlayCoordinator(
    private val scope: CoroutineScope,
    private val mainCoroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : VideoCarouselInternalListener {
    private var autoPlayJob: Job? = null

    private val videoPlayerMap = mutableMapOf<CarouselVideoPlayer, CarouselVideoPlayerReceiver?>()

    private val autoPlayReceiverDecider: AutoPlayReceiverDecider = DefaultAutoPlayReceiverDecider()

    private lateinit var mConfig: VideoCarouselConfigUiModel

    override fun onWidgetCardsScrollChanged(widgetCardsContainer: RecyclerView) {
        startAutoPlay(widgetCardsContainer)
    }

    override fun onWidgetDetached(widget: View) {
        autoPlayJob?.cancel()
        videoPlayerMap.entries.forEach { clearPlayerEntry(it) }
    }

    fun onPause() {
        autoPlayJob?.cancel()
        videoPlayerMap.keys.forEach { it.stop() }
    }

    fun onDestroy() {
        autoPlayJob?.cancel()
        videoPlayerMap.keys.forEach { it.release() }
    }

    fun onResume() {
        videoPlayerMap.keys.forEach { it.restart() }
    }

    fun configureAutoPlay(widget: View, config: VideoCarouselConfigUiModel) =
        synchronized(this@VideoCarouselAutoPlayCoordinator) {
            mConfig = config

            if (!config.autoPlay) {
                videoPlayerMap.keys.forEach { it.release() }
                videoPlayerMap.clear()
                return@synchronized
            }

            val maxAutoPlay = config.autoPlayAmount

            if (videoPlayerMap.size < maxAutoPlay) {
                videoPlayerMap.putAll(
                    List(maxAutoPlay - videoPlayerMap.size) {
                        CarouselVideoPlayer(widget.context) to null
                    }
                )
            } else if (videoPlayerMap.size > maxAutoPlay) {
                val lastVideoPlayer = videoPlayerMap.keys.lastOrNull()
                lastVideoPlayer?.stop()
            }
        }

    private fun startAutoPlay(widgetCardsContainer: RecyclerView) {
        val visibleCards = getVisibleWidgetInRecyclerView(widgetCardsContainer)
        if (visibleCards.isEmpty()) return

        autoPlayJob?.cancel()
        autoPlayJob = scope.launch(mainCoroutineDispatcher) {
            delay(MAX_DELAY)
            val autoPlayEligibleReceivers = autoPlayReceiverDecider.getEligibleAutoPlayReceivers(
                visibleCards = visibleCards,
                itemCount = widgetCardsContainer.layoutManager?.itemCount ?: 0,
                maxAutoPlay = getMaxAutoPlayCard()
            )

            videoPlayerMap.entries.forEach {
                if (it.value !in autoPlayEligibleReceivers) clearPlayerEntry(it)
            }

            autoPlayEligibleReceivers
                .filter { it.getPlayer() == null }
                .forEach {
                    val nextIdlePlayer = getNextIdlePlayer()

                    if (nextIdlePlayer != null) {
                        it.setPlayer(nextIdlePlayer)
                        videoPlayerMap[nextIdlePlayer] = it
                    }
                }
        }
    }

    private fun clearPlayerEntry(entry: MutableMap.MutableEntry<CarouselVideoPlayer, CarouselVideoPlayerReceiver?>) {
        entry.key.stop()
        entry.key.listener = null
        entry.value?.setPlayer(null)
        entry.setValue(null)
    }

    private fun getNextIdlePlayer(): CarouselVideoPlayer? {
        return videoPlayerMap.entries.firstOrNull { it.value == null }?.key
    }

    private fun getVisibleWidgetInRecyclerView(recyclerView: RecyclerView): List<AutoPlayModel> {
        val layoutManager = recyclerView.layoutManager ?: return emptyList()

        return try {
            val firstCompleteVisiblePosition =
                LayoutManagerUtil.getFirstVisibleItemIndex(layoutManager, true)
            val lastCompleteVisiblePosition =
                LayoutManagerUtil.getLastVisibleItemIndex(layoutManager)

            val allVisibleViews = (firstCompleteVisiblePosition..lastCompleteVisiblePosition)
                .mapNotNull {
                    val view = layoutManager.findViewByPosition(it)
                    if (view == null) null
                    else AutoPlayModel(view, it)
                }

            allVisibleViews
        } catch (e: Throwable) {
            emptyList()
        }
    }

    private fun getMaxAutoPlayCard(): Int {
        return if (::mConfig.isInitialized) mConfig.autoPlayAmount else 0
    }

    companion object {
        private const val MAX_DELAY = 500L // set delay before play to 0.5s
    }
}