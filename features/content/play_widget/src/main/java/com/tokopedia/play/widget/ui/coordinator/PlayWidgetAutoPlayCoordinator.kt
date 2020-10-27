package com.tokopedia.play.widget.ui.coordinator

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.player.PlayVideoPlayer
import com.tokopedia.play.widget.player.PlayVideoPlayerReceiver
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.autoplay.AutoPlayModel
import com.tokopedia.play.widget.ui.autoplay.AutoPlayReceiverDecider
import com.tokopedia.play.widget.ui.autoplay.DefaultAutoPlayReceiverDecider
import com.tokopedia.play.widget.ui.listener.PlayWidgetInternalListener
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
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
        val visibleCards = getVisibleWidgetInRecyclerView(widgetCardsContainer)
        if (visibleCards.isEmpty()) return

        autoPlayJob?.cancel()
        autoPlayJob = scope.launch(mainCoroutineDispatcher) {
            delay(FAKE_MAX_DELAY)
            val autoPlayEligibleReceivers = autoPlayReceiverDecider.getEligibleAutoPlayReceivers(
                    visibleCards = visibleCards,
                    itemCount = widgetCardsContainer.layoutManager?.itemCount ?: 0,
                    maxAutoPlay = getMaxAutoPlayCard()
            )

            videoPlayerMap.entries.forEach {
                if (it.value !in autoPlayEligibleReceivers) {
                    it.key.stop()
                    it.key.listener = null
                    it.value?.setPlayer(null)
                    it.setValue(null)
                }
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

    fun onPause() {
        videoPlayerMap.keys.forEach { it.stop() }
    }

    fun onDestroy() {
        videoPlayerMap.keys.forEach { it.release() }
    }

    fun onResume() {
        videoPlayerMap.keys.forEach { it.restart() }
    }

    fun configureAutoPlay(widget: PlayWidgetView, config: PlayWidgetConfigUiModel) = synchronized(this@PlayWidgetAutoPlayCoordinator) {
        mConfig = config

        if (false) { //TODO(!config.autoPlay)
            videoPlayerMap.keys.forEach { it.release() }
            videoPlayerMap.clear()
            return@synchronized
        }

        val maxAutoPlay = config.autoPlayAmount

        if (videoPlayerMap.size < maxAutoPlay) {
            videoPlayerMap.putAll(
                    List(maxAutoPlay - videoPlayerMap.size) {
                        PlayVideoPlayer(widget.context) to null
                    }
            )
        } else if (videoPlayerMap.size > maxAutoPlay) {
            val lastVideoPlayer = videoPlayerMap.keys.lastOrNull()
            lastVideoPlayer?.release()
        }
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
        private const val FAKE_MAX_DELAY = 3000L // set delay before play to 3s
    }
}