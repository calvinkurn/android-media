package com.tokopedia.test.application.util

import android.content.Context
import com.tokopedia.instrumentation.test.R
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

object MockResponseList {
    fun create(context: Context): HashMap<String, String> {
        val responseList = HashMap<String, String>()
        //official store
        responseList.put("slides", getRawString(context, R.raw.response_mock_data_official_store_banners))
        responseList.put("pdpGetLayout", getRawString(context, R.raw.response_mock_data_pdp_get_layout))
        return responseList
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