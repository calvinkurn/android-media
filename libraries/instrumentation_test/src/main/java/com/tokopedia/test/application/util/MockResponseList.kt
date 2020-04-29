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

        //home channel
        responseList["dynamicHomeChannel"] = getRawString(context, R.raw.response_mock_data_dynamic_home_channel)
        responseList["widget_tab"] = getRawString(context, R.raw.response_mock_data_home_widget_tab)
        responseList["widget_grid"] = getRawString(context, R.raw.response_mock_data_home_widget_grid)
        responseList["suggestedProductReview"] = getRawString(context, R.raw.response_mock_data_suggested_review)
        responseList["playGetLiveDynamicChannels"] = getRawString(context, R.raw.response_mock_data_play_widget)
        responseList["rechargeRecommendation"] = getRawString(context, R.raw.response_mock_data_recharge_recommendation)

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
        var temp: String
        val bufferedReader = BufferedReader(InputStreamReader(`in`))
        val stringBuilder = StringBuilder()
        try {
            while (bufferedReader.readLine().also { temp = it } != null) {
                stringBuilder.append("""
    $temp
    
    """.trimIndent())
            }
        } catch (e: IOException) {
        }
        return stringBuilder.toString()
    }
}