package com.tokopedia.tokopedianow.home.presentation.view.listener

import com.tokopedia.tokopedianow.home.presentation.viewholder.claimcoupon.HomeClaimCouponWidgetItemViewHolder.HomeCouponWidgetItemListener
import com.tokopedia.tokopedianow.home.presentation.viewmodel.TokoNowHomeViewModel

class ClaimCouponWidgetItemCallback(
    private val viewModel: TokoNowHomeViewModel
): HomeCouponWidgetItemListener {
    override fun onClaimButtonClicked(catalogId: String) {
        viewModel.claimCoupon(catalogId)
    }

    override fun onCouponWidgetClicked() {

    }
}
