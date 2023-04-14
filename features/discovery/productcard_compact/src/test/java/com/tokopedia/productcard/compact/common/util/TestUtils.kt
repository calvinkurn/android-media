package com.tokopedia.productcard.compact.common.util

object TestUtils {
    fun Any.mockPrivateField(name: String, value: Any?) {
        this::class.java.getDeclaredField(name)
            .also { it.isAccessible = true }
            .set(this, value)
    }
}
