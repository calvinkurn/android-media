package com.tokopedia.logisticaddaddress.helper

import com.google.gson.Gson
import com.tokopedia.logisticCommon.data.entity.response.AutoFillResponse
import java.io.IOException
import java.nio.charset.Charset

object KeroMapsAutofillProvider {
    fun provideAutofillResponse(): AutoFillResponse {
        return Gson().fromJson(
            getJsonFromAsset("kero_maps_autofill_response.json"),
            AutoFillResponse::class.java
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
