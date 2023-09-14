package com.scp.auth

import android.content.Context
import com.scp.verification.core.data.network.header.AdditionalHeaders
import com.scp.verification.core.data.network.header.AkamaiHeaderData
import com.tokopedia.config.GlobalConfig
import com.tokopedia.devicefingerprint.header.FingerprintModelGenerator
import com.tokopedia.network.authentication.AuthHelper

class TkpdAdditionalHeaders(val context: Context): AdditionalHeaders {
    override fun getBotProtectionHeaders(): AkamaiHeaderData {
        return AkamaiHeaderData(
            "User-Agent",
            "userAgentValue",
            "sensorDataKey",
            "sensorDataValue"
        )
    }

    override fun getAdditionalHeaders(): HashMap<String, String>? {
        val fpHash = FingerprintModelGenerator.generateFingerprintModel(context)
        return hashMapOf(
            "Fingerprint-Data" to fpHash.fingerprintHash,
            "Fingerprint-Hash" to AuthHelper.Companion.getMD5Hash(fpHash.fingerprintHash + "+" + ""),
            "X-Device" to "android-" + GlobalConfig.VERSION_NAME,
            "X-Tkpd-Akamai" to "testing_akamai",
            "X-Tkpd-App-Name" to GlobalConfig.getPackageApplicationName(),
            "X-GA-ID" to fpHash.adsId,
        )
    }

}
