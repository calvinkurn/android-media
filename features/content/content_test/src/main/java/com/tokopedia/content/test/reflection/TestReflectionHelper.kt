package com.tokopedia.content.test.reflection

/**
 * Created By : Jonathan Darwin on October 05, 2022
 */
fun <T> Any.getPrivateField(name: String): T {
    val field = this.javaClass.getDeclaredField(name)
    field.isAccessible = true
    return field.get(this) as T
}