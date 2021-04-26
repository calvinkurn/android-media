package com.tokopedia.buyerorderdetail.common

import android.content.Context
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
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

    fun goToShopPage(context: Context, shopId: String) {
        RouteManager.route(context, ApplinkConstInternalMarketplace.SHOP_PAGE, shopId)
    }

    fun goToProductSnapshotPage(context: Context, orderId: String, orderDetailId: String) {
        val appLinkSnapShot = "${ApplinkConst.SNAPSHOT_ORDER}/$orderId/$orderDetailId"
        val intent = RouteManager.getIntent(context, appLinkSnapShot)
        intent.putExtra(ApplinkConstInternalOrder.IS_SNAPSHOT_FROM_SOM, true)
        context.startActivity(intent)
    }
}