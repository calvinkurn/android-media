package com.tokopedia.interceptors.refreshtoken

import android.util.Base64
import java.util.*

/**
 * Created by Yoris on 14/10/21.
 */

object TokenUtils {
    fun randomChar(length: Int): String {
        return if (length > 0) {
            UUID.randomUUID().toString().replace("-", "").substring(0, length)
        } else {
            ""
        }
    }

    fun encode(type: String): String {
        return if (type.isNotBlank()) {
            val secretId = randomChar(4)
            val asB64 = Base64.encodeToString(type.toByteArray(), Base64.NO_WRAP)
            "$asB64$secretId"
        } else {
            ""
        }

    }
}