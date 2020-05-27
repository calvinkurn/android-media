package com.tokopedia.play.broadcaster.pusher

import android.view.SurfaceView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherInfoState
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherNetworkState


/**
 * Created by mzennis on 24/05/20.
 */
class PlayPusherImplNoop : PlayPusher {

    private val _observableInfoState = MutableLiveData<PlayPusherInfoState>()

    override fun create() {
        _observableInfoState.value = PlayPusherInfoState.Error
    }

    override fun startPreview(surfaceView: SurfaceView) {
    }

    override fun stopPreview() {
    }

    override fun startPush(ingestUrl: String) {
    }

    override fun restartPush() {
    }

    override fun stopPush() {
    }

    override fun switchCamera() {
    }

    override fun resume() {
    }

    override fun pause() {
    }

    override fun destroy() {
    }

    override fun addMaxStreamDuration(millis: Long) {
    }

    override fun getObservablePlayPusherInfoState(): LiveData<PlayPusherInfoState> {
        return _observableInfoState
    }

    override fun getObservablePlayPusherNetworkState(): LiveData<PlayPusherNetworkState>? {
        return null
    }
}