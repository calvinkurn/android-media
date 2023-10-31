package com.scp.auth.common.utils

import android.content.Context
import com.akamai.botman.CYFMonitor
import com.scp.verification.core.data.network.header.AdditionalHeaders
import com.scp.verification.core.data.network.header.AkamaiHeaderData
import com.tokopedia.akamai_bot_lib.getAkamaiValue
import com.tokopedia.akamai_bot_lib.getExpiredTime
import com.tokopedia.akamai_bot_lib.setAkamaiValue
import com.tokopedia.akamai_bot_lib.setExpire
import com.tokopedia.akamai_bot_lib.setExpiredTime
import com.tokopedia.config.GlobalConfig
import com.tokopedia.devicefingerprint.header.FingerprintModelGenerator
import com.tokopedia.network.authentication.AuthHelper

class TkpdAdditionalHeaders(val context: Context) : AdditionalHeaders {

    override fun getBotProtectionHeaders(): AkamaiHeaderData {
        return AkamaiHeaderData(
            USER_AGENT,
            AuthHelper.getUserAgent(),
            AKAMAI_SENSOR_DATA_HEADER,
            getAkamaiValue()
        )
    }

    override fun getAdditionalHeaders(): HashMap<String, String> {
        val fpHash = FingerprintModelGenerator.generateFingerprintModel(context)
        return hashMapOf(
            FINGERPRINT_DATA_KEY to fpHash.fingerprintHash,
            FINGERPRINT_HASH_KEY to AuthHelper.Companion.getMD5Hash(fpHash.fingerprintHash + "+" + ""),
            DEVICE_KEY to DEVICE_PREFIX_VALUE + GlobalConfig.VERSION_NAME,
            X_TKPD_APP_NAME_KEY to GlobalConfig.getPackageApplicationName(),
            X_TKPD_AKAMAI_KEY to X_GA_ID_VALUE,
            X_GA_ID_KEY to fpHash.adsId
        )
    }
    private fun getAkamaiValue(): String {
        return setExpire(
            { System.currentTimeMillis() },
            { context.getExpiredTime() },
            { time -> context.setExpiredTime(time) },
            { context.setAkamaiValue(CYFMonitor.getSensorData()) },
            { context.getAkamaiValue() }
        )
    }

    companion object {
        private const val AKAMAI_SENSOR_DATA_HEADER = "X-acf-sensor-data"
        private const val USER_AGENT = "User-Agent"
        private const val FINGERPRINT_DATA_KEY = "Fingerprint-Data"
        private const val FINGERPRINT_HASH_KEY = "Fingerprint-Hash"
        private const val DEVICE_KEY = "X-Device"
        private const val X_TKPD_APP_NAME_KEY = "X-Tkpd-App-Name"
        private const val X_TKPD_AKAMAI_KEY = "X-Tkpd-Akamai"
        private const val X_GA_ID_KEY = "X-GA-ID"
        private const val X_GA_ID_VALUE = "login"
        private const val DEVICE_PREFIX_VALUE = "android-"
    }
}
