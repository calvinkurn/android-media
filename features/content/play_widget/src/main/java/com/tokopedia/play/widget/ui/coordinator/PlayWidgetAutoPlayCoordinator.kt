package com.tokopedia.play.widget.ui.coordinator

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.player.PlayVideoPlayer
import com.tokopedia.play.widget.player.PlayVideoPlayerReceiver
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.autoplay.AutoPlayModel
import com.tokopedia.play.widget.ui.autoplay.AutoPlayReceiverDecider
import com.tokopedia.play.widget.ui.autoplay.DefaultAutoPlayReceiverDecider
import com.tokopedia.play.widget.ui.listener.PlayWidgetViewListener
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import kotlinx.coroutines.*

/**
 * Created by jegul on 21/10/20
 */
class PlayWidgetAutoPlayCoordinator(
        scope: CoroutineScope,
        mainCoroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) {

    private var autoPlayJob: Job? = null

    private val videoPlayerMap = mutableMapOf<PlayVideoPlayer, PlayVideoPlayerReceiver?>()

    private val autoPlayReceiverDecider: AutoPlayReceiverDecider = DefaultAutoPlayReceiverDecider()

    private val widgetViewListener = object : PlayWidgetViewListener {

        override fun onWidgetCardsScrollChanged(widgetCardsContainer: RecyclerView) {
            val visibleCards = getVisibleWidgetInRecyclerView(widgetCardsContainer)
            if (visibleCards.isEmpty()) return

            autoPlayJob?.cancel()
            autoPlayJob = scope.launch(mainCoroutineDispatcher) {
                delay(150)
                val autoPlayEligibleReceivers = autoPlayReceiverDecider.getEligibleAutoPlayReceivers(
                        visibleCards = visibleCards,
                        itemCount = widgetCardsContainer.layoutManager?.itemCount ?: 0,
                        maxAutoPlay = FAKE_MAX_AUTOPLAY
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
    }

    fun onPause() {
        videoPlayerMap.keys.forEach { it.pause() }
    }

    fun onResume() {
        videoPlayerMap.keys.forEach { it.resume() }
    }

    fun controlWidget(widget: PlayWidgetView) {
        widget.setWidgetViewListener(widgetViewListener)
    }

    fun configureAutoPlay(widget: PlayWidgetView, config: PlayWidgetConfigUiModel) = synchronized(this@PlayWidgetAutoPlayCoordinator) {
        if (config.autoPlay) {
            videoPlayerMap.keys.forEach { it.release() }
            videoPlayerMap.clear()
            return@synchronized
        }

        if (videoPlayerMap.size < FAKE_MAX_AUTOPLAY) {
            videoPlayerMap.putAll(
                    List(FAKE_MAX_AUTOPLAY - videoPlayerMap.size) {
                        PlayVideoPlayer(widget.context) to null
                    }
            )
        } /** else if (videoPlayerMap.size > FAKE_MAX_AUTOPLAY) {
    videoPlayerMap.
    val lastPlayer = videoPlayerList.removeAt(videoPlayerList.lastIndex)
    lastPlayer.release()
    } **/
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

    companion object {
        private const val FAKE_MAX_AUTOPLAY = 3
    }
}