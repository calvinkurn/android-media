package com.tokopedia.play.broadcaster.pusher

import android.content.Context
import android.os.Handler
import android.view.SurfaceHolder
import com.tokopedia.broadcaster.revamp.Broadcaster
import com.tokopedia.broadcaster.revamp.state.BroadcastInitState
import com.tokopedia.broadcaster.revamp.state.BroadcastState
import com.tokopedia.broadcaster.revamp.util.statistic.BroadcasterMetric
import com.tokopedia.play.broadcaster.di.ActivityRetainedScope
import com.tokopedia.play.broadcaster.pusher.state.PlayBroadcasterState
import com.tokopedia.play.broadcaster.ui.model.config.BroadcastingConfigUIModel
import com.tokopedia.remoteconfig.RemoteConfig
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by meyta.taliti on 03/03/22.
 */
class PlayBroadcaster(
    activityContext: Context,
    handler: Handler?,
    private val broadcaster: Broadcaster,
    private val callback: Callback,
    private val remoteConfig: RemoteConfig,
    private val broadcastingConfigUIModel: BroadcastingConfigUIModel,
) : Broadcaster by broadcaster {

    @ActivityRetainedScope
    class Factory @Inject constructor(
        private val broadcaster: Broadcaster,
    ) {
        fun create(
            activityContext: Context,
            handler: Handler?,
            callback: Callback,
            remoteConfig: RemoteConfig,
            broadcastingConfigUIModel: BroadcastingConfigUIModel,
        ): PlayBroadcaster {
            return PlayBroadcaster(activityContext, handler, broadcaster, callback, remoteConfig, broadcastingConfigUIModel)
        }
    }

    private var mLastIngestUrl = ""
    private var isStartedBefore = false

    private val broadcastListener = object : Broadcaster.Listener {
        override fun onBroadcastInitStateChanged(state: BroadcastInitState) {
            callback.onBroadcastInitStateChanged(state)
        }

        override fun onBroadcastStateChanged(state: BroadcastState) {
            val newState = when(state) {
                is BroadcastState.Error -> PlayBroadcasterState.Error(state.cause)
                BroadcastState.Started -> PlayBroadcasterState.Started
                BroadcastState.Recovered -> PlayBroadcasterState.Recovered
                else -> null
            }
            if (newState != null) callback.onBroadcastStateChanged(newState)
        }

        override fun onBroadcastStatisticUpdate(metric: BroadcasterMetric) {
            callback.onBroadcastStatisticUpdate(metric)
        }
    }

    init {
        broadcaster.init(activityContext, handler)
        broadcaster.addListener(broadcastListener)
        enableDebugMonitoring()
    }

    /**
     * start with cached rtmpUrl
     */
    fun start() {
        if (mLastIngestUrl.isBlank()) return
        start(mLastIngestUrl)
    }

    override fun start(rtmpUrl: String) {
        mLastIngestUrl = rtmpUrl
        broadcaster.start(rtmpUrl)
        isStartedBefore = true
    }

    override fun create(holder: SurfaceHolder, surfaceSize: Broadcaster.Size) {
        broadcaster.create(holder, surfaceSize)
        updateAspectFrameSize()
        Timber.d("datanyaa $broadcastingConfigUIModel")
    }

    override fun flip() {
        broadcaster.flip()
        updateAspectFrameSize()
    }

    override fun stop() {
        isStartedBefore = false
        broadcaster.release()
        callback.onBroadcastStateChanged(PlayBroadcasterState.Stopped)
    }

    override fun destroy() {
        broadcaster.removeListener(broadcastListener)
        broadcaster.destroy()
    }

    fun resume(shouldContinue: Boolean) {
        callback.onBroadcastStateChanged(
            PlayBroadcasterState.Resume(
                startedBefore = isStartedBefore,
                shouldContinue = shouldContinue
            )
        )
    }

    fun pause() {
        callback.onBroadcastStateChanged(PlayBroadcasterState.Paused)
    }

    private fun updateAspectFrameSize() {
        val size = broadcaster.activeCameraVideoSize ?: return
        callback.updateAspectRatio(size)
    }

    private fun enableDebugMonitoring() {
        val monitoringInterval = remoteConfig.getLong(FIREBASE_REMOTE_CONFIG_KEY_BRO_MONITORING, 0)
        if (monitoringInterval > 0) broadcaster.enableStatistic(TimeUnit.SECONDS.toMillis(monitoringInterval))
    }

    interface Callback {
        fun updateAspectRatio(activeCameraVideoSize: Broadcaster.Size)
        fun onBroadcastInitStateChanged(state: BroadcastInitState)
        fun onBroadcastStateChanged(state: PlayBroadcasterState)
        fun onBroadcastStatisticUpdate(metric: BroadcasterMetric)
    }

    companion object {
        const val RETRY_TIMEOUT = 3000L
        private const val FIREBASE_REMOTE_CONFIG_KEY_BRO_MONITORING = "android_mainapp_play_broadcaster_monitoring"
    }
}
