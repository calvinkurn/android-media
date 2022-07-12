package com.tokopedia.tokofood.feature.ordertracking.presentation.navigator

import androidx.fragment.app.Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.tokofood.common.util.TokofoodRouteManager
import com.tokopedia.tokofood.feature.ordertracking.analytics.TokoFoodPostPurchaseAnalytics
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder.TrackingWrapperUiModel

class OrderTrackingNavigator(
    private val fragment: Fragment,
    private val tracking: TokoFoodPostPurchaseAnalytics
) {

    fun goToHelpPage(orderId: String?, appUrl: String, merchantId: String) {
        orderId?.let { tracking.clickCallHelpInStickyButton(it, merchantId) }
        RouteManager.route(fragment.context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, appUrl))
    }

    fun goToMerchantPage(trackingWrapperUiModel: TrackingWrapperUiModel, appUrl: String) {
        with(trackingWrapperUiModel) {
            tracking.clickBuyAgainButton(orderId, merchantData, foodItems)
        }
        TokofoodRouteManager.routePrioritizeInternal(fragment.context, appUrl)
    }

    fun goToPrintInvoicePage(url: String, invoiceNum: String) {
        val intent = RouteManager.getIntent(fragment.context, ApplinkConstInternalOrder.INVOICE)?.apply {
            putExtra(KEY_URL, url)
            putExtra(INVOICE_REF_NUM, invoiceNum)
        }
        fragment.startActivity(intent)
    }

    companion object {
        private const val KEY_URL = "url"
        private const val INVOICE_REF_NUM = "invoice_ref_num"
    }
}