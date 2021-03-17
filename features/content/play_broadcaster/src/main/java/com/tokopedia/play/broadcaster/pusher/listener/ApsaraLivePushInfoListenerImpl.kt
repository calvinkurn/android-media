package com.tokopedia.play.broadcaster.pusher.listener

import com.alivc.live.pusher.AlivcLivePushInfoListener
import com.alivc.live.pusher.AlivcLivePusher
import com.tokopedia.play.broadcaster.pusher.ApsaraLivePusherWrapper
import com.tokopedia.play.broadcaster.pusher.state.ApsaraLivePusherState


/**
 * Created by mzennis on 16/03/21.
 */
class ApsaraLivePushInfoListenerImpl(
        private val listener: ApsaraLivePusherWrapper.Listener?
) : AlivcLivePushInfoListener {

    override fun onPreviewStarted(pusher: AlivcLivePusher?) {
        // Indicates that a preview starts.
    }

    override fun onPreviewStoped(pusher: AlivcLivePusher?) {
        // Indicates that a preview stops.
    }

    override fun onPushStarted(pusher: AlivcLivePusher?) {
        listener?.onConnectionStateChanged(ApsaraLivePusherState.Start)
    }

    override fun onPushPauesed(pusher: AlivcLivePusher?) {
        listener?.onConnectionStateChanged(ApsaraLivePusherState.Pause)
    }

    override fun onPushResumed(pusher: AlivcLivePusher?) {
        listener?.onConnectionStateChanged(ApsaraLivePusherState.Resume)
    }

    override fun onPushStoped(pusher: AlivcLivePusher?) {
        listener?.onConnectionStateChanged(ApsaraLivePusherState.Stop)
    }

    override fun onPushRestarted(pusher: AlivcLivePusher?) {
        listener?.onConnectionStateChanged(ApsaraLivePusherState.Restart)
    }

    override fun onFirstFramePreviewed(pusher: AlivcLivePusher?) {
        // Indicates first-frame rendering.
    }

    override fun onDropFrame(pusher: AlivcLivePusher?, countBef: Int, countAft: Int) {
        // Indicates that frames are discarded.
    }

    override fun onFirstAVFramePushed(pusher: AlivcLivePusher?) {
    }

    override fun onAdjustBitRate(pusher: AlivcLivePusher?, curBr: Int, targetBr: Int) {
        // Indicates that the bitrate is adjusted.
    }

    override fun onAdjustFps(pusher: AlivcLivePusher?, curFps: Int, targetFps: Int) {
        // Indicates that the frame rate is adjusted.
    }

}