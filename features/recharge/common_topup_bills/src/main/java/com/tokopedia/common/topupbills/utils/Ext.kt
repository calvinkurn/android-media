package com.tokopedia.common.topupbills.utils

import android.content.res.Resources
import android.util.TypedValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and

/**
 * Created by nabillasabbaha on 07/05/19.
 */

fun String.generateRechargeCheckoutToken(): String {
    val timeMillis = System.currentTimeMillis().toString()
    val token = md5(timeMillis)
    return this + "_" + if (token.isEmpty()) timeMillis else token
}

private fun md5(s: String): String {
    try {
        val digest = MessageDigest.getInstance("MD5")
        digest.update(s.toByteArray())
        val messageDigest = digest.digest()
        val hexString = StringBuilder()
        for (b in messageDigest) {
            hexString.append(String.format("%02x", b and 0xff.toByte()))
        }
        return hexString.toString()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
        return ""
    }

}