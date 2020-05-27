package com.tokopedia.play.broadcaster.pusher

import android.view.SurfaceView


/**
 * Created by mzennis on 24/05/20.
 */
class PlayPusherImplNoop : PlayPusher {

    override fun create() {
    }

    override fun addCountDownTimer(countDownTimer: PlayPusherCountDownTimer) {
    }

    override fun startPreview(surfaceView: SurfaceView) {
    }

    override fun stopPreview() {
    }

    override fun startPush(ingestUrl: String) {
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
}