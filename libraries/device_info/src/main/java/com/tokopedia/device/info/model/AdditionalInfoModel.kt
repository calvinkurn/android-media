package com.tokopedia.device.info.model

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaDrm
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.util.Base64
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.config.GlobalConfig
import com.tokopedia.device.info.DeviceInfo
import timber.log.Timber
import java.util.UUID

data class AdditionalInfoModel(
    @SerializedName("time")
    @Expose
    val time: String,

    @SerializedName("brand")
    @Expose
    val brand: String,

    @SerializedName("product")
    @Expose
    val product: String,

    @SerializedName("board")
    @Expose
    val board: String,

    @SerializedName("cpuAbi")
    @Expose
    val cpuAbi: String,

    @SerializedName("device")
    @Expose
    val device: String,

    @SerializedName("versionName")
    @Expose
    val versionName: String,

    @SerializedName("advertisingId")
    @Expose
    val advertisingId: String,

    @SerializedName("wideVineId")
    @Expose
    val wideVineId: String
) {

    companion object {
        private const val UNKNOWN = "unknown"
        private const val MOST_SIG_BITS = -0x121074568629b532L
        private const val LEAST_SIG_BITS = -0x5c37d8232ae2de13L
        private const val ANDROID = "android"

        fun generate(context: Context): AdditionalInfoModel {

            return AdditionalInfoModel(
                time = System.currentTimeMillis().toString(),
                brand = Build.BRAND,
                product = Build.PRODUCT,
                board = Build.BOARD,
                cpuAbi = Build.CPU_ABI,
                device = Build.DEVICE,
                versionName = GlobalConfig.VERSION_NAME,
                advertisingId = DeviceInfo.getAdsId(context),
                wideVineId = getWidevineId()
            )
        }

        fun generateJson(context: Context): String {
            return Gson().toJson(generate(context))
        }

        @SuppressLint("DeprecatedMethod")
        private fun getWidevineId(): String {
            var widevineMediaDrm: MediaDrm? = null

            return try {
                widevineMediaDrm = MediaDrm(UUID(MOST_SIG_BITS, LEAST_SIG_BITS))
                val wideVineId =
                    widevineMediaDrm.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID)
                Base64.encodeToString(wideVineId, Base64.DEFAULT).trim()
            } catch (e: Exception) {
                Timber.e(e)
                UNKNOWN
            } finally {
                widevineMediaDrm?.let {
                    when {
                        SDK_INT >= 28 -> {
                            it.close()
                        }

                        SDK_INT in 18..27 -> {
                            it.release()
                        }

                        else -> {
                            // do nothing
                            // this is to stop linter's warn
                        }
                    }
                }
            }
        }
    }
}
