package com.tokopedia.effect

/**
 * Created By : Jonathan Darwin on March 24, 2023
 */
interface EffectManager {

    fun init()

    fun process(
        srcTexture: Int,
        cameraRotation: Int,
        width: Int,
        height: Int,
        surfaceWidth: Int,
        surfaceHeight: Int
    )

    fun setCameraPosition(isFront: Boolean)
}
