package com.tokopedia.autocompletecomponent.utils

import androidx.annotation.RawRes
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.google.gson.Gson

inline fun <reified T> rawToObject(@RawRes id: Int): T {
    val jsonString = getInstrumentation()
            .context
            .resources
            .openRawResource(id)
            .bufferedReader().use { it.readText() }

    return Gson().fromJson(jsonString, T::class.java)
}