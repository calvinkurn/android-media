package com.tokopedia.broadcaster

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.view.SurfaceView
import com.tokopedia.broadcaster.bitrate.BitrateAdapter
import com.tokopedia.broadcaster.camera.CameraInfo
import com.tokopedia.broadcaster.camera.CameraManager
import com.tokopedia.broadcaster.camera.CameraType
import com.tokopedia.broadcaster.chucker.ui.notification.ChuckerNotification
import com.tokopedia.broadcaster.data.BitrateMode
import com.tokopedia.broadcaster.data.BroadcasterConfig
import com.tokopedia.broadcaster.data.BroadcasterConnection
import com.tokopedia.broadcaster.listener.BroadcasterListener
import com.tokopedia.broadcaster.state.BroadcasterState
import com.tokopedia.broadcaster.state.isError
import com.tokopedia.broadcaster.tracker.BroadcasterDataLog
import com.tokopedia.broadcaster.tracker.BroadcasterLoggerImpl
import com.tokopedia.broadcaster.utils.BroadcasterUtil
import com.tokopedia.broadcaster.utils.DeviceInfo
import com.tokopedia.broadcaster.utils.retry
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.orZero
import com.wmspanel.libstream.CameraConfig
import com.wmspanel.libstream.Streamer
import com.wmspanel.libstream.StreamerGL
import com.wmspanel.libstream.StreamerGLBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*
import kotlin.coroutines.CoroutineContext
import com.tokopedia.broadcaster.bitrate.BitrateAdapter.Companion.ladderAscend as ladderAscendMode
import com.tokopedia.broadcaster.bitrate.BitrateAdapter.Companion.logarithmicDescend as logarithmicDescendMode

class LiveBroadcasterManager : LiveBroadcaster, Streamer.Listener, CoroutineScope {

    private var mContext: Context? = null

    private var streamer: ExternalStreamerGL? = null
    private var mListener: BroadcasterListener? = null
    private var mState: BroadcasterState = BroadcasterState.Idle
    private var mConfig = BroadcasterConfig()
    private var mHandler: Handler? = null
    private var mBitrateAdapter: BitrateAdapter? = null
    private val mConnection = BroadcasterConnection()

    private val mLogger by lazy { BroadcasterLoggerImpl() }
    private val mLog = BroadcasterDataLog()

    private var isPushStarted = false

    private var mAvailableCameras = mutableListOf<CameraInfo>()
    private var statisticUpdateTimer: Timer? = null

    private val job = SupervisorJob()

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
        if (config != null) mConfig = config
        configureStreamer(mConfig)
    }

    override fun setListener(listener: BroadcasterListener) {
        mListener = listener
    }

    override fun startPreview(surfaceView: SurfaceView) {
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
        // showing debug notif for tracking chucker of live broadcaster
        if (GlobalConfig.DEBUG) {
            mContext?.let {
                ChuckerNotification.build(it, url)
            }
        }

        broadcastState(BroadcasterState.Connecting)
        mConnection.uri = url
        mConfig.ingestUrl = url
        startStream()
    }

    override fun resume() {
        broadcastState(BroadcasterState.Connecting)
        startStream()
    }

    override fun pause() {
        stopStream()
        broadcastState(BroadcasterState.Pause)
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
        broadcastState(BroadcasterState.Stop)
        streamer?.release()
        job.cancel()
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
                broadcastState(BroadcasterState.Connecting)
            }
            Streamer.CONNECTION_STATE.CONNECTED -> mLog.init(streamer, connectionId)
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

    private fun createStreamer(surfaceView: SurfaceView) {
        val context = surfaceView.context
        val builder = StreamerGLBuilder()

        builder.setContext(context)
        builder.setListener(this)

        // configure audio
        builder.setAudioConfig(BroadcasterUtil.getAudioConfig(mConfig))

        // configure camera
        builder.setCamera2(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        builder.setSurface(surfaceView.holder.surface)
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

        streamer = ExternalStreamerGLImpl(builder.build())
    }

    private fun safeStartPreview() {
        Handler().postDelayed({
            streamer?.startVideoCapture()
            streamer?.startAudioCapture()
        }, SAFE_OPEN_CAMERA_DELAYED)
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
        streamer?.setFrontMirror(false, false)
    }

    private fun broadcastState(state: BroadcasterState) {
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
                    mContext?.let { mLog.update(it, mConfig, mLogger) }
                    mListener?.onUpdateLivePusherStatistic(mLog)
                }
            }

        }, DELAYED_TIME, PERIOD_TIME)
    }

    private fun cancelStatsJob() {
        try {
            statisticUpdateTimer?.cancel()
        } catch (ignored: Exception) { }
    }

    companion object {
        private const val SAFE_OPEN_CAMERA_DELAYED = 500L

        private const val DELAYED_TIME = 1000L
        private const val PERIOD_TIME = 1000L
    }

}