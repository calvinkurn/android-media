package com.tokopedia.broadcaster.revamp

import android.content.Context
import android.os.Handler
import android.view.SurfaceHolder
import com.tokopedia.broadcaster.revamp.state.BroadcastInitState
import com.tokopedia.broadcaster.revamp.state.BroadcastState
import com.tokopedia.broadcaster.revamp.util.statistic.BroadcasterMetric

/**
 * Created by meyta.taliti on 01/03/22.
 */
interface Broadcaster {

    fun addListener(listener: Listener)

    fun removeListener(listener: Listener)

    fun init(activityContext: Context, handler: Handler?)

    fun create(holder: SurfaceHolder, surfaceSize: Size)

    fun updateSurfaceSize(surfaceSize: Size)

    fun start(rtmpUrl: String)

    fun retry()

    fun stop()

    fun release()

    fun destroy()

    fun flip()

    fun snapShot()

    fun enableStatistic(interval: Long)

    val broadcastState: BroadcastState

    val broadcastInitState: BroadcastInitState

    /**
     * please update aspect ratio to actual value, after create() & flip()
     */
    val activeCameraVideoSize: Size?

    interface Listener {

        fun onBroadcastStateChanged(state: BroadcastState) {}

        fun onBroadcastInitStateChanged(state: BroadcastInitState) {}

        fun onBroadcastStatisticUpdate(metric: BroadcasterMetric) {}
    }

    data class Size(
        val width: Int,
        val height: Int
    )
}