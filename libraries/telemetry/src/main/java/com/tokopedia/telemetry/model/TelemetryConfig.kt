package com.tokopedia.telemetry.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlin.math.roundToInt

const val DEFAULT_TELEMETRY_CONFIG =
    "{\"touch_rate\":100,\"touch_cnt\":3, \"touch_itv\":3600," +
            "\"type_rate\":100,\"type_cnt\":3, \"type_itv\":3600," +
            "\"gyro_rate\":100,\"gyro_cnt\":3, \"gyro_itv\":3600," +
            "\"accel_rate\":100,\"accel_cnt\":3, \"accel_itv\":3600,}"
const val TELEMETRY_REMOTE_CONFIG_KEY = "android_customer_telemetry_config"

data class TelemetryConfig(
    /**
     * sampling percentage, 0 to 100%
     */
    @SerializedName("touch_rate")
    var touchSampling: Float = 100.0F,
    @SerializedName("type_rate")
    var typeSampling: Float = 100.0F,
    @SerializedName("gyro_rate")
    var gyroSampling: Float = 100.0F,
    @SerializedName("accel_rate")
    var accelSampling: Float = 100.0F,

    /*
     * count 3 and interval 3600 means
     * every 3600 seconds will collect at least first 3, ignore sampling
     * interval <20 will disable the logic.
     */
    @SerializedName("touch_cnt")
    var touchCount: Int = 3,
    @SerializedName("type_cnt")
    var typeCount: Int = 3,
    @SerializedName("gyro_cnt")
    var gyroCount: Int = 3,
    @SerializedName("accel_cnt")
    var accelCount: Int = 3,

    @SerializedName("touch_itv")
    var touchInterval: Long = 3600L,
    @SerializedName("type_itv")
    var typeInterval: Long = 3600L,
    @SerializedName("gyro_itv")
    var gyroInterval: Long = 3600L,
    @SerializedName("accel_itv")
    var accelInterval: Long = 3600L
) {
    val touchSamplingInt = touchSampling.trimToInt()
    val typeSamplingInt = typeSampling.trimToInt()
    val gyroSamplingInt = gyroSampling.trimToInt()
    val accelSamplingInt = accelSampling.trimToInt()

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
            } catch (e: Exception) {
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