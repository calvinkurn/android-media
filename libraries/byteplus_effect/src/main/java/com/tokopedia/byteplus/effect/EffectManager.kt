package com.tokopedia.byteplus.effect

import android.graphics.SurfaceTexture
import android.os.Handler

/**
 * Created By : Jonathan Darwin on March 24, 2023
 */
interface EffectManager {

    fun startRenderThread()

    fun stopRenderThread()

    fun init()

    fun setupTexture(
        surfaceWidth: Int,
        surfaceHeight: Int,
    )

    fun setRenderListener(
        surfaceWidth: Int,
        surfaceHeight: Int,
        listener: Listener,
    )

    fun drawFrameBase(
        textureWidth: Int,
        textureHeight: Int,
        surfaceWidth: Int,
        surfaceHeight: Int,
        dstTexture: Int,
    )

    fun setCameraPosition(isFront: Boolean)

    fun setFaceFilter(faceFilterId: String, value: Float): Boolean

    fun removeFaceFilter()

    fun setPreset(presetId: String, value: Float): Boolean

    fun removePreset()

    fun release()

    fun getSurfaceTexture(): SurfaceTexture?

    fun getHandler(): Handler?

    interface Listener {
        fun onRenderFrame(
            destinationTexture: Int,
            textureWidth: Int,
            textureHeight: Int,
        )
    }
}
