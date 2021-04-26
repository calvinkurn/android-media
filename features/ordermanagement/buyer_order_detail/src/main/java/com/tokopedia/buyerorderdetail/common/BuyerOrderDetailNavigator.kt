package com.tokopedia.buyerorderdetail.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalOrder

object BuyerOrderDetailNavigator {

    private const val KEY_URL = "url"
    private const val TELEPHONY_URI = "tel:"

    fun goToPrintInvoicePage(context: Context, url: String) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalOrder.INVOICE)?.apply {
            putExtra(KEY_URL, url)
        }
        context.startActivity(intent)
    }

    fun goToTrackOrderPage(context: Context, orderId: String) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalOrder.TRACK, "")
                .putExtra(ApplinkConstInternalOrder.EXTRA_ORDER_ID, orderId)
                .putExtra(ApplinkConstInternalOrder.EXTRA_USER_MODE, 1)
        context.startActivity(intent)
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

    fun goToCallingPage(context: Context, phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = composeCallIntentData(phoneNumber)
        }
        context.startActivity(intent)
    }

    private fun composeCallIntentData(phoneNumber: String): Uri {
        return Uri.parse("$TELEPHONY_URI$phoneNumber")
    }
}