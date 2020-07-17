package com.tokopedia.play.broadcaster.pusher

import android.view.SurfaceView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherNetworkState
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherInfoListener
import com.tokopedia.play.broadcaster.pusher.timer.PlayPusherTimer
import com.tokopedia.play.broadcaster.pusher.timer.PlayPusherTimerListener


/**
 * Created by mzennis on 24/05/20.
 */
class PlayPusherImplNoop(private val builder: PlayPusherBuilder) : PlayPusher {

//    private val _observableInfoState = MutableLiveData<PlayPusherTimerInfoState>()
    private val _observableNetworkState = MutableLiveData<PlayPusherNetworkState>()

    //TODO("for testing only")
    private var mTimer: PlayPusherTimer? = null
    private var isPushing: Boolean = false

    override suspend fun create() {}

    override suspend fun startPreview(surfaceView: SurfaceView) {}

    override fun stopPreview() {}

    //TODO("for testing only")
    override suspend fun startPush(ingestUrl: String) {
        if (!isPushing) {
            mTimer?.start()
            this.isPushing = true
        }
    }

    override suspend fun restartPush() {}

    //TODO("for testing only")
    override suspend fun stopPush() {
        if (isPushing) {
            mTimer?.stop()
            this.isPushing = false
        }
    }

    override suspend fun switchCamera() {}

    //TODO("for testing only")
    override suspend fun resume() {
        if (!isPushing) {
            mTimer?.resume()
        }
    }

    //TODO("for testing only")
    override suspend fun pause() {
        if (isPushing) {
            mTimer?.pause()
            this.isPushing = false
        }
    }

    override fun destroy() {
        mTimer?.destroy()
    }

    //TODO("for testing only")
    override fun addStreamDuration(durationInMillis: Long) {
        if (this.mTimer == null)
            this.mTimer = PlayPusherTimer(builder.context, durationInMillis)

        this.mTimer?.callback = mPlayPusherTimerListener
    }

    override fun addMaxStreamDuration(durationInMillis: Long) {
        this.mTimer?.mMaxDuration = durationInMillis
    }

    //TODO("for testing only")
    override fun restartStreamDuration(durationInMillis: Long) {
        this.mTimer?.restart(durationInMillis)
    }

    //TODO("for testing only")
    override fun addMaxPauseDuration(durationInMillis: Long) {
        if (this.mTimer == null)
            this.mTimer = PlayPusherTimer(builder.context)

        this.mTimer?.pauseDuration = durationInMillis
        this.mTimer?.callback = mPlayPusherTimerListener
    }

    override fun addPusherInfoListener(playPusherInfoListener: PlayPusherInfoListener) {

    }

//    override fun getObservablePlayPusherInfoState(): LiveData<PlayPusherTimerInfoState> {
//        return _observableInfoState
//    }

    override fun getObservablePlayPusherNetworkState(): LiveData<PlayPusherNetworkState> {
        return _observableNetworkState
    }

    override fun isPushing(): Boolean {
        return isPushing
    }

    private val mPlayPusherTimerListener = object : PlayPusherTimerListener{
        override fun onCountDownActive(timeLeft: String) {
//            _observableInfoState.postValue(PlayPusherTimerInfoState.TimerActive(timeLeft))
        }

        override fun onCountDownAlmostFinish(minutesUntilFinished: Long) {
//            _observableInfoState.postValue(PlayPusherTimerInfoState.TimerAlmostFinish(minutesUntilFinished))
        }

        override fun onCountDownFinish() {
//            _observableInfoState.postValue(PlayPusherTimerInfoState.TimerFinish(timeElapsed))
        }

        override fun onReachMaximumPauseDuration() {
//            _observableInfoState.postValue(PlayPusherTimerInfoState.ReachMaximumPauseDuration)
        }
    }
}