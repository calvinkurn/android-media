package com.tokopedia.play.broadcaster.pusher

import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.play.broadcaster.util.state.PlayLivePusherViewStateListener.Companion.errorStateParser
import com.tokopedia.play.broadcaster.view.custom.SurfaceAspectRatioView
import com.tokopedia.play.broadcaster.view.state.isNetworkTrouble


/**
 * Created by mzennis on 04/06/21.
 */
class PlayLivePusherMediator(
    private val livePusher: PlayLivePusher,
    private val cacheHandler: LocalCacheHandler
) : PlayLivePusher by livePusher {

    private var pauseDuration = 0L

    private val mListeners = mutableListOf<PlayLivePusherMediatorListener>()

    private val livePusherListener = object : PlayLivePusherListener {
        override fun onNewLivePusherState(pusherState: PlayLivePusherState) {
            broadcastStateChanged(pusherState)

            if (pusherState.isStopped) removeLastPauseMillis()
            if (pusherState is PlayLivePusherState.Error) triggerAutoReconnect(pusherState.reason)
        }
    }

    init {
        livePusher.setListener(livePusherListener)
    }

    fun addListener(listener: PlayLivePusherMediatorListener) {
        mListeners.add(listener)
    }

    fun removeListener(listener: PlayLivePusherMediatorListener) {
        mListeners.remove(listener)
    }

    fun onCameraChanged(surfaceView: SurfaceAspectRatioView) {
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

    private fun triggerAutoReconnect(reason: String) {
        val errorState = errorStateParser(reason)
        if (errorState.isNetworkTrouble) livePusher.reconnect()
    }

    private fun broadcastStateChanged(state: PlayLivePusherState) {
        mListeners.forEach { it.onLivePusherStateChanged(state) }
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