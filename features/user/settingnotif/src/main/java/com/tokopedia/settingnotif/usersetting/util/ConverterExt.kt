package com.tokopedia.settingnotif.usersetting.util

import com.google.gson.Gson

val gson by lazy(LazyThreadSafetyMode.NONE) {
    Gson()
}

inline fun <reified T> toJson(src: Any): String {
    return gson.toJson(src, T::class.java)
}

inline fun <reified T> fromJson(src: String): T {
    return gson.fromJson(src, T::class.java)
}

inline fun <reified T> dataClone(src: Any): T {
    val json = toJson<T>(src)
    return fromJson(json)
}