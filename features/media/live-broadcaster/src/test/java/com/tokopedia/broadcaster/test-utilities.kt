package com.tokopedia.broadcaster

import android.os.Build
import com.tokopedia.unit.test.TestUtils

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

fun mockArchBuild(isAboveLollipop: Boolean, value: Any) {
    val supportedAbis = "SUPPORTED_ABIS"
    val cpuAbi = "CPU_ABI"

    val field = if (isAboveLollipop) {
        Build::class.java.getField(supportedAbis)
    } else {
        Build::class.java.getField(cpuAbi)
    }

    TestUtils.setFinalStatic(field, value)
}

fun mockVersionCodeOf(version: String, value: Int) {
    val field = Build.VERSION_CODES::class.java.getField(version)
    TestUtils.setFinalStatic(field, value)
}

fun mockSdkInt(value: Int) {
    val field = Build.VERSION::class.java.getField("SDK_INT")
    TestUtils.setFinalStatic(field, value)
}
