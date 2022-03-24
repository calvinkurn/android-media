package com.tokopedia.video_widget.carousel

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.video_widget.util.LayoutManagerUtil
import com.tokopedia.video_widget.util.RecyclerViewUtils
import com.tokopedia.video_widget.util.ViewMeasurement
import com.tokopedia.video_widget.util.VisibilityMeasurementMethod
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VideoCarouselAutoPlayCoordinator(
    private val scope: CoroutineScope,
    private val mainCoroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
    private val hasExternalAutoPlayController: Boolean,
) : VideoCarouselInternalListener {
    companion object {
        private const val MAX_DELAY = 500L // set delay before play to 0.5s
        private const val VISIBILITY_PERCENTAGE_THRESHOLD = 50f
    }

    private var autoPlayJob: Job? = null
    private var canStartAutoPlay: Boolean = false

    private val videoPlayerMap = mutableMapOf<CarouselVideoPlayer, CarouselVideoPlayerReceiver?>()

    private val autoPlayReceiverDecider: AutoPlayReceiverDecider = DefaultAutoPlayReceiverDecider()

    private lateinit var mConfig: VideoCarouselConfigUiModel

    private val canUpdateAutoPlay: Boolean
        get() = !hasExternalAutoPlayController || canStartAutoPlay

    override fun playVideo(container: RecyclerView) {
        if (hasExternalAutoPlayController) canStartAutoPlay = true
        startAutoPlay(container)
    }

    override fun stopVideo() {
        stopVideoAutoPlay()
    }

    override fun onWidgetCardsScrollChanged(container: RecyclerView) {
        if (canUpdateAutoPlay) startAutoPlay(container)
    }

    override fun onWidgetDetached(widget: View) {
        stopVideoAutoPlay()
    }

    private fun stopVideoAutoPlay() {
        autoPlayJob?.cancel()
        videoPlayerMap.entries.forEach { clearPlayerEntry(it) }
        if (hasExternalAutoPlayController) canStartAutoPlay = false
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

            videoPlayerMap.keys.forEach {
                it.setMaxDurationInSecond(config.maxAutoPlayWifiDuration)
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
                LayoutManagerUtil.getFirstVisibleItemIndex(layoutManager, false)
            val lastCompleteVisiblePosition =
                LayoutManagerUtil.getLastVisibleItemIndex(layoutManager)

            val positionRange = firstCompleteVisiblePosition..lastCompleteVisiblePosition
            val (recyclerViewPosition, recyclerViewMeasurement) = RecyclerViewUtils.getRecyclerViewLocationAndMeasurement(
                recyclerView
            )
            val allVisibleViews = positionRange.mapNotNull { position ->
                val view = layoutManager.findViewByPosition(position)
                getAutoPlayModel(view, recyclerViewPosition, recyclerViewMeasurement, position)
            }

            allVisibleViews
        } catch (e: Throwable) {
            emptyList()
        }
    }

    private fun getAutoPlayModel(
        view: View?,
        recyclerViewPosition: IntArray,
        recyclerViewMeasurement: ViewMeasurement,
        position: Int,
    ): AutoPlayModel? {
        if (view == null) return null

        val viewVisibilityPercentage = RecyclerViewUtils.getViewVisibilityOnRecyclerView(
            view,
            recyclerViewPosition,
            recyclerViewMeasurement,
            VisibilityMeasurementMethod.HorizontalOnly
        )
        return if (viewVisibilityPercentage > VISIBILITY_PERCENTAGE_THRESHOLD) {
            AutoPlayModel(view, position)
        } else null
    }

    private fun getMaxAutoPlayCard(): Int {
        return if (::mConfig.isInitialized) mConfig.autoPlayAmount else 0
    }
}