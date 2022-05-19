package com.tokopedia.play.broadcaster.pusher.timer

import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.play.broadcaster.util.countup.PlayCountUp
import com.tokopedia.play.broadcaster.util.countup.PlayCountUpListener
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 23, 2021
 */
class PlayLivePusherCountUpTimerImpl @Inject constructor(
    private val cacheHandler: LocalCacheHandler,
    private val countUp: PlayCountUp,
) : PlayLivePusherTimer {

    override val remainingDurationInMillis: Long
        get() = mMaxDuration - mDuration

    private var mDuration: Long = 0L
    private var mMaxDuration: Long = 0L

    private var mListener: PlayLivePusherTimerListener? = null

    override fun setDuration(duration: Long, maxDuration: Long) {
        setupDuration(duration, maxDuration)
    }

    override fun setListener(listener: PlayLivePusherTimerListener) {
        mListener = listener
    }

    override fun start() {
        startCountUp()
    }

    override fun stop() {
        countUp.stop()
        removeLastDurationMillis()
    }

    override fun restart(duration: Long, maxDuration: Long) {
        setupDuration(duration, maxDuration)
        startCountUp()
    }

    override fun resume() {
        val lastDuration = cacheHandler.getLong(KEY_DURATION_MILLIS, 0L)
        val lastMaxDuration = cacheHandler.getLong(KEY_MAX_DURATION_MILLIS, 0L)
        restart(lastDuration, lastMaxDuration)
    }

    override fun pause() {
        countUp.stop()
        saveLastDurationMillis()
    }

    override fun destroy() {
        countUp.stop()
        mListener = null
    }

    private fun setupDuration(duration: Long, maxDuration: Long) {
        this.mDuration = duration
        this.mMaxDuration = maxDuration
    }

    private fun startCountUp() {
        countUp.stop()
        countUp.start(mDuration, mMaxDuration)
        countUp.setListener(object: PlayCountUpListener{
            override fun onTick(duration: Long) {
                mDuration = duration
                mListener?.onTimerActive(mDuration)
            }

            override fun onFinish() {
                mListener?.onTimerFinish()
            }
        })
    }

    private fun removeLastDurationMillis() {
        cacheHandler.remove(KEY_DURATION_MILLIS)
        cacheHandler.remove(KEY_MAX_DURATION_MILLIS)
    }

    private fun saveLastDurationMillis() {
        cacheHandler.putLong(KEY_DURATION_MILLIS, mDuration)
        cacheHandler.putLong(KEY_MAX_DURATION_MILLIS, mMaxDuration)
        cacheHandler.applyEditor()
    }

    companion object {
        const val KEY_DURATION_MILLIS = "play_broadcast_duration_millis"
        const val KEY_MAX_DURATION_MILLIS = "play_broadcast_max_duration_millis"
    }
}