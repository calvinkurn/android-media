package com.tokopedia.play.broadcaster.pusher.mediator

import android.content.Context
import android.os.Handler
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.broadcaster.LiveBroadcaster
import com.tokopedia.broadcaster.data.BroadcasterConfig
import com.tokopedia.broadcaster.listener.BroadcasterListener
import com.tokopedia.broadcaster.mediator.LivePusherStatistic
import com.tokopedia.broadcaster.state.*
import com.tokopedia.broadcaster.widget.SurfaceAspectRatioView
import com.tokopedia.play.broadcaster.pusher.*
import com.tokopedia.play.broadcaster.pusher.mediator.mapper.map
import com.tokopedia.play.broadcaster.pusher.timer.*
import com.tokopedia.play.broadcaster.util.error.PlayLivePusherException
import com.tokopedia.play.broadcaster.util.error.isNetworkTrouble

class LiveBroadcasterMediator(
    private val livePusher: LiveBroadcaster,
    private val cacheHandler: LocalCacheHandler,
    private val mTimer: PlayLivePusherTimer,
) : LiveBroadcaster by livePusher, PusherMediator {

    override val remainingDurationInMillis: Long
        get() = mTimer.remainingDurationInMillis

    override val ingestUrl: String
        get() = livePusher.ingestUrl

    override val config: BroadcasterConfig
        get() = livePusher.config

    private val mListeners = mutableListOf<PlayLivePusherMediatorListener>()

    private var mPauseDuration = 0L

    private val livePusherListener = object : BroadcasterListener {
        override fun onNewLivePusherState(state: BroadcasterState) {
            val pusherMediatorState = parseState(state)
            broadcastStateChanged(pusherMediatorState)

            when {
                state.isPaused -> mTimer.pause()
                state.isResumed -> mTimer.resume()
                state.isStopped -> removeLastPauseMillis()
                pusherMediatorState is PlayLivePusherMediatorState.Error -> {
                    if (pusherMediatorState.error.type.isNetworkTrouble) doAutoReconnect()
                }
            }
        }

        override fun onUpdateLivePusherStatistic(log: LivePusherStatistic) {
            broadcastStatsUpdated(log)
        }
    }

    private val countDownTimerListener = object : PlayLivePusherTimerListener {
        override fun onTimerActive(timeInMillis: Long) {
            broadcastTimerActive(timeInMillis)
        }

        override fun onTimerFinish() {
            broadcastTimerFinish()
        }
    }

    init {
        livePusher.setListener(livePusherListener)
        mTimer.setListener(countDownTimerListener)
    }

    override fun init(context: Context, handler: Handler) {
        livePusher.init(context, handler)
    }

    override fun switchCamera() {
        livePusher.switchCamera()
    }

    override fun start(url: String) {
        livePusher.start(url)
    }

    override fun resume() {
        livePusher.resume()
    }

    override fun pause() {
        livePusher.pause()
    }

    override fun stop() {
        livePusher.stop()
    }

    override fun release() {
        livePusher.release()
    }

    override fun getLivePusherState(): PlayLivePusherState {
        return state.map()
    }

    override fun setLiveStreamingDuration(durationInMillis: Long, maxDuration: Long) {
        mTimer.setDuration(durationInMillis, maxDuration)
    }

    override fun setLiveStreamingPauseDuration(durationInMillis: Long) {
        mPauseDuration = durationInMillis
    }

    override fun startLiveStreaming(ingestUrl: String, withTimer: Boolean) {
        livePusher.start(ingestUrl)
        if (withTimer) startLiveTimer()
    }

    override fun stopLiveStreaming() {
        mTimer.stop()
        livePusher.stop()
    }

    override fun startLiveTimer() {
        mTimer.start()
    }

    override fun restartLiveTimer(duration: Long, maxDuration: Long) {
        mTimer.restart(duration, maxDuration)
    }

    override fun destroy() {
        mTimer.destroy()
        release()
    }

    override fun addListener(listener: PlayLivePusherMediatorListener) {
        mListeners.add(listener)
    }

    override fun removeListener(listener: PlayLivePusherMediatorListener) {
        mListeners.remove(listener)
    }

    override fun clearListener() {
        mListeners.clear()
    }

    override fun onCameraChanged(surfaceView: SurfaceAspectRatioView) {
        if (livePusher.state.isStopped) return

        livePusher.startPreview(surfaceView)
        if (livePusher.state.isPaused) {
            if (isReachMaxPauseDuration()) broadcastReachMaxPauseDuration()
            else broadcastStateChanged(PlayLivePusherMediatorState.Resume(false))
        }
    }

    override fun onCameraDestroyed() {
        if (livePusher.state.isPushing) {
            livePusher.pause()
            setLastPauseMillis()
        }
        livePusher.stopPreview()
    }

    /**
     * Private methods
     */
    private fun parseState(pusherState: BroadcasterState): PlayLivePusherMediatorState {
        return when (pusherState) {
            BroadcasterState.Connecting -> PlayLivePusherMediatorState.Connecting
            is BroadcasterState.Error -> PlayLivePusherMediatorState.Error(
                PlayLivePusherException(pusherState.reason)
            )
            BroadcasterState.Paused -> PlayLivePusherMediatorState.Paused
            BroadcasterState.Recovered -> PlayLivePusherMediatorState.Recovered
            BroadcasterState.Resumed -> PlayLivePusherMediatorState.Resume(true)
            BroadcasterState.Started -> PlayLivePusherMediatorState.Started
            BroadcasterState.Idle -> PlayLivePusherMediatorState.Idle
            BroadcasterState.Stopped -> PlayLivePusherMediatorState.Stopped
        }
    }

    private fun broadcastStateChanged(state: PlayLivePusherMediatorState) {
        mListeners.forEach { it.onLivePusherStateChanged(state) }
    }

    private fun broadcastStatsUpdated(stats: LivePusherStatistic) {
        mListeners.forEach { it.onLivePusherStatsUpdated(stats) }
    }

    private fun broadcastTimerActive(timeInMillis: Long) {
        mListeners.forEach { it.onLiveTimerActive(timeInMillis) }
    }

    private fun broadcastTimerFinish() {
        mListeners.forEach { it.onLiveTimerFinish() }
    }

    private fun broadcastReachMaxPauseDuration() {
        mListeners.forEach { it.onReachMaximumPausePeriod() }
    }

    private fun setLastPauseMillis() {
        cacheHandler.putLong(KEY_PAUSE_TIME, System.currentTimeMillis())
        cacheHandler.applyEditor()
    }

    private fun doAutoReconnect() {
        livePusher.reconnect()
    }

    private fun isReachMaxPauseDuration(): Boolean {
        val lastPauseMillis = cacheHandler.getLong(KEY_PAUSE_TIME, 0L)
        val currentMillis = System.currentTimeMillis()
        if (lastPauseMillis > 0 && ((currentMillis - lastPauseMillis) > mPauseDuration)) {
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