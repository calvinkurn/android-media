package com.tokopedia.home.benchmark.network_request

import android.content.Context
import com.tokopedia.home.test.R
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

object HomeMockResponseList {
    fun getDynamicHomeChannel(context: Context): String {
        return getRawString(context, R.raw.home_data_dynamic_home_channel_benchmark)
    }

    private fun getRawString(context: Context, res: Int): String {
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