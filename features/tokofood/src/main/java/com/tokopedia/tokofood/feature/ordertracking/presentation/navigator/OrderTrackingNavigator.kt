package com.tokopedia.tokofood.feature.ordertracking.presentation.navigator

import androidx.fragment.app.Fragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.tokofood.feature.ordertracking.analytics.TokoFoodPostPurchaseAnalytics
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder.TrackingWrapperUiModel

class OrderTrackingNavigator(
    private val fragment: Fragment,
    private val tracking: TokoFoodPostPurchaseAnalytics
) {

    fun goToHelpPage(orderId: String?, appUrl: String) {
        orderId?.let { tracking.clickCallHelpInStickyButton(it, "") }
        RouteManager.route(fragment.context, appUrl)
    }

    fun goToMerchantPage(trackingWrapperUiModel: TrackingWrapperUiModel, appUrl: String) {
        with(trackingWrapperUiModel) {
            tracking.clickBuyAgainButton(orderId, shopId, foodItems)
        }
        RouteManager.route(fragment.context, appUrl)
    }
}