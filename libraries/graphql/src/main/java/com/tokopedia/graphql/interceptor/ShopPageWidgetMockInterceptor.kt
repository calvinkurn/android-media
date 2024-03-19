package com.tokopedia.graphql.interceptor

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.Keep
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import java.io.IOException

@Keep
class ShopPageWidgetMockInterceptor(val applicationContext: Context) : Interceptor {
    companion object {
        private const val SHARED_PREF_SHOP_PAGE_MOCK_WIDGET = "SHARED_PREF_SHOP_PAGE_MOCK_WIDGET"
        private const val SHARED_PREF_MOCK_WIDGET_DATA = "SHARED_PREF_MOCK_WIDGET_DATA"
    }

    private val sharedPref: SharedPreferences = applicationContext.getSharedPreferences(
        SHARED_PREF_SHOP_PAGE_MOCK_WIDGET,
        Context.MODE_PRIVATE
    )

    private val gson by lazy {
        Gson()
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            //first pair is dynamicTab widget mock response
            //second pair is shopPageGetLayout widget mock response
            val stringListOfPairMockWidgetData = sharedPref.getString(SHARED_PREF_MOCK_WIDGET_DATA, null)
            val requestBody = chain.request()
            val buffer = Buffer()
            requestBody.body?.writeTo(buffer)
            val requestString = buffer.readUtf8()
            if (requestString.isNotEmpty()) {
                val networkResponse = chain.proceed(chain.request())
                return if (null != stringListOfPairMockWidgetData) {
                    val listOfPairMockWidgetData = getListOfPairMockWidgetData(stringListOfPairMockWidgetData)
                    val mockResponseDynamicTabWidget = listOfPairMockWidgetData.map {
                        it.first
                    }.toString()
                    val mockResponseShopLayoutV2Widget = listOfPairMockWidgetData.map {
                        it.second
                    }.toString()
                    val networkResponseBody = networkResponse.body?.string().orEmpty()
                    val networkRequest = requestBody.newBuilder().build()
                    if (requestString.contains("shopPageGetDynamicTab")) {
                        mockResponseDynamicTabWidget(networkRequest, networkResponseBody, mockResponseDynamicTabWidget)
                    } else if (requestString.contains("get_shop_page_home_layout_v2")) {
                        mockResponseShopLayoutV2(networkRequest, networkResponseBody, mockResponseShopLayoutV2Widget)
                    } else {
                        networkResponse
                    }
                } else {
                    networkResponse
                }
            }
        } catch (e: IOException) {
            return chain.proceed(chain.request())
        }
        return chain.proceed(chain.request())
    }

    private fun getListOfPairMockWidgetData(stringListOfPairMockWidgetData: String): List<Pair<String, String>> {
        val type = object : TypeToken<List<Pair<String, String>>>() {}.type
        return (gson.fromJson(stringListOfPairMockWidgetData, type) as List<Pair<String, String>>)
    }

    private fun mockResponseDynamicTabWidget(request: Request, responseString: String, mockWidget: String): Response {
        val networkDynamicTabResponse = JsonParser.parseString(responseString).asJsonArray
        val mockResponseDynamicTabWidget = JsonParser.parseString(mockWidget).asJsonArray
        val mockDynamicTabResponse = networkDynamicTabResponse.get(0).asJsonObject.apply {
            get("data").asJsonObject
            .get("shopPageGetDynamicTab").asJsonObject
            .get("tabData").asJsonArray.first { it.asJsonObject.get("name").asString == "HomeTab" }.asJsonObject
            .get("data").asJsonObject
            .get("homeLayoutData").asJsonObject.apply {
                add("widgetIDList", mockResponseDynamicTabWidget)
            }
        }.toString()

        return Response.Builder()
            .request(request)
            .code(200)
            .protocol(Protocol.HTTP_2)
            .message(mockDynamicTabResponse)
            .body(
                mockDynamicTabResponse.toByteArray()
                    .toResponseBody("application/json".toMediaTypeOrNull())
            )
            .addHeader("content-type", "application/json")
            .build()
    }

    private fun mockResponseShopLayoutV2(copy: Request, responseString: String, mockWidget: String): Response {
        val networkShopLayoutV2Response = JsonParser.parseString(responseString).asJsonArray
        val mockResponseShopLayoutV2Widget = JsonParser.parseString(mockWidget).asJsonArray
        val mockShopLayoutV2Response = networkShopLayoutV2Response.get(0).asJsonObject.apply {
            get("data").asJsonObject
            .get("shopPageGetLayoutV2").asJsonObject.apply {
                add("widgets", mockResponseShopLayoutV2Widget)
            }
        }.toString()
        return Response.Builder()
            .request(copy)
            .code(200)
            .protocol(Protocol.HTTP_2)
            .message(mockShopLayoutV2Response)
            .body(
                mockShopLayoutV2Response.toByteArray()
                    .toResponseBody("application/json".toMediaTypeOrNull())
            )
            .addHeader("content-type", "application/json")
            .build()
    }
}
