package com.tokopedia.mediauploader.common.util

import java.io.File
import java.io.FileInputStream

fun File.slice(
    partNumber: Int,
    chunkSize: Int,
): ByteArray? {
    if (partNumber < 0) return null
    if (chunkSize < 0) return null

    val inputStream = FileInputStream(this)
    val byteArray = ByteArray(chunkSize)

    val offset = (partNumber - 1) * chunkSize
    inputStream.skip(offset.toLong())

    inputStream.read(byteArray, 0, chunkSize)

    return byteArray
}

fun ByteArray.trimLastZero(): ByteArray {
    var length = this.size - 1

    for (i in length downTo 0) {
        if (this[i].toInt() == 0) {
            length--
        } else {
            break
        }
    }

    return this.copyOf(length + 1)
}
