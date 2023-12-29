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
import com.tokopedia.play.widget.ui.model.WidgetInList
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

    private var mIsVisible: Boolean = false

    override fun onWidgetCardsScrollChanged(widgetCardsContainer: RecyclerView) {
        val visibleCards = getVisibleWidgetInRecyclerView(widgetCardsContainer)
        startAutoPlay(
            visibleCards.map {
                WidgetInList(it.card, it.position)
            }
        )
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
        if (!mIsVisible) return
        videoPlayerMap.keys.forEach { it.restart() }
    }

    fun onVisible() {
        mIsVisible = true
        videoPlayerMap.keys.forEach {
            if (it.getPlayer().isPlaying) return@forEach
            it.start()
        }
    }

    fun onNotVisible() {
        mIsVisible = false
        videoPlayerMap.keys.forEach {
            it.stop()
        }
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
            it.maxDurationWifiInSeconds = config.maxAutoPlayWifiDuration
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

    private fun startAutoPlay(focusedWidgets: List<WidgetInList>) {
        autoPlayJob?.cancel()

        val autoPlayEligibleReceivers = autoPlayReceiverDecider.getEligibleAutoPlayReceivers(
            visibleCards = focusedWidgets.map {
                AutoPlayModel(it.widget, it.position)
            },
            itemCount = focusedWidgets.size,
            maxAutoPlay = getMaxAutoPlayCard()
        )

        autoPlayJob = scope.launch(mainCoroutineDispatcher) {
            delay(DELAY_BEFORE_PAUSE)
            videoPlayerMap.entries.forEach {
                if (it.value !in autoPlayEligibleReceivers) clearPlayerEntry(it)
            }

            delay(DELAY_BEFORE_PLAY)
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
        entry.key.repeat(false)
        entry.key.mute(true)
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
        private const val DELAY_BEFORE_PAUSE = 200L
        private const val DELAY_BEFORE_PLAY = 1500L
    }
}
