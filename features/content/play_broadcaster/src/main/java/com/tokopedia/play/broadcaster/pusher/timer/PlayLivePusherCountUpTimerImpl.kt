package com.tokopedia.play.broadcaster.pusher.timer

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 23, 2021
 */
class PlayLivePusherCountUpTimerImpl @Inject constructor(
    private val cacheHandler: LocalCacheHandler,
    private val dispatcher: CoroutineDispatchers
) : PlayLivePusherCountDownTimer {

    override val remainingDurationInMillis: Long
        get() = mRemainingMillis

    private var job: Job? = null
    private var mDuration: Long = 0L
    private var mRemainingMillis: Long = 0L

    private var mListener: PlayLivePusherCountDownTimerListener? = null

    override fun setDuration(duration: Long) {
        setupDuration(duration)
    }

    override fun setListener(listener: PlayLivePusherCountDownTimerListener) {
        mListener = listener
    }

    override fun start() {
        startCountUp()
    }

    override fun stop() {
        job?.cancel()
        removeLastDurationMillis()
    }

    override fun restart(duration: Long) {
        setupDuration(duration)
        startCountUp()
    }

    override fun resume() {
        val lastDuration = cacheHandler.getLong(KEY_DURATION_MILLIS, 0L)
        restart(lastDuration)
    }

    override fun pause() {
        job?.cancel()
        saveLastDurationMillis()
    }

    override fun destroy() {
        job?.cancel()
        job = null
        mListener = null
    }

    private fun setupDuration(duration: Long) {
        this.mDuration = duration
        this.mRemainingMillis = mDuration
    }

    private fun startCountUp() {
        job?.cancel()
        job = CoroutineScope(dispatcher.io).launch {
            while(true) {

                delay(DEFAULT_INTERVAL)
                mDuration += DEFAULT_INTERVAL

                withContext(Dispatchers.Main) {
                    mListener?.onCountDownTimerActive(mDuration)
                }
            }
        }
    }

    private fun removeLastDurationMillis() {
        cacheHandler.remove(KEY_DURATION_MILLIS)
    }

    private fun saveLastDurationMillis() {
        cacheHandler.putLong(KEY_DURATION_MILLIS, mDuration)
        cacheHandler.applyEditor()
    }

    companion object {
        const val DEFAULT_INTERVAL = 1000L

        const val KEY_DURATION_MILLIS = "play_broadcast_duration_millis"
    }
}