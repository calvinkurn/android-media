package com.tokopedia.productcard


object ProductConstraintLayoutUtilTest {

    inline fun <reified T> Any.getPrivateField(name: String): T {
        return this::class.java.getDeclaredField(name).let {
            it.isAccessible = true
            return@let it.get(this) as T
        }
    }

    inline fun <reified T> T.mockPrivateMethod(name: String, value: Any?, classType: Class<*>) {
        T::class.java.getDeclaredMethod(name, classType)
            .also { it.isAccessible = true }
            .invoke(this, value)
    }
}
