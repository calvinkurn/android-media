package com.tokopedia.sessioncommon.util

import android.content.Context
import com.tkpd.util.Base64
import com.tokopedia.encryption.security.sha256

object AuthenticityUtils {
    fun normalizePhoneNumber(phoneNum: String): String {
        return try {
            if (phoneNum.isNotEmpty()) {
                phoneNum.replace("[^0-9]".toRegex(), "").replaceFirst("^0(?!$)".toRegex(), "62")
            } else ""
        }catch (e: Exception) {
            ""
        }
    }

    fun generateAuthenticity(msisdn: String, timeUnix: String, context: Context): String {
        val normalizedMsisdn = normalizePhoneNumber(msisdn)
        val encoder = Base64.GetDecoder(context).trim()
        val content = (normalizedMsisdn + timeUnix + encoder)
        return content.sha256()
    }
}