package com.tokopedia.keys

object Keys {
    @JvmField val SCALYR_TOKEN_CUSTOMERAPP = encodeKey(SCALYR_P1)
    @JvmField val SCALYR_TOKEN_SELLERAPP = encodeKey(SCALYR_P1_SA)

    private fun encodeKey(keys: IntArray): String {
        return keys.joinToString(separator = "") { it.toChar().toString() }
    }
}