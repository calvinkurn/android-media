package com.tokopedia.play.broadcaster.pusher

import android.net.Uri
import android.os.Build
import android.os.Handler
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.play.broadcaster.pusher.bitrate.BitrateAdapter
import com.tokopedia.play.broadcaster.pusher.camera.CameraInfo
import com.tokopedia.play.broadcaster.pusher.camera.CameraManager
import com.tokopedia.play.broadcaster.pusher.camera.CameraType
import com.tokopedia.play.broadcaster.view.custom.SurfaceAspectRatioView
import com.wmspanel.libstream.*
import org.json.JSONObject
import java.util.*


/**
 * Created by mzennis on 03/06/21.
 */
class PlayLivePusherImpl : PlayLivePusher, Streamer.Listener {

    private var streamer: StreamerGL? = null

    private var mListener: PlayLivePusherListener? = null
    private var mState: PlayLivePusherState = PlayLivePusherState.Idle
    private var mConfig = PlayLivePusherConfig()
    private var mHandler: Handler? = null
    private var mBitrateAdapter: BitrateAdapter? = null
    private val mConnection = PlayLivePusherConnection()
    private val mStatistic = PlayLivePusherStatistic()

    private var isPushStarted = false
    private var canSwitchCamera = false

    private var mAvailableCameras = emptyList<CameraInfo>()

    private var statisticUpdateTimer: Timer? = null

    private fun createStreamer(surfaceView: SurfaceAspectRatioView) {
        val context = surfaceView.context
        val builder = StreamerGLBuilder()

        builder.setContext(context)
        builder.setListener(this)

        // default config: 44.1kHz, Mono, CAMCORDER input
        builder.setAudioConfig(AudioConfig()) // TODO: edit default audio config

        builder.setCamera2(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)

        // preview surface
        builder.setSurface(surfaceView.surfaceHolder.surface)
        builder.setSurfaceSize(Streamer.Size(surfaceView.width, surfaceView.height))

        mAvailableCameras = CameraManager.getAvailableCameras(context)
        if (mAvailableCameras.isEmpty()) {
            broadcastState(PlayLivePusherState.Error("system: unable to live stream as no camera available"))
            return
        } else {
            canSwitchCamera = mAvailableCameras.size > 1
        }

        val activeCamera = mAvailableCameras.firstOrNull { it.lensFacing == CameraType.Front } ?: mAvailableCameras.first()
        builder.setCameraId(activeCamera.cameraId)

        val videoSize = CameraManager.getVideoSize(activeCamera) ?: mConfig.getVideoSize()

        // default config: h264, 2 sec. keyframe interval
        val videoConfig = VideoConfig()
        videoConfig.bitRate = mConfig.videoBitrate
        videoConfig.videoSize = CameraManager.verifyResolution(
            type = videoConfig.type,
            videoSize = Streamer.Size(videoSize.height, videoSize.width),
            defaultVideoSize = mConfig.getVideoSize()
        )
        videoConfig.fps = mConfig.fps
        builder.setVideoConfig(videoConfig)

        mAvailableCameras.forEach {
            val cameraConfig = CameraConfig()
            cameraConfig.cameraId = it.cameraId
            cameraConfig.videoSize = videoSize
            builder.addCamera(cameraConfig)
        }

        builder.setVideoOrientation(StreamerGL.ORIENTATIONS.PORTRAIT)
        builder.setDisplayRotation(0)
        builder.setFullView(true)

        mBitrateAdapter = BitrateAdapter.newInstance(context, videoConfig.bitRate.toLong(), activeCamera.fpsRanges)

        streamer = builder.build()
    }

    override val connection: PlayLivePusherConnection
        get() = mConnection

    override val config: PlayLivePusherConfig
        get() = mConfig

    override val state: PlayLivePusherState
        get() = mState

    override fun init(handler: Handler) {
        this.mHandler = handler
    }

    override fun prepare(config: PlayLivePusherConfig?) {
        mConfig = config ?: PlayLivePusherConfig()
        configureStreamer(mConfig)
    }

    override fun setListener(listener: PlayLivePusherListener) {
        mListener = listener
    }

    override fun startPreview(surfaceView: SurfaceAspectRatioView) {
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
        if (!canSwitchCamera) return
        mBitrateAdapter?.pause()
        streamer?.flip()

        val activeCamera = mAvailableCameras.firstOrNull { it.cameraId == streamer?.activeCameraId }
        activeCamera?.let { mBitrateAdapter?.setFpsRanges(it.fpsRanges) }
        if (isPushStarted) mBitrateAdapter?.resume()
    }

    override fun start(url: String) {
        broadcastState(PlayLivePusherState.Connecting)
        mConnection.uri = url
        startStream()
    }

    override fun resume() {
        broadcastState(PlayLivePusherState.Connecting)
        startStream()
    }

    private fun startStream() {
        createConnection()
        streamer?.let { mBitrateAdapter?.start(it, mConnection.connectionId.orZero()) }
        startStatsJob()
    }

    private fun createConnection() {
        mConnection.connectionId = streamer?.createConnection(mConnection)
    }

    private fun stopStream() {
        mConnection.connectionId?.let { id -> streamer?.releaseConnection(id) }
        mBitrateAdapter?.stop()
        cancelStatsJob()
    }

    override fun pause() {
        stopStream()
        broadcastState(PlayLivePusherState.Pause)
    }

    override fun reconnect() {
        createConnection() // TODO: handling max retry & reconnectDelay
    }

    override fun stop() {
        stopStream()
        broadcastState(PlayLivePusherState.Stop)
        streamer?.release()
    }

    override fun getHandler(): Handler {
        return mHandler ?: throw IllegalStateException("PlayLivePusher is not initialized.")
    }

    override fun onConnectionStateChanged(
        connectionId: Int,
        state: Streamer.CONNECTION_STATE?,
        status: Streamer.STATUS?,
        info: JSONObject?
    ) {
        if (state == null) return
        if (connectionId != mConnection.connectionId) {
            // ignore already released connection
            return
        }
        val lastState = mState
        when(state) {
            Streamer.CONNECTION_STATE.IDLE -> broadcastState(PlayLivePusherState.Idle)
            Streamer.CONNECTION_STATE.INITIALIZED,
            Streamer.CONNECTION_STATE.SETUP -> {
                // ignored
            }
            Streamer.CONNECTION_STATE.CONNECTED -> mStatistic.init(streamer, connectionId)
            Streamer.CONNECTION_STATE.RECORD -> {
                when {
                    lastState.isError -> broadcastState(PlayLivePusherState.Recovered)
                    isPushStarted -> broadcastState(PlayLivePusherState.Resumed)
                    else -> {
                        broadcastState(PlayLivePusherState.Started)
                        isPushStarted = true
                    }
                }
                configureMirrorFrontCamera()
            }
            Streamer.CONNECTION_STATE.DISCONNECTED -> {
                if (lastState is PlayLivePusherState.Pause) return // ignore and just call resume()
                if (status == null) {
                    broadcastState(PlayLivePusherState.Error("network: unknown network fail"))
                    return
                }
                when(status) {
                    Streamer.STATUS.CONN_FAIL -> broadcastState(
                        PlayLivePusherState.Error(
                            if (isPushStarted) "network: network fail" else "connect fail: Can not connect to server"
                        )
                    )
                    Streamer.STATUS.AUTH_FAIL -> broadcastState(PlayLivePusherState.Error("connect fail: Can not connect to server authentication failure, please check stream credentials."))
                    Streamer.STATUS.UNKNOWN_FAIL -> {
                        if (info?.length().orZero() > 0) {
                            broadcastState(PlayLivePusherState.Error("network: unknown network fail"))
                        } else {
                            broadcastState(PlayLivePusherState.Error("network: reason ${info?.toString()}"))
                        }
                    }
                    Streamer.STATUS.SUCCESS -> {
                        // ignored
                    }
                }
            }
        }
    }

    override fun onVideoCaptureStateChanged(state: Streamer.CAPTURE_STATE?) {
        if (state == null) return
        when(state) {
            Streamer.CAPTURE_STATE.ENCODER_FAIL -> broadcastState(PlayLivePusherState.Error("system: Video encoding failure, try to change video resolution"))
            Streamer.CAPTURE_STATE.FAILED -> broadcastState(PlayLivePusherState.Error("system: Video capture failure"))
            Streamer.CAPTURE_STATE.STARTED,
            Streamer.CAPTURE_STATE.STOPPED -> {
                // ignored
            }
        }
    }

    override fun onAudioCaptureStateChanged(state: Streamer.CAPTURE_STATE?) {
        if (state == null) return
        when(state) {
            Streamer.CAPTURE_STATE.ENCODER_FAIL -> broadcastState(PlayLivePusherState.Error("system: Audio encoding failure"))
            Streamer.CAPTURE_STATE.FAILED -> broadcastState(PlayLivePusherState.Error("system: Audio capture failure"))
            Streamer.CAPTURE_STATE.STARTED,
            Streamer.CAPTURE_STATE.STOPPED -> {
                // ignored
            }
        }
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

    private fun configureStreamer(config: PlayLivePusherConfig) {
        // TODO: change audio, video configuration
        // errorMessage: Error preparing stream, This device cant do it
    }

    private fun configureMirrorFrontCamera() {
        streamer?.setFrontMirror(false, false)
    }

    private fun broadcastState(state: PlayLivePusherState) {
        mState = state
        mListener?.onNewLivePusherState(state)
    }

    private fun startStatsJob() {
        cancelStatsJob()
        statisticUpdateTimer = Timer()
        statisticUpdateTimer?.schedule(object : TimerTask() {
            override fun run() {
                mHandler?.post {
                    mStatistic.update()
                    mListener?.onUpdateLivePusherStatistic(mStatistic)
                }
            }

        }, 1000, 1000)
    }

    private fun cancelStatsJob() {
        try {
            statisticUpdateTimer?.cancel()
        } catch (ignored: Exception) { }
    }
}