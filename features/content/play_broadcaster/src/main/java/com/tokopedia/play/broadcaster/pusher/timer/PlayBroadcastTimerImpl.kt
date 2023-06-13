package com.tokopedia.play.broadcaster.pusher.timer

import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.play.broadcaster.util.countup.PlayCountUp
import com.tokopedia.play.broadcaster.util.countup.PlayCountUpListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 23, 2021
 */
class PlayBroadcastTimerImpl @Inject constructor(
    private val cacheHandler: LocalCacheHandler,
    private val countUp: PlayCountUp,
) : PlayBroadcastTimer {

    override val remainingDuration: Long
        get() = mMaxDuration - mDuration

    override val isPastPauseDuration: Boolean
        get() {
            val lastPauseMillis = cacheHandler.getLong(KEY_PAUSE_TIME, 0L)
            val currentMillis = System.currentTimeMillis()
            if (lastPauseMillis > 0 && ((currentMillis - lastPauseMillis) > mPauseDuration)) {
                removeLastPauseMillis()
                return true
            }
            return false
        }

    private var mDuration: Long = 0L
    private var mMaxDuration: Long = 0L

    private var mPauseDuration = 0L

    private var mListener: PlayBroadcastTimer.Listener? = null

    override val stateChanged: Flow<PlayBroadcastTimerState>
        get() = callbackFlow {
            val listener = object : PlayBroadcastTimer.Listener {
                override fun onStateChanged(state: PlayBroadcastTimerState) {
                    trySend(state)
                }
            }

            setListener(listener)
            awaitClose { setListener(null) }
        }


    override fun setupDuration(duration: Long, maxDuration: Long) {
        removeLastDurationMillis()
        setupDuration(duration)
        this.mMaxDuration = maxDuration
    }

    override fun setupPauseDuration(duration: Long) {
        removeLastPauseMillis()
        mPauseDuration = duration
    }

    override fun setListener(listener: PlayBroadcastTimer.Listener?) {
        mListener = listener
    }

    override fun start() {
        val lastDuration = cacheHandler.getLong(KEY_DURATION_MILLIS, 0L)
        if (lastDuration > 0) restart(lastDuration) else startCountUp()
    }

    override fun stop() {
        countUp.stop()
        removeLastDurationMillis()
        removeLastPauseMillis()
    }

    override fun restart(duration: Long) {
        setupDuration(duration)
        startCountUp()
    }

    override fun pause() {
        countUp.stop()
        saveLastDurationMillis()
        saveLastPauseMillis()
    }

    override fun destroy() {
        countUp.stop()
        removeLastDurationMillis()
        removeLastPauseMillis()
        mListener = null
    }

    private fun setupDuration(duration: Long) {
        this.mDuration = duration
    }

    private fun startCountUp() {
        countUp.stop()
        countUp.start(mDuration, mMaxDuration)
        countUp.setListener(object: PlayCountUpListener{
            override fun onTick(duration: Long) {
                mDuration = duration
                mListener?.onStateChanged(PlayBroadcastTimerState.Active(mDuration))
            }

            override fun onFinish() {
                mListener?.onStateChanged(PlayBroadcastTimerState.Finish)
            }
        })
    }

    private fun removeLastDurationMillis() {
        cacheHandler.remove(KEY_DURATION_MILLIS)
        cacheHandler.applyEditor()
    }

    private fun saveLastDurationMillis() {
        cacheHandler.putLong(KEY_DURATION_MILLIS, mDuration)
        cacheHandler.applyEditor()
    }

    private fun saveLastPauseMillis() {
        cacheHandler.putLong(KEY_PAUSE_TIME, System.currentTimeMillis())
        cacheHandler.applyEditor()
    }

    private fun removeLastPauseMillis() {
        cacheHandler.remove(KEY_PAUSE_TIME)
        cacheHandler.applyEditor()
    }

    companion object {
        const val KEY_DURATION_MILLIS = "play_broadcast_duration_millis"
        const val KEY_PAUSE_TIME = "play_broadcast_pause_time"
    }
}