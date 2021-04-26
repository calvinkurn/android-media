package com.tokopedia.buyerorderdetail.common

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalOrder

object BuyerOrderDetailNavigator {

    private const val KEY_URL = "url"

    fun goToPrintInvoicePage(context: Context, url: String) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalOrder.INVOICE)?.apply {
            putExtra(KEY_URL, url)
        }
        context.startActivity(intent)
    }

    fun goToTrackOrderPage(context: Context, orderId: String) {
        context.startActivity(RouteManager.getIntent(context, ApplinkConstInternalOrder.TRACK, "")
                .putExtra(ApplinkConstInternalOrder.EXTRA_ORDER_ID, orderId)
                .putExtra(ApplinkConstInternalOrder.EXTRA_USER_MODE, 1))
    }
}