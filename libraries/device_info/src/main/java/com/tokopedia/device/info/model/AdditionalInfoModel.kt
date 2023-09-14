package com.tokopedia.device.info.model

import android.content.Context
import android.media.MediaDrm
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.util.Base64
import com.google.gson.Gson
import com.tokopedia.config.GlobalConfig
import com.tokopedia.device.info.DeviceInfo
import timber.log.Timber
import java.util.UUID

data class AdditionalInfoModel(
    val time: String,
    val brand: String,
    val product: String,
    val board: String,
    val cpuAbi: String,
    val device: String,
    val versionName: String,
    val advertisingId: String,
    val wideVineId: String
) {

    companion object {
        private const val UNKNOWN = "unknown"

        fun generate(context: Context): AdditionalInfoModel {
            var widevineMediaDrm: MediaDrm? = null

            val widevineId: String = try {
                // This UUID bits are for widevine DRM
                widevineMediaDrm = MediaDrm(UUID(-0x121074568629b532L, -0x5c37d8232ae2de13L))
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

            return AdditionalInfoModel(
                time = System.currentTimeMillis().toString(),
                brand = Build.BRAND,
                product = Build.PRODUCT,
                board = Build.BOARD,
                cpuAbi = Build.CPU_ABI,
                device = Build.DEVICE,
                versionName = GlobalConfig.VERSION_NAME,
                advertisingId = DeviceInfo.getAdsId(context),
                wideVineId = widevineId
            )
        }

        fun generateJson(context: Context): String {
            return Gson().toJson(generate(context))
        }
    }
}
