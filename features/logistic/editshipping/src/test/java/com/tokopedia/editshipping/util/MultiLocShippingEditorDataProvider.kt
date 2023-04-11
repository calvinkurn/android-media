package com.tokopedia.editshipping.util

import com.google.gson.Gson
import com.tokopedia.logisticCommon.data.response.shippingeditor.GetShipperListResponse
import com.tokopedia.logisticCommon.data.response.shippingeditor.GetShipperTickerResponse
import java.io.IOException
import java.nio.charset.Charset

object MultiLocShippingEditorDataProvider {
    private val gson = Gson()

    fun provideShipperList(): GetShipperListResponse {
        return gson.fromJson(
            getJsonFromAsset("assets/get_shipper_list.json"),
            GetShipperListResponse::class.java
        )
    }

    fun provideShipperTickerState(): GetShipperTickerResponse {
        return gson.fromJson(
            getJsonFromAsset("assets/get_shipper_ticker_state.json"),
            GetShipperTickerResponse::class.java
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
