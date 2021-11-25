package com.tokopedia.play.broadcaster.pusher.mediator

import android.content.Context
import android.os.Handler
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.broadcaster.mediator.LivePusherStatistic
import com.tokopedia.broadcaster.widget.SurfaceAspectRatioView
import com.tokopedia.play.broadcaster.pusher.*
import com.tokopedia.play.broadcaster.pusher.timer.PlayLivePusherCountDownTimer
import com.tokopedia.play.broadcaster.pusher.timer.PlayLivePusherCountDownTimerImpl
import com.tokopedia.play.broadcaster.pusher.timer.PlayLivePusherCountDownTimerListener
import com.tokopedia.play.broadcaster.util.error.PlayLivePusherException
import com.tokopedia.play.broadcaster.util.error.isNetworkTrouble

/**
 * Created by mzennis on 04/06/21.
 */
class PlayLivePusherMediator(
    private val livePusher: PlayLivePusher,
    private val cacheHandler: LocalCacheHandler
): PlayLivePusher by livePusher, PusherMediator {

    override val remainingDurationInMillis: Long
        get() = mCountDownTimer.remainingDurationInMillis

    override val ingestUrl: String
        get() = livePusher.ingestUrl

    override val config: PlayLivePusherConfig
        get() = livePusher.config

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

        override fun onUpdateLivePusherStatistic(pusherStatistic: LivePusherStatistic) {
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

    override fun setLiveStreamingDuration(durationInMillis: Long) {
        mCountDownTimer.setDuration(durationInMillis)
    }

    override fun setLiveStreamingPauseDuration(durationInMillis: Long) {
        mPauseDuration = durationInMillis
    }

    override fun startLiveStreaming(ingestUrl: String, withTimer: Boolean) {
        livePusher.start(ingestUrl)
        if (withTimer) startLiveCountDownTimer()
    }

    override fun stopLiveStreaming() {
        mCountDownTimer.stop()
        livePusher.stop()
    }

    override fun startLiveCountDownTimer() {
        mCountDownTimer.start()
    }

    override fun restartLiveCountDownTimer(duration: Long) {
        mCountDownTimer.restart(duration)
    }

    override fun destroy() {
        mCountDownTimer.destroy()
        release()
    }

    override fun addListener(listener: PlayLivePusherMediatorListener) {
        mListeners.add(listener)
    }

    override fun removeListener(listener: PlayLivePusherMediatorListener) {
        mListeners.remove(listener)
    }

    fun clearListener() {
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