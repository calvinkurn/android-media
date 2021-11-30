package com.tokopedia.broadcaster

import android.os.Build
import java.lang.reflect.Field
import java.lang.reflect.Modifier

data class MockProperty(var name: String, var value: Any) {
    fun afterSet(`do`: (Any) -> Unit = {}) {
        `do`(value)
    }
}

infix fun Any.setPrivateProperty(property: MockProperty): Any {
    javaClass.getDeclaredField(property.name)
        .also { it.isAccessible = true }
        .set(this, property.value)

    property.afterSet()

    return this
}

inline fun <reified T> Any.getPrivateProperty(propertyName: String): T? {
    return javaClass.getDeclaredField(propertyName)
        .also { it.isAccessible = true }
        .get(this) as T
}

fun mockStaticValue(field: Field, value: Any) {
    field.isAccessible = true

    Field::class.java.getDeclaredField("modifiers").also {
        it.isAccessible = true
        it.set(field, field.modifiers and Modifier.FINAL.inv())
    }

    field.set(null, value)
}

fun mockArchBuild(isAboveLollipop: Boolean, value: Any) {
    val supportedAbis = "SUPPORTED_ABIS"
    val cpuAbi = "CPU_ABI"

    val field = if (isAboveLollipop) {
        Build::class.java.getField(supportedAbis)
    } else {
        Build::class.java.getField(cpuAbi)
    }

    mockStaticValue(field, value)
}

fun mockVersionCodeOf(version: String, value: Int) {
    val field = Build.VERSION_CODES::class.java.getField(version)
    mockStaticValue(field, value)
}

fun mockSdkInt(value: Int) {
    val field = Build.VERSION::class.java.getField("SDK_INT")
    mockStaticValue(field, value)
}