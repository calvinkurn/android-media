package com.tokopedia.device.info.model

import android.content.Context
import android.media.MediaDrm
import android.os.Build
import android.util.Base64
import com.google.gson.Gson
import com.tokopedia.config.GlobalConfig
import com.tokopedia.device.info.DeviceInfo
import java.util.UUID

data class AdditionalInfoModel(
    val time: Long,
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
        const val MOST_SIG_BITS = -0x121074568629b532L
        const val LEAST_SIG_BITS = -0x5c37d8232ae2de13L
        private const val ANDROID = "android"

        fun generate(context: Context): AdditionalInfoModel {
            val widevineMediaDrm = MediaDrm(UUID(MOST_SIG_BITS, LEAST_SIG_BITS))
            val wideVineId = widevineMediaDrm.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID)
            val wideVineIdBase64 = Base64.encodeToString(wideVineId, Base64.DEFAULT)

            return AdditionalInfoModel(
                time = System.currentTimeMillis(),
                brand = Build.BRAND,
                product = Build.PRODUCT,
                board = Build.BOARD,
                cpuAbi = Build.CPU_ABI,
                device = Build.DEVICE,
                versionName = GlobalConfig.VERSION_NAME,
                advertisingId = DeviceInfo.getAdsId(context),
                wideVineId = wideVineIdBase64
            )
        }

        fun generateJson(context: Context): String {
            return Gson().toJson(generate(context))
        }
    }
}
