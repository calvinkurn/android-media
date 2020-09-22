package com.tokopedia.play.broadcaster.pusher.apsara

import android.content.Context
import android.view.SurfaceView
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.play.broadcaster.pusher.timer.PlayPusherTimer
import com.tokopedia.play.broadcaster.pusher.timer.PlayPusherTimerListener


/**
 * Created by mzennis on 22/09/20.
 */
class PlayPusherImpl(@ApplicationContext private val mContext: Context) : PlayPusher {

    private val mApsaraLivePusher = ApsaraLivePusher(mContext)
    private var mTimerDuration: PlayPusherTimer? = null

    private var mPlayPusherInfoListener: PlayPusherInfoListener? = null

    override fun init() {
        mApsaraLivePusher.init()
        mApsaraLivePusher.mApsaraLivePusherInfoListener = mApsaraLivePusherInfoListener
    }

    override fun startPreview(surfaceView: SurfaceView) {
        mApsaraLivePusher.startPreview(surfaceView)
    }

    override fun stopPreview() {
        mApsaraLivePusher.stopPreview()
    }

    override fun startPush(ingestUrl: String) {
        mApsaraLivePusher.startPush(ingestUrl)
    }

    /**
     *  Call this method to restart stream ingest during stream ingest or after you receive any callback event notification related to an error.
     *  When such an error occurs, you can only call this method to restart stream ingest, call the reconnectPushAsync method to reconnect to the network,
     *  or call the destroy method to destroy stream ingest. After you call this method, the stream ingest SDK restarts stream ingest,
     *  and restarts all resources of the AlivcLivePusher object, including preview frames and ingest streams.
      */
    override fun restartPush() {
        mApsaraLivePusher.restartPush()
    }

    override fun stopPush() {
        mApsaraLivePusher.stopPush()
    }

    override fun switchCamera() {
        mApsaraLivePusher.switchCamera()
    }

    override fun resume() {
        mApsaraLivePusher.resume()
    }

    override fun pause() {
        mApsaraLivePusher.pause()
    }

    override fun destroy() {
        mApsaraLivePusher.destroy()
    }

    override fun addStreamDuration(durationInMillis: Long) {
        if (this.mTimerDuration == null) {
            this.mTimerDuration = PlayPusherTimer(mContext, durationInMillis)
        }
    }

    override fun addMaxStreamDuration(durationInMillis: Long) {
        this.mTimerDuration?.mMaxDuration = durationInMillis
    }

    override fun restartStreamDuration(durationInMillis: Long) {
        this.mTimerDuration?.restart(durationInMillis)
    }

    override fun addMaxPauseDuration(durationInMillis: Long) {
        if (this.mTimerDuration == null) {
            this.mTimerDuration = PlayPusherTimer(mContext)
        }

        this.mTimerDuration?.pauseDuration = durationInMillis
    }

    override fun getTimeElapsed(): String = this.mTimerDuration?.getTimeElapsed().orEmpty()

    override fun addPlayPusherTimerListener(listener: PlayPusherTimerListener) {
        this.mTimerDuration?.callback = listener
    }

    override fun addPlayPusherInfoListener(listener: PlayPusherInfoListener) {
        this.mPlayPusherInfoListener = listener
    }

    private val mApsaraLivePusherInfoListener = object : ApsaraLivePusherInfoListener {
        override fun onStarted() {
            mTimerDuration?.start()
            mPlayPusherInfoListener?.onStarted()
        }

        override fun onPushed(activeStatus: ApsaraLivePusherActiveStatus) {
            mPlayPusherInfoListener?.onPushed(activeStatus)
        }

        override fun onResumed() {
            mTimerDuration?.resume()
        }

        override fun onPaused() {
            mPlayPusherInfoListener?.onPaused()
            mTimerDuration?.pause()
        }

        override fun onStop() {
            mPlayPusherInfoListener?.onStop()
            mTimerDuration?.stop()
        }

        override fun onRestarted() {
            mPlayPusherInfoListener?.onRestarted()
        }

        override fun onError(errorStatus: ApsaraLivePusherErrorStatus) {
            mPlayPusherInfoListener?.onError(errorStatus)
        }
    }
}