package com.tokopedia.sessioncommon.util

import android.util.Base64
import java.util.*

/**
 * @author by nisie on 07/05/19.
 */
class TokenGenerator {

    companion object {
        val TOKEN_TYPE = "Basic"
    }

    fun createBasicToken(): String {
        val clientID = "7ea919182ff"
        val clientSecret = "b36cbf904d14bbf90e7f25431595a364"
        val encodeString = "$clientID:$clientSecret"

        val asB64 = Base64.encodeToString(encodeString.toByteArray(), Base64.NO_WRAP)
        return asB64
    }

    fun createBasicTokenGQL(): String {
        val clientID = "7ea919182ff"
        val asB64 = Base64.encodeToString(clientID.toByteArray(), Base64.NO_WRAP)
        val secretId = randomChar(4)
        return "$asB64$secretId"
    }

    fun encode(type: String): String {
        return if (type.isNotBlank()) {
            val secretId = randomChar(4)
            System.out.println("type : $type")
            val asB64 = Base64.encodeToString(type.toByteArray(), Base64.NO_WRAP)
            System.out.println("asB64 : $asB64")

            "$asB64$secretId"
        } else {
            ""
        }

    }

    fun randomChar(length: Int): String {
        return if (length > 0) {
            UUID.randomUUID().toString().replace("-", "").substring(0, length)
        } else {
            ""
        }
    }
}