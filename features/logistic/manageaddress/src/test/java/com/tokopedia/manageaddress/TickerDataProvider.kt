package com.tokopedia.manageaddress

import com.google.gson.Gson
import com.tokopedia.logisticCommon.domain.response.GetTargetedTickerResponse
import java.io.IOException
import java.nio.charset.Charset

object TickerDataProvider {
    private val gson = Gson()

    fun provideDummy(): GetTargetedTickerResponse {
        return gson.fromJson(
            getJsonFromAsset("get_targeted_ticker_response.json"),
            GetTargetedTickerResponse::class.java
        )
    }

    private fun getJsonFromAsset(path: String?): String {
        var json = ""
        javaClass.classLoader?.getResourceAsStream(path)?.let {
            try {
                val size = it.available()
                val buffer = ByteArray(size)
                it.read(buffer)
                it.close()
                json = String(buffer, Charset.forName("UTF-8"))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return json
    }
}
