package com.tokopedia.play.broadcaster.pusher.apsara

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.view.SurfaceView
import androidx.core.app.ActivityCompat
import com.alivc.live.pusher.*
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.play.broadcaster.util.extension.sendCrashlyticsLog
import java.io.File
import java.util.regex.Pattern


/**
 * Created by mzennis on 22/09/20.
 *
 * Doc:
 * https://github.com/mzennis/live/blob/master/intl.en-US/Stream%20ingest%20SDK/Android%20stream%20ingest%20SDK/SDK%20usage.md
 */
class ApsaraLivePusher(@ApplicationContext private val mContext: Context) {

    companion object {
        private const val PATTERN_URL = "((rtmp|rtsp)?://[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,})"
    }

    private val mApsaraLivePusherConfig = ApsaraLivePusherConfig().apply {
        setCameraType(AlivcLivePushCameraTypeEnum.CAMERA_TYPE_FRONT)

        // set the screen orientation to portrait
        setPreviewOrientation(AlivcPreviewOrientationEnum.ORIENTATION_PORTRAIT)

        // indicates that the stream ingest SDK crops the video to fit the screen for a preview.
        // If the aspect ratio of the video is different from that of the preview screen, the video is cropped for the preview.
        // These preview modes do not affect stream ingest.
        previewDisplayMode = AlivcPreviewDisplayMode.ALIVC_LIVE_PUSHER_PREVIEW_ASPECT_FILL

        // max resolution
        setResolution(AlivcResolutionEnum.RESOLUTION_720P)

        // max fps
        setFps(AlivcFpsEnum.FPS_30)

        // configure mirroring
        setPushMirror(this.getCameraTypeEnum() == AlivcLivePushCameraTypeEnum.CAMERA_TYPE_FRONT)

        //  the resolution-first mode in which the stream ingest SDK sets the bitrate to prioritize the resolution of video streams.
        qualityMode = AlivcQualityModeEnum.QM_RESOLUTION_FIRST
        isEnableBitrateControl = true
        isEnableAutoResolution = true
        pausePushImage = mContext.filesDir.path.toString() + File.separator + "sample_pause_push_image.png"
        networkPoorPushImage = mContext.filesDir.path.toString() + File.separator + "sample_pause_push_image.png"
    }

    var mApsaraLivePusherInfoListener: ApsaraLivePusherInfoListener? = null
    var mApsaraLivePusherStatus: ApsaraLivePusherStatus = ApsaraLivePusherStatus.Idle

    private var mAliVcLivePusher: AlivcLivePusher? = null
    private var mIngestUrl: String = ""

    fun init() {
        if (mAliVcLivePusher != null) {
            mAliVcLivePusher?.destroy()
            mAliVcLivePusher = null
        }
        try {
            mAliVcLivePusher = AlivcLivePusher()
            mAliVcLivePusher?.init(mContext, mApsaraLivePusherConfig)
            mAliVcLivePusher?.setLivePushErrorListener(mAliVcLivePushErrorListener)
            mAliVcLivePusher?.setLivePushNetworkListener(mAliVcLivePushNetworkListener)
            mAliVcLivePusher?.setLivePushInfoListener(mAliVcLivePushInfoListener)
            mAliVcLivePusher?.setAudioDenoise(true)
        } catch (e: Exception) {
            sendCrashlyticsLog(e)
        }
    }

    fun startPreview(surfaceView: SurfaceView) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            return
        }
        try {
            mAliVcLivePusher?.startPreviewAysnc(surfaceView)
        } catch (e: Exception) {
            sendCrashlyticsLog(e)
        }
    }

    fun switchCamera() {
        try {
            mAliVcLivePusher?.switchCamera()
            mAliVcLivePusher?.setPushMirror(mApsaraLivePusherConfig.getCameraTypeEnum()
                    == AlivcLivePushCameraTypeEnum.CAMERA_TYPE_FRONT)
        } catch (e: Exception) {
            sendCrashlyticsLog(e)
        }
    }

    fun stopPreview() {
        try {
            mAliVcLivePusher?.stopPreview()
        } catch (e: Exception) {
            sendCrashlyticsLog(e)
        }
    }

    @Throws(IllegalArgumentException::class)
    fun startPush(ingestUrl: String)  {
        if (ingestUrl.isNotEmpty()) {
            this.mIngestUrl = ingestUrl
        }
        if (this.mIngestUrl.isBlank()) {
            throw IllegalArgumentException("ingestUrl must not be empty")
        }
//        if (!validUrl(ingestUrl)) {
//            throw IllegalArgumentException("ingestUrl must valid url")
//        }
        mAliVcLivePusher?.startPushAysnc(this.mIngestUrl)
    }

    private fun validUrl(ingestUrl: String) : Boolean = Pattern.compile(PATTERN_URL)
            .matcher(ingestUrl)
            .matches()

    fun restartPush() {
        mAliVcLivePusher?.restartPushAync()
    }

    fun stopPush() {
        try {
            mAliVcLivePusher?.stopPush()
        } catch (e: Exception) {
            sendCrashlyticsLog(e)
        }
        mApsaraLivePusherStatus = ApsaraLivePusherStatus.Stop
        mApsaraLivePusherInfoListener?.onStop()
    }

    fun resume() {
        try {
            mAliVcLivePusher?.resumeAsync()
        } catch (e: Exception) {
            sendCrashlyticsLog(e)
        }
    }

    fun pause() {
        try {
            mAliVcLivePusher?.pause()
        } catch (e: Exception) {
            sendCrashlyticsLog(e)
        }
    }

    fun destroy() {
        try {
            mAliVcLivePusher?.destroy()
        } catch (e: Exception) {
            sendCrashlyticsLog(e)
        }
    }

    private fun reconnectPushAsync() {
        try {
            mAliVcLivePusher?.reconnectPushAsync(mIngestUrl)
        } catch (e: Exception) {
            sendCrashlyticsLog(e)
        }
    }

    private val mAliVcLivePushErrorListener = object : AlivcLivePushErrorListener {
        override fun onSystemError(pusher: AlivcLivePusher?, pusherError: AlivcLivePushError?) {
            mApsaraLivePusherStatus = ApsaraLivePusherStatus.Error(ApsaraLivePusherErrorStatus.SystemError)
            mApsaraLivePusherInfoListener?.onError(ApsaraLivePusherErrorStatus.SystemError)
        }

        override fun onSDKError(pusher: AlivcLivePusher?, pusherError: AlivcLivePushError?) {
            mApsaraLivePusherStatus = ApsaraLivePusherStatus.Error(ApsaraLivePusherErrorStatus.SystemError)
            mApsaraLivePusherInfoListener?.onError(ApsaraLivePusherErrorStatus.SystemError)
        }
    }

    private val mAliVcLivePushNetworkListener = object : AlivcLivePushNetworkListener {
        override fun onNetworkRecovery(pusher: AlivcLivePusher?) {
            // Indicates that the network is recovered.
        }

        override fun onSendMessage(pusher: AlivcLivePusher?) {
        }

        override fun onReconnectFail(pusher: AlivcLivePusher?) {
            mApsaraLivePusherStatus = ApsaraLivePusherStatus.Error(ApsaraLivePusherErrorStatus.ReconnectFailed)
            mApsaraLivePusherInfoListener?.onError(ApsaraLivePusherErrorStatus.ReconnectFailed)
            reconnectPushAsync()
        }

        override fun onConnectionLost(p0: AlivcLivePusher?) {
        }

        override fun onSendDataTimeout(pusher: AlivcLivePusher?) {
            // Indicates that data transmission times out.
            mApsaraLivePusherStatus = ApsaraLivePusherStatus.Error(ApsaraLivePusherErrorStatus.NetworkLoss)
            mApsaraLivePusherInfoListener?.onError(ApsaraLivePusherErrorStatus.NetworkLoss)
            reconnectPushAsync()
        }

        override fun onConnectFail(pusher: AlivcLivePusher?) {
            mApsaraLivePusherStatus = ApsaraLivePusherStatus.Error(ApsaraLivePusherErrorStatus.ConnectFailed)
            mApsaraLivePusherInfoListener?.onError(ApsaraLivePusherErrorStatus.ConnectFailed)
        }

        override fun onPacketsLost(p0: AlivcLivePusher?) {
        }

        override fun onReconnectStart(pusher: AlivcLivePusher?) {
            // Indicates that a reconnection starts.
        }

        override fun onReconnectSucceed(pusher: AlivcLivePusher?) {
            // Indicates that a reconnection is successful.
            mApsaraLivePusherStatus = ApsaraLivePusherStatus.Live
            mApsaraLivePusherInfoListener?.onRecovered()
        }

        override fun onPushURLAuthenticationOverdue(pusher: AlivcLivePusher?): String {
            return ""
        }

        override fun onNetworkPoor(pusher: AlivcLivePusher?) {
            // Indicates poor network conditions.
            mApsaraLivePusherStatus = ApsaraLivePusherStatus.Error(ApsaraLivePusherErrorStatus.NetworkPoor)
            mApsaraLivePusherInfoListener?.onError(ApsaraLivePusherErrorStatus.NetworkPoor)
        }
    }

    private val mAliVcLivePushInfoListener = object : AlivcLivePushInfoListener {
        override fun onPreviewStarted(pusher: AlivcLivePusher?) {
            // Indicates that a preview starts.
        }

        override fun onPreviewStoped(pusher: AlivcLivePusher?) {
            // Indicates that a preview stops.
        }

        override fun onPushStarted(pusher: AlivcLivePusher?) {
            mApsaraLivePusherStatus = ApsaraLivePusherStatus.Live
            mApsaraLivePusherInfoListener?.onStarted()
        }

        override fun onPushPauesed(pusher: AlivcLivePusher?) {
            if (mApsaraLivePusherStatus !is ApsaraLivePusherStatus.Live) return

            mApsaraLivePusherStatus = ApsaraLivePusherStatus.Pause
            mApsaraLivePusherInfoListener?.onPaused()
        }

        override fun onPushResumed(pusher: AlivcLivePusher?) {
            if (mApsaraLivePusherStatus !is ApsaraLivePusherStatus.Pause) return

            mApsaraLivePusherStatus = ApsaraLivePusherStatus.Live
            mApsaraLivePusherInfoListener?.onResumed()
        }

        override fun onPushStoped(pusher: AlivcLivePusher?) {
        }

        override fun onPushRestarted(pusher: AlivcLivePusher?) {
            mApsaraLivePusherStatus = ApsaraLivePusherStatus.Restart
            mApsaraLivePusherInfoListener?.onRestarted()
        }

        override fun onFirstFramePreviewed(pusher: AlivcLivePusher?) {
            // Indicates first-frame rendering.
        }

        override fun onDropFrame(pusher: AlivcLivePusher?, countBef: Int, countAft: Int) {
            // Indicates that frames are discarded.
        }

        override fun onFirstAVFramePushed(pusher: AlivcLivePusher?) {

        }

        override fun onAdjustBitRate(pusher: AlivcLivePusher?, curBr: Int, targetBr: Int) {
            // Indicates that the bitrate is adjusted.
        }

        override fun onAdjustFps(pusher: AlivcLivePusher?, curFps: Int, targetFps: Int) {
            // Indicates that the frame rate is adjusted.
        }
    }
}