package com.tokopedia.core.network.retrofit.coverters

import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class GeneratedHostConverter : Converter.Factory() {

    private val MEDIA_TYPE = MediaType.parse("text/plain")

    override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
        return if (GeneratedHost::class.java == type) {
            Converter<ResponseBody, Any> { value ->
                val body = value.string()
                try {
                    val jsonObject = JSONObject(body)
                    return@Converter if (jsonObject.isNull("data") || jsonObject.getJSONObject("data").isNull("generated_host")) {
                        Gson().fromJson(jsonObject.toString(), GeneratedHost::class.java)
                    } else {
                        Gson().fromJson<Any>(jsonObject
                                .getJSONObject("data")
                                .getJSONObject("generated_host")
                                .toString(), GeneratedHost::class.java)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    return@Converter null
                }
            }
        } else null
    }

    override fun requestBodyConverter(type: Type?, parameterAnnotations: Array<Annotation>?, methodAnnotations: Array<Annotation>?, retrofit: Retrofit?): Converter<*, RequestBody>? {
        return if (String::class.java == type) {
            Converter<String, RequestBody> { value -> RequestBody.create(MEDIA_TYPE, value) }
        } else null
    }
}
