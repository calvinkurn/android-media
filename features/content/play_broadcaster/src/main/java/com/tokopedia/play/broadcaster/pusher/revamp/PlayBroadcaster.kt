package com.tokopedia.play.broadcaster.pusher.revamp

import android.content.Context
import android.os.Handler
import android.view.SurfaceHolder
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.broadcaster.revamp.Broadcaster
import com.tokopedia.broadcaster.revamp.util.log.DefaultBroadcasterLogger
import com.tokopedia.play.broadcaster.di.ActivityRetainedScope
import com.tokopedia.play.broadcaster.pusher.mediator.PlayLivePusherMediator
import com.tokopedia.play.broadcaster.pusher.timer.PlayLivePusherTimer
import com.tokopedia.play.broadcaster.pusher.timer.PlayLivePusherTimerListener
import com.tokopedia.play.broadcaster.ui.model.DurationConfigUiModel
import javax.inject.Inject

/**
 * Created by meyta.taliti on 03/03/22.
 */
class PlayBroadcaster(
    private val activityContext: Context,
    private val handler: Handler?,
    private val callback: Callback,
    private val broadcaster: Broadcaster,
    private val localCache: LocalCacheHandler,
    private val timer: PlayLivePusherTimer,
) : Broadcaster by broadcaster, Broadcaster.Callback {

    @ActivityRetainedScope
    class Factory @Inject constructor(
        private val broadcaster: Broadcaster,
        private val localCache: LocalCacheHandler,
        private val timer: PlayLivePusherTimer,
    ) {
        fun create(
            activityContext: Context,
            handler: Handler?,
            callback: Callback,
        ): PlayBroadcaster {
            return PlayBroadcaster(
                activityContext,
                handler,
                callback,
                broadcaster,
                localCache,
                timer
            )
        }
    }

    init {
        broadcaster.setCallback(this)
    }

    val remainLiveDuration: Long
        get() = timer.remainingDurationInMillis

    private var isBroadcastStarted = false
    private var mPauseDuration = 0L
    private var mMaxDuration = 0L

    // call this onDestroy() to avoid memory leak
    fun destroy() {
        timer.destroy()
        broadcaster.setCallback(null)
    }

    fun setCountUpCallback(callback: PlayLivePusherTimerListener) {
        timer.setListener(callback)
    }

    fun setupDuration(durationConfig: DurationConfigUiModel) {
        mPauseDuration = durationConfig.pauseDuration
        mMaxDuration = durationConfig.maxDuration
        timer.setDuration(durationConfig.remainingDuration, mMaxDuration)
    }

    fun setCurrentDuration(duration: Long) {
        timer.restart(duration, mMaxDuration)
    }

    fun startCountUp() {
        if (isBroadcastStarted) timer.resume()
        else timer.start()
    }

    fun start(
        rtmpUrl: String,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit,
    ) {
        broadcaster.setLogger(object : DefaultBroadcasterLogger() {
            override fun e(msg: String) {
                super.e(msg)
                onError(msg)
            }
        })
        broadcaster.start(rtmpUrl)
        isBroadcastStarted = true
        onSuccess()
        removeLastPauseMillis()
    }

    override fun create(holder: SurfaceHolder, surfaceSize: Broadcaster.Size) {
        broadcaster.create(holder, surfaceSize)
        updateAspectFrameSize()
    }

    override fun flip() {
        broadcaster.flip()
        updateAspectFrameSize()
    }

    fun shouldStop() {
        isBroadcastStarted = false
        timer.stop()
        broadcaster.release()
    }

    fun pause() {
        timer.pause()
        setLastPauseMillis()
    }

    // info: only set false when user manually click stop
    fun isBroadcastStartedBefore() = isBroadcastStarted

    fun isEligibleContinueBroadcast(): Boolean {
        val lastPauseMillis = localCache.getLong(PlayLivePusherMediator.KEY_PAUSE_TIME, 0L)
        val currentMillis = System.currentTimeMillis()
        if (lastPauseMillis > 0 && ((currentMillis - lastPauseMillis) > mPauseDuration)) {
            localCache.remove(PlayLivePusherMediator.KEY_PAUSE_TIME)
            return true
        }
        return false
    }

    override fun getHandler(): Handler? {
        return handler
    }

    override fun getActivityContext(): Context = activityContext

    interface Callback {
        fun updateAspectRatio(aspectRatio: Double)
    }

    private fun setLastPauseMillis() {
        localCache.putLong(KEY_PAUSE_TIME, System.currentTimeMillis())
        localCache.applyEditor()
    }

    private fun removeLastPauseMillis() {
        localCache.remove(PlayLivePusherMediator.KEY_PAUSE_TIME)
        localCache.applyEditor()
    }

    private fun updateAspectFrameSize() {
        val size = broadcaster.activeCameraVideoSize ?: return
        callback.updateAspectRatio(size.height.toDouble() / size.width.toDouble())
    }


    companion object {
        const val KEY_PAUSE_TIME = "play_broadcast_pause_time"
    }
}