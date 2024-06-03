package com.tokopedia.graphql.interceptor

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.Keep
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import timber.log.Timber
import java.io.IOException

@Keep
class ShopPageWidgetMockInterceptor(val applicationContext: Context) : Interceptor {
    companion object {
        private const val SHARED_PREF_SHOP_PAGE_MOCK_WIDGET = "SHARED_PREF_SHOP_PAGE_MOCK_WIDGET"
        private const val SHARED_PREF_MOCK_WIDGET_DATA = "SHARED_PREF_MOCK_WIDGET_DATA"
        private const val SHARED_PREF_MOCK_BMSM_WIDGET_DATA = "SHARED_PREF_MOCK_BMSM_WIDGET_DATA"
        private const val SHARED_PREF_MOCK_PLAY_WIDGET_DATA = "SHARED_PREF_MOCK_PLAY_WIDGET_DATA"
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
            // first pair is dynamicTab widget mock response
            // second pair is shopPageGetLayout widget mock response
            val stringListOfPairMockWidgetData = sharedPref.getString(SHARED_PREF_MOCK_WIDGET_DATA, null)
            val stringPairOfBmsmMockWidgetData = sharedPref.getString(SHARED_PREF_MOCK_BMSM_WIDGET_DATA, null)
            val stringMockPlayWidgetData = sharedPref.getString(SHARED_PREF_MOCK_PLAY_WIDGET_DATA, null)

            val requestBody = chain.request()
            val buffer = Buffer()
            requestBody.body?.writeTo(buffer)
            val requestString = buffer.readUtf8()
            if (requestString.isNotEmpty()) {
                return if (null != stringListOfPairMockWidgetData) {
                    val listOfPairMockWidgetData = getListOfPairMockWidgetData(stringListOfPairMockWidgetData)
                    val mockResponseDynamicTabWidget = listOfPairMockWidgetData.map {
                        it.first
                    }.toString()
                    val mockResponseShopLayoutV2Widget = listOfPairMockWidgetData.map {
                        it.second
                    }.toString()

                    val networkRequest = requestBody.newBuilder().build()
                    if (requestString.contains("shopPageGetDynamicTab")) {
                        mockResponseDynamicTabWidget(networkRequest, chain.proceed(chain.request()).body?.string().orEmpty(), mockResponseDynamicTabWidget)
                    } else if (requestString.contains("get_shop_page_home_layout_v2")) {
                        mockResponseShopLayoutV2(networkRequest, chain.proceed(chain.request()).body?.string().orEmpty(), mockResponseShopLayoutV2Widget)
                    } else if (null != stringPairOfBmsmMockWidgetData) {
                        val mockResponseBmsmWidgetData = getBmsmPairOfMockWidgetData(stringPairOfBmsmMockWidgetData)
                        val mockResponseOfferingInfo = mockResponseBmsmWidgetData.first
                        val mockResponseOfferingProduct = mockResponseBmsmWidgetData.second
                        return if (requestString.contains("getOfferingInfoForBuyer")) {
                            mockResponseBmsmOfferingInfo(networkRequest, mockResponseOfferingInfo)
                        } else if (requestString.contains("getOfferingProductList")) {
                            mockResponseBmsmOfferingProduct(networkRequest, mockResponseOfferingProduct)
                        } else {
                            chain.proceed(chain.request())
                        }
                    } else if (null != stringMockPlayWidgetData) {
                        return if (requestString.contains("playGetWidgetV2")) {
                            mockResponseGetPlayWidgetV2(networkRequest, stringMockPlayWidgetData)
                        } else {
                            chain.proceed(chain.request())
                        }
                    } else {
                        chain.proceed(chain.request())
                    }
                } else {
                    chain.proceed(chain.request())
                }
            }
        } catch (e: IOException) {
            Timber.e(e)
            return chain.proceed(chain.request())
        }
        return chain.proceed(chain.request())
    }

    private fun getListOfPairMockWidgetData(stringListOfPairMockWidgetData: String): List<Pair<String, String>> {
        val type = object : TypeToken<List<Pair<String, String>>>() {}.type
        return (gson.fromJson(stringListOfPairMockWidgetData, type) as List<Pair<String, String>>)
    }

    private fun getBmsmPairOfMockWidgetData(stringPairOfMockData: String): Pair<String, String> {
        val type = object : TypeToken<Pair<String, String>>() {}.type
        return (gson.fromJson(stringPairOfMockData, type)) as Pair<String, String>
    }

    private fun mockResponseDynamicTabWidget(request: Request, responseString: String, mockWidget: String): Response {
        val networkDynamicTabResponse = JsonParser.parseString(responseString).asJsonArray
        val mockResponseDynamicTabWidget = JsonParser.parseString(mockWidget).asJsonArray
        val mockDynamicTabResponse = networkDynamicTabResponse.apply {
            get(0).asJsonObject.get("data").asJsonObject.get("shopPageGetDynamicTab").asJsonObject
                .get("tabData").asJsonArray.first { it.asJsonObject.get("name").asString == "HomeTab" }.asJsonObject
                .get("data").asJsonObject
                .get("homeLayoutData").asJsonObject.add("widgetIDList", mockResponseDynamicTabWidget)
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
        val mockShopLayoutV2Response = networkShopLayoutV2Response.apply {
            get(0).asJsonObject.get("data").asJsonObject.get("shopPageGetLayoutV2").asJsonObject
                .add("widgets", mockResponseShopLayoutV2Widget)
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

    private fun mockResponseBmsmOfferingInfo(copy: Request, mockData: String): Response {
        val mockOfferingInfoJsonObject = JsonParser.parseString(mockData).asJsonObject

        val mockOfferingInfoResponse = JsonArray()
        val data = JsonObject()
        data.add("data", mockOfferingInfoJsonObject)
        mockOfferingInfoResponse.add(data)

        return Response.Builder()
            .request(copy)
            .code(200)
            .protocol(Protocol.HTTP_2)
            .message(mockOfferingInfoResponse.toString())
            .body(
                mockOfferingInfoResponse.toString().toByteArray()
                    .toResponseBody("application/json".toMediaTypeOrNull())
            )
            .addHeader("content-type", "application/json")
            .build()
    }

    private fun mockResponseBmsmOfferingProduct(copy: Request, mockData: String): Response {
        val mockOfferingProductJsonObject = JsonParser.parseString(mockData).asJsonObject

        val mockOfferingProductResponse = JsonArray()
        val data = JsonObject()
        data.add("data", mockOfferingProductJsonObject)
        mockOfferingProductResponse.add(data)

        return Response.Builder()
            .request(copy)
            .code(200)
            .protocol(Protocol.HTTP_2)
            .message(mockOfferingProductResponse.toString())
            .body(
                mockOfferingProductResponse.toString().toByteArray()
                    .toResponseBody("application/json".toMediaTypeOrNull())
            )
            .addHeader("content-type", "application/json")
            .build()
    }

    private fun mockResponseGetPlayWidgetV2(copy: Request, mockData: String): Response {
        val mockGetPlayWidgetV2JsonObject = JsonParser.parseString(mockData).asJsonObject

        val mockGetPlayWidgetV2Response = JsonArray()
        val data = JsonObject()
        data.add("data", mockGetPlayWidgetV2JsonObject)
        mockGetPlayWidgetV2Response.add(data)

        return Response.Builder()
            .request(copy)
            .code(200)
            .protocol(Protocol.HTTP_2)
            .message(mockGetPlayWidgetV2Response.toString())
            .body(
                mockGetPlayWidgetV2Response.toString().toByteArray()
                    .toResponseBody("application/json".toMediaTypeOrNull())
            )
            .addHeader("content-type", "application/json")
            .build()
    }
}
