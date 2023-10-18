package com.tokopedia.content.common.util

/**
 * Created by kenny.hadisaputra on 06/07/23
 */
private const val ONE_THOUSAND_BYTES = 1_024

@JvmInline
value class Bytes(val length: Long) {
    companion object
}

fun Bytes.Companion.fromKiloBytes(length: Long): Bytes {
    return Bytes(length * ONE_THOUSAND_BYTES)
}

fun Bytes.Companion.fromMegaBytes(length: Long): Bytes {
    return Bytes(length * ONE_THOUSAND_BYTES * ONE_THOUSAND_BYTES)
}
