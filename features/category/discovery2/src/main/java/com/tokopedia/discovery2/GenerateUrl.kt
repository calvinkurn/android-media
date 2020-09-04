package com.tokopedia.discovery2

class GenerateUrl {

    companion object {
        private const val BASE_URL = "https://ace-staging.tokopedia.com"
        private const val PAGE_URL = "$BASE_URL/discopage/discovery/api/page/"
        private const val CLAIM_COUPON_URL = "$BASE_URL/discopage/discovery/api/component/"
        private const val CLAIM_COUPON = "tokopedia://rewards/kupon/detail/"

        private const val COMPONENT_URL = "$BASE_URL/discopage/discovery/api/component/"

        fun getUrl(endPoint: String) = String.format("$PAGE_URL%s", endPoint)
        fun getClaimCouponUrl(endPoint1: String, endPoint2: String) = String.format("$CLAIM_COUPON_URL%s/%s", endPoint1, endPoint2)
        fun getClaimCouponApplink(slug: String) = String.format("$CLAIM_COUPON%s", slug)
        fun getComponentUrl(pageEndPoint: String, componentId: String) = String.format("$COMPONENT_URL%s%s", "${pageEndPoint}/", componentId)
    }


}