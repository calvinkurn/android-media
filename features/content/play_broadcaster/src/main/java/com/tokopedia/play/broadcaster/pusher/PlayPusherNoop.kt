package com.tokopedia.play.broadcaster.pusher

import android.content.Context
import android.os.Build
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.annotation.RequiresApi
import com.alivc.live.pusher.*
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.config.GlobalConfig


/**
 * Created by mzennis on 24/05/20.
 */
class PlayPusherNoop(private val builder: Builder) {

    var playPusherCountDownTimer: PlayPusherCountDownTimerNoop? = null

    private var ingestUrl: String = ""

    fun create() {
    }

    fun startPreview(surfaceView: SurfaceView) {
    }

    fun stopPreview() {
    }

    fun startPush(ingestUrl: String = "") {
    }

    fun stopPush() {
    }

    fun switchCamera() {
    }

    fun resume() {
    }

    fun pause() {
    }

    fun destroy() {
    }

    private fun setQualityMode() {
    }

    open class Builder(@ApplicationContext var context: Context) {

        fun build() = PlayPusherNoop(this)
    }
}