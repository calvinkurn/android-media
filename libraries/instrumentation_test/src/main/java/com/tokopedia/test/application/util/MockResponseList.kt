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
        responseList.put("dynamicHomeChannel", getRawString(context, R.raw.response_mock_data_dynamic_home_channel))
        responseList.put("widget_tab", getRawString(context, R.raw.response_mock_data_home_widget_tab))
        responseList.put("widget_grid", getRawString(context, R.raw.response_mock_data_home_widget_grid))
        responseList.put("suggestedProductReview", getRawString(context, R.raw.response_mock_data_suggested_review))
        responseList.put("playGetLiveDynamicChannels", getRawString(context, R.raw.response_mock_data_play_widget))
        responseList.put("rechargeRecommendation", getRawString(context, R.raw.response_mock_data_recharge_recommendation))

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