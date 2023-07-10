package com.tokopedia.telemetry.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlin.math.roundToInt

const val TELEMETRY_REMOTE_CONFIG_KEY = "android_customer_telemetry_config"

data class TelemetryConfig(
    @SerializedName("rate")
    var rate: Float = 50.0F,
    @SerializedName("cnt")
    var count: Int = 10,
    @SerializedName("itv")
    var interval: Int = 300
) {
    val samplingInt = rate.trimToInt()

    private fun Float.trimToInt(): Int = if (this > 100F) {
        100
    } else if (this < 0F) {
        0
    } else {
        this.roundToInt()
    }

    companion object {
        @JvmStatic
        fun parseFromRemoteConfig(json: String): TelemetryConfig {
            return try {
                Gson().fromJson(json, TelemetryConfig::class.java)
            } catch (@Suppress("SwallowedException") e: Exception) {
                TelemetryConfig()
            }
        }

        fun Int.isInSamplingArea(): Boolean {
            return when (this) {
                100 -> {
                    true
                }
                0 -> {
                    false
                }
                else -> {
                    (0..100).random() < this
                }
            }
        }
    }

}