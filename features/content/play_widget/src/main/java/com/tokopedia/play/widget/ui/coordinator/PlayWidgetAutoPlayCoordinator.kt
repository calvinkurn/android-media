package com.tokopedia.play.widget.ui.coordinator

import android.view.View
import com.tokopedia.play.widget.player.PlayVideoPlayer
import com.tokopedia.play.widget.player.PlayVideoPlayerReceiver
import com.tokopedia.play.widget.ui.PlayWidgetView
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

    private val widgetViewListener = object : PlayWidgetViewListener {

        override fun onWidgetVisibleCardsChanged(visibleCards: List<View>) {
            autoPlayJob?.cancel()
            autoPlayJob = scope.launch(mainCoroutineDispatcher) {
                delay(200)
                val playerReceivers = visibleCards.filterIsInstance<PlayVideoPlayerReceiver>()
                val autoPlayEligibleReceivers = getEligibleAutoPlayReceivers(playerReceivers)

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

    private fun getEligibleAutoPlayReceivers(playerReceivers: List<PlayVideoPlayerReceiver>): List<PlayVideoPlayerReceiver> {
        return playerReceivers.take(FAKE_MAX_AUTOPLAY)
    }

    companion object {
        private const val FAKE_MAX_AUTOPLAY = 3
    }
}