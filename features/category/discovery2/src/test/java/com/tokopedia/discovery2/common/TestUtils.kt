package com.tokopedia.discovery2.common

import androidx.lifecycle.LiveData
import org.junit.Assert

object TestUtils {
    inline fun <reified T>Any.getPrivateField(name: String): T {
        return this::class.java.getDeclaredField(name).let {
            it.isAccessible = true
            return@let it.get(this) as T
        }
    }

    fun Any.mockPrivateField(name: String, value: Any?) {
        this::class.java.getDeclaredField(name)
            .also { it.isAccessible = true }
            .set(this, value)
    }

    fun<T: Any> LiveData<T>.verify(expected: Any) {
        Assert.assertEquals(expected, value)
    }
}
