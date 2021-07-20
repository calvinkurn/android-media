package com.tokopedia.broadcaster

import android.net.Uri
import android.os.Build
import android.os.Handler
import android.view.SurfaceView
import com.tokopedia.broadcaster.bitrate.BitrateAdapter
import com.tokopedia.broadcaster.camera.CameraInfo
import com.tokopedia.broadcaster.camera.CameraManager
import com.tokopedia.broadcaster.camera.CameraType
import com.tokopedia.broadcaster.data.BroadcasterLogger
import com.tokopedia.broadcaster.listener.BroadcasterListener
import com.tokopedia.broadcaster.state.BroadcasterState
import com.tokopedia.broadcaster.state.isError
import com.tokopedia.broadcaster.data.BroadcasterConfig
import com.tokopedia.broadcaster.data.BroadcasterConnection
import com.tokopedia.kotlin.extensions.view.orZero
import com.wmspanel.libstream.*
import org.json.JSONObject
import java.util.*

class LiveBroadcasterManager : LiveBroadcaster, Streamer.Listener {

    private var streamer: StreamerGL? = null

    private var mListener: BroadcasterListener? = null
    private var mState: BroadcasterState = BroadcasterState.Idle
    private var mConfig = BroadcasterConfig()
    private var mHandler: Handler? = null
    private var mBitrateAdapter: BitrateAdapter? = null
    private val mConnection = BroadcasterConnection()
    private val mStatistic = BroadcasterLogger()

    private var isPushStarted = false
    private var canSwitchCamera = false

    private var mAvailableCameras = emptyList<CameraInfo>()

    private var statisticUpdateTimer: Timer? = null

    private fun createStreamer(surfaceView: SurfaceView) {
        val context = surfaceView.context
        val builder = StreamerGLBuilder()

        mAvailableCameras = CameraManager.getAvailableCameras(context)
        if (mAvailableCameras.isEmpty()) {
            broadcastState(BroadcasterState.Error("system: unable to live stream as no camera available"))
            return
        } else {
            canSwitchCamera = mAvailableCameras.size > 1
        }

        builder.setContext(context)
        builder.setListener(this)

        // default config: 44.1kHz, Mono, CAMCORDER input
        builder.setAudioConfig(AudioConfig()) // TODO: edit default audio config

        builder.setCamera2(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)

        // preview surface
        builder.setSurface(surfaceView.holder.surface)
        builder.setSurfaceSize(Streamer.Size(surfaceView.width, surfaceView.height))

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

    override val connection: BroadcasterConnection
        get() = mConnection

    override val config: BroadcasterConfig
        get() = mConfig

    override val state: BroadcasterState
        get() = mState

    override fun init(handler: Handler) {
        this.mHandler = handler
    }

    override fun prepare(config: BroadcasterConfig?) {
        mConfig = config ?: BroadcasterConfig()
        configureStreamer(mConfig)
    }

    override fun setListener(listener: BroadcasterListener) {
        mListener = listener
    }

    override fun startPreview(surfaceView: SurfaceView) {
        if (streamer == null) createStreamer(surfaceView)
        safeStartPreview()
    }

    private fun safeStartPreview() {
        streamer?.startVideoCapture()
        streamer?.startAudioCapture()
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
        broadcastState(BroadcasterState.Connecting)
        mConnection.uri = url
        startStream()
    }

    override fun resume() {
        broadcastState(BroadcasterState.Connecting)
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
        broadcastState(BroadcasterState.Pause)
    }

    override fun reconnect() {
        createConnection() // TODO: handling max retry & reconnectDelay
    }

    override fun stop() {
        stopStream()
        broadcastState(BroadcasterState.Stop)
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
            Streamer.CONNECTION_STATE.IDLE -> broadcastState(BroadcasterState.Idle)
            Streamer.CONNECTION_STATE.INITIALIZED,
            Streamer.CONNECTION_STATE.SETUP -> {
                // ignored
            }
            Streamer.CONNECTION_STATE.CONNECTED -> mStatistic.init(streamer, connectionId)
            Streamer.CONNECTION_STATE.RECORD -> {
                when {
                    lastState.isError -> broadcastState(BroadcasterState.Recovered)
                    isPushStarted -> broadcastState(BroadcasterState.Resumed)
                    else -> {
                        broadcastState(BroadcasterState.Started)
                        isPushStarted = true
                    }
                }
                configureMirrorFrontCamera()
            }
            Streamer.CONNECTION_STATE.DISCONNECTED -> {
                if (lastState is BroadcasterState.Pause) return // ignore and just call resume()
                if (status == null) {
                    broadcastState(BroadcasterState.Error("network: unknown network fail"))
                    return
                }
                when(status) {
                    Streamer.STATUS.CONN_FAIL -> broadcastState(
                        BroadcasterState.Error(
                            if (isPushStarted) "network: network fail" else "connect fail: Can not connect to server"
                        )
                    )
                    Streamer.STATUS.AUTH_FAIL -> broadcastState(BroadcasterState.Error("connect fail: Can not connect to server authentication failure, please check stream credentials."))
                    Streamer.STATUS.UNKNOWN_FAIL -> {
                        if (info?.length().orZero() > 0) {
                            broadcastState(BroadcasterState.Error("network: unknown network fail"))
                        } else {
                            broadcastState(BroadcasterState.Error("network: reason ${info?.toString()}"))
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
            Streamer.CAPTURE_STATE.ENCODER_FAIL -> broadcastState(BroadcasterState.Error("system: Video encoding failure, try to change video resolution"))
            Streamer.CAPTURE_STATE.FAILED -> broadcastState(BroadcasterState.Error("system: Video capture failure"))
            Streamer.CAPTURE_STATE.STARTED,
            Streamer.CAPTURE_STATE.STOPPED -> {
                // ignored
            }
        }
    }

    override fun onAudioCaptureStateChanged(state: Streamer.CAPTURE_STATE?) {
        if (state == null) return
        when(state) {
            Streamer.CAPTURE_STATE.ENCODER_FAIL -> broadcastState(BroadcasterState.Error("system: Audio encoding failure"))
            Streamer.CAPTURE_STATE.FAILED -> broadcastState(BroadcasterState.Error("system: Audio capture failure"))
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

    private fun configureStreamer(config: BroadcasterConfig) {
        // TODO: change audio, video configuration
        // errorMessage: Error preparing stream, This device cant do it
    }

    private fun configureMirrorFrontCamera() {
        streamer?.setFrontMirror(false, false)
    }

    private fun broadcastState(state: BroadcasterState) {
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