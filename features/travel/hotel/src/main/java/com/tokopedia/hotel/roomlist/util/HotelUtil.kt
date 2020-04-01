package com.tokopedia.hotel.roomlist.util

import java.security.NoSuchAlgorithmException
import kotlin.experimental.and

/**
 * @author by jessica on 08/05/19
 */

class HotelUtil{
    companion object {
        fun md5(s: String): String {
            try {
                val digest = java.security.MessageDigest.getInstance("MD5")
                digest.update(s.toByteArray())
                val messageDigest = digest.digest()
                val hexString = StringBuilder()
                for (b in messageDigest) {
                    hexString.append(String.format("%02x", b and 0xff.toByte()))
                }
                return hexString.toString()
            } catch (e: NoSuchAlgorithmException) {
                return ""
            }

        }
    }
}