package com.tokopedia.play.broadcaster.pusher

import android.os.Handler
import com.pedro.encoder.input.video.CameraHelper
import com.pedro.rtmp.utils.ConnectCheckerRtmp
import com.pedro.rtplibrary.rtmp.RtmpCamera2
import com.pedro.rtplibrary.util.BitrateAdapter
import com.pedro.rtplibrary.view.LightOpenGlView


/**
 * Created by mzennis on 03/06/21.
 */
class PlayLivePusherImpl : PlayLivePusher, ConnectCheckerRtmp {

    private lateinit var rtmpCamera: RtmpCamera2

    private var mLivePusherListener: PlayLivePusherListener? = null
    private var mLivePusherState: PlayLivePusherState = PlayLivePusherState.Idle
    private var mLivePusherConfig = PlayLivePusherConfig()

    private var bitrateAdapter: BitrateAdapter? = null
    private var rtmpUrl = ""
    private var isPushStarted = false

    private fun initialize(lightOpenGlView: LightOpenGlView) {
        rtmpCamera = RtmpCamera2(lightOpenGlView, this)
        rtmpCamera.prepareVideo(
            lightOpenGlView.height,
            lightOpenGlView.width,
            mLivePusherConfig.videoBitrate
        )
    }

    override val state: PlayLivePusherState
        get() = mLivePusherState

    override fun prepare(config: PlayLivePusherConfig?) {
        mLivePusherConfig = config ?: PlayLivePusherConfig()
        if (!setup()) broadcastState(PlayLivePusherState.Error("Error preparing stream, This device cant do it"))
    }

    override fun setListener(listener: PlayLivePusherListener) {
        mLivePusherListener = listener
    }

    override fun startPreview(lightOpenGlView: LightOpenGlView) {
        if (!::rtmpCamera.isInitialized) {
            initialize(lightOpenGlView)
            safeStartPreview(CameraHelper.Facing.FRONT)
        } else safeStartPreview(rtmpCamera.cameraFacing)
    }

    private fun safeStartPreview(cameraFacing: CameraHelper.Facing) {
        Handler().postDelayed({
            rtmpCamera.startPreview(cameraFacing)
        }, 500)
    }

    override fun stopPreview() {
        rtmpCamera.stopPreview()
    }

    override fun switchCamera() {
        rtmpCamera.switchCamera()
    }

    override fun start(url: String) {
        broadcastState(PlayLivePusherState.Connecting)
        this.rtmpUrl = url
        startStream()
    }

    override fun resume() {
        broadcastState(PlayLivePusherState.Connecting)
        startStream()
    }

    private fun startStream() {
        prepare(mLivePusherConfig)
        rtmpCamera.startStream(rtmpUrl)
    }

    override fun pause() {
       rtmpCamera.stopStream()
        broadcastState(PlayLivePusherState.Pause)
    }

    override fun reconnect(reason: String) {
        rtmpCamera.reTry(mLivePusherConfig.reconnectDelay, reason)
    }

    override fun stop() {
        rtmpCamera.stopStream()
        broadcastState(PlayLivePusherState.Stop)
    }

    override fun onAuthErrorRtmp() {
        broadcastState(PlayLivePusherState.Error("authentication rtmp error"))
    }

    override fun onAuthSuccessRtmp() {
    }

    override fun onConnectionFailedRtmp(reason: String) {
        broadcastState(PlayLivePusherState.Error(reason))
    }

    override fun onConnectionStartedRtmp(rtmpUrl: String) {
    }

    override fun onConnectionSuccessRtmp() {
        val lastState = mLivePusherState
        when {
            lastState.isError -> broadcastState(PlayLivePusherState.Recovered)
            isPushStarted -> broadcastState(PlayLivePusherState.Resumed)
            else -> {
                broadcastState(PlayLivePusherState.Started)
                isPushStarted = true
            }
        }
        configureAdaptiveBitrate()
    }

    override fun onDisconnectRtmp() {
    }

    override fun onNewBitrateRtmp(bitrate: Long) {
        adaptBitrate(bitrate)
    }

    private fun setup(): Boolean {
        val prepareAudio = rtmpCamera.prepareAudio(
            mLivePusherConfig.audioBitrate,
            mLivePusherConfig.audioSampleRate,
            mLivePusherConfig.audioStereo,
            mLivePusherConfig.audioEchoCanceler,
            mLivePusherConfig.audioNoiseSuppressor
        )
        val prepareVideo = rtmpCamera.prepareVideo(
            mLivePusherConfig.fps,
            mLivePusherConfig.videoBitrate,
            mLivePusherConfig.videoOrientation
        )
        rtmpCamera.setReTries(mLivePusherConfig.maxRetry)
        return prepareAudio && prepareVideo
    }

    private fun broadcastState(state: PlayLivePusherState) {
        mLivePusherState = state
        mLivePusherListener?.onNewLivePusherState(state)
    }

    private fun configureAdaptiveBitrate() {
        bitrateAdapter = BitrateAdapter { bitrate -> rtmpCamera.setVideoBitrateOnFly(bitrate) }
        bitrateAdapter?.setMaxBitrate(rtmpCamera.bitrate)
    }

    private fun adaptBitrate(bitrate: Long) {
        bitrateAdapter?.adaptBitrate(bitrate, rtmpCamera.hasCongestion())
    }
}