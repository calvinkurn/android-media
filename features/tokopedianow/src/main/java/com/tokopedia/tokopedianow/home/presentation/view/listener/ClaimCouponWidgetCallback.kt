package com.tokopedia.tokopedianow.home.presentation.view.listener

import com.tokopedia.tokopedianow.home.presentation.viewholder.claimcoupon.HomeClaimCouponWidgetViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewmodel.TokoNowHomeViewModel

class ClaimCouponWidgetCallback(
    private val viewModel: TokoNowHomeViewModel
): HomeClaimCouponWidgetViewHolder.HomeClaimCouponWidgetListener {
    override fun onClickRefreshButton(slugs: List<String>) {
        viewModel.getCatalogCouponList(slugs)
    }
}
