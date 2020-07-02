package com.tokopedia.play.broadcaster.pusher

import android.view.SurfaceView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherErrorType
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherInfoState
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherNetworkState
import com.tokopedia.play.broadcaster.pusher.timer.PlayPusherTimer
import com.tokopedia.play.broadcaster.pusher.timer.PlayPusherTimerListener


/**
 * Created by mzennis on 24/05/20.
 */
class PlayPusherImplNoop(private val builder: PlayPusherBuilder) : PlayPusher {

    private val _observableInfoState = MutableLiveData<PlayPusherInfoState>()
    private val _observableNetworkState = MutableLiveData<PlayPusherNetworkState>()

    //TODO("for testing only")
    private var mTimer: PlayPusherTimer? = null
    private var isPushing: Boolean = false

    override fun create() {
        _observableInfoState.postValue(PlayPusherInfoState.Error(PlayPusherErrorType.UnSupportedDevice))
    }

    override fun startPreview(surfaceView: SurfaceView) {}

    override fun stopPreview() {}

    //TODO("for testing only")
    override fun startPush(ingestUrl: String, onActive: () -> Unit) {
        if (!isPushing) {
            mTimer?.start()
            this.isPushing = true
            onActive()
        }
    }

    override fun restartPush() {}

    //TODO("for testing only")
    override fun stopPush(onStop: () -> Unit) {
        if (isPushing) {
            mTimer?.stop()
            this.isPushing = false
            onStop()
        }
    }

    override fun switchCamera() {}

    //TODO("for testing only")
    override fun resume(onActive: () -> Unit) {
        if (!isPushing) {
            mTimer?.resume()
            onActive()
        }
    }

    //TODO("for testing only")
    override fun pause(onPause: () -> Unit) {
        if (isPushing) {
            mTimer?.pause()
            this.isPushing = false
            onPause()
        }
    }

    override fun destroy() {
        mTimer?.destroy()
    }

    //TODO("for testing only")
    override fun addMaxStreamDuration(millis: Long) {
        this.mTimer = PlayPusherTimer(builder.context, millis, object: PlayPusherTimerListener {
            override fun onCountDownActive(timeLeft: String) {
                _observableInfoState.postValue(PlayPusherInfoState.TimerActive(timeLeft))
            }

            override fun onCountDownAlmostFinish(minutesUntilFinished: Long) {
                _observableInfoState.postValue(PlayPusherInfoState.TimerAlmostFinish(minutesUntilFinished))
            }

            override fun onCountDownFinish(timeElapsed: String) {
                stopPush()
                _observableInfoState.postValue(PlayPusherInfoState.TimerFinish(timeElapsed))
            }

            override fun onReachMaximumPauseDuration() {
                _observableInfoState.postValue(PlayPusherInfoState.Error(PlayPusherErrorType.ReachMaximumPauseDuration))
            }
        })
    }

    //TODO("for testing only")
    override fun addMaxPauseDuration(millis: Long) {
        this.mTimer?.pauseDuration = millis
    }

    override fun getObservablePlayPusherInfoState(): LiveData<PlayPusherInfoState> {
        return _observableInfoState
    }

    override fun getObservablePlayPusherNetworkState(): LiveData<PlayPusherNetworkState> {
        return _observableNetworkState
    }

    override fun isPushing(): Boolean {
        return isPushing
    }
}