package com.tokopedia.play.broadcaster.pusher.listener

import com.alivc.live.pusher.AlivcLivePushInfoListener
import com.alivc.live.pusher.AlivcLivePushStats
import com.alivc.live.pusher.AlivcLivePusher
import com.tokopedia.play.broadcaster.pusher.state.ApsaraLivePusherState
import com.tokopedia.play.broadcaster.pusher.state.ApsaraLivePusherStateProcessor


/**
 * Created by mzennis on 16/03/21.
 */
class ApsaraLivePushInfoListenerImpl(
        private val livePusherStateProcessor: ApsaraLivePusherStateProcessor
) : AlivcLivePushInfoListener {

    override fun onPreviewStarted(pusher: AlivcLivePusher?) {
        // Indicates that a preview starts.
    }

    override fun onPreviewStoped(pusher: AlivcLivePusher?) {
        // Indicates that a preview stops.
    }

    override fun onPushStarted(pusher: AlivcLivePusher?) {
        livePusherStateProcessor.onStateChanged(ApsaraLivePusherState.Start)
    }

    override fun onPushPauesed(pusher: AlivcLivePusher?) {
        livePusherStateProcessor.onStateChanged(ApsaraLivePusherState.Pause)
    }

    override fun onPushResumed(pusher: AlivcLivePusher?) {
        livePusherStateProcessor.onStateChanged(ApsaraLivePusherState.Resume)
    }

    override fun onPushStoped(pusher: AlivcLivePusher?) {
        livePusherStateProcessor.onStateChanged(ApsaraLivePusherState.Stop)
    }

    override fun onPushRestarted(pusher: AlivcLivePusher?) {
        livePusherStateProcessor.onStateChanged(ApsaraLivePusherState.Restart)
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