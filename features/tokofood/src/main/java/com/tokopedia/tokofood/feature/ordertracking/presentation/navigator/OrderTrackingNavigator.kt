package com.tokopedia.tokofood.feature.ordertracking.presentation.navigator

import androidx.fragment.app.Fragment
import com.tokopedia.applink.RouteManager

class OrderTrackingNavigator(
    private val fragment: Fragment
) {

    fun goToHelpPage(appUrl: String) {
        RouteManager.route(fragment.context, appUrl)
    }

    fun goToMerchantPage(appUrl: String) {
        RouteManager.route(fragment.context, appUrl)
    }
}