package com.tokopedia.kotlin.extensions

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by Ade Fulki on 02/06/20.
 */

private inline fun <T> SharedPreferences.delegate(
        defaultValue: T, key: String? = null,
        crossinline getter: SharedPreferences.(String, T) -> T,
        crossinline setter: SharedPreferences.Editor.(String, T) -> SharedPreferences.Editor
): ReadWriteProperty<Any, T> =
        object : ReadWriteProperty<Any, T> {
            override fun getValue(thisRef: Any, property: KProperty<*>): T =
                    getter(key ?: property.name, defaultValue) ?: defaultValue

            override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
                value?.let { edit().setter(key ?: property.name, it).apply() }
            }
        }

fun SharedPreferences.int(def: Int = 0, key: String? = null): ReadWriteProperty<Any, Int> =
        delegate(def, key, SharedPreferences::getInt, SharedPreferences.Editor::putInt)

fun SharedPreferences.long(def: Long = 0, key: String? = null): ReadWriteProperty<Any, Long> =
        delegate(def, key, SharedPreferences::getLong, SharedPreferences.Editor::putLong)

fun SharedPreferences.float(
        def: Float = 0f,
        key: String? = null
): ReadWriteProperty<Any, Float> =
        delegate(def, key, SharedPreferences::getFloat, SharedPreferences.Editor::putFloat)

fun SharedPreferences.boolean(
        def: Boolean = false,
        key: String? = null
): ReadWriteProperty<Any, Boolean> =
        delegate(def, key, SharedPreferences::getBoolean, SharedPreferences.Editor::putBoolean)

fun SharedPreferences.stringSet(
        def: Set<String> = emptySet(),
        key: String? = null
): ReadWriteProperty<Any, Set<String>?> =
        delegate(def, key, SharedPreferences::getStringSet, SharedPreferences.Editor::putStringSet)

fun SharedPreferences.string(
        def: String = "",
        key: String? = null
): ReadWriteProperty<Any, String?> =
        delegate(def, key, SharedPreferences::getString, SharedPreferences.Editor::putString)

fun SharedPreferences.remove(
        key: String? = null
) {
    edit().remove(key).apply()
}

fun SharedPreferences.clear(
) {
    edit().clear().apply()
}

fun Editor.backgroundCommit() {
    try {
        CoroutineScope(Dispatchers.IO).launch {
            commit()
        }
    } catch (e: Exception) {
        apply()
        ServerLogger.log(
            Priority.P1,
            "DEV_CRASH",
            mapOf("method" to "launch_commit", "exception" to e.toString())
        )
    }
}
