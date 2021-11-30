package com.tokopedia.play.broadcaster.pusher

import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.play.broadcaster.pusher.timer.PlayLivePusherCountDownTimer
import com.tokopedia.play.broadcaster.pusher.timer.PlayLivePusherCountDownTimerImpl
import com.tokopedia.play.broadcaster.pusher.timer.PlayLivePusherCountDownTimerListener
import com.tokopedia.play.broadcaster.util.error.PlayLivePusherException
import com.tokopedia.play.broadcaster.util.error.isNetworkTrouble
import com.tokopedia.play.broadcaster.view.custom.SurfaceAspectRatioView


/**
 * Created by mzennis on 04/06/21.
 */
class PlayLivePusherMediator(
    private val livePusher: PlayLivePusher,
    private val cacheHandler: LocalCacheHandler
): PlayLivePusher by livePusher {

    val remainingDurationInMillis: Long
        get() = mCountDownTimer.remainingDurationInMillis

    private val mCountDownTimer: PlayLivePusherCountDownTimer = PlayLivePusherCountDownTimerImpl(cacheHandler)
    private val mListeners = mutableListOf<PlayLivePusherMediatorListener>()

    private var mPauseDuration = 0L

    private val livePusherListener = object : PlayLivePusherListener {
        override fun onNewLivePusherState(pusherState: PlayLivePusherState) {
            val pusherMediatorState = parseState(pusherState)
            broadcastStateChanged(pusherMediatorState)

            when {
                pusherState.isPaused -> mCountDownTimer.pause()
                pusherState.isResumed -> mCountDownTimer.resume()
                pusherState.isStopped -> removeLastPauseMillis()
                pusherMediatorState is PlayLivePusherMediatorState.Error -> {
                    if (pusherMediatorState.error.type.isNetworkTrouble) doAutoReconnect()
                }
            }
        }

        override fun onUpdateLivePusherStatistic(pusherStatistic: PlayLivePusherStatistic) {
            broadcastStatsUpdated(pusherStatistic)
        }
    }

    private val countDownTimerListener = object : PlayLivePusherCountDownTimerListener {
        override fun onCountDownTimerActive(timeInMillis: Long) {
            broadcastCountDownTimerActive(timeInMillis)
        }

        override fun onCountDownTimerFinish() {
            broadcastCountDownTimerFinish()
        }
    }

    init {
        livePusher.setListener(livePusherListener)
        mCountDownTimer.setListener(countDownTimerListener)
    }

    fun setLiveStreamingDuration(durationInMillis: Long) {
        mCountDownTimer.setDuration(durationInMillis)
    }

    fun setLiveStreamingPauseDuration(durationInMillis: Long) {
        mPauseDuration = durationInMillis
    }

    fun startLiveStreaming(ingestUrl: String, withTimer: Boolean = true) {
        livePusher.start(ingestUrl)
        if (withTimer) startLiveCountDownTimer()
    }

    fun stopLiveStreaming() {
        mCountDownTimer.stop()
        livePusher.stop()
    }

    fun startLiveCountDownTimer() {
        mCountDownTimer.start()
    }

    fun restartLiveCountDownTimer(duration: Long) {
        mCountDownTimer.restart(duration)
    }

    fun destroy() {
        mCountDownTimer.destroy()
        release()
    }

    fun addListener(listener: PlayLivePusherMediatorListener) {
        mListeners.add(listener)
    }

    fun removeListener(listener: PlayLivePusherMediatorListener) {
        mListeners.remove(listener)
    }

    fun clearListener() {
        mListeners.clear()
    }

    fun onCameraChanged(surfaceView: SurfaceAspectRatioView) {
        if (livePusher.state.isStopped) return

        livePusher.startPreview(surfaceView)
        if (livePusher.state.isPaused) {
            if (isReachMaxPauseDuration()) broadcastReachMaxPauseDuration()
            else broadcastStateChanged(PlayLivePusherMediatorState.Resume(false))
        }
    }

    fun onCameraDestroyed() {
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

    private fun broadcastStatsUpdated(stats: PlayLivePusherStatistic) {
        mListeners.forEach { it.onLivePusherStatsUpdated(stats) }
    }

    private fun broadcastCountDownTimerActive(timeInMillis: Long) {
        mListeners.forEach { it.onLiveCountDownTimerActive(timeInMillis) }
    }

    private fun broadcastCountDownTimerFinish() {
        mListeners.forEach { it.onLiveCountDownTimerFinish() }
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