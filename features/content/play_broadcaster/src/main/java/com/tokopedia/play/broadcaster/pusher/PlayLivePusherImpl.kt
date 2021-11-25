package com.tokopedia.play.broadcaster.pusher

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Handler
import com.tokopedia.broadcaster.widget.SurfaceAspectRatioView
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.play.broadcaster.pusher.bitrate.BitrateAdapter
import com.tokopedia.play.broadcaster.pusher.camera.CameraInfo
import com.tokopedia.play.broadcaster.pusher.camera.CameraManager
import com.tokopedia.play.broadcaster.pusher.camera.CameraType
import com.tokopedia.play.broadcaster.util.deviceinfo.DeviceInfoUtil
import com.tokopedia.play.broadcaster.util.extension.safeExecute
import com.wmspanel.libstream.*
import org.json.JSONObject
import java.util.*


/**
 * Created by mzennis on 03/06/21.
 */
class PlayLivePusherImpl : PlayLivePusher, Streamer.Listener {

    private var streamer: StreamerGL? = null

    private var mConfig = PlayLivePusherConfig()

    private var mState: PlayLivePusherState = PlayLivePusherState.Idle
    private var mListener: PlayLivePusherListener? = null
    private var mHandler: Handler? = null
    private var mBitrateAdapter: BitrateAdapter? = null

    private val mConnection = PlayLivePusherConnection()
    private val mStatistic = PlayLivePusherStatistic()
    private val mAvailableCameras = mutableListOf<CameraInfo>()

    private var isPushStarted = false
    private var statisticUpdateTimer: Timer? = null

    override val ingestUrl: String
        get() = mConnection.uri

    override val config: PlayLivePusherConfig
        get() = mConfig

    override val state: PlayLivePusherState
        get() = mState


    override fun init(context: Context, handler: Handler) {
        this.mHandler = handler

        if (!DeviceInfoUtil.isDeviceSupported()) throw IllegalAccessException("please use device with armeabi-v7a or arm64-v8a")
        if (!isDeviceHaveCameraAvailable(context)) throw IllegalAccessException("unable to live stream as no camera available")
    }

    override fun prepare(config: PlayLivePusherConfig?) {
        if (config != null) mConfig = config
        configureStreamer(mConfig)
    }

    override fun setListener(listener: PlayLivePusherListener) {
        mListener = listener
    }

    override fun startPreview(surfaceView: SurfaceAspectRatioView) {
        if (streamer == null) createStreamer(surfaceView)
        streamer?.setSurface(surfaceView.surfaceHolder.surface)
        safeStartPreview()
    }

    override fun stopPreview() {
        streamer?.safeExecute { stopAudioCapture() }
        streamer?.safeExecute { stopVideoCapture() }
    }

    override fun switchCamera() {
        if (!isSwitchCameraSupported()) return
        pauseCalculateAdaptiveBitrate()
        streamer?.safeExecute { flip() }

        resumeCalculateAdaptiveBitrate()
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

    override fun pause() {
        stopStream()
        broadcastState(PlayLivePusherState.Paused)
    }

    override fun reconnect() {
        releaseConnection()
        createConnection()
    }

    override fun stop() {
        stopStream()
        stopPreview()
        broadcastState(PlayLivePusherState.Stopped)
    }

    override fun release() {
        pauseCalculateAdaptiveBitrate()
        cancelStatsJob()
        streamer?.safeExecute { release() }
        mListener = null
        mBitrateAdapter = null
        streamer = null
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
        when(status) {
            Streamer.STATUS.CONN_FAIL -> broadcastState(
                PlayLivePusherState.Error(
                    if (isPushStarted) "network: network fail" else "connect fail: Can not connect to server"
                )
            )
            Streamer.STATUS.AUTH_FAIL -> broadcastState(PlayLivePusherState.Error("connect fail: Can not connect to server authentication failure, please check stream credentials."))
            Streamer.STATUS.UNKNOWN_FAIL -> {
                // let’s ignore when last state nya stopped
                if (state == Streamer.CONNECTION_STATE.DISCONNECTED && (lastState is PlayLivePusherState.Stopped || lastState is PlayLivePusherState.Paused)) return
                if (info?.length().orZero() > 0) {
                    broadcastState(PlayLivePusherState.Error("network: reason ${info?.toString()}"))
                }
                else {
                    broadcastState(PlayLivePusherState.Error("network: unknown network fail"))
                }
            }
            Streamer.STATUS.SUCCESS -> {
                when(state) {
                    Streamer.CONNECTION_STATE.IDLE -> broadcastState(PlayLivePusherState.Idle)
                    Streamer.CONNECTION_STATE.INITIALIZED,
                    Streamer.CONNECTION_STATE.SETUP,
                    Streamer.CONNECTION_STATE.DISCONNECTED -> {
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

    /**
     * Private methods
     */
    private fun isDeviceHaveCameraAvailable(context: Context): Boolean {
        this.mAvailableCameras.clear()
        this.mAvailableCameras.addAll(CameraManager.getAvailableCameras(context))
        return mAvailableCameras.isNotEmpty()
    }

    private fun isSwitchCameraSupported() = mAvailableCameras.size > 1

    private fun createStreamer(surfaceView: SurfaceAspectRatioView) {
        val context = surfaceView.context
        val builder = StreamerGLBuilder()

        builder.setContext(context)
        builder.setListener(this)

        // configure audio
        builder.setAudioConfig(PlayLivePusherUtil.getAudioConfig(mConfig))

        // configure camera
        builder.setCamera2(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        builder.setSurface(surfaceView.surfaceHolder.surface)
        builder.setSurfaceSize(Streamer.Size(surfaceView.width, surfaceView.height))

        val activeCamera = mAvailableCameras.firstOrNull { it.lensFacing == CameraType.Front } ?: mAvailableCameras.first()
        builder.setCameraId(activeCamera.cameraId)

        val cameraSize = CameraManager.getVideoSize(activeCamera) ?: mConfig.getVideoSize()

        // configure video
        val videoConfig = PlayLivePusherUtil.getVideoConfig(mConfig, cameraSize)
        builder.setVideoConfig(videoConfig)

        mAvailableCameras.forEach {
            builder.addCamera(getCameraConfig(it, cameraSize))
        }

        builder.setVideoOrientation(StreamerGL.ORIENTATIONS.PORTRAIT)
        builder.setDisplayRotation(0)
        builder.setFullView(true)

        mBitrateAdapter = BitrateAdapter.newInstance(context, videoConfig.bitRate.toLong(), activeCamera.fpsRanges)

        streamer = builder.build()
    }

    private fun configureStreamer(config: PlayLivePusherConfig) {
        streamer?.safeExecute { changeAudioConfig(PlayLivePusherUtil.getAudioConfig(config)) }
        streamer?.safeExecute { changeVideoConfig(PlayLivePusherUtil.getVideoConfig(config)) }
    }

    private fun getCameraConfig(cameraInfo: CameraInfo, cameraSize: Streamer.Size): CameraConfig {
        return CameraConfig().apply {
            cameraId = cameraInfo.cameraId
            videoSize = cameraSize
        }
    }

    private fun getActiveCamera(): CameraInfo? = mAvailableCameras.firstOrNull { it.cameraId == streamer?.activeCameraId }

    private fun configureMirrorFrontCamera() {
        streamer?.setFrontMirror(false, false)
    }

    private fun safeStartPreview() {
        Handler().postDelayed({
            streamer?.safeExecute { startVideoCapture() }
            streamer?.safeExecute { startAudioCapture() }
        }, SAFE_OPEN_CAMERA_DELAY)
    }

    private fun startStream() {
        createConnection()
        startCalculateAdaptiveBitrate()
        startStatsJob()
    }

    private fun createConnection() {
        mConnection.connectionId = streamer?.createConnection(mConnection)
    }

    private fun releaseConnection() {
        try {
            mConnection.connectionId?.let { id -> streamer?.safeExecute { releaseConnection(id) } }
        } catch (ignored: IllegalStateException) {}

    }

    private fun stopStream() {
        releaseConnection()
        stopCalculateAdaptiveBitrate()
        cancelStatsJob()
    }

    private fun broadcastState(state: PlayLivePusherState) {
        mState = state
        mListener?.onNewLivePusherState(state)
    }

    private fun startCalculateAdaptiveBitrate() {
        streamer?.let { mBitrateAdapter?.start(it, mConnection.connectionId.orZero()) }
    }

    private fun pauseCalculateAdaptiveBitrate() {
        mBitrateAdapter?.pause()
    }

    private fun resumeCalculateAdaptiveBitrate() {
        getActiveCamera()?.let { mBitrateAdapter?.setFpsRanges(it.fpsRanges) }
        if (isPushStarted) mBitrateAdapter?.resume()
    }

    private fun stopCalculateAdaptiveBitrate() {
        mBitrateAdapter?.stop()
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

        }, STATISTIC_JOB_DELAY, STATISTIC_JOB_PERIOD)
    }

    private fun cancelStatsJob() {
        try {
            statisticUpdateTimer?.cancel()
            statisticUpdateTimer = null
        } catch (ignored: Exception) { }
    }

    companion object {
        private const val SAFE_OPEN_CAMERA_DELAY = 500L
        private const val STATISTIC_JOB_DELAY = 1000L
        private const val STATISTIC_JOB_PERIOD = 1000L
    }
}