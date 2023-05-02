package com.tokopedia.search.utils

import androidx.annotation.RawRes
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson

inline fun <reified T> rawToObject(@RawRes id: Int): T {
    val jsonString = InstrumentationRegistry.getInstrumentation()
        .context
        .resources
        .openRawResource(id)
        .bufferedReader().use { it.readText() }

    return Gson().fromJson(jsonString, T::class.java)
}
