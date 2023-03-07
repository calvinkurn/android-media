package com.tokopedia.tokopedianow.home.presentation.view.listener

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics
import com.tokopedia.tokopedianow.home.presentation.viewholder.claimcoupon.HomeClaimCouponWidgetItemViewHolder.HomeClaimCouponWidgetItemTracker
import com.tokopedia.tokopedianow.home.presentation.viewholder.claimcoupon.HomeClaimCouponWidgetItemViewHolder.HomeClaimCouponWidgetItemListener
import com.tokopedia.tokopedianow.home.presentation.viewmodel.TokoNowHomeViewModel

class ClaimCouponWidgetItemCallback(
    private val viewModel: TokoNowHomeViewModel,
    private val context: Context?,
    private val analytics: HomeAnalytics
): HomeClaimCouponWidgetItemListener, HomeClaimCouponWidgetItemTracker {

    override fun onClickClaimButton(
        widgetId: String,
        catalogId: String,
        couponStatus: String,
        position: Int,
        slugText: String,
        couponName: String,
        warehouseId: String
    ) {
        viewModel.claimCoupon(
            widgetId = widgetId,
            catalogId = catalogId,
            couponStatus = couponStatus,
            position = position,
            slugText = slugText,
            couponName = couponName,
            warehouseId = warehouseId
        )
    }

    override fun onClickCouponWidget(appLink: String) {
        RouteManager.route(context, appLink)
    }

    override fun onImpressCouponTracker(
        couponStatus: String,
        position: Int,
        slugText: String,
        couponName: String,
        warehouseId: String,
        isDouble: Boolean
    ) {
        analytics.trackImpressCouponWidget(
            couponStatus = couponStatus,
            position = position,
            slugText = slugText,
            couponName = couponName,
            warehouseId = warehouseId,
            isDouble = isDouble
        )
    }

    override fun onClickClaimButtonTracker(
        couponStatus: String,
        position: Int,
        slugText: String,
        couponName: String,
        warehouseId: String
    ) {
        analytics.trackClickClaimCouponWidget(
            couponStatus = couponStatus,
            position = position,
            slugText = slugText,
            couponName = couponName,
            warehouseId = warehouseId
        )
    }

    override fun onClickCouponWidgetTracker(
        couponStatus: String,
        position: Int,
        slugText: String,
        couponName: String,
        warehouseId: String
    ) {
        analytics.trackClickCouponWidget(
            couponStatus = couponStatus,
            position = position,
            slugText = slugText,
            couponName = couponName,
            warehouseId = warehouseId
        )
    }
}
