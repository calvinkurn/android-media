package com.tokopedia.play.broadcaster.pusher

import android.content.Context
import android.view.SurfaceView
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.play.broadcaster.pusher.apsara.ApsaraLivePusher
import com.tokopedia.play.broadcaster.pusher.apsara.ApsaraLivePusherErrorStatus
import com.tokopedia.play.broadcaster.pusher.apsara.ApsaraLivePusherInfoListener
import com.tokopedia.play.broadcaster.pusher.timer.PlayPusherTimer
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


/**
 * Created by mzennis on 22/09/20.
 */
class PlayPusherImpl(@ApplicationContext private val mContext: Context) : PlayPusher {

    private val pusherJob = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main.immediate + pusherJob)

    private val mApsaraLivePusher: ApsaraLivePusher = ApsaraLivePusher(mContext)
    private var mTimerDuration: PlayPusherTimer? = null

    private var mPlayPusherInfoListener: PlayPusherInfoListener? = null
    private var mPlayPusherTimerListener: PlayPusherTimerListener? = null

    override fun init() {
        scope.launch {
            mApsaraLivePusher.init()
            mApsaraLivePusher.mApsaraLivePusherInfoListener = mApsaraLivePusherInfoListener
        }
    }

    override fun startPreview(surfaceView: SurfaceView) {
        scope.launch {
            mApsaraLivePusher.startPreview(surfaceView)
        }
    }

    override fun stopPreview() {
        scope.launch {
            mApsaraLivePusher.stopPreview()
        }
    }

    override fun startPush(ingestUrl: String) {
        scope.launchCatchError(block = {
            mApsaraLivePusher.startPush(ingestUrl)
        }) {
            mPlayPusherInfoListener?.onError(ApsaraLivePusherErrorStatus.ConnectFailed)
        }
    }

    /**
     *  Call this method to restart stream ingest during stream ingest or after you receive any callback event notification related to an error.
     *  When such an error occurs, you can only call this method to restart stream ingest, call the reconnectPushAsync method to reconnect to the network,
     *  or call the destroy method to destroy stream ingest. After you call this method, the stream ingest SDK restarts stream ingest,
     *  and restarts all resources of the AlivcLivePusher object, including preview frames and ingest streams.
      */
    override fun restartPush() {
        scope.launch(Dispatchers.IO) {
            mApsaraLivePusher.restartPush()
        }
    }

    override fun stopPush() {
        mApsaraLivePusher.stopPush()
    }

    override fun switchCamera() {
        scope.launch(Dispatchers.IO) {
            mApsaraLivePusher.switchCamera()
        }
    }

    override fun resumePush() {
        scope.launch(Dispatchers.IO) {
            mApsaraLivePusher.resume()
        }
    }

    override fun pausePush() {
        scope.launch(Dispatchers.IO) {
            mApsaraLivePusher.pause()
        }
    }

    override fun destroy() {
        mApsaraLivePusher.destroy()
        mTimerDuration?.destroy()
    }

    override fun setStreamDuration(durationInMillis: Long) {
        if (this.mTimerDuration == null) {
            this.mTimerDuration = PlayPusherTimer(mContext, durationInMillis)
        }
        this.mTimerDuration?.callback = mPlayPusherTimerListener
    }

    override fun setMaxStreamDuration(durationInMillis: Long) {
        this.mTimerDuration?.mMaxDuration = durationInMillis
    }

    override fun startTimer() {
        this.mTimerDuration?.start()
    }

    override fun resumeTimer() {
        this.mTimerDuration?.resume()
    }

    override fun pauseTimer() {
        this.mTimerDuration?.pause()
    }

    override fun stopTimer() {
        try {
            this.mTimerDuration?.stop()
        } catch (throwable: Throwable){
        }
    }

    override fun restartStreamDuration(durationInMillis: Long) {
        this.mTimerDuration?.restart(durationInMillis)
    }

    override fun setMaxPauseDuration(durationInMillis: Long) {
        this.mTimerDuration?.pauseDuration = durationInMillis
    }

    override fun getTimeElapsed(): String = this.mTimerDuration?.getTimeElapsed().orEmpty()

    override fun setPlayPusherTimerListener(listener: PlayPusherTimerListener) {
        this.mPlayPusherTimerListener = listener
        this.mTimerDuration?.callback = mPlayPusherTimerListener
    }

    override fun setPlayPusherInfoListener(listener: PlayPusherInfoListener) {
        this.mPlayPusherInfoListener = listener
    }

    private val mApsaraLivePusherInfoListener = object : ApsaraLivePusherInfoListener {
        override fun onStarted() {
            scope.launch {
                mPlayPusherInfoListener?.onStarted()
            }
        }

        override fun onResumed() {
            scope.launch {
                mPlayPusherInfoListener?.onResumed()
            }
        }

        override fun onPaused() {
            scope.launch {
                mPlayPusherInfoListener?.onPaused()
                mTimerDuration?.pause()
            }
        }

        override fun onStop() {
            scope.launch {
                mPlayPusherInfoListener?.onStop()
            }
        }

        override fun onRestarted() {
        }

        override fun onRecovered() {
            scope.launch {
                mPlayPusherInfoListener?.onRecovered()
            }
        }

        override fun onError(errorStatus: ApsaraLivePusherErrorStatus) {
           scope.launch {
               mPlayPusherInfoListener?.onError(errorStatus)
           }
        }
    }
}