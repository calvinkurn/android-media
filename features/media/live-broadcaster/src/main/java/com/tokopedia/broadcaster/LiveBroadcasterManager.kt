package com.tokopedia.broadcaster

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Handler
import com.tokopedia.broadcaster.bitrate.BitrateAdapter
import com.tokopedia.broadcaster.bitrate.BitrateAdapter.Companion.instance
import com.tokopedia.broadcaster.camera.CameraInfo
import com.tokopedia.broadcaster.camera.CameraManager
import com.tokopedia.broadcaster.camera.CameraType
import com.tokopedia.broadcaster.lib.BroadcasterConnection
import com.tokopedia.broadcaster.data.BroadcasterConfig
import com.tokopedia.broadcaster.data.consts.*
import com.tokopedia.broadcaster.lib.LarixStreamer
import com.tokopedia.broadcaster.lib.LarixStreamerFactory
import com.tokopedia.broadcaster.listener.BroadcasterListener
import com.tokopedia.broadcaster.state.BroadcasterState
import com.tokopedia.broadcaster.state.isError
import com.tokopedia.broadcaster.log.ui.notification.NotificationBuilder
import com.tokopedia.broadcaster.tracker.BroadcasterStatistic
import com.tokopedia.broadcaster.utils.BroadcasterUtils
import com.tokopedia.broadcaster.utils.BroadcasterUtils.getCameraConfig
import com.tokopedia.broadcaster.utils.DeviceInfo
import com.tokopedia.broadcaster.widget.SurfaceAspectRatioView
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.orZero
import com.wmspanel.libstream.Streamer
import com.wmspanel.libstream.StreamerGL
import com.wmspanel.libstream.StreamerGLBuilder
import kotlinx.coroutines.*
import org.json.JSONObject
import java.util.*
import kotlin.coroutines.CoroutineContext

class LiveBroadcasterManager constructor(
    var streamer: LarixStreamer? = null,
    var mConfig: BroadcasterConfig = BroadcasterConfig(),
    var mConnection: BroadcasterConnection = BroadcasterConnection(),
    val statistic: BroadcasterStatistic = BroadcasterStatistic(),
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
            throw IllegalAccessException(THROW_DEVICE_NOT_SUPPORTED)
        }

        if (!isDeviceHaveCameraAvailable(context)) {
            throw IllegalAccessException(THROW_NO_CAMERA_AVAILABLE)
        }
    }

    override fun prepare(config: BroadcasterConfig?) {
        if (hasPrepared) throw IllegalAccessException(THROW_STREAMER_ALREADY_PREPARED)
        if (config != null) mConfig = config

        configureStreamer(mConfig)
        hasPrepared = true
    }

    override fun setListener(listener: BroadcasterListener) {
        mListener = listener
    }

    override fun startPreview(surfaceView: SurfaceAspectRatioView) {
        if (streamer == null) createStreamer(surfaceView)
        streamer?.setSurface(surfaceView.surfaceHolder.surface)
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
        if (mContext == null) throw IllegalAccessException(THROW_FORGET_TO_INIT)

        if (GlobalConfig.DEBUG) {
            mContext?.let {
                NotificationBuilder.build(it, url)
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
        stopPreview()
        broadcastState(BroadcasterState.Paused)
    }

    override fun reconnect() {
        // don't use exponential retry for now
        releaseConnection()
        createConnection()
    }

    override fun stop() {
        stopStream()
        stopPreview()
        broadcastState(BroadcasterState.Stopped)
    }

    override fun release() {
        pauseCalculateAdaptiveBitrate()
        cancelStatsJob()
        streamer?.release()

        hasPrepared = false
        mListener = null
        mBitrateAdapter = null
        streamer = null
    }

    override fun getHandler(): Handler {
        return mHandler ?: throw IllegalStateException(THROW_FORGET_TO_INIT)
    }

    override fun onConnectionStateChanged(
        connectionId: Int,
        state: Streamer.CONNECTION_STATE?,
        status: Streamer.STATUS?,
        info: JSONObject?
    ) {
        if (state == null) return

        // already released connection
        if (connectionId != mConnection.connectionId) return

        val lastState = mState

        when (status) {
            Streamer.STATUS.CONN_FAIL -> broadcastState(
                BroadcasterState.Error(
                    if (isPushStarted) {
                        ERROR_NETWORK_FAIL
                    } else {
                        ERROR_CONNECTION_FAIL
                    }
                )
            )
            Streamer.STATUS.AUTH_FAIL -> broadcastState(
                BroadcasterState.Error(ERROR_AUTH_FAIL)
            )
            Streamer.STATUS.UNKNOWN_FAIL -> {
                if (state == Streamer.CONNECTION_STATE.DISCONNECTED
                    && (lastState is BroadcasterState.Stopped
                    || lastState is BroadcasterState.Paused)
                ) return

                if (info?.length().orZero() > 0) {
                    broadcastState(
                        BroadcasterState.Error("network: ${info?.toString()}")
                    )
                } else {
                    broadcastState(
                        BroadcasterState.Error(ERROR_UNKNOWN_FAIL)
                    )
                }
            }
            Streamer.STATUS.SUCCESS -> {
                when(state) {
                    Streamer.CONNECTION_STATE.IDLE -> broadcastState(BroadcasterState.Idle)
                    Streamer.CONNECTION_STATE.INITIALIZED,
                    Streamer.CONNECTION_STATE.SETUP,
                    Streamer.CONNECTION_STATE.DISCONNECTED -> {} // ignored
                    Streamer.CONNECTION_STATE.CONNECTED -> statistic.init(streamer, connectionId)
                    Streamer.CONNECTION_STATE.RECORD -> {
                        when {
                            lastState.isError -> broadcastState(BroadcasterState.Recovered)
                            isPushStarted -> broadcastState(BroadcasterState.Resumed)
                            else -> {
                                broadcastState(BroadcasterState.Started)
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
            Streamer.CAPTURE_STATE.ENCODER_FAIL -> broadcastState(
                BroadcasterState.Error(ERROR_VIDEO_ENCODING_FAIL)
            )
            Streamer.CAPTURE_STATE.FAILED -> broadcastState(
                BroadcasterState.Error(ERROR_VIDEO_CAPTURE_FAIL)
            )
            Streamer.CAPTURE_STATE.STARTED,
            Streamer.CAPTURE_STATE.STOPPED -> {
                // ignored
            }
        }
    }

    override fun onAudioCaptureStateChanged(state: Streamer.CAPTURE_STATE?) {
        if (state == null) return
        when(state) {
            Streamer.CAPTURE_STATE.ENCODER_FAIL -> broadcastState(
                BroadcasterState.Error(ERROR_AUDIO_ENCODING_FAIL)
            )
            Streamer.CAPTURE_STATE.FAILED -> broadcastState(
                BroadcasterState.Error(ERROR_AUDIO_CAPTURE_FAIL)
            )
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

    @SuppressLint("ObsoleteSdkInt")
    private fun createStreamer(surfaceView: SurfaceAspectRatioView) {
        val context = surfaceView.context
        val builder = StreamerGLBuilder()

        builder.setContext(context)
        builder.setListener(this)

        // configure audio
        builder.setAudioConfig(BroadcasterUtils.getAudioConfig(mConfig))

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
        val videoConfig = BroadcasterUtils.getVideoConfig(mConfig, cameraSize)
        builder.setVideoConfig(videoConfig)

        mAvailableCameras.forEach {
            builder.addCamera(getCameraConfig(it, cameraSize))
        }

        builder.setVideoOrientation(StreamerGL.ORIENTATIONS.PORTRAIT)
        builder.setDisplayRotation(0)
        builder.setFullView(true)

        mBitrateAdapter = instance(
            bitrate = videoConfig.bitRate.toLong(),
            bitrateMode = mConfig.bitrateMode,
            fpsRanges = activeCamera.fpsRanges
        )

        streamer = LarixStreamerFactory(builder.build())
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
        mAvailableCameras.clear()
        mAvailableCameras.addAll(CameraManager.getAvailableCameras(context))
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

    private fun releaseConnection() {
        try {
            mConnection.connectionId?.let { id -> streamer?.releaseConnection(id) }
        } catch (ignored: IllegalStateException) {}
    }

    private fun stopStream() {
        releaseConnection()
        stopCalculateAdaptiveBitrate()
        cancelStatsJob()
    }

    private fun configureStreamer(config: BroadcasterConfig) {
        streamer?.changeAudioConfig(BroadcasterUtils.getAudioConfig(config))
        streamer?.changeVideoConfig(BroadcasterUtils.getVideoConfig(config))
    }

    private fun setMirrorFrontCamera() {
        streamer?.setFrontMirror(
            isPreview = false,
            isStream = false
        )
    }

    fun broadcastState(state: BroadcasterState) {
        mState = state
        mListener?.onNewLivePusherState(state)
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
                    if (mContext != null && mConnection.uri != null) {
                        statistic.update(mContext, mConnection.uri, mConfig)
                    }
                    mListener?.onUpdateLivePusherStatistic(statistic)
                }
            }

        }, DELAYED_TIME, PERIOD_TIME)
    }

    private fun cancelStatsJob() {
        try {
            statisticUpdateTimer?.cancel()
            statisticUpdateTimer = null
            statistic.tracker.stopTrack()
        } catch (ignored: Exception) { }
    }

    companion object {
        private const val SAFE_OPEN_CAMERA_DELAYED = 500L
        private const val DELAYED_TIME = 1000L
        private const val PERIOD_TIME = 1000L
    }

}