package com.tokopedia.macrobenchmark_util.env.mock

import android.content.Context
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

object InstrumentationMockHelper {
    fun getRawString(context: Context, res: Int): String {
        val rawResource: InputStream = context.resources.openRawResource(res)
        val content = streamToString(rawResource)
        try {
            rawResource.close()
        } catch (e: IOException) {
        }
        return content
    }

    fun streamToString(`in`: InputStream?): String {
        var temp: String?
        val bufferedReader = BufferedReader(InputStreamReader(`in`))
        val stringBuilder = StringBuilder()
        try {
            while (bufferedReader.readLine().also { temp = it } != null) {
                stringBuilder.append(temp + "\n")
            }
        } catch (e: IOException) {
        }
        return stringBuilder.toString()
    }
}