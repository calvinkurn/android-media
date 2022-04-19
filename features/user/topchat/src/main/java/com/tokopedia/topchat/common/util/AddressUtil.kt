package com.tokopedia.topchat.common.util

import kotlin.math.ceil

object AddressUtil {

    @Suppress("MagicNumber")
    fun getAddressMasking(address: String): String {
        val addressLabel = address.trim()
        val addresses = addressLabel.split(" ").toMutableList()
        for (i in addresses.indices) {
            val word = addresses[i]
            val wordCount = word.length
            val removedPercentage = 0.3
            val totalRemovedWord = ceil((wordCount * removedPercentage)).toInt()
            val endIndex = wordCount - totalRemovedWord
            addresses[i] = "${word.substring(0, endIndex)}${"*".repeat(totalRemovedWord)}"
        }
        return addresses.joinToString(" ")
    }

}