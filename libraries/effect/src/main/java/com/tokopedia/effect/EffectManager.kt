package com.tokopedia.effect

/**
 * Created By : Jonathan Darwin on March 24, 2023
 */
interface EffectManager {

    fun init(
        surfaceWidth: Int,
        surfaceHeight: Int,
    )

    fun process(
        textureId: Int,
        textureWidth: Int,
        textureHeight: Int,
        width: Int,
        height: Int,
    ): Int

    fun drawFrameBase(
        textureWidth: Int,
        textureHeight: Int,
        surfaceWidth: Int,
        surfaceHeight: Int,
        dstTexture: Int,
    )

    fun setCameraPosition(isFront: Boolean)

    fun getExternalOESTextureID(): Int

    fun setPreset(presetId: String, value: Float)

    fun removePreset()
}
