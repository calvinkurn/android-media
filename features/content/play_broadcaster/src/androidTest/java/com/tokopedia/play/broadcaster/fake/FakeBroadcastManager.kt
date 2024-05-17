package com.tokopedia.play.broadcaster.fake

import android.content.Context
import android.os.Handler
import android.view.SurfaceHolder
import com.tokopedia.broadcaster.revamp.Broadcaster
import com.tokopedia.broadcaster.revamp.state.BroadcastInitState
import com.tokopedia.broadcaster.revamp.state.BroadcastState
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by Jonathan Darwin on 25 March 2024
 */
class FakeBroadcastManager : Broadcaster {

    private val mListeners: ConcurrentLinkedQueue<Broadcaster.Listener> = ConcurrentLinkedQueue()

    override fun setConfig(audioRate: String, videoRate: String, videoFps: String) {
        
    }

    override fun addListener(listener: Broadcaster.Listener) {
        mListeners.add(listener)
    }

    override fun removeListener(listener: Broadcaster.Listener) {
        mListeners.remove(listener)
    }

    override fun init(activityContext: Context, handler: Handler?) {
        
    }

    override fun setupThread(withByteplus: Boolean) {
        
    }

    override fun create(
        holder: SurfaceHolder,
        surfaceSize: Broadcaster.Size,
        withByteplus: Boolean
    ) {
        
    }

    override fun updateSurfaceSize(surfaceSize: Broadcaster.Size) {
        
    }

    override fun start(rtmpUrl: String) {
        broadcastState(BroadcastState.Started)
    }

    override fun retry() {
        
    }

    override fun stop() {
        
    }

    override fun release() {
        
    }

    override fun destroy() {
        
    }

    override fun flip() {
        
    }

    override fun snapShot() {
        
    }

    override fun enableStatistic(interval: Long) {
        
    }

    override fun setFaceFilter(faceFilterId: String, value: Float): Boolean {
        return true
    }

    override fun removeFaceFilter() {
        
    }

    override fun setPreset(presetId: String, value: Float): Boolean {
        return true
    }

    override fun removePreset() {
        
    }

    private fun broadcastState(state: BroadcastState) {
        mListeners.forEach { it.onBroadcastStateChanged(state) }
    }

    override val broadcastState: BroadcastState
        get() = BroadcastState.Started
    override val broadcastInitState: BroadcastInitState
        get() = BroadcastInitState.Initialized
    override val activeCameraVideoSize: Broadcaster.Size?
        get() = null
}
