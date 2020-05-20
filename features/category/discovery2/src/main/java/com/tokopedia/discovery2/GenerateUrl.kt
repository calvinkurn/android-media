package com.tokopedia.discovery2

class GenerateUrl {

    companion object {
        const val url = "https://ace.tokopedia.com/hoth/discovery/api/page/"
        const val claimCouponUrl = "https://ace.tokopedia.com/hoth/discovery/api/component/"
        const val claimCoupon = "tokopedia://tokopoints/kupon-saya/detail/"
//        const val url = "https://ace-staging.tokopedia.com/hoth/discovery/api/page/"

        const val componentURL = "https://ace.tokopedia.com/hoth/discovery/api/component/"
//        const val componentURL = "https://ace-staging.tokopedia.com/hoth/discovery/api/component/"

        fun getUrl(endPoint: String) = String.format("$url%s", endPoint)
        fun getClaimCouponUrl(endPoint1: String,endPoint2: String) = String.format("$claimCouponUrl%s/%s", endPoint1,endPoint2)
        fun getClaimCoupon(couponCode: String)  = String.format("$claimCoupon%s", couponCode)

        fun getComponentUrl(pageEndPoint: String, componentId: Int) = String.format("$componentURL%s%s","${pageEndPoint}/" , componentId)
    }


}