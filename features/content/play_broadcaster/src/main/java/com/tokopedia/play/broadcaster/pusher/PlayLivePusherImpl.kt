package com.tokopedia.play.broadcaster.pusher

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.view.SurfaceView
import com.wmspanel.libstream.*
import org.json.JSONObject


/**
 * Created by mzennis on 03/06/21.
 */
class PlayLivePusherImpl : PlayLivePusher, Streamer.Listener {

    private var streamer: StreamerGL? = null

    private var mLivePusherListener: PlayLivePusherListener? = null
    private var mLivePusherState: PlayLivePusherState = PlayLivePusherState.Idle
    private var mLivePusherConfig = PlayLivePusherConfig()
    private var mHandler: Handler? = null

    private var isPushStarted = false

    private val mLivePusherConnection: PlayLivePusherConnection = PlayLivePusherConnection()

    private fun createStreamer(surfaceView: SurfaceView) {
        val context = surfaceView.context
        val builder = StreamerGLBuilder()

        builder.setContext(context)
        builder.setListener(this)

        // default config: 44.1kHz, Mono, CAMCORDER input
        builder.setAudioConfig(AudioConfig()) // TODO: edit default audio config

        // default config: h264, 2 mbps, 2 sec. keyframe interval
        val videoConfig = VideoConfig() // TODO: edit default video config
        videoConfig.videoSize = Streamer.Size(720, 1280)
        builder.setVideoConfig(videoConfig)

        builder.setCamera2(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)

        // preview surface
        builder.setSurface(surfaceView.holder.surface)
        builder.setSurfaceSize(Streamer.Size(surfaceView.width, surfaceView.height))

        val availableCameras =  getAvailableCameras(context)
        availableCameras.forEach { camera -> builder.addCamera(camera) }

        // streamer will start capture from this camera id
        val cameraId = availableCameras.last().cameraId // 0 back 1 front
        builder.setCameraId(cameraId)

        builder.setVideoOrientation(StreamerGL.ORIENTATIONS.PORTRAIT)
        builder.setDisplayRotation(90)

        streamer = builder.build()
    }

    override val state: PlayLivePusherState
        get() = mLivePusherState

    override fun init(handler: Handler) {
        this.mHandler = handler
    }

    override fun prepare(config: PlayLivePusherConfig?) {
        mLivePusherConfig = config ?: PlayLivePusherConfig()
        if (!setup()) broadcastState(PlayLivePusherState.Error("Error preparing stream, This device cant do it"))
    }

    override fun setListener(listener: PlayLivePusherListener) {
        mLivePusherListener = listener
    }

    override fun startPreview(surfaceView: SurfaceView) {
        if (streamer == null) createStreamer(surfaceView)
        safeStartPreview()
    }

    private fun safeStartPreview() {
        Handler().postDelayed({
            streamer?.startVideoCapture()
            streamer?.startAudioCapture()
        }, 500)
    }

    override fun stopPreview() {
        streamer?.stopAudioCapture()
        streamer?.stopVideoCapture()
    }

    override fun switchCamera() {
        streamer?.flip()
    }

    override fun start(url: String) {
        broadcastState(PlayLivePusherState.Connecting)
        mLivePusherConnection.uri = url
        startStream()
    }

    override fun resume() {
        broadcastState(PlayLivePusherState.Connecting)
        startStream()
    }

    private fun startStream() {
        mLivePusherConnection.connectionId = streamer?.createConnection(mLivePusherConnection)
    }

    private fun stopStream() {
        mLivePusherConnection.connectionId?.let { id -> streamer?.releaseConnection(id) }
    }

    override fun pause() {
        stopStream()
        broadcastState(PlayLivePusherState.Pause)
    }

    override fun reconnect() {
        startStream()
    }

    override fun stop() {
        stopStream()
        broadcastState(PlayLivePusherState.Stop)
    }

//    override fun onAuthErrorRtmp() {
//        broadcastState(PlayLivePusherState.Error("authentication rtmp error"))
//    }
//
//    override fun onAuthSuccessRtmp() {
//    }
//
//    override fun onConnectionFailedRtmp(reason: String) {
//        broadcastState(PlayLivePusherState.Error(reason))
//    }
//
//    override fun onConnectionStartedRtmp(rtmpUrl: String) {
//    }
//
//    override fun onConnectionSuccessRtmp() {
//        val lastState = mLivePusherState
//        when {
//            lastState.isError -> broadcastState(PlayLivePusherState.Recovered)
//            isPushStarted -> broadcastState(PlayLivePusherState.Resumed)
//            else -> {
//                broadcastState(PlayLivePusherState.Started)
//                isPushStarted = true
//            }
//        }
//        configureAdaptiveBitrate()
//    }
//
//    override fun onDisconnectRtmp() {
//    }
//
//    override fun onNewBitrateRtmp(bitrate: Long) {
//        adaptBitrate(bitrate)
//    }

    private fun setup(): Boolean { // TODO: change audio, video configuration
//        val prepareAudio = rtmpCamera.prepareAudio(
//            mLivePusherConfig.audioBitrate,
//            mLivePusherConfig.audioSampleRate,
//            mLivePusherConfig.audioStereo,
//            mLivePusherConfig.audioEchoCanceler,
//            mLivePusherConfig.audioNoiseSuppressor
//        )
//        val prepareVideo = rtmpCamera.prepareVideo(
//            rtmpCamera.streamWidth,
//            rtmpCamera.streamWidth,
//            mLivePusherConfig.fps,
//            mLivePusherConfig.videoBitrate,
//            mLivePusherConfig.videoOrientation
//        )
//        rtmpCamera.setReTries(mLivePusherConfig.maxRetry)
//        return prepareAudio && prepareVideo
        return true
    }

    private fun broadcastState(state: PlayLivePusherState) {
        mLivePusherState = state
        mLivePusherListener?.onNewLivePusherState(state)
    }

    private fun getAvailableCameras(context: Context): List<CameraConfig> {
        val size = Streamer.Size(1280, 720) // TODO: find best fit for front & back camera
        try {
            val cameraList = mutableListOf<CameraConfig>()
            val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            val cameraIdList = cameraManager.cameraIdList
            for (cameraId in cameraIdList) {
                val camera = CameraConfig().apply {
                    this.cameraId = cameraId
                    this.videoSize = size
                }
                cameraList.add(camera)
            }
        } catch (ignored: CameraAccessException) {
        }
        return emptyList()
    }

    override fun getHandler(): Handler {
        return mHandler ?: throw IllegalStateException("PlayLivePusher is not initialized")
    }

    override fun onConnectionStateChanged(
        connectionId: Int,
        state: Streamer.CONNECTION_STATE?,
        status: Streamer.STATUS?,
        info: JSONObject?
    ) {
        // TODO:
    }

    override fun onVideoCaptureStateChanged(state: Streamer.CAPTURE_STATE?) {
        // TODO:
    }

    override fun onAudioCaptureStateChanged(state: Streamer.CAPTURE_STATE?) {
        // TODO:
    }

    override fun onRecordStateChanged(
        state: Streamer.RECORD_STATE?,
        uri: Uri?,
        method: Streamer.SAVE_METHOD?
    ) {
        // ignored
    }

    override fun onSnapshotStateChanged(
        state: Streamer.RECORD_STATE?,
        uri: Uri?,
        method: Streamer.SAVE_METHOD?
    ) {
        // ignored
    }
}