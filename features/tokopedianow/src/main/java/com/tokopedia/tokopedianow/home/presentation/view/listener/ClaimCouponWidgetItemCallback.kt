package com.tokopedia.tokopedianow.home.presentation.view.listener

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.tokopedianow.home.presentation.viewholder.claimcoupon.HomeClaimCouponWidgetItemViewHolder.HomeClaimCouponWidgetItemListener
import com.tokopedia.tokopedianow.home.presentation.viewmodel.TokoNowHomeViewModel

class ClaimCouponWidgetItemCallback(
    private val viewModel: TokoNowHomeViewModel,
    private val context: Context?
): HomeClaimCouponWidgetItemListener {
    override fun onClaimButtonClicked(catalogId: String) {
        viewModel.claimCoupon(catalogId)
    }

    override fun onCouponWidgetClicked(appLink: String) {
        RouteManager.route(context, appLink)
    }
}
