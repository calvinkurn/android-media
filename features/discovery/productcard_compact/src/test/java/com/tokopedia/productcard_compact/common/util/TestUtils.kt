package com.tokopedia.productcard_compact.common.util

object TestUtils {
    fun Any.mockPrivateField(name: String, value: Any?) {
        this::class.java.getDeclaredField(name)
            .also { it.isAccessible = true }
            .set(this, value)
    }
}
