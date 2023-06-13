package com.tokopedia.sessioncommon.util

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

    fun generateAuthenticity(msisdn: String, timeUnix: String): String {
        val normalizedMsisdn = normalizePhoneNumber(msisdn)
        val content = (normalizedMsisdn + timeUnix)
        return content.sha256()
    }
}
