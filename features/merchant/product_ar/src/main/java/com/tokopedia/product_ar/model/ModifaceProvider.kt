package com.tokopedia.product_ar.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ModifaceProvider(
        @SerializedName("gamma")
        @Expose
        val gamma: Float = 0.0F,
        @SerializedName("glossDetail")
        @Expose
        val glossDetail: Float = 0.0F,
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
        @SerializedName("placement")
        @Expose
        val placement: String = "",
        @SerializedName("sparkleDensity")
        @Expose
        val sparkleDensity: Float = 0.0F,
        @SerializedName("sparkleSize")
        @Expose
        val sparkleSize: Float = 0.0F,
        @SerializedName("sparkleColorVariation")
        @Expose
        val sparkleColorVariation: Float = 0.0F,
        @SerializedName("sparkleSizeVariation")
        @Expose
        val sparkleSizeVariation: Float = 0.0F,
        @SerializedName("sparkleBaseReflectivity")
        @Expose
        val sparkleBaseReflectivity: Float = 0.0F,
        @SerializedName("metallicIntensity")
        @Expose
        val metallicIntensity: Float = 0.0F,
        @SerializedName("vinylIntensity")
        @Expose
        val vinylIntensity: Float = 0.0F,
        @SerializedName("finish")
        @Expose
        val finish: String = "",
        @SerializedName("sub_category")
        @Expose
        val sub_category: String = ""
)
