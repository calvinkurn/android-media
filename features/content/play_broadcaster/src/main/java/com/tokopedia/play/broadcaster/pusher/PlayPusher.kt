package com.tokopedia.play.broadcaster.pusher

import android.os.Build
import android.view.SurfaceView
import androidx.annotation.RequiresApi


/**
 * Created by mzennis on 24/05/20.
 */
interface PlayPusher {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun create()

    fun addCountDownTimer(countDownTimer: PlayPusherCountDownTimer)

    fun startPreview(surfaceView: SurfaceView)

    fun stopPreview()

    fun startPush(ingestUrl: String)

    fun stopPush()

    fun switchCamera()

    fun resume()

    fun pause()

    fun destroy()
}