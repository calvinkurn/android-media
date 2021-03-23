package com.tokopedia.encryption.utils

object Utils {

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

    fun decodeDecimalToText(keys: IntArray): String {
        return keys.joinToString(separator = "") { it.toChar().toString() }
    }

    private fun hexToByte(hexString: String): Byte {
        val firstDigit = toDigit(hexString[0])
        val secondDigit =
                toDigit(hexString[1])
        return ((firstDigit shl 4) + secondDigit).toByte()
    }

    private fun toDigit(hexChar: Char): Int {
        val digit = Character.digit(hexChar, 16)
        require(digit != -1) { "Invalid Hexadecimal Character: $hexChar" }
        return digit
    }

}

