package com.tokopedia.search.result.product.coupon

import com.tokopedia.search.result.domain.model.SearchCouponModel
import com.tokopedia.search.result.presentation.model.CouponDataView
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselPresenter
import com.tokopedia.search.utils.FragmentProvider

internal class CouponListenerDelegate(
    private val presenter: InspirationCarouselPresenter?,
    searchParameterProvider: SearchParameterProvider,
    fragmentProvider: FragmentProvider,
): CouponListener,
    SearchParameterProvider by searchParameterProvider,
    FragmentProvider by fragmentProvider {
    override fun onCouponImpressed(couponDataView: CouponDataView) {
        presenter?.onCouponImpressed(couponDataView)
    }

    override fun claimCoupon(
        couponDataView: CouponDataView,
        widget: SearchCouponModel.CouponListWidget
    ) {
        presenter?.ctaCoupon(couponDataView, widget)
    }
}
