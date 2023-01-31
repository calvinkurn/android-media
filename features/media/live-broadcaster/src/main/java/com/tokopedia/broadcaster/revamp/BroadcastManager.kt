package com.tokopedia.broadcaster.revamp

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.util.Pair
import android.view.Surface
import android.view.SurfaceHolder
import com.tokopedia.broadcaster.revamp.state.BroadcastInitState
import com.tokopedia.broadcaster.revamp.state.BroadcastState
import com.tokopedia.broadcaster.revamp.util.BroadcasterUtil
import com.tokopedia.broadcaster.revamp.util.bitrate.BroadcasterAdaptiveBitrate
import com.tokopedia.broadcaster.revamp.util.bitrate.BroadcasterAdaptiveBitrateImpl
import com.tokopedia.broadcaster.revamp.util.camera.BroadcasterCamera
import com.tokopedia.broadcaster.revamp.util.camera.BroadcasterCameraManager
import com.tokopedia.broadcaster.revamp.util.error.BroadcasterErrorType
import com.tokopedia.broadcaster.revamp.util.error.BroadcasterException
import com.tokopedia.broadcaster.revamp.util.statistic.BroadcasterMetric
import com.tokopedia.broadcaster.revamp.util.statistic.BroadcasterStatistic
import com.tokopedia.device.info.DeviceConnectionInfo
import com.wmspanel.libstream.*
import com.wmspanel.libstream.Streamer.VERSION_NAME
import org.json.JSONObject
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by meyta.taliti on 01/03/22.
 */
class BroadcastManager: Broadcaster, Streamer.Listener, BroadcasterAdaptiveBitrate.Listener, BroadcasterStatistic.Listener {

    override val broadcastState: BroadcastState
        get() = mState

    override val broadcastInitState: BroadcastInitState
        get() = mInitState

    override val activeCameraVideoSize: Broadcaster.Size?
        get() = mStreamerGL?.activeCameraVideoSize?.let {
            Broadcaster.Size(it.width, it.height)
        }

    private var mStreamer: Streamer? = null
    private var mStreamerGL: StreamerGL? = null

    private var mConnectionId: Pair<Int, ConnectionConfig>? = null
    private var mConnectionState: Streamer.CONNECTION_STATE? = null

    private var mVideoCaptureState: Streamer.CAPTURE_STATE? = Streamer.CAPTURE_STATE.FAILED
    private var mAudioCaptureState: Streamer.CAPTURE_STATE? = Streamer.CAPTURE_STATE.FAILED

    private var mCameraManager: BroadcasterCameraManager? = null
    private var mSelectedCamera: BroadcasterCamera? = null

    private var mAdaptiveBitrate: BroadcasterAdaptiveBitrate? = null

    private var mIsStatisticEnabled: Boolean = false
    private var mStatisticTimerInterval: Long = STATISTIC_DEFAULT_INTERVAL
    private var mStatistic: BroadcasterStatistic? = null
    private var mStatisticTimer: Timer? = null
    private var mMetric: BroadcasterMetric = BroadcasterMetric.Empty

    private var mContext: Context? = null
    private var mHandler: Handler? = null
    private val mListeners: ConcurrentLinkedQueue<Broadcaster.Listener> = ConcurrentLinkedQueue()

    private var mInitState: BroadcastInitState = BroadcastInitState.Uninitialized
    private var mState: BroadcastState = BroadcastState.Unprepared

    private var mBroadcastOn = false

    private var mAudioRate: String? = null
    private var mVideoRate: String? = null
    private var mFps: String? = null

    private val mAudioCallback =
        Streamer.AudioCallback { audioFormat, data, audioInputLength, channelCount, sampleRate, samplesPerFrame ->

            /**
             * @param audioFormat [android.media.AudioFormat.ENCODING_PCM_16BIT]
             * @param data
             * @param audioInputLength [android.media.AudioRecord.read]
             * @param channelCount
             * @param sampleRate
             * @param samplesPerFrame AAC frame size (1024 samples)
             */

            // If your app needs advanced audio processing (boost input volume, etc.), you can modify
            // raw pcm data before it goes to aac encoder.
            // Arrays.fill(data, (byte) 0); // "Mute" audio
        }

    override fun setAudioRate(audioRate: String) {
        mAudioRate = audioRate
    }

    override fun setVideoRate(videoRate: String) {
        mVideoRate = videoRate
    }

    override fun setFps(fps: String) {
        mFps = fps
    }

    override fun addListener(listener: Broadcaster.Listener) {
        mListeners.add(listener)
    }

    override fun removeListener(listener: Broadcaster.Listener) {
        mListeners.remove(listener)
    }

    override fun init(activityContext: Context, handler: Handler?) {
        mContext = activityContext
        mHandler = handler
    }

    override fun create(holder: SurfaceHolder, surfaceSize: Broadcaster.Size) {
        if (mStreamer != null) return

        val context = mContext
        if (context == null) {
            broadcastInitStateChanged(
                BroadcastInitState.Error(
                    BroadcasterException(BroadcasterErrorType.ContextNotFound)
                )
            )
            return
        }

        mCameraManager = BroadcasterCameraManager.newInstance(context)
        val cameraManager = mCameraManager ?: return
        val cameraList = cameraManager.getCameraList()
        if (cameraList.isNullOrEmpty()) {
            broadcastInitStateChanged(
                BroadcastInitState.Error(
                    BroadcasterException(BroadcasterErrorType.CameraNotFound)
                )
            )
            return
        }

        val builder = StreamerGLBuilder()

        // common
        builder.setContext(context)
        builder.setListener(this)
        builder.setUserAgent("Larix/$VERSION_NAME")

        // audio
        val audioConfig = BroadcasterUtil.getAudioConfig(mAudioRate)
        builder.setAudioConfig(audioConfig)

        // video
        builder.setCamera2(BroadcasterCameraManager.allowCamera2Support(context))

        val videoConfig = BroadcasterUtil.getVideoConfig(mVideoRate, mFps)

        // get camera id
        val activeCamera = mSelectedCamera ?: findPreferredCamera(cameraList).also {
            mSelectedCamera = it
        }

        // video resolution for stream and mp4 recording,
        // larix uses same resolution for camera preview and stream to simplify setup
        val videoSize = BroadcasterUtil.getVideoSize(activeCamera.recordSizes, TARGET_ASPECT_RATIO)

        // Stream resolution is not tied to camera preview sizes
        // For example, you need gorgeous FullHD preview and smaller stream to save traffic
        // To achieve this, you should first set VideoConfig.videoSize to 640x360,
        // then pass 1920x1080 to addCamera()

        // The Android Compatibility Definition Document (CDD) defines a set of mandatory resolutions
        // https://source.android.com/compatibility/cdd.html

        // If you need same resolution for all devices, check if resolution is listed in section 5.2 of CDD,
        // please note that resolution list is not stable and changes from v4.3 to v5.1
        // On API 21 you can check if resolution is supported with MediaCodecInfo.VideoCapabilities#getVideoCapabilities()

        // Larix itself rely on camera preview resolution set
        // http://stackoverflow.com/questions/32076412/find-the-mediacodec-supported-video-encoding-resolutions-under-jelly-bean-mr2

        // If stream and camera resolutions are different, then video will be
        // up-scaled or down-scaled to fill 100% of stream

        // If aspect ratios of stream and camera are different, video will be be letterboxed
        // or pillarboxed to fit stream
        // https://en.wikipedia.org/wiki/Letterboxing_(filming)

        // This is useful option for camera flip, for example if back camera can do 1280x720 HD
        // and front camera can only produce 640x480
        videoConfig.videoSize = getAndroidVideoSize(videoSize)

        // verify video resolution support by encoder
        val supportedSize = BroadcasterUtil.verifyResolution(videoConfig.type, videoConfig.videoSize)
        if (!videoConfig.videoSize.equals(supportedSize)) {
            videoConfig.videoSize = getAndroidVideoSize(supportedSize)
        }

        builder.setVideoConfig(videoConfig)

        // camera preview and it's size, if surface view size changes, then
        // app should pass new size to streamer (refer to surfaceChanged() for details)

        // show the camera preview with the black stripes on the sides
        builder.setFullView(false)

        // note that camera preview is not affected by setVideoOrientation
        // preview will fill entire rectangle, app should take care of aspect ratio
        // larix wraps preview surface with AspectFrameLayout to maintain a specific aspect ratio

        builder.setSurface(holder.surface)
        builder.setSurfaceSize(Streamer.Size(surfaceSize.width, surfaceSize.height))

        // orientation will be later changed to actual device orientation when user press "Broadcast" button
        // or will be updated dynamically from onConfigurationChanged listener if Live rotation is on
        // with corresponding mStreamerGL.setVideoOrientation(...) method call
        //
        // setVideoOrientation() doesn't change stream resolution set by VideoConfig.videoSize, for example:
        //
        // stream is 1920x1080, orientation is LANDSCAPE -> horizontal video
        // stream is 1920x1080, orientation is PORTRAIT -> pillarboxed horizontal video
        //
        // stream is 1080x1920, orientation is PORTRAIT -> vertical video
        // stream is 1080x1920, orientation is LANDSCAPE -> letterboxed vertical video
        //
        // try to turn "Video/Live rotation" and "Video/Vertical stream" on/off
        // to see these options in action
        //
        // refer to onConfigurationChanged, mCaptureButtonListener and mLockOrientation flag
        // to see how exactly Live rotation is implemented
        builder.setVideoOrientation(StreamerGL.ORIENTATIONS.PORTRAIT)

        // http://developer.android.com/reference/android/hardware/Camera.html#setDisplayOrientation%28int%29
        // will be updated from onConfigurationChanged and mCaptureButtonListener
        // with corresponding mStreamerGL.setDisplayRotation(...) method call
        // this value is required to make correct video rotation
        builder.setDisplayRotation(Surface.ROTATION_0)

        // start adding cameras from default camera, then add second camera
        // larix uses same resolution for camera preview and stream to simplify setup

        // add first camera to flip list, make sure you called setVideoConfig before
        builder.addCamera(
            CameraConfig().apply {
                this.cameraId = activeCamera.cameraId
                this.videoSize = videoSize
            }
        )

        // set start position in flip list to camera id
        builder.setCameraId(activeCamera.cameraId)
        cameraList.forEach {
            if (it.cameraId != activeCamera.cameraId) {
                // add next camera to flip list
                builder.addCamera(
                    CameraConfig().apply {
                        this.cameraId = it.cameraId
                        this.videoSize = BroadcasterUtil.findFlipSize(it.recordSizes, videoSize)
                    }
                )
            }
        }

        mStreamerGL = builder.build()
        if (mStreamerGL == null) {
            broadcastInitStateChanged(
                BroadcastInitState.Error(
                    BroadcasterException(BroadcasterErrorType.ServiceUnrecoverable)
                )
            )
            return
        }

        mStreamer = mStreamerGL

        // Streamer build succeeded, can start Video/Audio capture
        // call startVideoCapture, wait for onVideoCaptureStateChanged callback
        startVideoCapture()
        // call startAudioCapture, wait for onAudioCaptureStateChanged callback
        startAudioCapture()

        mAdaptiveBitrate = BroadcasterAdaptiveBitrateImpl(
            BroadcasterAdaptiveBitrate.Builder(
                BroadcasterAdaptiveBitrate.Mode.LadderAscend
            )
                .setBitrate(videoConfig.bitRate)
                .setFpsRanges(activeCamera.fpsRanges)
        ).apply {
            setListener(this@BroadcastManager)
        }

        mMetric = mMetric.copy(
            audioBitrate = audioConfig.bitRate.toLong(),
            videoBitrate = videoConfig.bitRate.toLong(),
            fps = videoConfig.fps.toDouble(),
            resolutionWidth = videoConfig.videoSize.width.toLong(),
            resolutionHeight = videoConfig.videoSize.height.toLong(),
        )

        broadcastInitStateChanged(BroadcastInitState.Initialized)
    }

    // To get vertical video just swap width and height
    // do not modify videoSize itself because Android camera is always landscape
    private fun getAndroidVideoSize(videoSize: Streamer.Size): Streamer.Size {
        return if(videoSize.height > videoSize.width) {
            Streamer.Size(videoSize.width, videoSize.height)
        } else {
            // noinspection SuspiciousNameCombination
            Streamer.Size(videoSize.height, videoSize.width)
        }
    }

    override fun updateSurfaceSize(surfaceSize: Broadcaster.Size) {
        mStreamerGL?.setSurfaceSize(Streamer.Size(surfaceSize.width, surfaceSize.height))
    }

    override fun start(rtmpUrl: String) {
        if (mStreamer == null) return

        if (rtmpUrl.isEmpty()) {
            broadcastStateChanged(
                BroadcastState.Error(BroadcasterException(BroadcasterErrorType.UrlEmpty))
            )
            return
        }

        val connectionConfig = BroadcasterUtil.getConnectionConfig(rtmpUrl)
        val success = createConnection(connectionConfig)
        if (success) broadcastStateChanged(BroadcastState.Started)
    }

    private fun isStreamerReady(): Boolean {
        return isAudioCaptureStarted() && isVideoCaptureStarted()
    }

    private fun isInternetAvailable(context: Context): Boolean {
        return DeviceConnectionInfo.isConnectWifi(context) ||
                DeviceConnectionInfo.isConnectCellular(context)
    }

    private fun createConnection(connectionConfig: ConnectionConfig): Boolean {
        val context = mContext ?: return false
        if (!isStreamerReady()) {
            broadcastStateChanged(
                BroadcastState.Error(
                    BroadcasterException(BroadcasterErrorType.ServiceNotReady)
                )
            )
            return false
        }

        if (!isInternetAvailable(context)) {
            broadcastStateChanged(
                BroadcastState.Error(
                    BroadcasterException(BroadcasterErrorType.InternetUnavailable)
                )
            )
            return false
        }

        val connectionId = mStreamer?.createConnection(connectionConfig) ?: -1
        if (connectionId == -1) {
            broadcastStateChanged(
                BroadcastState.Error(
                    BroadcasterException(BroadcasterErrorType.StartFailed)
                )
            )
            return false
        }

        mBroadcastOn = true

        mConnectionId = Pair<Int, ConnectionConfig>(connectionId, connectionConfig)
        startTracking(connectionId)

        mAdaptiveBitrate?.start(connectionId)
        return true
    }

    private fun releaseConnection() {
        stopTracking()

        mConnectionId?.let {
            mStreamer?.releaseConnection(it.first)
        }

        mAdaptiveBitrate?.stop()
        mConnectionState = null
    }

    override fun retry() {
        val connectionConfig = mConnectionId?.second ?: return

        releaseConnection()
        if (mBroadcastOn) createConnection(connectionConfig)
    }

    override fun stop() {
        mBroadcastOn = false
        broadcastStateChanged(BroadcastState.Stopped)
        releaseConnection()
        mConnectionId = null
    }

    override fun release() {
        if (mStreamer == null) return
        // stop broadcast
        stop()
        // cancel audio and video capture
        mStreamer?.stopAudioCapture()
        mStreamer?.stopVideoCapture()
        // finally release streamer, after release(), the object is no longer available
        // if a Streamer is in released state, all methods will throw an IllegalStateException
        mStreamer?.release()
        // sanitize Streamer object holder
        mStreamer = null

        // discard adaptive bitrate calculator
        mAdaptiveBitrate = null

        mStreamerGL = null

        broadcastInitStateChanged(BroadcastInitState.Uninitialized)
    }

    override fun destroy() {
        mContext = null
        mHandler = null
        mSelectedCamera = null
    }

    override fun flip() {
        if (mStreamer == null || !isVideoCaptureStarted()) {
            // preventing accidental touch issues
            return
        }
        mAdaptiveBitrate?.pause()
        mStreamerGL?.flip()

        // Re-select camera
        val cameraManager = mCameraManager ?: return
        val cameraList = cameraManager.getCameraList()
        if (cameraList.isNullOrEmpty()) return
        mSelectedCamera = findPreferredCamera(cameraList)

        updateFpsRanges(mSelectedCamera)

        if (mBroadcastOn) mAdaptiveBitrate?.resume()
    }

    override fun snapShot() {
        if (mStreamer == null || !isVideoCaptureStarted()) {
            // preventing accidental touch issues
            return
        }

       // TODO("Not yet implemented")
    }

    override fun enableStatistic(interval: Long) {
        mIsStatisticEnabled = true
        mStatisticTimerInterval = interval
    }

    override fun getHandler(): Handler? {
        return mHandler
    }

    override fun onConnectionStateChanged(
        connectionId: Int,
        state: Streamer.CONNECTION_STATE?,
        status: Streamer.STATUS?,
        info: JSONObject?,
    ) {
        if (mStreamer == null) return
        if (mConnectionId?.first != connectionId) return

        mConnectionState = state

        when (state) {
            Streamer.CONNECTION_STATE.INITIALIZED,
            Streamer.CONNECTION_STATE.SETUP,
            Streamer.CONNECTION_STATE.RECORD,
            -> {
            }
            Streamer.CONNECTION_STATE.CONNECTED -> {
                mStatistic?.start(connectionId)
                if (mState is BroadcastState.Error) broadcastStateChanged(BroadcastState.Recovered)
            }
            Streamer.CONNECTION_STATE.IDLE -> {
                // connection established successfully, but no data is flowing
                // Larix app expect data always flowing, so this is error for us
                // but in some special cases app can pause capture and keep connection alive
                //
                // real-life example: video chat app opens full screen Gallery to select picture
                // camera will be closed in onPause, but app keeps connection alive to keep
                // ongoing stream recording on server; so idle state is expected and ignored
            }
            Streamer.CONNECTION_STATE.DISCONNECTED, null -> {
                if (status == Streamer.STATUS.AUTH_FAIL) {
                    broadcastStateChanged(
                        BroadcastState.Error(
                            BroadcasterException(BroadcasterErrorType.AuthFailed)
                        )
                    )
                } else {
                    broadcastStateChanged(
                        BroadcastState.Error(
                            BroadcasterException(BroadcasterErrorType.StreamFailed)
                        )
                    )
                }
            }
        }
    }

    override fun onVideoCaptureStateChanged(state: Streamer.CAPTURE_STATE?) {
        mVideoCaptureState = state

        when (state) {
            Streamer.CAPTURE_STATE.STARTED -> {
                // can start broadcasting video
                // mVideoCaptureState will be checked in start(rtmpUrl)
            }
            Streamer.CAPTURE_STATE.STOPPED -> {
                // stop confirmation
            }
            Streamer.CAPTURE_STATE.ENCODER_FAIL, Streamer.CAPTURE_STATE.FAILED, null -> {
                mStreamer?.stopVideoCapture()
                broadcastInitStateChanged(
                    BroadcastInitState.Error(
                        BroadcasterException(BroadcasterErrorType.VideoFailed)
                    )
                )
            }
        }
    }

    override fun onAudioCaptureStateChanged(state: Streamer.CAPTURE_STATE?) {
        mAudioCaptureState = state

        when (state) {
            Streamer.CAPTURE_STATE.STARTED -> {
                // can start broadcasting audio
                // mAudioCaptureState will be checked in start(rtmpUrl)
            }
            Streamer.CAPTURE_STATE.STOPPED -> {
                // stop confirmation
            }
            Streamer.CAPTURE_STATE.ENCODER_FAIL, Streamer.CAPTURE_STATE.FAILED, null -> {
                mStreamer?.stopAudioCapture()
                broadcastInitStateChanged(
                    BroadcastInitState.Error(
                        BroadcasterException(BroadcasterErrorType.AudioFailed)
                    )
                )
            }
        }
    }

    override fun onRecordStateChanged(
        state: Streamer.RECORD_STATE?,
        uri: Uri?,
        method: Streamer.SAVE_METHOD?,
    ) {
        // unused, can be ignored at least for now
    }

    override fun onSnapshotStateChanged(
        state: Streamer.RECORD_STATE?,
        uri: Uri?,
        method: Streamer.SAVE_METHOD?,
    ) {
        // todo: Not yet implemented
    }

    override fun isEligibleQuery(): Boolean {
        return mConnectionState == Streamer.CONNECTION_STATE.RECORD
    }

    override fun fps(): Double? {
        return mStreamer?.fps
    }

    override fun bytesSent(connectionId: Int): Long? {
        return mStreamer?.getBytesSent(connectionId)
    }

    override fun audioPacketsLost(connectionId: Int): Long? {
        return mStreamer?.getAudioPacketsLost(connectionId)
    }

    override fun videoPacketsLost(connectionId: Int): Long? {
        return mStreamer?.getVideoPacketsLost(connectionId)
    }

    override fun udpPacketsLost(connectionId: Int): Long? {
        return mStreamer?.getUdpPacketsLost(connectionId)
    }

    override fun changeBitrate(bitrate: Int) {
        mStreamer?.changeBitRate(bitrate)
        mMetric = mMetric.copy(
            videoBitrate = bitrate.toLong()
        )
    }

    override fun changeFpsRange(fpsRange: Streamer.FpsRange) {
        mStreamer?.changeFpsRange(fpsRange)
    }

    private fun findPreferredCamera(cameraList: List<BroadcasterCamera>): BroadcasterCamera {
        val activeCamId = mStreamerGL?.activeCameraId
        if (activeCamId != null) {
            val activeCamera = cameraList.firstOrNull { it.cameraId == activeCamId }
            if (activeCamera != null)
                return activeCamera
        }

        val frontFacingCamera = cameraList.firstOrNull { it.lensFacing == BroadcasterCamera.LENS_FACING_FRONT }
        if (frontFacingCamera != null)
            return frontFacingCamera

        return cameraList.first()
    }

    private fun startAudioCapture() {
        // Pass Streamer.AudioCallback instance to access raw pcm audio and calculate audio level
        mStreamer?.startAudioCapture(mAudioCallback)
    }

    private fun startVideoCapture() {
        mStreamer?.startVideoCapture()
    }

    private fun isAudioCaptureStarted(): Boolean {
        return mAudioCaptureState == Streamer.CAPTURE_STATE.STARTED
    }

    private fun isVideoCaptureStarted(): Boolean {
        return mVideoCaptureState == Streamer.CAPTURE_STATE.STARTED
    }

    private fun updateFpsRanges(activeCamera: BroadcasterCamera?) {
        if (activeCamera == null) return
        if (mAdaptiveBitrate == null) return

        if (activeCamera.fpsRanges != null) mAdaptiveBitrate?.setFpsRanges(activeCamera.fpsRanges)
    }

    private fun startTracking(connectionId: Int) {
        if (!mIsStatisticEnabled) return

        mStatistic = BroadcasterStatistic(this)

        mStatisticTimer = Timer()
        mStatisticTimer?.schedule(object : TimerTask() {
            override fun run() {
                val connectionState = mConnectionState ?: return
                if (connectionState != Streamer.CONNECTION_STATE.RECORD) return

                val statistic = mStatistic ?: return
                statistic.update(connectionId)
                mMetric = mMetric.copy(
                    traffic = statistic.getTraffic(),
                    bandwidth = statistic.getBandwidth(),
                    fps = statistic.getFps(),
                    packetLossIncreased = statistic.isPacketLossIncreased()
                )
                broadcastStatisticUpdate()
            }
        }, STATISTIC_TIMER_DELAY, mStatisticTimerInterval)
    }

    private fun stopTracking() {
        mStatisticTimer?.cancel()
        mStatisticTimer = null
        mStatistic = null
    }

    private fun broadcastStateChanged(state: BroadcastState) {
        mState = state
        mListeners.forEach { it.onBroadcastStateChanged(state) }
    }

    private fun broadcastInitStateChanged(initState: BroadcastInitState) {
        mInitState = initState
        mListeners.forEach { it.onBroadcastInitStateChanged(initState) }
    }

    private fun broadcastStatisticUpdate() {
        mListeners.forEach { it.onBroadcastStatisticUpdate(mMetric) }
    }

    companion object {
        private const val STATISTIC_TIMER_DELAY = 1000L
        private const val STATISTIC_DEFAULT_INTERVAL = 3000L

        private const val TARGET_ASPECT_RATIO = 16.0 / 9.0
    }
}
