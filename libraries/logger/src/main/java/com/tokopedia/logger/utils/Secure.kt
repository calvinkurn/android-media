package com.tokopedia.logger.utils

import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


fun generateKey(key: String): SecretKey {
    return SecretKeySpec(key.toByteArray(Charsets.UTF_8), Constants.ENCRYPTION_ALGORITHM);
}

fun encrypt(message: String, key: SecretKey): String {
    val cipher = Cipher.getInstance(Constants.ENCRYPTION_MODE)
    cipher.init(Cipher.ENCRYPT_MODE, key)
    val byteArray = cipher.doFinal(message.toByteArray(Charsets.UTF_8))
    return byteToHex(byteArray)
}

fun decrypt(message: String, key: SecretKey): String {
    val cipher = Cipher.getInstance(Constants.ENCRYPTION_MODE)
    cipher.init(Cipher.DECRYPT_MODE, key)
    return String(cipher.doFinal(decodeHex(message)), Charsets.UTF_8)
}

fun byteToHex(byteArray: ByteArray): String {
    var hexString = ""
    for (byte in byteArray) {
        hexString += String.format("%02X", byte)
    }
    return hexString
}

fun decodeHex(hexString: String): ByteArray {
    val bytes = ByteArray(hexString.length / 2)
    var i = 0
    while (i < hexString.length) {
        bytes[i / 2] = hexToByte(
            hexString.substring(
                i,
                i + 2
            )
        )
        i += 2
    }
    return bytes
}

fun hexToByte(hexString: String): Byte {
    val firstDigit = toDigit(hexString[0])
    val secondDigit =
        toDigit(hexString[1])
    return ((firstDigit shl 4) + secondDigit).toByte()
}

fun toDigit(hexChar: Char): Int {
    val digit = Character.digit(hexChar, 16)
    require(digit != -1) { "Invalid Hexadecimal Character: $hexChar" }
    return digit
}
