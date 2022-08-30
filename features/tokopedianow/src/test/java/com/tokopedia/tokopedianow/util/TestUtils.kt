package com.tokopedia.tokopedianow.util

import com.tokopedia.tokopedianow.recipebookmark.util.UiState
import org.junit.Assert
import java.lang.reflect.Method

object TestUtils {

    inline fun <reified T>Any.getPrivateField(name: String): T {
        return this::class.java.getDeclaredField(name).let {
            it.isAccessible = true
            return@let it.get(this) as T
        }
    }

    inline fun <reified T>Any.getParentPrivateField(name: String): T {
        val superClass = this::class.java.superclass
        return superClass.getDeclaredField(name).let {
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

    fun<T: Any> UiState<T>?.verifySuccess(): UiState.Success<T> {
        Assert.assertTrue(this is UiState.Success)
        return this as UiState.Success<T>
    }

    fun<T: Any> UiState<T>?.verifyFail(): UiState.Fail<T> {
        Assert.assertTrue(this is UiState.Fail)
        return this as UiState.Fail<T>
    }

    fun<T: Any> UiState<T>?.verifyEquals(data: T?): UiState<T>? {
        Assert.assertEquals(this?.data, data)
        return this
    }

    fun<T: Any> UiState<T>?.verifyThrowable(throwable: Throwable): UiState<T>? {
        Assert.assertEquals(this?.throwable, throwable)
        return this
    }

    fun<T: Any> UiState<T>?.verifyErrorCode(errorCode: String): UiState<T>? {
        Assert.assertEquals(this?.errorCode, errorCode)
        return this
    }

}