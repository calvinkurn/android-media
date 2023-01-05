package com.tokopedia.tokomember_seller_dashboard.callbacks

interface TmCouponDetailCallback {
    fun openCouponDetailFragment(voucherId:Int, tmCouponListRefreshCallback: TmCouponListRefreshCallback)
}
