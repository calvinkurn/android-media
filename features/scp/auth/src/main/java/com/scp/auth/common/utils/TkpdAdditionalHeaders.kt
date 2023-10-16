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
            "Fingerprint-Data" to fpHash.fingerprintHash,
            "Fingerprint-Hash" to AuthHelper.Companion.getMD5Hash(fpHash.fingerprintHash + "+" + ""),
            "X-Device" to "android-" + GlobalConfig.VERSION_NAME,
            "X-Tkpd-App-Name" to GlobalConfig.getPackageApplicationName(),
            "X-Tkpd-Akamai" to "login",
            "X-GA-ID" to fpHash.adsId
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
    }
}
