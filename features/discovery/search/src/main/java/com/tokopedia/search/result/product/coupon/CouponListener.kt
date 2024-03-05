package com.tokopedia.search.result.product.coupon

import com.tokopedia.search.result.domain.model.SearchCouponModel
import com.tokopedia.search.result.presentation.model.CouponDataView

interface CouponListener {
    fun onCouponImpressed(couponDataView: CouponDataView)
    fun claimCoupon(couponDataView: CouponDataView, widget: SearchCouponModel.CouponListWidget)
}
