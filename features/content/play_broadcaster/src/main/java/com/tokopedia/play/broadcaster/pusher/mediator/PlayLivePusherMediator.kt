package com.tokopedia.play.broadcaster.pusher.mediator

import android.content.Context
import android.os.Handler
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.play.broadcaster.pusher.timer.PlayLivePusherTimer
import com.tokopedia.play.broadcaster.pusher.timer.PlayLivePusherTimerListener
import com.tokopedia.broadcaster.mediator.LivePusherStatistic
import com.tokopedia.broadcaster.widget.SurfaceAspectRatioView
import com.tokopedia.play.broadcaster.pusher.*
import com.tokopedia.play.broadcaster.util.error.PlayLivePusherException
import com.tokopedia.play.broadcaster.util.error.isNetworkTrouble

/**
 * Created by mzennis on 04/06/21.
 */
class PlayLivePusherMediator(
    private val livePusher: PlayLivePusher,
    private val cacheHandler: LocalCacheHandler,
    private val mTimer: PlayLivePusherTimer,
): PlayLivePusher by livePusher, PusherMediator {

    override val remainingDurationInMillis: Long
        get() = mTimer.remainingDurationInMillis

    override val ingestUrl: String
        get() = livePusher.ingestUrl

    override val config: PlayLivePusherConfig
        get() = livePusher.config

    private val mListeners = mutableListOf<PlayLivePusherMediatorListener>()

    private var mPauseDuration = 0L

    private val livePusherListener = object : PlayLivePusherListener {
        override fun onNewLivePusherState(pusherState: PlayLivePusherState) {
            val pusherMediatorState = parseState(pusherState)
            broadcastStateChanged(pusherMediatorState)

            when {
                pusherState.isPaused -> mTimer.pause()
                pusherState.isResumed -> mTimer.resume()
                pusherState.isStopped -> removeLastPauseMillis()
                pusherMediatorState is PlayLivePusherMediatorState.Error -> {
                    if (pusherMediatorState.error.type.isNetworkTrouble) doAutoReconnect()
                }
            }
        }

        override fun onUpdateLivePusherStatistic(pusherStatistic: LivePusherStatistic) {
            broadcastStatsUpdated(pusherStatistic)
        }
    }

    private val timerListener = object : PlayLivePusherTimerListener {
        override fun onTimerActive(timeInMillis: Long) {
            broadcastTimerActive(timeInMillis)
        }

        override fun onTimerFinish() {
            broadcastTimerFinish()
        }
    }

    init {
        livePusher.setListener(livePusherListener)
        mTimer.setListener(timerListener)
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
        return state
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
    private fun parseState(pusherState: PlayLivePusherState): PlayLivePusherMediatorState {
        return when (pusherState) {
            PlayLivePusherState.Connecting -> PlayLivePusherMediatorState.Connecting
            is PlayLivePusherState.Error -> PlayLivePusherMediatorState.Error(
                PlayLivePusherException(pusherState.reason)
            )
            PlayLivePusherState.Paused -> PlayLivePusherMediatorState.Paused
            PlayLivePusherState.Recovered -> PlayLivePusherMediatorState.Recovered
            PlayLivePusherState.Resumed -> PlayLivePusherMediatorState.Resume(true)
            PlayLivePusherState.Started -> PlayLivePusherMediatorState.Started
            PlayLivePusherState.Idle -> PlayLivePusherMediatorState.Idle
            PlayLivePusherState.Stopped -> PlayLivePusherMediatorState.Stopped
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