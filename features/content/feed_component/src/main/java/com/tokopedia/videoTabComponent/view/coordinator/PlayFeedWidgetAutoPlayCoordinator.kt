package com.tokopedia.videoTabComponent.view.coordinator

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.play.widget.player.PlayVideoPlayer
import com.tokopedia.play.widget.player.PlayVideoPlayerReceiver
import com.tokopedia.play.widget.ui.autoplay.AutoPlayModel
import com.tokopedia.play.widget.ui.autoplay.AutoPlayReceiverDecider
import com.tokopedia.play.widget.ui.autoplay.DefaultAutoPlayReceiverDecider
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetAutoPlayCoordinator
import com.tokopedia.play.widget.ui.listener.PlayWidgetInternalListener
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetType
import com.tokopedia.play.widget.ui.model.WidgetInList
import kotlinx.coroutines.*

/**
 * Created by jegul on 21/10/20
 */
class PlayFeedWidgetAutoPlayCoordinator(
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

    override fun onFocusedWidgetsChanged(focusedWidgets: List<WidgetInList>) {
        startAutoPlay(focusedWidgets)
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
    fun configureLargeOrJumboWidgetAutoplay(widgetCardsContainer: RecyclerView){
        startAutoPlay(widgetCardsContainer)
    }

    fun configureAutoPlay(context : Context, config: PlayWidgetConfigUiModel, type: PlayWidgetType) = synchronized(this@PlayFeedWidgetAutoPlayCoordinator) {
        mConfig = config

        if (!config.autoPlay) {
            videoPlayerMap.keys.forEach { it.release() }
            videoPlayerMap.clear()
            return@synchronized
        }

        val maxAutoPlay = config.autoPlayAmount

        if (videoPlayerMap.size <= maxAutoPlay) {
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

    private fun startAutoPlay(focusedWidgets: List<WidgetInList>) {
        autoPlayJob?.cancel()

        val autoPlayEligibleReceivers = autoPlayReceiverDecider.getEligibleAutoPlayReceivers(
            visibleCards = focusedWidgets.map {
                AutoPlayModel(it.widget, it.position)
            },
            itemCount = focusedWidgets.size,
            maxAutoPlay = getMaxAutoPlayCard()
        )

        videoPlayerMap.entries.forEach {
            if (it.value !in autoPlayEligibleReceivers) clearPlayerEntry(it)
        }

        autoPlayJob = scope.launch(mainCoroutineDispatcher) {
            delay(MAX_DELAY)

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
                if (view == null)
                    null
                else {
                    val actualPosition = Rect()
                    view.getGlobalVisibleRect(actualPosition)
                    val screen = Rect(0, 0, getScreenWidth(), getScreenHeight())
                    if (!view.isShown || !actualPosition.intersect(screen)) {
                        null
                    } else
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
