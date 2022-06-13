package com.tokopedia.attachproduct.utils

import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.tokopedia.test.application.util.InstrumentationMockHelper
import java.io.StringReader
import java.lang.reflect.Type

object FileUtils {
    fun <T> parseRaw(
        resId: Int,
        typeOfT: Type
    ): T {
        val stringFile = fileContent(resId)
        return fromJson(stringFile, typeOfT)
    }

    fun fileContent(resId: Int): String {
        val context = InstrumentationRegistry
            .getInstrumentation()
            .context
        return InstrumentationMockHelper.getRawString(
            context, resId
        )
    }

    fun <T> fromJson(json: String?, typeOfT: Type?): T {
        if (json == null) {
            throw NullPointerException()
        }
        val reader = StringReader(json)
        val model = Gson().fromJson<T>(reader, typeOfT)
        return model
    }

}