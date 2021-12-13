package com.tokopedia.product_ar.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ModifaceProvider(
        @SerializedName("gamma")
        @Expose
        val gamma: Double = 0.0,
        @SerializedName("sparkle_a")
        @Expose
        val sparkleA: Double = 0.0,
        @SerializedName("sparkle_r")
        @Expose
        val sparkleR: Double = 0.0,
        @SerializedName("sparkle_g")
        @Expose
        val sparkleG: Double = 0.0,
        @SerializedName("sparkle_b")
        @Expose
        val sparkleB: Double = 0.0,
        @SerializedName("color_a")
        @Expose
        val colorA: Double = 0.0,
        @SerializedName("color_r")
        @Expose
        val colorR: Double = 0.0,
        @SerializedName("color_g")
        @Expose
        val colorG: Double = 0.0,
        @SerializedName("color_b")
        @Expose
        val colorB: Double = 0.0,
        @SerializedName("placement")
        @Expose
        val placement: String = "",
        @SerializedName("sparkleDensity")
        @Expose
        val sparkleDensity: Double = 0.0,
        @SerializedName("sparkleSize")
        @Expose
        val sparkleSize: Double = 0.0,
        @SerializedName("sparkleColorVariation")
        @Expose
        val sparkleColorVariation: Double = 0.0,
        @SerializedName("sparkleSizeVariation")
        @Expose
        val sparkleSizeVariation: Double = 0.0,
        @SerializedName("sparkleBaseReflectivity")
        @Expose
        val sparkleBaseReflectivity: Double = 0.0,
        @SerializedName("metallicIntensity")
        @Expose
        val metallicIntensity: Double = 0.0,
        @SerializedName("vinylIntensity")
        @Expose
        val vinylIntensity: Double = 0.0,
        @SerializedName("finish")
        @Expose
        val finish: String = "",
        @SerializedName("sub_category")
        @Expose
        val sub_category: String = ""
)
