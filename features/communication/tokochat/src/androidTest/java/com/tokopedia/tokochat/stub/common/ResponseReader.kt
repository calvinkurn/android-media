package com.tokopedia.tokochat.stub.common

import androidx.test.platform.app.InstrumentationRegistry
import java.io.InputStreamReader

object ResponseReader {

    fun convertJsonToStream(fileName: String): String {
        val inputStream = (InstrumentationRegistry.getInstrumentation().targetContext
            .applicationContext).assets.open(fileName)
        val builder = StringBuilder()
        val reader = InputStreamReader(inputStream, "UTF-8")
        reader.readLines().forEach {
            builder.append(it)
        }
        return builder.toString()
    }
}
