package com.tokopedia.tokopedianow.util

import java.lang.reflect.Method

object TestUtils {

    inline fun <reified T>Any.getPrivateField(name: String): T {
        return this::class.java.getDeclaredField(name).let {
            it.isAccessible = true
            return@let it.get(this) as T
        }
    }

    fun Any.getPrivateMethod(name: String, vararg params: Class<*>?): Method {
        return if(params.isNullOrEmpty()) {
            this::class.java.getDeclaredMethod(name)
        } else {
            this::class.java.getDeclaredMethod(name, *params)
        }.apply {
            isAccessible = true
        }
    }

    fun Any.mockPrivateField(name: String, value: Any?) {
        this::class.java.getDeclaredField(name)
            .also { it.isAccessible = true }
            .set(this, value)
    }

    fun Any.mockSuperClassField(name: String, value: Any?) {
        this::class.java.superclass.getDeclaredField(name)
            .also { it.isAccessible = true }
            .set(this, value)
    }
}