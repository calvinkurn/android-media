package com.tokopedia.product.addedit.util

import androidx.lifecycle.MutableLiveData
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

inline fun <reified T> T.callPrivateFunc(name: String, vararg args: Any?): Any? =
        T::class
                .declaredMemberFunctions
                .firstOrNull { it.name == name }
                ?.apply { isAccessible = true }
                ?.call(this, *args)

inline fun <reified T : Any, R> T.getPrivateProperty(name: String): R? =
        T::class
                .memberProperties
                .firstOrNull { it.name == name }
                ?.apply { isAccessible = true }
                ?.get(this) as? R

inline fun <reified T> T.setPrivateProperty(name: String, value: Any?) {
    T::class.java.getDeclaredField(name)
            .apply { isAccessible = true }
            .set(this, value)
}