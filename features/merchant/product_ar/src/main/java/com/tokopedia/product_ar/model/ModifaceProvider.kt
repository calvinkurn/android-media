package com.tokopedia.product_ar.model

import android.graphics.Color
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.toIntSafely

/**
 * https://sdk.modiface.com/retailers/Android/mfemakeupkit/detailed_docs/2021-11-02_20:54:43/getmakeupdata-api-documentation-v8.md
 */

data class ModifaceProvider(
        @SerializedName("color_a")
        @Expose
        val colorA: Float = 0.0F,
        @SerializedName("color_r")
        @Expose
        val colorR: Float = 0.0F,
        @SerializedName("color_g")
        @Expose
        val colorG: Float = 0.0F,
        @SerializedName("color_b")
        @Expose
        val colorB: Float = 0.0F,
        @SerializedName("gamma")
        @Expose
        val gamma: Float = 0.0F,
        @SerializedName("glossDetail")
        @Expose
        val glossDetail: Float = 0.0F,
        @SerializedName("wetness")
        @Expose
        val wetness: Float = 0.0F,
        @SerializedName("sparkle_a")
        @Expose
        val sparkleA: Float = 0.0F,
        @SerializedName("sparkle_r")
        @Expose
        val sparkleR: Float = 0.0F,
        @SerializedName("sparkle_g")
        @Expose
        val sparkleG: Float = 0.0F,
        @SerializedName("sparkle_b")
        @Expose
        val sparkleB: Float = 0.0F,
        @SerializedName("lipPlumping")
        @Expose
        val lipPlumping: Float = 0.0F,
        @SerializedName("matteness")
        @Expose
        val matteness: Float = 0.0F,
        @SerializedName("sparkleDensity")
        @Expose
        val sparkleDensity: Float = 1F,
        @SerializedName("sparkleSize")
        @Expose
        val sparkleSize: Float = 1F,
        @SerializedName("sparkleColorVariation")
        @Expose
        val sparkleColorVariation: Float = 0.0F,
        @SerializedName("sparkleSizeVariation")
        @Expose
        val sparkleSizeVariation: Float = 0.0F,
        @SerializedName("sparkleBaseReflectivity")
        @Expose
        val sparkleBaseReflectivity: Float = 0.3F,
        @SerializedName("intensity")
        @Expose
        val intensity: Float = 1F,
        @SerializedName("envMappingIntensity")
        @Expose
        val envMappingIntensity: Float = 0.0F,
        @SerializedName("envMappingR")
        @Expose
        val envMappingR: Float = 0.0F,
        @SerializedName("envMappingG")
        @Expose
        val envMappingG: Float = 0.0F,
        @SerializedName("envMappingB")
        @Expose
        val envMappingB: Float = 0.0F,
        @SerializedName("envMappingBumpIntensity")
        @Expose
        val envMappingBumpIntensity: Float = 0.6F,
        @SerializedName("envMappingCurve")
        @Expose
        val envMappingCurve: Float = 2.3F,
        @SerializedName("envMappingRotationY")
        @Expose
        val envMappingRotationY: Float = 0.0F,
        @SerializedName("metallicIntensity")
        @Expose
        val metallicIntensity: Float = 0.0F,
        @SerializedName("vinylIntensity")
        @Expose
        val vinylIntensity: Float = 0.0F
) {
    fun getGlitterColor(): Int = Color.rgb(sparkleR.toIntSafely(),
            sparkleG.toIntSafely(),
            sparkleB.toIntSafely())

    fun getEnvMappingColor(): Int = Color.rgb(envMappingR.toIntSafely(),
            envMappingG.toIntSafely(),
            envMappingB.toIntSafely())
}
