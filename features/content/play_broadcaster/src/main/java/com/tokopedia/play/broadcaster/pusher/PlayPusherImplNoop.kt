package com.tokopedia.play.broadcaster.pusher

import android.view.SurfaceView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherInfoState
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherNetworkState
import com.tokopedia.play.broadcaster.pusher.timer.PlayPusherCountDownTimer
import com.tokopedia.play.broadcaster.pusher.timer.PlayPusherCountDownTimerListener


/**
 * Created by mzennis on 24/05/20.
 */
class PlayPusherImplNoop(private val builder: PlayPusher.Builder) : PlayPusher {

    private val _observableInfoState = MutableLiveData<PlayPusherInfoState>()
    private val _observableNetworkState = MutableLiveData<PlayPusherNetworkState>()

    //TODO("for testing only")
    private var mCountDownTimer: PlayPusherCountDownTimer? = null

    override fun create() {
        _observableInfoState.value = PlayPusherInfoState.Error
    }

    override fun startPreview(surfaceView: SurfaceView) {
    }

    override fun stopPreview() {
    }

    override fun startPush(ingestUrl: String) {
        mCountDownTimer?.start()
    }

    override fun restartPush() {
        mCountDownTimer?.start()
    }

    override fun stopPush() {
        mCountDownTimer?.stop()
    }

    override fun switchCamera() {
    }

    override fun resume() {
        mCountDownTimer?.start()
    }

    override fun pause() {
        mCountDownTimer?.stop()
    }

    override fun destroy() {
        mCountDownTimer?.stop()
    }

    override fun addMaxStreamDuration(millis: Long) {
        this.mCountDownTimer = PlayPusherCountDownTimer(builder.context, millis)
        this.mCountDownTimer?.addCallback(mCountDownTimerListener)
    }

    override fun getObservablePlayPusherInfoState(): LiveData<PlayPusherInfoState> {
        return _observableInfoState
    }

    override fun getObservablePlayPusherNetworkState(): LiveData<PlayPusherNetworkState> {
        return _observableNetworkState
    }

    private val mCountDownTimerListener = object: PlayPusherCountDownTimerListener {

        override fun onCountDownActive(millisUntilFinished: Long) {
            _observableInfoState.value  = PlayPusherInfoState.Active(millisUntilFinished)
        }

        override fun onCountDownFinish() {
            stopPush()
            _observableInfoState.value = PlayPusherInfoState.Finish
        }

    }
}