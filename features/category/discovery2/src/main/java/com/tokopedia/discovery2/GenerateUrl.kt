package com.tokopedia.discovery2

class GenerateUrl {

    companion object {
        private const val BASE_URL = "https://ace.tokopedia.com"
        private const val PAGE_URL = "$BASE_URL/discopage/discovery/api/page/"
        private const val CLAIM_COUPON_URL = "$BASE_URL/discopage/discovery/api/component/"
        private const val CLAIM_COUPON = "tokopedia://tokopoints/tukar-point/detail/"

        private const val COMPONENT_URL = "$BASE_URL/discopage/discovery/api/component/"

        fun getUrl(endPoint: String) = String.format("$PAGE_URL%s", endPoint)
        fun getClaimCouponUrl(endPoint1: String, endPoint2: String) = String.format("$CLAIM_COUPON_URL%s/%s", endPoint1, endPoint2)
        fun getClaimCoupon(couponCode: String) = String.format("$CLAIM_COUPON%s", couponCode)

        fun getComponentUrl(pageEndPoint: String, componentId: String) = String.format("$COMPONENT_URL%s%s", "${pageEndPoint}/", componentId)
    }


}