package com.tokopedia.play.widget.ui.coordinator

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.player.PlayVideoPlayer
import com.tokopedia.play.widget.player.PlayVideoPlayerReceiver
import com.tokopedia.play.widget.ui.PlayWidgetLargeView
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.PlayWidgetSmallView
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.autoplay.AutoPlayModel
import com.tokopedia.play.widget.ui.autoplay.AutoPlayReceiverDecider
import com.tokopedia.play.widget.ui.autoplay.DefaultAutoPlayReceiverDecider
import com.tokopedia.play.widget.ui.listener.PlayWidgetInternalListener
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetType
import kotlinx.coroutines.*

/**
 * Created by jegul on 21/10/20
 */
class PlayWidgetAutoPlayCoordinator(
    private val scope: CoroutineScope,
    private val mainCoroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : PlayWidgetInternalListener {

    private var autoPlayJob: Job? = null

    private val videoPlayerMap = mutableMapOf<PlayVideoPlayer, PlayVideoPlayerReceiver?>()

    private val autoPlayReceiverDecider: AutoPlayReceiverDecider = DefaultAutoPlayReceiverDecider()

    private lateinit var mConfig: PlayWidgetConfigUiModel

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

    private fun setupAutoplay(context: Context, config: PlayWidgetConfigUiModel, type: PlayWidgetType) {
        mConfig = config

        if (!config.autoPlay) {
            videoPlayerMap.keys.forEach { it.release() }
            videoPlayerMap.clear()
            return
        }

        val maxAutoPlay = config.autoPlayAmount

        if (videoPlayerMap.size < maxAutoPlay) {
            videoPlayerMap.putAll(
                List(maxAutoPlay - videoPlayerMap.size) {
                    PlayVideoPlayer(context, type) to null
                }
            )
        } else if (videoPlayerMap.size > maxAutoPlay) {
            val lastVideoPlayer = videoPlayerMap.keys.lastOrNull()
            lastVideoPlayer?.stop()
        }

        videoPlayerMap.keys.forEach {
            it.maxDurationCellularInSeconds = config.maxAutoPlayCellularDuration
        }
    }

    // For PlayWidgetView
    fun configureAutoPlay(widget: PlayWidgetView, config: PlayWidgetConfigUiModel, type: PlayWidgetType) = synchronized(this@PlayWidgetAutoPlayCoordinator) {
        setupAutoplay(widget.context, config, type)
    }

    // For Small View
    fun configureAutoPlaySmall(widget: PlayWidgetSmallView, config: PlayWidgetConfigUiModel, type: PlayWidgetType) = synchronized(this@PlayWidgetAutoPlayCoordinator) {
        setupAutoplay(widget.context, config, type)
    }

    // For Medium View
    fun configureAutoPlayMedium(widget: PlayWidgetMediumView, config: PlayWidgetConfigUiModel, type: PlayWidgetType) = synchronized(this@PlayWidgetAutoPlayCoordinator) {
        setupAutoplay(widget.context, config, type)
    }

    // For Large View
    fun configureAutoPlayLarge(widget: PlayWidgetLargeView, config: PlayWidgetConfigUiModel, type: PlayWidgetType) = synchronized(this@PlayWidgetAutoPlayCoordinator) {
        setupAutoplay(widget.context, config, type)
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

    private fun clearPlayerEntry(entry: MutableMap.MutableEntry<PlayVideoPlayer, PlayVideoPlayerReceiver?>) {
        entry.key.stop()
        entry.key.listener = null
        entry.value?.setPlayer(null)
        entry.setValue(null)
    }

    private fun getNextIdlePlayer(): PlayVideoPlayer? {
        return videoPlayerMap.entries.firstOrNull { it.value == null }?.key
    }

    private fun getVisibleWidgetInRecyclerView(recyclerView: RecyclerView): List<AutoPlayModel> {
        val layoutManager = recyclerView.layoutManager
        if (layoutManager !is LinearLayoutManager) return emptyList()

        return try {
            val firstCompleteVisiblePosition = layoutManager.findFirstCompletelyVisibleItemPosition()
            val lastCompleteVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition()

            val allVisibleViews = (firstCompleteVisiblePosition..lastCompleteVisiblePosition).mapNotNull {
                val view = layoutManager.findViewByPosition(it)
                if (view == null) {
                    null
                } else {
                    AutoPlayModel(view, it)
                }
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
        private const val MAX_DELAY = 2000L // set delay before play to 2s
    }
}
