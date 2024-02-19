package com.tokopedia.product.detail.view.widget

import com.tokopedia.remoteconfig.RemoteConfig
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private inline fun <T> RemoteConfig.delegate(
    defaultValue: T,
    key: String? = null,
    crossinline getter: RemoteConfig.(String, T) -> T
): ReadOnlyProperty<Any, T> =
    object : ReadWriteProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T =
            getter(key ?: property.name, defaultValue) ?: defaultValue

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
            // no op
        }
    }

fun RemoteConfig.string(
    key: String? = null,
    def: String = ""
): ReadOnlyProperty<Any, String?> =
    delegate(def, key, RemoteConfig::getString)

fun RemoteConfig.boolean(
    key: String? = null,
    def: Boolean = false
): ReadOnlyProperty<Any, Boolean> =
    delegate(defaultValue = def, key = key, RemoteConfig::getBoolean)

fun RemoteConfig.long(
    key: String? = null,
    def: Long = 0L
): ReadOnlyProperty<Any, Long> =
    delegate(defaultValue = def, key = key, RemoteConfig::getLong)
