package com.tokopedia.topupbills.telco.common

import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import androidx.annotation.ColorRes
import com.tokopedia.common.topupbills.view.model.DigitalContactData
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and

/**
 * Created by nabillasabbaha on 07/05/19.
 */


fun Resources.getColorFromResources(context: Context, @ColorRes resId: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.getColor(resId, context.theme)
    } else {
        this.getColor(resId)
    }
}

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