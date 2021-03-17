package com.tokopedia.play.broadcaster.pusher

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.view.SurfaceView
import androidx.core.app.ActivityCompat
import com.alivc.live.pusher.*
import com.tokopedia.play.broadcaster.pusher.config.ApsaraLivePusherConfig
import com.tokopedia.play.broadcaster.pusher.config.DefaultApsaraLivePusherConfig
import com.tokopedia.play.broadcaster.pusher.listener.ApsaraLivePushInfoListenerImpl
import com.tokopedia.play.broadcaster.pusher.listener.ApsaraLivePusherErrorListenerImpl
import com.tokopedia.play.broadcaster.pusher.listener.ApsaraLivePusherNetworkListenerImpl
import com.tokopedia.play.broadcaster.pusher.state.ApsaraLivePusherState


/**
 * Created by mzennis on 15/03/21.
 */
class ApsaraLivePusherWrapper private constructor(
        private val context: Context,
        private val pusherConfig: ApsaraLivePusherConfig
) {

    class Builder(context: Context) {

        private val mContext: Context = context.applicationContext
        private var mApsaraLivePushConfig: ApsaraLivePusherConfig? = null

        fun setPushConfig(pushConfig: ApsaraLivePusherConfig): Builder {
            mApsaraLivePushConfig = pushConfig
            return this
        }

        fun build(): ApsaraLivePusherWrapper {
            return ApsaraLivePusherWrapper(mContext, mApsaraLivePushConfig ?: DefaultApsaraLivePusherConfig(mContext))
        }

    }

    private var aliVcLivePusher: AlivcLivePusher? = null

    private var listener: Listener? = null

    private var ingestUrl: String = ""

    private val aliVcLivePushInfoListener: AlivcLivePushInfoListener = ApsaraLivePushInfoListenerImpl(listener)
    private val alivcLivePushNetworkListener: AlivcLivePushNetworkListener = ApsaraLivePusherNetworkListenerImpl(listener)
    private val alivcLivePushErrorListener: AlivcLivePushErrorListener = ApsaraLivePusherErrorListenerImpl(listener)

    init {
        if (aliVcLivePusher != null) {
            aliVcLivePusher?.destroy()
            aliVcLivePusher = null
        }
        safeAction {
            aliVcLivePusher = AlivcLivePusher()
            aliVcLivePusher?.init(context, pusherConfig)
            aliVcLivePusher?.setLivePushInfoListener(aliVcLivePushInfoListener)
            aliVcLivePusher?.setLivePushNetworkListener(alivcLivePushNetworkListener)
            aliVcLivePusher?.setLivePushErrorListener(alivcLivePushErrorListener)
            aliVcLivePusher?.setAudioDenoise(true)
        }
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun startPreview(surfaceView: SurfaceView) {
        if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_DENIED) {
            listener?.onError(PLAY_PUSHER_ERROR_SYSTEM_ERROR, IllegalStateException("Camera permission denied"))
            return
        }

        safeAction { aliVcLivePusher?.startPreviewAysnc(surfaceView) }
    }

    fun switchCamera() {
        safeAction {
            aliVcLivePusher?.switchCamera()
            aliVcLivePusher?.setPushMirror(
                    pusherConfig.getCameraTypeEnum() == AlivcLivePushCameraTypeEnum.CAMERA_TYPE_FRONT
            )
        }
    }

    fun stopPreview() {
        safeAction { aliVcLivePusher?.stopPreview() }
    }

    fun start(ingestUrl: String)  {
        if (ingestUrl.isNotEmpty()) {
            this.ingestUrl = ingestUrl
        }
        if (ingestUrl.isEmpty() || ingestUrl.isBlank()) {
            listener?.onError(PLAY_PUSHER_ERROR_SYSTEM_ERROR, IllegalArgumentException("ingestUrl must not be empty"))
            return
        }

        safeAction { aliVcLivePusher?.startPushAysnc(ingestUrl) }
    }

    fun restart() {
        safeAction { aliVcLivePusher?.restartPushAync() }
    }

    fun stop() {
        safeAction { aliVcLivePusher?.stopPush() }
    }

    fun resume() {
        safeAction { aliVcLivePusher?.resumeAsync() }
    }

    fun pause() {
        safeAction { aliVcLivePusher?.pause() }
    }

    fun destroy() {
        safeAction { aliVcLivePusher?.destroy() }
    }

    fun reconnect() {
        safeAction { aliVcLivePusher?.reconnectPushAsync(ingestUrl) }
    }

    private fun safeAction(onAction: () -> Unit) {
        try {
            onAction()
        } catch (exception: Exception) {
            listener?.onError(PLAY_PUSHER_ERROR_SYSTEM_ERROR, exception)
        } catch (error: Error) {
            listener?.onError(PLAY_PUSHER_ERROR_SYSTEM_ERROR, error)
        }
    }

    interface Listener {

        fun onConnectionStateChanged(state: ApsaraLivePusherState)
        fun onError(code: Int, throwable: Throwable)
    }

    companion object {

        const val PLAY_PUSHER_ERROR_NETWORK_POOR = 102
        const val PLAY_PUSHER_ERROR_NETWORK_LOSS = 103
        const val PLAY_PUSHER_ERROR_CONNECTION_FAILED = 104
        const val PLAY_PUSHER_ERROR_RECONNECTION_FAILED = 105
        const val PLAY_PUSHER_ERROR_SYSTEM_ERROR = 106
    }
}