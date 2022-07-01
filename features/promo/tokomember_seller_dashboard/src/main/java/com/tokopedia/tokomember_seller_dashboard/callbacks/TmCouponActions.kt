package com.tokopedia.tokomember_seller_dashboard.callbacks

interface TmCouponActions {
    fun option(type: String, voucherId: String, couponType: String, currentQuota: Int, maxCashback: Int = 1)
}