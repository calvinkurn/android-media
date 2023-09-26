package com.tokopedia.mediauploader.common.util

import java.io.File
import java.io.FileInputStream

private var tempByteArray: Array<ByteArray?> = arrayOf()

fun File.slice(
    partNumber: Int,
    chunkSize: Int,
    reuseSlot: Int?,
    slotSize: Int
): Pair<Int, ByteArray?>? {
    synchronized(this){
        if (partNumber < 0) return null
        if (chunkSize < 0) return null
        if (tempByteArray.isEmpty()) {
            tempByteArray = Array(slotSize) { null }
        }

        val inputStream = FileInputStream(this)
        val offset = (partNumber - 1) * chunkSize
        inputStream.skip(offset.toLong())

        val tempIndex = if (reuseSlot != null && tempByteArray[reuseSlot] != null) {
            inputStream.read(tempByteArray[reuseSlot], 0, chunkSize)
            reuseSlot
        } else {
            val slotIndex = partNumber - 1
            tempByteArray[slotIndex] = ByteArray(chunkSize)
            inputStream.read(tempByteArray[slotIndex], 0, chunkSize)
            slotIndex
        }

        inputStream.close()

        return Pair(tempIndex, tempByteArray[tempIndex])
    }
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
