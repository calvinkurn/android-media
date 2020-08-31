package com.tokopedia.test.application.environment.interceptor

import com.tokopedia.network.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.regex.Matcher
import java.util.regex.Pattern

class TopAdsDetectorInterceptor(private val adder: (Int) -> Unit) : Interceptor {

    companion object {
        const val KEY = "TopAdsInterceptor"
    }
    val topAdsInEachRequest = hashMapOf<String, Int>()

    override fun intercept(chain: Interceptor.Chain): Response {
        if (BuildConfig.DEBUG) {
            try {
                val requestCopy = chain.request()
                val buffer = Buffer()
                requestCopy.body()?.writeTo(buffer)
                val requestString = buffer.readUtf8()

                val requestArray = JSONArray(requestString)
                val requestObject: JSONObject = requestArray.getJSONObject(0)
                val queryString = requestObject.getString("query")

                val queryStringCopy = queryString.removePrefix("query ")
                        .removePrefix("{\n ")
                        .removePrefix(" ")
                val firstWord = queryStringCopy.substringBefore(" ", "")

                val response = chain.proceed(chain.request())
                val jsonDataString = response.peekBody(Long.MAX_VALUE).string()
                findTopAds(firstWord, jsonDataString)
                return response
            } catch (e: IOException) {
                "did not work"
            }
        } else {
            //just to be on safe side.
            throw IllegalAccessError("MockInterceptor is only meant for Testing Purposes and " +
                    "bound to be used only with DEBUG mode")
        }
        return chain.proceed(chain.request())
    }

    private fun findTopAds(key: String, jsonDataString: String?) {
        val `in` = jsonDataString ?: ""
        var i = 0
        val p: Pattern = Pattern.compile("\"isTopads\":true")
        val p2: Pattern = Pattern.compile("\"is_topads\":true")

        val m: Matcher = p.matcher(`in`)
        while (m.find()) {
            i++
        }

        val m2: Matcher = p2.matcher(`in`)
        while (m2.find()) {
            i++
        }
        if (i > 0 && !topAdsInEachRequest.containsKey(key)) {
            topAdsInEachRequest[key] = i
            adder.invoke(i)
        }
    }
}