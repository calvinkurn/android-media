package com.tokopedia.play.widget.ui.coordinator

import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import kotlinx.coroutines.*

/**
 * Created by jegul on 21/10/20
 */
class PlayWidgetAutoRefreshCoordinator(
        private val scope: CoroutineScope,
        mainCoroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
        private val workCoroutineDispatcher: CoroutineDispatcher = Dispatchers.Default,
        private val listener: Listener
) {

    private var timerJob: Job? = null

    fun onPause() {
        stopTimer()
    }

    fun configureAutoRefresh(config: PlayWidgetConfigUiModel) {
        stopTimer()
        if (config.autoRefresh) {
            timerJob = scope.launch {
                initTimer(config.autoRefreshTimer) {
                    listener.onWidgetShouldRefresh()
                }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
    }

    private suspend fun initTimer(durationInSecs: Long, handler: () -> Unit) {
        withContext(workCoroutineDispatcher) {
            delay(durationInSecs * 1000L)
        }
        handler()
    }

    interface Listener {

        fun onWidgetShouldRefresh()
    }
}