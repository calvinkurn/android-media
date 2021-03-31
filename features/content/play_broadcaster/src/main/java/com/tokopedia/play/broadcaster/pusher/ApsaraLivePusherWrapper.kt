package com.tokopedia.play.broadcaster.pusher

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.view.SurfaceView
import androidx.core.app.ActivityCompat
import com.alivc.live.pusher.*
import com.tokopedia.play.broadcaster.pusher.config.ApsaraLivePusherConfig
import com.tokopedia.play.broadcaster.pusher.config.DefaultApsaraLivePusherConfig
import com.tokopedia.play.broadcaster.pusher.error.ApsaraFatalException
import com.tokopedia.play.broadcaster.pusher.listener.ApsaraLivePushInfoListenerImpl
import com.tokopedia.play.broadcaster.pusher.listener.ApsaraLivePusherErrorListenerImpl
import com.tokopedia.play.broadcaster.pusher.listener.ApsaraLivePusherNetworkListenerImpl
import com.tokopedia.play.broadcaster.pusher.state.ApsaraLivePusherState
import com.tokopedia.play.broadcaster.pusher.state.ApsaraLivePusherStateProcessor
import java.util.concurrent.ConcurrentLinkedQueue


/**
 * Created by mzennis on 15/03/21.
 */
class ApsaraLivePusherWrapper private constructor(
        val context: Context,
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
            return ApsaraLivePusherWrapper(
                    context = mContext,
                    pusherConfig = mApsaraLivePushConfig ?: DefaultApsaraLivePusherConfig(mContext)
            )
        }

    }

    val pusherState: ApsaraLivePusherState
        get() = apsaraLivePusherState

    private var aliVcLivePusher: AlivcLivePusher? = null
    private var apsaraLivePusherState: ApsaraLivePusherState = ApsaraLivePusherState.Idle

    private val livePusherStateProcessor = object : ApsaraLivePusherStateProcessor {
        override fun onStateChanged(state: ApsaraLivePusherState) {
            broadcastStateToListeners(state)
        }

        override fun onError(code: Int, throwable: Throwable) {
            broadcastErrorToListeners(code, throwable)
        }
    }

    private val aliVcLivePushInfoListener: AlivcLivePushInfoListener = ApsaraLivePushInfoListenerImpl(livePusherStateProcessor)
    private val alivcLivePushNetworkListener: AlivcLivePushNetworkListener = ApsaraLivePusherNetworkListenerImpl(livePusherStateProcessor)
    private val alivcLivePushErrorListener: AlivcLivePushErrorListener = ApsaraLivePusherErrorListenerImpl(livePusherStateProcessor)

    private var listeners: ConcurrentLinkedQueue<Listener> = ConcurrentLinkedQueue()

    private var ingestUrl: String = ""

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

    fun addListener(listener: Listener) {
        this.listeners.add(listener)
    }

    fun removeListener(listener: Listener) {
        this.listeners.remove(listener)
    }

    fun startPreview(surfaceView: SurfaceView) {
        if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_DENIED) {
            broadcastErrorToListeners(
                    PLAY_PUSHER_ERROR_SYSTEM_ERROR,
                    IllegalStateException("We need camera permission to continue live streaming"))
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
        try {
            aliVcLivePusher?.stopPreview()
        } catch (exception: IllegalStateException) {
        }
    }

    fun start(ingestUrl: String)  {
        if (ingestUrl.isNotEmpty()) {
            this.ingestUrl = ingestUrl
        }
        if (ingestUrl.isEmpty() || ingestUrl.isBlank()) {
            broadcastErrorToListeners(PLAY_PUSHER_ERROR_SYSTEM_ERROR, IllegalArgumentException("ingestUrl must not be empty"))
            return
        }

        try {
            broadcastStateToListeners(ApsaraLivePusherState.Connecting)
            aliVcLivePusher?.startPushAysnc(ingestUrl)
        } catch (exception: IllegalStateException) {
            if (exception.localizedMessage.orEmpty().contains("start push", true)){
                broadcastErrorToListeners(
                        PLAY_PUSHER_ERROR_SYSTEM_ERROR,
                        ApsaraFatalException("Something went wrong, please restart application"))
            }
        }
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
        } catch (exception: IllegalStateException) {
            // ignored because sometimes it's an unnecessary error, ex. status error current state init
        } catch (exception: Exception) {
             broadcastErrorToListeners(PLAY_PUSHER_ERROR_SYSTEM_ERROR, exception)
        } catch (error: Error) {
            broadcastErrorToListeners(PLAY_PUSHER_ERROR_SYSTEM_ERROR, error)
        }
    }

    private fun broadcastStateToListeners(state: ApsaraLivePusherState) {
        if (state == ApsaraLivePusherState.Pause && aliVcLivePusher?.isPushing == false) return
        apsaraLivePusherState = state
        listeners.forEach { it.onStateChanged(state) }
    }

    private fun broadcastErrorToListeners(code: Int, throwable: Throwable) {
        listeners.forEach { it.onError(code, throwable) }
    }

    interface Listener {

        fun onStateChanged(state: ApsaraLivePusherState)
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