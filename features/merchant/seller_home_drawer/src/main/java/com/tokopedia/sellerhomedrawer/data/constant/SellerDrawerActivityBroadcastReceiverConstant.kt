package com.tokopedia.sellerhomedrawer.data.constant

class SellerDrawerActivityBroadcastReceiverConstant {

    companion object {
        val INTENT_ACTION_MAIN_APP = "com.tokopedia.core.app.DrawerPresenterActivity.DrawerActivityBroadcastReceiver.ACTION_MAIN_APP"
        val EXTRA_TOKOPOINT_DRAWER_DATA = "EXTRA_TOKOPOINT_DRAWER_DATA"
        val EXTRA_TOKOCASH_DRAWER_DATA = "EXTRA_TOKOCASH_DRAWER_DATA"
        val EXTRA_ACTION_RECEIVER = "EXTRA_ACTION_RECEIVER"

        val ACTION_RECEIVER_RECEIVED_TOKOPOINT_DATA = 1
        val ACTION_RECEIVER_RECEIVED_TOKOCASH_DATA = 5
        val ACTION_RECEIVER_RECEIVED_TOKOCASH_PENDING_DATA = 6
        val ACTION_RECEIVER_GET_TOKOCASH_DATA = 2
        val ACTION_RECEIVER_GET_TOKOPOINT_DATA = 3
        val ACTION_RECEIVER_GET_TOKOCASH_PENDING_DATA = 4
    }

}
