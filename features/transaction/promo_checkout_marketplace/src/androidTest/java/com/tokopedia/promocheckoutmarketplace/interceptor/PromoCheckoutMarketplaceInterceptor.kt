package com.tokopedia.promocheckoutmarketplace.interceptor

import com.tokopedia.test.application.util.ResourcePathUtil.getJsonFromResource
import okhttp3.*
import okio.Buffer

class PromoCheckoutMarketplaceInterceptor : Interceptor {

    var customCouponListRecommendationResponsePath: String? = null
    var customValidateUsePromoRevampResponsePath: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request().newBuilder().build()
        val requestString = readRequestString(copy)

        if (requestString.contains(COUPON_LIST_RECOMMENDATION_QUERY) && customCouponListRecommendationResponsePath != null) {
            return mockResponse(copy, getJsonFromResource(customCouponListRecommendationResponsePath!!))
        }
        if (requestString.contains(VALIDATE_USE_PROMO_REVAMP_QUERY) && customValidateUsePromoRevampResponsePath != null) {
            return mockResponse(copy, getJsonFromResource(customValidateUsePromoRevampResponsePath!!))
        }
        return chain.proceed(chain.request())
    }

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
}

const val COUPON_LIST_RECOMMENDATION_QUERY = "coupon_list_recommendation"
const val VALIDATE_USE_PROMO_REVAMP_QUERY = "validate_use_promo_revamp"