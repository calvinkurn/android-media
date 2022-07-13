package com.tokopedia.tokomember_seller_dashboard.callbacks

interface TmCouponListRefreshCallback {
    fun refreshCouponList(editCoupon: Boolean = false)
}