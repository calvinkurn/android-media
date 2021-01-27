package com.tokopedia.promocheckoutmarketplace

import com.tokopedia.test.application.util.ResourcePathUtil.getJsonFromResource
import okhttp3.*
import okio.Buffer

class PromoOccInterceptor : Interceptor {

    var customCouponListRecommendationResponsePath: String? = null

    private fun readRequestString(copyRequest: Request): String {
        val buffer = Buffer()
        copyRequest.body()?.writeTo(buffer)
        return buffer.readUtf8()
    }

    private fun mockResponse(copy: Request, responseString: String): Response {
        return Response.Builder()
                .request(copy)
                .code(200)
                .protocol(Protocol.HTTP_2)
                .message(responseString)
                .body(ResponseBody.create(MediaType.parse("application/json"),
                        responseString.toByteArray()))
                .addHeader("content-type", "application/json")
                .build()
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request().newBuilder().build()
        val requestString = readRequestString(copy)

        if (requestString.contains(COUPON_LIST_RECOMMENDATION_QUERY)) {
            if (customCouponListRecommendationResponsePath != null) {
                return mockResponse(copy, getJsonFromResource(customCouponListRecommendationResponsePath!!))
            }
        }
        return chain.proceed(chain.request())
    }
}

const val COUPON_LIST_RECOMMENDATION_QUERY = "coupon_list_recommendation"