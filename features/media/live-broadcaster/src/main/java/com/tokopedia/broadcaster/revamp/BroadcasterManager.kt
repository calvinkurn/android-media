package com.tokopedia.broadcaster.revamp

import android.net.Uri
import android.os.Handler
import android.util.Log
import android.util.Pair
import android.view.Surface
import android.view.SurfaceHolder
import com.tokopedia.broadcaster.revamp.util.BroadcasterUtil
import com.tokopedia.broadcaster.revamp.util.bitrate.BroadcasterAdaptiveBitrate
import com.tokopedia.broadcaster.revamp.util.bitrate.BroadcasterAdaptiveBitrateImpl
import com.tokopedia.broadcaster.revamp.util.camera.BroadcasterCamera
import com.tokopedia.broadcaster.revamp.util.camera.BroadcasterCameraManager
import com.tokopedia.broadcaster.revamp.util.statistic.BroadcasterStatisticManager
import com.tokopedia.device.info.DeviceConnectionInfo
import com.wmspanel.libstream.*
import com.wmspanel.libstream.Streamer.*
import org.json.JSONObject
import java.util.*

/**
 * Created by meyta.taliti on 01/03/22.
 */
class BroadcasterManager : Broadcaster, Listener, BroadcasterAdaptiveBitrate.Listener, BroadcasterStatisticManager.Listener {

    private var mStreamer: Streamer? = null
    private var mStreamerGL: StreamerGL? = null

    private var mConnectionId: Pair<Int, ConnectionConfig>? = null
    private var mConnectionState: CONNECTION_STATE? = null

    private var mVideoCaptureState: CAPTURE_STATE? = CAPTURE_STATE.FAILED
    private var mAudioCaptureState: CAPTURE_STATE? = CAPTURE_STATE.FAILED

    private var mCameraManager: BroadcasterCameraManager? = null

    private var mAdaptiveBitrate: BroadcasterAdaptiveBitrate? = null

    private var mStatisticManager: BroadcasterStatisticManager? = null
    private var mStatisticTimer: Timer? = null

    private var mListener: Broadcaster.Listener? = null

    private val mAudioCallback =
        AudioCallback { audioFormat, data, audioInputLength, channelCount, sampleRate, samplesPerFrame ->

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

    override fun setListener(listener: Broadcaster.Listener) {
        mListener = listener
    }

    override fun create(holder: SurfaceHolder, surfaceSize: Broadcaster.Size) {
        if (mStreamer != null) {
            Log.e(TAG, "mStreamer != null")
            return
        }

        val context = mListener?.getActivityContext()
        if (mListener == null || context == null) {
            // todo: log & show error to user
            Log.e(TAG, "mListener == null ||  context == null")
            return
        }

        mCameraManager = BroadcasterCameraManager.newInstance(context)
        val cameraManager = mCameraManager ?: return
        val cameraList = cameraManager.getCameraList()
        if (cameraList.isNullOrEmpty()) {
            // todo: log & show error to user
            Log.e(TAG, "cameraList.isNullOrEmpty()")
            return
        }

        val builder = StreamerGLBuilder()

        // common
        builder.setContext(context)
        builder.setListener(this)
        builder.setUserAgent("Larix/$VERSION_NAME")

        // audio
        val audioConfig = BroadcasterUtil.getAudioConfig()
        builder.setAudioConfig(audioConfig)

        // video
        builder.setCamera2(BroadcasterCameraManager.allowCamera2Support(context))

        val videoConfig = BroadcasterUtil.getVideoConfig()

        // get default camera id
        val activeCamera = cameraManager.getCameraList()
            .firstOrNull { it.lensFacing == BroadcasterCamera.LENS_FACING_FRONT } ?:
        cameraManager.getCameraList().first()

        // video resolution for stream and mp4 recording,
        // larix uses same resolution for camera preview and stream to simplify setup
        val videoSize = BroadcasterUtil.getVideoSize(activeCamera)

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

        // To get vertical video just swap width and height
        // do not modify videoSize itself because Android camera is always landscape
        // noinspection SuspiciousNameCombination
        videoConfig.videoSize = Size(videoSize.height, videoSize.width)

        // verify video resolution support by encoder
        val supportedSize = BroadcasterUtil.verifyResolution(videoConfig.type, videoConfig.videoSize)
        if (!videoConfig.videoSize.equals(supportedSize)) {
            // todo: log & show error to user
            Log.i(TAG, "!videoConfig.videoSize.equals(supportedSize)")
            videoConfig.videoSize = supportedSize
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
        // builder.setSurfaceSize(Streamer.Size(binding.surfaceView.getWidth(), binding.surfaceView.getHeight()))
        builder.setSurfaceSize(Size(surfaceSize.width, surfaceSize.height))

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
                        this.videoSize = BroadcasterUtil.findFlipSize(it, videoSize)
                    }
                )
            }
        }

        mStreamerGL = builder.build()
        if (mStreamerGL == null) {
            Log.e(TAG, "mStreamerGL == null)")
            return
        }

        mStreamer = mStreamerGL

        // Streamer build succeeded, can start Video/Audio capture
        // call startVideoCapture, wait for onVideoCaptureStateChanged callback
        startVideoCapture()
        // call startAudioCapture, wait for onAudioCaptureStateChanged callback
        startAudioCapture()

        // Deal with preview's aspect ratio
        mStreamerGL?.activeCameraVideoSize?.let {
            mListener?.updateAspectFrameSize(Broadcaster.Size(it.width, it.height))
        }

        mAdaptiveBitrate = BroadcasterAdaptiveBitrateImpl(
            BroadcasterAdaptiveBitrate.Builder(
                BroadcasterAdaptiveBitrate.Mode.LadderAscend
            )
                .setBitrate(videoConfig.bitRate)
                .setFpsRanges(activeCamera.fpsRanges)
        ).apply {
            setListener(this@BroadcasterManager)
        }
    }

    override fun updateSurfaceSize(surfaceSize: Broadcaster.Size) {
        mStreamerGL?.setSurfaceSize(Size(surfaceSize.width, surfaceSize.height))
    }

    override fun start(rtmpUrl: String) {
        if (mStreamer == null || mListener == null) return
        val context = mListener?.getActivityContext() ?: return

        val isStreamerReady = isAudioCaptureStarted() && isVideoCaptureStarted()
        if (!isStreamerReady) {
            // todo: log & show error to user => Streamer is not ready, please wait
            return
        }

        if (!DeviceConnectionInfo.isInternetAvailable(context,
                checkWifi = true,
                checkCellular = true,
                checkEthernet = true)) {
            // todo: log & show error to user => No internet connection
            return
        }

        if (rtmpUrl.isEmpty()) {
            // todo: log & show error to user => RTMP URL is Empty
            return
        }

        val connectionConfig = BroadcasterUtil.getConnectionConfig(rtmpUrl)
        val connectionId = mStreamer?.createConnection(connectionConfig) ?: -1
        if (connectionId == -1) {
            // todo: log & show error to user => connection.name: Unknown error, please try again later.
            return
        }

        mConnectionId = Pair<Int, ConnectionConfig>(connectionId, connectionConfig)
        startTracking(connectionId)

        mAdaptiveBitrate?.start(connectionId)
    }

    override fun stop() {
        stopTracking()

        mConnectionId?.let {
            mStreamer?.releaseConnection(it.first)
        }

        mAdaptiveBitrate?.stop()
        mConnectionId = null
        mConnectionState = null
    }

    override fun release() {
        // check if Streamer instance exists
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

        mListener = null
    }

    override fun flip() {
        if (mStreamer == null || !isVideoCaptureStarted()) {
            // preventing accidental touch issues
            return
        }
        mAdaptiveBitrate?.pause()
        mStreamerGL?.flip()

        // camera is changed, so update aspect ratio to actual value
        mStreamerGL?.activeCameraVideoSize?.let {
            mListener?.updateAspectFrameSize(Broadcaster.Size(it.width, it.height))
        }

        updateFpsRanges()
        mAdaptiveBitrate?.resume()
    }

    override fun snapShot() {
        if (mStreamer == null || !isVideoCaptureStarted()) {
            // preventing accidental touch issues
            return
        }

        TODO("Not yet implemented")
    }

    override fun getHandler(): Handler? {
        return mListener?.getHandler()
    }

    override fun onConnectionStateChanged(
        connectionId: Int,
        state: CONNECTION_STATE?,
        status: STATUS?,
        info: JSONObject?,
    ) {
        if (mStreamer == null) return
        if (mConnectionId?.first != connectionId) return

        mConnectionState = state

        when (state) {
            CONNECTION_STATE.INITIALIZED,
            CONNECTION_STATE.SETUP,
            CONNECTION_STATE.RECORD,
            -> {
            }
            CONNECTION_STATE.CONNECTED -> mStatisticManager?.start(connectionId)
            CONNECTION_STATE.IDLE -> {
                // connection established successfully, but no data is flowing
                // Larix app expect data always flowing, so this is error for us
                // but in some special cases app can pause capture and keep connection alive
                //
                // real-life example: video chat app opens full screen Gallery to select picture
                // camera will be closed in onPause, but app keeps connection alive to keep
                // ongoing stream recording on server; so idle state is expected and ignored
            }
            CONNECTION_STATE.DISCONNECTED, null -> {
                // todo: log & show error to user: error message including connection name
            }
        }
    }

    override fun onVideoCaptureStateChanged(state: CAPTURE_STATE?) {
        mVideoCaptureState = state

        when (state) {
            CAPTURE_STATE.STARTED -> {
                // can start broadcasting video
                // mVideoCaptureState will be checked in createConnections()
            }
            CAPTURE_STATE.STOPPED -> {
                // stop confirmation
            }
            CAPTURE_STATE.ENCODER_FAIL, CAPTURE_STATE.FAILED, null -> {
                mStreamer?.stopVideoCapture()
                // todo: log & show error to user
            }
        }
    }

    override fun onAudioCaptureStateChanged(state: CAPTURE_STATE?) {
        mAudioCaptureState = state

        when (state) {
            CAPTURE_STATE.STARTED -> {
                // can start broadcasting audio
                // mAudioCaptureState will be checked in createConnection()
            }
            CAPTURE_STATE.STOPPED -> {
                // stop confirmation
            }
            CAPTURE_STATE.ENCODER_FAIL, CAPTURE_STATE.FAILED, null -> {
                mStreamer?.stopAudioCapture()
                // todo: log & show error to user
            }
        }
    }

    override fun onRecordStateChanged(
        state: RECORD_STATE?,
        uri: Uri?,
        method: SAVE_METHOD?,
    ) {
        // unused, can be ignored at least for now
    }

    override fun onSnapshotStateChanged(
        state: RECORD_STATE?,
        uri: Uri?,
        method: SAVE_METHOD?,
    ) {
        TODO("Not yet implemented")
    }

    override fun isEligibleQuery(): Boolean {
        return mConnectionState == CONNECTION_STATE.RECORD
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
        return mStreamer?.getVideoPacketsSent(connectionId)
    }

    override fun udpPacketsLost(connectionId: Int): Long? {
        return mStreamer?.getUdpPacketsLost(connectionId)
    }

    override fun changeBitrate(bitrate: Int) {
        mStreamer?.changeBitRate(bitrate)
    }

    override fun changeFpsRange(fpsRange: FpsRange) {
        mStreamer?.changeFpsRange(fpsRange)
    }

    private fun startAudioCapture() {
        // Pass Streamer.AudioCallback instance to access raw pcm audio and calculate audio level
        mStreamer?.startAudioCapture(mAudioCallback)
    }

    private fun startVideoCapture() {
        mStreamer?.startVideoCapture()
    }

    private fun isAudioCaptureStarted(): Boolean {
        return mAudioCaptureState == CAPTURE_STATE.STARTED
    }

    private fun isVideoCaptureStarted(): Boolean {
        return mVideoCaptureState == CAPTURE_STATE.STARTED
    }

    private fun updateFpsRanges() {
        if (mAdaptiveBitrate == null) return

        val camId = mStreamerGL?.activeCameraId
        val activeCamera = mCameraManager?.getCameraList()?.firstOrNull { it.cameraId == camId }
        activeCamera?.let {
            if (it.fpsRanges != null) mAdaptiveBitrate?.setFpsRanges(it.fpsRanges)
        }
    }

    private fun startTracking(connectionId: Int) {
        mStatisticManager = BroadcasterStatisticManager(this)

        mStatisticTimer = Timer()
        mStatisticTimer?.schedule(object : TimerTask() {
            override fun run() {
                val connectionState = mConnectionState ?: return
                if (connectionState != CONNECTION_STATE.RECORD) return

                val statisticManager = mStatisticManager ?: return
                statisticManager.update(connectionId)
                // mListener?.onStatisticInfoChanged(statisticManager.getStatistic())
            }
        }, STATISTIC_TIMER_DELAY, STATISTIC_TIMER_INTERVAL)
    }

    private fun stopTracking() {
        mStatisticTimer?.cancel()
        mStatisticTimer = null
        mStatisticManager = null
    }

    companion object {
        private const val STATISTIC_TIMER_DELAY = 1000L
        private const val STATISTIC_TIMER_INTERVAL = 1000L

        private const val TAG = "BroadcasterManager"
    }
}