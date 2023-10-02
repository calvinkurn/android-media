package com.tokopedia.unit.test

import java.lang.reflect.Field
import java.lang.reflect.Modifier

object TestUtils {
    @Throws(Exception::class)
    fun setFinalStatic(field: Field, newValue: Any?) {
        field.isAccessible = true
        val getDeclaredFields0 =
            Class::class.java.getDeclaredMethod(
                "getDeclaredFields0",
                Boolean::class.javaPrimitiveType
            )
        getDeclaredFields0.isAccessible = true
        val fields = getDeclaredFields0.invoke(Field::class.java, false) as Array<Field>
        var modifiersField: Field? = null
        for (each in fields) {
            if ("modifiers" == each.name) {
                modifiersField = each
                break
            }
        }
        modifiersField?.isAccessible = true
        modifiersField?.setInt(field, field.modifiers and Modifier.FINAL.inv())

        field.set(null, newValue)
    }
}
