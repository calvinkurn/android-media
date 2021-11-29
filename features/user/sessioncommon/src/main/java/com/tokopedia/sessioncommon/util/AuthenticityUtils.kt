package com.tokopedia.sessioncommon.util

import android.content.Context
import com.tkpd.util.Base64
import com.tokopedia.encryption.security.sha256

object AuthenticityUtils {
    fun generateAuthenticity(msisdn: String, timeUnix: String, context: Context): String {
        val encoder = Base64.GetDecoder(context).trim()
        val content = (msisdn + timeUnix + encoder)
        return content.sha256()
    }
}