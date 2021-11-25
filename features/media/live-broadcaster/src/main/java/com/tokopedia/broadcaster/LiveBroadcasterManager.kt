package com.tokopedia.broadcaster

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Handler
import com.tokopedia.broadcaster.bitrate.BitrateAdapter
import com.tokopedia.broadcaster.camera.CameraInfo
import com.tokopedia.broadcaster.camera.CameraManager
import com.tokopedia.broadcaster.camera.CameraType
import com.tokopedia.broadcaster.data.BitrateMode
import com.tokopedia.broadcaster.data.BroadcasterConfig
import com.tokopedia.broadcaster.data.BroadcasterConnection
import com.tokopedia.broadcaster.listener.BroadcasterListener
import com.tokopedia.broadcaster.state.BroadcasterState
import com.tokopedia.broadcaster.state.isError
import com.tokopedia.broadcaster.statsnerd.ui.notification.LogDebugNotification
import com.tokopedia.broadcaster.tracker.LiveBroadcasterLogger
import com.tokopedia.broadcaster.utils.BroadcasterUtil
import com.tokopedia.broadcaster.utils.DeviceInfo
import com.tokopedia.broadcaster.utils.retry
import com.tokopedia.broadcaster.widget.SurfaceAspectRatioView
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.orZero
import com.wmspanel.libstream.CameraConfig
import com.wmspanel.libstream.Streamer
import com.wmspanel.libstream.StreamerGL
import com.wmspanel.libstream.StreamerGLBuilder
import kotlinx.coroutines.*
import org.json.JSONObject
import java.util.*
import kotlin.coroutines.CoroutineContext
import com.tokopedia.broadcaster.bitrate.BitrateAdapter.Companion.ladderAscend as ladderAscendMode
import com.tokopedia.broadcaster.bitrate.BitrateAdapter.Companion.logarithmicDescend as logarithmicDescendMode

class LiveBroadcasterManager constructor(
    var streamer: LibStreamerGL? = null,
    var mConfig: BroadcasterConfig = BroadcasterConfig(),
    var mConnection: BroadcasterConnection = BroadcasterConnection(),
    val logger: LiveBroadcasterLogger = LiveBroadcasterLogger(),
) : LiveBroadcaster, Streamer.Listener, CoroutineScope {

    private var mListener: BroadcasterListener? = null
    private var mBitrateAdapter: BitrateAdapter? = null

    private var mContext: Context? = null
    private var mHandler: Handler? = null
    private var statisticUpdateTimer: Timer? = null

    var mAvailableCameras = mutableListOf<CameraInfo>()
    var mState: BroadcasterState = BroadcasterState.Idle
    var isPushStarted = false

    private val job = SupervisorJob()
    private var hasPrepared = false

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    override val connection: BroadcasterConnection
        get() = mConnection

    override val config: BroadcasterConfig
        get() = mConfig

    override val state: BroadcasterState
        get() = mState

    override val ingestUrl: String
        get() = mConnection.uri

    override fun init(context: Context, handler: Handler) {
        this.mHandler = handler
        this.mContext = context

        if (!DeviceInfo.isDeviceSupported()) {
            throw IllegalAccessException("please use device with armeabi-v7a or arm64-v8a")
        }

        if (!isDeviceHaveCameraAvailable(context)) {
            throw IllegalAccessException("unable to live stream as no camera available")
        }
    }

    override fun prepare(config: BroadcasterConfig?) {
        if (hasPrepared) error("the streamer already prepared")

        mConfig = config ?: BroadcasterConfig()
        configureStreamer(mConfig)

        hasPrepared = true
    }

    override fun setListener(listener: BroadcasterListener) {
        mListener = listener
    }

    override fun startPreview(surfaceView: SurfaceAspectRatioView) {
        if (streamer == null) createStreamer(surfaceView)
        safeStartPreview()
    }

    override fun stopPreview() {
        streamer?.stopAudioCapture()
        streamer?.stopVideoCapture()
    }

    override fun switchCamera() {
        if (!isSwitchCameraSupported()) return
        pauseCalculateAdaptiveBitrate()
        streamer?.flip()

        resumeCalculateAdaptiveBitrate()
    }

    override fun start(url: String) {
        if (mContext == null) {
            throw IllegalAccessException("you have to initialize first with call init()")
        }

        if (GlobalConfig.DEBUG) {
            mContext?.let {
                LogDebugNotification.build(it, url)
            }
        }

        broadcastState(BroadcasterState.Connecting)
        mConnection.uri = url

        startStream()
    }

    override fun resume() {
        broadcastState(BroadcasterState.Connecting)
        startStream()
    }

    override fun pause() {
        stopStream()
        broadcastState(BroadcasterState.Paused)
    }

    override fun reconnect() {
        launch {
            retry(
                times = config.maxRetry,
                reconnectDelay = config.reconnectDelay.toLong(),
                block = {
                    createConnection()
                },
                onError = {
                    broadcastState(BroadcasterState.Error("network: streamer cannot connected."))
                }
            )
        }
    }

    override fun stop() {
        stopStream()
        stopPreview()
        broadcastState(BroadcasterState.Stopped)
        job.cancel()
    }

    override fun release() {
        streamer?.release()
        mListener = null
        mBitrateAdapter = null
        streamer = null
    }

    override fun getHandler(): Handler {
        return mHandler ?: throw IllegalStateException("LiveBroadcasterManager is not initialized.")
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
                broadcastState(BroadcasterState.Connecting)
            }
            Streamer.CONNECTION_STATE.CONNECTED -> {
                logger.init(streamer, connectionId)
            }
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
                if (lastState is BroadcasterState.Paused) return // ignore and just call resume()

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
                        broadcastState(BroadcasterState.Error("network: streamer disconnected"))
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

    private fun createStreamer(surfaceView: SurfaceAspectRatioView) {
        val context = surfaceView.context
        val builder = StreamerGLBuilder()

        builder.setContext(context)
        builder.setListener(this)

        // configure audio
        builder.setAudioConfig(BroadcasterUtil.getAudioConfig(mConfig))

        // configure camera
        builder.setCamera2(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        builder.setSurface(surfaceView.surfaceHolder.surface)
        builder.setSurfaceSize(Streamer.Size(surfaceView.width, surfaceView.height))

        val activeCamera = mAvailableCameras.firstOrNull {
            it.lensFacing == CameraType.Front
        } ?: mAvailableCameras.first()

        builder.setCameraId(activeCamera.cameraId)

        // configure video
        val cameraSize = CameraManager.getVideoSize(activeCamera) ?: mConfig.getVideoSize()
        val videoConfig = BroadcasterUtil.getVideoConfig(mConfig, cameraSize)
        builder.setVideoConfig(videoConfig)

        mAvailableCameras.forEach {
            builder.addCamera(getCameraConfig(it, cameraSize))
        }

        builder.setVideoOrientation(StreamerGL.ORIENTATIONS.PORTRAIT)
        builder.setDisplayRotation(0)
        builder.setFullView(true)

        mBitrateAdapter = if (mConfig.bitrateMode == BitrateMode.LogarithmicDescend) {
            logarithmicDescendMode(videoConfig.bitRate.toLong(), activeCamera.fpsRanges)
        } else {
            ladderAscendMode(videoConfig.bitRate.toLong(), activeCamera.fpsRanges)
        }

        streamer = LibStreamerGLFactory(builder.build())
    }

    private fun safeStartPreview(
        dispatcher: CoroutineDispatcher = Dispatchers.Main
    ) {
        launch(dispatcher) {
            delay(SAFE_OPEN_CAMERA_DELAYED)

            streamer?.startVideoCapture()
            streamer?.startAudioCapture()
        }
    }

    private fun isDeviceHaveCameraAvailable(context: Context): Boolean {
        this.mAvailableCameras.clear()
        this.mAvailableCameras.addAll(CameraManager.getAvailableCameras(context))
        return mAvailableCameras.isNotEmpty()
    }

    private fun startStream() {
        createConnection()
        startCalculateAdaptiveBitrate()
        startStatsJob()
    }

    private fun createConnection() {
        mConnection.connectionId = streamer?.createConnection(mConnection)
    }

    private fun stopStream() {
        mConnection.connectionId?.let { id -> streamer?.releaseConnection(id) }
        stopCalculateAdaptiveBitrate()
        cancelStatsJob()
    }

    private fun configureStreamer(config: BroadcasterConfig) {
        streamer?.changeAudioConfig(BroadcasterUtil.getAudioConfig(config))
        streamer?.changeVideoConfig(BroadcasterUtil.getVideoConfig(config))
    }

    private fun configureMirrorFrontCamera() {
        streamer?.setFrontMirror(
            isPreview = false,
            isStream = false
        )
    }

    fun broadcastState(state: BroadcasterState) {
        mState = state
        mListener?.onNewLivePusherState(state)
    }

    private fun getCameraConfig(
        cameraInfo: CameraInfo,
        cameraSize: Streamer.Size
    ): CameraConfig {
        return CameraConfig().apply {
            cameraId = cameraInfo.cameraId
            videoSize = cameraSize
        }
    }

    private fun isSwitchCameraSupported() = mAvailableCameras.size > 1

    private fun getActiveCamera(): CameraInfo? {
        return mAvailableCameras.firstOrNull {
            it.cameraId == streamer?.activeCameraId
        }
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
                    mContext?.let { logger.update(it, mConnection.uri, mConfig) }
                    mListener?.onUpdateLivePusherStatistic(logger)
                }
            }

        }, DELAYED_TIME, PERIOD_TIME)
    }

    private fun cancelStatsJob() {
        try {
            statisticUpdateTimer?.cancel()
            statisticUpdateTimer = null
            logger.tracker.stopTrack()
        } catch (ignored: Exception) { }
    }

    companion object {
        private const val SAFE_OPEN_CAMERA_DELAYED = 500L
        private const val DELAYED_TIME = 1000L
        private const val PERIOD_TIME = 1000L
    }

}