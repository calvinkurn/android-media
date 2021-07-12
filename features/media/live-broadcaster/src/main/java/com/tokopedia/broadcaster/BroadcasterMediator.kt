package com.tokopedia.broadcaster

import android.view.SurfaceView
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.broadcaster.data.BroadcasterLogger
import com.tokopedia.broadcaster.listener.BroadcasterListener
import com.tokopedia.broadcaster.listener.BroadcasterMediatorListener
import com.tokopedia.broadcaster.state.BroadcasterState
import com.tokopedia.broadcaster.state.isPaused
import com.tokopedia.broadcaster.state.isPushing
import com.tokopedia.broadcaster.state.isStopped

class BroadcasterMediator(
    private val livePusher: LiveBroadcaster,
    private val cacheHandler: LocalCacheHandler
) : LiveBroadcaster by livePusher {

    private var pauseDuration = 0L

    private val mListeners = mutableListOf<BroadcasterMediatorListener>()

    private val livePusherListener = object : BroadcasterListener {
        override fun onNewLivePusherState(pusherState: BroadcasterState) {
            broadcastStateChanged(pusherState)

            if (pusherState.isStopped) removeLastPauseMillis()
        }

        override fun onUpdateLivePusherStatistic(pusherStatistic: BroadcasterLogger) {
            broadcastStatsUpdated(pusherStatistic)
        }
    }

    init {
        livePusher.setListener(livePusherListener)
    }

    fun addListener(listener: BroadcasterMediatorListener) {
        mListeners.add(listener)
    }

    fun removeListener(listener: BroadcasterMediatorListener) {
        mListeners.remove(listener)
    }

    fun onCameraChanged(surfaceView: SurfaceView) {
        if (livePusher.state.isStopped) return

        livePusher.startPreview(surfaceView)
        if (livePusher.state.isPaused) {
            if (isReachMaxPauseDuration()) broadcastReachMaxPauseDuration()
            else broadcastShouldContinueLiveStreaming()
        }
    }

    fun onCameraDestroyed() {
        if (livePusher.state.isPushing) {
            livePusher.pause()
            setLastPauseMillis()
        }
        livePusher.stopPreview()
    }

    private fun broadcastStateChanged(state: BroadcasterState) {
        mListeners.forEach { it.onLivePusherStateChanged(state) }
    }

    private fun broadcastStatsUpdated(stats: BroadcasterLogger) {
        mListeners.forEach { it.onLivePusherStatsUpdated(stats) }
    }

    private fun broadcastReachMaxPauseDuration() {
        mListeners.forEach { it.onReachMaxPauseDuration() }
    }

    private fun broadcastShouldContinueLiveStreaming() {
        mListeners.forEach { it.onShouldContinueLiveStreaming() }
    }

    fun setPauseDuration(duration: Long) {
        this.pauseDuration = duration
    }

    private fun setLastPauseMillis() {
        cacheHandler.putLong(KEY_PAUSE_TIME, System.currentTimeMillis())
        cacheHandler.applyEditor()
    }

    private fun isReachMaxPauseDuration(): Boolean {
        val lastPauseMillis = cacheHandler.getLong(KEY_PAUSE_TIME, 0L)
        val currentMillis = System.currentTimeMillis()
        if (lastPauseMillis > 0 && ((currentMillis - lastPauseMillis) > pauseDuration)) {
            cacheHandler.remove(KEY_PAUSE_TIME)
            return true
        }
        return false
    }

    private fun removeLastPauseMillis() {
        cacheHandler.remove(KEY_PAUSE_TIME)
    }

    companion object {

        const val KEY_PAUSE_TIME = "play_broadcast_pause_time"
    }
}